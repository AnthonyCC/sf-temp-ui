alter table CUST.PAYMENT modify SEQUENCE_NUMBER     VARCHAR2(40 BYTE);
  
ALTER TABLE  cust.payment 
 ADD (
 TRANS_REF_INDEX    VARCHAR2(4 BYTE),
PROFILE_ID    VARCHAR2(22 BYTE),
GATEWAY_ID    VARCHAR2(1 BYTE),
GATEWAY_ORDER  VARCHAR2(30 BYTE)
);

ALTER TABLE  CUST.PAYMENTINFO_NEW
 ADD (
PROFILE_ID    VARCHAR2(22 BYTE),
ACCOUNT_NUM_MASKED VARCHAR2(25 BYTE) 
)

ALTER TABLE  CUST.PAYMENTMETHOD_NEW
 ADD (
PROFILE_ID    VARCHAR2(22 BYTE),
ACCOUNT_NUM_MASKED VARCHAR2(25 BYTE) 
)

CREATE SEQUENCE CUST.BIN_INFO_SEQ
  START WITH 1
  MAXVALUE 999999999999999999999999999
  MINVALUE 1
  NOCYCLE
  NOCACHE
  NOORDER;
  
  CREATE SEQUENCE CUST.BIN_HISTORY_SEQ
  START WITH 1
  MAXVALUE 999999999999999999999999999
  MINVALUE 1
  NOCYCLE
  NOCACHE
  NOORDER;
  
 CREATE TABLE CUST.BIN_HISTORY
(
  VERSION       NUMBER(10)                      NOT NULL,
  DATE_CREATED  DATE,
  STATUS        VARCHAR2(10 BYTE)
);

CREATE UNIQUE INDEX CUST.BIN_HISTORY_PK ON CUST.BIN_HISTORY(VERSION);


ALTER TABLE CUST.BIN_HISTORY ADD (
  CONSTRAINT BIN_HISTORY_PK
 PRIMARY KEY
 (VERSION));
 
 CREATE TABLE CUST.BIN_INFO
 (
   ID       VARCHAR2(16 BYTE)                     NOT NULL,
   RECORD_SEQ VARCHAR2(16 BYTE) ,
   VERSION NUMBER(10)                      NOT NULL,
   LOW_RANGE VARCHAR2(16 BYTE),
   HIGH_RANGE VARCHAR2(16 BYTE) ,
   CARD_TYPE VARCHAR2(10 BYTE)
    
 );
 
 
 CREATE UNIQUE INDEX CUST.BIN_INFO_PK ON CUST.BIN_INFO(ID);
 
 
 ALTER TABLE CUST.BIN_INFO ADD (
   CONSTRAINT BIN_INFO_PK
  PRIMARY KEY
 (ID));
 
 CREATE TABLE MIS.GATEWAY_ACTIVITY_LOG
 (
   ID                   NUMBER                   NOT NULL,
   TRANSACTION_TIME     DATE                     NOT NULL,
   TRANSACTION_TYPE     VARCHAR2(30 BYTE)        NOT NULL,
   GATEWAY              VARCHAR2(25 BYTE)        NOT NULL,
   MERCHANT             VARCHAR2(25 BYTE),
   PROFILE_ID           VARCHAR2(22 BYTE),
   CUSTOMER_ID          VARCHAR2(25 BYTE),
   PAYMENT_TYPE         VARCHAR2(20 BYTE),
   CARD_TYPE            VARCHAR2(20 BYTE),
   BANK_ACCOUNT_TYPE    VARCHAR2(20 BYTE),
   ACCOUNT_NUM_LAST4    VARCHAR2(4 BYTE),
   EXP_DATE             DATE,
   CUSTOMER_NAME        VARCHAR2(30 BYTE),
   CUSTOMER_ADDRESS1    VARCHAR2(30 BYTE),
   CUSTOMER_ADDRESS2    VARCHAR2(30 BYTE),
   CUSTOMER_CITY        VARCHAR2(20 BYTE),
   CUSTOMER_STATE       VARCHAR2(2 BYTE),
   CUSTOMER_ZIP         VARCHAR2(10 BYTE),
   CUSTOMER_COUNTRY     VARCHAR2(30 BYTE),
   ORDER_ID             VARCHAR2(30 BYTE),
   TX_REF_NUM           VARCHAR2(40 BYTE),
   TX_REF_IDX           VARCHAR2(4 BYTE),
   IS_PROCESSED         VARCHAR2(1 BYTE),
   IS_APPROVED          VARCHAR2(1 BYTE),
   IS_DECLINED          VARCHAR2(1 BYTE),
   IS_PROCESSING_ERROR  VARCHAR2(1 BYTE),
   AUTH_CODE            VARCHAR2(6 BYTE),
   IS_AVS_MATCH         VARCHAR2(1 BYTE),
   IS_CVV_MATCH         VARCHAR2(1 BYTE),
   AVS_RESPONSE_CODE    VARCHAR2(2 BYTE),
   CVV_RESPONSE_CODE    VARCHAR2(1 BYTE),
   RESPONSE_CODE        VARCHAR2(6 BYTE),
   RESPONSE_CODE_ALT    VARCHAR2(6 BYTE),
   STATUS_CODE          VARCHAR2(10 BYTE),
   STATUS_MSG           VARCHAR2(250 BYTE),
   AMOUNT               FLOAT(126)
 )
 ;
 
 
 CREATE UNIQUE INDEX MIS.GATEWAY_ACTIVITY_LOG_PK ON MIS.GATEWAY_ACTIVITY_LOG
 (ID)
 ;
 
 
 CREATE INDEX MIS.IDX_CUSTOMER ON MIS.GATEWAY_ACTIVITY_LOG
 (CUSTOMER_ID)
 ;
 
 
 CREATE INDEX MIS.IDX_GATEWAYTRANSDATETIME ON MIS.GATEWAY_ACTIVITY_LOG
 (TRANSACTION_TIME)
 ;
 
 
 CREATE INDEX MIS.IDX_ORDER_ID ON MIS.GATEWAY_ACTIVITY_LOG
 (ORDER_ID)
 ;

Insert into CUST.PROFILE_ATTR_NAME
   (NAME, DESCRIPTION, CATEGORY, IS_EDITABLE)
 Values
   ('siteFeature.Paymentech', 'Paymentech Gateway', 'Feature', 'X');
   
CREATE SEQUENCE MIS.PAYMENT_MIGRATION_BATCH_SEQ
  START WITH 1
  MAXVALUE 999999999999999999999999999
  MINVALUE 1
  NOCYCLE
  CACHE 20
  NOORDER;
  
CREATE TABLE MIS.PAYMENT_MIGRATION_CUSTOMER
(
  CUSTOMER_ID  VARCHAR2(16 BYTE),
  BATCH_ID     VARCHAR2(16 BYTE),
  IS_IGNORE    VARCHAR2(1 BYTE)
);

CREATE TABLE MIS.PAYMENT_MIGRATION_OUTPUT
(
  CUSTOMER_ID       VARCHAR2(16 BYTE),
  INSERT_TIMESTAMP  DATE,
  PROFILE_CREATED   VARCHAR2(1 BYTE)
);

CREATE TABLE MIS.PAYMENT_MIGRATION_OUTPUT_DTL
(
  CUSTOMER_ID       VARCHAR2(16 BYTE),
  INSERT_TIMESTAMP  DATE,
  PAYMENTMETHOD_ID  VARCHAR2(16 BYTE),
  STATUS            VARCHAR2(20 BYTE),
  EXCEPTION_MSG     VARCHAR2(250 BYTE)
);

CREATE TABLE MIS.PAYMENT_MIGRATION_BATCH
(
  BATCH_ID           VARCHAR2(16 BYTE),
  CREATED_BY         VARCHAR2(16 BYTE),
  CREATED_TIMESTAMP  DATE,
  STATUS             VARCHAR2(16 BYTE)
);


ALTER TABLE MIS.PAYMENT_MIGRATION_BATCH ADD (
  PRIMARY KEY
 (BATCH_ID)
    );
  
  