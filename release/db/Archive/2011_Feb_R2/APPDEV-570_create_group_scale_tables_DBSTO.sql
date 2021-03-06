
CREATE TABLE ERPS.GRP_HISTORY
(
  VERSION          NUMBER(10)          PRIMARY KEY,
  DATE_CREATED     DATE                         NOT NULL,
  CREATED_BY       VARCHAR2(40)            NOT NULL,
  APPROVAL_STATUS  CHAR(1)                 NOT NULL,
  DATE_APPROVED    DATE,
  APPROVED_BY      VARCHAR2(40),
  DESCRIPTION      VARCHAR2(255)
)

CREATE TABLE ERPS.GRP_SCALE_MASTER 
 (
 ID VARCHAR2(16) PRIMARY KEY,
  VERSION NUMBER(10),
SAP_ID  VARCHAR2(10) not null,
 SHORT_DESC VARCHAR2(55) not null,
 LONG_DESC VARCHAR2(255) not null,
 ACTIVE VARCHAR2(1)
 );
 
CREATE INDEX ERPS.GRP_SCLE_SAP_ID_IDX  ON ERPS.GRP_SCALE_MASTER(SAP_ID) LOGGING NOPARALLEL tablespace fderpidx;

CREATE UNIQUE INDEX ERPS.GRP_SCLE_SAP_ID_VERSION_IDX  ON ERPS.GRP_SCALE_MASTER(SAP_ID,VERSION) LOGGING NOPARALLEL tablespace fderpidx;



 CREATE TABLE ERPS.GRP_PRICING (
 ID VARCHAR2(16) PRIMARY KEY,
 VERSION NUMBER(10),
 GRP_ID VARCHAR2(16) not null,
 ZONE_ID VARCHAR2(10) not null,
 QUANTITY NUMBER(10,2) not null,
 PRICING_UNIT VARCHAR2(3) not null,
 PRICE NUMBER(10,2) not null,
 SCALE_UNIT VARCHAR2(3) NOT NULL
 );
 
 
 CREATE INDEX ERPS.GRP_PRI_GRP_ID_IDX  ON ERPS.GRP_PRICING(GRP_ID) LOGGING NOPARALLEL tablespace fderpidx;
 
 CREATE INDEX ERPS.GRP_PRI_GRP_VERSION_IDX  ON ERPS.GRP_PRICING(VERSION) LOGGING NOPARALLEL tablespace fderpidx;

 CREATE TABLE ERPS.MATERIAL_GRP 
 (
 ID VARCHAR2(16) PRIMARY KEY,
  VERSION NUMBER(10),
 MAT_ID  VARCHAR2(18) not null,
 GRP_ID VARCHAR2(16) not null
 );
 
  CREATE INDEX ERPS.GRP_MAT_MAT_ID_IDX  ON ERPS.MATERIAL_GRP(MAT_ID) LOGGING NOPARALLEL tablespace fderpidx;
 
  CREATE INDEX ERPS.GRP_MAT_GRP_ID_IDX  ON ERPS.MATERIAL_GRP(GRP_ID) LOGGING NOPARALLEL tablespace fderpidx;
 
  CREATE INDEX ERPS.GRP_MAT_GRP_VERSION_IDX  ON ERPS.MATERIAL_GRP(VERSION) LOGGING NOPARALLEL tablespace fderpidx;
 
  ALTER TABLE ERPS.GRP_PRICING ADD (
  CONSTRAINT GRP_PRICING_GRP_ID_FK 
 FOREIGN KEY (GRP_ID) 
 REFERENCES ERPS.GRP_SCALE_MASTER (ID));
 
 ALTER TABLE ERPS.MATERIAL_GRP ADD (
  CONSTRAINT MAT_GRP_MAT_FK 
 FOREIGN KEY (MAT_ID) 
 REFERENCES ERPS.MATERIAL (ID));
 
 ALTER TABLE ERPS.MATERIAL_GRP ADD (
  CONSTRAINT MAT_GRP_GRP_FK 
 FOREIGN KEY (GRP_ID) 
 REFERENCES ERPS.GRP_SCALE_MASTER (ID));
 

CREATE SEQUENCE ERPS.GRPBATCH_SEQ
  START WITH 1
  MAXVALUE 999999999999999999999999999
  MINVALUE 1
  NOCYCLE
  NOCACHE
  NOORDER;





GRANT DELETE, INSERT, SELECT, UPDATE ON ERPS.GRP_HISTORY TO fdstore_stprd01;

GRANT SELECT ON ERPS.GRP_HISTORY TO APPDEV;

GRANT ALTER, SELECT ON ERPS.GRPBATCH_SEQ TO fdstore_stprd01; 

GRANT SELECT ON ERPS.GRPBATCH_SEQ TO APPDEV; 

GRANT DELETE, INSERT, SELECT, UPDATE ON ERPS.GRP_SCALE_MASTER TO fdstore_stprd01;

GRANT SELECT ON ERPS.GRP_SCALE_MASTER TO APPDEV;
 
GRANT DELETE, INSERT, SELECT, UPDATE ON  ERPS.GRP_PRICING TO fdstore_stprd01;

GRANT SELECT ON  ERPS.GRP_PRICING TO APPDEV;
 
GRANT DELETE, INSERT, SELECT, UPDATE ON  ERPS.MATERIAL_GRP TO fdstore_stprd01;

GRANT SELECT ON  ERPS.MATERIAL_GRP TO APPDEV;


