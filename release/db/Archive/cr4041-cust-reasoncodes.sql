-- Create new complaint code for Voice Shot / Outbound Late
insert into CUST.COMPLAINT_CODE(CODE, NAME) values ('VSHOT', 'Voice Shot/Outbound Late');


-- Associate complaint codes with all 
-- Transportation <-> Voice Shot
insert into CUST.COMPLAINT_DEPT_CODE(COMP_CODE, COMP_DEPT, PRIORITY, ID)
  values('VSHOT', 'TRN', 11, system_seq.nextval);

-- Wine <-> Product Damaged
insert into CUST.COMPLAINT_DEPT_CODE(COMP_CODE, COMP_DEPT, PRIORITY, ID)
  values('PRDDAM', 'WIN', 11, system_seq.nextval);

-- Create new subject for q
insert into CUST.CASE_SUBJECT(CODE, NAME, DESCRIPTION, CASE_QUEUE, CASE_PRIORITY)
  values('TRQ-009', 'Voice Shot/Outbound Late', '', 'TRQ', 'MD');
