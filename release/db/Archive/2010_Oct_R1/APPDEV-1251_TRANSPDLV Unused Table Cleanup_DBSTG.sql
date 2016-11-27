--DLV Schema

DROP TABLE DLV.SERVICETIME CASCADE CONSTRAINTS;

ALTER TABLE DLV.SERVICETIME_SCENARIO drop (DEFAULT_SERVICETIME_TYPE, DEFAULT_ZONE_TYPE);

Alter table DLV.DELIVERY_BUILDING_DETAIL drop column SVC_TIME_OVERRIDE;

DROP TABLE DLV.DELIVERY_BUILDING_DTL CASCADE CONSTRAINTS;

--TRANSP Scheema

DROP TABLE TRANSP.TRN_ZONE CASCADE CONSTRAINTS;
 
DROP TABLE TRANSP.TRN_DISPATCH CASCADE CONSTRAINTS;

DROP TABLE TRANSP.TRN_DISPATCH_PLAN CASCADE CONSTRAINTS;

DROP TABLE TRANSP.TRN_EMPLOYEE CASCADE CONSTRAINTS;

DROP TABLE TRANSP.TRN_EMPLOYEE_JOB_TYPE CASCADE CONSTRAINTS;

DROP TABLE TRANSP.TRN_EMPLOYEEASSIGNMENT CASCADE CONSTRAINTS;

DROP TABLE TRANSP.TRN_ROUTE CASCADE CONSTRAINTS;

DROP TABLE TRANSP.TRN_ROUTENUMBER CASCADE CONSTRAINTS;

DROP TABLE TRANSP.TRN_TRUCK CASCADE CONSTRAINTS;

DROP TABLE TRANSP.TRN_TRUCKASSIGNMENT CASCADE CONSTRAINTS;

DROP TABLE TRANSP.TRN_TIMESLOT CASCADE CONSTRAINTS;

DROP SEQUENCE TRANSP.TRUCKSEQ;

DROP SEQUENCE TRANSP.TRUCKASSIGNSEQ;

DROP SEQUENCE TRANSP.EMPSEQ;