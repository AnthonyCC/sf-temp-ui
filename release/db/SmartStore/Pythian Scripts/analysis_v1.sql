--connect to dwdev1 as ss_analysis

conn ss_analysis

create table ss_analysis.purchase_history_1 as
  select sl.customer_id, st.parent_content_id product_id, count(st.parent_content_id) score
  from ss_analysis.orderline ol, ss_analysis.sale sl, ss_analysis.store st
  where ol.sale_id = sl.id
        and st.content_type = 'Sku'
        and st.content_id = ol.sku_code
        and 1 <> 1
        group by sl.customer_id, st.parent_content_id;

create index purchase_history_1_index1 on ss_analysis.purchase_history_1(customer_id);

create table ss_analysis.purchase_history_2 as select * from ss_analysis.purchase_history_1;
create index purchase_history_2_index1 on ss_analysis.purchase_history_2(customer_id);
create or replace view ss_analysis.purchase_history as select * from ss_analysis.purchase_history_1;


create or replace procedure refresh_purchase_history as
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
           ' select sl.customer_id, st.parent_content_id product_id, count(st.parent_content_id) score
            from ss_analysis.orderline ol, ss_analysis.sale sl, ss_analysis.store st
            where ol.sale_id = sl.id
                  and st.content_type = ''Sku''
                  and st.content_id = ol.sku_code
                  group by sl.customer_id, st.parent_content_id';

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

DECLARE
  X NUMBER;
BEGIN
  SYS.DBMS_JOB.SUBMIT
    ( job       => X 
     ,what      => 'refresh_purchase_history;'
     ,next_date => sysdate
     ,interval  => 'sysdate + 1/24 '
     ,no_parse  => TRUE
    );
END;
/

commit;