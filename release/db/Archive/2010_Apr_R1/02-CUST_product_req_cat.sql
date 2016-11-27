CREATE TABLE CUST.PRODUCT_REQ_CAT
(
  ID             VARCHAR2(16 BYTE)               NOT NULL,
  CATID          VARCHAR2(128 BYTE)              NOT NULL,
  NAME           VARCHAR2(256 BYTE)              NOT NULL,
  CATIDNAME      VARCHAR2(384 BYTE)              NOT NULL,
  CREATE_DATE    DATE,
  OBSOLETE       VARCHAR2(1 BYTE)
)
TABLESPACE FDCUSDAT
PCTUSED    0
PCTFREE    10
INITRANS   1
MAXTRANS   255
STORAGE    (
            INITIAL          64K
            MINEXTENTS       1
            MAXEXTENTS       UNLIMITED
            PCTINCREASE      0
            BUFFER_POOL      DEFAULT
           )
LOGGING 
NOCOMPRESS 
NOCACHE
NOPARALLEL
MONITORING;


CREATE UNIQUE INDEX CUST.CATIDNAME_IDX ON CUST.PRODUCT_REQ_CAT
(CATIDNAME)
LOGGING
TABLESPACE FDCUSDAT
PCTFREE    10
INITRANS   2
MAXTRANS   255
STORAGE    (
            INITIAL          64K
            MINEXTENTS       1
            MAXEXTENTS       UNLIMITED
            PCTINCREASE      0
            BUFFER_POOL      DEFAULT
           )
NOPARALLEL;


ALTER TABLE CUST.PRODUCT_REQ_CAT ADD (
  CONSTRAINT PK_PRODREQCAT
 PRIMARY KEY
 (ID)
    USING INDEX 
    TABLESPACE FDCUSDAT
    PCTFREE    10
    INITRANS   2
    MAXTRANS   255
    STORAGE    (
                INITIAL          64K
                MINEXTENTS       1
                MAXEXTENTS       UNLIMITED
                PCTINCREASE      0
               ));
