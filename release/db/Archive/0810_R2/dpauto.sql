insert into cust.complaint_code values ('DPAUTODL', 'DP Auto Renew - Customer Declined');
insert into cust.complaint_code values ('DPAUTOCX', 'DP Auto Renew - No Response/Cancelled');

insert into cust.complaint_dept_code(comp_code, comp_dept, priority, obsolete, id) values ('DPAUTODL', 'OURPICKS', 2, NULL, cust.system_seq.nextval);
insert into cust.complaint_dept_code(comp_code, comp_dept, priority, obsolete, id) values ('DPAUTOCX', 'OURPICKS', 2, NULL, cust.system_seq.nextval);

-- 4 rows inserted