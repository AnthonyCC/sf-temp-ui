
DROP MATERIALIZED VIEW TRANSP.KRONOS_EMPLOYEE;
 
CREATE MATERIALIZED VIEW TRANSP.KRONOS_EMPLOYEE_OLD
TABLESPACE FDDLVDAT
PCTUSED    0
PCTFREE    10
INITRANS   2
MAXTRANS   255
STORAGE    (
            INITIAL          64K
            MINEXTENTS       1
            MAXEXTENTS       UNLIMITED
            PCTINCREASE      0
            BUFFER_POOL      DEFAULT
           )
NOCACHE
LOGGING
NOCOMPRESS
NOPARALLEL
BUILD IMMEDIATE
REFRESH COMPLETE
START WITH TO_DATE('11-Feb-2014 03:00:14','dd-mon-yyyy hh24:mi:ss')
NEXT SYSDATE+1
WITH PRIMARY KEY
AS 
SELECT * FROM dbo.FDDW_EMPLOYEEV42_TRANSP@MSSQLDSNPROD.NYC.FRESHDIRECT.COM;
COMMENT ON MATERIALIZED VIEW TRANSP.KRONOS_EMPLOYEE_OLD IS 'snapshot table for snapshot TRANSP.KRONOS_EMPLOYEE_OLD';

CREATE TABLE TRANSP.KRONOS_EMPLOYEE (
PERSONID    NUMBER (10),
PERSONNUM    VARCHAR2 (15 Byte),
PERSONFULLNAME    VARCHAR2 (64 Byte),
FIRSTNM    VARCHAR2 (30 Byte),
MIDDLEINITIALNM    VARCHAR2 (1 Byte),
LASTNM    VARCHAR2 (30 Byte),
SHORTNM    VARCHAR2 (20 Byte),
COMPANYHIREDTM    DATE,
FTSTDHRSQTY    NUMBER (16,6),
EMPLOYMENTSTATUS    VARCHAR2 (30 Byte),
EMPLOYMENTSTATUSDT    DATE,
BADGENUM    VARCHAR2 (50 Byte),
BADGEEFFECTIVEDTM    DATE,
BADGEEXPIRATIONDTM    DATE,
HOMELABORACCTNAME    VARCHAR2 (230 Byte),
HOMELABORLEVELNM1    VARCHAR2 (50 Byte),
HOMELABORLEVELNM2    VARCHAR2 (50 Byte),
HOMELABORLEVELNM3    VARCHAR2 (50 Byte),
HOMELABORLEVELNM4    VARCHAR2 (50 Byte),
HOMELABORLEVELNM5    VARCHAR2 (50 Byte),
HOMELABORLEVELNM6    VARCHAR2 (50 Byte),
HOMELABORLEVELNM7    VARCHAR2 (50 Byte),
HOMELABORACCTDSC    VARCHAR2 (230 Byte),
HOMELABORLEVELDSC1    VARCHAR2 (250 Byte),
HOMELABORLEVELDSC2    VARCHAR2 (250 Byte),
HOMELABORLEVELDSC3    VARCHAR2 (250 Byte),
HOMELABORLEVELDSC4    VARCHAR2 (250 Byte),
HOMELABORLEVELDSC5    VARCHAR2 (250 Byte),
HOMELABORLEVELDSC6    VARCHAR2 (250 Byte),
HOMELABORLEVELDSC7    VARCHAR2 (250 Byte),
HAEFFECTIVEDTM    DATE,
HAEXPIRATIONDTM    DATE,
MGRSIGNOFFTHRUDTM    DATE,
PENDINGSIGNOFFSW    NUMBER (10),
PAYROLLOCKTHRUDTM    DATE,
WAGEPRFLNAME    VARCHAR2 (50 Byte),
GRPSCHEDNAME    VARCHAR2 (50 Byte),
TIMEZONENAME    VARCHAR2 (30 Byte),
DCMDEVGRPNAME    VARCHAR2 (20 Byte),
SUPERVISORNUM    VARCHAR2 (15 Byte),
SUPERVISORFULLNAME    VARCHAR2 (64 Byte),
SUPERVISORID    NUMBER (10),
CURRPAYPERIODSTART    DATE,
CURRPAYPERIODEND    DATE,
PREVPAYPERIODSTART    DATE,
PREVPAYPERIODEND    DATE,
NEXTPAYPERIODSTART    DATE,
NEXTPAYPERIODEND    DATE,
LABORACCTID    NUMBER (10),
DEVICEGROUPID    NUMBER (10),
TERMINALRULEID    NUMBER (10),
SCHEDULEVERSION    NUMBER (10),
LASTTOTALTIME    DATE,
LASTTOTALCHNGTIME    DATE,
PAYRULEID    NUMBER (10),
EMPLOYEEID    NUMBER (10),
EMPLOYMENTSTATID    NUMBER (10),
ORGPATHTXT    VARCHAR2 (1000 Byte),
ORGPATHDSCTXT    VARCHAR2 (2000 Byte),
SENIORITYRANKDATE    DATE,
WORKERTYPENM    VARCHAR2 (50 Byte)

);
