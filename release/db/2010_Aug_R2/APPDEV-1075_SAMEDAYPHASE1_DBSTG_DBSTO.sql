CREATE OR REPLACE TYPE TRANSP.HANDOFF_ROUTING_ROUTE_NO AS VARRAY(1000)  OF varchar2(10);

CREATE TABLE TRANSP.HANDOFF_BATCH
(
  BATCH_ID     VARCHAR2(16 BYTE)             NOT NULL,
  DELIVERY_DATE   DATE                          NOT NULL,
  BATCH_STATUS VARCHAR2(10 BYTE)     NOT NULL,
  SYS_MESSAGE    VARCHAR2(1024 BYTE),
  SCENARIO VARCHAR2(8 BYTE),
  CUTOFF_DATETIME DATE
  IS_COMMIT_ELIGIBLE VARCHAR2(1 BYTE)
);

ALTER TABLE TRANSP.HANDOFF_BATCH ADD (CONSTRAINT PK_HANDOFF_BATCH  PRIMARY KEY (BATCH_ID));

CREATE TABLE TRANSP.HANDOFF_BATCHACTION
(
  BATCH_ID     VARCHAR2(16 BYTE)             NOT NULL,
  ACTION_DATETIME   TIMESTAMP                          NOT NULL,
  ACTION_TYPE   VARCHAR2(50 BYTE)                          NOT NULL,
  ACTION_BY   VARCHAR2(50 BYTE)                          NOT NULL  
);


ALTER TABLE TRANSP.HANDOFF_BATCHACTION ADD (CONSTRAINT PK_HANDOFF_BATCHACTION  PRIMARY KEY (BATCH_ID, ACTION_DATETIME));
ALTER TABLE TRANSP.HANDOFF_BATCHACTION ADD (CONSTRAINT ACT_HANDOFF_BATCHACTION_FK FOREIGN KEY (BATCH_ID) REFERENCES TRANSP.HANDOFF_BATCH (BATCH_ID));

CREATE TABLE TRANSP.HANDOFF_BATCHSESSION
(
  BATCH_ID     VARCHAR2(16 BYTE)             NOT NULL,
  SESSION_NAME     VARCHAR2(100 BYTE)             NOT NULL,
  REGION     VARCHAR2(10 BYTE)             NOT NULL 
);

ALTER TABLE TRANSP.HANDOFF_BATCHSESSION ADD (CONSTRAINT PK_HANDOFF_BATCHSESSION  PRIMARY KEY (BATCH_ID, SESSION_NAME));
ALTER TABLE TRANSP.HANDOFF_BATCHSESSION ADD (CONSTRAINT ACT_HANDOFF_BATCHSESSION_FK FOREIGN KEY (BATCH_ID) REFERENCES TRANSP.HANDOFF_BATCH (BATCH_ID));

CREATE TABLE TRANSP.HANDOFF_BATCHDEPOTSCHEDULE
(
  BATCH_ID     VARCHAR2(16 BYTE)             NOT NULL,
  AREA     VARCHAR2(10 BYTE)             NOT NULL,
  DEPOTARRIVALTIME     DATE             NOT NULL,
  TRUCKDEPARTURETIME     DATE             NOT NULL
);

ALTER TABLE TRANSP.HANDOFF_BATCHDEPOTSCHEDULE ADD (CONSTRAINT PK_HANDOFF_BATDPTSCHEDULE  PRIMARY KEY (BATCH_ID, AREA, DEPOTARRIVALTIME, TRUCKDEPARTURETIME));
ALTER TABLE TRANSP.HANDOFF_BATCHDEPOTSCHEDULE ADD (CONSTRAINT ACT_HANDOFF_BATDPTSCHEDULE_FK FOREIGN KEY (BATCH_ID) REFERENCES TRANSP.HANDOFF_BATCH (BATCH_ID));

CREATE TABLE TRANSP.HANDOFF_BATCHROUTE
(
  BATCH_ID     VARCHAR2(16 BYTE)             NOT NULL,
  SESSION_NAME     VARCHAR2(100 BYTE)             NOT NULL,
  ROUTE_NO     VARCHAR2(50 BYTE)             NOT NULL,
  ROUTING_ROUTE_NO     TRANSP.HANDOFF_ROUTING_ROUTE_NO            NOT NULL,
  AREA     VARCHAR2(10 BYTE)             NOT NULL,
  STARTTIME   DATE                          NOT NULL,
  COMPLETETIME   DATE                          NOT NULL,
  DISTANCE NUMBER(10,2) NOT NULL ,
  TRAVELTIME NUMBER(10,2) NOT NULL ,
  SERVICETIME NUMBER(10,2) NOT NULL 
);

	
ALTER TABLE TRANSP.HANDOFF_BATCHROUTE ADD (CONSTRAINT PK_HANDOFF_BATCHROUTE  PRIMARY KEY (BATCH_ID,SESSION_NAME, ROUTE_NO));
ALTER TABLE TRANSP.HANDOFF_BATCHROUTE ADD (CONSTRAINT ACT_HANDOFF_BATCHROUTE_FK FOREIGN KEY (BATCH_ID, SESSION_NAME) REFERENCES TRANSP.HANDOFF_BATCHSESSION (BATCH_ID, SESSION_NAME));

CREATE TABLE TRANSP.HANDOFF_BATCHSTOP
(
  BATCH_ID     VARCHAR2(16 BYTE)             NOT NULL,
  WEBORDER_ID  VARCHAR2(16 BYTE)             NOT NULL,
  ERPORDER_ID  VARCHAR2(20 BYTE)             , 
  AREA     VARCHAR2(10 BYTE)             NOT NULL,
  DELIVERY_TYPE          VARCHAR2(2 BYTE) NOT NULL,
  WINDOW_STARTTIME   DATE                          NOT NULL,
  WINDOW_ENDTIME   DATE                          NOT NULL,
  LOCATION_ID  VARCHAR2(16 BYTE)     NOT NULL,
  SESSION_NAME     VARCHAR2(100 BYTE), 
  ROUTE_NO     VARCHAR2(50 BYTE) ,
  ROUTING_ROUTE_NO     VARCHAR2(50 BYTE)  ,
  STOP_SEQUENCE       NUMBER(9),
  STOP_ARRIVALDATETIME DATE,
  STOP_DEPARTUREDATETIME DATE,
  IS_EXCEPTION  VARCHAR2(1 BYTE)
);

ALTER TABLE TRANSP.HANDOFF_BATCHSTOP ADD (CONSTRAINT PK_HANDOFF_BATCHSTOP  PRIMARY KEY (BATCH_ID, WEBORDER_ID));
ALTER TABLE TRANSP.HANDOFF_BATCHSTOP ADD (CONSTRAINT ACT_HANDOFF_BATCHSTOP_FK FOREIGN KEY (BATCH_ID) REFERENCES TRANSP.HANDOFF_BATCH (BATCH_ID));

CREATE SEQUENCE TRANSP.HANDOFFBATCHSEQ
  START WITH 1
  MAXVALUE 999999999999999999999999999
  MINVALUE 0
  NOCYCLE
  CACHE 20
  NOORDER;
  
CREATE INDEX TRANSP.IDX_HF_BATCH_DATE ON TRANSP.HANDOFF_BATCH(DELIVERY_DATE);  
CREATE INDEX TRANSP.IDX_HF_BATCHACTION_BATCHID ON TRANSP.HANDOFF_BATCHACTION(BATCH_ID);  
CREATE INDEX TRANSP.IDX_HF_BATCHSESSION_BATCHID ON TRANSP.HANDOFF_BATCHSESSION(BATCH_ID);  
CREATE INDEX TRANSP.IDX_HF_BATCHROUTE_BATCHID ON TRANSP.HANDOFF_BATCHROUTE(BATCH_ID); 
CREATE INDEX TRANSP.IDX_HF_BATCHSTOP_BATCHID ON TRANSP.HANDOFF_BATCHSTOP(BATCH_ID); 
CREATE INDEX TRANSP.IDX_HF_BATCHDPTSCH_BATCHID ON TRANSP.HANDOFF_BATCHDEPOTSCHEDULE(BATCH_ID);




 
 
 