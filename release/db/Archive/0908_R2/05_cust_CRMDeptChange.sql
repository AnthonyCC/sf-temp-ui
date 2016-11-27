-- Add new credit reasons and deactivate unused credit reasons
--
-- APPDEV-432
-- 
-- requested in APPDEV-329
insert into cust.complaint_dept_code(id, comp_code, comp_dept, priority) values(system_seq.nextval, 'OTHER','MGD', 10);
insert into cust.complaint_dept_code(id, comp_code, comp_dept, priority) values(system_seq.nextval, 'DELERR','MGD', 10);

-- requested in APPDEV-215
insert into cust.complaint_dept_code(id, comp_code, comp_dept, priority) values(system_seq.nextval, 'NODEL','MGD', 10);
-- insert into cust.complaint_dept_code(id, comp_code, comp_dept, priority) values(system_seq.nextval, 'OTHER','MGD', 10);

-- requested in APPDEV-432
insert into cust.complaint_code(code,name) values('LATEDL0','Late Delivery (< 30 minutes)');
insert into cust.complaint_code(code,name) values('LATEDL30','Late Delivery (30-60 minutes)');
insert into cust.complaint_code(code,name) values('LATEDL60','Late Delivery (60-90 minutes)');
insert into cust.complaint_code(code,name) values('LATEDL90','Late Delivery (> 90 minutes)');

insert into cust.complaint_dept_code(id, comp_code, comp_dept, priority) values(system_seq.nextval, 'LATEDL0','TRN', 10);
insert into cust.complaint_dept_code(id, comp_code, comp_dept, priority) values(system_seq.nextval, 'LATEDL30','TRN', 10);
insert into cust.complaint_dept_code(id, comp_code, comp_dept, priority) values(system_seq.nextval, 'LATEDL60','TRN', 10);
insert into cust.complaint_dept_code(id, comp_code, comp_dept, priority) values(system_seq.nextval, 'LATEDL90','TRN', 10);

insert into cust.complaint_dept_code(id, comp_code, comp_dept, priority) values(system_seq.nextval, 'LATEDL0','GDW', 10);
insert into cust.complaint_dept_code(id, comp_code, comp_dept, priority) values(system_seq.nextval, 'LATEDL30','GDW', 10);
insert into cust.complaint_dept_code(id, comp_code, comp_dept, priority) values(system_seq.nextval, 'LATEDL60','GDW', 10);
insert into cust.complaint_dept_code(id, comp_code, comp_dept, priority) values(system_seq.nextval, 'LATEDL90','GDW', 10);

insert into cust.complaint_dept_code(id, comp_code, comp_dept, priority) values(system_seq.nextval, 'LATEDL0','OURPICKS', 10);
insert into cust.complaint_dept_code(id, comp_code, comp_dept, priority) values(system_seq.nextval, 'LATEDL30','OURPICKS', 10);
insert into cust.complaint_dept_code(id, comp_code, comp_dept, priority) values(system_seq.nextval, 'LATEDL60','OURPICKS', 10);
insert into cust.complaint_dept_code(id, comp_code, comp_dept, priority) values(system_seq.nextval, 'LATEDL90','OURPICKS', 10);


insert into cust.complaint_code(code,name) values('NTELIG','Customer Not Eligibile');
insert into cust.complaint_code(code,name) values('PRMNTWRK','Promo Code not working');
insert into cust.complaint_code(code,name) values('PRMNTDLV','Promotion Item Not Delivered');

insert into cust.complaint_dept_code(id, comp_code, comp_dept, priority) values(system_seq.nextval, 'NTELIG','GDW', 10);
insert into cust.complaint_dept_code(id, comp_code, comp_dept, priority) values(system_seq.nextval, 'PRMNTWRK','GDW', 10);
insert into cust.complaint_dept_code(id, comp_code, comp_dept, priority) values(system_seq.nextval, 'PRMNTDLV','GDW', 10);

update cust.complaint_dept_code set obsolete='X' where comp_code in ('LATEDEL','PROMO','XTRITM','DRVCMPLI','PRDCOM','REQINGR','TIPS','VSHOT');
