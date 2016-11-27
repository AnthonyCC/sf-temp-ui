insert into cust.complaint_code(code,name,subject_code,priority,dlv_issue_code) values ('DAMBOXC', 'Damaged Box - customer reported', 'ORQ-015',9,'DAMG');
insert into cust.complaint_code(code,name,subject_code,priority,dlv_issue_code) values ('DAMBOXD', 'Damaged Box - driver reported', 'DSQ-003',10,'DAMG');

-- Replicate DAMBOX relationships
insert into cust.complaint_dept_code(id, comp_code,comp_dept,priority,obsolete)
select system_seq.nextval as id,'DAMBOXC' as comp_code, comp_dept, priority, obsolete
from cust.complaint_dept_code where comp_code='DAMBOX';

insert into cust.complaint_dept_code(id, comp_code,comp_dept,priority,obsolete)
select system_seq.nextval as id,'DAMBOXD' as comp_code, comp_dept, priority, obsolete
from cust.complaint_dept_code where comp_code='DAMBOX';

-- Obsolete old DAMBOX bindings
update cust.complaint_dept_code set obsolete='X' where comp_code='DAMBOX';

