--connect to dwdev1 as ss_analysis. Need to stop all replication jobs

conn ss_analysis

create table ss_analysis.sku as
  select distinct id from (
    select id from ss_analysis.contentnode where contenttype_id = 'Sku'
    union all
    select sku_code from ss_analysis.orderline_snap
  );

alter table ss_analysis.sku add constraint pk_sku primary key (id);

create table ss_analysis.sale as
  select s.id, s.customer_id, sa.requested_date
  from ss_analysis.sale_snap s, ss_analysis.salesaction sa
  where sa.sale_id = s.id
        and s.cromod_date = sa.action_date;

alter table ss_analysis.sale add constraint sale_customer_id_fk foreign key (customer_id)
  references ss_analysis.customer_snap1 (id);

create index sale_index_customer_id on ss_analysis.sale(customer_id);
create index sale_index_requested_date on ss_analysis.sale(requested_date);

create table ss_analysis.orderline as
  select ol.id, sa.sale_id, ol.sku_code
  from ss_analysis.sale_snap s, ss_analysis.salesaction sa, ss_analysis.orderline_snap ol
  where sa.sale_id = s.id
        and s.cromod_date = sa.action_date
        and ol.salesaction_id = sa.id;

alter table ss_analysis.orderline add constraint orderline_sale_id_fk foreign key (sale_id)
  references ss_analysis.sale_snap1 (id);

alter table ss_analysis.orderline add constraint orderline_sku_code_fk foreign key (sku_code)
  references ss_analysis.sku (id);

create index orderline_index_sale_id on ss_analysis.orderline(sale_id);
create index orderline_index_sku_code on ss_analysis.orderline(sku_code);

create table ss_analysis.store as
  select substr(parent_id,1,instr(parent_id,':')-1) parent_content_type,
         substr(parent_id,instr(parent_id,':')+1) parent_content_id,
         substr(child_id,1,instr(child_id,':')-1) content_type,
         substr(child_id,instr(child_id,':')+1) content_id
  from (select parent_contentnode_id parent_id,
               child_contentnode_id child_id
        from ss_analysis.all_nodes
        union all
        select child_contentnode_id, parent_contentnode_id from ss_analysis.relationship
        union all
        select null, orig_id from ss_analysis.contentnode where contenttype_id = 'Store'
       );

alter table ss_analysis.store add constraint pk_store primary key (content_type, content_id);
create index store_index_content_id on ss_analysis.store(content_id);
create index store_index_parent_content_id on ss_analysis.store(parent_content_id);
create index store_index_par_content_type on ss_analysis.store(parent_content_type);

exec ss_analysis.update_refresh_log('ss_analysis.orderline_snap1');
exec ss_analysis.update_refresh_log('ss_analysis.contentnode_snap1');
exec ss_analysis.update_refresh_log('ss_analysis.all_nodes_snap1');
exec ss_analysis.update_refresh_log('ss_analysis.relationship_snap1');
exec ss_analysis.update_refresh_log('ss_analysis.sale_snap1');
exec ss_analysis.update_refresh_log('ss_analysis.salesaction_snap1');
