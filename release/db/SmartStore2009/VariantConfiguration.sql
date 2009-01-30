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
INSERT INTO ss_variant_assignment(COHORT_ID, "DATE", variant_id)
VALUES('C1', CURRENT_DATE, 'dyf-freqbought1');
INSERT INTO ss_variant_assignment(COHORT_ID, "DATE", variant_id)
VALUES('C2', CURRENT_DATE, 'dyf-freqbought1');
INSERT INTO ss_variant_assignment(COHORT_ID, "DATE", variant_id)
VALUES('C3', CURRENT_DATE, 'dyf-freqbought1');
INSERT INTO ss_variant_assignment(COHORT_ID, "DATE", variant_id)
VALUES('C4', CURRENT_DATE, 'dyf-freqbought1');
INSERT INTO ss_variant_assignment(COHORT_ID, "DATE", variant_id)
VALUES('C5', CURRENT_DATE, 'dyf-freqbought1');

INSERT INTO ss_variant_assignment(COHORT_ID, "DATE", variant_id)
VALUES('C6', CURRENT_DATE, 'dyf-freqbought2');
INSERT INTO ss_variant_assignment(COHORT_ID, "DATE", variant_id)
VALUES('C7', CURRENT_DATE, 'dyf-freqbought2');
INSERT INTO ss_variant_assignment(COHORT_ID, "DATE", variant_id)
VALUES('C8', CURRENT_DATE, 'dyf-freqbought2');
INSERT INTO ss_variant_assignment(COHORT_ID, "DATE", variant_id)
VALUES('C9', CURRENT_DATE, 'dyf-freqbought2');
INSERT INTO ss_variant_assignment(COHORT_ID, "DATE", variant_id)
VALUES('C10', CURRENT_DATE, 'dyf-freqbought2');

INSERT INTO ss_variant_assignment(COHORT_ID, "DATE", variant_id)
VALUES('C11', CURRENT_DATE, 'dyf-freqbought3');
INSERT INTO ss_variant_assignment(COHORT_ID, "DATE", variant_id)
VALUES('C12', CURRENT_DATE, 'dyf-freqbought3');
INSERT INTO ss_variant_assignment(COHORT_ID, "DATE", variant_id)
VALUES('C13', CURRENT_DATE, 'dyf-freqbought3');
INSERT INTO ss_variant_assignment(COHORT_ID, "DATE", variant_id)
VALUES('C14', CURRENT_DATE, 'dyf-freqbought3');
INSERT INTO ss_variant_assignment(COHORT_ID, "DATE", variant_id)
VALUES('C15', CURRENT_DATE, 'dyf-freqbought3');

INSERT INTO ss_variant_assignment(COHORT_ID, "DATE", variant_id)
VALUES('C16', CURRENT_DATE, 'dyf-freqbought4');
INSERT INTO ss_variant_assignment(COHORT_ID, "DATE", variant_id)
VALUES('C17', CURRENT_DATE, 'dyf-freqbought4');
INSERT INTO ss_variant_assignment(COHORT_ID, "DATE", variant_id)
VALUES('C18', CURRENT_DATE, 'dyf-freqbought4');
INSERT INTO ss_variant_assignment(COHORT_ID, "DATE", variant_id)
VALUES('C19', CURRENT_DATE, 'dyf-freqbought4');
INSERT INTO ss_variant_assignment(COHORT_ID, "DATE", variant_id)
VALUES('C20', CURRENT_DATE, 'dyf-freqbought4');





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

--   configs
INSERT INTO SS_VARIANT_PARAMS(ID,KEY,VALUE)
VALUES('favorites-1','favorite_list_id','fd_favorites_1');
INSERT INTO SS_VARIANT_PARAMS(ID,KEY,VALUE)
VALUES('favorites-1','prez_title','FRESHDIRECT FAVORITES');
INSERT INTO SS_VARIANT_PARAMS(ID,KEY,VALUE)
VALUES('favorites-1','prez_desc','These are just a few of our most-loved products.');

INSERT INTO SS_VARIANT_PARAMS(ID,KEY,VALUE)
VALUES('favorites-2','favorite_list_id','fd_favorites_2');
INSERT INTO SS_VARIANT_PARAMS(ID,KEY,VALUE)
VALUES('favorites-2','prez_title','FRESHDIRECT FAVORITES');
INSERT INTO SS_VARIANT_PARAMS(ID,KEY,VALUE)
VALUES('favorites-2','prez_desc','These are just a few of our most-loved products.');

INSERT INTO SS_VARIANT_PARAMS(ID,KEY,VALUE)
VALUES('favorites-3','favorite_list_id','fd_favorites_3');
INSERT INTO SS_VARIANT_PARAMS(ID,KEY,VALUE)
VALUES('favorites-3','prez_title','FRESHDIRECT FAVORITES');
INSERT INTO SS_VARIANT_PARAMS(ID,KEY,VALUE)
VALUES('favorites-3','prez_desc','These are just a few of our most-loved products.');


-- cohort->variant assignment
INSERT INTO ss_variant_assignment(COHORT_ID, "DATE", variant_id)
VALUES('C1', CURRENT_DATE, 'favorites-1');
INSERT INTO ss_variant_assignment(COHORT_ID, "DATE", variant_id)
VALUES('C2', CURRENT_DATE, 'favorites-1');
INSERT INTO ss_variant_assignment(COHORT_ID, "DATE", variant_id)
VALUES('C3', CURRENT_DATE, 'favorites-1');
INSERT INTO ss_variant_assignment(COHORT_ID, "DATE", variant_id)
VALUES('C4', CURRENT_DATE, 'favorites-1');
INSERT INTO ss_variant_assignment(COHORT_ID, "DATE", variant_id)
VALUES('C5', CURRENT_DATE, 'favorites-1');
INSERT INTO ss_variant_assignment(COHORT_ID, "DATE", variant_id)
VALUES('C6', CURRENT_DATE, 'favorites-1');
INSERT INTO ss_variant_assignment(COHORT_ID, "DATE", variant_id)
VALUES('C7', CURRENT_DATE, 'favorites-1');

INSERT INTO ss_variant_assignment(COHORT_ID, "DATE", variant_id)
VALUES('C8', CURRENT_DATE, 'favorites-2');
INSERT INTO ss_variant_assignment(COHORT_ID, "DATE", variant_id)
VALUES('C9', CURRENT_DATE, 'favorites-2');
INSERT INTO ss_variant_assignment(COHORT_ID, "DATE", variant_id)
VALUES('C10', CURRENT_DATE, 'favorites-2');
INSERT INTO ss_variant_assignment(COHORT_ID, "DATE", variant_id)
VALUES('C11', CURRENT_DATE, 'favorites-2');
INSERT INTO ss_variant_assignment(COHORT_ID, "DATE", variant_id)
VALUES('C12', CURRENT_DATE, 'favorites-2');
INSERT INTO ss_variant_assignment(COHORT_ID, "DATE", variant_id)
VALUES('C13', CURRENT_DATE, 'favorites-2');
INSERT INTO ss_variant_assignment(COHORT_ID, "DATE", variant_id)
VALUES('C14', CURRENT_DATE, 'favorites-2');

INSERT INTO ss_variant_assignment(COHORT_ID, "DATE", variant_id)
VALUES('C15', CURRENT_DATE, 'favorites-3');
INSERT INTO ss_variant_assignment(COHORT_ID, "DATE", variant_id)
VALUES('C16', CURRENT_DATE, 'favorites-3');
INSERT INTO ss_variant_assignment(COHORT_ID, "DATE", variant_id)
VALUES('C17', CURRENT_DATE, 'favorites-3');
INSERT INTO ss_variant_assignment(COHORT_ID, "DATE", variant_id)
VALUES('C18', CURRENT_DATE, 'favorites-3');
INSERT INTO ss_variant_assignment(COHORT_ID, "DATE", variant_id)
VALUES('C19', CURRENT_DATE, 'favorites-3');
INSERT INTO ss_variant_assignment(COHORT_ID, "DATE", variant_id)
VALUES('C20', CURRENT_DATE, 'favorites-3');


--
-- Featured Items
--

INSERT INTO ss_variants(ID, feature, type)
VALUES('featured-cms','FEATURED_ITEMS', 'featured_items');
INSERT INTO ss_variants(ID, feature, type)
VALUES('featured-popular','FEATURED_ITEMS', 'manual_override');

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
VALUES('featured-popular','top_perc','10');

-- cohort->variant assignment
INSERT INTO ss_variant_assignment(COHORT_ID, "DATE", variant_id)
VALUES('C1', CURRENT_DATE, 'featured-cms');
INSERT INTO ss_variant_assignment(COHORT_ID, "DATE", variant_id)
VALUES('C2', CURRENT_DATE, 'featured-cms');
INSERT INTO ss_variant_assignment(COHORT_ID, "DATE", variant_id)
VALUES('C3', CURRENT_DATE, 'featured-cms');
INSERT INTO ss_variant_assignment(COHORT_ID, "DATE", variant_id)
VALUES('C4', CURRENT_DATE, 'featured-cms');
INSERT INTO ss_variant_assignment(COHORT_ID, "DATE", variant_id)
VALUES('C5', CURRENT_DATE, 'featured-cms');
INSERT INTO ss_variant_assignment(COHORT_ID, "DATE", variant_id)
VALUES('C6', CURRENT_DATE, 'featured-cms');
INSERT INTO ss_variant_assignment(COHORT_ID, "DATE", variant_id)
VALUES('C7', CURRENT_DATE, 'featured-cms');
INSERT INTO ss_variant_assignment(COHORT_ID, "DATE", variant_id)
VALUES('C8', CURRENT_DATE, 'featured-cms');
INSERT INTO ss_variant_assignment(COHORT_ID, "DATE", variant_id)
VALUES('C9', CURRENT_DATE, 'featured-cms');
INSERT INTO ss_variant_assignment(COHORT_ID, "DATE", variant_id)
VALUES('C10', CURRENT_DATE, 'featured-cms');

INSERT INTO ss_variant_assignment(COHORT_ID, "DATE", variant_id)
VALUES('C11', CURRENT_DATE, 'featured-popular');
INSERT INTO ss_variant_assignment(COHORT_ID, "DATE", variant_id)
VALUES('C12', CURRENT_DATE, 'featured-popular');
INSERT INTO ss_variant_assignment(COHORT_ID, "DATE", variant_id)
VALUES('C13', CURRENT_DATE, 'featured-popular');
INSERT INTO ss_variant_assignment(COHORT_ID, "DATE", variant_id)
VALUES('C14', CURRENT_DATE, 'featured-popular');
INSERT INTO ss_variant_assignment(COHORT_ID, "DATE", variant_id)
VALUES('C15', CURRENT_DATE, 'featured-popular');
INSERT INTO ss_variant_assignment(COHORT_ID, "DATE", variant_id)
VALUES('C16', CURRENT_DATE, 'featured-popular');
INSERT INTO ss_variant_assignment(COHORT_ID, "DATE", variant_id)
VALUES('C17', CURRENT_DATE, 'featured-popular');
INSERT INTO ss_variant_assignment(COHORT_ID, "DATE", variant_id)
VALUES('C18', CURRENT_DATE, 'featured-popular');
INSERT INTO ss_variant_assignment(COHORT_ID, "DATE", variant_id)
VALUES('C19', CURRENT_DATE, 'featured-popular');
INSERT INTO ss_variant_assignment(COHORT_ID, "DATE", variant_id)
VALUES('C20', CURRENT_DATE, 'featured-popular');
