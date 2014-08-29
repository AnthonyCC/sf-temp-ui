/* TRANSP.ZONE */ 
ALTER TABLE TRANSP.ZONE ADD (NEXTSTOP_SMS_ENABLED VARCHAR2(1), DLV_UNATTENDED_SMS_ENABLED  VARCHAR2(1 BYTE), DLV_ATTEMPTED_SMS_ENABLED VARCHAR2(1 BYTE)); 

/* TRANSP.HANDOFF_BATCHSTOP */ 
ALTER TABLE TRANSP.HANDOFF_BATCHSTOP ADD (MOBILE_NUMBER  VARCHAR2(16));

CREATE INDEX TRANSP.HANDOFF_BATCHSTOP_MN_IDX ON TRANSP.HANDOFF_BATCHSTOP (MOBILE_NUMBER);

/* CUST.CUSTOMERINFO */
ALTER TABLE CUST.CUSTOMERINFO 
ADD (ORDER_NOTIFICATION  VARCHAR2(15), ORDEREXCEPTION_NOTIFICATION  VARCHAR2(15), SMS_OFFERS_ALERT  VARCHAR2(15), PARTNERMESSAGE_NOTIFICATION  VARCHAR2(15), SMS_OPTIN_DATE DATE, SMS_PREFERENCE_FLAG  VARCHAR2(16));


/* CUST.MIS.SMS_ALERT_CAPTURE */
CREATE TABLE MIS.SMS_ALERT_CAPTURE
(
  ID             NUMBER                         NOT NULL,
  USER_ID        VARCHAR2(16)                   NOT NULL,
  ALERT_TYPE     VARCHAR2(20)                   NOT NULL,
  CREATE_DATE    DATE,
  INSERT_DATE    DATE,
  STATUS         VARCHAR2(16)                   NOT NULL,
  ERROR          NUMBER                         NOT NULL,
  ERROR_DESC     VARCHAR2(200),
  MOBILE_NUMBER  VARCHAR2(20)                   NOT NULL,
  MESSAGE        VARCHAR2(200)                  NOT NULL,
  STI_SMS_ID     NUMBER,
  ORDER_ID       VARCHAR2(16)
);

ALTER TABLE MIS.SMS_ALERT_CAPTURE ADD (CONSTRAINT SMS_ALERT_CAPTURE_PK PRIMARY KEY (ID));

CREATE INDEX MIS.SMS_ALERT_CAPTURE_IDX ON MIS.SMS_ALERT_CAPTURE (ORDER_ID);

/* MIS.SMS_ALERT_SEQ */ 
CREATE SEQUENCE MIS.SMS_ALERT_SEQ
START WITH 1
INCREMENT BY 1
MINVALUE 1
MAXVALUE 999999999999999999999999999
CACHE 20
NOCYCLE 
NOORDER; 

/* MIS.SMS_RECEIVED */
CREATE TABLE MIS.SMS_RECEIVED
(
  SMS_ID         NUMBER                         NOT NULL,
  CUSTOMER_ID    VARCHAR2(16)                   NOT NULL,
  MOBILE_NUMBER  VARCHAR2(16)                   NOT NULL,
  SHORT_CODE     VARCHAR2(16),
  CARRIER_NAME   VARCHAR2(50),
  RECEIVED_DATE  DATE,
  MESSAGE        VARCHAR2(200)                  NOT NULL
);

ALTER TABLE MIS.SMS_RECEIVED ADD (CONSTRAINT SMS_RECEIVED_PK PRIMARY KEY(SMS_ID));

CREATE INDEX MIS.SMS_RECEIVED_IDX ON MIS.SMS_RECEIVED (MOBILE_NUMBER);

/*DLV.SMS_TRANSIT_EXPORT*/
CREATE TABLE DLV.SMS_TRANSIT_EXPORT
(
  LAST_EXPORT  DATE,
  SMS_ALERT_TYPE  VARCHAR2(16),
  SUCCESS      VARCHAR2(1)
);

CREATE INDEX DLV.SMS_TRANSIT_EXPORT_IDX ON DLV.SMS_TRANSIT_EXPORT (LAST_EXPORT);

/* DLV.NEXTSTOP_SMS */
CREATE TABLE DLV.NEXTSTOP_SMS
(
  TRANSIT_DATE          DATE,
  ROUTE                 VARCHAR2(250),
  EMPLOYEE              VARCHAR2(250),
  LOCATION_SOURCE       VARCHAR2(250),
  LOCATION_DESTINATION  VARCHAR2(250),
  TRANSACTIONID         VARCHAR2(250),
  INSERT_TIMESTAMP      DATE
);

CREATE INDEX DLV.NEXTSTOP_SMS_IDX ON DLV.NEXTSTOP_SMS (INSERT_TIMESTAMP);

CREATE INDEX DLV.NEXTSTOP_SMS_ROUTE_IDX ON DLV.NEXTSTOP_SMS (ROUTE);