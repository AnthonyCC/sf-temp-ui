--Gift Card related------------

Insert into cust.case_queue values('GCQ','Gift Card','Gift Card Issues Queue',null);

insert into cust.case_subject values('GCQ-001','Lost/Stolen','Lost or Stolen Gift Card','GCQ','LO','','');
insert into cust.case_subject values('GCQ-002','Place on hold','Place the gift card on hold','GCQ','LO','','');
insert into cust.case_subject values('GCQ-003','Check balance','','GCQ','LO','','');
insert into cust.case_subject values('GCQ-004','Unlock Gift Card','','GCQ','LO','','');
insert into cust.case_subject values('GCQ-005','Cancel/Refund','','GCQ','LO','','');
insert into cust.case_subject values('GCQ-006','Remove gift card','','GCQ','LO','','');
insert into cust.case_subject values('GCQ-007','Resend gift card ','','GCQ','LO','','');
insert into cust.case_subject values('GCQ-008','General Inquiry ','','GCQ','LO','','');
insert into cust.case_subject values('GCQ-009','Other','','GCQ','LO','','');

insert into cust.case_subject values('ASQ-104','Orders over $750','','ASQ','LO','','');

insert into cust.complaint_dept values('GCD','Gift Cards');

insert into cust.complaint_dept_code values('SMELL','GCD', 99, null, '92346331');
insert into cust.complaint_dept_code values('OTHER','GCD', 99, null, '92346332');
insert into cust.complaint_dept_code values('CUSTERR','GCD', 99, null, '92346333');
insert into cust.complaint_dept_code values('DPMOVE','GCD', 99, null, '92346334');