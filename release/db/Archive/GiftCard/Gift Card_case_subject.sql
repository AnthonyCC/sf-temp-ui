--Gift Card related------------

Insert into cust.case_queue values('GCQ','Gift Card','Gift Card Issues Queue',null);

insert into cust.case_subject values('GCQ-001','Lost/Stolen','Lost or Stolen Gift Card','GCQ','LO','',null);
insert into cust.case_subject values('GCQ-002','Place on hold','Place the gift card on hold','GCQ','LO','',null);
insert into cust.case_subject values('GCQ-003','Check balance','','GCQ','LO','',null);
insert into cust.case_subject values('GCQ-004','Unlock Gift Card','','GCQ','LO','',null);
insert into cust.case_subject values('GCQ-005','Cancel/Refund','','GCQ','LO','',null);
insert into cust.case_subject values('GCQ-006','Remove gift card','','GCQ','LO','',null);
insert into cust.case_subject values('GCQ-007','Resend gift card','','GCQ','LO','',null);
insert into cust.case_subject values('GCQ-008','General Inquiry','','GCQ','LO','',null);
insert into cust.case_subject values('GCQ-009','Other','','GCQ','LO','',null);
insert into cust.case_subject values('GCQ-010','Purchase gift card','','GCQ','LO','',null);
insert into cust.case_subject values('ASQ-104','Orders over $750','','ASQ','LO','',null);

insert into cust.complaint_dept values('GCD','Gift Cards');

INSERT INTO CUST.COMPLAINT_CODE ( CODE, NAME, SUBJECT_CODE,PRIORITY, DLV_ISSUE_CODE) values('GCCSTERR','Customer Purchased by Error','GCQ-010',90,null);
INSERT INTO CUST.COMPLAINT_CODE ( CODE, NAME, SUBJECT_CODE,PRIORITY, DLV_ISSUE_CODE) values('GCMOVE','Moved out of delivery area','GCQ-010',90,null);
INSERT INTO CUST.COMPLAINT_CODE ( CODE, NAME, SUBJECT_CODE,PRIORITY, DLV_ISSUE_CODE) values('GCDNWANT','Does not want','GCQ-010',90,null);
INSERT INTO CUST.COMPLAINT_CODE ( CODE, NAME, SUBJECT_CODE,PRIORITY, DLV_ISSUE_CODE) values('GCOTHER','Other','GCQ-010',90,null);

insert into cust.complaint_dept_code values('GCCSTERR','GCD', 99, null, to_char(cust.system_seq.nextval));
insert into cust.complaint_dept_code values('GCMOVE','GCD', 99, null, to_char(cust.system_seq.nextval));
insert into cust.complaint_dept_code values('GCDNWANT','GCD', 99, null, to_char(cust.system_seq.nextval));
insert into cust.complaint_dept_code values('GCOTHER','GCD', 99, null, to_char(cust.system_seq.nextval));

