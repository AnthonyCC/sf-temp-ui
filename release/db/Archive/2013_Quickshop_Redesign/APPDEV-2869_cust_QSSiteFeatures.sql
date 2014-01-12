--
-- FD Favorites in QuickShop
--
insert into cust.ss_site_feature(ID, title, smart_saving)
values('FAVORITES_QS', 'FreshDirect Favorites in QuickShop', 0);

-- assign an aliased variant
insert into cust.ss_variants(id,feature,type,alias_id,archived)
values('favorites-4qs','FAVORITES_QS', 'alias', 'favorites-4', 'N');

-- distribute
insert into ss_variant_assignment("DATE",cohort_id,variant_id) values(sysdate, 'C1', 'favorites-4qs');
insert into ss_variant_assignment("DATE",cohort_id,variant_id) values(sysdate, 'C2', 'favorites-4qs');
insert into ss_variant_assignment("DATE",cohort_id,variant_id) values(sysdate, 'C3', 'favorites-4qs');
insert into ss_variant_assignment("DATE",cohort_id,variant_id) values(sysdate, 'C4', 'favorites-4qs');
insert into ss_variant_assignment("DATE",cohort_id,variant_id) values(sysdate, 'C5', 'favorites-4qs');
insert into ss_variant_assignment("DATE",cohort_id,variant_id) values(sysdate, 'C6', 'favorites-4qs');
insert into ss_variant_assignment("DATE",cohort_id,variant_id) values(sysdate, 'C7', 'favorites-4qs');
insert into ss_variant_assignment("DATE",cohort_id,variant_id) values(sysdate, 'C8', 'favorites-4qs');
insert into ss_variant_assignment("DATE",cohort_id,variant_id) values(sysdate, 'C9', 'favorites-4qs');
insert into ss_variant_assignment("DATE",cohort_id,variant_id) values(sysdate, 'C10', 'favorites-4qs');

insert into ss_variant_assignment("DATE",cohort_id,variant_id) values(sysdate, 'C11', 'favorites-4qs');
insert into ss_variant_assignment("DATE",cohort_id,variant_id) values(sysdate, 'C12', 'favorites-4qs');
insert into ss_variant_assignment("DATE",cohort_id,variant_id) values(sysdate, 'C13', 'favorites-4qs');
insert into ss_variant_assignment("DATE",cohort_id,variant_id) values(sysdate, 'C14', 'favorites-4qs');
insert into ss_variant_assignment("DATE",cohort_id,variant_id) values(sysdate, 'C15', 'favorites-4qs');
insert into ss_variant_assignment("DATE",cohort_id,variant_id) values(sysdate, 'C16', 'favorites-4qs');
insert into ss_variant_assignment("DATE",cohort_id,variant_id) values(sysdate, 'C17', 'favorites-4qs');
insert into ss_variant_assignment("DATE",cohort_id,variant_id) values(sysdate, 'C18', 'favorites-4qs');
insert into ss_variant_assignment("DATE",cohort_id,variant_id) values(sysdate, 'C19', 'favorites-4qs');
insert into ss_variant_assignment("DATE",cohort_id,variant_id) values(sysdate, 'C20', 'favorites-4qs');



--
-- Deals in QuickShop
--
insert into cust.ss_site_feature(ID, title, smart_saving)
values('DEALS_QS', 'Don''t Miss Deals in QuickShop', 0);

-- assign an aliased variant
insert into cust.ss_variants(id,feature,type,alias_id,archived)
values('deals_qs','DEALS_QS', 'alias', 'c_deal_1', 'N');

-- distribute
insert into ss_variant_assignment("DATE",cohort_id,variant_id) values(sysdate, 'C1', 'deals_qs');
insert into ss_variant_assignment("DATE",cohort_id,variant_id) values(sysdate, 'C2', 'deals_qs');
insert into ss_variant_assignment("DATE",cohort_id,variant_id) values(sysdate, 'C3', 'deals_qs');
insert into ss_variant_assignment("DATE",cohort_id,variant_id) values(sysdate, 'C4', 'deals_qs');
insert into ss_variant_assignment("DATE",cohort_id,variant_id) values(sysdate, 'C5', 'deals_qs');
insert into ss_variant_assignment("DATE",cohort_id,variant_id) values(sysdate, 'C6', 'deals_qs');
insert into ss_variant_assignment("DATE",cohort_id,variant_id) values(sysdate, 'C7', 'deals_qs');
insert into ss_variant_assignment("DATE",cohort_id,variant_id) values(sysdate, 'C8', 'deals_qs');
insert into ss_variant_assignment("DATE",cohort_id,variant_id) values(sysdate, 'C9', 'deals_qs');
insert into ss_variant_assignment("DATE",cohort_id,variant_id) values(sysdate, 'C10', 'deals_qs');

insert into ss_variant_assignment("DATE",cohort_id,variant_id) values(sysdate, 'C11', 'deals_qs');
insert into ss_variant_assignment("DATE",cohort_id,variant_id) values(sysdate, 'C12', 'deals_qs');
insert into ss_variant_assignment("DATE",cohort_id,variant_id) values(sysdate, 'C13', 'deals_qs');
insert into ss_variant_assignment("DATE",cohort_id,variant_id) values(sysdate, 'C14', 'deals_qs');
insert into ss_variant_assignment("DATE",cohort_id,variant_id) values(sysdate, 'C15', 'deals_qs');
insert into ss_variant_assignment("DATE",cohort_id,variant_id) values(sysdate, 'C16', 'deals_qs');
insert into ss_variant_assignment("DATE",cohort_id,variant_id) values(sysdate, 'C17', 'deals_qs');
insert into ss_variant_assignment("DATE",cohort_id,variant_id) values(sysdate, 'C18', 'deals_qs');
insert into ss_variant_assignment("DATE",cohort_id,variant_id) values(sysdate, 'C19', 'deals_qs');
insert into ss_variant_assignment("DATE",cohort_id,variant_id) values(sysdate, 'C20', 'deals_qs');



--
-- Expert Rated in QuickShop
--
insert into cust.ss_site_feature(ID, title, smart_saving)
values('EXPRATED_QS', 'Expert Rated in QuickShop', 0);

-- new global variant
insert into cust.ss_variants(id,feature,type,alias_id,archived)
values('exprated_qs','EXPRATED_QS', 'scripted', NULL, 'N');

-- parameters
insert into cust.ss_variant_params(id,key,value)
values('exprated_qs','prez_title', 'Expert Ratings');
insert into cust.ss_variant_params(id,key,value)
values('exprated_qs','sampling_strat', 'power');
insert into cust.ss_variant_params(id,key,value)
values('exprated_qs','exponent', '0.4');
insert into cust.ss_variant_params(id,key,value)
values('exprated_qs','top_n', '100');
insert into cust.ss_variant_params(id,key,value)
values('exprated_qs','top_perc', '2.0');
insert into cust.ss_variant_params(id,key,value)
values('exprated_qs','include_cart_items', 'false');
insert into cust.ss_variant_params(id,key,value)
values('exprated_qs','generator', 'RecursiveNodes("fru","veg","sea"):atLeast(QualityRating_Discretized2,3)');
insert into cust.ss_variant_params(id,key,value)
values('exprated_qs','scoring', 'QualityRating; Popularity_Discretized');
insert into cust.ss_variant_params(id,key,value)
values('exprated_qs','use_alternatives', 'true');

-- distribute
insert into ss_variant_assignment("DATE",cohort_id,variant_id) values(sysdate, 'C1', 'exprated_qs');
insert into ss_variant_assignment("DATE",cohort_id,variant_id) values(sysdate, 'C2', 'exprated_qs');
insert into ss_variant_assignment("DATE",cohort_id,variant_id) values(sysdate, 'C3', 'exprated_qs');
insert into ss_variant_assignment("DATE",cohort_id,variant_id) values(sysdate, 'C4', 'exprated_qs');
insert into ss_variant_assignment("DATE",cohort_id,variant_id) values(sysdate, 'C5', 'exprated_qs');
insert into ss_variant_assignment("DATE",cohort_id,variant_id) values(sysdate, 'C6', 'exprated_qs');
insert into ss_variant_assignment("DATE",cohort_id,variant_id) values(sysdate, 'C7', 'exprated_qs');
insert into ss_variant_assignment("DATE",cohort_id,variant_id) values(sysdate, 'C8', 'exprated_qs');
insert into ss_variant_assignment("DATE",cohort_id,variant_id) values(sysdate, 'C9', 'exprated_qs');
insert into ss_variant_assignment("DATE",cohort_id,variant_id) values(sysdate, 'C10', 'exprated_qs');

insert into ss_variant_assignment("DATE",cohort_id,variant_id) values(sysdate, 'C11', 'exprated_qs');
insert into ss_variant_assignment("DATE",cohort_id,variant_id) values(sysdate, 'C12', 'exprated_qs');
insert into ss_variant_assignment("DATE",cohort_id,variant_id) values(sysdate, 'C13', 'exprated_qs');
insert into ss_variant_assignment("DATE",cohort_id,variant_id) values(sysdate, 'C14', 'exprated_qs');
insert into ss_variant_assignment("DATE",cohort_id,variant_id) values(sysdate, 'C15', 'exprated_qs');
insert into ss_variant_assignment("DATE",cohort_id,variant_id) values(sysdate, 'C16', 'exprated_qs');
insert into ss_variant_assignment("DATE",cohort_id,variant_id) values(sysdate, 'C17', 'exprated_qs');
insert into ss_variant_assignment("DATE",cohort_id,variant_id) values(sysdate, 'C18', 'exprated_qs');
insert into ss_variant_assignment("DATE",cohort_id,variant_id) values(sysdate, 'C19', 'exprated_qs');
insert into ss_variant_assignment("DATE",cohort_id,variant_id) values(sysdate, 'C20', 'exprated_qs');

--
-- Customer Rated in QuickShop
--
insert into cust.ss_site_feature(ID, title, smart_saving)
values('CUSTRATED_QS', 'Customer Rated in QuickShop', 0);

-- new global variant
insert into cust.ss_variants(id,feature,type,alias_id,archived)
values('custrated_qs','CUSTRATED_QS', 'scripted', NULL, 'N');

-- parameters
insert into cust.ss_variant_params(id,key,value)
values('custrated_qs','prez_title', 'Customer Ratings');
insert into cust.ss_variant_params(id,key,value)
values('custrated_qs','sampling_strat', 'power');
insert into cust.ss_variant_params(id,key,value)
values('custrated_qs','include_cart_items', 'false');
insert into cust.ss_variant_params(id,key,value)
values('custrated_qs','generator', 'CustomerRatedItems');
insert into cust.ss_variant_params(id,key,value)
values('custrated_qs','scoring', 'CustomerRating');

-- distribute
insert into ss_variant_assignment("DATE",cohort_id,variant_id) values(sysdate, 'C1', 'custrated_qs');
insert into ss_variant_assignment("DATE",cohort_id,variant_id) values(sysdate, 'C2', 'custrated_qs');
insert into ss_variant_assignment("DATE",cohort_id,variant_id) values(sysdate, 'C3', 'custrated_qs');
insert into ss_variant_assignment("DATE",cohort_id,variant_id) values(sysdate, 'C4', 'custrated_qs');
insert into ss_variant_assignment("DATE",cohort_id,variant_id) values(sysdate, 'C5', 'custrated_qs');
insert into ss_variant_assignment("DATE",cohort_id,variant_id) values(sysdate, 'C6', 'custrated_qs');
insert into ss_variant_assignment("DATE",cohort_id,variant_id) values(sysdate, 'C7', 'custrated_qs');
insert into ss_variant_assignment("DATE",cohort_id,variant_id) values(sysdate, 'C8', 'custrated_qs');
insert into ss_variant_assignment("DATE",cohort_id,variant_id) values(sysdate, 'C9', 'custrated_qs');
insert into ss_variant_assignment("DATE",cohort_id,variant_id) values(sysdate, 'C10', 'custrated_qs');

insert into ss_variant_assignment("DATE",cohort_id,variant_id) values(sysdate, 'C11', 'custrated_qs');
insert into ss_variant_assignment("DATE",cohort_id,variant_id) values(sysdate, 'C12', 'custrated_qs');
insert into ss_variant_assignment("DATE",cohort_id,variant_id) values(sysdate, 'C13', 'custrated_qs');
insert into ss_variant_assignment("DATE",cohort_id,variant_id) values(sysdate, 'C14', 'custrated_qs');
insert into ss_variant_assignment("DATE",cohort_id,variant_id) values(sysdate, 'C15', 'custrated_qs');
insert into ss_variant_assignment("DATE",cohort_id,variant_id) values(sysdate, 'C16', 'custrated_qs');
insert into ss_variant_assignment("DATE",cohort_id,variant_id) values(sysdate, 'C17', 'custrated_qs');
insert into ss_variant_assignment("DATE",cohort_id,variant_id) values(sysdate, 'C18', 'custrated_qs');
insert into ss_variant_assignment("DATE",cohort_id,variant_id) values(sysdate, 'C19', 'custrated_qs');
insert into ss_variant_assignment("DATE",cohort_id,variant_id) values(sysdate, 'C20', 'custrated_qs');
