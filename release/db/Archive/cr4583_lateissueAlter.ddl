-- alterations after first release  ---
alter table cust.lateissue add(
  agent_user_id varchar(16),
  actual_stopsText  varchar2(100),
  actual_stopsCount number(6)
);

-- add a foreign  key constraint to the agent_user_id field  ---

ALTER TABLE cust.lateissue ADD  CONSTRAINT Lateissue_AGTUID_FK
 FOREIGN KEY (AGENT_USER_ID) 
  REFERENCES CUST.AGENT (USER_ID) ;

update cust.lateissue set agent_user_id = reported_by, reported_by='Driver';