-- Standing Orders
create table cust.standing_order(
  "ID" varchar2(16) primary key,
  "CUSTOMER_ID" varchar2(16) not null,
  "CUSTOMERLIST_ID" varchar2(16) not null,
  "ADDRESS_ID" varchar2(16) not null,
  "PAYMENTMETHOD_ID" varchar2(16) not null,

  "START_TIME" date,
  "END_TIME" date,

  "NEXT_DATE" date,

  "FREQUENCY" int default 1,
  "ALCOHOL_AGREEMENT" char(1) default 0,
  "DELETED" char(1) default 0,

  "LAST_ERROR" varchar2(64),
  "ERROR_HEADER" varchar2(1024),
  "ERROR_DETAIL" varchar2(1024),
  
  constraint "SO_FK_CUSTOMERLIST" foreign key ("CUSTOMERLIST_ID") references cust.customerlist("ID")
);


-- Grant store user
grant select, insert, delete, update on CUST.standing_order to FDSTORE_PRDA;
grant select, insert, delete, update on CUST.STANDING_ORDER to FDSTORE_PRDB;
grant select, insert, delete, update on CUST.standing_order to appdev;

-- add standing order FK to sale table
alter table CUST.SALE add "STANDINGORDER_ID" varchar2(16);
alter table cust.sale add constraint "sale_fk_standingorder" foreign key ("STANDINGORDER_ID") references cust.standing_order("ID");
CREATE INDEX SO_FK ON CUST.SALE (STANDINGORDER_ID);
CREATE INDEX SO_DELETED ON CUST.STANDING_ORDER(DELETED);

-- add standing order FK to activity log table
alter table CUST.ACTIVITY_LOG add "STANDINGORDER_ID" varchar2(16);
alter table CUST.activity_log add constraint "activity_fk_standingorder" foreign key ("STANDINGORDER_ID") references CUST.STANDING_ORDER("ID");



-- drop part
-- alter table CUST.ACTIVITY_LOG drop constraint "activity_fk_standingorder";
-- alter table CUST.ACTIVITY_LOG column "STANDINGORDER_ID";
-- alter table CUST.SALE drop constraint "sale_fk_standingorder";
-- alter table cust.sale drop column "STANDINGORDER_ID";
-- drop table cust.standing_order;

