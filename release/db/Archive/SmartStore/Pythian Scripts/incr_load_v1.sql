--connect to dwdev1 as ss_analysis

conn ss_analysis

begin
  insert into ss_analysis.sku
  select distinct id from (
    select id from ss_analysis.contentnode where contenttype_id = 'Sku'
      and sq_id >= (select min_sq_id from ss_analysis.refresh_log
                    where table_name = 'ss_analysis.contentnode_snap1')
    union all
    select sku_code from ss_analysis.orderline_snap
      where sq_id >= (select min_sq_id from ss_analysis.refresh_log
                      where table_name = 'ss_analysis.orderline_snap1')
    minus select id from ss_analysis.sku
  );


  insert into ss_analysis.sale
  select s.id, s.customer_id, sa.requested_date
  from ss_analysis.sale_snap s, ss_analysis.salesaction sa
  where sa.sale_id = s.id
        and s.cromod_date = sa.action_date
        and s.sq_id >= (select min_sq_id from ss_analysis.refresh_log
                        where table_name = 'ss_analysis.sale_snap1')
        and sa.sq_id >= (select min_sq_id from ss_analysis.refresh_log
                         where table_name = 'ss_analysis.salesaction_snap1');


  insert into ss_analysis.orderline
  select ol.id, sa.sale_id, ol.sku_code
  from ss_analysis.sale_snap s, ss_analysis.salesaction sa, ss_analysis.orderline_snap ol
  where sa.sale_id = s.id
        and s.cromod_date = sa.action_date
        and ol.salesaction_id = sa.id
        and s.sq_id >= (select min_sq_id from ss_analysis.refresh_log
                        where table_name = 'ss_analysis.sale_snap1')
        and sa.sq_id >= (select min_sq_id from ss_analysis.refresh_log
                         where table_name = 'ss_analysis.salesaction_snap1')
        and ol.sq_id >= (select min_sq_id from ss_analysis.refresh_log
                         where table_name = 'ss_analysis.orderline_snap1');


  insert into ss_analysis.store
  select substr(parent_id,1,instr(parent_id,':')-1) parent_content_type,
         substr(parent_id,instr(parent_id,':')+1) parent_content_id,
         substr(child_id,1,instr(child_id,':')-1) content_type,
         substr(child_id,instr(child_id,':')+1) content_id
  from (select parent_contentnode_id parent_id,
               child_contentnode_id child_id
        from ss_analysis.all_nodes
        where sq_id >= (select min_sq_id from ss_analysis.refresh_log
                        where table_name = 'ss_analysis.all_nodes_snap1')
        union all
        select child_contentnode_id, parent_contentnode_id from ss_analysis.relationship
        where sq_id >= (select min_sq_id from ss_analysis.refresh_log
                        where table_name = 'ss_analysis.relationship_snap1')
        union all
        select null, orig_id from ss_analysis.contentnode where contenttype_id = 'Store'
        where sq_id >= (select min_sq_id from ss_analysis.refresh_log
                        where table_name = 'ss_analysis.contentnode_snap1')
       );

  commit;

exeception
  when others do
    rollback;
end;
