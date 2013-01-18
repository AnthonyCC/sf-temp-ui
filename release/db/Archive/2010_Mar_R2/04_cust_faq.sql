-- Create new table in CUST schema
create table cust.TOP_FAQS(CMSNODE_ID varchar2(255) not null, TIME_STAMP timestamp);

grant insert,select,delete,update on cust.TOP_FAQS to fdstore_prda;

grant insert,select,delete,update on cust.TOP_FAQS to fdstore_prdb;

grant insert,select,delete,update on cust.TOP_FAQS to fdstore_ststg01;

grant select on cust.TOP_FAQS to appdev;

