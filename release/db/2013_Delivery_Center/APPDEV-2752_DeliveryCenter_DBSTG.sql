/*Table to capture order delivery metric*/
CREATE TABLE MIS.ORDER_DELIVERY_METRIC
(
  INSERT_TIMESTAMP    DATE                      NOT NULL,
  WEBORDER_ID         VARCHAR2(16 BYTE)         NOT NULL,
  ROUTE_NO            VARCHAR2(16 BYTE)         NOT NULL,
  ROUTING_ROUTE_NO    VARCHAR2(10 BYTE)         NOT NULL,
  STOP_SEQUENCE       NUMBER(9)                 NOT NULL,
  DLV_ATTEMPTED_FLAG  VARCHAR2(1 BYTE),
  LATEISSUE_FLAG      VARCHAR2(1 BYTE),
  EVENTLOG_FLAG       VARCHAR2(1 BYTE),
  ORDER_MAXSCANTIME   DATE,
  ROUTE_MAXSCANTIME   DATE,
  EMBARK_NEXTTIME     DATE,
  WORK_TIME           NUMBER,
  MIN_UNTIL_DELIVERY  NUMBER,
  ESTIMATED_DLV_TIME  DATE
);

ALTER TABLE MIS.ORDER_DELIVERY_METRIC ADD ( PRIMARY KEY (WEBORDER_ID));


/*Procedure to capture delivery metric data*/

CREATE OR REPLACE PROCEDURE MIS.ORDER_DELIVERY_METRIC_CAPTURE
IS  
   BEGIN
   DECLARE  
   current_date            DATE := SYSDATE;
   
   CURSOR c_deliverymetric
   IS
       select bs.weborder_id,
             bs.route_no, 
             bs.ROUTING_ROUTE_NO, 
             bs.stop_sequence, 
             DECODE(x.ATTEMPTED_TIME, NULL, 'F','T') AS DLV_ATTEMPTED_FLAG,
             DECODE(lo.STOP_NUMBER, NULL, 'F','T') AS LATEISSUE_FLAG, 
             DECODE(z.ROUTE_NO, NULL, 'F','T') AS EVENTLOG_FLAG,          
             x.ORDER_MAX_TIME AS ORDER_MAXSCANTIME, 
             DECODE(y.ROUTE_MAXSCANTIME, null, bs.window_starttime, y.ROUTE_MAXSCANTIME) ROUTE_MAXSCANTIME,             
             CASE WHEN X.ATTEMPTED_TIME IS NULL AND z.ROUTE_NO IS NULL
             THEN
                   (sysdate - (bs.servicetime+bs.traveltime) / 86400)
             ELSE
                    NULL
             END AS EMBARK_NEXTTIME,
             ROUND((bs.servicetime + bs.traveltime) / 60) work_time, 
             CASE WHEN X.ATTEMPTED_TIME IS NULL AND z.ROUTE_NO IS NULL
             THEN 
                 SUM(ROUND((BS.SERVICETIME + BS.TRAVELTIME) / 60)) OVER (PARTITION BY BS.ROUTING_ROUTE_NO, DECODE(X.ATTEMPTED_TIME, NULL, 'F','T'), DECODE(z.ROUTE_NO, NULL, 'F','T')  ORDER BY BS.STOP_SEQUENCE)   
             ELSE 0  
             END AS MIN_UNTIL_DELIVERY, 
             CASE WHEN X.ATTEMPTED_TIME IS NULL AND z.ROUTE_NO IS NULL
             THEN 
                 GREATEST(
                    DECODE(y.ROUTE_MAXSCANTIME, null, bs.window_starttime, y.ROUTE_MAXSCANTIME)
                           + SUM((BS.SERVICETIME + BS.TRAVELTIME) / 86400) OVER (PARTITION BY BS.ROUTING_ROUTE_NO, DECODE(X.ATTEMPTED_TIME, NULL, 'F','T'), DECODE(z.ROUTE_NO, NULL, 'F','T')  ORDER BY BS.STOP_SEQUENCE) 
                    , 
                    CASE WHEN X.ATTEMPTED_TIME IS NULL AND z.ROUTE_NO IS NULL
                    THEN
                            (sysdate - (bs.servicetime+bs.traveltime) / 86400)
                    ELSE 
                            NULL 
                     END
                         + SUM((BS.SERVICETIME + BS.TRAVELTIME) / 86400) OVER (PARTITION BY BS.ROUTING_ROUTE_NO, DECODE(X.ATTEMPTED_TIME, NULL, 'F','T'), DECODE(z.ROUTE_NO, NULL, 'F','T')  ORDER BY BS.STOP_SEQUENCE) 
                 ) 
             ELSE 
                NULL 
             END as ESTIMATED_DLV_TIME 
             from transp.handoff_batch b, transp.handoff_batchstop bs, cust.lateissue_orders lo,
             ( 
                  select distinct webordernum, routing_route_no, 
                  MAX(CASE WHEN CT.CARTONSTATUS IN  ('DELIVERED','REFUSED') THEN SCANDATE ELSE NULL END) OVER (PARTITION BY WEBORDERNUM) ATTEMPTED_TIME, 
                  MAX(SCANDATE) OVER (PARTITION BY WEBORDERNUM) ORDER_MAX_TIME
                  from transp.handoff_batch bx, transp.handoff_batchstop bsx, dlv.cartontracking ct 
                  where bx.batch_id = bsx.batch_id and bx.delivery_date = trunc(sysdate)  and bx.batch_status in ('CPD','CPD/ADC','CPD/ADF') 
                  and bsx.weborder_id = ct.webordernum(+) 
                  and ct.cartonstatus in ('DELIVERED','REFUSED','IN_TRANSIT')
              ) X, 
              ( 
                  select bsx.ROUTING_ROUTE_NO, max(ct.scandate) AS  ROUTE_MAXSCANTIME 
                  from transp.handoff_batch bx, transp.handoff_batchstop bsx, dlv.cartontracking ct  
                  where bx.batch_id = bsx.batch_id and bx.delivery_date =trunc(sysdate)  and bx.batch_status in ('CPD','CPD/ADC','CPD/ADF')  
                  and bsx.weborder_id=ct.webordernum(+) 
                  and ct.cartonstatus in ('DELIVERED','REFUSED','IN_TRANSIT') 
                  GROUP BY BSX.ROUTING_ROUTE_NO 
              ) Y,
              ( 
                  select bsx.ROUTE_NO, bsx.stop_sequence
                      from transp.handoff_batch bx, transp.handoff_batchstop bsx, transp.eventlog_book el, transp.eventlog_stop els
                      where bx.batch_id = bsx.batch_id and bx.delivery_date = trunc(sysdate)  and bx.batch_status in ('CPD','CPD/ADC','CPD/ADF')  
                      and el.EVENT_ID = els.EVENTLOG_ID and  bx.delivery_date = el.event_date
                      and el.ROUTE = bsx.route_no                  
                      and els.STOP_NUMBER = bsx.stop_sequence
                      GROUP BY BSX.ROUTE_NO, bsx.stop_sequence
                  UNION
                        select bsx.ROUTE_NO, bsx.stop_sequence
                      from transp.handoff_batch bx, transp.handoff_batchstop bsx, transp.moteventlog_book el, transp.moteventlog_stop els
                      where bx.batch_id = bsx.batch_id and bx.delivery_date = trunc(sysdate)  and bx.batch_status in ('CPD','CPD/ADC','CPD/ADF')  
                      and el.EVENT_ID = els.EVENTLOG_ID and  bx.delivery_date = el.event_date
                      and el.ROUTE = bsx.route_no                  
                      and els.STOP_NUMBER = bsx.stop_sequence
                      GROUP BY BSX.ROUTE_NO, bsx.stop_sequence
              ) Z                                
             where b.batch_id = bs.batch_id
             and b.delivery_date = trunc(sysdate) and b.batch_status in ('CPD','CPD/ADC','CPD/ADF') 
             and x.webordernum(+) = bs.weborder_id             
             and lo.stop_number(+) = bs.weborder_id
             and y.ROUTING_ROUTE_NO(+) = bs.ROUTING_ROUTE_NO 
             and z.ROUTE_NO(+) = bs.ROUTE_NO 
             and z.stop_sequence(+) = bs.stop_sequence           
             ORDER BY bs.route_no, bs.ROUTING_ROUTE_NO, bs.stop_sequence;
          
      deliverymetric_data_rec   c_deliverymetric%ROWTYPE;     
BEGIN
   OPEN c_deliverymetric;

   LOOP
      FETCH c_deliverymetric INTO deliverymetric_data_rec;
      EXIT WHEN c_deliverymetric%NOTFOUND;      
      
      dbms_output.put_line(deliverymetric_data_rec.WEBORDER_ID);
      DELETE FROM MIS.ORDER_DELIVERY_METRIC WHERE WEBORDER_ID=deliverymetric_data_rec.WEBORDER_ID;
      
      INSERT INTO MIS.ORDER_DELIVERY_METRIC (INSERT_TIMESTAMP,
                          WEBORDER_ID,
                          ROUTE_NO,
                          ROUTING_ROUTE_NO,
                          STOP_SEQUENCE,
                          DLV_ATTEMPTED_FLAG,
                          LATEISSUE_FLAG,
                          EVENTLOG_FLAG,
                          ORDER_MAXSCANTIME,
                          ROUTE_MAXSCANTIME,
                          EMBARK_NEXTTIME,
                          WORK_TIME,
                          MIN_UNTIL_DELIVERY,
                          ESTIMATED_DLV_TIME
                          )
                           VALUES
                          (                         
                                current_date,
                                deliverymetric_data_rec.WEBORDER_ID,
                                deliverymetric_data_rec.ROUTE_NO,
                                deliverymetric_data_rec.ROUTING_ROUTE_NO,
                                deliverymetric_data_rec.STOP_SEQUENCE,
                                deliverymetric_data_rec.DLV_ATTEMPTED_FLAG,
                                deliverymetric_data_rec.LATEISSUE_FLAG,
                                deliverymetric_data_rec.EVENTLOG_FLAG,
                                deliverymetric_data_rec.ORDER_MAXSCANTIME,
                                deliverymetric_data_rec.ROUTE_MAXSCANTIME,
                                deliverymetric_data_rec.EMBARK_NEXTTIME,
                                deliverymetric_data_rec.WORK_TIME,
                                deliverymetric_data_rec.MIN_UNTIL_DELIVERY,
                                deliverymetric_data_rec.ESTIMATED_DLV_TIME                           
                           );
   END LOOP;   
   CLOSE c_deliverymetric;
   dbms_output.put_line('Procedure completed Successfully');     
   COMMIT;
 Exception
        when others then
       dbms_output.put_line('Error: '||sqlerrm);    
       ROLLBACK;
END;
END;



/*Event Log Scripts*/

CREATE TABLE TRANSP.EVENTLOG_BOOK
(
  EVENT_ID          VARCHAR2(16 BYTE)           NOT NULL,
  EVENT_DATE        DATE                        NOT NULL,
  ROUTE             VARCHAR2(10 BYTE)           NOT NULL,
  TRUCK             VARCHAR2(12 BYTE),
  WINDOW_STARTTIME  DATE,
  WINDOW_ENDTIME    DATE,
  EVENT_TYPE        VARCHAR2(40 BYTE)           NOT NULL,
  EVENT_SUBTYPE     VARCHAR2(40 BYTE)           NOT NULL,
  DESCRIPTION       VARCHAR2(250 BYTE),
  CROSSSTREET       VARCHAR2(40 BYTE),
  EMPLOYEE_ID       VARCHAR2(30 BYTE),
  SCANNER_NUMBER    VARCHAR2(30 BYTE),
  CROMOD_DATE       DATE,
  CREATED_BY        VARCHAR2(30 BYTE),
  EVENT_REFID       VARCHAR2(16 BYTE)
);

CREATE INDEX TRANSP.IDX_EL_BOOK_DATE ON TRANSP.EVENTLOG_BOOK (EVENT_DATE);

ALTER TABLE TRANSP.EVENTLOG_BOOK ADD ( CONSTRAINT PK_EVENTLOG_BOOK PRIMARY KEY (EVENT_ID));

CREATE TABLE TRANSP.EVENTLOG_MESSAGEGROUP
(
  GROUP_NAME   VARCHAR2(45 BYTE)                NOT NULL,
  EMAIL        VARCHAR2(45 BYTE),
  CREATEDBY    VARCHAR2(45 BYTE),
  CROMOD_DATE  DATE
);

ALTER TABLE TRANSP.EVENTLOG_MESSAGEGROUP ADD (PRIMARY KEY (GROUP_NAME));

CREATE TABLE TRANSP.EVENTLOG_STOP
(
  STOP_NUMBER  NUMBER(6),
  EVENTLOG_ID  VARCHAR2(40 BYTE)
);

ALTER TABLE TRANSP.EVENTLOG_STOP ADD ( CONSTRAINT FK_EVENTLOG_ID FOREIGN KEY (EVENTLOG_ID)  REFERENCES TRANSP.EVENTLOG_BOOK (EVENT_ID));

CREATE TABLE TRANSP.EVENTLOG_TYPE
(
  EVENTTYPE_NAME         VARCHAR2(40 BYTE)      NOT NULL,
  EVENTTYPE_DESCRIPTION  VARCHAR2(45 BYTE),
  CREATEDBY              VARCHAR2(30 BYTE),
  CROMOD_DATE            DATE,
  EMPLOYEE_REQ           VARCHAR2(1 BYTE),
  CUSTOMER_REQ           VARCHAR2(1 BYTE)
);

ALTER TABLE TRANSP.EVENTLOG_TYPE ADD ( PRIMARY KEY (EVENTTYPE_NAME));

CREATE TABLE TRANSP.EVENTLOG_SUBTYPE
(
  EVENTSUBTYPE_NAME  VARCHAR2(45 BYTE)          NOT NULL,
  DESCRIPTION        VARCHAR2(45 BYTE),
  EVENTTYPE_ID       VARCHAR2(45 BYTE)          NOT NULL,
  CREATEDBY          VARCHAR2(30 BYTE),
  CROMOD_DATE        DATE,
  MSGGROUP_ID        VARCHAR2(45 BYTE)
);

CREATE INDEX TRANSP.FK_EVENTSUBTYPE_EVENTTYPEID ON TRANSP.EVENTLOG_SUBTYPE(EVENTTYPE_ID);

ALTER TABLE TRANSP.EVENTLOG_SUBTYPE ADD (UNIQUE (EVENTSUBTYPE_NAME, EVENTTYPE_ID));

ALTER TABLE TRANSP.EVENTLOG_SUBTYPE ADD ( CONSTRAINT FK_EVENTSUBTYPE_EVENTTYPEID  FOREIGN KEY (EVENTTYPE_ID)  REFERENCES TRANSP.EVENTLOG_TYPE (EVENTTYPE_NAME)
    ON DELETE CASCADE,
  CONSTRAINT FK_EVENTSUBTYPE_MSGGROUPID  FOREIGN KEY (MSGGROUP_ID)  REFERENCES TRANSP.EVENTLOG_MESSAGEGROUP (GROUP_NAME) ON DELETE CASCADE);

/*Mot EventLog scripts*/

CREATE TABLE TRANSP.MOTEVENTLOG_BOOK
(
  EVENT_ID       VARCHAR2(16 BYTE)              NOT NULL,
  EVENT_DATE     DATE                           NOT NULL,
  ROUTE          VARCHAR2(10 BYTE)              NOT NULL,
  MOTTRUCK       VARCHAR2(30 BYTE),
  NEXTEL         VARCHAR2(12 BYTE),
  EVENT_TYPE     VARCHAR2(40 BYTE)              NOT NULL,
  DESCRIPTION    VARCHAR2(250 BYTE),
  TICKET_NUMBER  VARCHAR2(40 BYTE),
  DATE_VERIFIED  DATE,
  VERIFIED_BY    VARCHAR2(30 BYTE),
  CROMOD_DATE    DATE,
  CREATED_BY     VARCHAR2(30 BYTE)
);

CREATE INDEX TRANSP.IDX_MOTEL_BOOK_DATE ON TRANSP.MOTEVENTLOG_BOOK (EVENT_DATE);

ALTER TABLE TRANSP.MOTEVENTLOG_BOOK ADD ( CONSTRAINT PK_MOTEVENTLOG_BOOK PRIMARY KEY (EVENT_ID));

CREATE TABLE TRANSP.MOTEVENTLOG_MESSAGEGROUP
(
  GROUP_NAME   VARCHAR2(45 BYTE)                NOT NULL,
  EMAIL        VARCHAR2(45 BYTE),
  CREATEDBY    VARCHAR2(45 BYTE),
  CROMOD_DATE  DATE
);

ALTER TABLE TRANSP.MOTEVENTLOG_MESSAGEGROUP ADD ( PRIMARY KEY (GROUP_NAME));

CREATE TABLE TRANSP.MOTEVENTLOG_STOP
(
  STOP_NUMBER  NUMBER(6),
  EVENTLOG_ID  VARCHAR2(40 BYTE)
);

CREATE INDEX TRANSP.IDX_MOTEL_STOP ON TRANSP.MOTEVENTLOG_STOP(STOP_NUMBER);

ALTER TABLE TRANSP.MOTEVENTLOG_STOP ADD ( CONSTRAINT FK_MOTEVENTLOG_ID FOREIGN KEY (EVENTLOG_ID) REFERENCES TRANSP.MOTEVENTLOG_BOOK (EVENT_ID));

CREATE TABLE TRANSP.MOTEVENTLOG_TYPE
(
  NAME         VARCHAR2(40 BYTE)                NOT NULL,
  DESCRIPTION  VARCHAR2(45 BYTE),
  MSGGROUP_ID  VARCHAR2(45 BYTE),
  CREATED_BY   VARCHAR2(30 BYTE),
  CROMOD_DATE  DATE
);

ALTER TABLE TRANSP.MOTEVENTLOG_TYPE ADD ( PRIMARY KEY (NAME));

ALTER TABLE TRANSP.MOTEVENTLOG_TYPE ADD (  CONSTRAINT FK_MOTEVTSUBTYPE_GROUPID FOREIGN KEY (MSGGROUP_ID) REFERENCES TRANSP.MOTEVENTLOG_MESSAGEGROUP (GROUP_NAME)
    ON DELETE CASCADE);

/*Grants*/
GRANT delete, insert, select, update ON MIS.ORDER_DELIVERY_METRIC TO fdtrn_ststg01;
GRANT delete, insert, select, update ON MIS.ORDER_DELIVERY_METRIC TO fdstore_ststg01;
GRANT select ON MIS.ORDER_DELIVERY_METRIC TO appdev;

GRANT DELETE, INSERT, SELECT, UPDATE ON TRANSP.EVENTLOG_BOOK TO fdstore_ststg01;
GRANT DELETE, INSERT, SELECT, UPDATE ON TRANSP.EVENTLOG_BOOK TO fdtrn_ststg01;
GRANT SELECT ON TRANSP.EVENTLOG_BOOK TO appdev;
GRANT SELECT ON TRANSP.EVENTLOG_BOOK TO mis;
GRANT DELETE, INSERT, SELECT, UPDATE ON TRANSP.EVENTLOG_MESSAGEGROUP TO fdstore_ststg01;
GRANT DELETE, INSERT, SELECT, UPDATE ON TRANSP.EVENTLOG_MESSAGEGROUP TO fdtrn_ststg01;
GRANT SELECT ON TRANSP.EVENTLOG_MESSAGEGROUP TO appdev;
GRANT DELETE, INSERT, SELECT, UPDATE ON TRANSP.EVENTLOG_STOP TO fdstore_ststg01;
GRANT DELETE, INSERT, SELECT, UPDATE ON TRANSP.EVENTLOG_STOP TO fdtrn_ststg01;
GRANT SELECT ON TRANSP.EVENTLOG_STOP TO appdev;
GRANT SELECT ON TRANSP.EVENTLOG_STOP TO mis;
GRANT DELETE, INSERT, SELECT, UPDATE ON TRANSP.EVENTLOG_TYPE TO fdstore_ststg01;
GRANT DELETE, INSERT, SELECT, UPDATE ON TRANSP.EVENTLOG_TYPE TO fdtrn_ststg01;
GRANT SELECT ON TRANSP.EVENTLOG_TYPE TO appdev;
GRANT DELETE, INSERT, SELECT, UPDATE ON TRANSP.EVENTLOG_SUBTYPE TO fdstore_ststg01;
GRANT DELETE, INSERT, SELECT, UPDATE ON TRANSP.EVENTLOG_SUBTYPE TO fdtrn_ststg01;
GRANT SELECT ON TRANSP.EVENTLOG_SUBTYPE TO appdev;

GRANT DELETE, INSERT, SELECT, UPDATE ON TRANSP.MOTEVENTLOG_BOOK TO fdstore_ststg01;
GRANT DELETE, INSERT, SELECT, UPDATE ON TRANSP.MOTEVENTLOG_BOOK TO fdtrn_ststg01;
GRANT SELECT ON TRANSP.MOTEVENTLOG_BOOK TO mis;
GRANT SELECT ON TRANSP.MOTEVENTLOG_BOOK TO appdev;
GRANT DELETE, INSERT, SELECT, UPDATE ON TRANSP.MOTEVENTLOG_STOP TO fdstore_ststg01;
GRANT DELETE, INSERT, SELECT, UPDATE ON TRANSP.MOTEVENTLOG_STOP TO fdtrn_ststg01;
GRANT SELECT ON TRANSP.MOTEVENTLOG_STOP TO MIS;
GRANT SELECT ON TRANSP.MOTEVENTLOG_STOP TO appdev;
GRANT DELETE, INSERT, SELECT, UPDATE ON TRANSP.MOTEVENTLOG_MESSAGEGROUP TO fdstore_ststg01;
GRANT DELETE, INSERT, SELECT, UPDATE ON TRANSP.MOTEVENTLOG_MESSAGEGROUP TO fdtrn_ststg01;
GRANT SELECT ON TRANSP.MOTEVENTLOG_MESSAGEGROUP TO appdev;
GRANT DELETE, INSERT, SELECT, UPDATE ON TRANSP.MOTEVENTLOG_TYPE TO fdstore_ststg01;
GRANT DELETE, INSERT, SELECT, UPDATE ON TRANSP.MOTEVENTLOG_TYPE TO fdtrn_ststg01;
GRANT SELECT ON TRANSP.MOTEVENTLOG_TYPE TO appdev;

GRANT SELECT ON TRANSP.HANDOFF_BATCH TO mis;
GRANT SELECT ON TRANSP.HANDOFF_BATCHSTOP TO mis;
GRANT SELECT ON DLV.CARTONTRACKING TO mis;
GRANT SELECT ON CUST.LATEISSUE_ORDERS TO mis;


/*Event log Message Groups */
Insert into TRANSP.EVENTLOG_MESSAGEGROUP  (GROUP_NAME, EMAIL, CREATEDBY, CROMOD_DATE) Values   ('Fire Truck Management', 'firetruckmanagement@freshdirect.com', 'kkanuganti', sysdate);
Insert into TRANSP.EVENTLOG_MESSAGEGROUP  (GROUP_NAME, EMAIL, CREATEDBY, CROMOD_DATE) Values   ('CSG Senior Staff', 'csgseniorstaff@freshdirect.com', 'kkanuganti', sysdate);
Insert into TRANSP.EVENTLOG_MESSAGEGROUP  (GROUP_NAME, EMAIL, CREATEDBY, CROMOD_DATE) Values   ('Field Managers', 'fieldmanagers@freshdirect.com', 'kkanuganti', sysdate);
Insert into TRANSP.EVENTLOG_MESSAGEGROUP  (GROUP_NAME, EMAIL, CREATEDBY, CROMOD_DATE) Values   ('Frank Mansour', 'fmansour@freshdirect.com', 'kkanuganti', sysdate);
Insert into TRANSP.EVENTLOG_MESSAGEGROUP  (GROUP_NAME, EMAIL, CREATEDBY, CROMOD_DATE) Values   ('George Walker', 'gwalker@freshdirect.com', 'kkanuganti', sysdate);
Insert into TRANSP.EVENTLOG_MESSAGEGROUP  (GROUP_NAME, EMAIL, CREATEDBY, CROMOD_DATE) Values   ('Jay Narvasa', 'jnarvasa@freshdirect.com', 'kkanuganti', sysdate);
Insert into TRANSP.EVENTLOG_MESSAGEGROUP  (GROUP_NAME, EMAIL, CREATEDBY, CROMOD_DATE) Values   ('Katie Whalen', 'kwhalen@freshdirect.com', 'kkanuganti', sysdate);
Insert into TRANSP.EVENTLOG_MESSAGEGROUP  (GROUP_NAME, EMAIL, CREATEDBY, CROMOD_DATE) Values   ('Ops Management', 'OpsManagement@freshdirect.com', 'kkanuganti', sysdate);
Insert into TRANSP.EVENTLOG_MESSAGEGROUP  (GROUP_NAME, EMAIL, CREATEDBY, CROMOD_DATE) Values   ('Ops Management Alerts', 'OpsMgmtAlerts@freshdirect.com', 'kkanuganti', sysdate);
Insert into TRANSP.EVENTLOG_MESSAGEGROUP  (GROUP_NAME, EMAIL, CREATEDBY, CROMOD_DATE) Values   ('Returns Center', 'returnscenter@freshdirect.com', 'kkanuganti', sysdate);
Insert into TRANSP.EVENTLOG_MESSAGEGROUP  (GROUP_NAME, EMAIL, CREATEDBY, CROMOD_DATE) Values   ('Transportartion', 'TransportationOperationsTeam@freshdirect.com', 'kkanuganti', sysdate);
Insert into TRANSP.EVENTLOG_MESSAGEGROUP  (GROUP_NAME, EMAIL, CREATEDBY, CROMOD_DATE) Values   ('Transportation Admins', 'TransportationAdmins@freshdirect.com', 'kkanuganti', sysdate);
Insert into TRANSP.EVENTLOG_MESSAGEGROUP  (GROUP_NAME, EMAIL, CREATEDBY, CROMOD_DATE) Values   ('Transportation Shipping Yard', 'transportationshippingyard@freshdirect.com', 'kkanuganti', sysdate);
Insert into TRANSP.EVENTLOG_MESSAGEGROUP  (GROUP_NAME, EMAIL, CREATEDBY, CROMOD_DATE) Values   ('Trucks', 'trucks@freshdirect.com', 'kkanuganti', sysdate);

Insert into TRANSP.MOTEVENTLOG_MESSAGEGROUP  (GROUP_NAME, EMAIL, CREATEDBY, CROMOD_DATE) Values   ('CSG Senior Staff', 'csgseniorstaff@freshdirect.com', 'kkanuganti', sysdate);
Insert into TRANSP.MOTEVENTLOG_MESSAGEGROUP  (GROUP_NAME, EMAIL, CREATEDBY, CROMOD_DATE) Values   ('Ops Management', 'OpsManagement@freshdirect.com', 'kkanuganti', sysdate);


/*Mot EventType */
Insert into TRANSP.MOTEVENTLOG_TYPE  (NAME, DESCRIPTION, MSGGROUP_ID, CREATED_BY, CROMOD_DATE) Values  ('Delivery Asst.', 'Delivery Asst.', 'CSG Senior Staff', 'kkanuganti', sysdate);
Insert into TRANSP.MOTEVENTLOG_TYPE  (NAME, DESCRIPTION, MSGGROUP_ID, CREATED_BY, CROMOD_DATE) Values  ('Mechanical Issue', 'Mechanical Issue', 'CSG Senior Staff', 'kkanuganti', sysdate);
Insert into TRANSP.MOTEVENTLOG_TYPE  (NAME, DESCRIPTION, MSGGROUP_ID, CREATED_BY, CROMOD_DATE) Values  ('AirClic Equipment', 'AirClic Equipment', 'CSG Senior Staff', 'kkanuganti', sysdate);
Insert into TRANSP.MOTEVENTLOG_TYPE  (NAME, DESCRIPTION, MSGGROUP_ID, CREATED_BY, CROMOD_DATE) Values  ('Special Delivery', 'Special Delivery', 'CSG Senior Staff', 'kkanuganti', sysdate);
Insert into TRANSP.MOTEVENTLOG_TYPE  (NAME, DESCRIPTION, MSGGROUP_ID, CREATED_BY, CROMOD_DATE) Values  ('Other', 'Other', 'CSG Senior Staff', 'kkanuganti', sysdate);
Insert into TRANSP.MOTEVENTLOG_TYPE  (NAME, DESCRIPTION, MSGGROUP_ID, CREATED_BY, CROMOD_DATE) Values  ('Late Box', 'Late Box', 'CSG Senior Staff', 'kkanuganti', sysdate);
Insert into TRANSP.MOTEVENTLOG_TYPE  (NAME, DESCRIPTION, MSGGROUP_ID, CREATED_BY, CROMOD_DATE) Values  ('Note from Ops Ctr', 'Note from Ops Ctr', 'CSG Senior Staff', 'kkanuganti', sysdate);
Insert into TRANSP.MOTEVENTLOG_TYPE  (NAME, DESCRIPTION, MSGGROUP_ID, CREATED_BY, CROMOD_DATE) Values  ('End of Shift', 'End of Shift', 'CSG Senior Staff', 'kkanuganti', sysdate);
Insert into TRANSP.MOTEVENTLOG_TYPE  (NAME, DESCRIPTION, MSGGROUP_ID, CREATED_BY, CROMOD_DATE) Values  ('Risk', 'Risk', 'CSG Senior Staff', 'kkanuganti', sysdate);
Insert into TRANSP.MOTEVENTLOG_TYPE  (NAME, DESCRIPTION, MSGGROUP_ID, CREATED_BY, CROMOD_DATE) Values  ('Pick-Up', 'Pick-Up', 'CSG Senior Staff', 'kkanuganti', sysdate);
Insert into TRANSP.MOTEVENTLOG_TYPE  (NAME, DESCRIPTION, MSGGROUP_ID, CREATED_BY, CROMOD_DATE) Values  ('Route Refused Help', 'Route Refused Help', 'CSG Senior Staff', 'kkanuganti', sysdate);
Insert into TRANSP.MOTEVENTLOG_TYPE  (NAME, DESCRIPTION, MSGGROUP_ID, CREATED_BY, CROMOD_DATE) Values  ('Note on >30 Min Late', 'Note on >30 Min Late', 'CSG Senior Staff', 'kkanuganti', sysdate);
Insert into TRANSP.MOTEVENTLOG_TYPE  (NAME, DESCRIPTION, MSGGROUP_ID, CREATED_BY, CROMOD_DATE) Values  ('Start of Shift', 'Start of Shift', 'CSG Senior Staff', 'kkanuganti', sysdate);
Insert into TRANSP.MOTEVENTLOG_TYPE  (NAME, DESCRIPTION, MSGGROUP_ID, CREATED_BY, CROMOD_DATE) Values  ('MOT Recap', 'MOT Recap', 'CSG Senior Staff', 'kkanuganti', sysdate);
Insert into TRANSP.MOTEVENTLOG_TYPE  (NAME, DESCRIPTION, MSGGROUP_ID, CREATED_BY, CROMOD_DATE) Values  ('No MOT Available', 'No MOT Available', 'Ops Management', 'kkanuganti', sysdate);
Insert into TRANSP.MOTEVENTLOG_TYPE  (NAME, DESCRIPTION, MSGGROUP_ID, CREATED_BY, CROMOD_DATE) Values  ('MOT Recap - Product', 'MOT Recap - Product', 'CSG Senior Staff', 'kkanuganti', sysdate);
Insert into TRANSP.MOTEVENTLOG_TYPE  (NAME, DESCRIPTION, MSGGROUP_ID, CREATED_BY, CROMOD_DATE) Values  ('MOT Recap - People', 'MOT Recap - People', 'CSG Senior Staff', 'kkanuganti', sysdate);
Insert into TRANSP.MOTEVENTLOG_TYPE  (NAME, DESCRIPTION, MSGGROUP_ID, CREATED_BY, CROMOD_DATE) Values  ('MOT Recap - Equipmen', 'MOT Recap - Equipment', 'CSG Senior Staff', 'kkanuganti', sysdate);
Insert into TRANSP.MOTEVENTLOG_TYPE  (NAME, DESCRIPTION, MSGGROUP_ID, CREATED_BY, CROMOD_DATE) Values  ('Request Canceled', 'Request Canceled', 'CSG Senior Staff', 'kkanuganti', sysdate);
Insert into TRANSP.MOTEVENTLOG_TYPE  (NAME, DESCRIPTION, MSGGROUP_ID, CREATED_BY, CROMOD_DATE) Values  ('MOT Not Available', 'MOT Not Available', 'Ops Management', 'kkanuganti', sysdate);


Insert into TRANSP.EVENTLOG_TYPE  (EVENTTYPE_NAME, EVENTTYPE_DESCRIPTION, CREATEDBY, CROMOD_DATE, EMPLOYEE_REQ, CUSTOMER_REQ) Values  ('Accident', 'Log of incident', 'kkanuganti', sysdate, 'X', 'X');
Insert into TRANSP.EVENTLOG_TYPE  (EVENTTYPE_NAME, EVENTTYPE_DESCRIPTION, CREATEDBY, CROMOD_DATE) Values  ('Late Dispatch', 'Truck leaving facility due to various reasons', 'kkanuganti', sysdate);
Insert into TRANSP.EVENTLOG_TYPE  (EVENTTYPE_NAME, EVENTTYPE_DESCRIPTION, CREATEDBY, CROMOD_DATE) Values  ('Out of Service', 'Out of service notifications', 'kkanuganti', sysdate);
Insert into TRANSP.EVENTLOG_TYPE  (EVENTTYPE_NAME, EVENTTYPE_DESCRIPTION, CREATEDBY, CROMOD_DATE) Values  ('Delivery Issues', 'Late delivery and recovery issues', 'kkanuganti', sysdate);
Insert into TRANSP.EVENTLOG_TYPE  (EVENTTYPE_NAME, EVENTTYPE_DESCRIPTION, CREATEDBY, CROMOD_DATE, EMPLOYEE_REQ) Values ('Employee Issues', 'Log of employee follow-up needed', 'kkanuganti', sysdate, 'X');
Insert into TRANSP.EVENTLOG_TYPE  (EVENTTYPE_NAME, EVENTTYPE_DESCRIPTION, CREATEDBY, CROMOD_DATE) Values  ('Other', 'Various', 'kkanuganti', sysdate);
Insert into TRANSP.EVENTLOG_TYPE  (EVENTTYPE_NAME, EVENTTYPE_DESCRIPTION, CREATEDBY, CROMOD_DATE, EMPLOYEE_REQ) Values  ('Missing Box Follow-U', 'Entries related to assigned MB follow-up', 'kkanuganti', sysdate, 'X');
Insert into TRANSP.EVENTLOG_TYPE  (EVENTTYPE_NAME, EVENTTYPE_DESCRIPTION, CREATEDBY, CROMOD_DATE) Values  ('Scanning', 'Scanning Issues', 'kkanuganti', sysdate);
Insert into TRANSP.EVENTLOG_TYPE  (EVENTTYPE_NAME, EVENTTYPE_DESCRIPTION, CREATEDBY, CROMOD_DATE, EMPLOYEE_REQ) Values  ('Field Audit', 'Field Audit', 'kkanuganti', sysdate, 'X');
Insert into TRANSP.EVENTLOG_TYPE  (EVENTTYPE_NAME, EVENTTYPE_DESCRIPTION, CREATEDBY, CROMOD_DATE, EMPLOYEE_REQ) Values   ('Late', 'bike tour', 'kkanuganti', sysdate, 'X');
Insert into TRANSP.EVENTLOG_TYPE  (EVENTTYPE_NAME, EVENTTYPE_DESCRIPTION, CREATEDBY, CROMOD_DATE) Values  ('Kronos', 'System Not Working', 'kkanuganti', sysdate);
Insert into TRANSP.EVENTLOG_TYPE  (EVENTTYPE_NAME, EVENTTYPE_DESCRIPTION, CREATEDBY, CROMOD_DATE) Values  ('Pro Foods', 'Pro Foods', 'kkanuganti', sysdate);
Insert into TRANSP.EVENTLOG_TYPE  (EVENTTYPE_NAME, EVENTTYPE_DESCRIPTION, CREATEDBY, CROMOD_DATE) Values  ('Returns Truck Pro fo', 'Returns truck is ready', 'kkanuganti', sysdate);
Insert into TRANSP.EVENTLOG_TYPE  (EVENTTYPE_NAME, EVENTTYPE_DESCRIPTION, CREATEDBY, CROMOD_DATE, CUSTOMER_REQ) Values  ('MOT Specials', 'Events concerning MOT/Fire Truck work', 'kkanuganti', sysdate, 'X');
Insert into TRANSP.EVENTLOG_TYPE  (EVENTTYPE_NAME, EVENTTYPE_DESCRIPTION, CREATEDBY, CROMOD_DATE) Values  ('Action Taken by Ops', 'Used only for ops DSS Reports', 'kkanuganti', sysdate);
Insert into TRANSP.EVENTLOG_TYPE  (EVENTTYPE_NAME, EVENTTYPE_DESCRIPTION, CREATEDBY, CROMOD_DATE) Values  ('Action Taken by Planning', 'Transportation Planning Action', 'kkanuganti', sysdate);
Insert into TRANSP.EVENTLOG_TYPE  (EVENTTYPE_NAME, EVENTTYPE_DESCRIPTION, CREATEDBY, CROMOD_DATE, EMPLOYEE_REQ, CUSTOMER_REQ) Values  ('Ride Along Audit', 'Ride Along Audit', 'kkanuganti', sysdate, 'X',    'X');
Insert into TRANSP.EVENTLOG_TYPE (EVENTTYPE_NAME, EVENTTYPE_DESCRIPTION, CREATEDBY, CROMOD_DATE, EMPLOYEE_REQ) Values  ('Interviews', 'Interviews', 'kkanuganti', sysdate, 'X');
Insert into TRANSP.EVENTLOG_TYPE (EVENTTYPE_NAME, EVENTTYPE_DESCRIPTION, CREATEDBY, CROMOD_DATE, EMPLOYEE_REQ) Values  ('Training', 'Training', 'kkanuganti', sysdate, 'X');
Insert into TRANSP.EVENTLOG_TYPE  (EVENTTYPE_NAME, EVENTTYPE_DESCRIPTION, CREATEDBY, CROMOD_DATE, EMPLOYEE_REQ, CUSTOMER_REQ) Values  ('yard', 'set up', 'kkanuganti', sysdate, 'X', 'X');
Insert into TRANSP.EVENTLOG_TYPE  (EVENTTYPE_NAME, EVENTTYPE_DESCRIPTION, CREATEDBY, CROMOD_DATE) Values ('DSS6', 'DSS screen', 'kkanuganti', sysdate);
Insert into TRANSP.EVENTLOG_TYPE
   (EVENTTYPE_NAME, EVENTTYPE_DESCRIPTION, CREATEDBY, CROMOD_DATE)
 Values
   ('Telargo Alert', 'Telargo Alert Email', 'kkanuganti', sysdate);
Insert into TRANSP.EVENTLOG_TYPE
   (EVENTTYPE_NAME, EVENTTYPE_DESCRIPTION, CREATEDBY, CROMOD_DATE)
 Values
   ('TGO Manual Deactivation', 'TGO Manual Deactivation', 'kkanuganti', sysdate);
Insert into TRANSP.EVENTLOG_TYPE
   (EVENTTYPE_NAME, EVENTTYPE_DESCRIPTION, CREATEDBY, CROMOD_DATE, EMPLOYEE_REQ, CUSTOMER_REQ)
 Values
   ('Muni Meter Card', 'Muni Meter Card', 'kkanuganti', sysdate, 'X',  'X');
Insert into TRANSP.EVENTLOG_TYPE
   (EVENTTYPE_NAME, EVENTTYPE_DESCRIPTION, CREATEDBY, CROMOD_DATE)
 Values
   ('TGO Excessive Back Door', 'TGO Excessive Back Door', 'kkanuganti', sysdate);
Insert into TRANSP.EVENTLOG_TYPE
   (EVENTTYPE_NAME, EVENTTYPE_DESCRIPTION, CREATEDBY, CROMOD_DATE)
 Values
   ('TGO Excessive Freezer Door', 'TGO Excessive Freezer Door', 'kkanuganti', sysdate);
Insert into TRANSP.EVENTLOG_TYPE
   (EVENTTYPE_NAME, EVENTTYPE_DESCRIPTION, CREATEDBY, CROMOD_DATE)
 Values
   ('AA Late Box Feedback', 'AA Late Box Feedback', 'kkanuganti', sysdate);
Insert into TRANSP.EVENTLOG_TYPE
   (EVENTTYPE_NAME, EVENTTYPE_DESCRIPTION, CREATEDBY, CROMOD_DATE)
 Values
   ('Recommendation by Planning Team', 'Planning Team Recommends', 'kkanuganti', sysdate);
Insert into TRANSP.EVENTLOG_TYPE
   (EVENTTYPE_NAME, EVENTTYPE_DESCRIPTION, CREATEDBY, CROMOD_DATE)
 Values
   ('Bottom_Routes', 'Bottom_Routes', 'kkanuganti', sysdate);
Insert into TRANSP.EVENTLOG_TYPE
   (EVENTTYPE_NAME, EVENTTYPE_DESCRIPTION, CREATEDBY, CROMOD_DATE)
 Values
   ('TGO-Door Open While Driving', 'Driving while door open and speed > 10mph', 'kkanuganti', sysdate);
Insert into TRANSP.EVENTLOG_TYPE
   (EVENTTYPE_NAME, EVENTTYPE_DESCRIPTION, CREATEDBY, CROMOD_DATE)
 Values
   ('TGO DPF Light', 'Diesel Particulate Filters (DPF) Light is on', 'kkanuganti', sysdate);
Insert into TRANSP.EVENTLOG_TYPE
   (EVENTTYPE_NAME, EVENTTYPE_DESCRIPTION, CREATEDBY, CROMOD_DATE)
 Values
   ('Emergency Brake', 'Emergency Brake is engaged and truck is out o', 'kkanuganti', sysdate);
Insert into TRANSP.EVENTLOG_TYPE
   (EVENTTYPE_NAME, EVENTTYPE_DESCRIPTION, CREATEDBY, CROMOD_DATE)
 Values
   ('TGO Emergency Brake', 'Emergency Brake is engaged and truck is out o', 'kkanuganti', sysdate);
Insert into TRANSP.EVENTLOG_TYPE
   (EVENTTYPE_NAME, EVENTTYPE_DESCRIPTION, CREATEDBY, CROMOD_DATE)
 Values
   ('Nextel Inbound Log', 'Nextel Inbound Log', 'kkanuganti', sysdate);
Insert into TRANSP.EVENTLOG_TYPE
   (EVENTTYPE_NAME, EVENTTYPE_DESCRIPTION, CREATEDBY, CROMOD_DATE)
 Values
   ('MOT Assistance', 'MOT Assistance', 'kkanuganti', sysdate);
Insert into TRANSP.EVENTLOG_TYPE
   (EVENTTYPE_NAME, EVENTTYPE_DESCRIPTION, CREATEDBY, CROMOD_DATE)
 Values
   ('Fraud Review', 'Fraud Review', 'kkanuganti', sysdate);
Insert into TRANSP.EVENTLOG_TYPE
   (EVENTTYPE_NAME, EVENTTYPE_DESCRIPTION, CREATEDBY, CROMOD_DATE)
 Values
   ('Carton Issues', 'Carton Related Issues', 'kkanuganti', sysdate);
Insert into TRANSP.EVENTLOG_TYPE
   (EVENTTYPE_NAME, EVENTTYPE_DESCRIPTION, CREATEDBY, CROMOD_DATE, CUSTOMER_REQ)
 Values
   ('Phone issues', 'Intermec issues', 'kkanuganti', sysdate, 'X');
Insert into TRANSP.EVENTLOG_TYPE
   (EVENTTYPE_NAME, EVENTTYPE_DESCRIPTION, CREATEDBY, CROMOD_DATE, EMPLOYEE_REQ)
 Values
   ('Intermec issues', 'Phone issues', 'kkanuganti', sysdate, 'X');
Insert into TRANSP.EVENTLOG_TYPE
   (EVENTTYPE_NAME, EVENTTYPE_DESCRIPTION, CREATEDBY, CROMOD_DATE)
 Values
   ('Ops Supervision', 'Ops Supervision', 'kkanuganti', sysdate);
Insert into TRANSP.EVENTLOG_TYPE
   (EVENTTYPE_NAME, EVENTTYPE_DESCRIPTION, CREATEDBY, CROMOD_DATE)
 Values
   ('End of Shift Scanner Log', 'End of Shift Scanner Log', 'kkanuganti', sysdate);
Insert into TRANSP.EVENTLOG_TYPE
   (EVENTTYPE_NAME, EVENTTYPE_DESCRIPTION, CREATEDBY, CROMOD_DATE)
 Values
   ('Mechanical Failure and Service', 'Mechanical Failure and Service', 'kkanuganti', sysdate);
   
   
 /*Event log Sub Type*/

Insert into TRANSP.EVENTLOG_SUBTYPE
   (EVENTSUBTYPE_NAME, DESCRIPTION, EVENTTYPE_ID, CREATEDBY, CROMOD_DATE)
 Values
   ('False/Order Not Found', 'False/Order Not Found', 'Action Taken by Ops', 'kkanuganti', sysdate);
Insert into TRANSP.EVENTLOG_SUBTYPE
   (EVENTSUBTYPE_NAME, DESCRIPTION, EVENTTYPE_ID, CREATEDBY, CROMOD_DATE)
 Values
   ('False/Freeze', 'False/Freeze', 'Action Taken by Ops', 'kkanuganti', sysdate);
Insert into TRANSP.EVENTLOG_SUBTYPE
   (EVENTSUBTYPE_NAME, DESCRIPTION, EVENTTYPE_ID, CREATEDBY, CROMOD_DATE)
 Values
   ('False/Laser Problems', 'False/Laser Problems', 'Action Taken by Ops', 'kkanuganti', sysdate);
Insert into TRANSP.EVENTLOG_SUBTYPE
   (EVENTSUBTYPE_NAME, DESCRIPTION, EVENTTYPE_ID, CREATEDBY, CROMOD_DATE)
 Values
   ('Roadnet Map Changes', 'Inform or Request Roadnet Map Changes', 'Action Taken by Planning', 'kkanuganti', sysdate);
Insert into TRANSP.EVENTLOG_SUBTYPE
   (EVENTSUBTYPE_NAME, DESCRIPTION, EVENTTYPE_ID, CREATEDBY, CROMOD_DATE)
 Values
   ('Redelivery - Dairy', 'Sameday redelivery of product', 'Action Taken by Ops', 'kkanuganti', sysdate);
Insert into TRANSP.EVENTLOG_SUBTYPE
   (EVENTSUBTYPE_NAME, DESCRIPTION, EVENTTYPE_ID, CREATEDBY, CROMOD_DATE)
 Values
   ('New Hire', 'Employee', 'Interviews', 'kkanuganti', sysdate);
Insert into TRANSP.EVENTLOG_SUBTYPE
   (EVENTSUBTYPE_NAME, DESCRIPTION, EVENTTYPE_ID, CREATEDBY, CROMOD_DATE)
 Values
   ('Stolen Handtruck', 'Handtruck stolen', 'Employee Issues', 'kkanuganti', sysdate);
Insert into TRANSP.EVENTLOG_SUBTYPE
   (EVENTSUBTYPE_NAME, DESCRIPTION, EVENTTYPE_ID, CREATEDBY, CROMOD_DATE)
 Values
   ('Heavy Window', 'Heavy Window', 'Delivery Issues', 'kkanuganti', sysdate);
Insert into TRANSP.EVENTLOG_SUBTYPE
   (EVENTSUBTYPE_NAME, DESCRIPTION, EVENTTYPE_ID, CREATEDBY, CROMOD_DATE)
 Values
   ('Screen error', 'Error on screen', 'DSS6', 'kkanuganti', sysdate);
Insert into TRANSP.EVENTLOG_SUBTYPE
   (EVENTSUBTYPE_NAME, DESCRIPTION, EVENTTYPE_ID, CREATEDBY, CROMOD_DATE)
 Values
   ('On break', 'On break', 'DSS6', 'kkanuganti', sysdate);
Insert into TRANSP.EVENTLOG_SUBTYPE
   (EVENTSUBTYPE_NAME, DESCRIPTION, EVENTTYPE_ID, CREATEDBY, CROMOD_DATE)
 Values
   ('On a call', 'On a call', 'DSS6', 'kkanuganti', sysdate);
Insert into TRANSP.EVENTLOG_SUBTYPE
   (EVENTSUBTYPE_NAME, DESCRIPTION, EVENTTYPE_ID, CREATEDBY, CROMOD_DATE)
 Values
   ('Staffing', 'Staffing Issues', 'Employee Issues', 'kkanuganti', sysdate);
Insert into TRANSP.EVENTLOG_SUBTYPE
   (EVENTSUBTYPE_NAME, DESCRIPTION, EVENTTYPE_ID, CREATEDBY, CROMOD_DATE)
 Values
   ('Did not see it', 'Did not see it', 'DSS6', 'kkanuganti', sysdate);
Insert into TRANSP.EVENTLOG_SUBTYPE
   (EVENTSUBTYPE_NAME, DESCRIPTION, EVENTTYPE_ID, CREATEDBY, CROMOD_DATE)
 Values
   ('Other', 'Other', 'DSS6', 'kkanuganti', sysdate);
Insert into TRANSP.EVENTLOG_SUBTYPE
   (EVENTSUBTYPE_NAME, DESCRIPTION, EVENTTYPE_ID, CREATEDBY, CROMOD_DATE)
 Values
   ('False/No_Data_Transmit', 'False/No_Data_Transmit', 'Action Taken by Ops', 'kkanuganti', sysdate);
Insert into TRANSP.EVENTLOG_SUBTYPE
   (EVENTSUBTYPE_NAME, DESCRIPTION, EVENTTYPE_ID, CREATEDBY, CROMOD_DATE)
 Values
   ('False/Fusion_Error', 'False/Fusion_Error', 'Action Taken by Ops', 'kkanuganti', sysdate);
Insert into TRANSP.EVENTLOG_SUBTYPE
   (EVENTSUBTYPE_NAME, DESCRIPTION, EVENTTYPE_ID, CREATEDBY, CROMOD_DATE)
 Values
   ('Other', 'Other', 'TGO Manual Deactivation', 'kkanuganti', sysdate);
Insert into TRANSP.EVENTLOG_SUBTYPE
   (EVENTSUBTYPE_NAME, DESCRIPTION, EVENTTYPE_ID, CREATEDBY, CROMOD_DATE)
 Values
   ('Equipment Failure/Reefer Code', 'Equipment Failure/Reefer Code', 'TGO Manual Deactivation', 'kkanuganti', sysdate);
Insert into TRANSP.EVENTLOG_SUBTYPE
   (EVENTSUBTYPE_NAME, DESCRIPTION, EVENTTYPE_ID, CREATEDBY, CROMOD_DATE)
 Values
   ('Dispatch', 'Dispatch', 'Muni Meter Card', 'kkanuganti', sysdate);
Insert into TRANSP.EVENTLOG_SUBTYPE
   (EVENTSUBTYPE_NAME, DESCRIPTION, EVENTTYPE_ID, CREATEDBY, CROMOD_DATE)
 Values
   ('Nxtl- Cust Call Requested, no IVR Issue', 'Nxtl- Cust Call Requested, no IVR Issue', 'Action Taken by Ops', 'kkanuganti', sysdate);
Insert into TRANSP.EVENTLOG_SUBTYPE
   (EVENTSUBTYPE_NAME, DESCRIPTION, EVENTTYPE_ID, CREATEDBY, CROMOD_DATE)
 Values
   ('Nxtl- Missing/Damaged/Found Carton', 'Nxtl- Missing/Damaged/Found Carton', 'Action Taken by Ops', 'kkanuganti', sysdate);
Insert into TRANSP.EVENTLOG_SUBTYPE
   (EVENTSUBTYPE_NAME, DESCRIPTION, EVENTTYPE_ID, CREATEDBY, CROMOD_DATE)
 Values
   ('Nxtl- Needs Directions/Address', 'Nxtl- Needs Directions/Address', 'Action Taken by Ops', 'kkanuganti', sysdate);
Insert into TRANSP.EVENTLOG_SUBTYPE
   (EVENTSUBTYPE_NAME, DESCRIPTION, EVENTTYPE_ID, CREATEDBY, CROMOD_DATE)
 Values
   ('Nxtl- Other', 'Nxtl- Other', 'Action Taken by Ops', 'kkanuganti', sysdate);
Insert into TRANSP.EVENTLOG_SUBTYPE
   (EVENTSUBTYPE_NAME, DESCRIPTION, EVENTTYPE_ID, CREATEDBY, CROMOD_DATE)
 Values
   ('Nxtl- Report Late, FT not needed', 'Nxtl- Report Late, FT not needed', 'Action Taken by Ops', 'kkanuganti', sysdate);
Insert into TRANSP.EVENTLOG_SUBTYPE
   (EVENTSUBTYPE_NAME, DESCRIPTION, EVENTTYPE_ID, CREATEDBY, CROMOD_DATE)
 Values
   ('Nxtl- Request FireTruck', 'Nxtl- Request FireTruck', 'Action Taken by Ops', 'kkanuganti', sysdate);
Insert into TRANSP.EVENTLOG_SUBTYPE
   (EVENTSUBTYPE_NAME, DESCRIPTION, EVENTTYPE_ID, CREATEDBY, CROMOD_DATE)
 Values
   ('Nxtl- Scanner Issue', 'Nxtl- Scanner Issue', 'Action Taken by Ops', 'kkanuganti', sysdate);
Insert into TRANSP.EVENTLOG_SUBTYPE
   (EVENTSUBTYPE_NAME, DESCRIPTION, EVENTTYPE_ID, CREATEDBY, CROMOD_DATE)
 Values
   ('Nxtl- 1st Attempt Completed', 'Nxtl- 1st Attempt Completed', 'Action Taken by Ops', 'kkanuganti', sysdate);
Insert into TRANSP.EVENTLOG_SUBTYPE
   (EVENTSUBTYPE_NAME, DESCRIPTION, EVENTTYPE_ID, CREATEDBY, CROMOD_DATE)
 Values
   ('At Risk Recap', 'At Risk Recap', 'Action Taken by Ops', 'kkanuganti', sysdate);
Insert into TRANSP.EVENTLOG_SUBTYPE
   (EVENTSUBTYPE_NAME, DESCRIPTION, EVENTTYPE_ID, CREATEDBY, CROMOD_DATE)
 Values
   ('Nxtl- IVR Issue', 'Nxtl- IVR Issue', 'Action Taken by Ops', 'kkanuganti', sysdate);
Insert into TRANSP.EVENTLOG_SUBTYPE
   (EVENTSUBTYPE_NAME, DESCRIPTION, EVENTTYPE_ID, CREATEDBY, CROMOD_DATE)
 Values
   ('At Risk Recap - Driver did not call us abou', 'At Risk Recap - Driver did not call us abou', 'Action Taken by Ops', 'kkanuganti', sysdate);
Insert into TRANSP.EVENTLOG_SUBTYPE
   (EVENTSUBTYPE_NAME, DESCRIPTION, EVENTTYPE_ID, CREATEDBY, CROMOD_DATE)
 Values
   ('At Risk Recap - Spoke with driver, but not', 'At Risk Recap - Spoke with driver, but not', 'Action Taken by Ops', 'kkanuganti', sysdate);
Insert into TRANSP.EVENTLOG_SUBTYPE
   (EVENTSUBTYPE_NAME, DESCRIPTION, EVENTTYPE_ID, CREATEDBY, CROMOD_DATE)
 Values
   ('At Risk Recap - Scanning Issue', 'At Risk Recap - Scanning Issue', 'Action Taken by Ops', 'kkanuganti', sysdate);
Insert into TRANSP.EVENTLOG_SUBTYPE
   (EVENTSUBTYPE_NAME, DESCRIPTION, EVENTTYPE_ID, CREATEDBY, CROMOD_DATE)
 Values
   ('At Risk Recap - Forgot to Scan in window', 'At Risk Recap - Forgot to Scan in window', 'Action Taken by Ops', 'kkanuganti', sysdate);
Insert into TRANSP.EVENTLOG_SUBTYPE
   (EVENTSUBTYPE_NAME, DESCRIPTION, EVENTTYPE_ID, CREATEDBY, CROMOD_DATE)
 Values
   ('At Risk Recap - Other', 'At Risk Recap - Other', 'Action Taken by Ops', 'kkanuganti', sysdate);
Insert into TRANSP.EVENTLOG_SUBTYPE
   (EVENTSUBTYPE_NAME, DESCRIPTION, EVENTTYPE_ID, CREATEDBY, CROMOD_DATE)
 Values
   ('Special Delivery', 'Special Delivery', 'MOT Specials', 'kkanuganti', sysdate);
Insert into TRANSP.EVENTLOG_SUBTYPE
   (EVENTSUBTYPE_NAME, DESCRIPTION, EVENTTYPE_ID, CREATEDBY, CROMOD_DATE)
 Values
   ('Pick-Up', 'Pick-Up', 'MOT Specials', 'kkanuganti', sysdate);
Insert into TRANSP.EVENTLOG_SUBTYPE
   (EVENTSUBTYPE_NAME, DESCRIPTION, EVENTTYPE_ID, CREATEDBY, CROMOD_DATE)
 Values
   ('Window Steering Turned On', 'Activated window steering promotion', 'Action Taken by Planning', 'kkanuganti', sysdate);
Insert into TRANSP.EVENTLOG_SUBTYPE
   (EVENTSUBTYPE_NAME, DESCRIPTION, EVENTTYPE_ID, CREATEDBY, CROMOD_DATE)
 Values
   ('Food Safety', 'Food Safety', 'MOT Specials', 'kkanuganti', sysdate);
Insert into TRANSP.EVENTLOG_SUBTYPE
   (EVENTSUBTYPE_NAME, DESCRIPTION, EVENTTYPE_ID, CREATEDBY, CROMOD_DATE)
 Values
   ('Risk', 'Risk', 'MOT Specials', 'kkanuganti', sysdate);
Insert into TRANSP.EVENTLOG_SUBTYPE
   (EVENTSUBTYPE_NAME, DESCRIPTION, EVENTTYPE_ID, CREATEDBY, CROMOD_DATE)
 Values
   ('COS Recap 8-11am', 'COS Recap 8-11am', 'Action Taken by Ops', 'kkanuganti', sysdate);
Insert into TRANSP.EVENTLOG_SUBTYPE
   (EVENTSUBTYPE_NAME, DESCRIPTION, EVENTTYPE_ID, CREATEDBY, CROMOD_DATE)
 Values
   ('COS Recap 11-2pm', 'COS Recap 11-2pm', 'Action Taken by Ops', 'kkanuganti', sysdate);
Insert into TRANSP.EVENTLOG_SUBTYPE
   (EVENTSUBTYPE_NAME, DESCRIPTION, EVENTTYPE_ID, CREATEDBY, CROMOD_DATE)
 Values
   ('COS Recap 2-4pm', 'COS Recap 2-4pm', 'Action Taken by Ops', 'kkanuganti', sysdate);
Insert into TRANSP.EVENTLOG_SUBTYPE
   (EVENTSUBTYPE_NAME, DESCRIPTION, EVENTTYPE_ID, CREATEDBY, CROMOD_DATE)
 Values
   ('COS Recap Overall', 'COS Recap Overall', 'Action Taken by Ops', 'kkanuganti', sysdate);
Insert into TRANSP.EVENTLOG_SUBTYPE
   (EVENTSUBTYPE_NAME, DESCRIPTION, EVENTTYPE_ID, CREATEDBY, CROMOD_DATE)
 Values
   ('AirClic Equipment', 'AirClic Equipment', 'MOT Assistance', 'kkanuganti', sysdate);
Insert into TRANSP.EVENTLOG_SUBTYPE
   (EVENTSUBTYPE_NAME, DESCRIPTION, EVENTTYPE_ID, CREATEDBY, CROMOD_DATE)
 Values
   ('Delivery Asst.', 'Delivery Asst.', 'MOT Assistance', 'kkanuganti', sysdate);
Insert into TRANSP.EVENTLOG_SUBTYPE
   (EVENTSUBTYPE_NAME, DESCRIPTION, EVENTTYPE_ID, CREATEDBY, CROMOD_DATE)
 Values
   ('Late Box', 'Late Box', 'MOT Assistance', 'kkanuganti', sysdate);
Insert into TRANSP.EVENTLOG_SUBTYPE
   (EVENTSUBTYPE_NAME, DESCRIPTION, EVENTTYPE_ID, CREATEDBY, CROMOD_DATE)
 Values
   ('Other', 'Other', 'MOT Assistance', 'kkanuganti', sysdate);
Insert into TRANSP.EVENTLOG_SUBTYPE
   (EVENTSUBTYPE_NAME, DESCRIPTION, EVENTTYPE_ID, CREATEDBY, CROMOD_DATE)
 Values
   ('False/Scanner Issue', 'False/Scanner Issue', 'Action Taken by Ops', 'kkanuganti', sysdate);
Insert into TRANSP.EVENTLOG_SUBTYPE
   (EVENTSUBTYPE_NAME, DESCRIPTION, EVENTTYPE_ID, CREATEDBY, CROMOD_DATE)
 Values
   ('Nxtl- Unable to Hear Driver/Ops', 'Nxtl- Unable to Hear Driver/Ops', 'Action Taken by Ops', 'kkanuganti', sysdate);
Insert into TRANSP.EVENTLOG_SUBTYPE
   (EVENTSUBTYPE_NAME, DESCRIPTION, EVENTTYPE_ID, CREATEDBY, CROMOD_DATE)
 Values
   ('Nxtl- MOT/FireTruck Calling In', 'Nxtl- MOT/FireTruck Calling In', 'Action Taken by Ops', 'kkanuganti', sysdate);
Insert into TRANSP.EVENTLOG_SUBTYPE
   (EVENTSUBTYPE_NAME, DESCRIPTION, EVENTTYPE_ID, CREATEDBY, CROMOD_DATE)
 Values
   ('No Issues', 'No Issues', 'Fraud Review', 'kkanuganti', TO_DATE('11/28/2012 09:53:35', 'MM/DD/YYYY HH24:MI:SS'));
Insert into TRANSP.EVENTLOG_SUBTYPE
   (EVENTSUBTYPE_NAME, DESCRIPTION, EVENTTYPE_ID, CREATEDBY, CROMOD_DATE)
 Values
   ('Holiday Meal Replacement', 'Holiday Meal Replacement', 'MOT Specials', 'kkanuganti', sysdate);
Insert into TRANSP.EVENTLOG_SUBTYPE
   (EVENTSUBTYPE_NAME, DESCRIPTION, EVENTTYPE_ID, CREATEDBY, CROMOD_DATE)
 Values
   ('End of Shift Recap', 'End of Shift Recap', 'Ops Supervision', 'kkanuganti', sysdate);
Insert into TRANSP.EVENTLOG_SUBTYPE
   (EVENTSUBTYPE_NAME, DESCRIPTION, EVENTTYPE_ID, CREATEDBY, CROMOD_DATE)
 Values
   ('Freeze', 'Freeze', 'End of Shift Scanner Log', 'kkanuganti', sysdate);
Insert into TRANSP.EVENTLOG_SUBTYPE
   (EVENTSUBTYPE_NAME, DESCRIPTION, EVENTTYPE_ID, CREATEDBY, CROMOD_DATE)
 Values
   ('Battery', 'Battery', 'End of Shift Scanner Log', 'kkanuganti', sysdate);
Insert into TRANSP.EVENTLOG_SUBTYPE
   (EVENTSUBTYPE_NAME, DESCRIPTION, EVENTTYPE_ID, CREATEDBY, CROMOD_DATE)
 Values
   ('Upload', 'Upload', 'End of Shift Scanner Log', 'kkanuganti', sysdate);
Insert into TRANSP.EVENTLOG_SUBTYPE
   (EVENTSUBTYPE_NAME, DESCRIPTION, EVENTTYPE_ID, CREATEDBY, CROMOD_DATE)
 Values
   ('Other', 'Other', 'End of Shift Scanner Log', 'kkanuganti', sysdate);
Insert into TRANSP.EVENTLOG_SUBTYPE
   (EVENTSUBTYPE_NAME, DESCRIPTION, EVENTTYPE_ID, CREATEDBY, CROMOD_DATE)
 Values
   ('Unfulfilled Request', 'Unfulfilled Request', 'Action Taken by Planning', 'kkanuganti', sysdate);
Insert into TRANSP.EVENTLOG_SUBTYPE
   (EVENTSUBTYPE_NAME, DESCRIPTION, EVENTTYPE_ID, CREATEDBY, CROMOD_DATE)
 Values
   ('Volume', 'Volume', 'End of Shift Scanner Log', 'kkanuganti', sysdate);
Insert into TRANSP.EVENTLOG_SUBTYPE
   (EVENTSUBTYPE_NAME, DESCRIPTION, EVENTTYPE_ID, CREATEDBY, CROMOD_DATE)
 Values
   ('Ops can hear driver', 'Ops can hear driver', 'End of Shift Scanner Log', 'kkanuganti', sysdate);
Insert into TRANSP.EVENTLOG_SUBTYPE
   (EVENTSUBTYPE_NAME, DESCRIPTION, EVENTTYPE_ID, CREATEDBY, CROMOD_DATE)
 Values
   ('Driver can hear Ops', 'Driver can hear Ops', 'End of Shift Scanner Log', 'kkanuganti', sysdate);
Insert into TRANSP.EVENTLOG_SUBTYPE
   (EVENTSUBTYPE_NAME, DESCRIPTION, EVENTTYPE_ID, CREATEDBY, CROMOD_DATE)
 Values
   ('Scanner - Freeze', 'Scanner Issues', 'Action Taken by Ops', 'kkanuganti', sysdate);
Insert into TRANSP.EVENTLOG_SUBTYPE
   (EVENTSUBTYPE_NAME, DESCRIPTION, EVENTTYPE_ID, CREATEDBY, CROMOD_DATE)
 Values
   ('Scanner - Battery Issue', 'Scanner', 'Action Taken by Ops', 'kkanuganti', sysdate);
Insert into TRANSP.EVENTLOG_SUBTYPE
   (EVENTSUBTYPE_NAME, DESCRIPTION, EVENTTYPE_ID, CREATEDBY, CROMOD_DATE)
 Values
   ('Scanner - Delayed Upload', 'Scanner', 'Action Taken by Ops', 'kkanuganti', sysdate);
Insert into TRANSP.EVENTLOG_SUBTYPE
   (EVENTSUBTYPE_NAME, DESCRIPTION, EVENTTYPE_ID, CREATEDBY, CROMOD_DATE)
 Values
   ('Scanner - Volume Issue', 'Scanner', 'Action Taken by Ops', 'kkanuganti', sysdate);
Insert into TRANSP.EVENTLOG_SUBTYPE
   (EVENTSUBTYPE_NAME, DESCRIPTION, EVENTTYPE_ID, CREATEDBY, CROMOD_DATE)
 Values
   ('Scanner - Other', 'Scanner', 'Action Taken by Ops', 'kkanuganti', sysdate);
Insert into TRANSP.EVENTLOG_SUBTYPE
   (EVENTSUBTYPE_NAME, DESCRIPTION, EVENTTYPE_ID, CREATEDBY, CROMOD_DATE)
 Values
   ('Scanner ¿ Phone Shut-off, Cannot turn on', 'Scanner', 'Action Taken by Ops', 'kkanuganti', sysdate);
Insert into TRANSP.EVENTLOG_SUBTYPE
   (EVENTSUBTYPE_NAME, DESCRIPTION, EVENTTYPE_ID, CREATEDBY, CROMOD_DATE)
 Values
   ('IVR ¿  Missing/Found Carton', 'Driver Call In', 'Action Taken by Ops', 'kkanuganti', sysdate);
Insert into TRANSP.EVENTLOG_SUBTYPE
   (EVENTSUBTYPE_NAME, DESCRIPTION, EVENTTYPE_ID, CREATEDBY, CROMOD_DATE)
 Values
   ('IVR ¿  Reported Missing/Damaged Carton', 'Driver Call In', 'Action Taken by Planning', 'kkanuganti', sysdate);
Insert into TRANSP.EVENTLOG_SUBTYPE
   (EVENTSUBTYPE_NAME, DESCRIPTION, EVENTTYPE_ID, CREATEDBY, CROMOD_DATE)
 Values
   ('IVR ¿ Needs Directions/Address', 'Driver Call In', 'Action Taken by Ops', 'kkanuganti', sysdate);
Insert into TRANSP.EVENTLOG_SUBTYPE
   (EVENTSUBTYPE_NAME, DESCRIPTION, EVENTTYPE_ID, CREATEDBY, CROMOD_DATE)
 Values
   ('IVR ¿ Reported forgot to scan', 'Driver Call In', 'Action Taken by Ops', 'kkanuganti', sysdate);
Insert into TRANSP.EVENTLOG_SUBTYPE
   (EVENTSUBTYPE_NAME, DESCRIPTION, EVENTTYPE_ID, CREATEDBY, CROMOD_DATE)
 Values
   ('IVR ¿ Customer Call Requested', 'Driver Calls In', 'Action Taken by Ops', 'kkanuganti', sysdate);
Insert into TRANSP.EVENTLOG_SUBTYPE
   (EVENTSUBTYPE_NAME, DESCRIPTION, EVENTTYPE_ID, CREATEDBY, CROMOD_DATE)
 Values
   ('IVR ¿ Report Lateness', 'Driver Calls In', 'Action Taken by Ops', 'kkanuganti', sysdate);
Insert into TRANSP.EVENTLOG_SUBTYPE
   (EVENTSUBTYPE_NAME, DESCRIPTION, EVENTTYPE_ID, CREATEDBY, CROMOD_DATE)
 Values
   ('IVR - Returning Ops Call', 'Driver Call In', 'Action Taken by Ops', 'kkanuganti', sysdate);
Insert into TRANSP.EVENTLOG_SUBTYPE
   (EVENTSUBTYPE_NAME, DESCRIPTION, EVENTTYPE_ID, CREATEDBY, CROMOD_DATE)
 Values
   ('New Card', 'New Card', 'Muni Meter Card', 'kkanuganti', sysdate);
Insert into TRANSP.EVENTLOG_SUBTYPE
   (EVENTSUBTYPE_NAME, DESCRIPTION, EVENTTYPE_ID, CREATEDBY, CROMOD_DATE)
 Values
   ('Modified a Route', 'Modified a Route', 'Action Taken by Planning', 'kkanuganti', sysdate);
Insert into TRANSP.EVENTLOG_SUBTYPE
   (EVENTSUBTYPE_NAME, DESCRIPTION, EVENTTYPE_ID, CREATEDBY, CROMOD_DATE)
 Values
   ('Recommendations', 'Recommendations', 'Recommendation by Planning Team', 'kkanuganti', sysdate);
Insert into TRANSP.EVENTLOG_SUBTYPE
   (EVENTSUBTYPE_NAME, DESCRIPTION, EVENTTYPE_ID, CREATEDBY, CROMOD_DATE)
 Values
   ('Other', 'Other', 'TGO Excessive Freezer Door', 'kkanuganti', sysdate);
Insert into TRANSP.EVENTLOG_SUBTYPE
   (EVENTSUBTYPE_NAME, DESCRIPTION, EVENTTYPE_ID, CREATEDBY, CROMOD_DATE)
 Values
   ('Summary', 'Summary', 'Bottom_Routes', 'kkanuganti', sysdate);
Insert into TRANSP.EVENTLOG_SUBTYPE
   (EVENTSUBTYPE_NAME, DESCRIPTION, EVENTTYPE_ID, CREATEDBY, CROMOD_DATE)
 Values
   ('Emergency Brake OFF', 'While taking action on the alert, event value', 'TGO Emergency Brake', 'kkanuganti', sysdate);
Insert into TRANSP.EVENTLOG_SUBTYPE
   (EVENTSUBTYPE_NAME, DESCRIPTION, EVENTTYPE_ID, CREATEDBY, CROMOD_DATE)
 Values
   ('Report Late, FT not needed', 'Report Late, FT not needed', 'Nextel Inbound Log', 'kkanuganti', sysdate);
Insert into TRANSP.EVENTLOG_SUBTYPE
   (EVENTSUBTYPE_NAME, DESCRIPTION, EVENTTYPE_ID, CREATEDBY, CROMOD_DATE)
 Values
   ('Request Firetruck', 'Request Firetruck', 'Nextel Inbound Log', 'kkanuganti', sysdate);
Insert into TRANSP.EVENTLOG_SUBTYPE
   (EVENTSUBTYPE_NAME, DESCRIPTION, EVENTTYPE_ID, CREATEDBY, CROMOD_DATE)
 Values
   ('2nd Attempt Verify Cust is Home', '2nd Attempt Verify Cust is Home', 'Nextel Inbound Log', 'kkanuganti', sysdate);
Insert into TRANSP.EVENTLOG_SUBTYPE
   (EVENTSUBTYPE_NAME, DESCRIPTION, EVENTTYPE_ID, CREATEDBY, CROMOD_DATE)
 Values
   ('Nxtl- 2nd Attempt Completed', 'Nxtl- 2nd Attempt Completed', 'Action Taken by Ops', 'kkanuganti', sysdate);
Insert into TRANSP.EVENTLOG_SUBTYPE
   (EVENTSUBTYPE_NAME, DESCRIPTION, EVENTTYPE_ID, CREATEDBY, CROMOD_DATE)
 Values
   ('Nxtl- Confirm/Forgot to Scan', 'Nxtl- Confirm/Forgot to Scan', 'Action Taken by Ops', 'kkanuganti', sysdate);
Insert into TRANSP.EVENTLOG_SUBTYPE
   (EVENTSUBTYPE_NAME, DESCRIPTION, EVENTTYPE_ID, CREATEDBY, CROMOD_DATE)
 Values
   ('IVR -  Other', 'Driver called in', 'Action Taken by Ops', 'kkanuganti', sysdate);
Insert into TRANSP.EVENTLOG_SUBTYPE
   (EVENTSUBTYPE_NAME, DESCRIPTION, EVENTTYPE_ID, CREATEDBY, CROMOD_DATE)
 Values
   ('IVR ¿ Reported Missing/Damaged Box', 'Driver Called In', 'Action Taken by Ops', 'kkanuganti', sysdate);
Insert into TRANSP.EVENTLOG_SUBTYPE
   (EVENTSUBTYPE_NAME, DESCRIPTION, EVENTTYPE_ID, CREATEDBY, CROMOD_DATE, MSGGROUP_ID)
 Values
   ('Damaged Mirror', 'Log of incident', 'Accident', 'kkanuganti',sysdate, 
    'Transportartion');
Insert into TRANSP.EVENTLOG_SUBTYPE
   (EVENTSUBTYPE_NAME, DESCRIPTION, EVENTTYPE_ID, CREATEDBY, CROMOD_DATE, MSGGROUP_ID)
 Values
   ('Parked Vehicle', 'Log of incident', 'Accident', 'kkanuganti',sysdate, 
    'Transportartion');
Insert into TRANSP.EVENTLOG_SUBTYPE
   (EVENTSUBTYPE_NAME, DESCRIPTION, EVENTTYPE_ID, CREATEDBY, CROMOD_DATE, MSGGROUP_ID)
 Values
   ('Moving Vehicle', 'Log of incident', 'Accident', 'kkanuganti',sysdate, 
    'Transportartion');
Insert into TRANSP.EVENTLOG_SUBTYPE
   (EVENTSUBTYPE_NAME, DESCRIPTION, EVENTTYPE_ID, CREATEDBY, CROMOD_DATE, MSGGROUP_ID)
 Values
   ('Keys', 'Late dispatch due to keys', 'Late Dispatch', 'kkanuganti',sysdate, 
    'Transportartion');
Insert into TRANSP.EVENTLOG_SUBTYPE
   (EVENTSUBTYPE_NAME, DESCRIPTION, EVENTTYPE_ID, CREATEDBY, CROMOD_DATE, MSGGROUP_ID)
 Values
   ('Plant', 'Late dispatch due to plant', 'Late Dispatch', 'kkanuganti',sysdate, 
    'Transportartion');
Insert into TRANSP.EVENTLOG_SUBTYPE
   (EVENTSUBTYPE_NAME, DESCRIPTION, EVENTTYPE_ID, CREATEDBY, CROMOD_DATE, MSGGROUP_ID)
 Values
   ('Staffing', 'Late dispatch due to staffing', 'Late Dispatch', 'kkanuganti',sysdate, 
    'Transportartion');
Insert into TRANSP.EVENTLOG_SUBTYPE
   (EVENTSUBTYPE_NAME, DESCRIPTION, EVENTTYPE_ID, CREATEDBY, CROMOD_DATE, MSGGROUP_ID)
 Values
   ('Completed', 'Closed Follow-Up', 'Missing Box Follow-U', 'kkanuganti',sysdate, 
    'Transportartion');
Insert into TRANSP.EVENTLOG_SUBTYPE
   (EVENTSUBTYPE_NAME, DESCRIPTION, EVENTTYPE_ID, CREATEDBY, CROMOD_DATE, MSGGROUP_ID)
 Values
   ('Out/Low Fuel', 'Ran out of Fuel', 'Delivery Issues', 'kkanuganti',sysdate, 
    'Transportartion');
Insert into TRANSP.EVENTLOG_SUBTYPE
   (EVENTSUBTYPE_NAME, DESCRIPTION, EVENTTYPE_ID, CREATEDBY, CROMOD_DATE, MSGGROUP_ID)
 Values
   ('Flat Tire', 'Flat Tire', 'Out of Service', 'kkanuganti',sysdate, 
    'Transportartion');
Insert into TRANSP.EVENTLOG_SUBTYPE
   (EVENTSUBTYPE_NAME, DESCRIPTION, EVENTTYPE_ID, CREATEDBY, CROMOD_DATE, MSGGROUP_ID)
 Values
   ('Skin is Torn', 'Skin is Torn', 'Out of Service', 'kkanuganti', sysdate, 
    'Transportartion');
Insert into TRANSP.EVENTLOG_SUBTYPE
   (EVENTSUBTYPE_NAME, DESCRIPTION, EVENTTYPE_ID, CREATEDBY, CROMOD_DATE, MSGGROUP_ID)
 Values
   ('Door Damaged', 'Door Damaged', 'Accident', 'kkanuganti', sysdate, 
    'Transportartion');
Insert into TRANSP.EVENTLOG_SUBTYPE
   (EVENTSUBTYPE_NAME, DESCRIPTION, EVENTTYPE_ID, CREATEDBY, CROMOD_DATE, MSGGROUP_ID)
 Values
   ('Reefer is Not Working', 'Reefer is Not Working', 'Out of Service', 'kkanuganti', sysdate, 
    'Transportartion');
Insert into TRANSP.EVENTLOG_SUBTYPE
   (EVENTSUBTYPE_NAME, DESCRIPTION, EVENTTYPE_ID, CREATEDBY, CROMOD_DATE, MSGGROUP_ID)
 Values
   ('No Specific Informat', 'No Specific Information', 'Out of Service', 'kkanuganti', sysdate, 
    'Transportartion');
Insert into TRANSP.EVENTLOG_SUBTYPE
   (EVENTSUBTYPE_NAME, DESCRIPTION, EVENTTYPE_ID, CREATEDBY, CROMOD_DATE, MSGGROUP_ID)
 Values
   ('Wrong Truck', 'Wrong Truck', 'Late Dispatch', 'kkanuganti', sysdate, 
    'Transportartion');
Insert into TRANSP.EVENTLOG_SUBTYPE
   (EVENTSUBTYPE_NAME, DESCRIPTION, EVENTTYPE_ID, CREATEDBY, CROMOD_DATE, MSGGROUP_ID)
 Values
   ('Brakes are Bad', 'Brakes are Bad', 'Out of Service', 'kkanuganti', sysdate, 
    'Transportartion');
Insert into TRANSP.EVENTLOG_SUBTYPE
   (EVENTSUBTYPE_NAME, DESCRIPTION, EVENTTYPE_ID, CREATEDBY, CROMOD_DATE, MSGGROUP_ID)
 Values
   ('Damaged Phone', 'damaged phone', 'Employee Issues', 'kkanuganti', sysdate, 
    'Transportartion');
Insert into TRANSP.EVENTLOG_SUBTYPE
   (EVENTSUBTYPE_NAME, DESCRIPTION, EVENTTYPE_ID, CREATEDBY, CROMOD_DATE, MSGGROUP_ID)
 Values
   ('attendance', 'Employee arrived late', 'Late Dispatch', 'kkanuganti', sysdate, 
    'Transportartion');
Insert into TRANSP.EVENTLOG_SUBTYPE
   (EVENTSUBTYPE_NAME, DESCRIPTION, EVENTTYPE_ID, CREATEDBY, CROMOD_DATE, MSGGROUP_ID)
 Values
   ('Power Outage', 'Power Outage', 'Pro Foods', 'kkanuganti', sysdate, 
    'Transportartion');
Insert into TRANSP.EVENTLOG_SUBTYPE
   (EVENTSUBTYPE_NAME, DESCRIPTION, EVENTTYPE_ID, CREATEDBY, CROMOD_DATE, MSGGROUP_ID)
 Values
   ('Cracked Windshield', 'Windshield Cracked', 'Accident', 'kkanuganti', sysdate, 
    'Transportartion');
Insert into TRANSP.EVENTLOG_SUBTYPE
   (EVENTSUBTYPE_NAME, DESCRIPTION, EVENTTYPE_ID, CREATEDBY, CROMOD_DATE, MSGGROUP_ID)
 Values
   ('Check Battery', 'Check Battery', 'Out of Service', 'kkanuganti', sysdate, 
    'Transportartion');
Insert into TRANSP.EVENTLOG_SUBTYPE
   (EVENTSUBTYPE_NAME, DESCRIPTION, EVENTTYPE_ID, CREATEDBY, CROMOD_DATE, MSGGROUP_ID)
 Values
   ('Injury', 'Injured delivery personnel', 'Accident', 'kkanuganti', sysdate, 
    'Transportartion');
Insert into TRANSP.EVENTLOG_SUBTYPE
   (EVENTSUBTYPE_NAME, DESCRIPTION, EVENTTYPE_ID, CREATEDBY, CROMOD_DATE, MSGGROUP_ID)
 Values
   ('Refuse Assignment', 'Failure to follow instructions', 'MOT Specials', 'kkanuganti', sysdate, 
    'Transportartion');
Insert into TRANSP.EVENTLOG_SUBTYPE
   (EVENTSUBTYPE_NAME, DESCRIPTION, EVENTTYPE_ID, CREATEDBY, CROMOD_DATE, MSGGROUP_ID)
 Values
   ('Route Count', 'Route Count by Driver', 'Missing Box Follow-U', 'kkanuganti', sysdate, 
    'Transportartion');
Insert into TRANSP.EVENTLOG_SUBTYPE
   (EVENTSUBTYPE_NAME, DESCRIPTION, EVENTTYPE_ID, CREATEDBY, CROMOD_DATE, MSGGROUP_ID)
 Values
   ('Completed', 'Ride Along Completed', 'Ride Along Audit', 'kkanuganti', sysdate, 
    'Transportartion');
Insert into TRANSP.EVENTLOG_SUBTYPE
   (EVENTSUBTYPE_NAME, DESCRIPTION, EVENTTYPE_ID, CREATEDBY, CROMOD_DATE, MSGGROUP_ID)
 Values
   ('Call-Out Personal Day', 'Day Of Call-Out but not sick', 'Employee Issues', 'kkanuganti', sysdate, 
    'Transportartion');
Insert into TRANSP.EVENTLOG_SUBTYPE
   (EVENTSUBTYPE_NAME, DESCRIPTION, EVENTTYPE_ID, CREATEDBY, CROMOD_DATE, MSGGROUP_ID)
 Values
   ('Injury', 'Injury', 'Employee Issues', 'kkanuganti', sysdate, 
    'Transportartion');
Insert into TRANSP.EVENTLOG_SUBTYPE
   (EVENTSUBTYPE_NAME, DESCRIPTION, EVENTTYPE_ID, CREATEDBY, CROMOD_DATE, MSGGROUP_ID)
 Values
   ('Equipment Failure', 'Driver reports equipment issue (i.e.door not', 'TGO Excessive Freezer Door', 'kkanuganti', sysdate, 
    'Transportartion');
Insert into TRANSP.EVENTLOG_SUBTYPE
   (EVENTSUBTYPE_NAME, DESCRIPTION, EVENTTYPE_ID, CREATEDBY, CROMOD_DATE, MSGGROUP_ID)
 Values
   ('Intermec issue', 'phone issues', 'Employee Issues', 'kkanuganti', sysdate, 
    'Transportartion');
Insert into TRANSP.EVENTLOG_SUBTYPE
   (EVENTSUBTYPE_NAME, DESCRIPTION, EVENTTYPE_ID, CREATEDBY, CROMOD_DATE, MSGGROUP_ID)
 Values
   ('Intermec issues', 'Phone issues reported', 'Phone issues', 'kkanuganti', sysdate, 
    'Transportartion');
Insert into TRANSP.EVENTLOG_SUBTYPE
   (EVENTSUBTYPE_NAME, DESCRIPTION, EVENTTYPE_ID, CREATEDBY, CROMOD_DATE, MSGGROUP_ID)
 Values
   ('Bio-Diesel', 'Bio-Diesel Issues', 'Other', 'kkanuganti', sysdate, 
    'Trucks');
Insert into TRANSP.EVENTLOG_SUBTYPE
   (EVENTSUBTYPE_NAME, DESCRIPTION, EVENTTYPE_ID, CREATEDBY, CROMOD_DATE, MSGGROUP_ID)
 Values
   ('Transportation', 'reefer', 'Delivery Issues', 'kkanuganti', sysdate, 
    'Trucks');
Insert into TRANSP.EVENTLOG_SUBTYPE
   (EVENTSUBTYPE_NAME, DESCRIPTION, EVENTTYPE_ID, CREATEDBY, CROMOD_DATE, MSGGROUP_ID)
 Values
   ('Shipping Yard', 'Yard set up', 'yard', 'kkanuganti', sysdate, 
    'Trucks');
Insert into TRANSP.EVENTLOG_SUBTYPE
   (EVENTSUBTYPE_NAME, DESCRIPTION, EVENTTYPE_ID, CREATEDBY, CROMOD_DATE, MSGGROUP_ID)
 Values
   ('Equipment Failure', 'Equipment Failure', 'TGO Manual Deactivation', 'kkanuganti', sysdate, 
    'Trucks');
Insert into TRANSP.EVENTLOG_SUBTYPE
   (EVENTSUBTYPE_NAME, DESCRIPTION, EVENTTYPE_ID, CREATEDBY, CROMOD_DATE, MSGGROUP_ID)
 Values
   ('Mirror', 'Mirror Broken', 'Mechanical Failure and Service', 'kkanuganti', sysdate, 
    'Trucks');
Insert into TRANSP.EVENTLOG_SUBTYPE
   (EVENTSUBTYPE_NAME, DESCRIPTION, EVENTTYPE_ID, CREATEDBY, CROMOD_DATE, MSGGROUP_ID)
 Values
   ('Training', 'Training Class Problems', 'Employee Issues', 'kkanuganti', sysdate, 
    'Jay Narvasa');
Insert into TRANSP.EVENTLOG_SUBTYPE
   (EVENTSUBTYPE_NAME, DESCRIPTION, EVENTTYPE_ID, CREATEDBY, CROMOD_DATE, MSGGROUP_ID)
 Values
   ('Driver Unreachable', 'Driver is not responding', 'Scanning', 'kkanuganti', sysdate, 
    'Field Managers');
Insert into TRANSP.EVENTLOG_SUBTYPE
   (EVENTSUBTYPE_NAME, DESCRIPTION, EVENTTYPE_ID, CREATEDBY, CROMOD_DATE, MSGGROUP_ID)
 Values
   ('Manual Deactivation', 'Reefer Manually Turned Off', 'Telargo Alert', 'kkanuganti', sysdate, 
    'Field Managers');
Insert into TRANSP.EVENTLOG_SUBTYPE
   (EVENTSUBTYPE_NAME, DESCRIPTION, EVENTTYPE_ID, CREATEDBY, CROMOD_DATE, MSGGROUP_ID)
 Values
   ('Pedestrian Request', 'Pedestrian Request', 'TGO Manual Deactivation', 'kkanuganti', sysdate, 
    'Field Managers');
Insert into TRANSP.EVENTLOG_SUBTYPE
   (EVENTSUBTYPE_NAME, DESCRIPTION, EVENTTYPE_ID, CREATEDBY, CROMOD_DATE, MSGGROUP_ID)
 Values
   ('End of Route - No Returns', 'End of Route - No Returns', 'TGO Manual Deactivation', 'kkanuganti', sysdate, 
    'Field Managers');
Insert into TRANSP.EVENTLOG_SUBTYPE
   (EVENTSUBTYPE_NAME, DESCRIPTION, EVENTTYPE_ID, CREATEDBY, CROMOD_DATE, MSGGROUP_ID)
 Values
   ('Driver Not Responding', 'Driver Not Responding', 'TGO Manual Deactivation', 'kkanuganti', sysdate, 
    'Field Managers');
Insert into TRANSP.EVENTLOG_SUBTYPE
   (EVENTSUBTYPE_NAME, DESCRIPTION, EVENTTYPE_ID, CREATEDBY, CROMOD_DATE, MSGGROUP_ID)
 Values
   ('Independent Driver Decision', 'Independent Driver Decision', 'TGO Manual Deactivation', 'kkanuganti', sysdate, 
    'Field Managers');
Insert into TRANSP.EVENTLOG_SUBTYPE
   (EVENTSUBTYPE_NAME, DESCRIPTION, EVENTTYPE_ID, CREATEDBY, CROMOD_DATE, MSGGROUP_ID)
 Values
   ('End of Route - Returns on Truck', 'End of Route - Returns on Truck', 'TGO Manual Deactivation', 'kkanuganti', sysdate, 
    'Field Managers');
Insert into TRANSP.EVENTLOG_SUBTYPE
   (EVENTSUBTYPE_NAME, DESCRIPTION, EVENTTYPE_ID, CREATEDBY, CROMOD_DATE, MSGGROUP_ID)
 Values
   ('Driver Not Responding', 'Driver Not Responding', 'TGO Excessive Back Door', 'kkanuganti', sysdate, 
    'Field Managers');
Insert into TRANSP.EVENTLOG_SUBTYPE
   (EVENTSUBTYPE_NAME, DESCRIPTION, EVENTTYPE_ID, CREATEDBY, CROMOD_DATE, MSGGROUP_ID)
 Values
   ('Off Load - In Field', 'Off Load of Truck that is in the field', 'Delivery Issues', 'kkanuganti', sysdate, 
    'Field Managers');
Insert into TRANSP.EVENTLOG_SUBTYPE
   (EVENTSUBTYPE_NAME, DESCRIPTION, EVENTTYPE_ID, CREATEDBY, CROMOD_DATE, MSGGROUP_ID)
 Values
   ('Improper Truck Seal', 'Improper Truck Seal', 'Delivery Issues', 'kkanuganti', sysdate, 
    'Field Managers');
Insert into TRANSP.EVENTLOG_SUBTYPE
   (EVENTSUBTYPE_NAME, DESCRIPTION, EVENTTYPE_ID, CREATEDBY, CROMOD_DATE, MSGGROUP_ID)
 Values
   ('Wrong Truck Number', 'Yard failed to correct truck number in syste,', 'Late Dispatch', 'kkanuganti', sysdate, 
    'Field Managers');
Insert into TRANSP.EVENTLOG_SUBTYPE
   (EVENTSUBTYPE_NAME, DESCRIPTION, EVENTTYPE_ID, CREATEDBY, CROMOD_DATE, MSGGROUP_ID)
 Values
   ('Depot Truck Inventory Reorganization', 'Depot Truck Inventory Reorganization', 'TGO Excessive Back Door', 'kkanuganti', sysdate, 
    'Field Managers');
Insert into TRANSP.EVENTLOG_SUBTYPE
   (EVENTSUBTYPE_NAME, DESCRIPTION, EVENTTYPE_ID, CREATEDBY, CROMOD_DATE, MSGGROUP_ID)
 Values
   ('Route Truck Inventory Reorganization', 'Route Truck Inventory Reorganization', 'TGO Excessive Back Door', 'kkanuganti', sysdate, 
    'Field Managers');
Insert into TRANSP.EVENTLOG_SUBTYPE
   (EVENTSUBTYPE_NAME, DESCRIPTION, EVENTTYPE_ID, CREATEDBY, CROMOD_DATE, MSGGROUP_ID)
 Values
   ('Depot Offloading to Runners', 'Depot Offloading to Runners', 'TGO Excessive Back Door', 'kkanuganti', sysdate, 
    'Field Managers');
Insert into TRANSP.EVENTLOG_SUBTYPE
   (EVENTSUBTYPE_NAME, DESCRIPTION, EVENTTYPE_ID, CREATEDBY, CROMOD_DATE, MSGGROUP_ID)
 Values
   ('Depot Truck Reorganization', 'Depot Truck Reorganization', 'TGO Excessive Freezer Door', 'kkanuganti', sysdate, 
    'Field Managers');
Insert into TRANSP.EVENTLOG_SUBTYPE
   (EVENTSUBTYPE_NAME, DESCRIPTION, EVENTTYPE_ID, CREATEDBY, CROMOD_DATE, MSGGROUP_ID)
 Values
   ('Freezer Door Does Not Close', 'Freezer Door Does Not Close', 'TGO Excessive Freezer Door', 'kkanuganti', sysdate, 
    'Field Managers');
Insert into TRANSP.EVENTLOG_SUBTYPE
   (EVENTSUBTYPE_NAME, DESCRIPTION, EVENTTYPE_ID, CREATEDBY, CROMOD_DATE, MSGGROUP_ID)
 Values
   ('Driver Not Responding', 'Driver Not Responding', 'TGO Excessive Freezer Door', 'kkanuganti', sysdate, 
    'Field Managers');
Insert into TRANSP.EVENTLOG_SUBTYPE
   (EVENTSUBTYPE_NAME, DESCRIPTION, EVENTTYPE_ID, CREATEDBY, CROMOD_DATE, MSGGROUP_ID)
 Values
   ('Driver Not Responding', 'OPS Center Agent has sent a minimum of 2 Next', 'TGO-Door Open While Driving', 'kkanuganti', sysdate, 
    'Field Managers');
Insert into TRANSP.EVENTLOG_SUBTYPE
   (EVENTSUBTYPE_NAME, DESCRIPTION, EVENTTYPE_ID, CREATEDBY, CROMOD_DATE, MSGGROUP_ID)
 Values
   ('Independent Driver Decision', 'Driver is unaware', 'TGO-Door Open While Driving', 'kkanuganti', sysdate, 
    'Field Managers');
Insert into TRANSP.EVENTLOG_SUBTYPE
   (EVENTSUBTYPE_NAME, DESCRIPTION, EVENTTYPE_ID, CREATEDBY, CROMOD_DATE, MSGGROUP_ID)
 Values
   ('Driver Not Responding', 'OPS Center Agent has sent a minimum of 2 Next', 'TGO DPF Light', 'kkanuganti', sysdate, 
    'Field Managers');
Insert into TRANSP.EVENTLOG_SUBTYPE
   (EVENTSUBTYPE_NAME, DESCRIPTION, EVENTTYPE_ID, CREATEDBY, CROMOD_DATE, MSGGROUP_ID)
 Values
   ('Driver Not Responding', 'OPS Center Agent has sent a minimum of 2 Next', 'TGO Emergency Brake', 'kkanuganti', sysdate, 
    'Field Managers');
Insert into TRANSP.EVENTLOG_SUBTYPE
   (EVENTSUBTYPE_NAME, DESCRIPTION, EVENTTYPE_ID, CREATEDBY, CROMOD_DATE, MSGGROUP_ID)
 Values
   ('Independent Driver Decision', 'Driver is unaware that his Emergency Brake is', 'TGO Emergency Brake', 'kkanuganti', sysdate, 
    'Field Managers');
Insert into TRANSP.EVENTLOG_SUBTYPE
   (EVENTSUBTYPE_NAME, DESCRIPTION, EVENTTYPE_ID, CREATEDBY, CROMOD_DATE, MSGGROUP_ID)
 Values
   ('Independent Driver Decision', 'Driver reports that he isnt aware of the door', 'TGO Excessive Back Door', 'kkanuganti', sysdate, 
    'Field Managers');
Insert into TRANSP.EVENTLOG_SUBTYPE
   (EVENTSUBTYPE_NAME, DESCRIPTION, EVENTTYPE_ID, CREATEDBY, CROMOD_DATE, MSGGROUP_ID)
 Values
   ('Call-In', 'Emplyoee Called in Absent', 'Employee Issues', 'kkanuganti',sysdate, 
    'Transportartion');
Insert into TRANSP.EVENTLOG_SUBTYPE
   (EVENTSUBTYPE_NAME, DESCRIPTION, EVENTTYPE_ID, CREATEDBY, CROMOD_DATE, MSGGROUP_ID)
 Values
   ('Independent Driver Decision', 'Driver reports that he isnt aware of the door', 'TGO Excessive Freezer Door', 'kkanuganti', sysdate, 
    'Field Managers');
Insert into TRANSP.EVENTLOG_SUBTYPE
   (EVENTSUBTYPE_NAME, DESCRIPTION, EVENTTYPE_ID, CREATEDBY, CROMOD_DATE, MSGGROUP_ID)
 Values
   ('Dead Battery', 'Dead Battery', 'Mechanical Failure and Service', 'kkanuganti', sysdate, 
    'Returns Center');
Insert into TRANSP.EVENTLOG_SUBTYPE
   (EVENTSUBTYPE_NAME, DESCRIPTION, EVENTTYPE_ID, CREATEDBY, CROMOD_DATE, MSGGROUP_ID)
 Values
   ('Flat Tire', 'Flat Tire', 'Mechanical Failure and Service', 'kkanuganti', sysdate, 
    'Returns Center');
Insert into TRANSP.EVENTLOG_SUBTYPE
   (EVENTSUBTYPE_NAME, DESCRIPTION, EVENTTYPE_ID, CREATEDBY, CROMOD_DATE, MSGGROUP_ID)
 Values
   ('Flaps', 'Flaps are cut', 'Mechanical Failure and Service', 'kkanuganti', sysdate, 
    'Returns Center');
Insert into TRANSP.EVENTLOG_SUBTYPE
   (EVENTSUBTYPE_NAME, DESCRIPTION, EVENTTYPE_ID, CREATEDBY, CROMOD_DATE, MSGGROUP_ID)
 Values
   ('Freezer Door', 'Freezer Door', 'Mechanical Failure and Service', 'kkanuganti', sysdate, 
    'Returns Center');
Insert into TRANSP.EVENTLOG_SUBTYPE
   (EVENTSUBTYPE_NAME, DESCRIPTION, EVENTTYPE_ID, CREATEDBY, CROMOD_DATE, MSGGROUP_ID)
 Values
   ('Reefer', 'Reefer Issues', 'Mechanical Failure and Service', 'kkanuganti', sysdate, 
    'Returns Center');
Insert into TRANSP.EVENTLOG_SUBTYPE
   (EVENTSUBTYPE_NAME, DESCRIPTION, EVENTTYPE_ID, CREATEDBY, CROMOD_DATE, MSGGROUP_ID)
 Values
   ('Truck Towed', 'Towed', 'Mechanical Failure and Service', 'kkanuganti', sysdate, 
    'Returns Center');
Insert into TRANSP.EVENTLOG_SUBTYPE
   (EVENTSUBTYPE_NAME, DESCRIPTION, EVENTTYPE_ID, CREATEDBY, CROMOD_DATE, MSGGROUP_ID)
 Values
   ('Stalling', 'Truck turned off / smoking', 'Mechanical Failure and Service', 'kkanuganti', sysdate, 
    'Returns Center');
Insert into TRANSP.EVENTLOG_SUBTYPE
   (EVENTSUBTYPE_NAME, DESCRIPTION, EVENTTYPE_ID, CREATEDBY, CROMOD_DATE, MSGGROUP_ID)
 Values
   ('Lights', 'Lights are out', 'Mechanical Failure and Service', 'kkanuganti', sysdate, 
    'Returns Center');
Insert into TRANSP.EVENTLOG_SUBTYPE
   (EVENTSUBTYPE_NAME, DESCRIPTION, EVENTTYPE_ID, CREATEDBY, CROMOD_DATE, MSGGROUP_ID)
 Values
   ('Transmission Issues', 'Transmission Issues', 'Mechanical Failure and Service', 'kkanuganti', sysdate, 
    'Returns Center');
Insert into TRANSP.EVENTLOG_SUBTYPE
   (EVENTSUBTYPE_NAME, DESCRIPTION, EVENTTYPE_ID, CREATEDBY, CROMOD_DATE, MSGGROUP_ID)
 Values
   ('Door Damaged', 'door will not close', 'Mechanical Failure and Service', 'kkanuganti', sysdate, 
    'Returns Center');
Insert into TRANSP.EVENTLOG_SUBTYPE
   (EVENTSUBTYPE_NAME, DESCRIPTION, EVENTTYPE_ID, CREATEDBY, CROMOD_DATE, MSGGROUP_ID)
 Values
   ('Shelving Issue', 'Shelves need to be replaced', 'Mechanical Failure and Service', 'kkanuganti', sysdate, 
    'Returns Center');
Insert into TRANSP.EVENTLOG_SUBTYPE
   (EVENTSUBTYPE_NAME, DESCRIPTION, EVENTTYPE_ID, CREATEDBY, CROMOD_DATE, MSGGROUP_ID)
 Values
   ('Battery Inspection', 'Battery Inspection', 'Mechanical Failure and Service', 'kkanuganti', sysdate, 
    'Returns Center');
Insert into TRANSP.EVENTLOG_SUBTYPE
   (EVENTSUBTYPE_NAME, DESCRIPTION, EVENTTYPE_ID, CREATEDBY, CROMOD_DATE, MSGGROUP_ID)
 Values
   ('Bio-Diesel', 'Bio-Diesel', 'Mechanical Failure and Service', 'kkanuganti', TO_DATE('11/28/2012 09:53:44', 'MM/DD/YYYY HH24:MI:SS'), 
    'Returns Center');
Insert into TRANSP.EVENTLOG_SUBTYPE
   (EVENTSUBTYPE_NAME, DESCRIPTION, EVENTTYPE_ID, CREATEDBY, CROMOD_DATE, MSGGROUP_ID)
 Values
   ('Engine', 'Engine', 'Mechanical Failure and Service', 'kkanuganti', sysdate, 
    'Returns Center');
Insert into TRANSP.EVENTLOG_SUBTYPE
   (EVENTSUBTYPE_NAME, DESCRIPTION, EVENTTYPE_ID, CREATEDBY, CROMOD_DATE, MSGGROUP_ID)
 Values
   ('Regular Maintenance', 'Regular Maintenance', 'Mechanical Failure and Service', 'kkanuganti', sysdate, 
    'Returns Center');
Insert into TRANSP.EVENTLOG_SUBTYPE
   (EVENTSUBTYPE_NAME, DESCRIPTION, EVENTTYPE_ID, CREATEDBY, CROMOD_DATE, MSGGROUP_ID)
 Values
   ('Skin', 'Skin', 'Mechanical Failure and Service', 'kkanuganti', sysdate, 
    'Returns Center');
Insert into TRANSP.EVENTLOG_SUBTYPE
   (EVENTSUBTYPE_NAME, DESCRIPTION, EVENTTYPE_ID, CREATEDBY, CROMOD_DATE)
 Values
   ('Door Closed', 'While taking action on the alert, event value', 'TGO-Door Open While Driving', 'kkanuganti', sysdate);
Insert into TRANSP.EVENTLOG_SUBTYPE
   (EVENTSUBTYPE_NAME, DESCRIPTION, EVENTTYPE_ID, CREATEDBY, CROMOD_DATE)
 Values
   ('Cust Call Requested/IVR Issue', 'Cust Call Requested/IVR Issue', 'Nextel Inbound Log', 'kkanuganti', sysdate);
Insert into TRANSP.EVENTLOG_SUBTYPE
   (EVENTSUBTYPE_NAME, DESCRIPTION, EVENTTYPE_ID, CREATEDBY, CROMOD_DATE)
 Values
   ('Scanner Issue', 'Scanner Issue', 'Nextel Inbound Log', 'kkanuganti', sysdate);
Insert into TRANSP.EVENTLOG_SUBTYPE
   (EVENTSUBTYPE_NAME, DESCRIPTION, EVENTTYPE_ID, CREATEDBY, CROMOD_DATE)
 Values
   ('Other', 'Other', 'Nextel Inbound Log', 'kkanuganti', sysdate);
Insert into TRANSP.EVENTLOG_SUBTYPE
   (EVENTSUBTYPE_NAME, DESCRIPTION, EVENTTYPE_ID, CREATEDBY, CROMOD_DATE)
 Values
   ('Needs Partners Number', 'Needs Partners Number', 'Nextel Inbound Log', 'kkanuganti', sysdate);
Insert into TRANSP.EVENTLOG_SUBTYPE
   (EVENTSUBTYPE_NAME, DESCRIPTION, EVENTTYPE_ID, CREATEDBY, CROMOD_DATE)
 Values
   ('2nd Attempt Completed', '2nd Attempt Completed', 'Nextel Inbound Log', 'kkanuganti', sysdate);
Insert into TRANSP.EVENTLOG_SUBTYPE
   (EVENTSUBTYPE_NAME, DESCRIPTION, EVENTTYPE_ID, CREATEDBY, CROMOD_DATE)
 Values
   ('Confirm/Forgot to scan', 'Confirm/Forgot to scan', 'Nextel Inbound Log', 'kkanuganti', sysdate);
Insert into TRANSP.EVENTLOG_SUBTYPE
   (EVENTSUBTYPE_NAME, DESCRIPTION, EVENTTYPE_ID, CREATEDBY, CROMOD_DATE)
 Values
   ('Needs Directions/Address', 'Needs Directions/Address', 'Nextel Inbound Log', 'kkanuganti', sysdate);
Insert into TRANSP.EVENTLOG_SUBTYPE
   (EVENTSUBTYPE_NAME, DESCRIPTION, EVENTTYPE_ID, CREATEDBY, CROMOD_DATE, MSGGROUP_ID)
 Values
   ('Lock', 'Lock', 'Mechanical Failure and Service', 'kkanuganti', sysdate, 
    'Returns Center');
Insert into TRANSP.EVENTLOG_SUBTYPE
   (EVENTSUBTYPE_NAME, DESCRIPTION, EVENTTYPE_ID, CREATEDBY, CROMOD_DATE, MSGGROUP_ID)
 Values
   ('Other', 'Other', 'Mechanical Failure and Service', 'kkanuganti', sysdate, 
    'Returns Center');
Insert into TRANSP.EVENTLOG_SUBTYPE
   (EVENTSUBTYPE_NAME, DESCRIPTION, EVENTTYPE_ID, CREATEDBY, CROMOD_DATE, MSGGROUP_ID)
 Values
   ('Lost/damaged key', 'Key was lost or damaged', 'Mechanical Failure and Service', 'kkanuganti', sysdate, 
    'Returns Center');
Insert into TRANSP.EVENTLOG_SUBTYPE
   (EVENTSUBTYPE_NAME, DESCRIPTION, EVENTTYPE_ID, CREATEDBY, CROMOD_DATE, MSGGROUP_ID)
 Values
   ('Windshield wipers', 'Wipers not working', 'Mechanical Failure and Service', 'kkanuganti', sysdate, 
    'Returns Center');
Insert into TRANSP.EVENTLOG_SUBTYPE
   (EVENTSUBTYPE_NAME, DESCRIPTION, EVENTTYPE_ID, CREATEDBY, CROMOD_DATE, MSGGROUP_ID)
 Values
   ('GPS System Failure', 'GPS System Failure', 'Mechanical Failure and Service', 'kkanuganti', sysdate, 
    'Returns Center');
Insert into TRANSP.EVENTLOG_SUBTYPE
   (EVENTSUBTYPE_NAME, DESCRIPTION, EVENTTYPE_ID, CREATEDBY, CROMOD_DATE, MSGGROUP_ID)
 Values
   ('Misload - Duplicate Box', 'Misload - Duplicate Box', 'Carton Issues', 'kkanuganti', sysdate, 
    'Returns Center');
Insert into TRANSP.EVENTLOG_SUBTYPE
   (EVENTSUBTYPE_NAME, DESCRIPTION, EVENTTYPE_ID, CREATEDBY, CROMOD_DATE, MSGGROUP_ID)
 Values
   ('Telargo System Problem', 'Telargo System Problem', 'TGO Excessive Back Door', 'kkanuganti', sysdate, 
    'Katie Whalen');
Insert into TRANSP.EVENTLOG_SUBTYPE
   (EVENTSUBTYPE_NAME, DESCRIPTION, EVENTTYPE_ID, CREATEDBY, CROMOD_DATE, MSGGROUP_ID)
 Values
   ('Telargo System Problem', 'Telargo System Problem', 'TGO Excessive Freezer Door', 'kkanuganti', sysdate, 
    'Katie Whalen');
Insert into TRANSP.EVENTLOG_SUBTYPE
   (EVENTSUBTYPE_NAME, DESCRIPTION, EVENTTYPE_ID, CREATEDBY, CROMOD_DATE, MSGGROUP_ID)
 Values
   ('Telargo System Problem', 'Examples include: Event: is blank or no alert', 'TGO-Door Open While Driving', 'kkanuganti', sysdate, 
    'Katie Whalen');
Insert into TRANSP.EVENTLOG_SUBTYPE
   (EVENTSUBTYPE_NAME, DESCRIPTION, EVENTTYPE_ID, CREATEDBY, CROMOD_DATE, MSGGROUP_ID)
 Values
   ('Telargo System Problem', 'TBD', 'TGO DPF Light', 'kkanuganti', sysdate, 
    'Katie Whalen');
Insert into TRANSP.EVENTLOG_SUBTYPE
   (EVENTSUBTYPE_NAME, DESCRIPTION, EVENTTYPE_ID, CREATEDBY, CROMOD_DATE, MSGGROUP_ID)
 Values
   ('Telargo System Problem', 'Examples include: Event: is blank or no alert', 'TGO Emergency Brake', 'kkanuganti', sysdate, 
    'Katie Whalen');
Insert into TRANSP.EVENTLOG_SUBTYPE
   (EVENTSUBTYPE_NAME, DESCRIPTION, EVENTTYPE_ID, CREATEDBY, CROMOD_DATE, MSGGROUP_ID)
 Values
   ('Other', 'Other', 'TGO Excessive Back Door', 'kkanuganti', sysdate, 
    'Ops Management');
Insert into TRANSP.EVENTLOG_SUBTYPE
   (EVENTSUBTYPE_NAME, DESCRIPTION, EVENTTYPE_ID, CREATEDBY, CROMOD_DATE, MSGGROUP_ID)
 Values
   ('Reported Issues', 'Reported Issues', 'Ops Supervision', 'kkanuganti', sysdate, 
    'Ops Management');
Insert into TRANSP.EVENTLOG_SUBTYPE
   (EVENTSUBTYPE_NAME, DESCRIPTION, EVENTTYPE_ID, CREATEDBY, CROMOD_DATE, MSGGROUP_ID)
 Values
   ('On Premise 12AM - 5AM', 'On Premise 12AM - 5AM', 'TGO Manual Deactivation', 'kkanuganti', sysdate, 
    'Transportation Shipping Yard');
Insert into TRANSP.EVENTLOG_SUBTYPE
   (EVENTSUBTYPE_NAME, DESCRIPTION, EVENTTYPE_ID, CREATEDBY, CROMOD_DATE, MSGGROUP_ID)
 Values
   ('On Premises 12AM-5AM', 'On Premises 12AM-5AM', 'TGO Excessive Back Door', 'kkanuganti', sysdate, 
    'Transportation Shipping Yard');
Insert into TRANSP.EVENTLOG_SUBTYPE
   (EVENTSUBTYPE_NAME, DESCRIPTION, EVENTTYPE_ID, CREATEDBY, CROMOD_DATE, MSGGROUP_ID)
 Values
   ('On Premises 12AM-5AM', 'On Premises 12AM-5AM', 'TGO Excessive Freezer Door', 'kkanuganti', sysdate, 
    'Transportation Shipping Yard');
Insert into TRANSP.EVENTLOG_SUBTYPE
   (EVENTSUBTYPE_NAME, DESCRIPTION, EVENTTYPE_ID, CREATEDBY, CROMOD_DATE, MSGGROUP_ID)
 Values
   ('Independent Driver Decision', 'Driver was advised to go through the regenera', 'TGO DPF Light', 'kkanuganti', sysdate, 
    'Transportation Shipping Yard');
Insert into TRANSP.EVENTLOG_SUBTYPE
   (EVENTSUBTYPE_NAME, DESCRIPTION, EVENTTYPE_ID, CREATEDBY, CROMOD_DATE, MSGGROUP_ID)
 Values
   ('Equipment Failure', 'Emergency Brake is engaged and truck is out o', 'TGO Emergency Brake', 'kkanuganti', sysdate, 
    'Transportation Shipping Yard');
Insert into TRANSP.EVENTLOG_SUBTYPE
   (EVENTSUBTYPE_NAME, DESCRIPTION, EVENTTYPE_ID, CREATEDBY, CROMOD_DATE, MSGGROUP_ID)
 Values
   ('Telargo Equipment Failure', 'Sensor Issue open/close within a short period', 'TGO Excessive Freezer Door', 'kkanuganti', sysdate, 
    'Transportation Shipping Yard');
Insert into TRANSP.EVENTLOG_SUBTYPE
   (EVENTSUBTYPE_NAME, DESCRIPTION, EVENTTYPE_ID, CREATEDBY, CROMOD_DATE, MSGGROUP_ID)
 Values
   ('Route Not Found', 'Route Not Found', 'TGO Manual Deactivation', 'kkanuganti', sysdate, 
    'George Walker');
Insert into TRANSP.EVENTLOG_SUBTYPE
   (EVENTSUBTYPE_NAME, DESCRIPTION, EVENTTYPE_ID, CREATEDBY, CROMOD_DATE, MSGGROUP_ID)
 Values
   ('Route Not Found', 'Route Not Found', 'TGO Excessive Back Door', 'kkanuganti', sysdate, 
    'George Walker');
Insert into TRANSP.EVENTLOG_SUBTYPE
   (EVENTSUBTYPE_NAME, DESCRIPTION, EVENTTYPE_ID, CREATEDBY, CROMOD_DATE, MSGGROUP_ID)
 Values
   ('Route Not Found', 'Route Not Found', 'TGO Excessive Freezer Door', 'kkanuganti', sysdate, 
    'George Walker');
Insert into TRANSP.EVENTLOG_SUBTYPE
   (EVENTSUBTYPE_NAME, DESCRIPTION, EVENTTYPE_ID, CREATEDBY, CROMOD_DATE, MSGGROUP_ID)
 Values
   ('A   15 Min Prior to Dispatch', 'A   15 Min Prior to Dispatch', 'AA Late Box Feedback', 'kkanuganti', sysdate, 
    'Ops Management Alerts');
Insert into TRANSP.EVENTLOG_SUBTYPE
   (EVENTSUBTYPE_NAME, DESCRIPTION, EVENTTYPE_ID, CREATEDBY, CROMOD_DATE, MSGGROUP_ID)
 Values
   ('B   At Dispatch', 'B   At Dispatch', 'AA Late Box Feedback', 'kkanuganti', sysdate, 
    'Ops Management Alerts');
Insert into TRANSP.EVENTLOG_SUBTYPE
   (EVENTSUBTYPE_NAME, DESCRIPTION, EVENTTYPE_ID, CREATEDBY, CROMOD_DATE, MSGGROUP_ID)
 Values
   ('C    After Dispatch Exception', 'C    After Dispatch Exception', 'AA Late Box Feedback', 'kkanuganti', sysdate, 
    'Ops Management Alerts');
Insert into TRANSP.EVENTLOG_SUBTYPE
   (EVENTSUBTYPE_NAME, DESCRIPTION, EVENTTYPE_ID, CREATEDBY, CROMOD_DATE, MSGGROUP_ID)
 Values
   ('Other', 'Any reason that does not fall within any of t', 'TGO-Door Open While Driving', 'kkanuganti', sysdate, 
    'Ops Management Alerts');
Insert into TRANSP.EVENTLOG_SUBTYPE
   (EVENTSUBTYPE_NAME, DESCRIPTION, EVENTTYPE_ID, CREATEDBY, CROMOD_DATE, MSGGROUP_ID)
 Values
   ('Other', 'Any reason that does not fall within any of t', 'TGO DPF Light', 'kkanuganti', sysdate, 
    'Ops Management Alerts');
Insert into TRANSP.EVENTLOG_SUBTYPE
   (EVENTSUBTYPE_NAME, DESCRIPTION, EVENTTYPE_ID, CREATEDBY, CROMOD_DATE, MSGGROUP_ID)
 Values
   ('Other', 'Any reason that does not fall within any of t', 'TGO Emergency Brake', 'kkanuganti', sysdate, 
    'Ops Management Alerts');
Insert into TRANSP.EVENTLOG_SUBTYPE
   (EVENTSUBTYPE_NAME, DESCRIPTION, EVENTTYPE_ID, CREATEDBY, CROMOD_DATE, MSGGROUP_ID)
 Values
   ('D Shift Recap', 'End of Shift Recap', 'AA Late Box Feedback', 'kkanuganti', sysdate, 
    'Ops Management Alerts');
Insert into TRANSP.EVENTLOG_SUBTYPE
   (EVENTSUBTYPE_NAME, DESCRIPTION, EVENTTYPE_ID, CREATEDBY, CROMOD_DATE, MSGGROUP_ID)
 Values
   ('Truck Unassigned/Not Dispatched', 'Truck number has not been assigned to a Route', 'TGO-Door Open While Driving', 'kkanuganti', sysdate, 
    'Transportation Admins');
Insert into TRANSP.EVENTLOG_SUBTYPE
   (EVENTSUBTYPE_NAME, DESCRIPTION, EVENTTYPE_ID, CREATEDBY, CROMOD_DATE, MSGGROUP_ID)
 Values
   ('Incomplete FireTruck-IFS Handoff', 'Incomplete FireTruck-IFS Handoff', 'MOT Specials', 'kkanuganti', sysdate, 
    'Fire Truck Management');
Insert into TRANSP.EVENTLOG_SUBTYPE
   (EVENTSUBTYPE_NAME, DESCRIPTION, EVENTTYPE_ID, CREATEDBY, CROMOD_DATE, MSGGROUP_ID)
 Values
   ('Telargo Equipment Failure', 'Back Door Event has no "Close Values"', 'TGO-Door Open While Driving', 'kkanuganti', sysdate, 
    'Katie Whalen');
Insert into TRANSP.EVENTLOG_SUBTYPE
   (EVENTSUBTYPE_NAME, DESCRIPTION, EVENTTYPE_ID, CREATEDBY, CROMOD_DATE)
 Values
   ('Check In', 'Check In', 'Muni Meter Card', 'kkanuganti', sysdate);
Insert into TRANSP.EVENTLOG_SUBTYPE
   (EVENTSUBTYPE_NAME, DESCRIPTION, EVENTTYPE_ID, CREATEDBY, CROMOD_DATE, MSGGROUP_ID)
 Values
   ('Work Performance', 'Poor work performance', 'Employee Issues', 'kkanuganti',sysdate, 'Transportartion');
Insert into TRANSP.EVENTLOG_SUBTYPE
   (EVENTSUBTYPE_NAME, DESCRIPTION, EVENTTYPE_ID, CREATEDBY, CROMOD_DATE, MSGGROUP_ID)
 Values
   ('Malfunction Punch Clock', 'System Malfunction', 'Kronos', 'kkanuganti', sysdate, 
    'Transportartion');
Insert into TRANSP.EVENTLOG_SUBTYPE
   (EVENTSUBTYPE_NAME, DESCRIPTION, EVENTTYPE_ID, CREATEDBY, CROMOD_DATE, MSGGROUP_ID)
 Values
   ('Mechanical Issue', 'Mechanical Issue', 'MOT Assistance', 'kkanuganti', sysdate, 
    'Trucks');
Insert into TRANSP.EVENTLOG_SUBTYPE
   (EVENTSUBTYPE_NAME, DESCRIPTION, EVENTTYPE_ID, CREATEDBY, CROMOD_DATE, MSGGROUP_ID)
 Values
   ('Off Load - At Facility', 'Off Load that occurs at the facility (prior', 'Delivery Issues', 'kkanuganti', sysdate, 
    'Field Managers');
Insert into TRANSP.EVENTLOG_SUBTYPE
   (EVENTSUBTYPE_NAME, DESCRIPTION, EVENTTYPE_ID, CREATEDBY, CROMOD_DATE, MSGGROUP_ID)
 Values
   ('Equipment Failure', 'Equipment Failure', 'TGO Excessive Back Door', 'kkanuganti', sysdate, 
    'Field Managers');
Insert into TRANSP.EVENTLOG_SUBTYPE
   (EVENTSUBTYPE_NAME, DESCRIPTION, EVENTTYPE_ID, CREATEDBY, CROMOD_DATE, MSGGROUP_ID)
 Values
   ('Route Truck Inventory Reorganization', 'Route Truck Inventory Reorganization', 'TGO Excessive Freezer Door', 'kkanuganti', sysdate, 
    'Field Managers');
Insert into TRANSP.EVENTLOG_SUBTYPE
   (EVENTSUBTYPE_NAME, DESCRIPTION, EVENTTYPE_ID, CREATEDBY, CROMOD_DATE, MSGGROUP_ID)
 Values
   ('Brake', 'Brake light', 'Mechanical Failure and Service', 'kkanuganti', sysdate, 
    'Returns Center');
Insert into TRANSP.EVENTLOG_SUBTYPE
   (EVENTSUBTYPE_NAME, DESCRIPTION, EVENTTYPE_ID, CREATEDBY, CROMOD_DATE)
 Values
   ('EB On & Ignition Off at the base', 'Emergency Brake is ON, and the truck ignition', 'TGO Emergency Brake', 'kkanuganti', sysdate);
Insert into TRANSP.EVENTLOG_SUBTYPE
   (EVENTSUBTYPE_NAME, DESCRIPTION, EVENTTYPE_ID, CREATEDBY, CROMOD_DATE)
 Values
   ('Missing/Damaged/Found Carton', 'Missing/Damaged/Found Carton', 'Nextel Inbound Log', 'kkanuganti', sysdate);
Insert into TRANSP.EVENTLOG_SUBTYPE
   (EVENTSUBTYPE_NAME, DESCRIPTION, EVENTTYPE_ID, CREATEDBY, CROMOD_DATE, MSGGROUP_ID)
 Values
   ('Telargo System Problem', 'Telargo System Problem', 'TGO Manual Deactivation', 'kkanuganti', sysdate, 
    'Katie Whalen');
Insert into TRANSP.EVENTLOG_SUBTYPE
   (EVENTSUBTYPE_NAME, DESCRIPTION, EVENTTYPE_ID, CREATEDBY, CROMOD_DATE, MSGGROUP_ID)
 Values
   ('Telargo Equipment Failure', 'Telargo Sensor isnt reading open/close prope', 'TGO Excessive Back Door', 'kkanuganti', sysdate, 
    'Transportation Shipping Yard');
Insert into TRANSP.EVENTLOG_SUBTYPE
   (EVENTSUBTYPE_NAME, DESCRIPTION, EVENTTYPE_ID, CREATEDBY, CROMOD_DATE, MSGGROUP_ID)
 Values
   ('Customer Call Feature Failed', 'Customer Call Feature Failed', 'Action Taken by Ops', 'kkanuganti', sysdate, 
    'Ops Management Alerts');
Insert into TRANSP.EVENTLOG_SUBTYPE
   (EVENTSUBTYPE_NAME, DESCRIPTION, EVENTTYPE_ID, CREATEDBY, CROMOD_DATE)
 Values
   ('Nxtl- Needs Partners Number', 'Nxtl- Needs Partners Number', 'Action Taken by Ops', 'kkanuganti', sysdate);
Insert into TRANSP.EVENTLOG_SUBTYPE
   (EVENTSUBTYPE_NAME, DESCRIPTION, EVENTTYPE_ID, CREATEDBY, CROMOD_DATE, MSGGROUP_ID)
 Values
   ('Fraud Found', 'Fraud Found', 'Fraud Review', 'kkanuganti', sysdate, 
    'CSG Senior Staff');
Insert into TRANSP.EVENTLOG_SUBTYPE
   (EVENTSUBTYPE_NAME, DESCRIPTION, EVENTTYPE_ID, CREATEDBY, CROMOD_DATE, MSGGROUP_ID)
 Values
   ('Misload - Not Reported', 'Misload - Not Reported', 'Carton Issues', 'kkanuganti', sysdate, 
    'Returns Center');
Insert into TRANSP.EVENTLOG_SUBTYPE
   (EVENTSUBTYPE_NAME, DESCRIPTION, EVENTTYPE_ID, CREATEDBY, CROMOD_DATE)
 Values
   ('Open', 'Open Follow-Up', 'Missing Box Follow-U', 'kkanuganti', sysdate);
Insert into TRANSP.EVENTLOG_SUBTYPE
   (EVENTSUBTYPE_NAME, DESCRIPTION, EVENTTYPE_ID, CREATEDBY, CROMOD_DATE)
 Values
   ('Freeze', 'Phones Freezes', 'Scanning', 'kkanuganti', sysdate);
Insert into TRANSP.EVENTLOG_SUBTYPE
   (EVENTSUBTYPE_NAME, DESCRIPTION, EVENTTYPE_ID, CREATEDBY, CROMOD_DATE)
 Values
   ('Battery Low', 'Low Battery', 'Scanning', 'kkanuganti', sysdate);
Insert into TRANSP.EVENTLOG_SUBTYPE
   (EVENTSUBTYPE_NAME, DESCRIPTION, EVENTTYPE_ID, CREATEDBY, CROMOD_DATE)
 Values
   ('Helper Unreachable', 'Helper is not responding', 'Scanning', 'kkanuganti', sysdate);
Insert into TRANSP.EVENTLOG_SUBTYPE
   (EVENTSUBTYPE_NAME, DESCRIPTION, EVENTTYPE_ID, CREATEDBY, CROMOD_DATE)
 Values
   ('Training', 'Temp Employee is not scanning', 'Scanning', 'kkanuganti', sysdate);
Insert into TRANSP.EVENTLOG_SUBTYPE
   (EVENTSUBTYPE_NAME, DESCRIPTION, EVENTTYPE_ID, CREATEDBY, CROMOD_DATE)
 Values
   ('Laser Problems', 'Laser is Weak/Slow', 'Scanning', 'kkanuganti', sysdate);
Insert into TRANSP.EVENTLOG_SUBTYPE
   (EVENTSUBTYPE_NAME, DESCRIPTION, EVENTTYPE_ID, CREATEDBY, CROMOD_DATE)
 Values
   ('Not Scanning', 'Running Late/Refusal to Scan', 'Scanning', 'kkanuganti', sysdate);
Insert into TRANSP.EVENTLOG_SUBTYPE
   (EVENTSUBTYPE_NAME, DESCRIPTION, EVENTTYPE_ID, CREATEDBY, CROMOD_DATE)
 Values
   ('No Equipment', 'No Equipment to Scan', 'Scanning', 'kkanuganti', sysdate);
Insert into TRANSP.EVENTLOG_SUBTYPE
   (EVENTSUBTYPE_NAME, DESCRIPTION, EVENTTYPE_ID, CREATEDBY, CROMOD_DATE)
 Values
   ('Error - Comm Error', 'Comm Error on Phone', 'Scanning', 'kkanuganti', sysdate);
Insert into TRANSP.EVENTLOG_SUBTYPE
   (EVENTSUBTYPE_NAME, DESCRIPTION, EVENTTYPE_ID, CREATEDBY, CROMOD_DATE)
 Values
   ('Tardy', 'Phone Call to Report Late to Work', 'Employee Issues', 'kkanuganti', sysdate);
Insert into TRANSP.EVENTLOG_SUBTYPE
   (EVENTSUBTYPE_NAME, DESCRIPTION, EVENTTYPE_ID, CREATEDBY, CROMOD_DATE)
 Values
   ('Building Access', 'Delayed', 'Delivery Issues', 'kkanuganti', sysdate);
Insert into TRANSP.EVENTLOG_SUBTYPE
   (EVENTSUBTYPE_NAME, DESCRIPTION, EVENTTYPE_ID, CREATEDBY, CROMOD_DATE)
 Values
   ('Combining Windows', 'Combining windows', 'Delivery Issues', 'kkanuganti', sysdate);
Insert into TRANSP.EVENTLOG_SUBTYPE
   (EVENTSUBTYPE_NAME, DESCRIPTION, EVENTTYPE_ID, CREATEDBY, CROMOD_DATE)
 Values
   ('Sick', 'Employee was sick on route', 'Employee Issues', 'kkanuganti', sysdate);
Insert into TRANSP.EVENTLOG_SUBTYPE
   (EVENTSUBTYPE_NAME, DESCRIPTION, EVENTTYPE_ID, CREATEDBY, CROMOD_DATE)
 Values
   ('Stops Spread Out', 'distance between stops', 'Delivery Issues', 'kkanuganti', sysdate);
Insert into TRANSP.EVENTLOG_SUBTYPE
   (EVENTSUBTYPE_NAME, DESCRIPTION, EVENTTYPE_ID, CREATEDBY, CROMOD_DATE)
 Values
   ('Close', 'Field Audit Completed', 'Field Audit', 'kkanuganti', sysdate);
Insert into TRANSP.EVENTLOG_SUBTYPE
   (EVENTSUBTYPE_NAME, DESCRIPTION, EVENTTYPE_ID, CREATEDBY, CROMOD_DATE)
 Values
   ('Open', 'Assigned Field Audit', 'Field Audit', 'kkanuganti', sysdate);
Insert into TRANSP.EVENTLOG_SUBTYPE
   (EVENTSUBTYPE_NAME, DESCRIPTION, EVENTTYPE_ID, CREATEDBY, CROMOD_DATE)
 Values
   ('misplaced scanner', 'Went looking for scanner', 'Other', 'kkanuganti', sysdate);
Insert into TRANSP.EVENTLOG_SUBTYPE
   (EVENTSUBTYPE_NAME, DESCRIPTION, EVENTTYPE_ID, CREATEDBY, CROMOD_DATE)
 Values
   ('Mechanical Failure', 'Out of service', 'Out of Service', 'kkanuganti', sysdate);
Insert into TRANSP.EVENTLOG_SUBTYPE
   (EVENTSUBTYPE_NAME, DESCRIPTION, EVENTTYPE_ID, CREATEDBY, CROMOD_DATE)
 Values
   ('late', 'late deliveries', 'Late', 'kkanuganti', sysdate);
Insert into TRANSP.EVENTLOG_SUBTYPE
   (EVENTSUBTYPE_NAME, DESCRIPTION, EVENTTYPE_ID, CREATEDBY, CROMOD_DATE)
 Values
   ('Reattempts', 'Reattemps', 'Other', 'kkanuganti', sysdate);
Insert into TRANSP.EVENTLOG_SUBTYPE
   (EVENTSUBTYPE_NAME, DESCRIPTION, EVENTTYPE_ID, CREATEDBY, CROMOD_DATE)
 Values
   ('Customer Service', 'Waiting to call back from customer service', 'Other', 'kkanuganti', sysdate);
Insert into TRANSP.EVENTLOG_SUBTYPE
   (EVENTSUBTYPE_NAME, DESCRIPTION, EVENTTYPE_ID, CREATEDBY, CROMOD_DATE)
 Values
   ('Error - Order Not Found', 'AirClic order not found', 'Scanning', 'kkanuganti', sysdate);
Insert into TRANSP.EVENTLOG_SUBTYPE
   (EVENTSUBTYPE_NAME, DESCRIPTION, EVENTTYPE_ID, CREATEDBY, CROMOD_DATE)
 Values
   ('Experience', 'Driver not familiar with route.', 'Delivery Issues', 'kkanuganti', sysdate);
Insert into TRANSP.EVENTLOG_SUBTYPE
   (EVENTSUBTYPE_NAME, DESCRIPTION, EVENTTYPE_ID, CREATEDBY, CROMOD_DATE)
 Values
   ('Other', 'scanning problem other than usual', 'Scanning', 'kkanuganti', sysdate);
Insert into TRANSP.EVENTLOG_SUBTYPE
   (EVENTSUBTYPE_NAME, DESCRIPTION, EVENTTYPE_ID, CREATEDBY, CROMOD_DATE)
 Values
   ('Lack of Assets', 'Waiting for Hand trucks', 'Late Dispatch', 'kkanuganti', sysdate);
Insert into TRANSP.EVENTLOG_SUBTYPE
   (EVENTSUBTYPE_NAME, DESCRIPTION, EVENTTYPE_ID, CREATEDBY, CROMOD_DATE)
 Values
   ('Towed', 'Truck impounded', 'Other', 'kkanuganti', sysdate);
Insert into TRANSP.EVENTLOG_SUBTYPE
   (EVENTSUBTYPE_NAME, DESCRIPTION, EVENTTYPE_ID, CREATEDBY, CROMOD_DATE)
 Values
   ('No Label', 'No Label on Box/Bag, Unable to Scan', 'Scanning', 'kkanuganti', sysdate);
Insert into TRANSP.EVENTLOG_SUBTYPE
   (EVENTSUBTYPE_NAME, DESCRIPTION, EVENTTYPE_ID, CREATEDBY, CROMOD_DATE)
 Values
   ('Unable To Process', 'Unable to Process scanning error', 'Scanning', 'kkanuganti', sysdate);
Insert into TRANSP.EVENTLOG_SUBTYPE
   (EVENTSUBTYPE_NAME, DESCRIPTION, EVENTTYPE_ID, CREATEDBY, CROMOD_DATE)
 Values
   ('Customer Call Feature Failed', 'Customer Call IVR System failed', 'Scanning', 'kkanuganti', sysdate);
Insert into TRANSP.EVENTLOG_SUBTYPE
   (EVENTSUBTYPE_NAME, DESCRIPTION, EVENTTYPE_ID, CREATEDBY, CROMOD_DATE)
 Values
   ('Forgot to Scan', 'Forgot to scan', 'Scanning', 'kkanuganti', sysdate);
Insert into TRANSP.EVENTLOG_SUBTYPE
   (EVENTSUBTYPE_NAME, DESCRIPTION, EVENTTYPE_ID, CREATEDBY, CROMOD_DATE)
 Values
   ('Missing In Action', 'Driver not responding', 'MOT Specials', 'kkanuganti', sysdate);
Insert into TRANSP.EVENTLOG_SUBTYPE
   (EVENTSUBTYPE_NAME, DESCRIPTION, EVENTTYPE_ID, CREATEDBY, CROMOD_DATE)
 Values
   ('Phone Crashing', 'Phone shut down', 'Scanning', 'kkanuganti', sysdate);
Insert into TRANSP.EVENTLOG_SUBTYPE
   (EVENTSUBTYPE_NAME, DESCRIPTION, EVENTTYPE_ID, CREATEDBY, CROMOD_DATE)
 Values
   ('Wrong S - NH in Error', 'Scanned NH in error', 'Scanning', 'kkanuganti', sysdate);
Insert into TRANSP.EVENTLOG_SUBTYPE
   (EVENTSUBTYPE_NAME, DESCRIPTION, EVENTTYPE_ID, CREATEDBY, CROMOD_DATE)
 Values
   ('Wrong S - NH as Delivered', 'NH scanned as delivered', 'Scanning', 'kkanuganti', sysdate);
Insert into TRANSP.EVENTLOG_SUBTYPE
   (EVENTSUBTYPE_NAME, DESCRIPTION, EVENTTYPE_ID, CREATEDBY, CROMOD_DATE)
 Values
   ('MOT Dispatched', 'Delivery team sent to assist route', 'Action Taken by Ops', 'kkanuganti', sysdate);
Insert into TRANSP.EVENTLOG_SUBTYPE
   (EVENTSUBTYPE_NAME, DESCRIPTION, EVENTTYPE_ID, CREATEDBY, CROMOD_DATE)
 Values
   ('Called Customer', 'Outbound late notification', 'Action Taken by Ops', 'kkanuganti', sysdate);
Insert into TRANSP.EVENTLOG_SUBTYPE
   (EVENTSUBTYPE_NAME, DESCRIPTION, EVENTTYPE_ID, CREATEDBY, CROMOD_DATE)
 Values
   ('Status Unconfirmed', 'Delivery team unreachable', 'Action Taken by Ops', 'kkanuganti', sysdate);
Insert into TRANSP.EVENTLOG_SUBTYPE
   (EVENTSUBTYPE_NAME, DESCRIPTION, EVENTTYPE_ID, CREATEDBY, CROMOD_DATE)
 Values
   ('MOT/Called Customer', 'Dispatched MOT and called customer about the', 'Action Taken by Ops', 'kkanuganti', sysdate);
Insert into TRANSP.EVENTLOG_SUBTYPE
   (EVENTSUBTYPE_NAME, DESCRIPTION, EVENTTYPE_ID, CREATEDBY, CROMOD_DATE)
 Values
   ('False/Misc Scan Issue', 'False read on report because of a device issu', 'Action Taken by Ops', 'kkanuganti', sysdate);
Insert into TRANSP.EVENTLOG_SUBTYPE
   (EVENTSUBTYPE_NAME, DESCRIPTION, EVENTTYPE_ID, CREATEDBY, CROMOD_DATE)
 Values
   ('False/Forgot to Scan', 'False entry on report because a delivery team', 'Action Taken by Ops', 'kkanuganti', sysdate);
Insert into TRANSP.EVENTLOG_SUBTYPE
   (EVENTSUBTYPE_NAME, DESCRIPTION, EVENTTYPE_ID, CREATEDBY, CROMOD_DATE)
 Values
   ('False/Report Rules', 'False entry on report because of a report bus', 'Action Taken by Ops', 'kkanuganti', sysdate);
Insert into TRANSP.EVENTLOG_SUBTYPE
   (EVENTSUBTYPE_NAME, DESCRIPTION, EVENTTYPE_ID, CREATEDBY, CROMOD_DATE)
 Values
   ('No Action Req', 'Good excpetion however the orders are unlikel', 'Action Taken by Ops', 'kkanuganti', sysdate);
Insert into TRANSP.EVENTLOG_SUBTYPE
   (EVENTSUBTYPE_NAME, DESCRIPTION, EVENTTYPE_ID, CREATEDBY, CROMOD_DATE)
 Values
   ('Misplaced Scanner', 'Misplaced Scanner', 'Employee Issues', 'kkanuganti', sysdate);
Insert into TRANSP.EVENTLOG_SUBTYPE
   (EVENTSUBTYPE_NAME, DESCRIPTION, EVENTTYPE_ID, CREATEDBY, CROMOD_DATE)
 Values
   ('Uniform', 'Uniform', 'Employee Issues', 'kkanuganti', sysdate);
Insert into TRANSP.EVENTLOG_SUBTYPE
   (EVENTSUBTYPE_NAME, DESCRIPTION, EVENTTYPE_ID, CREATEDBY, CROMOD_DATE)
 Values
   ('No Call No Show', 'No Call No Show', 'Employee Issues', 'kkanuganti', sysdate);
Insert into TRANSP.EVENTLOG_SUBTYPE
   (EVENTSUBTYPE_NAME, DESCRIPTION, EVENTTYPE_ID, CREATEDBY, CROMOD_DATE)
 Values
   ('Experience', 'Experience', 'Employee Issues', 'kkanuganti', sysdate);
Insert into TRANSP.EVENTLOG_SUBTYPE
   (EVENTSUBTYPE_NAME, DESCRIPTION, EVENTTYPE_ID, CREATEDBY, CROMOD_DATE)
 Values
   ('Reattempts', 'Reattempts', 'Delivery Issues', 'kkanuganti', sysdate);
Insert into TRANSP.EVENTLOG_SUBTYPE
   (EVENTSUBTYPE_NAME, DESCRIPTION, EVENTTYPE_ID, CREATEDBY, CROMOD_DATE)
 Values
   ('Traffic Congestion', 'Traffic Congestion', 'Delivery Issues', 'kkanuganti', sysdate);
Insert into TRANSP.EVENTLOG_SUBTYPE
   (EVENTSUBTYPE_NAME, DESCRIPTION, EVENTTYPE_ID, CREATEDBY, CROMOD_DATE)
 Values
   ('Traffic Accident', 'Traffic Accident', 'Delivery Issues', 'kkanuganti', sysdate);
Insert into TRANSP.EVENTLOG_SUBTYPE
   (EVENTSUBTYPE_NAME, DESCRIPTION, EVENTTYPE_ID, CREATEDBY, CROMOD_DATE)
 Values
   ('Traffic Street Event', 'Traffic Street Event', 'Delivery Issues', 'kkanuganti', sysdate);
Insert into TRANSP.EVENTLOG_SUBTYPE
   (EVENTSUBTYPE_NAME, DESCRIPTION, EVENTTYPE_ID, CREATEDBY, CROMOD_DATE)
 Values
   ('Traffic Violation - Towed', 'Traffic Violation - Towed', 'Delivery Issues', 'kkanuganti', sysdate);
Insert into TRANSP.EVENTLOG_SUBTYPE
   (EVENTSUBTYPE_NAME, DESCRIPTION, EVENTTYPE_ID, CREATEDBY, CROMOD_DATE)
 Values
   ('Traffic Violation - Moving', 'Traffic Violation - Moving', 'Delivery Issues', 'kkanuganti', sysdate);
Insert into TRANSP.EVENTLOG_SUBTYPE
   (EVENTSUBTYPE_NAME, DESCRIPTION, EVENTTYPE_ID, CREATEDBY, CROMOD_DATE)
 Values
   ('Driver Request', 'Driver Request', 'MOT Specials', 'kkanuganti', sysdate);
Insert into TRANSP.EVENTLOG_SUBTYPE
   (EVENTSUBTYPE_NAME, DESCRIPTION, EVENTTYPE_ID, CREATEDBY, CROMOD_DATE)
 Values
   ('Misload', 'Misload', 'Delivery Issues', 'kkanuganti', sysdate);
Insert into TRANSP.EVENTLOG_SUBTYPE
   (EVENTSUBTYPE_NAME, DESCRIPTION, EVENTTYPE_ID, CREATEDBY, CROMOD_DATE)
 Values
   ('Performance', 'Performance', 'Scanning', 'kkanuganti', sysdate);
Insert into TRANSP.EVENTLOG_SUBTYPE
   (EVENTSUBTYPE_NAME, DESCRIPTION, EVENTTYPE_ID, CREATEDBY, CROMOD_DATE)
 Values
   ('Incorrect Scan', 'Incorrect Scan', 'Scanning', 'kkanuganti', sysdate);
Insert into TRANSP.EVENTLOG_SUBTYPE
   (EVENTSUBTYPE_NAME, DESCRIPTION, EVENTTYPE_ID, CREATEDBY, CROMOD_DATE)
 Values
   ('Added Route', 'Route Added', 'Action Taken by Planning', 'kkanuganti', sysdate);
Insert into TRANSP.EVENTLOG_SUBTYPE
   (EVENTSUBTYPE_NAME, DESCRIPTION, EVENTTYPE_ID, CREATEDBY, CROMOD_DATE)
 Values
   ('Removed Route', 'Removed Route from Plan', 'Action Taken by Planning', 'kkanuganti', sysdate);
Insert into TRANSP.EVENTLOG_SUBTYPE
   (EVENTSUBTYPE_NAME, DESCRIPTION, EVENTTYPE_ID, CREATEDBY, CROMOD_DATE)
 Values
   ('Phone Out of Service', 'Phone Issue', 'Out of Service', 'kkanuganti', sysdate);
Insert into TRANSP.EVENTLOG_SUBTYPE
   (EVENTSUBTYPE_NAME, DESCRIPTION, EVENTTYPE_ID, CREATEDBY, CROMOD_DATE)
 Values
   ('False/Battery Low', 'False/Battery Low', 'Action Taken by Ops', 'kkanuganti', sysdate);
Insert into TRANSP.EVENTLOG_SUBTYPE
   (EVENTSUBTYPE_NAME, DESCRIPTION, EVENTTYPE_ID, CREATEDBY, CROMOD_DATE)
 Values
   ('False/Comm Error', 'False/Comm Error', 'Action Taken by Ops', 'kkanuganti', sysdate);






