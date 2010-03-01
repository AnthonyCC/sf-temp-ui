CREATE TABLE ERPS.PRICING_ZONE(
ID VARCHAR2(16) PRIMARY KEY,
SAP_ID VARCHAR2(18) NOT NULL,
REGION_ID VARCHAR2(18) NOT NULL,
SERVICE_TYPE VARCHAR2(10) NOT NULL,
PARENT_ZONE_ID VARCHAR2(16),
DEFAULT_ZONE VARCHAR2(1),
VERSION NUMBER(10) NOT NULL,
DESCRIPTION VARCHAR2(255),
WEB_DESCRIPTION VARCHAR2(255)
);

CREATE INDEX ERPS.SAP_ID_IDX  ON ERPS.PRICING_ZONE(SAP_ID) LOGGING NOPARALLEL;

CREATE UNIQUE INDEX ERPS.SAP_ID_VERSION_IDX  ON ERPS.PRICING_ZONE(SAP_ID,VERSION) LOGGING NOPARALLEL;
 
CREATE TABLE ERPS.PRICING_REGION(
ID VARCHAR2(16) PRIMARY KEY,
SAP_ID VARCHAR2(18) NOT NULL,
VERSION NUMBER(10),
DESCRIPTION VARCHAR2(255)
);

CREATE TABLE ERPS.PRICING_REGION_ZIPS(
ID VARCHAR2(16) PRIMARY KEY,
REGION_ID VARCHAR2(18) NOT NULL,
VERSION NUMBER(10) NOT NULL,
ZIP_CODE VARCHAR2(10) NOT NULL
);

CREATE INDEX ERPS.REGION_ID_IDX  ON ERPS.PRICING_REGION_ZIPS(REGION_ID) LOGGING NOPARALLEL;

CREATE TABLE ERPS.ZONE_HISTORY(
  VERSION          NUMBER(10)                   NOT NULL PRIMARY KEY,
  DATE_CREATED     DATE                         NOT NULL,
  CREATED_BY       VARCHAR2(40 BYTE)            NOT NULL,
  APPROVAL_STATUS  CHAR(1 BYTE)                 NOT NULL,
  DATE_APPROVED    DATE,
  APPROVED_BY      VARCHAR2(40 BYTE),
  DESCRIPTION      VARCHAR2(255 BYTE)
);


ALTER TABLE ERPS.PRICING_ZONE ADD (
  CONSTRAINT ZONE_REGIONID_FK 
 FOREIGN KEY (REGION_ID) 
 REFERENCES ERPS.PRICING_REGION (ID));


ALTER TABLE ERPS.PRICING_REGION_ZIPS ADD (
  CONSTRAINT PRICING_REGIONID_FK 
 FOREIGN KEY (REGION_ID) 
 REFERENCES ERPS.PRICING_REGION (ID));
 
ALTER TABLE ERPS.PRICING_ZONE ADD (
  CONSTRAINT PRICING_ZONE_HISTORY_FK 
 FOREIGN KEY (VERSION) 
 REFERENCES ERPS.ZONE_HISTORY (VERSION));
 
ALTER TABLE ERPS.PRICING_REGION ADD (
  CONSTRAINT PRICING_REGION_HISTORY_FK 
 FOREIGN KEY (VERSION) 
 REFERENCES ERPS.ZONE_HISTORY (VERSION));
 
 ALTER TABLE ERPS.PRICING_REGION_ZIPS ADD (
  CONSTRAINT PRICING_REGION_ZIPS_HISTORY_FK 
 FOREIGN KEY (VERSION) 
 REFERENCES ERPS.ZONE_HISTORY (VERSION));



  DROP SEQUENCE ERPS.ZONEBATCH_SEQ;

CREATE SEQUENCE ERPS.ZONEBATCH_SEQ
  START WITH 24874
  MAXVALUE 999999999999999999999999999
  MINVALUE 1
  NOCYCLE
  NOCACHE
  NOORDER;


Grant all on ERPS.ZONEBATCH_SEQ to fdstore_prda;

grant all on ERPS.PRICING_REGION_ZIPS to fdstore_prda;
grant all on ERPS.PRICING_REGION to fdstore_prda;
grant all on ERPS.PRICING_ZONE to fdstore_prda;
grant all on ERPS.ZONE_HISTORY to fdstore_prda;

