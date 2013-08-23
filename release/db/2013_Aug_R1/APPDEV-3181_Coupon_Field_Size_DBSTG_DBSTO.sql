--Please execute the below script in STAGE and PROD databases.
alter table cust.fdcoupon modify(LONG_DESC varchar2(1000 byte));