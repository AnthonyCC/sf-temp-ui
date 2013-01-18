
ALTER TABLE CUST.COMPLAINT ADD COMPLAINT_TYPE varchar2(16);

CREATE INDEX CASE_STATE_AGENT_IDX ON CUST.CASE
(ASSIGNED_AGENT_ID, CASE_STATE) compute statistics;

UPDATE cust.complaint c1   
  SET c1.complaint_type = 
  (select  
  case 
     WHEN (((select count(*) from cust.complaintline cl1 where cl1.method='FDC' and cl1.complaint_id = c.id group by cl1.method) > 0 ) AND 
	 	  ((select count(*) from cust.complaintline cl2 where cl2.method='CSH' and cl2.complaint_id = c.id group by cl2.method) > 0)) then 'MIX'
     WHEN ((select count(*) from cust.complaintline cl3 where cl3.method='FDC' and cl3.complaint_id = c.id group by cl3.method) > 0) then 'FDC' 
     WHEN ((select count(*) from cust.complaintline cl4 where cl4.method='CSH' and cl4.complaint_id = c.id group by cl4.method) >0) then 'CSH'
  END as complaint_type 
from 
  cust.complaint c 
where c.id = c1.id );