
Insert into CUST.CASE_OPERATION   (CASE_SUBJECT, START_CASE_STATE, END_CASE_STATE, ROLE, CASEACTION_TYPE) select 'DSQ-014',START_CASE_STATE, END_CASE_STATE, ROLE, CASEACTION_TYPE from CUST.CASE_OPERATION where CASE_SUBJECT='DSQ-002' and ROLE in('NCS','CSR','SCS');

update cust.case c set C.CASE_STATE='CLSD' where c.case_state='REVW';