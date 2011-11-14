CREATE OR REPLACE TYPE TRANSP.CRISISMANAGER_ZONE_NO AS VARRAY(1000)  OF varchar2(10);

CREATE OR REPLACE TYPE TRANSP.CRISISMANAGER_DELIVERYTYPE AS VARRAY(1000)  OF varchar2(10);

CREATE SEQUENCE TRANSP.CRISISMNGBATCHSEQ  START WITH 1201  MAXVALUE 999999999999999999  MINVALUE 1  NOCYCLE  CACHE 10  NOORDER;

/*create CRISISMNG_BATCH table*/
CREATE TABLE TRANSP.CRISISMNG_BATCH
(
  BATCH_ID            VARCHAR2(16 BYTE)         NOT NULL,
  DELIVERY_DATE       DATE                      NOT NULL,
  BATCH_STATUS        VARCHAR2(10 BYTE)         NOT NULL,
  SYS_MESSAGE         VARCHAR2(1024 BYTE),
  IS_CANCEL_ELIGIBLE  VARCHAR2(1 BYTE),
  DESTINATION_DATE    DATE                      NOT NULL,
  CUTOFF_DATETIME     DATE,
  WINDOW_STARTTIME    DATE,
  WINDOW_ENDTIME      DATE,
  BATCH_TYPE          VARCHAR2(8 BYTE)          NOT NULL,
  AREA                TRANSP.CRISISMANAGER_ZONE_NO,
  DELIVERY_TYPE       TRANSP.CRISISMANAGER_DELIVERYTYPE,
  PROFILE_NAME        VARCHAR2(5 BYTE)
);

CREATE INDEX TRANSP.IDX_CMBATCH_DATE ON TRANSP.CRISISMNG_BATCH (DELIVERY_DATE);

ALTER TABLE TRANSP.CRISISMNG_BATCH ADD (CONSTRAINT PK_CRISISMNGBATCH PRIMARY KEY (BATCH_ID));

/*create CRISISMNG_BATCHACTION table*/
CREATE TABLE TRANSP.CRISISMNG_BATCHACTION
(
  BATCH_ID         VARCHAR2(16 BYTE)            NOT NULL,
  ACTION_DATETIME  TIMESTAMP(6)                 NOT NULL,
  ACTION_TYPE      VARCHAR2(50 BYTE)            NOT NULL,
  ACTION_BY        VARCHAR2(50 BYTE)            NOT NULL
);

CREATE INDEX TRANSP.IDX_CMBATCHACTION_BATCHID ON TRANSP.CRISISMNG_BATCHACTION (BATCH_ID);

ALTER TABLE TRANSP.CRISISMNG_BATCHACTION ADD (CONSTRAINT PK_CRISISMNG_BATCHACTION PRIMARY KEY (BATCH_ID, ACTION_DATETIME));

ALTER TABLE TRANSP.CRISISMNG_BATCHACTION ADD (CONSTRAINT ACT_CMBATCHACTION_FK FOREIGN KEY (BATCH_ID) REFERENCES TRANSP.CRISISMNG_BATCH (BATCH_ID));

/*create CRISISMNG_BATCHCUSTOMERINFO table*/
CREATE TABLE TRANSP.CRISISMNG_BATCHCUSTOMERINFO
(
  BATCH_ID        VARCHAR2(16 BYTE)             NOT NULL,
  CUSTOMER_ID     VARCHAR2(32 BYTE)             NOT NULL,
  FIRST_NAME      VARCHAR2(55 BYTE)             NOT NULL,
  LAST_NAME       VARCHAR2(55 BYTE)             NOT NULL,
  EMAIL           VARCHAR2(80 BYTE)             NOT NULL,
  HOME_PHONE      VARCHAR2(15 BYTE),
  BUSINESS_PHONE  VARCHAR2(15 BYTE),
  CELL_PHONE      VARCHAR2(15 BYTE),
  BUSINESS_EXT    VARCHAR2(6 BYTE),
  COMPANY_NAME    VARCHAR2(128 BYTE)
);

CREATE INDEX TRANSP.IDX_CMBATCHCUST_BATCHID ON TRANSP.CRISISMNG_BATCHCUSTOMERINFO(BATCH_ID);

ALTER TABLE TRANSP.CRISISMNG_BATCHCUSTOMERINFO ADD ( CONSTRAINT PK_CRISISMNG_BATCHCUSTOMER PRIMARY KEY (BATCH_ID, CUSTOMER_ID));

ALTER TABLE TRANSP.CRISISMNG_BATCHCUSTOMERINFO ADD ( CONSTRAINT ACT_CMBATCHCUST_FK  FOREIGN KEY (BATCH_ID)  REFERENCES TRANSP.CRISISMNG_BATCH (BATCH_ID));

/*create CRISISMNG_BATCHORDER table*/
CREATE TABLE TRANSP.CRISISMNG_BATCHORDER
(
  BATCH_ID           VARCHAR2(16 BYTE)          NOT NULL,
  DELIVERY_DATE      DATE                       NOT NULL,
  WEBORDER_ID        VARCHAR2(16 BYTE)          NOT NULL,
  ERPORDER_ID        VARCHAR2(20 BYTE),
  AREA               VARCHAR2(10 BYTE)          NOT NULL,
  WINDOW_STARTTIME   DATE                       NOT NULL,
  WINDOW_ENDTIME     DATE                       NOT NULL,
  ORDER_STATUS       VARCHAR2(16 BYTE)          NOT NULL,
  CUTOFFTIME         DATE                       NOT NULL,
  ADDRESS_ID         VARCHAR2(15 BYTE)          NOT NULL,
  CUSTOMER_ID        VARCHAR2(32 BYTE)          NOT NULL,
  RESERVATION_TYPE   VARCHAR2(15 BYTE)          NOT NULL,
  AMOUNT             NUMBER(8,2)                NOT NULL,
  DELIVERY_TYPE      VARCHAR2(1 BYTE),
  IS_EXCEPTION       VARCHAR2(1 BYTE),
  NEWRESERVATION_ID  VARCHAR2(20 BYTE)
);

CREATE INDEX TRANSP.IDX_CMBATCHORDER_BATCHID ON TRANSP.CRISISMNG_BATCHORDER(BATCH_ID);

ALTER TABLE TRANSP.CRISISMNG_BATCHORDER ADD ( CONSTRAINT PK_CRISISMNG_BATCHORDER PRIMARY KEY (BATCH_ID, WEBORDER_ID));

ALTER TABLE TRANSP.CRISISMNG_BATCHORDER ADD (CONSTRAINT ACT_CMBATCHORDER_FK FOREIGN KEY (BATCH_ID) REFERENCES TRANSP.CRISISMNG_BATCH (BATCH_ID));

/*create CRISISMNG_BATCHRESERVATION table*/
CREATE TABLE TRANSP.CRISISMNG_BATCHRESERVATION
(
  BATCH_ID          VARCHAR2(16 BYTE)           NOT NULL,
  CUSTOMER_ID       VARCHAR2(32 BYTE)           NOT NULL,
  RESERVATION_ID    VARCHAR2(15 BYTE)           NOT NULL,
  DELIVERY_DATE     DATE                        NOT NULL,
  AREA              VARCHAR2(10 BYTE)           NOT NULL,
  WINDOW_STARTTIME  DATE                        NOT NULL,
  WINDOW_ENDTIME    DATE                        NOT NULL,
  CUTOFFTIME        DATE                        NOT NULL,
  ADDRESS_ID        VARCHAR2(15 BYTE)           NOT NULL,
  RESERVATION_TYPE  VARCHAR2(15 BYTE)           NOT NULL,
  STATUS_CODE       NUMBER(8,2)                 NOT NULL,
  IS_EXCEPTION      VARCHAR2(1 BYTE)
);

CREATE INDEX TRANSP.IDX_CMBATCHRSV_BATCHID ON TRANSP.CRISISMNG_BATCHRESERVATION(BATCH_ID);

ALTER TABLE TRANSP.CRISISMNG_BATCHRESERVATION ADD (CONSTRAINT PK_CMBATCH_RESERVATION PRIMARY KEY (BATCH_ID, RESERVATION_ID));

ALTER TABLE TRANSP.CRISISMNG_BATCHRESERVATION ADD (CONSTRAINT ACT_CMBATCHRESERVATION_FK FOREIGN KEY (BATCH_ID) REFERENCES TRANSP.CRISISMNG_BATCH (BATCH_ID));

/*create CRISISMNG_BATCHSTANDINGORDER table*/
CREATE TABLE TRANSP.CRISISMNG_BATCHSTANDINGORDER
(
  BATCH_ID                VARCHAR2(16 BYTE)     NOT NULL,
  CUSTOMER_ID             VARCHAR2(32 BYTE)     NOT NULL,
  STANDINGORDER_ID        VARCHAR2(16 BYTE)     NOT NULL,
  WEBORDER_ID             VARCHAR2(16 BYTE)     NOT NULL,
  ERPORDER_ID             VARCHAR2(16 BYTE)     NOT NULL,
  WINDOW_STARTTIME        DATE                  NOT NULL,
  WINDOW_ENDTIME          DATE                  NOT NULL,
  AREA                    VARCHAR2(8 BYTE)      NOT NULL,
  ORDER_STATUS            VARCHAR2(16 BYTE)     NOT NULL,
  SALE_LINEITEMCOUNT      NUMBER(8,2)           NOT NULL,
  TEMPLATE_LINEITEMCOUNT  NUMBER(8,2)           NOT NULL,
  NOTIFICATION_MSG        VARCHAR2(1024 BYTE),
  PLACEORDER_STATUS       VARCHAR2(8 BYTE)
);

CREATE INDEX TRANSP.IDX_CMBATCHSO_BATCHID ON TRANSP.CRISISMNG_BATCHSTANDINGORDER(BATCH_ID);

ALTER TABLE TRANSP.CRISISMNG_BATCHSTANDINGORDER ADD ( CONSTRAINT PK_CMBATCHSTANDINGORDER PRIMARY KEY (BATCH_ID, WEBORDER_ID));

ALTER TABLE TRANSP.CRISISMNG_BATCHSTANDINGORDER ADD ( CONSTRAINT ACT_CMBATCHSTANDINGORDER_FK FOREIGN KEY (BATCH_ID) REFERENCES TRANSP.CRISISMNG_BATCH (BATCH_ID));

/*create CRISISMNG_BATCHTIMESLOT table*/
CREATE TABLE TRANSP.CRISISMNG_BATCHTIMESLOT
(
 BATCH_ID             VARCHAR2(16 BYTE)        NOT NULL,
  AREA                 VARCHAR2(10 BYTE)        NOT NULL,
  WINDOW_STARTTIME     DATE                     NOT NULL,
  WINDOW_ENDTIME       DATE                     NOT NULL,
  NEW_WINDOWSTARTTIME  DATE,
  NEW_WINDOWENDTIME    DATE,
  TIMESLOT_ID          VARCHAR2(15 BYTE)
);

CREATE INDEX TRANSP.IDX_CMBATCHTIMESLOT_BATCHID ON TRANSP.CRISISMNG_BATCHTIMESLOT(BATCH_ID);

ALTER TABLE TRANSP.CRISISMNG_BATCHTIMESLOT ADD (CONSTRAINT PK_CRISISMNG_BATCHTIMESLOT PRIMARY KEY (BATCH_ID, AREA, WINDOW_STARTTIME, WINDOW_ENDTIME));

ALTER TABLE TRANSP.CRISISMNG_BATCHTIMESLOT ADD (CONSTRAINT ACT_CMBATCHTIMESLOT_FK FOREIGN KEY (BATCH_ID) REFERENCES TRANSP.CRISISMNG_BATCH (BATCH_ID));

/*permissions*/
GRANT EXECUTE ON TRANSP.CRISISMANAGER_ZONE_NO TO fdtrn_stprd01;

GRANT EXECUTE ON TRANSP.CRISISMANAGER_ZONE_NO TO appdev;

GRANT EXECUTE ON TRANSP.CRISISMANAGER_DELIVERYTYPE TO fdtrn_stprd01;

GRANT EXECUTE ON TRANSP.CRISISMANAGER_DELIVERYTYPE TO appdev;

GRANT DELETE, INSERT, SELECT, UPDATE ON TRANSP.CRISISMNG_BATCH TO fdtrn_stprd01;

GRANT SELECT ON TRANSP.CRISISMNG_BATCH TO appdev;

GRANT DELETE, INSERT, SELECT, UPDATE ON TRANSP.CRISISMNG_BATCHACTION TO fdtrn_stprd01;

GRANT SELECT ON TRANSP.CRISISMNG_BATCHACTION TO appdev;

GRANT DELETE, INSERT, SELECT, UPDATE ON TRANSP.CRISISMNG_BATCHORDER TO fdtrn_stprd01;

GRANT SELECT ON TRANSP.CRISISMNG_BATCHORDER TO appdev;

GRANT DELETE, INSERT, SELECT, UPDATE ON TRANSP.CRISISMNG_BATCHRESERVATION TO fdtrn_stprd01;

GRANT SELECT ON TRANSP.CRISISMNG_BATCHRESERVATION TO appdev;

GRANT DELETE, INSERT, SELECT, UPDATE ON TRANSP.CRISISMNG_BATCHSTANDINGORDER TO fdtrn_stprd01;

GRANT SELECT ON TRANSP.CRISISMNG_BATCHSTANDINGORDER TO appdev;

GRANT DELETE, INSERT, SELECT, UPDATE ON TRANSP.CRISISMNG_BATCHTIMESLOT TO fdtrn_stprd01;

GRANT SELECT ON TRANSP.CRISISMNG_BATCHTIMESLOT TO appdev;

GRANT DELETE, INSERT, SELECT, UPDATE ON TRANSP.CRISISMNG_BATCHCUSTOMERINFO TO fdtrn_stprd01;

GRANT SELECT ON TRANSP.CRISISMNG_BATCHCUSTOMERINFO TO appdev;

GRANT SELECT ON TRANSP.CRISISMNGBATCHSEQ TO fdtrn_stprd01;   

GRANT SELECT ON TRANSP.CRISISMNGBATCHSEQ TO appdev;

GRANT SELECT ON cust.customer TO fdtrn_stprd01;
GRANT SELECT ON cust.fdcustomer TO fdtrn_stprd01;
GRANT SELECT ON cust.customerinfo TO fdtrn_stprd01;
GRANT SELECT ON cust.sale TO fdtrn_stprd01;
GRANT SELECT ON cust.salesaction TO fdtrn_stprd01;
GRANT SELECT ON cust.deliveryinfo TO fdtrn_stprd01;
GRANT SELECT ON dlv.reservation TO fdtrn_stprd01;
GRANT SELECT ON cust.standing_order TO fdtrn_stprd01;
GRANT SELECT ON cust.customerlist TO fdtrn_stprd01;
GRANT SELECT ON cust.orderline TO fdtrn_stprd01;
GRANT SELECT ON cust.customerlist_details TO fdtrn_stprd01;