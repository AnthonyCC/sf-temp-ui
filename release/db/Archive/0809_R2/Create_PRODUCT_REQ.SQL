CREATE TABLE CUST.PRODUCT_REQ
(
  ID                   VARCHAR2(16)        NOT NULL,
  CUSTOMER_ID          VARCHAR2(16)        NOT NULL,
  DEPT          	   VARCHAR2(16)        NOT NULL,
  CATEGORY             VARCHAR2(16),        
  SUB_CATEGORY         VARCHAR2(16),
  PRODUCT_NAME         VARCHAR2(256),
  PRODUCT_URL          VARCHAR2(256),
  STATUS               VARCHAR2(3)         NOT NULL,
  CREATE_DATE          DATE,  
  MOD_DATE          DATE,
  CONSTRAINT PRODUCT_REQ_PK PRIMARY KEY(ID),
  CONSTRAINT PRODUCT_REQ_FK1 FOREIGN KEY (CUSTOMER_ID) REFERENCES CUST.CUSTOMER (ID)
);
GRANT insert,update,select,delete on CUST.PRODUCT_REQ to FDSTORE_PRDA;
GRANT insert,update,select,delete on CUST.PRODUCT_REQ to FDSTORE_PRDB;
GRANT insert,update,select,delete on CUST.PRODUCT_REQ to APPDEV;
