


CREATE SEQUENCE CUST.COUPON_SEQ
 START WITH     1
 INCREMENT BY   1
 NOCACHE
 NOCYCLE;

CREATE TABLE CUST.FDCOUPON_HISTORY
(
  VERSION  NUMBER(10)                           NOT NULL,
  DATE_CREATED        DATE,
  STATUS VARCHAR2(10 BYTE) 
);

ALTER TABLE CUST.FDCOUPON_HISTORY ADD (
    CONSTRAINT FDCOUPON_HISTORY_PK
    PRIMARY KEY (VERSION)
);
-- CREATE TABLE CUST.FDCOUPON
CREATE TABLE CUST.FDCOUPON
(
	ID    VARCHAR2(16 BYTE)    NOT NULL,	
  COUPON_ID    VARCHAR2(9 BYTE)    NOT NULL,
  VERSION    NUMBER(10) NOT NULL,
  CATEGORY    VARCHAR2(100 BYTE),
  BRAND         VARCHAR2(100 BYTE),
  MANUFACTURER  VARCHAR2(100 BYTE),
  VALUE         NUMBER(10,2),
  REQUIRED_QTY  NUMBER(2),
  START_DATE DATE,
  EXP_DATE      DATE,
  SHORT_DESC    VARCHAR2(100 BYTE),
  LONG_DESC     VARCHAR2(200 BYTE),
  PURCHASE_REQ_DESC    VARCHAR2(1000 BYTE),
  PRIORITY      NUMBER(3),
  CREATED_TIME  TIMESTAMP,
  LAST_UPDATED_TIME    TIMESTAMP,
  IS_EXPIRED        CHAR(1),
  OFFER_TYPE varchar2(2 byte),
  ENABLED varchar2(1 byte)
);

ALTER TABLE CUST.FDCOUPON ADD (
    CONSTRAINT FDCOUPON_PK1
    PRIMARY KEY (ID)
);
ALTER TABLE CUST.FDCOUPON ADD (
    CONSTRAINT FDCOUPON_PK2
    UNIQUE (VERSION,COUPON_ID)
);
ALTER TABLE CUST.FDCOUPON ADD (
  CONSTRAINT FDCOUPON_FK1
 FOREIGN KEY (VERSION) 
 REFERENCES CUST.FDCOUPON_HISTORY (VERSION));

CREATE INDEX FDCOUPON_COUPON_ID_IDX ON CUST.FDCOUPON (COUPON_ID);
CREATE INDEX FDCOUPON_VERSION_IDX ON CUST.FDCOUPON (VERSION);

-- CREATE TABLE CUST.COUPONLINE
CREATE TABLE CUST.COUPONLINE
(
  ID    VARCHAR2(16 BYTE)    NOT NULL,
  COUPON_ID    VARCHAR2(9 BYTE)    NOT NULL,
  ORDERLINE_ID  VARCHAR2(16 BYTE)    NOT NULL,
  VERSION    NUMBER(10) NOT NULL,
  DISC_AMT  NUMBER(10,2),
  DISC_TYPE NUMBER(10),
  COUPON_DESC   VARCHAR2(200 BYTE),
  REQUIRED_QTY  NUMBER(2)
);
ALTER TABLE CUST.COUPONLINE ADD (
    CONSTRAINT COUPONLINE_PK
    PRIMARY KEY (ID)
);

ALTER TABLE CUST.COUPONLINE ADD (
  CONSTRAINT COUPONLINE_FK1
 FOREIGN KEY (ORDERLINE_ID) 
 REFERENCES CUST.ORDERLINE (ID));

 ALTER TABLE CUST.COUPONLINE ADD (
  CONSTRAINT COUPONLINE_FK2
 FOREIGN KEY (VERSION) 
 REFERENCES CUST.FDCOUPON_HISTORY (VERSION));

CREATE INDEX COUPONLINE_ORDERLINE_ID_IDX ON CUST.COUPONLINE (ORDERLINE_ID);

-- CREATE TABLE CUST.FDCOUPON_REWARD_UPC
CREATE TABLE CUST.FDCOUPON_REWARD_UPC
(
  FDCOUPON_ID    VARCHAR2(16 BYTE)    NOT NULL,
  UPC        VARCHAR2(20 BYTE)    NOT NULL,
  UPC_DESC     VARCHAR2(100 BYTE)
);

ALTER TABLE CUST.FDCOUPON_REWARD_UPC ADD (
    CONSTRAINT FDCOUPON_REWARD_UPC_PK
    PRIMARY KEY (FDCOUPON_ID,UPC)
);

ALTER TABLE CUST.FDCOUPON_REWARD_UPC ADD (
  CONSTRAINT FDCOUPON_REWARD_UPC_FK1
 FOREIGN KEY (FDCOUPON_ID) 
 REFERENCES CUST.FDCOUPON (ID));

 -- CREATE TABLE CUST.FDCOUPON_REQ_UPC
CREATE TABLE CUST.FDCOUPON_REQ_UPC
(
  FDCOUPON_ID    VARCHAR2(16 BYTE)    NOT NULL,
  UPC        VARCHAR2(20 BYTE)    NOT NULL,
  UPC_DESC     VARCHAR2(100 BYTE),
  IS_REQUIRED        CHAR(1)
);

ALTER TABLE CUST.FDCOUPON_REQ_UPC ADD (
    CONSTRAINT FDCOUPON_REQ_UPC_PK
    PRIMARY KEY (FDCOUPON_ID,UPC)
);

ALTER TABLE CUST.FDCOUPON_REQ_UPC ADD (
  CONSTRAINT FDCOUPON_REQ_UPC_FK1
 FOREIGN KEY (FDCOUPON_ID) 
 REFERENCES CUST.FDCOUPON (ID));

CREATE INDEX FDCOUPON_REQ_UPC_IDX ON CUST.FDCOUPON_REQ_UPC (UPC);

 CREATE TABLE CUST.COUPON_TRANS
 (
  ID    VARCHAR2(16 BYTE)    NOT NULL,
  SALESACTION_ID  VARCHAR2(16 BYTE)    NOT NULL,
  TRANS_STATUS    CHAR(1),
  TRANS_TYPE      VARCHAR2(100 BYTE),
  ERROR_MSG       VARCHAR2(255 BYTE),
  ERROR_DETAILS         VARCHAR2(255 BYTE),
  CREATE_TIME       DATE,
  TRANS_TIME       DATE
);

ALTER TABLE CUST.COUPON_TRANS ADD (
    CONSTRAINT COUPON_TRANS_PK
    PRIMARY KEY (ID)
);

ALTER TABLE CUST.COUPON_TRANS ADD (
  CONSTRAINT COUPON_TRANS_FK1
 FOREIGN KEY (SALESACTION_ID) 
 REFERENCES CUST.SALESACTION (ID));

CREATE INDEX COUPON_TRANS_SALESACTION_IDX ON CUST.COUPON_TRANS (SALESACTION_ID);
CREATE INDEX COUPON_TRANS_CREATE_TIME_IDX ON CUST.COUPON_TRANS (CREATE_TIME);

CREATE TABLE CUST.COUPON_TRANS_DETAILS
 (
  ID    VARCHAR2(16 BYTE)    NOT NULL,
  COUPON_TRANS_ID  VARCHAR2(16 BYTE)    NOT NULL,
  COUPON_ID VARCHAR2(9 BYTE)    NOT NULL,
  COUPONLINE_ID VARCHAR2(16 BYTE) ,
  DISCOUNT_AMT VARCHAR2(10 BYTE),
  TRANS_TIME       DATE
);

ALTER TABLE CUST.COUPON_TRANS_DETAILS ADD (
    CONSTRAINT COUPON_TRANS_DETAILS_PK
    PRIMARY KEY (ID)
);

ALTER TABLE CUST.COUPON_TRANS_DETAILS ADD (
  CONSTRAINT COUPON_TRANS_DETAILS_FK1
 FOREIGN KEY (COUPON_TRANS_ID) 
 REFERENCES CUST.COUPON_TRANS (ID));

CREATE INDEX COUPON_TRANS_DETAILS_IDX ON CUST.COUPON_TRANS_DETAILS (COUPON_TRANS_ID);

ALTER TABLE  CUST.INVOICELINE ADD(COUPON_DISC_AMT NUMBER(10,2));

CREATE TABLE MIS.COUPON_ACTIVITY_LOG
 (
  ID    NUMBER    NOT NULL,
  FDUSER_ID VARCHAR2(16 BYTE),  
  CUSTOMER_ID  VARCHAR2(16 BYTE),
  TRANS_TYPE      VARCHAR2(100 BYTE) NOT NULL,
  START_TIME       DATE NOT NULL,
  END_TIME DATE NOT NULL,
  SALE_ID VARCHAR2(16 BYTE),
  COUPON_ID VARCHAR2(16 BYTE),
  DETAILS VARCHAR2(255 BYTE),
  SOURCE            VARCHAR2(3 BYTE)            NOT NULL,
  INITIATOR         VARCHAR2(80 BYTE),
);

ALTER TABLE MIS.COUPON_ACTIVITY_LOG ADD (
    CONSTRAINT COUPON_ACTIVITY_LOG_PK
    PRIMARY KEY (ID)
);


ALTER TABLE CUST.ORDERLINE ADD( UPC  VARCHAR2(20 BYTE));
ALTER TABLE DLV.MUNICIPALITY_INFO add(TAXATION_TYPE varchar2(1 byte));

update DLV.MUNICIPALITY_INFO mi set MI.TAXATION_TYPE='1' where MI.STATE in ('NY','NJ');
update DLV.MUNICIPALITY_INFO mi set MI.TAXATION_TYPE='0' where MI.STATE in ('PA','CT');

ALTER TABLE CUST.ORDERLINE add(TAXATION_TYPE varchar2(1 byte));
ALTER TABLE CUST.chargeline add(TAXATION_TYPE varchar2(1 byte));

CREATE SEQUENCE MIS.COUPON_LOG_SEQUENCE
  START WITH 1
  MAXVALUE 999999999999999999999999999
  MINVALUE 1
  NOCYCLE
  CACHE 20
  NOORDER;



Insert into CMS.LOOKUP
   (LOOKUPTYPE_CODE, CODE, LABEL, DESCRIPTION, ORDINAL)
 Values
   ('Category.PRODUCT_PROMOTION_TYPE', 'E_COUPONS', 'E-Coupons', 'E-Coupons', 3);

Insert into CUST.PROFILE_ATTR_NAME
   (NAME, DESCRIPTION, CATEGORY, IS_EDITABLE)
 Values
   ('COUPONS_ELIGIBLE', 'COUPONS Eligibility', 'General', 'X');
