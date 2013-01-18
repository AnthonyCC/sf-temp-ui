ALTER TABLE OCF_RUN_LOG ADD STATUS VARCHAR2(16);

update cust.ocf_run_log set status = 'FINISHED' where status is null;

commit;
