--Please execute the below db script in STAGE and PROD.

insert into CUST.DEPARTMENT values ('PET','Pet','Pet Department','');

insert into CUST.COMPLAINT_DEPT values ('PET','Pet');

insert into CUST.COMPLAINT_DEPT_CODE select COMP_CODE, 'PET', PRIORITY, OBSOLETE, cust.system_seq.nextval from cust.complaint_dept_code where comp_dept = 'GRO'; 