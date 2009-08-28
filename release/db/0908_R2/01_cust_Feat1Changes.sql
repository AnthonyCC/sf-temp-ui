ALTER TABLE CUST.COMPLAINT ADD "AUTO_CASE_ID" VARCHAR2(16 BYTE);
alter table cust.complaintline add carton_number varchar2(20);

-- add subject code and priority to reason table
--
alter table cust.complaint_code
  add subject_code varchar2(8)
  add priority integer;

