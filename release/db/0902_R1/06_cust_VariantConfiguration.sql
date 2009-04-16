--
-- Configure SS 2009 variants
--


--
-- DYF
--

INSERT INTO ss_variants(ID, feature, type)
VALUES('dyf-freqbought1','DYF', 'freqbought_dyf');
INSERT INTO ss_variants(ID, feature, type)
VALUES('dyf-freqbought2','DYF', 'freqbought_dyf');
INSERT INTO ss_variants(ID, feature, type)
VALUES('dyf-freqbought3','DYF', 'freqbought_dyf');
INSERT INTO ss_variants(ID, feature, type)
VALUES('dyf-freqbought4','DYF', 'freqbought_dyf');



-- Configurations

--   config (1)
INSERT INTO SS_VARIANT_PARAMS(ID,KEY,VALUE)
VALUES('dyf-freqbought1','sampling_strat','complicated');
INSERT INTO SS_VARIANT_PARAMS(ID,KEY,VALUE)
VALUES('dyf-freqbought1','cat_aggr','true');
INSERT INTO SS_VARIANT_PARAMS(ID,KEY,VALUE)
VALUES('dyf-freqbought1','top_n','20');
INSERT INTO SS_VARIANT_PARAMS(ID,KEY,VALUE)
VALUES('dyf-freqbought1','top_perc','20');
INSERT INTO SS_VARIANT_PARAMS(ID,KEY,VALUE)
VALUES('dyf-freqbought1','prez_title','YOUR FAVORITES');
INSERT INTO SS_VARIANT_PARAMS(ID,KEY,VALUE)
VALUES('dyf-freqbought1','prez_desc','These are some of the items you''ve purchased most often.');

--   config (2)
INSERT INTO SS_VARIANT_PARAMS(ID,KEY,VALUE)
VALUES('dyf-freqbought2','sampling_strat','complicated');
INSERT INTO SS_VARIANT_PARAMS(ID,KEY,VALUE)
VALUES('dyf-freqbought2','cat_aggr','true');
INSERT INTO SS_VARIANT_PARAMS(ID,KEY,VALUE)
VALUES('dyf-freqbought2','top_n','50');
INSERT INTO SS_VARIANT_PARAMS(ID,KEY,VALUE)
VALUES('dyf-freqbought2','top_perc','20');
INSERT INTO SS_VARIANT_PARAMS(ID,KEY,VALUE)
VALUES('dyf-freqbought2','prez_title','YOUR FAVORITES');
INSERT INTO SS_VARIANT_PARAMS(ID,KEY,VALUE)
VALUES('dyf-freqbought2','prez_desc','These are some of the items you''ve purchased most often.');

--   config (3)
INSERT INTO SS_VARIANT_PARAMS(ID,KEY,VALUE)
VALUES('dyf-freqbought3','sampling_strat','power');
INSERT INTO SS_VARIANT_PARAMS(ID,KEY,VALUE)
VALUES('dyf-freqbought3','exponent','0.5');
INSERT INTO SS_VARIANT_PARAMS(ID,KEY,VALUE)
VALUES('dyf-freqbought3','cat_aggr','true');
INSERT INTO SS_VARIANT_PARAMS(ID,KEY,VALUE)
VALUES('dyf-freqbought3','top_n','50');
INSERT INTO SS_VARIANT_PARAMS(ID,KEY,VALUE)
VALUES('dyf-freqbought3','top_perc','20');
INSERT INTO SS_VARIANT_PARAMS(ID,KEY,VALUE)
VALUES('dyf-freqbought3','prez_title','YOUR FAVORITES');
INSERT INTO SS_VARIANT_PARAMS(ID,KEY,VALUE)
VALUES('dyf-freqbought3','prez_desc','These are some of the items you''ve purchased most often.');

--   config (4)
INSERT INTO SS_VARIANT_PARAMS(ID,KEY,VALUE)
VALUES('dyf-freqbought4','sampling_strat','power');
INSERT INTO SS_VARIANT_PARAMS(ID,KEY,VALUE)
VALUES('dyf-freqbought4','exponent','0.8');
INSERT INTO SS_VARIANT_PARAMS(ID,KEY,VALUE)
VALUES('dyf-freqbought4','cat_aggr','true');
INSERT INTO SS_VARIANT_PARAMS(ID,KEY,VALUE)
VALUES('dyf-freqbought4','top_n','50');
INSERT INTO SS_VARIANT_PARAMS(ID,KEY,VALUE)
VALUES('dyf-freqbought4','top_perc','20');
INSERT INTO SS_VARIANT_PARAMS(ID,KEY,VALUE)
VALUES('dyf-freqbought4','prez_title','YOUR FAVORITES');
INSERT INTO SS_VARIANT_PARAMS(ID,KEY,VALUE)
VALUES('dyf-freqbought4','prez_desc','These are some of the items you''ve purchased most often.');

-- cohort->variant assignment
INSERT INTO SS_VARIANT_ASSIGNMENT ( COHORT_ID, "DATE", VARIANT_ID ) VALUES ('C3',  sysdate, 'dyf-freqbought1'); 
INSERT INTO SS_VARIANT_ASSIGNMENT ( COHORT_ID, "DATE", VARIANT_ID ) VALUES ('C1',  sysdate, 'dyf-freqbought1'); 
INSERT INTO SS_VARIANT_ASSIGNMENT ( COHORT_ID, "DATE", VARIANT_ID ) VALUES ('C18',  sysdate, 'dyf-freqbought1'); 
INSERT INTO SS_VARIANT_ASSIGNMENT ( COHORT_ID, "DATE", VARIANT_ID ) VALUES ('C20',  sysdate, 'dyf-freqbought1'); 
INSERT INTO SS_VARIANT_ASSIGNMENT ( COHORT_ID, "DATE", VARIANT_ID ) VALUES ('C19',  sysdate, 'dyf-freqbought1');

INSERT INTO SS_VARIANT_ASSIGNMENT ( COHORT_ID, "DATE", VARIANT_ID ) VALUES ('C9',  sysdate, 'dyf-freqbought2');
INSERT INTO SS_VARIANT_ASSIGNMENT ( COHORT_ID, "DATE", VARIANT_ID ) VALUES ('C16',  sysdate, 'dyf-freqbought2');
INSERT INTO SS_VARIANT_ASSIGNMENT ( COHORT_ID, "DATE", VARIANT_ID ) VALUES ('C5',  sysdate, 'dyf-freqbought2');
INSERT INTO SS_VARIANT_ASSIGNMENT ( COHORT_ID, "DATE", VARIANT_ID ) VALUES ('C17',  sysdate, 'dyf-freqbought2');
INSERT INTO SS_VARIANT_ASSIGNMENT ( COHORT_ID, "DATE", VARIANT_ID ) VALUES ('C6',  sysdate, 'dyf-freqbought2');

INSERT INTO SS_VARIANT_ASSIGNMENT ( COHORT_ID, "DATE", VARIANT_ID ) VALUES ('C4',  sysdate, 'dyf-freqbought3');
INSERT INTO SS_VARIANT_ASSIGNMENT ( COHORT_ID, "DATE", VARIANT_ID ) VALUES ('C7',  sysdate, 'dyf-freqbought3');
INSERT INTO SS_VARIANT_ASSIGNMENT ( COHORT_ID, "DATE", VARIANT_ID ) VALUES ('C13',  sysdate, 'dyf-freqbought3');
INSERT INTO SS_VARIANT_ASSIGNMENT ( COHORT_ID, "DATE", VARIANT_ID ) VALUES ('C15',  sysdate, 'dyf-freqbought3');
INSERT INTO SS_VARIANT_ASSIGNMENT ( COHORT_ID, "DATE", VARIANT_ID ) VALUES ('C11',  sysdate, 'dyf-freqbought3');

INSERT INTO SS_VARIANT_ASSIGNMENT ( COHORT_ID, "DATE", VARIANT_ID ) VALUES ('C8',  sysdate, 'dyf-freqbought4');
INSERT INTO SS_VARIANT_ASSIGNMENT ( COHORT_ID, "DATE", VARIANT_ID ) VALUES ('C2',  sysdate, 'dyf-freqbought4');
INSERT INTO SS_VARIANT_ASSIGNMENT ( COHORT_ID, "DATE", VARIANT_ID ) VALUES ('C10',  sysdate, 'dyf-freqbought4');
INSERT INTO SS_VARIANT_ASSIGNMENT ( COHORT_ID, "DATE", VARIANT_ID ) VALUES ('C12',  sysdate, 'dyf-freqbought4');
INSERT INTO SS_VARIANT_ASSIGNMENT ( COHORT_ID, "DATE", VARIANT_ID ) VALUES ('C14',  sysdate, 'dyf-freqbought4');




--
-- FD Favorites
--

INSERT INTO ss_variants(ID, feature, type)
VALUES('favorites-1','FAVORITES', 'favorites');
INSERT INTO ss_variants(ID, feature, type)
VALUES('favorites-2','FAVORITES', 'favorites');
INSERT INTO ss_variants(ID, feature, type)
VALUES('favorites-3','FAVORITES', 'favorites');


-- Configurations
INSERT INTO SS_VARIANT_PARAMS(ID,KEY,VALUE)
VALUES('favorites-1','favorite_list_id','fd_favs_lm');
INSERT INTO SS_VARIANT_PARAMS(ID,KEY,VALUE)
VALUES('favorites-1','prez_title','FRESHDIRECT FAVORITES');
INSERT INTO SS_VARIANT_PARAMS(ID,KEY,VALUE)
VALUES('favorites-1','prez_desc','These are just a few of our most-loved products.');
INSERT INTO SS_VARIANT_PARAMS(ID,KEY,VALUE)
VALUES('favorites-1','sampling_strat','power');
INSERT INTO SS_VARIANT_PARAMS(ID,KEY,VALUE)
VALUES('favorites-1','exponent','0.7');
INSERT INTO SS_VARIANT_PARAMS(ID,KEY,VALUE)
VALUES('favorites-1','top_n','1');
INSERT INTO SS_VARIANT_PARAMS(ID,KEY,VALUE)
VALUES('favorites-1','top_perc','100');

INSERT INTO SS_VARIANT_PARAMS(ID,KEY,VALUE)
VALUES('favorites-2','favorite_list_id','fd_favs_ak');
INSERT INTO SS_VARIANT_PARAMS(ID,KEY,VALUE)
VALUES('favorites-2','prez_title','FRESHDIRECT FAVORITES');
INSERT INTO SS_VARIANT_PARAMS(ID,KEY,VALUE)
VALUES('favorites-2','prez_desc','These are just a few of our most-loved products.');
INSERT INTO SS_VARIANT_PARAMS(ID,KEY,VALUE)
VALUES('favorites-2','sampling_strat','power');
INSERT INTO SS_VARIANT_PARAMS(ID,KEY,VALUE)
VALUES('favorites-2','exponent','0.7');
INSERT INTO SS_VARIANT_PARAMS(ID,KEY,VALUE)
VALUES('favorites-2','top_n','1');
INSERT INTO SS_VARIANT_PARAMS(ID,KEY,VALUE)
VALUES('favorites-2','top_perc','100');

INSERT INTO SS_VARIANT_PARAMS(ID,KEY,VALUE)
VALUES('favorites-3','favorite_list_id','fd_favs_nb');
INSERT INTO SS_VARIANT_PARAMS(ID,KEY,VALUE)
VALUES('favorites-3','prez_title','FRESHDIRECT FAVORITES');
INSERT INTO SS_VARIANT_PARAMS(ID,KEY,VALUE)
VALUES('favorites-3','prez_desc','These are just a few of our most-loved products.');
INSERT INTO SS_VARIANT_PARAMS(ID,KEY,VALUE)
VALUES('favorites-3','sampling_strat','power');
INSERT INTO SS_VARIANT_PARAMS(ID,KEY,VALUE)
VALUES('favorites-3','exponent','0.7');
INSERT INTO SS_VARIANT_PARAMS(ID,KEY,VALUE)
VALUES('favorites-3','top_n','1');
INSERT INTO SS_VARIANT_PARAMS(ID,KEY,VALUE)
VALUES('favorites-3','top_perc','100');


-- cohort->variant assignment
INSERT INTO SS_VARIANT_ASSIGNMENT ( COHORT_ID, "DATE", VARIANT_ID ) VALUES ('C4',  sysdate, 'favorites-1');
INSERT INTO SS_VARIANT_ASSIGNMENT ( COHORT_ID, "DATE", VARIANT_ID ) VALUES ('C12',  sysdate, 'favorites-1');
INSERT INTO SS_VARIANT_ASSIGNMENT ( COHORT_ID, "DATE", VARIANT_ID ) VALUES ('C8',  sysdate, 'favorites-1');
INSERT INTO SS_VARIANT_ASSIGNMENT ( COHORT_ID, "DATE", VARIANT_ID ) VALUES ('C19',  sysdate, 'favorites-1');
INSERT INTO SS_VARIANT_ASSIGNMENT ( COHORT_ID, "DATE", VARIANT_ID ) VALUES ('C9',  sysdate, 'favorites-1');
INSERT INTO SS_VARIANT_ASSIGNMENT ( COHORT_ID, "DATE", VARIANT_ID ) VALUES ('C5',  sysdate, 'favorites-1');

INSERT INTO SS_VARIANT_ASSIGNMENT ( COHORT_ID, "DATE", VARIANT_ID ) VALUES ('C1',  sysdate, 'favorites-2');
INSERT INTO SS_VARIANT_ASSIGNMENT ( COHORT_ID, "DATE", VARIANT_ID ) VALUES ('C10',  sysdate, 'favorites-2');
INSERT INTO SS_VARIANT_ASSIGNMENT ( COHORT_ID, "DATE", VARIANT_ID ) VALUES ('C13',  sysdate, 'favorites-2');
INSERT INTO SS_VARIANT_ASSIGNMENT ( COHORT_ID, "DATE", VARIANT_ID ) VALUES ('C11',  sysdate, 'favorites-2');
INSERT INTO SS_VARIANT_ASSIGNMENT ( COHORT_ID, "DATE", VARIANT_ID ) VALUES ('C14',  sysdate, 'favorites-2');
INSERT INTO SS_VARIANT_ASSIGNMENT ( COHORT_ID, "DATE", VARIANT_ID ) VALUES ('C17',  sysdate, 'favorites-2');
INSERT INTO SS_VARIANT_ASSIGNMENT ( COHORT_ID, "DATE", VARIANT_ID ) VALUES ('C2',  sysdate, 'favorites-2');

INSERT INTO SS_VARIANT_ASSIGNMENT ( COHORT_ID, "DATE", VARIANT_ID ) VALUES ('C3',  sysdate, 'favorites-3');
INSERT INTO SS_VARIANT_ASSIGNMENT ( COHORT_ID, "DATE", VARIANT_ID ) VALUES ('C6',  sysdate, 'favorites-3');
INSERT INTO SS_VARIANT_ASSIGNMENT ( COHORT_ID, "DATE", VARIANT_ID ) VALUES ('C7',  sysdate, 'favorites-3');
INSERT INTO SS_VARIANT_ASSIGNMENT ( COHORT_ID, "DATE", VARIANT_ID ) VALUES ('C20',  sysdate, 'favorites-3');
INSERT INTO SS_VARIANT_ASSIGNMENT ( COHORT_ID, "DATE", VARIANT_ID ) VALUES ('C15',  sysdate, 'favorites-3');



--
-- Featured Items
--

INSERT INTO ss_variants(ID, feature, type)
VALUES('featured-cms','FEATURED_ITEMS', 'featured_items');
INSERT INTO ss_variants(ID, feature, type)
VALUES('featured-popular','FEATURED_ITEMS', 'manual_override');


-- Configurations
INSERT INTO SS_VARIANT_PARAMS(ID,KEY,VALUE)
VALUES('featured-cms','sampling_strat','deterministic');

INSERT INTO SS_VARIANT_PARAMS(ID,KEY,VALUE)
VALUES('featured-popular','prez_title','NEW! Top Sellers');
INSERT INTO SS_VARIANT_PARAMS(ID,KEY,VALUE)
VALUES('featured-popular','sampling_strat','power');
INSERT INTO SS_VARIANT_PARAMS(ID,KEY,VALUE)
VALUES('featured-popular','exponent','0.5');
INSERT INTO SS_VARIANT_PARAMS(ID,KEY,VALUE)
VALUES('featured-popular','top_n','10');
INSERT INTO SS_VARIANT_PARAMS(ID,KEY,VALUE)
VALUES('featured-popular','top_perc','2');

-- cohort->variant assignment
INSERT INTO SS_VARIANT_ASSIGNMENT ( COHORT_ID, "DATE", VARIANT_ID ) VALUES ('C1',  sysdate, 'featured-popular');
INSERT INTO SS_VARIANT_ASSIGNMENT ( COHORT_ID, "DATE", VARIANT_ID ) VALUES ('C2',  sysdate, 'featured-popular');
INSERT INTO SS_VARIANT_ASSIGNMENT ( COHORT_ID, "DATE", VARIANT_ID ) VALUES ('C3',  sysdate, 'featured-cms');
INSERT INTO SS_VARIANT_ASSIGNMENT ( COHORT_ID, "DATE", VARIANT_ID ) VALUES ('C4',  sysdate, 'featured-popular');
INSERT INTO SS_VARIANT_ASSIGNMENT ( COHORT_ID, "DATE", VARIANT_ID ) VALUES ('C5',  sysdate, 'featured-popular');
INSERT INTO SS_VARIANT_ASSIGNMENT ( COHORT_ID, "DATE", VARIANT_ID ) VALUES ('C6',  sysdate, 'featured-popular');
INSERT INTO SS_VARIANT_ASSIGNMENT ( COHORT_ID, "DATE", VARIANT_ID ) VALUES ('C7',  sysdate, 'featured-popular');
INSERT INTO SS_VARIANT_ASSIGNMENT ( COHORT_ID, "DATE", VARIANT_ID ) VALUES ('C8',  sysdate, 'featured-cms');
INSERT INTO SS_VARIANT_ASSIGNMENT ( COHORT_ID, "DATE", VARIANT_ID ) VALUES ('C9',  sysdate, 'featured-popular');
INSERT INTO SS_VARIANT_ASSIGNMENT ( COHORT_ID, "DATE", VARIANT_ID ) VALUES ('C10',  sysdate, 'featured-cms');
INSERT INTO SS_VARIANT_ASSIGNMENT ( COHORT_ID, "DATE", VARIANT_ID ) VALUES ('C11',  sysdate, 'featured-popular');
INSERT INTO SS_VARIANT_ASSIGNMENT ( COHORT_ID, "DATE", VARIANT_ID ) VALUES ('C12',  sysdate, 'featured-cms');
INSERT INTO SS_VARIANT_ASSIGNMENT ( COHORT_ID, "DATE", VARIANT_ID ) VALUES ('C13',  sysdate, 'featured-popular');
INSERT INTO SS_VARIANT_ASSIGNMENT ( COHORT_ID, "DATE", VARIANT_ID ) VALUES ('C14',  sysdate, 'featured-cms');
INSERT INTO SS_VARIANT_ASSIGNMENT ( COHORT_ID, "DATE", VARIANT_ID ) VALUES ('C15',  sysdate, 'featured-popular');
INSERT INTO SS_VARIANT_ASSIGNMENT ( COHORT_ID, "DATE", VARIANT_ID ) VALUES ('C16',  sysdate, 'featured-popular');
INSERT INTO SS_VARIANT_ASSIGNMENT ( COHORT_ID, "DATE", VARIANT_ID ) VALUES ('C17',  sysdate, 'featured-cms');
INSERT INTO SS_VARIANT_ASSIGNMENT ( COHORT_ID, "DATE", VARIANT_ID ) VALUES ('C18',  sysdate, 'featured-popular');
INSERT INTO SS_VARIANT_ASSIGNMENT ( COHORT_ID, "DATE", VARIANT_ID ) VALUES ('C19',  sysdate, 'featured-cms');
INSERT INTO SS_VARIANT_ASSIGNMENT ( COHORT_ID, "DATE", VARIANT_ID ) VALUES ('C20',  sysdate, 'featured-cms');




--
-- Control variant for FD Favorites
--
INSERT INTO ss_variants(ID, feature, type) VALUES('favorites-control','FAVORITES', 'nil');

INSERT INTO ss_variant_assignment(COHORT_ID, "DATE", variant_id) VALUES('C16', CURRENT_DATE, 'favorites-control');
INSERT INTO ss_variant_assignment(COHORT_ID, "DATE", variant_id) VALUES('C18', CURRENT_DATE, 'favorites-control');

