-- New list related fields for quickshop redesign

alter table cust.customerlist add recipe_id varchar2(128);
alter table cust.customerlist add recipe_name varchar2(128);
alter table cust.fduser add default_list_id varchar2(16);
