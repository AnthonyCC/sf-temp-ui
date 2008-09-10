--connect to dwdev1 as ss_analysis

conn ss_analysis

drop materialized view ss_analysis.customer_snap1;
drop table ss_analysis.customer_snap1;
create table ss_analysis.customer_snap1 (id varchar2(16), constraint pk_customer primary key (id)) organization index;
create or replace view ss_analysis.customer as select * from ss_analysis.customer_snap1;

create materialized view ss_analysis.customer_snap1
on prebuilt table
refresh fast start with sysdate next sysdate + 1/24
as
select id
from cust.customer@devint1.freshdirect.com;

exec dbms_snapshot.refresh('ss_analysis.customer_snap1','c');
exec dbms_snapshot.refresh('ss_analysis.customer_snap1','f');



drop table ss_analysis.refresh_log;
create table ss_analysis.refresh_log (table_name varchar2(32), min_sq_id number, constraint pk_refresh_log primary key (table_name));

create or replace procedure ss_analysis.update_refresh_log (p_table_name varchar2, p_sq_id number default 0) is
  v_sq_id number;
begin
  if p_sq_id = 0 then
    execute immediate 'select max(sq_id) from '||p_table_name into v_sq_id;
  else
    v_sq_id := p_sq_id;
  end if;
  update ss_analysis.refresh_log set min_sq_id = v_sq_id
    where table_name = p_table_name;
  if sql%rowcount = 0 then
    insert into ss_analysis.refresh_log (table_name, min_sq_id)
      values (p_table_name, v_sq_id);
  end if;
  commit;
end;
/



drop sequence ss_analysis.sq_orderline;
create sequence ss_analysis.sq_orderline nocache;
drop materialized view ss_analysis.orderline_snap1;
drop table ss_analysis.orderline_snap1;

create table ss_analysis.orderline_snap1
 (id varchar2(16), sku_code varchar2(18), salesaction_id varchar2(16), sq_id number,
  constraint pk_orderline primary key (id)) organization index;

create or replace trigger ss_analysis.trg_orderline_snap1_bir
  before insert on ss_analysis.orderline_snap1 for each row
declare
  v_sq_id number;
begin
  select ss_analysis.sq_orderline.nextval into v_sq_id from dual;
  :new.sq_id := v_sq_id;
end;
/

create or replace view ss_analysis.orderline_snap as select * from ss_analysis.orderline_snap1;

create materialized view ss_analysis.orderline_snap1
on prebuilt table
refresh fast start with sysdate next sysdate + 1/24
as
select id, sku_code, salesaction_id
from cust.orderline@devint1.freshdirect.com;

exec dbms_snapshot.refresh('ss_analysis.orderline_snap1','c');
exec dbms_snapshot.refresh('ss_analysis.orderline_snap1','f');
exec ss_analysis.update_refresh_log('ss_analysis.orderline_snap1');




drop sequence ss_analysis.sq_contentnode;
create sequence ss_analysis.sq_contentnode nocache;
drop materialized view ss_analysis.contentnode_snap1;
drop table ss_analysis.contentnode_snap1;

create table ss_analysis.contentnode_snap1
 (orig_id varchar2(128), id varchar2(128), contenttype_id varchar2(40), sq_id number,
  constraint pk_contentnode primary key (orig_id)) organization index;

create or replace trigger ss_analysis.trg_contentnode_snap1_bir
  before insert on ss_analysis.contentnode_snap1 for each row
declare
  v_sq_id number;
begin
  select ss_analysis.sq_contentnode.nextval into v_sq_id from dual;
  :new.sq_id := v_sq_id;
end;
/

create or replace view ss_analysis.contentnode as select * from ss_analysis.contentnode_snap1;

create materialized view ss_analysis.contentnode_snap1
on prebuilt table
refresh fast start with sysdate next sysdate + 1/24
as
select id orig_id, substr(id, instr(id, ':') + 1) id, contenttype_id
from cms.contentnode@devint1.freshdirect.com
where contenttype_id in ('Sku','Store');

exec dbms_snapshot.refresh('ss_analysis.contentnode_snap1','c');
exec dbms_snapshot.refresh('ss_analysis.contentnode_snap1','f');
exec ss_analysis.update_refresh_log('ss_analysis.contentnode_snap1');




drop sequence ss_analysis.sq_all_nodes;
create sequence ss_analysis.sq_all_nodes nocache;
drop materialized view ss_analysis.all_nodes_snap1;
drop table ss_analysis.all_nodes_snap1;

create table ss_analysis.all_nodes_snap1
 (parent_contentnode_id varchar2(128), child_contentnode_id varchar2(128), id varchar2(40), sq_id number,
  constraint pk_all_nodes primary key (id)) organization index;

create or replace trigger ss_analysis.trg_all_nodes_snap1_bir
  before insert on ss_analysis.all_nodes_snap1 for each row
declare
  v_sq_id number;
begin
  select ss_analysis.sq_all_nodes.nextval into v_sq_id from dual;
  :new.sq_id := v_sq_id;
end;
/

create or replace view ss_analysis.all_nodes as select * from ss_analysis.all_nodes_snap1;

create materialized view ss_analysis.all_nodes_snap1
on prebuilt table
refresh fast start with sysdate next sysdate + 1/24
as
select parent_contentnode_id, child_contentnode_id, id
from cms.all_nodes@devint1.freshdirect.com
where def_contenttype in ('Sku','Category','Department','Store');

exec dbms_snapshot.refresh('ss_analysis.all_nodes_snap1','c');
exec dbms_snapshot.refresh('ss_analysis.all_nodes_snap1','f');
exec ss_analysis.update_refresh_log('ss_analysis.all_nodes_snap1');




drop sequence ss_analysis.sq_relationship;
create sequence ss_analysis.sq_relationship nocache;
drop materialized view ss_analysis.relationship_snap1;
drop table ss_analysis.relationship_snap1;

create table ss_analysis.relationship_snap1
 (parent_contentnode_id varchar2(128), child_contentnode_id varchar2(128), id varchar2(40), sq_id number,
  constraint pk_relationship primary key (id)) organization index;

create or replace trigger ss_analysis.trg_relationship_snap1_bir
  before insert on ss_analysis.relationship_snap1 for each row
declare
  v_sq_id number;
begin
  select ss_analysis.sq_relationship.nextval into v_sq_id from dual;
  :new.sq_id := v_sq_id;
end;
/

create or replace view ss_analysis.relationship as select * from ss_analysis.relationship_snap1;

create materialized view ss_analysis.relationship_snap1
on prebuilt table
refresh fast start with sysdate next sysdate + 1/24
as
select parent_contentnode_id, child_contentnode_id, id
from cms.relationship@devint1.freshdirect.com
where def_name in ('PRIMARY_HOME');

exec dbms_snapshot.refresh('ss_analysis.relationship_snap1','c');
exec dbms_snapshot.refresh('ss_analysis.relationship_snap1','f');
exec ss_analysis.update_refresh_log('ss_analysis.relationship_snap1');




drop sequence ss_analysis.sq_sale;
create sequence ss_analysis.sq_sale nocache;
drop materialized view ss_analysis.sale_snap1;
drop table ss_analysis.sale_snap1;

create table ss_analysis.sale_snap1
 (id varchar2(16), customer_id varchar2(16), cromod_date date, sq_id number,
  constraint pk_sale primary key (id)) organization index;

create or replace trigger ss_analysis.trg_sale_snap1_bir
  before insert on ss_analysis.sale_snap1 for each row
declare
  v_sq_id number;
begin
  select ss_analysis.sq_sale.nextval into v_sq_id from dual;
  :new.sq_id := v_sq_id;
end;
/

create or replace view ss_analysis.sale_snap as select * from ss_analysis.sale_snap1;

create materialized view ss_analysis.sale_snap1
on prebuilt table
refresh fast start with sysdate next sysdate + 1/24
as
select id, customer_id, cromod_date
from cust.sale@devint1.freshdirect.com
where status not in ('CAN','NEW','SUB');

exec dbms_snapshot.refresh('ss_analysis.sale_snap1','c');
exec dbms_snapshot.refresh('ss_analysis.sale_snap1','f');
exec ss_analysis.update_refresh_log('ss_analysis.sale_snap1');




drop sequence ss_analysis.sq_salesaction;
create sequence ss_analysis.sq_salesaction nocache;
drop materialized view ss_analysis.salesaction_snap1;
drop table ss_analysis.salesaction_snap1;

create table ss_analysis.salesaction_snap1
 (id varchar2(16), sale_id varchar2(16), requested_date date, action_date date, sq_id number,
  constraint pk_salesaction primary key (id)) organization index;

create or replace trigger ss_analysis.trg_salesaction_snap1_bir
  before insert on ss_analysis.salesaction_snap1 for each row
declare
  v_sq_id number;
begin
  select ss_analysis.sq_salesaction.nextval into v_sq_id from dual;
  :new.sq_id := v_sq_id;
end;
/

create or replace view ss_analysis.salesaction as select * from ss_analysis.salesaction_snap1;

create materialized view ss_analysis.salesaction_snap1
on prebuilt table
refresh fast start with sysdate next sysdate + 1/24
as
select id, sale_id, requested_date, action_date
from cust.salesaction@devint1.freshdirect.com
where action_type in ('CRO','MOD');

exec dbms_snapshot.refresh('ss_analysis.salesaction_snap1','c');
exec dbms_snapshot.refresh('ss_analysis.salesaction_snap1','f');
exec ss_analysis.update_refresh_log('ss_analysis.salesaction_snap1');
