insert into cust.caseaction_type(code, name, description) values ('ACT_DM_R', 'Removed Doorman', 'Removed Doorman Option');
-- Expected 1 row inserted

insert into cust.caseaction_type(code, name, description) values ('ACT_DM_A', 'Added Doorman', 'Added Doorman Option');
-- Expected 1 row inserted

insert into cust.case_operation (
 select distinct case_subject, start_case_state, 'CLSD' as end_case_state, role, 'ACT_DM_R' as caseaction_type
 from cust.case_operation
 where case_subject='EAQ-001'
 and start_case_state='OPEN'
);
-- Expected 6 rows inserted

insert into cust.case_operation (
 select distinct case_subject, start_case_state, 'CLSD' as end_case_state, role, 'ACT_DM_A' as caseaction_type
 from cust.case_operation
 where case_subject='EAQ-001'
 and start_case_state='OPEN'
);
-- Expected 6 rows inserted

