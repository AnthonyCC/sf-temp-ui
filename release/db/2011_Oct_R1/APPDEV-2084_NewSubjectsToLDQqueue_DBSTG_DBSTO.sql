alter table  CUST.CASE_SUBJECT modify name varchar2(50 byte);

Insert into CUST.CASE_SUBJECT   (CODE, NAME, CASE_QUEUE, CASE_PRIORITY) Values   ('LDQ-011', 'Found-order with doorman', 'LDQ', 'MD');
Insert into CUST.CASE_SUBJECT   (CODE, NAME, CASE_QUEUE, CASE_PRIORITY) Values   ('LDQ-012', 'Found-unpacked by someone in home/office', 'LDQ', 'MD');