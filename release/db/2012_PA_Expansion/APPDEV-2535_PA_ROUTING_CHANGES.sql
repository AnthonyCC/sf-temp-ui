

CREATE TABLE TRN_REGION
(
  CODE         VARCHAR2(8 BYTE)                 NOT NULL,
  NAME         VARCHAR2(32 BYTE)                NOT NULL,
  DESCRIPTION  VARCHAR2(256 BYTE),
  IS_DEPOT     VARCHAR2(1 BYTE)
)


GRANT INSERT, SELECT, UPDATE, DELETE ON TRN_REGION TO FDTRN_PRDA, FDSTORE_PRDA;


GRANT SELECT ON TRN_REGION TO APPDEV;


Insert into TRANSP.TRN_FACILITY
   (ID, FACILITY_CODE, DESCRIPTION, ROUTING_CODE, PREFIX, LEAD_FROM_TIME, LEAD_TO_TIME, FACILITYTYPE_CODE)
 Values
   ('4318', 'CD_PHL', 'Cross Dock Philadelphia', '990', '9', 
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
   ('PHL', 'PHI');
Insert into TRANSP.TRN_REGION
   (CODE, NAME, IS_DEPOT)
 Values
   ('PDP', 'PDP', 'X');


