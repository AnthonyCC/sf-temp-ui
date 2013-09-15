CREATE TABLE TRANSP.EMPTIMESHEET_EXPORT
(
  LAST_EXPORT  DATE,
  SUCCESS      CHAR(1 BYTE)                     DEFAULT 'N'
);

CREATE INDEX TRANSP.ETS_EXPORT_IDX ON TRANSP.EMPTIMESHEET_EXPORT
(LAST_EXPORT);

GRANT SELECT,INSERT,UPDATE,DELETE ON TRANSP.EMPTIMESHEET_EXPORT TO FDSTORE_STSTG01, FDTRN_STSTG01;


CREATE TABLE TRANSP.EMP_TIMESHEET(ACTION               VARCHAR2(250 BYTE),
  ROUTE                VARCHAR2(250 BYTE),
  DEPOTDRIVERID        VARCHAR2(250 BYTE),
  RUNNERBADGEID        VARCHAR2(250 BYTE),
  NEXTELID             VARCHAR2(250 BYTE),
  INSERT_TIMESTAMP     DATE,
  CLOCK_IN_TIMESTAMP   DATE,
  CLOCK_IN_TIME        VARCHAR2(250 BYTE),
  CLOCK_OUT_TIMESTAMP  DATE,
  CLOCK_OUT_TIME       VARCHAR2(250 BYTE),
  DEPOT_SOURCE         VARCHAR2(250 BYTE),
  DEPOT_DESTINATION    VARCHAR2(250 BYTE),
  TIP                  VARCHAR2(250 BYTE),
  BREAK                VARCHAR2(250 BYTE),
  SIGNATURE            LONG RAW);

CREATE INDEX TRANSP.IDX_EMP_TS ON TRANSP.EMP_TIMESHEET(INSERT_TIMESTAMP);

GRANT SELECT,INSERT,UPDATE,DELETE ON TRANSP.EMP_TIMESHEET TO FDSTORE_STSTG01, FDTRN_STSTG01;

CREATE OR REPLACE PROCEDURE TRANSP.EMP_TIMESHEET_CAPTURE  IS
   BEGIN
   DECLARE
   r_date               DATE;
   curr_date            DATE := SYSDATE;
   
   CURSOR tsCursor
   IS
      SELECT * FROM AC_STAGETRAIN.RUNNERTRACKINGSTATUS@AIRCLICRW.NYC.FRESHDIRECT.COM RT 
       WHERE RT.ACTION = 'Clock Out' AND RT.INSERT_TIMESTAMP BETWEEN r_date AND curr_date;
          
   timesheet_data_rec   tsCursor%ROWTYPE;
BEGIN
   SELECT NVL (MAX (LAST_EXPORT), SYSDATE - 1 / 24)
     INTO r_date
     FROM TRANSP.EMPTIMESHEET_EXPORT
    WHERE SUCCESS = 'Y';

   OPEN tsCursor;

   LOOP
      FETCH tsCursor INTO timesheet_data_rec;

      EXIT WHEN tsCursor%NOTFOUND;

      IF UTL_RAW.LENGTH (timesheet_data_rec.SIGNATURE) > 0
      THEN
         INSERT INTO TRANSP.EMP_TIMESHEET (ACTION,
  ROUTE,
  DEPOTDRIVERID,
  RUNNERBADGEID,
  NEXTELID,
  INSERT_TIMESTAMP,
  CLOCK_IN_TIMESTAMP,
  CLOCK_IN_TIME,
  CLOCK_OUT_TIMESTAMP,
  CLOCK_OUT_TIME,
  DEPOT_SOURCE,
  DEPOT_DESTINATION,
  TIP,
  BREAK,
  SIGNATURE)
              VALUES (timesheet_data_rec.ACTION,
                      timesheet_data_rec.ROUTE,
                      timesheet_data_rec.DEPOTDRIVERID,
                      timesheet_data_rec.RUNNERBADGEID,
                      timesheet_data_rec.NEXTELID,
                      timesheet_data_rec.INSERT_TIMESTAMP,
                      timesheet_data_rec.CLOCK_IN_TIMESTAMP,
                      timesheet_data_rec.CLOCK_IN_TIME,
                      timesheet_data_rec.CLOCK_OUT_TIMESTAMP,
                      timesheet_data_rec.CLOCK_OUT_TIME,
                      timesheet_data_rec.DEPOT_SOURCE,
                      timesheet_data_rec.DEPOT_DESTINATION,
                      timesheet_data_rec.TIP,
                      timesheet_data_rec.BREAK,
                      timesheet_data_rec.SIGNATURE);
      
      END IF;
   END LOOP;

   INSERT INTO TRANSP.EMPTIMESHEET_EXPORT (LAST_EXPORT, SUCCESS)
        VALUES (curr_date, 'Y');

   COMMIT;

   CLOSE tsCursor;
   
   EXCEPTION
   WHEN OTHERS
   THEN
       IF (tsCursor%ISOPEN)
       THEN
           CLOSE tsCursor;
       END IF;
       
       ROLLBACK;
   
END;
END;
/

GRANT EXECUTE ON TRANSP.EMP_TIMESHEET_CAPTURE TO FDSTORE_STSTG01, FDTRN_STSTG01;




