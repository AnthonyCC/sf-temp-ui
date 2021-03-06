CREATE TABLE CUST.TRANS_EMAIL_MASTER(
ID VARCHAR2(16) PRIMARY KEY,
TEMPLATE_ID VARCHAR2(16) NOT NULL,
ORDER_ID VARCHAR2(16) ,
CUSTOMER_ID VARCHAR2(16),
TRANS_TYPE VARCHAR2(50) NOT NULL,
STATUS VARCHAR2(20) NOT NULL,
ERROR_TYPE VARCHAR2(50),
ERROR_DESC VARCHAR2(500),
EMAIL_TYPE VARCHAR2(16) not null,
PROVIDER VARCHAR2(16) not null,
CROMOD_DATE  DATE
);

CREATE TABLE TRANS_EMAIL_DETAILS(
ID VARCHAR2(16) PRIMARY KEY,
TRANS_EMAIL_ID VARCHAR2(16),
FROM_ADDR VARCHAR2(50) NOT NULL,
TO_ADDR VARCHAR2(50) NOT NULL,
CC_ADDR VARCHAR2(250),
BCC_ADDR VARCHAR2(250),
SUBJECT VARCHAR2(500) NOT NULL,
TEMPLATE_CONTENT VARCHAR2(1000) NOT NULL
);

ALTER TABLE CUST.TRANS_EMAIL_DETAILS ADD (
  CONSTRAINT TEMAILS_DTLS_FK 
 FOREIGN KEY (TRANS_EMAIL_ID) 
 REFERENCES CUST.TRANS_EMAIL_MASTER (ID));


CREATE TABLE TRANS_EMAIL_TYPES(
ID VARCHAR2(16) PRIMARY KEY,
PROVIDER VARCHAR2(20) NOT NULL,
TEMPLATE_ID VARCHAR2(16) NOT NULL,
TRANS_TYPE VARCHAR2(50) NOT NULL,
EMAIL_TYPE VARCHAR2(20) NOT NULL,
FROM_ADDR VARCHAR2(50) ,
SUBJECT VARCHAR2(250), 
DESCRIPTION VARCHAR2(100),
ACTIVE VARCHAR2(1) 
);

alter table TRANS_EMAIL_TYPES add FROM_ADDR varchar2(50) not null;

alter table TRANS_EMAIL_TYPES add SUBJECT varchar2(250);


GRANT ALL on CUST.TRANS_EMAIL_TYPES TO FDSTORE_PRDA

GRANT ALL on CUST.TRANS_EMAIL_MASTER TO FDSTORE_PRDA

GRANT ALL on CUST.TRANS_EMAIL_DETAILS TO FDSTORE_PRDA