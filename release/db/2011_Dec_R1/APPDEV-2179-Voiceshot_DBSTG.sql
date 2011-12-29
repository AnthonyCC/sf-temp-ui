create table cust.voiceshot_reasoncodes (
REASON_ID number(12) primary key,
REASON varchar(100)
)
/

GRANT DELETE, INSERT, SELECT, UPDATE ON cust.voiceshot_reasoncodes TO fdtrn_ststg01;

GRANT SELECT ON cust.voiceshot_reasoncodes TO appdev;

--GRANT DELETE, INSERT, SELECT, UPDATE ON cust.voiceshot_reasoncodes TO FDSTORE_PRDA;

CREATE TABLE CUST.VOICESHOT_SCHEDULED (
VS_ID	number(16) PRIMARY KEY,
CAMPAIGN_ID VARCHAR2(16),
REASON_ID number(12),
REDIAL VARCHAR2(1),
CREATED_BY_USER VARCHAR2(50),
CREATED_BY_DATE DATE,
START_TIME DATE,
CHANGE_BY_DATE  DATE,
CHANGE_BY_USER  VARCHAR2(16),
CALL_ID   VARCHAR2(25),
CALL_DATA_PULLED VARCHAR2(1),
CAMPAIGN_TYPE VARCHAR2(10)
)
/

GRANT SELECT, INSERT, DELETE, UPDATE on cust.VOICESHOT_SCHEDULED to fdtrn_ststg01;

GRANT SELECT on cust.VOICESHOT_SCHEDULED to APPDEV;

--GRANT DELETE, INSERT, SELECT, UPDATE ON cust.VOICESHOT_SCHEDULED TO FDSTORE_PRDA;

ALTER TABLE CUST.VOICESHOT_SCHEDULED
ADD CONSTRAINT fk_REASON_ID
  FOREIGN KEY (REASON_ID)
  REFERENCES CUST.voiceshot_reasoncodes(REASON_ID)
/

ALTER TABLE CUST.VOICESHOT_SCHEDULED
ADD CONSTRAINT fk_campaign_ID
  FOREIGN KEY (CAMPAIGN_ID)
  REFERENCES CUST.voiceshot_campaign(CAMPAIGN_ID)
/

CREATE TABLE CUST.VOICESHOT_LATEISSUE (
VS_ID NUMBER(16),
LATEISSUE_ID varchar2(16)
)
/

GRANT SELECT, INSERT, DELETE, UPDATE on cust.VOICESHOT_LATEISSUE to fdtrn_ststg01;

GRANT SELECT on cust.VOICESHOT_LATEISSUE to APPDEV;

--GRANT DELETE, INSERT, SELECT, UPDATE ON cust.VOICESHOT_LATEISSUE TO FDSTORE_PRDA;

ALTER TABLE CUST.VOICESHOT_LATEISSUE
ADD CONSTRAINT fk_LATEISSUE_ID
  FOREIGN KEY (LATEISSUE_ID)
  REFERENCES CUST.LATEISSUE(ID)
/


CREATE TABLE CUST.VOICESHOT_CUSTOMERS (
VS_ID  number(16),
CUSTOMER_ID VARCHAR2(16),
SALE_ID varchar(16),
PHONE VARCHAR2(20),
STATUS NUMBER(1),
REDIAL NUMBER(1),
LAST_REDIALED_DATE DATE
)
/

GRANT SELECT, INSERT, DELETE, UPDATE on cust.VOICESHOT_CUSTOMERS to fdtrn_ststg01;

GRANT SELECT on cust.VOICESHOT_CUSTOMERS to APPDEV;

--GRANT DELETE, INSERT, SELECT, UPDATE ON cust.VOICESHOT_CUSTOMERS TO FDSTORE_PRDA;

ALTER TABLE CUST.VOICESHOT_CUSTOMERS
ADD CONSTRAINT FK_VS_DETAIL_ID
  FOREIGN KEY (VS_ID)
  REFERENCES CUST.VOICESHOT_SCHEDULED(VS_ID)
/

alter table cust.voiceshot_campaign
add DELAY_IN_MINUTES number(3)
/

