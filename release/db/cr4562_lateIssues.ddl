create table lateIssue (
  id varchar2(16) not null,
  route number(5) not null,
  delivery_date date not null,
  stopsText varchar2(100) not null,
  reported_at date not null,
  reported_by  varchar2(100) not null,
  delay_minutes number(4) not null,
  delivery_window varchar2(25),
  comments varchar2(100)
);

ALTER TABLE CUST.LATEISSUE
  ADD CONSTRAINT PK_LATEISSUE PRIMARY KEY (id) 	


create index idx_lateIssue on cust.lateIssue (delivery_date,route);

grant select,insert,update, delete on cust.lateIssue to fdstore_prda;
grant select,insert,update, delete on cust.lateIssue to fdstore_prd;
grant select on cust.lateIssue to appdev;


create table cust.lateIssue_orders (
lateIssue_id varchar2(16) not null,
stop_number  varchar2(5) not null,
sale_id		 varchar2(16) 
);

create index cust.idx_lateIssueOrder on cust.lateIssue (sale_id, lateIssue_id);

ALTER TABLE CUST.LATEISSUE_ORDERS
  ADD CONSTRAINT PK_LATEISSUE_ORDER PRIMARY KEY (LATEISSUE_ID, STOP_NUMBER) 	

alter table cust.lateIssue_orders add constraint fk_lastissue_id foreign key (lateIssue_id) references lateIssue(id);

grant select,insert,update, delete on cust.lateIssue_orders to fdstore_prda;
grant select,insert,update, delete on cust.lateIssue_orders to fdstore_prdb;
grant select on cust.lateIssue_orders to appdev;

-- alter the case table to contain the fields for the projected quantity and actual quantity --
alter table cust.case add(projected_quantity number(5) null, actual_quantity number(5) null)

insert into cust.case_subject values ('TRQ-010','Damaged frozen','frozen item/s damaged','TRQ','MD',null);

insert into cust.case_subject values ('TRQ-011','Damaged box/bag','item/s damaged','TRQ','MD',null);

insert into cust.case_subject values ('TRQ-020','Missing frozen','frozen item/s missing','TRQ','MD',null);

insert into cust.case_subject values ('TRQ-021','Missing product','missing product/s','TRQ','MD',null);

insert into cust.case_subject values ('TRQ-030','Doorman refused','doorman refused delivery','TRQ','MD',null);

insert into cust.case_subject values ('TRQ-031','Box/Bag misloaded','box/es or bag/s misloaded','TRQ','MD',null);


--- create the case operation entries for the new subjects ---

insert into cust.case_operation
select 'TRQ-010', start_case_state, end_case_state,role, caseaction_type
from cust.case_operation where case_subject = 'TRQ-004';

insert into cust.case_operation
select 'TRQ-011', start_case_state, end_case_state,role, caseaction_type
from cust.case_operation where case_subject = 'TRQ-004';

insert into cust.case_operation
select 'TRQ-020', start_case_state, end_case_state,role, caseaction_type
from cust.case_operation where case_subject = 'TRQ-004';

insert into cust.case_operation
select 'TRQ-021', start_case_state, end_case_state,role, caseaction_type
from cust.case_operation where case_subject = 'TRQ-004';

insert into cust.case_operation
select 'TRQ-030', start_case_state, end_case_state,role, caseaction_type
from cust.case_operation where case_subject = 'TRQ-004';

insert into cust.case_operation
select 'TRQ-031', start_case_state, end_case_state,role, caseaction_type
from cust.case_operation where case_subject = 'TRQ-004'
