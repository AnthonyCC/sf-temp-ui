--
-- Define default variant for Featured Items Site Feature
--
insert into cust.ss_service_configs(id, type) values('def-fi', 'featured_items');
insert into cust.ss_variants(id, config_id, feature) values('FeaturedCMS', 'def-fi', 'FEATURED_ITEMS');

--
-- Create default cohort->variant map
--
insert into cust.ss_variant_assignment(COHORT_ID, "DATE", variant_id) VALUES('C1', CURRENT_DATE, 'FeaturedCMS');
insert into cust.ss_variant_assignment(COHORT_ID, "DATE", variant_id) VALUES('C2', CURRENT_DATE, 'FeaturedCMS');
insert into cust.ss_variant_assignment(COHORT_ID, "DATE", variant_id) VALUES('C3', CURRENT_DATE, 'FeaturedCMS');
insert into cust.ss_variant_assignment(COHORT_ID, "DATE", variant_id) VALUES('C4', CURRENT_DATE, 'FeaturedCMS');
insert into cust.ss_variant_assignment(COHORT_ID, "DATE", variant_id) VALUES('C5', CURRENT_DATE, 'FeaturedCMS');
insert into cust.ss_variant_assignment(COHORT_ID, "DATE", variant_id) VALUES('C6', CURRENT_DATE, 'FeaturedCMS');
insert into cust.ss_variant_assignment(COHORT_ID, "DATE", variant_id) VALUES('C7', CURRENT_DATE, 'FeaturedCMS');
insert into cust.ss_variant_assignment(COHORT_ID, "DATE", variant_id) VALUES('C8', CURRENT_DATE, 'FeaturedCMS');
insert into cust.ss_variant_assignment(COHORT_ID, "DATE", variant_id) VALUES('C9', CURRENT_DATE, 'FeaturedCMS');
insert into cust.ss_variant_assignment(COHORT_ID, "DATE", variant_id) VALUES('C10', CURRENT_DATE, 'FeaturedCMS');
insert into cust.ss_variant_assignment(COHORT_ID, "DATE", variant_id) VALUES('C11', CURRENT_DATE, 'FeaturedCMS');
insert into cust.ss_variant_assignment(COHORT_ID, "DATE", variant_id) VALUES('C12', CURRENT_DATE, 'FeaturedCMS');
insert into cust.ss_variant_assignment(COHORT_ID, "DATE", variant_id) VALUES('C13', CURRENT_DATE, 'FeaturedCMS');
insert into cust.ss_variant_assignment(COHORT_ID, "DATE", variant_id) VALUES('C14', CURRENT_DATE, 'FeaturedCMS');
insert into cust.ss_variant_assignment(COHORT_ID, "DATE", variant_id) VALUES('C15', CURRENT_DATE, 'FeaturedCMS');
insert into cust.ss_variant_assignment(COHORT_ID, "DATE", variant_id) VALUES('C16', CURRENT_DATE, 'FeaturedCMS');
insert into cust.ss_variant_assignment(COHORT_ID, "DATE", variant_id) VALUES('C17', CURRENT_DATE, 'FeaturedCMS');
insert into cust.ss_variant_assignment(COHORT_ID, "DATE", variant_id) VALUES('C18', CURRENT_DATE, 'FeaturedCMS');
insert into cust.ss_variant_assignment(COHORT_ID, "DATE", variant_id) VALUES('C19', CURRENT_DATE, 'FeaturedCMS');
insert into cust.ss_variant_assignment(COHORT_ID, "DATE", variant_id) VALUES('C20', CURRENT_DATE, 'FeaturedCMS');



--
-- Define default variant for Favorites Items Site Feature
--
insert into cust.ss_service_configs(id, type) values('def-fav', 'favorites');
insert into cust.ss_variants(id, config_id, feature) values('Favorites', 'def-fav', 'FAVORITES');
insert into cust.SS_SERVICE_PARAMS(id, key, value) values('def-fav', 'favorite_list_id', 'fd_favorites');


--
-- Create default cohort->variant map
--
insert into cust.ss_variant_assignment(COHORT_ID, "DATE", variant_id) VALUES('C1', CURRENT_DATE, 'Favorites');
insert into cust.ss_variant_assignment(COHORT_ID, "DATE", variant_id) VALUES('C2', CURRENT_DATE, 'Favorites');
insert into cust.ss_variant_assignment(COHORT_ID, "DATE", variant_id) VALUES('C3', CURRENT_DATE, 'Favorites');
insert into cust.ss_variant_assignment(COHORT_ID, "DATE", variant_id) VALUES('C4', CURRENT_DATE, 'Favorites');
insert into cust.ss_variant_assignment(COHORT_ID, "DATE", variant_id) VALUES('C5', CURRENT_DATE, 'Favorites');
insert into cust.ss_variant_assignment(COHORT_ID, "DATE", variant_id) VALUES('C6', CURRENT_DATE, 'Favorites');
insert into cust.ss_variant_assignment(COHORT_ID, "DATE", variant_id) VALUES('C7', CURRENT_DATE, 'Favorites');
insert into cust.ss_variant_assignment(COHORT_ID, "DATE", variant_id) VALUES('C8', CURRENT_DATE, 'Favorites');
insert into cust.ss_variant_assignment(COHORT_ID, "DATE", variant_id) VALUES('C9', CURRENT_DATE, 'Favorites');
insert into cust.ss_variant_assignment(COHORT_ID, "DATE", variant_id) VALUES('C10', CURRENT_DATE, 'Favorites');
insert into cust.ss_variant_assignment(COHORT_ID, "DATE", variant_id) VALUES('C11', CURRENT_DATE, 'Favorites');
insert into cust.ss_variant_assignment(COHORT_ID, "DATE", variant_id) VALUES('C12', CURRENT_DATE, 'Favorites');
insert into cust.ss_variant_assignment(COHORT_ID, "DATE", variant_id) VALUES('C13', CURRENT_DATE, 'Favorites');
insert into cust.ss_variant_assignment(COHORT_ID, "DATE", variant_id) VALUES('C14', CURRENT_DATE, 'Favorites');
insert into cust.ss_variant_assignment(COHORT_ID, "DATE", variant_id) VALUES('C15', CURRENT_DATE, 'Favorites');
insert into cust.ss_variant_assignment(COHORT_ID, "DATE", variant_id) VALUES('C16', CURRENT_DATE, 'Favorites');
insert into cust.ss_variant_assignment(COHORT_ID, "DATE", variant_id) VALUES('C17', CURRENT_DATE, 'Favorites');
insert into cust.ss_variant_assignment(COHORT_ID, "DATE", variant_id) VALUES('C18', CURRENT_DATE, 'Favorites');
insert into cust.ss_variant_assignment(COHORT_ID, "DATE", variant_id) VALUES('C19', CURRENT_DATE, 'Favorites');
insert into cust.ss_variant_assignment(COHORT_ID, "DATE", variant_id) VALUES('C20', CURRENT_DATE, 'Favorites');

// Titles,labels for DYF

insert into cust.SS_SERVICE_PARAMS(id, key, value) values('random-dyf', 'prez_title','RANDOM PICKZ');
insert into cust.SS_SERVICE_PARAMS(id, key, value) values('random-dyf', 'prez_desc','These are randomly picked from your purchase history.');

insert into cust.SS_SERVICE_PARAMS(id, key, value) values('freqbought-dyf', 'prez_title','YOUR FAVORITEZ');
insert into cust.SS_SERVICE_PARAMS(id, key, value) values('freqbought-dyf', 'prez_desc','These are some of the items you''ve purchased most often.');

insert into cust.SS_SERVICE_PARAMS(id, key, value) values('def-fav', 'prez_title','FRESHDIRECT FAVORITES');
insert into cust.SS_SERVICE_PARAMS(id, key, value) values('def-fav', 'prez_desc','Pick any item from our favorites.');

// Manual Override recommendation service

insert into cust.ss_service_configs(id, type) values('man-over-fi', 'manual_override');
insert into cust.ss_variants(id, config_id, feature) values('FeaturedPopular', 'man-over-fi', 'FEATURED_ITEMS');


