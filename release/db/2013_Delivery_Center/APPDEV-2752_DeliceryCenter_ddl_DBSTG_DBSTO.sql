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
                      from transp.handoff_batch bx, transp.handoff_batchstop bsx, transp.eventlogbook el, transp.eventstopnumberbreakdown els
                      where bx.batch_id = bsx.batch_id and bx.delivery_date = trunc(sysdate)  and bx.batch_status in ('CPD','CPD/ADC','CPD/ADF')  
                      and el.ID = els.EVENTLOGID and  bx.delivery_date = el.eventdate
                      and el.ROUTENUMBER = bsx.route_no                  
                      and els.STOPNUMBER = bsx.stop_sequence
                      GROUP BY BSX.ROUTE_NO, bsx.stop_sequence
                  UNION
                        select bsx.ROUTE_NO, bsx.stop_sequence
                      from transp.handoff_batch bx, transp.handoff_batchstop bsx, transp.moteventlogbook el, transp.motstopnumberbreakdown els
                      where bx.batch_id = bsx.batch_id and bx.delivery_date = trunc(sysdate)  and bx.batch_status in ('CPD','CPD/ADC','CPD/ADF')  
                      and el.ID = els.EVENTLOGID and  bx.delivery_date = el.eventdate
                      and el.ROUTENUMBER = bsx.route_no                  
                      and els.STOPNUMBER = bsx.stop_sequence
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
   dbms_output.put_line('Order delivery metric Store Proc completed Successfully');     
   COMMIT;
 Exception
        when others then
       dbms_output.put_line('Error: '||sqlerrm);    
       ROLLBACK;
END;
END;



/*Event Log Scripts*/

CREATE TABLE TRANSP.EVENTLOGBOOK
(
  ID                    VARCHAR2(32 BYTE)       NOT NULL,
  EVENTDATE             DATE                    NOT NULL,
  ROUTENUMBER           VARCHAR2(10 BYTE),
  TRUCKNUMBER           VARCHAR2(12 BYTE),
  ROUTEWINDOWSTARTTIME  DATE,
  ROUTEWINDOWENDTIME    DATE,
  EVENTTYPE             VARCHAR2(40 BYTE),
  EVENTSUBTYPE          VARCHAR2(40 BYTE),
  EVENTDESCRIPTION      VARCHAR2(250 BYTE),
  CROSSSTREETS          VARCHAR2(40 BYTE),
  EMPLOYEENUMBER        VARCHAR2(30 BYTE),
  SCANNERNUMBER         VARCHAR2(30 BYTE),
  DATECREATED           DATE,
  CREATEDBY             VARCHAR2(30 BYTE),
  REFERENCETICKETID     VARCHAR2(16 BYTE)
);

CREATE INDEX TRANSP.IDX_EL_BOOK_DATE ON TRANSP.EVENTLOGBOOK (EVENTDATE);

ALTER TABLE TRANSP.EVENTLOGBOOK ADD ( CONSTRAINT PK_EVENTLOG_BOOK PRIMARY KEY (ID));

CREATE TABLE TRANSP.EVENTLOG_MESSAGEGROUP
(
  GROUP_NAME   VARCHAR2(45 BYTE)                NOT NULL,
  EMAIL        VARCHAR2(45 BYTE),
  CREATEDBY    VARCHAR2(45 BYTE),
  CROMOD_DATE  DATE
);

ALTER TABLE TRANSP.EVENTLOG_MESSAGEGROUP ADD (PRIMARY KEY (GROUP_NAME));

CREATE TABLE TRANSP.EVENTSTOPNUMBERBREAKDOWN
(
  STOPNUMBER  NUMBER(6),
  EVENTLOGID  VARCHAR2(40 BYTE)
);

ALTER TABLE TRANSP.EVENTSTOPNUMBERBREAKDOWN ADD ( CONSTRAINT FK_EVENTLOG_ID FOREIGN KEY (EVENTLOGID)  REFERENCES TRANSP.EVENTLOG_BOOK (ID));

CREATE INDEX TRANSP.IDX_EL_STOP ON TRANSP.EVENTSTOPNUMBERBREAKDOWN (STOPNUMBER);

CREATE TABLE TRANSP.EVENTTYPE
(
  ID                    VARCHAR2(40 BYTE)       NOT NULL,
  EVENTTYPENAME         VARCHAR2(40 BYTE)      NOT NULL,
  EVENTTYPEDESCRIPTION  VARCHAR2(45 BYTE),
  CREATEDBY             VARCHAR2(30 BYTE),
  DATECREATED           DATE,
  ISEMPLOYEEREQUIRED    VARCHAR2(1 BYTE),
  ISCUSTOMERREQUIRED    VARCHAR2(1 BYTE),
  ISACTIVE              VARCHAR2(1 BYTE)
);

ALTER TABLE TRANSP.EVENTTYPE ADD ( PRIMARY KEY (ID));

CREATE TABLE TRANSP.EVENTSUBTYPE
(
  ID                       VARCHAR2(40 BYTE)    NOT NULL,
  EVENTSUBTYPENAME         VARCHAR2(45 BYTE)    NOT NULL,
  EVENTSUBTYPEDESCRIPTION  VARCHAR2(45 BYTE),
  EVENTTYPEID              VARCHAR2(45 BYTE)    NOT NULL,
  CREATEDBY                VARCHAR2(30 BYTE),
  DATECREATED              DATE,
  MSGGROUP_ID              VARCHAR2(45 BYTE),
  ISACTIVE                 VARCHAR2(1 BYTE)
)

CREATE INDEX TRANSP.FK_EVENTSUBTYPE_EVENTTYPEID ON TRANSP.EVENTSUBTYPE(EVENTTYPEID);

ALTER TABLE TRANSP.EVENTSUBTYPE ADD (UNIQUE (ID, EVENTTYPEID));

ALTER TABLE TRANSP.EVENTSUBTYPE ADD ( CONSTRAINT FK_EVENTSUBTYPE_EVENTTYPEID  FOREIGN KEY (EVENTTYPEID)  REFERENCES TRANSP.EVENTLOG_TYPE (ID),
  CONSTRAINT FK_EVENTSUBTYPE_MSGGROUPID  FOREIGN KEY (MSGGROUP_ID)  REFERENCES TRANSP.EVENTLOG_MESSAGEGROUP (GROUP_NAME));

  
/*Mot EventLog scripts*/

CREATE TABLE TRANSP.MOTEVENTLOGBOOK
(
  ID                VARCHAR2(16 BYTE)           NOT NULL,
  EVENTDATE         DATE                        NOT NULL,
  ROUTENUMBER       VARCHAR2(10 BYTE)           NOT NULL,
  MOTROUTENUMBER    VARCHAR2(30 BYTE),
  NEXTELNUMBER      VARCHAR2(12 BYTE),
  EVENTTYPEID       VARCHAR2(40 BYTE)           NOT NULL,
  EVENTDESCRIPTION  VARCHAR2(250 BYTE),
  TICKETNUMBER      VARCHAR2(40 BYTE),
  ISVERIFIED        VARCHAR2(1 BYTE),
  DATE_VERIFIED     DATE,
  VERIFIED_BY       VARCHAR2(30 BYTE),
  DATECREATED       DATE,
  CREATEDBY         VARCHAR2(30 BYTE)
);

CREATE INDEX TRANSP.IDX_MOTEL_BOOK_DATE ON TRANSP.MOTEVENTLOGBOOK (EVENTDATE);

ALTER TABLE TRANSP.MOTEVENTLOGBOOK ADD ( CONSTRAINT PK_MOTEVENTLOG_BOOK PRIMARY KEY (ID));

CREATE TABLE TRANSP.MOTEVENTLOG_MESSAGEGROUP
(
  GROUP_NAME   VARCHAR2(45 BYTE)                NOT NULL,
  EMAIL        VARCHAR2(45 BYTE),
  CREATEDBY    VARCHAR2(45 BYTE),
  CROMOD_DATE  DATE
);

ALTER TABLE TRANSP.MOTEVENTLOG_MESSAGEGROUP ADD ( PRIMARY KEY (GROUP_NAME));

CREATE TABLE TRANSP.MOTSTOPNUMBERBREAKDOWN
(
  MOTSTOPNUMBER  NUMBER(6),
  MOTEVENTLOGID  VARCHAR2(40 BYTE)
);

CREATE INDEX TRANSP.IDX_MOTEL_STOP ON TRANSP.MOTSTOPNUMBERBREAKDOWN(STOP_NUMBER);

ALTER TABLE TRANSP.MOTSTOPNUMBERBREAKDOWN ADD ( CONSTRAINT FK_MOTEVENTLOG_ID FOREIGN KEY (MOTEVENTLOGID) REFERENCES TRANSP.MOTEVENTLOGBOOK (ID));

CREATE TABLE TRANSP.MOTEVENTTYPE
(
  ID                       VARCHAR2(40 BYTE)    NOT NULL,
  MOTEVENTTYPENAME         VARCHAR2(40 BYTE)    NOT NULL,
  MOTEVENTTYPEDESCRIPTION  VARCHAR2(45 BYTE),
  MSGGROUP_ID              VARCHAR2(45 BYTE),
  CREATEDBY                VARCHAR2(30 BYTE),
  DATECREATED              DATE,
  ISACTIVE                 VARCHAR2(1 BYTE)
)

ALTER TABLE TRANSP.MOTEVENTTYPE ADD ( PRIMARY KEY (ID));

ALTER TABLE TRANSP.MOTEVENTTYPE ADD ( CONSTRAINT FK_MOTEVTSUBTYPE_GROUPID FOREIGN KEY (MSGGROUP_ID) REFERENCES TRANSP.MOTEVENTLOG_MESSAGEGROUP (GROUP_NAME));

CREATE SEQUENCE TRANSP.EVENTLOGSEQ
  START WITH 4801
  MAXVALUE 100000000000000000
  MINVALUE 1
  NOCYCLE
  CACHE 20
  NOORDER;
  
CREATE SEQUENCE TRANSP.MOTEVENTLOGSEQ
  START WITH 1
  MAXVALUE 100000000000000000
  MINVALUE 1
  NOCYCLE
  CACHE 20
  NOORDER;  

