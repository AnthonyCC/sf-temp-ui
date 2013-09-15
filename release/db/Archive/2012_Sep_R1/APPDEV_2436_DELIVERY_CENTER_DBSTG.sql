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
   PD_RECIPIENT,
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
          pd_recipient,
          INSERT_TIMESTAMP
     FROM ac_stagetrain.cartonstatus@AIRCLICRW.NYC.FRESHDIRECT.COM;

     
CREATE TABLE DLV.CREATECASE_EXPORT
(
  LAST_EXPORT  DATE,
  SUCCESS      CHAR(1 BYTE)                     DEFAULT 'N'
);


CREATE INDEX DLV.CC_EXPORT_IDX ON DLV.CREATECASE_EXPORT (LAST_EXPORT);
CREATE INDEX CUST.CI_CARTON_ID_IDX ON CUST.CARTON_INFO (CARTON_NUMBER);

GRANT DELETE, INSERT, SELECT, UPDATE ON  DLV.CREATECASE_EXPORT TO fdstore_ststg01;
GRANT SELECT ON DLV.CREATECASE_EXPORT TO APPDEV;
GRANT SELECT ON DLV.CARTONTRACKING TO fdstore_ststg01;
GRANT SELECT ON DLV.CARTONTRACKING TO APPDEV;


Insert into CUST.AGENT
   (ID, USER_ID, PASSWORD, FIRST_NAME, LAST_NAME, ACTIVE, ROLE, CREATED, MASQUERADE_ALLOWED, LDAP_ID)
 Values
   (CUST.SYSTEM_SEQ.nextval , 'systemdriver', 'systemdriver', 'SystemDriver', 'User', 
    'X', 'SYS',sysdate, 'N', 'systemdriver');

Insert into DLV.CREATECASE_EXPORT
   (LAST_EXPORT, SUCCESS)
 Values
   (TO_DATE('10/17/2012 15:30:09', 'MM/DD/YYYY HH24:MI:SS'), 'Y');

Insert into CUST.CASE_SUBJECT
   (CODE, NAME, CASE_QUEUE, CASE_PRIORITY, CARTONS_REQ)
 Values
   ('DSQ-055', 'Not Home -Scanned', 'DSQ', 'MD', 'X');

