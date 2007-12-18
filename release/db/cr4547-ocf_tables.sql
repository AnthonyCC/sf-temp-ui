
CREATE TABLE CUST.OCF_ACTION
(
  ID         VARCHAR2(16 BYTE)                 NOT NULL,
  TYPE       VARCHAR2(255 BYTE)                 NOT NULL,
  FLIGHT_ID  VARCHAR2(255 BYTE),
  ORDINAL    NUMBER(10)
)
LOGGING 
NOCACHE
NOPARALLEL;


CREATE TABLE CUST.OCF_CAMPAIGN
(
  ID           VARCHAR2(16 BYTE)               NOT NULL,
  NAME         VARCHAR2(255 BYTE),
  DESCRIPTION  VARCHAR2(255 BYTE),
  STARTDATE    TIMESTAMP(6),
  ENDDATE      TIMESTAMP(6)
)
LOGGING 
NOCACHE
NOPARALLEL;


CREATE TABLE CUST.OCF_CASEACTION
(
  ACTION_ID    VARCHAR2(16 BYTE)               NOT NULL,
  SUMMARY      VARCHAR2(255 BYTE)               NOT NULL,
  NOTE         VARCHAR2(255 BYTE),
  SUBJECTCODE  VARCHAR2(255 BYTE)               NOT NULL
)
LOGGING 
NOCACHE
NOPARALLEL;


CREATE TABLE CUST.OCF_EMAILER
(
  ACTION_ID             VARCHAR2(16 BYTE)      NOT NULL,
  MAILING_ID            VARCHAR2(255 BYTE)      NOT NULL,
  NOTIFY_EMAIL_ADDRESS  VARCHAR2(255 BYTE),
  NEXT_ENQUEUE_DATE     TIMESTAMP(6),
  ENQUEUE_FREQUENCY     NUMBER(10)              NOT NULL,
  SILVERPOP_JOB_ID      VARCHAR2(255 BYTE),
  SILVERPOP_FILE_PATH   VARCHAR2(255 BYTE)
)
LOGGING 
NOCACHE
NOPARALLEL;


CREATE TABLE CUST.OCF_EMAIL_STATS
(
  MAILING_ID            VARCHAR2(255 BYTE)      NOT NULL,
  CUSTOMER_ID           VARCHAR2(16 BYTE)      NOT NULL,
  OPEN_COUNT            NUMBER(10),
  CLICKSTREAM_COUNT     NUMBER(10),
  CLICKTHRU_COUNT       NUMBER(10),
  CONVERSION_COUNT      NUMBER(10),
  ATTACHMENT_COUNT      NUMBER(10),
  FORWARD_COUNT         NUMBER(10),
  MEDIA_COUNT           NUMBER(10),
  BOUNCE_COUNT          NUMBER(10),
  OPTOUT_COUNT          NUMBER(10),
  OPTIN_COUNT           NUMBER(10),
  ABUSE_COUNT           NUMBER(10),
  CHANGE_ADDRESS_COUNT  NUMBER(10),
  BLOCKED_COUNT         NUMBER(10),
  RESTRICTED_COUNT      NUMBER(10),
  OTHER_REPLY_COUNT     NUMBER(10),
  SUPPRESSION_COUNT     NUMBER(10)
)
LOGGING 
NOCACHE
NOPARALLEL;


CREATE TABLE CUST.OCF_FLIGHT
(
  ID           VARCHAR2(16 BYTE)               NOT NULL,
  NAME         VARCHAR2(255 BYTE),
  SCHEDULE     VARCHAR2(255 BYTE),
  MULTIPLE     NUMBER(1),
  LIST_ID      VARCHAR2(255 BYTE),
  CAMPAIGN_ID  VARCHAR2(255 BYTE)               NOT NULL
)
LOGGING 
NOCACHE
NOPARALLEL;


CREATE TABLE CUST.OCF_LIST
(
  ID           VARCHAR2(16 BYTE)               NOT NULL,
  NAME         VARCHAR2(255 BYTE)               NOT NULL,
  DESCRIPTION  VARCHAR2(255 BYTE),
  QUERY        VARCHAR2(1024 BYTE)              NOT NULL
)
LOGGING 
NOCACHE
NOPARALLEL;


CREATE TABLE CUST.OCF_PROFILEACTION
(
  ACTION_ID  VARCHAR2(16 BYTE)                 NOT NULL,
  KEY        VARCHAR2(255 BYTE)                 NOT NULL,
  VALUE      VARCHAR2(255 BYTE)                 NOT NULL
)
LOGGING 
NOCACHE
NOPARALLEL;


CREATE TABLE CUST.OCF_PROMOTIONPOPULATOR
(
  ACTION_ID  VARCHAR2(16 BYTE)                 NOT NULL,
  PROMOID    VARCHAR2(16 BYTE)                 NOT NULL
)
LOGGING 
NOCACHE
NOPARALLEL;


CREATE TABLE CUST.OCF_RUN_CUSTS
(
  RUN_ID       VARCHAR2(16 BYTE)               NOT NULL,
  CUSTOMER_ID  VARCHAR2(16 BYTE),
  EMAIL        VARCHAR2(255 BYTE)
)
LOGGING 
NOCACHE
NOPARALLEL;


CREATE TABLE CUST.OCF_RUN_LOG
(
  ID         VARCHAR2(16 BYTE)                 NOT NULL,
  FLIGHTID   VARCHAR2(255 BYTE),
  TIMESTAMP  TIMESTAMP(6)
)
LOGGING 
NOCACHE
NOPARALLEL;


ALTER TABLE CUST.OCF_ACTION ADD (
  CONSTRAINT PK_ACTION_ID PRIMARY KEY (ID));


ALTER TABLE CUST.OCF_CAMPAIGN ADD (
  CONSTRAINT PK_CAMPAIGN_ID PRIMARY KEY (ID));


ALTER TABLE CUST.OCF_CASEACTION ADD (
  CONSTRAINT PK_CASEACTION_ID PRIMARY KEY (ACTION_ID));


ALTER TABLE CUST.OCF_EMAILER ADD (
  CONSTRAINT PK_EMAILER_ID PRIMARY KEY (ACTION_ID));


ALTER TABLE CUST.OCF_EMAIL_STATS ADD (
  CONSTRAINT PK_EMAIL_STATS PRIMARY KEY (MAILING_ID, CUSTOMER_ID));


ALTER TABLE CUST.OCF_FLIGHT ADD (
  CONSTRAINT PK_FLIGHT_ID PRIMARY KEY (ID));

ALTER TABLE CUST.OCF_FLIGHT ADD (
  UNIQUE (LIST_ID));


ALTER TABLE CUST.OCF_LIST ADD (
  CONSTRAINT PK_LIST PRIMARY KEY (ID));


ALTER TABLE CUST.OCF_PROFILEACTION ADD (
  CONSTRAINT PK_PROFILEACTION PRIMARY KEY (ACTION_ID));


ALTER TABLE CUST.OCF_PROMOTIONPOPULATOR ADD (
  CONSTRAINT PK_PROMOTIONPOPULATOR PRIMARY KEY (ACTION_ID));


ALTER TABLE CUST.OCF_RUN_LOG ADD (
  CONSTRAINT PK_RUN_LOG PRIMARY KEY (ID));


ALTER TABLE CUST.OCF_ACTION ADD (
  CONSTRAINT FK_ACTION_FLIGHT FOREIGN KEY (FLIGHT_ID) 
    REFERENCES CUST.OCF_FLIGHT (ID));


ALTER TABLE CUST.OCF_CASEACTION ADD (
  CONSTRAINT FK_CASE_ACTION FOREIGN KEY (ACTION_ID) 
    REFERENCES CUST.OCF_ACTION (ID));


ALTER TABLE CUST.OCF_EMAILER ADD (
  CONSTRAINT FK_EMAILER_ACTION FOREIGN KEY (ACTION_ID) 
    REFERENCES CUST.OCF_ACTION (ID));


ALTER TABLE CUST.OCF_FLIGHT ADD (
  CONSTRAINT FK_CAMP_FLIGHT FOREIGN KEY (CAMPAIGN_ID) 
    REFERENCES CUST.OCF_CAMPAIGN (ID));


ALTER TABLE CUST.OCF_PROFILEACTION ADD (
  CONSTRAINT FK_PROFILE_ACTION FOREIGN KEY (ACTION_ID) 
    REFERENCES CUST.OCF_ACTION (ID));


ALTER TABLE CUST.OCF_PROMOTIONPOPULATOR ADD (
  CONSTRAINT FK_PROMO_ACTION FOREIGN KEY (ACTION_ID) 
    REFERENCES CUST.OCF_ACTION (ID));


ALTER TABLE CUST.OCF_RUN_CUSTS ADD (
  CONSTRAINT FK_RUNCUSTS_RUNLOG FOREIGN KEY (RUN_ID) 
    REFERENCES CUST.OCF_RUN_LOG (ID));
	
CREATE SEQUENCE CUST.HIBERNATE_SEQUENCE
  START WITH 21
  MAXVALUE 999999999999999999999999999
  MINVALUE 1
  NOCYCLE
  CACHE 20
  NOORDER;



grant select, update, insert, delete on cust.OCF_ACTION to fdstore_prda;
grant select, update, insert, delete on cust.OCF_CAMPAIGN to fdstore_prda;
grant select, update, insert, delete on cust.OCF_CASEACTION to fdstore_prda;
grant select, update, insert, delete on cust.OCF_EMAILER to fdstore_prda;
grant select, update, insert, delete on cust.OCF_EMAIL_STATS to fdstore_prda;
grant select, update, insert, delete on cust.OCF_FLIGHT to fdstore_prda;
grant select, update, insert, delete on cust.OCF_LIST to fdstore_prda;
grant select, update, insert, delete on cust.OCF_PROFILEACTION to fdstore_prda;
grant select, update, insert, delete on cust.OCF_PROMOTIONPOPULATOR to fdstore_prda;
grant select, update, insert, delete on cust.OCF_RUN_CUSTS to fdstore_prda;
grant select, update, insert, delete on cust.OCF_RUN_LOG to fdstore_prda;
grant select, update, insert, delete on cust.HIBERNATE_SEQUENCE to fdstore_prda;

grant select, update, insert, delete on cust.OCF_ACTION to fdstore_prdb;
grant select, update, insert, delete on cust.OCF_CAMPAIGN to fdstore_prdb;
grant select, update, insert, delete on cust.OCF_CASEACTION to fdstore_prdb;
grant select, update, insert, delete on cust.OCF_EMAILER to fdstore_prdb;
grant select, update, insert, delete on cust.OCF_EMAIL_STATS to fdstore_prdb;
grant select, update, insert, delete on cust.OCF_FLIGHT to fdstore_prdb;
grant select, update, insert, delete on cust.OCF_LIST to fdstore_prdb;
grant select, update, insert, delete on cust.OCF_PROFILEACTION to fdstore_prdb;
grant select, update, insert, delete on cust.OCF_PROMOTIONPOPULATOR to fdstore_prdb;
grant select, update, insert, delete on cust.OCF_RUN_CUSTS to fdstore_prdb;
grant select, update, insert, delete on cust.OCF_RUN_LOG to fdstore_prdb;
grant select, update, insert, delete on cust.HIBERNATE_SEQUENCE  to fdstore_prdb;

grant select on cust.OCF_ACTION to appdev;
grant select on cust.OCF_CAMPAIGN to appdev;
grant select on cust.OCF_CASEACTION to appdev;
grant select on cust.OCF_EMAILER to appdev;
grant select on cust.OCF_EMAIL_STATS to appdev;
grant select on cust.OCF_FLIGHT to appdev;
grant select on cust.OCF_LIST to appdev;
grant select on cust.OCF_PROFILEACTION to appdev;
grant select on cust.OCF_PROMOTIONPOPULATOR to appdev;
grant select on cust.OCF_RUN_CUSTS to appdev;
grant select on cust.OCF_RUN_LOG to appdev;
grant select on cust.HIBERNATE_SEQUENCE  to appdev;
