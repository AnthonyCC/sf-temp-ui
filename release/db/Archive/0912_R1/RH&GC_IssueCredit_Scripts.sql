------------------------Robin Hood------------------------------------

insert into cust.case_subject values('NOQ-012','RH-Customer Error','Customer Error','NOQ','LO','',null);

insert into cust.case_subject values('NOQ-013','RH-Changed Mind/Cancel','Changed Mind/Cancel','NOQ','LO','',null)

insert into cust.case_subject values('NOQ-014','RH-System Error','','NOQ','LO','',null);

insert into cust.case_subject values('NOQ-015','RH-Inquiry','','NOQ','LO','',null);


INSERT INTO CUST.COMPLAINT_CODE ( CODE, NAME, SUBJECT_CODE,PRIORITY, DLV_ISSUE_CODE) values('RHCSTERR','RH-Cust Error','NOQ-012',90,null);
 
INSERT INTO CUST.COMPLAINT_CODE ( CODE, NAME, SUBJECT_CODE,PRIORITY, DLV_ISSUE_CODE) values('RHCHANGE','RH-Customer changed Mind','NOQ-013',90,null);

INSERT INTO CUST.COMPLAINT_CODE ( CODE, NAME, SUBJECT_CODE,PRIORITY, DLV_ISSUE_CODE) values('RHSYSERR','RH-System Error','NOQ-014',90,null);

INSERT INTO CUST.COMPLAINT_CODE ( CODE, NAME, SUBJECT_CODE,PRIORITY, DLV_ISSUE_CODE) values('RHOTHER','RH-Other','NOQ-015',90,null);


insert into cust.complaint_dept_code values('RHCSTERR','OURPICKS', 99, null, to_char(cust.system_seq.nextval));

insert into cust.complaint_dept_code values('RHCHANGE','OURPICKS', 99, null, to_char(cust.system_seq.nextval));

insert into cust.complaint_dept_code values('RHSYSERR','OURPICKS', 99, null, to_char(cust.system_seq.nextval));

insert into cust.complaint_dept_code values('RHOTHER','OURPICKS', 99, null, to_char(cust.system_seq.nextval));






------------------------Gift Card-----------------------------------


update cust.complaint_code set name='GC-Customer Purchased by Error' where code='GCCSTERR'

update cust.complaint_code set name='GC-Moved out of delivery area' where code='GCMOVE'

update cust.complaint_code set name='GC-Does not want' where code='GCDNWANT'

update cust.complaint_code set name='GC-Other' where code='GCOTHER'


update cust.complaint_dept_code set comp_dept='OURPICKS' where comp_code='GCCSTERR'

update cust.complaint_dept_code set comp_dept='OURPICKS' where comp_code='GCMOVE'

update cust.complaint_dept_code set comp_dept='OURPICKS' where comp_code='GCDNWANT'

update cust.complaint_dept_code set comp_dept='OURPICKS' where comp_code='GCOTHER'