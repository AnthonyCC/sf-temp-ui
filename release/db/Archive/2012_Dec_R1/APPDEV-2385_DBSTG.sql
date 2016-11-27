create table cust.promotion_batch(
batch_id varchar2(16),
batch_count number,
create_date date,
created_by varchar2(80));

GRANT DELETE, INSERT, SELECT, UPDATE on CUST.promotion_batch TO FDSTORE_STSTG01;

GRANT SELECT on CUST.promotion_batch TO APPDEV;

alter table cust.promotion_new add batch_id varchar2(16);