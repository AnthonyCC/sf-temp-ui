create table cust.complaint_dlv_type(
  "CODE" VARCHAR2(8) NOT NULL PRIMARY KEY,
  "NAME" VARCHAR2(32) NOT NULL,
  "DESCRIPTION" VARCHAR2(256 BYTE)
);
insert into cust.complaint_dlv_type(code,name,description) values('DAMG','Damaged','Damaged');
insert into cust.complaint_dlv_type(code,name,description) values('QTY','Quality','Quality');
insert into cust.complaint_dlv_type(code,name,description) values('MISS','Missing','Missing');
insert into cust.complaint_dlv_type(code,name,description) values('LATE','Late','Late');

grant select on cust.complaint_dlv_type to fdstore_prda;
grant select on cust.complaint_dlv_type to fdstore_prdb;

alter table cust.complaint_code add "DLV_ISSUE_CODE" varchar2(8);
alter table cust.complaint_code add constraint "DLV_ISSUE_CODE_FK" foreign key("DLV_ISSUE_CODE") references "CUST"."COMPLAINT_DLV_TYPE"("CODE");
