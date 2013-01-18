alter table 
  cust.agent 
add
  MASQUERADE_ALLOWED char(1) 
  default 'N';

alter table 
  cust.activity_log
add
  MASQUERADE_AGENT varchar2(64)
  default null;
  

-- rollback
-- alter table cust.agent drop column MASQUERADE_ALLOWED;
-- alter table cust.activity_log drop column MASQUERADE_AGENT;
