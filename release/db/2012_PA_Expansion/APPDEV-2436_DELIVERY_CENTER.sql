CREATE TABLE CUST.IVR_CALLLOG
( 
  ID      VARCHAR2(64 BYTE)    NOT NULL,
  CALLERID      VARCHAR2(64 BYTE)    NOT NULL,
  ORDERNUMBER  VARCHAR2(256 BYTE),
  CALLTIME  DATE NOT NULL,
  CALLDURATION  NUMBER(5),
  TALKTIME  NUMBER(5),
  PHONE_NUMBER  VARCHAR2(256 BYTE) NOT NULL,
  CALL_OUTCOME  VARCHAR2(256 BYTE),
  MENU_OPTION   VARCHAR2(64 BYTE) 
);

ALTER TABLE CUST.IVR_CALLLOG ADD ( CONSTRAINT PK_IVRCALLLOG PRIMARY KEY(ID));

CREATE INDEX CUST.IVR_CALLLOG$ORDERNUMBER ON CUST.IVR_CALLLOG(ORDERNUMBER);

CREATE OR REPLACE FORCE VIEW DLV.CARTONTRACKING
(
   SCANDATE,
   WEBORDERNUM,
   CARTONID,
   ACTION,
   CARTONSTATUS,
   EMPLOYEE,
   ROUTE,
   STOP,
   NEXTELID,
   USERID,
   DELIVEREDTO,
   RETURNREASON,
   EVENTID,
   INSERT_TIMESTAMP
)
AS
   SELECT scandate,
          webordernum,
          cartonid,
          action,
          cartonstatus,
          employee,
          route,
          stop,
          nextelid,
          userid,
          deliveredto,
          returnreason,
          eventid,
          INSERT_TIMESTAMP
     FROM AIRCLIC_PROD.cartonstatus@AIRCLICRW.NYC.FRESHDIRECT.COM;


GRANT SELECT ON DLV.CARTONTRACKING TO fdstore_stprd01;
GRANT SELECT ON DLV.CARTONTRACKING TO APPDEV;
GRANT SELECT, INSERT, UPDATE, DELETE ON CUST.IVR_CALLLOG TO fdstore_stprd01;
GRANT SELECT ON CUST.IVR_CALLLOG TO APPDEV;