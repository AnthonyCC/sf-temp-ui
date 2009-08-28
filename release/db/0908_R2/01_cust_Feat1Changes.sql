ALTER TABLE CUST.COMPLAINT ADD "AUTO_CASE_ID" VARCHAR2(16 BYTE);
alter table cust.complaintline add carton_number varchar2(20);

-- add subject code and priority to reason table
--
alter table cust.complaint_code
  add subject_code varchar2(8)
  add priority integer;


--
-- Run the following comments if you want to assing random priorities and subjects to complaint reasons
--

-- assign random case subjects
--
update cust.complaint_code set subject_code = (
  select b.code as subject_code from
  (
    select code, name, rownum as priority
    from complaint_code
  ) a
  join
  (
    select code, rownum as id
    from (
      SELECT code FROM
      (
        SELECT code, rownum as ident FROM case_subject
        ORDER BY dbms_random.value
      )
      WHERE rownum <= (select count(*) from complaint_code)
    ) order by id
  ) b on(a.priority=b.id)
  where a.code=complaint_code.code
);

-- assign mock priorities
--
update cust.complaint_code set priority=rownum;

