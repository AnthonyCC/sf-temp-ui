insert into ss_site_feature (id, title, prez_title, prez_desc, smart_saving) values ('RIGHT_NAV_PERS', 'Personal recommendations on right navigation bar', '', '', 0);
insert into ss_site_feature (id, title, prez_title, prez_desc, smart_saving) values ('RIGHT_NAV_RLTD', 'Related recommendations on right navigation bar', '', '', 0);
insert into ss_site_feature (id, title, prez_title, prez_desc, smart_saving) values ('BRWS_CAT_LST', 'Browse category listing page recommendations', '', '', 0);
insert into ss_site_feature (id, title, prez_title, prez_desc, smart_saving) values ('BRWS_PRD_LST', 'Browse product listing page recommendations', '', '', 0);

insert into ss_variants (id, config_id, feature, type, alias_id, archived) values ('right_nav_certona', null, 'RIGHT_NAV_PERS', 'scripted', '', 'N');
insert into ss_variants (id, config_id, feature, type, alias_id, archived) values ('right_nav_scarab', null, 'RIGHT_NAV_PERS', 'scripted', '', 'N');
insert into ss_variants (id, config_id, feature, type, alias_id, archived) values ('right_nav_certona_rltd', null, 'RIGHT_NAV_RLTD', 'scripted', '', 'N');
insert into ss_variants (id, config_id, feature, type, alias_id, archived) values ('right_nav_scarab_rltd', null, 'RIGHT_NAV_RLTD', 'scripted', '', 'N');
insert into ss_variants (id, config_id, feature, type, alias_id, archived) values ('right_nav_scarab_pop', null, 'RIGHT_NAV_RLTD', 'alias', 'prod-grp-pop-1', 'N');

insert into ss_variant_params (id, key, value) values ('right_nav_certona', 'prez_desc', 'People who bought the same items as you enjoyed...');
insert into ss_variant_params (id, key, value) values ('right_nav_certona', 'cat_aggr', 'false');
insert into ss_variant_params (id, key, value) values ('right_nav_certona', 'hide_bursts', '');
insert into ss_variant_params (id, key, value) values ('right_nav_certona', 'prez_title', 'You May Also Like');
insert into ss_variant_params (id, key, value) values ('right_nav_certona', 'include_cart_items', 'false');
insert into ss_variant_params (id, key, value) values ('right_nav_certona', 'generator', 'PersonalizedItems_certonaDepartment1rr()');
insert into ss_variant_params (id, key, value) values ('right_nav_certona', 'use_alternatives', 'true');
insert into ss_variant_params (id, key, value) values ('right_nav_certona', 'brand_uniq_sort', 'false');
insert into ss_variant_params (id, key, value) values ('right_nav_certona', 'sampling_strat', 'deterministic');

insert into ss_variant_params (id, key, value) values ('right_nav_scarab', 'exponent', '0.4');
insert into ss_variant_params (id, key, value) values ('right_nav_scarab', 'prez_desc', 'People who bought the same items as you enjoyed...');
insert into ss_variant_params (id, key, value) values ('right_nav_scarab', 'cat_aggr', 'false');
insert into ss_variant_params (id, key, value) values ('right_nav_scarab', 'hide_bursts', '');
insert into ss_variant_params (id, key, value) values ('right_nav_scarab', 'prez_title', 'You May Also Like');
insert into ss_variant_params (id, key, value) values ('right_nav_scarab', 'include_cart_items', 'false');
insert into ss_variant_params (id, key, value) values ('right_nav_scarab', 'generator', 'PersonalizedItems_scarab1()');
insert into ss_variant_params (id, key, value) values ('right_nav_scarab', 'use_alternatives', 'true');
insert into ss_variant_params (id, key, value) values ('right_nav_scarab', 'brand_uniq_sort', 'false');
insert into ss_variant_params (id, key, value) values ('right_nav_scarab', 'sampling_strat', 'power');

insert into ss_variant_params (id, key, value) values ('right_nav_scarab_rltd', 'exponent', '0.4');
insert into ss_variant_params (id, key, value) values ('right_nav_scarab_rltd', 'prez_desc', 'People who bought the same items as you enjoyed...');
insert into ss_variant_params (id, key, value) values ('right_nav_scarab_rltd', 'cat_aggr', 'false');
insert into ss_variant_params (id, key, value) values ('right_nav_scarab_rltd', 'hide_bursts', '');
insert into ss_variant_params (id, key, value) values ('right_nav_scarab_rltd', 'prez_title', 'You May Also Like');
insert into ss_variant_params (id, key, value) values ('right_nav_scarab_rltd', 'include_cart_items', 'false');
insert into ss_variant_params (id, key, value) values ('right_nav_scarab_rltd', 'generator', 'RelatedItems_scarabAlsoViewed(currentNode)+SmartYMAL():deprioritize()');
insert into ss_variant_params (id, key, value) values ('right_nav_scarab_rltd', 'use_alternatives', 'true');
insert into ss_variant_params (id, key, value) values ('right_nav_scarab_rltd', 'brand_uniq_sort', 'false');
insert into ss_variant_params (id, key, value) values ('right_nav_scarab_rltd', 'sampling_strat', 'power');

insert into ss_variant_params(id, key, value) values('right_nav_scarab_pop', 'prez_title', 'Customer Favorites');

insert into ss_variant_params (id, key, value) values ('right_nav_certona_rltd', 'prez_desc', 'People who bought the same items as you enjoyed...');
insert into ss_variant_params (id, key, value) values ('right_nav_certona_rltd', 'cat_aggr', 'false');
insert into ss_variant_params (id, key, value) values ('right_nav_certona_rltd', 'hide_bursts', '');
insert into ss_variant_params (id, key, value) values ('right_nav_certona_rltd', 'prez_title', 'You May Also Like');
insert into ss_variant_params (id, key, value) values ('right_nav_certona_rltd', 'include_cart_items', 'false');
insert into ss_variant_params (id, key, value) values ('right_nav_certona_rltd', 'generator', 'RelatedItems_certonaRelatedDepartment1rr(currentNode)');
insert into ss_variant_params (id, key, value) values ('right_nav_certona_rltd', 'use_alternatives', 'true');
insert into ss_variant_params (id, key, value) values ('right_nav_certona_rltd', 'brand_uniq_sort', 'false');
insert into ss_variant_params (id, key, value) values ('right_nav_certona_rltd', 'sampling_strat', 'deterministic');

insert into ss_variants (id, config_id, feature, type, alias_id, archived) values ('brws_cat_list_certona', null, 'BRWS_CAT_LST', 'scripted', '', 'N');
insert into ss_variants (id, config_id, feature, type, alias_id, archived) values ('brws_cat_list_scr_pers', null, 'BRWS_CAT_LST', 'scripted', '', 'N');

insert into ss_variant_params (id, key, value) values ('brws_cat_list_certona', 'prez_desc', 'People who bought the same items as you enjoyed...');
insert into ss_variant_params (id, key, value) values ('brws_cat_list_certona', 'cat_aggr', 'false');
insert into ss_variant_params (id, key, value) values ('brws_cat_list_certona', 'hide_bursts', '');
insert into ss_variant_params (id, key, value) values ('brws_cat_list_certona', 'prez_title', 'Other Customers Enjoyed...');
insert into ss_variant_params (id, key, value) values ('brws_cat_list_certona', 'include_cart_items', 'false');
insert into ss_variant_params (id, key, value) values ('brws_cat_list_certona', 'generator', 'PersonalizedItems_certonaCategory1rr()');
insert into ss_variant_params (id, key, value) values ('brws_cat_list_certona', 'use_alternatives', 'true');
insert into ss_variant_params (id, key, value) values ('brws_cat_list_certona', 'brand_uniq_sort', 'false');
insert into ss_variant_params (id, key, value) values ('brws_cat_list_certona', 'sampling_strat', 'deterministic');


insert into ss_variant_params (id, key, value) values ('brws_cat_list_scr_pers', 'prez_desc', 'People who bought the same items as you enjoyed...');
insert into ss_variant_params (id, key, value) values ('brws_cat_list_scr_pers', 'cat_aggr', 'false');
insert into ss_variant_params (id, key, value) values ('brws_cat_list_scr_pers', 'hide_bursts', '');
insert into ss_variant_params (id, key, value) values ('brws_cat_list_scr_pers', 'prez_title', 'Other Customers Enjoyed...');
insert into ss_variant_params (id, key, value) values ('brws_cat_list_scr_pers', 'include_cart_items', 'false');
insert into ss_variant_params (id, key, value) values ('brws_cat_list_scr_pers', 'generator', 'PersonalizedItems_scarab1()');
insert into ss_variant_params (id, key, value) values ('brws_cat_list_scr_pers', 'use_alternatives', 'true');
insert into ss_variant_params (id, key, value) values ('brws_cat_list_scr_pers', 'brand_uniq_sort', 'false');
insert into ss_variant_params (id, key, value) values ('brws_cat_list_scr_pers', 'sampling_strat', 'power');

insert into ss_variants (id, config_id, feature, type, alias_id, archived) values ('brws_prd_list_certona', null, 'BRWS_PRD_LST', 'scripted', '', 'N');
insert into ss_variants (id, config_id, feature, type, alias_id, archived) values ('brws_prd_list_scr_pers', null, 'BRWS_PRD_LST', 'scripted', '', 'N');

insert into ss_variant_params (id, key, value) values ('brws_prd_list_certona', 'prez_desc', 'People who bought the same items as you enjoyed...');
insert into ss_variant_params (id, key, value) values ('brws_prd_list_certona', 'cat_aggr', 'false');
insert into ss_variant_params (id, key, value) values ('brws_prd_list_certona', 'hide_bursts', '');
insert into ss_variant_params (id, key, value) values ('brws_prd_list_certona', 'prez_title', 'Other Customers Enjoyed...');
insert into ss_variant_params (id, key, value) values ('brws_prd_list_certona', 'include_cart_items', 'false');
insert into ss_variant_params (id, key, value) values ('brws_prd_list_certona', 'generator', 'PersonalizedItems_certonaSubCategory1rr()');
insert into ss_variant_params (id, key, value) values ('brws_prd_list_certona', 'use_alternatives', 'true');
insert into ss_variant_params (id, key, value) values ('brws_prd_list_certona', 'brand_uniq_sort', 'false');
insert into ss_variant_params (id, key, value) values ('brws_prd_list_certona', 'sampling_strat', 'deterministic');

insert into ss_variant_params (id, key, value) values ('brws_prd_list_scr_pers', 'exponent', '0.4');
insert into ss_variant_params (id, key, value) values ('brws_prd_list_scr_pers', 'prez_desc', 'People who bought the same items as you enjoyed...');
insert into ss_variant_params (id, key, value) values ('brws_prd_list_scr_pers', 'cat_aggr', 'false');
insert into ss_variant_params (id, key, value) values ('brws_prd_list_scr_pers', 'hide_bursts', '');
insert into ss_variant_params (id, key, value) values ('brws_prd_list_scr_pers', 'prez_title', 'Other Customers Enjoyed...');
insert into ss_variant_params (id, key, value) values ('brws_prd_list_scr_pers', 'include_cart_items', 'false');
insert into ss_variant_params (id, key, value) values ('brws_prd_list_scr_pers', 'generator', 'PersonalizedItems_scarab1()');
insert into ss_variant_params (id, key, value) values ('brws_prd_list_scr_pers', 'use_alternatives', 'true');
insert into ss_variant_params (id, key, value) values ('brws_prd_list_scr_pers', 'brand_uniq_sort', 'false');
insert into ss_variant_params (id, key, value) values ('brws_prd_list_scr_pers', 'sampling_strat', 'power');

insert into ss_variants (id, config_id, feature, type, alias_id, archived) values ('ymal_pdtl_certona', null, 'YMAL_PDTL', 'scripted', '', 'N');
insert into ss_variants (id, config_id, feature, type, alias_id, archived) values ('ymal_certona', null, 'YMAL', 'scripted', '', 'N');
insert into ss_variants (id, config_id, feature, type, alias_id, archived) values ('srch_certona', null, 'SRCH', 'scripted', '', 'N');


insert into ss_variant_params (id, key, value) values ('ymal_pdtl_certona', 'prez_desc', null);
insert into ss_variant_params (id, key, value) values ('ymal_pdtl_certona', 'cat_aggr', 'false');
insert into ss_variant_params (id, key, value) values ('ymal_pdtl_certona', 'hide_bursts', '');
insert into ss_variant_params (id, key, value) values ('ymal_pdtl_certona', 'prez_title', 'You May Also Like');
insert into ss_variant_params (id, key, value) values ('ymal_pdtl_certona', 'include_cart_items', 'false');
insert into ss_variant_params (id, key, value) values ('ymal_pdtl_certona', 'generator', 'RelatedItems_certonaProduct1rr(currentNode)');
insert into ss_variant_params (id, key, value) values ('ymal_pdtl_certona', 'use_alternatives', 'true');
insert into ss_variant_params (id, key, value) values ('ymal_pdtl_certona', 'brand_uniq_sort', 'false');
insert into ss_variant_params (id, key, value) values ('ymal_pdtl_certona', 'sampling_strat', 'deterministic');

insert into ss_variant_params (id, key, value) values ('ymal_certona', 'prez_desc', null);
insert into ss_variant_params (id, key, value) values ('ymal_certona', 'cat_aggr', 'false');
insert into ss_variant_params (id, key, value) values ('ymal_certona', 'hide_bursts', '');
insert into ss_variant_params (id, key, value) values ('ymal_certona', 'prez_title', null);
insert into ss_variant_params (id, key, value) values ('ymal_certona', 'include_cart_items', 'false');
insert into ss_variant_params (id, key, value) values ('ymal_certona', 'generator', 'RelatedItems_certonaCartConfirm1rr(currentNode)');
insert into ss_variant_params (id, key, value) values ('ymal_certona', 'use_alternatives', 'true');
insert into ss_variant_params (id, key, value) values ('ymal_certona', 'brand_uniq_sort', 'false');
insert into ss_variant_params (id, key, value) values ('ymal_certona', 'sampling_strat', 'deterministic');

insert into ss_variant_params (id, key, value) values ('srch_certona', 'prez_desc', null);
insert into ss_variant_params (id, key, value) values ('srch_certona', 'cat_aggr', 'false');
insert into ss_variant_params (id, key, value) values ('srch_certona', 'hide_bursts', '');
insert into ss_variant_params (id, key, value) values ('srch_certona', 'prez_title', null);
insert into ss_variant_params (id, key, value) values ('srch_certona', 'include_cart_items', 'false');
insert into ss_variant_params (id, key, value) values ('srch_certona', 'generator', 'PersonalizedItems_certonaSearch1rr()');
insert into ss_variant_params (id, key, value) values ('srch_certona', 'use_alternatives', 'true');
insert into ss_variant_params (id, key, value) values ('srch_certona', 'brand_uniq_sort', 'false');
insert into ss_variant_params (id, key, value) values ('srch_certona', 'sampling_strat', 'deterministic');

