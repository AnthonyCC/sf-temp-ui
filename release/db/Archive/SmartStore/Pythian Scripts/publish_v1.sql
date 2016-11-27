--connect to devint1 as sysdba

sqlplus / as sysdba

create public database link dwdev1.freshdirect.com
  connect to ss_analysis identified by xxx using 'dwdev1';

create table cust.purchase_history_1 as
  select * from ss_analysis.purchase_history@dwdev1.freshdirect.com where 1<>1;

create index purchase_history_1_index1 on cust.purchase_history_1(customer_id);

create table cust.purchase_history_2 as select * from cust.purchase_history_1;
create index purchase_history_2_index1 on cust.purchase_history_2(customer_id);
create or replace view cust.purchase_history as select * from cust.purchase_history_1;


create or replace procedure cust.refresh_purchase_history as
  v_view_master varchar2(30);
  v_target varchar2(30) := 'PURCHASE_HISTORY';
  v_view_master_new varchar2(30);
  v_sql varchar2(2000);
begin
  select referenced_name into v_view_master from user_dependencies where name = v_target and type = 'VIEW';
  if v_view_master = v_target||'_1' then
    v_view_master_new := v_target||'_2';
  else
    v_view_master_new := v_target||'_1';
  end if;
  v_sql := 'alter table '||v_view_master_new||' nologging';
  execute immediate v_sql;
  v_sql := 'drop index '||v_view_master_new||'_index1';
  execute immediate v_sql;
  v_sql := 'truncate table ' || v_view_master_new;

  v_sql := 'insert /*+ append */ into ' || v_view_master_new ||
           ' select * from ss_analysis.purchase_history@dwdev1.freshdirect.com';

  execute immediate v_sql;
  commit;

  v_sql := 'create index '||v_view_master_new||'_index1 on '||v_view_master_new||'(customer_id)';
  execute immediate v_sql;
  v_sql := 'alter table '||v_view_master_new||' logging';
  execute immediate v_sql;
  v_sql := 'create or replace view '||v_target||' as select * from '||v_view_master_new;
  execute immediate v_sql;
  v_sql := 'truncate table '||v_view_master;
  execute immediate v_sql;
end;
/
