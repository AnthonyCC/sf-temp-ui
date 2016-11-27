insert into cust.ss_site_feature (id, title, prez_title, prez_desc, smart_saving) values ('TOP_ITEMS_QS', 'Your Top Items in QuickShop', '', '', 0);

insert into cust.ss_variants (id, config_id, feature, type, alias_id, archived) values ('top_items_qs', null, 'TOP_ITEMS_QS', 'scripted', '', 'N');

insert into cust.ss_variant_params (id, key, value) values ('top_items_qs', 'cat_aggr', 'false');
insert into cust.ss_variant_params (id, key, value) values ('top_items_qs', 'exponent', '0.4');
insert into cust.ss_variant_params (id, key, value) values ('top_items_qs', 'generator', 'PurchaseHistory');
insert into cust.ss_variant_params (id, key, value) values ('top_items_qs', 'include_cart_items', 'false');
insert into cust.ss_variant_params (id, key, value) values ('top_items_qs', 'prez_desc', 'Your Top Items in QuickShop');
insert into cust.ss_variant_params (id, key, value) values ('top_items_qs', 'prez_title', 'TOP ITEMS');
insert into cust.ss_variant_params (id, key, value) values ('top_items_qs', 'sampling_strat', 'power');
insert into cust.ss_variant_params (id, key, value) values ('top_items_qs', 'scoring', 'Recency_Discretized;Frequency_Discretized;QualityRating_Discretized2;DealsPercentage_Discretized;ReorderRate_DepartmentNormalized');
insert into cust.ss_variant_params (id, key, value) values ('top_items_qs', 'top_n', '50');
insert into cust.ss_variant_params (id, key, value) values ('top_items_qs', 'top_perc', '20');

insert into cust.ss_variant_assignment values ('C1', sysdate, 'top_items_qs');
insert into cust.ss_variant_assignment values ('C2', sysdate, 'top_items_qs');
insert into cust.ss_variant_assignment values ('C3', sysdate, 'top_items_qs');
insert into cust.ss_variant_assignment values ('C4', sysdate, 'top_items_qs');
insert into cust.ss_variant_assignment values ('C5', sysdate, 'top_items_qs');
insert into cust.ss_variant_assignment values ('C6', sysdate, 'top_items_qs');
insert into cust.ss_variant_assignment values ('C7', sysdate, 'top_items_qs');
insert into cust.ss_variant_assignment values ('C8', sysdate, 'top_items_qs');
insert into cust.ss_variant_assignment values ('C9', sysdate, 'top_items_qs');
insert into cust.ss_variant_assignment values ('C10', sysdate, 'top_items_qs');
insert into cust.ss_variant_assignment values ('C11', sysdate, 'top_items_qs');
insert into cust.ss_variant_assignment values ('C12', sysdate, 'top_items_qs');
insert into cust.ss_variant_assignment values ('C13', sysdate, 'top_items_qs');
insert into cust.ss_variant_assignment values ('C14', sysdate, 'top_items_qs');
insert into cust.ss_variant_assignment values ('C15', sysdate, 'top_items_qs');
insert into cust.ss_variant_assignment values ('C16', sysdate, 'top_items_qs');
insert into cust.ss_variant_assignment values ('C17', sysdate, 'top_items_qs');
insert into cust.ss_variant_assignment values ('C18', sysdate, 'top_items_qs');
insert into cust.ss_variant_assignment values ('C19', sysdate, 'top_items_qs');
insert into cust.ss_variant_assignment values ('C20', sysdate, 'top_items_qs');