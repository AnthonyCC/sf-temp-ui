create table cust.promotion_batch(
batch_id varchar2(16),
batch_count number,
create_date date,
created_by varchar2(80))
/

GRANT SELECT, INSERT, DELETE, UPDATE on CUST.promotion_batch TO FDSTORE_PRDA;

GRANT SELECT on CUST.promotion_batch TO FDSTORE_PRDA, APPDEV;

alter table cust.promotion_new
add batch_id varchar2(16)
/