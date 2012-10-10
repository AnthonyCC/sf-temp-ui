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
     FROM AIRCLIC_PROD.cartontracking@AIRCLICRW.NYC.FRESHDIRECT.COM;


GRANT SELECT ON DLV.CARTONTRACKING TO fdstore_stprd01;
GRANT SELECT ON DLV.CARTONTRACKING TO APPDEV;