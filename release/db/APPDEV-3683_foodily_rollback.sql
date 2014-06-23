alter table CUST.FDCARTLINE drop column EXTERNAL_AGENCY;
alter table CUST.FDCARTLINE drop column EXTERNAL_SOURCE;
alter table CUST.FDCARTLINE drop column EXTERNAL_GROUP;

alter table CUST.ORDERLINE drop column EXTERNAL_AGENCY;
alter table CUST.ORDERLINE drop column EXTERNAL_SOURCE;
alter table CUST.ORDERLINE drop column EXTERNAL_GROUP;
