

CREATE TABLE TRANSP.TRN_REGION
(
  CODE         VARCHAR2(8 BYTE)                 NOT NULL,
  NAME         VARCHAR2(32 BYTE)                NOT NULL,
  DESCRIPTION  VARCHAR2(256 BYTE),
  IS_DEPOT     VARCHAR2(1 BYTE)
);

Insert into TRANSP.TRN_FACILITY
   (ID, FACILITY_CODE, DESCRIPTION, ROUTING_CODE, PREFIX, LEAD_FROM_TIME, LEAD_TO_TIME, FACILITYTYPE_CODE)
 Values
   (TRANSP.FACILITYSEQ.NEXTVAL, 'CD_PHL', 'Cross Dock Philadelphia', '990', '9', 
    30, 30, 'CD');



Insert into TRANSP.TRN_REGION
   (CODE, NAME, IS_DEPOT)
 Values
   ('MDP', 'MDP', 'X');
Insert into TRANSP.TRN_REGION
   (CODE, NAME)
 Values
   ('FD', 'FD');
Insert into TRANSP.TRN_REGION
   (CODE, NAME)
 Values
   ('PHL', 'PHL');
Insert into TRANSP.TRN_REGION
   (CODE, NAME, IS_DEPOT)
 Values
   ('PDP', 'PDP', 'X');

alter table transp.trn_area add  REGION_CODE         VARCHAR2(8 BYTE);
        
update transp.trn_area set region_code = 'FD' where code in (SELECT CODE FROM TRANSP.TRN_AREA WHERE IS_DEPOT IS NULL);

update transp.trn_area set region_code = 'MDP' where code in (SELECT CODE FROM TRANSP.TRN_AREA WHERE IS_DEPOT = 'X');

update transp.trn_area set region_code = 'PHL'   where code in ('475','480');

alter table transp.trn_area MODIFY  REGION_CODE         VARCHAR2(8 BYTE) NOT NULL;


