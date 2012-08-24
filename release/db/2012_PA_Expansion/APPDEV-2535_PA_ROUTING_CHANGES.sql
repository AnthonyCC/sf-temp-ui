

CREATE TABLE TRN_REGION
(
  CODE         VARCHAR2(8 BYTE)                 NOT NULL,
  NAME         VARCHAR2(32 BYTE)                NOT NULL,
  DESCRIPTION  VARCHAR2(256 BYTE),
  IS_DEPOT     VARCHAR2(1 BYTE)
);


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
   ('PHL', 'PHL');
Insert into TRANSP.TRN_REGION
   (CODE, NAME, IS_DEPOT)
 Values
   ('PDP', 'PDP', 'X');

alter table transp.trn_area add  REGION_CODE         VARCHAR2(8 BYTE);
        
update transp.trn_area set region_code = 'FD' where code in ('500','561','580','516','680','562','920','750','505','510','582','000','001','002','400',
'690','450','511','800','585','017','019','105','922','560','700','581','515','200','455');

update transp.trn_area set region_code = 'MDP' where code in ('004','005','013','014','023','030','040','050','052','060','062','066','070','072','080','520','521','522',
'523','524','526','527','528','531','532','533','534','535','536','541','542','543')

update transp.trn_area set region_code = 'PHL'   where code in ('475','480')
