/*Trn_Faciility_type*/
CREATE TABLE TRANSP.TRN_FACILITYTYPE
(
  FACILITYTYPE_CODE  VARCHAR2(32 BYTE)          NOT NULL,
  DESCRIPTION        VARCHAR2(256 BYTE)
);

ALTER TABLE TRANSP.TRN_FACILITYTYPE ADD ( CONSTRAINT PK_TRN_FACILITYTYPE PRIMARY KEY (FACILITYTYPE_CODE));

/*Trn_Faciility*/
CREATE TABLE TRANSP.TRN_FACILITY
(
  ID                 VARCHAR2(16 BYTE)          NOT NULL,
  FACILITY_CODE      VARCHAR2(32 BYTE)          NOT NULL,
  DESCRIPTION        VARCHAR2(256 BYTE),
  ROUTING_CODE       VARCHAR2(8 BYTE),
  PREFIX             VARCHAR2(2 BYTE),
  LEAD_FROM_TIME     NUMBER,
  LEAD_TO_TIME       NUMBER,
  FACILITYTYPE_CODE  VARCHAR2(32 BYTE)          NOT NULL 
);

ALTER TABLE TRANSP.TRN_FACILITY ADD ( CONSTRAINT PK_TRN_FACILITY PRIMARY KEY (ID)
                                                                , CONSTRAINT TRNFACILITY_UNIQUE UNIQUE (FACILITY_CODE, ROUTING_CODE));

ALTER TABLE TRANSP.TRN_FACILITY ADD ( CONSTRAINT ACT_FACILITYTYPE_FK FOREIGN KEY (FACILITYTYPE_CODE) REFERENCES TRANSP.TRN_FACILITYTYPE (FACILITYTYPE_CODE));

CREATE SEQUENCE TRANSP.FACILITYSEQ
  START WITH 4316
  MAXVALUE 99999999
  MINVALUE 1
  NOCYCLE
  CACHE 10
  NOORDER;   
  
/*HANDOFF_BATCH_TRAILER*/
CREATE TABLE TRANSP.HANDOFF_BATCHTRAILER
(
  BATCH_ID        VARCHAR2(16 BYTE)             NOT NULL,
  TRAILER_NO      VARCHAR2(50 BYTE)             NOT NULL,
  STARTTIME       DATE                          NOT NULL,
  COMPLETETIME    DATE                          NOT NULL,
  DISPATCHTIME    DATE                          NOT NULL,
  CROSSDOCK_CODE  VARCHAR2(32 BYTE)             NOT NULL
);

CREATE INDEX TRANSP.IDX_HF_BATCHTRAILER_BATCHID ON TRANSP.HANDOFF_BATCHTRAILER (BATCH_ID);

ALTER TABLE TRANSP.HANDOFF_BATCHTRAILER ADD ( CONSTRAINT PK_HANDOFF_BATCHTRAILER PRIMARY KEY (BATCH_ID, TRAILER_NO));

/*HANDOFF_BATCH_ROUTE*/                                                     
ALTER TABLE TRANSP.HANDOFF_BATCHROUTE ADD (  TRAILER_NO   VARCHAR2(50 BYTE));

ALTER TABLE TRANSP.HANDOFF_BATCHROUTE ADD (CONSTRAINT BATCHROUTETRAILER_FK FOREIGN KEY (BATCH_ID, TRAILER_NO) REFERENCES TRANSP.HANDOFF_BATCHTRAILER (BATCH_ID,TRAILER_NO));

/*Scrib*/
ALTER TABLE TRANSP.SCRIB ADD (ORIGIN_FACILITY    VARCHAR2(16 BYTE),  DESTINATION_FACILITY  VARCHAR2(16 BYTE));

ALTER TABLE TRANSP.SCRIB ADD ( CONSTRAINT SCRIBDESTFACILITY_FK FOREIGN KEY (DESTINATION_FACILITY) REFERENCES TRANSP.TRN_FACILITY (ID)
                                                     , CONSTRAINT SCRIBORIGINFACILITY_FK  FOREIGN KEY (ORIGIN_FACILITY) REFERENCES TRANSP.TRN_FACILITY (ID));
  

/*Plan*/
ALTER TABLE TRANSP.PLAN ADD (ORIGIN_FACILITY    VARCHAR2(16 BYTE),  DESTINATION_FACILITY  VARCHAR2(16 BYTE));

ALTER TABLE TRANSP.PLAN ADD ( CONSTRAINT PLANDESTFACILITY_FK FOREIGN KEY (DESTINATION_FACILITY) REFERENCES TRANSP.TRN_FACILITY (ID)
                                                     , CONSTRAINT PLANORIGINFACILITY_FK  FOREIGN KEY (ORIGIN_FACILITY) REFERENCES TRANSP.TRN_FACILITY (ID));
  
/*Dispatch*/
ALTER TABLE TRANSP.DISPATCH ADD (ORIGIN_FACILITY    VARCHAR2(16 BYTE),  DESTINATION_FACILITY  VARCHAR2(16 BYTE), CUTOFF_DATETIME DATE);

ALTER TABLE TRANSP.DISPATCH ADD ( CONSTRAINT DISPATCHDESTFACILITY_FK FOREIGN KEY (DESTINATION_FACILITY) REFERENCES TRANSP.TRN_FACILITY (ID)
                                                     , CONSTRAINT DISPATCHORIGINFACILITY_FK  FOREIGN KEY (ORIGIN_FACILITY) REFERENCES TRANSP.TRN_FACILITY (ID));
                                                     
/*WAVE_INSTANCE*/
ALTER TABLE TRANSP.WAVE_INSTANCE  ADD  (ORIGIN_FACILITY    VARCHAR2(16 BYTE));

ALTER TABLE TRANSP.WAVE_INSTANCE  DROP CONSTRAINT TRANSP.ACT_WAVE_INSTANCE_UK;

ALTER TABLE TRANSP.WAVE_INSTANCE ADD CONSTRAINT ACT_WAVE_INSTANCE_UK UNIQUE (ORIGIN_FACILITY, DELIVERY_DATE, AREA, DISPATCH_TIME, FIRST_DLV_TIME, LAST_DLV_TIME, CUTOFF_DATETIME);

ALTER TABLE TRANSP.WAVE_INSTANCE ADD ( CONSTRAINT WAVEORIGINFACILITY_FK FOREIGN KEY (ORIGIN_FACILITY) REFERENCES TRANSP.TRN_FACILITY (ID));
   

/*DLV Schema*/
ALTER TABLE DLV.SERVICETIME_SCENARIO ADD (TRAILER_CONTAINERMAX   NUMBER(10,2),  TRAILER_CONTAINERCARTONMAX    NUMBER(10,2));

/*GRANTS*/
GRANT DELETE, INSERT, SELECT, UPDATE ON TRANSP.TRN_FACILITY TO fdtrn_ststg01;

GRANT SELECT ON TRANSP.TRN_FACILITY TO appdev;

GRANT DELETE, INSERT, SELECT, UPDATE ON TRANSP.TRN_FACILITYTYPE TO fdtrn_ststg01;

GRANT SELECT ON TRANSP.TRN_FACILITYTYPE TO appdev;

GRANT DELETE, INSERT, SELECT, UPDATE ON TRANSP.HANDOFF_BATCHTRAILER TO fdtrn_ststg01;

GRANT SELECT ON TRANSP.HANDOFF_BATCHTRAILER TO appdev;

GRANT SELECT ON TRANSP.FACILITYSEQ TO fdtrn_ststg01;   

GRANT SELECT ON TRANSP.FACILITYSEQ TO appdev;   

/*Insert scripts*/
Insert into TRANSP.EMPLOYEE_ROLE_SUB_TYPE (CODE, NAME, TCODE, DESCRIPTION) Values ('013', 'Trailer Driver', '001', 'Trailer Driver');
Insert into TRANSP.EMPLOYEE_ROLE_SUB_TYPE (CODE, NAME, TCODE, DESCRIPTION) Values ('014', 'Trailer Helper', '002', 'Trailer Helper');
Insert into TRANSP.EMPLOYEE_ROLE_SUB_TYPE (CODE, NAME, TCODE, DESCRIPTION) Values ('015', 'Trailer Runner', '003', 'Trailer Runner');

Insert into TRANSP.TRN_FACILITYTYPE (FACILITYTYPE_CODE, DESCRIPTION) Values ('DPT', 'Regular Delivery');
Insert into TRANSP.TRN_FACILITYTYPE (FACILITYTYPE_CODE, DESCRIPTION) Values ('CD', 'Cross Dock');
Insert into TRANSP.TRN_FACILITYTYPE (FACILITYTYPE_CODE, DESCRIPTION) Values ('SIT', 'Regular zone delivery');

Insert into TRANSP.TRN_FACILITY (ID, FACILITY_CODE, DESCRIPTION, FACILITYTYPE_CODE) Values (TRANSP.FACILITYSEQ.nextval, 'FD', 'FD Head Quaters', 'DPT');

Insert into TRANSP.TRN_FACILITY (ID, FACILITY_CODE, DESCRIPTION, FACILITYTYPE_CODE) Values  (TRANSP.FACILITYSEQ.nextval, 'ZONE', 'Customer delivery zone', 'SIT');

/*Update scripts for existing entries*/
update transp.scrib s set s.origin_facility=(select id from TRANSP.TRN_FACILITY where FACILITY_CODE='FD');
update transp.scrib s set s.destination_facility=(select id from TRANSP.TRN_FACILITY where FACILITY_CODE='ZONE');
  
update transp.plan p set p.origin_facility = (select id from TRANSP.TRN_FACILITY where FACILITY_CODE='FD') where p.zone is not null; 
update transp.plan p set p.destination_facility = (select id from TRANSP.TRN_FACILITY where FACILITY_CODE='ZONE') where p.zone is not null;

update transp.dispatch d set d.origin_facility= (select id from TRANSP.TRN_FACILITY where FACILITY_CODE='FD')  where d.zone is not null; 
update transp.dispatch d set d.destination_facility= (select id from TRANSP.TRN_FACILITY where FACILITY_CODE='ZONE') where d.zone is not null;

update transp.WAVE_INSTANCE w set w.origin_facility=(select id from TRANSP.TRN_FACILITY where FACILITY_CODE='FD');

ALTER TABLE TRANSP.WAVE_INSTANCE  MODIFY  (ORIGIN_FACILITY    NOT NULL);
ALTER TABLE TRANSP.SCRIB  MODIFY  (ORIGIN_FACILITY    NOT NULL, DESTINATION_FACILITY    NOT NULL);
