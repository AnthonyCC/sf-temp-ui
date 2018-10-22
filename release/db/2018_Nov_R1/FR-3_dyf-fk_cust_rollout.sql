-- Rollout

-- FK cart recommender site features
Insert into CUST.SS_SITE_FEATURE (ID,TITLE,SMART_SAVING,E_STORE) values ('FK_DYF','Did You Forget on FoodKick',0,'FDX');
Insert into CUST.SS_SITE_FEATURE (ID,TITLE,SMART_SAVING,E_STORE) values ('FK_CART_TOPRATED','Top Rated on FoodKick',0,'FDX');
Insert into CUST.SS_SITE_FEATURE (ID,TITLE,SMART_SAVING,E_STORE) values ('FK_CART_MOSTPOP','Most Popular on FoodKick',0,'FDX');
Insert into CUST.SS_SITE_FEATURE (ID,TITLE,SMART_SAVING,E_STORE) values ('FK_CART_DEALS','Last Chance Deals on FoodKick',0,'FDX');

-- Variant definitions
Insert into CUST.SS_VARIANTS (ID,CONFIG_ID,FEATURE,TYPE,ALIAS_ID,ARCHIVED) values ('fk_scarab_cart',null,'FK_DYF','scripted',null,'N');
Insert into CUST.SS_VARIANTS (ID,CONFIG_ID,FEATURE,TYPE,ALIAS_ID,ARCHIVED) values ('fk_cart_toprated',null,'FK_CART_TOPRATED','scripted',null,'N');
Insert into CUST.SS_VARIANTS (ID,CONFIG_ID,FEATURE,TYPE,ALIAS_ID,ARCHIVED) values ('fk_cart_most_pop',null,'FK_CART_MOSTPOP','scripted',null,'N');
Insert into CUST.SS_VARIANTS (ID,CONFIG_ID,FEATURE,TYPE,ALIAS_ID,ARCHIVED) values ('fk_cart_deals',null,'FK_CART_DEALS','scripted',null,'N');

-- Variant configurations
Insert into CUST.SS_VARIANT_PARAMS (ID,KEY,VALUE) values ('fk_scarab_cart','generator','RelatedItems_scarabCart(cartContents)');
Insert into CUST.SS_VARIANT_PARAMS (ID,KEY,VALUE) values ('fk_scarab_cart','sampling_strat','power');
Insert into CUST.SS_VARIANT_PARAMS (ID,KEY,VALUE) values ('fk_scarab_cart','exponent','0.4');

Insert into CUST.SS_VARIANT_PARAMS (ID,KEY,VALUE) values ('fk_cart_toprated','exponent','0.4');
Insert into CUST.SS_VARIANT_PARAMS (ID,KEY,VALUE) values ('fk_cart_toprated','generator','RecursiveNodes("fvg","msa"):atLeast(QualityRating_Discretized2,4)');
Insert into CUST.SS_VARIANT_PARAMS (ID,KEY,VALUE) values ('fk_cart_toprated','include_cart_items','false');
Insert into CUST.SS_VARIANT_PARAMS (ID,KEY,VALUE) values ('fk_cart_toprated','sampling_strat','power');
Insert into CUST.SS_VARIANT_PARAMS (ID,KEY,VALUE) values ('fk_cart_toprated','scoring','QualityRating; Popularity_Discretized');
Insert into CUST.SS_VARIANT_PARAMS (ID,KEY,VALUE) values ('fk_cart_toprated','top_n','100');
Insert into CUST.SS_VARIANT_PARAMS (ID,KEY,VALUE) values ('fk_cart_toprated','top_perc','2.0');
Insert into CUST.SS_VARIANT_PARAMS (ID,KEY,VALUE) values ('fk_cart_toprated','use_alternatives','true');
Insert into CUST.SS_VARIANT_PARAMS (ID,KEY,VALUE) values ('fk_cart_toprated','prez_title','Top Rated');

Insert into CUST.SS_VARIANT_PARAMS (ID,KEY,VALUE) values ('fk_cart_most_pop','exponent','0.5');
Insert into CUST.SS_VARIANT_PARAMS (ID,KEY,VALUE) values ('fk_cart_most_pop','generator','RecursiveNodes("FDX")');
Insert into CUST.SS_VARIANT_PARAMS (ID,KEY,VALUE) values ('fk_cart_most_pop','sampling_strat','power');
Insert into CUST.SS_VARIANT_PARAMS (ID,KEY,VALUE) values ('fk_cart_most_pop','scoring','Popularity8W_Discretized;QualityRating_Discretized2');
Insert into CUST.SS_VARIANT_PARAMS (ID,KEY,VALUE) values ('fk_cart_most_pop','top_n','25');
Insert into CUST.SS_VARIANT_PARAMS (ID,KEY,VALUE) values ('fk_cart_most_pop','top_perc','1');
Insert into CUST.SS_VARIANT_PARAMS (ID,KEY,VALUE) values ('fk_cart_most_pop','prez_title','Most Popular');

Insert into CUST.SS_VARIANT_PARAMS (ID,KEY,VALUE) values ('fk_cart_deals','exponent','0.4');
Insert into CUST.SS_VARIANT_PARAMS (ID,KEY,VALUE) values ('fk_cart_deals','generator','RecursiveNodes("FDX"):between(DealsPercentage,0.4,0.75)');
Insert into CUST.SS_VARIANT_PARAMS (ID,KEY,VALUE) values ('fk_cart_deals','sampling_strat','power');
Insert into CUST.SS_VARIANT_PARAMS (ID,KEY,VALUE) values ('fk_cart_deals','scoring','Recency_Discretized:top; DealsPercentage_Discretized; Popularity_Discretized');
Insert into CUST.SS_VARIANT_PARAMS (ID,KEY,VALUE) values ('fk_cart_deals','top_n','100');
Insert into CUST.SS_VARIANT_PARAMS (ID,KEY,VALUE) values ('fk_cart_deals','top_perc','2.0');
Insert into CUST.SS_VARIANT_PARAMS (ID,KEY,VALUE) values ('fk_cart_deals','prez_title','Last Chance Deals');

-- Variant distribution
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (cohort_id, variant_id, "DATE") values ('C1', 'fk_scarab_cart', sysdate);
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (cohort_id, variant_id, "DATE") values ('C2', 'fk_scarab_cart', sysdate);
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (cohort_id, variant_id, "DATE") values ('C3', 'fk_scarab_cart', sysdate);
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (cohort_id, variant_id, "DATE") values ('C4', 'fk_scarab_cart', sysdate);
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (cohort_id, variant_id, "DATE") values ('C5', 'fk_scarab_cart', sysdate);
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (cohort_id, variant_id, "DATE") values ('C6', 'fk_scarab_cart', sysdate);
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (cohort_id, variant_id, "DATE") values ('C7', 'fk_scarab_cart', sysdate);
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (cohort_id, variant_id, "DATE") values ('C8', 'fk_scarab_cart', sysdate);
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (cohort_id, variant_id, "DATE") values ('C9', 'fk_scarab_cart', sysdate);
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (cohort_id, variant_id, "DATE") values ('C10', 'fk_scarab_cart', sysdate);
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (cohort_id, variant_id, "DATE") values ('C11', 'fk_scarab_cart', sysdate);
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (cohort_id, variant_id, "DATE") values ('C12', 'fk_scarab_cart', sysdate);
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (cohort_id, variant_id, "DATE") values ('C13', 'fk_scarab_cart', sysdate);
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (cohort_id, variant_id, "DATE") values ('C14', 'fk_scarab_cart', sysdate);
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (cohort_id, variant_id, "DATE") values ('C15', 'fk_scarab_cart', sysdate);
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (cohort_id, variant_id, "DATE") values ('C16', 'fk_scarab_cart', sysdate);
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (cohort_id, variant_id, "DATE") values ('C17', 'fk_scarab_cart', sysdate);
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (cohort_id, variant_id, "DATE") values ('C18', 'fk_scarab_cart', sysdate);
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (cohort_id, variant_id, "DATE") values ('C19', 'fk_scarab_cart', sysdate);
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (cohort_id, variant_id, "DATE") values ('C20', 'fk_scarab_cart', sysdate);

INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (cohort_id, variant_id, "DATE") values ('C1', 'fk_cart_toprated', sysdate);
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (cohort_id, variant_id, "DATE") values ('C2', 'fk_cart_toprated', sysdate);
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (cohort_id, variant_id, "DATE") values ('C3', 'fk_cart_toprated', sysdate);
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (cohort_id, variant_id, "DATE") values ('C4', 'fk_cart_toprated', sysdate);
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (cohort_id, variant_id, "DATE") values ('C5', 'fk_cart_toprated', sysdate);
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (cohort_id, variant_id, "DATE") values ('C6', 'fk_cart_toprated', sysdate);
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (cohort_id, variant_id, "DATE") values ('C7', 'fk_cart_toprated', sysdate);
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (cohort_id, variant_id, "DATE") values ('C8', 'fk_cart_toprated', sysdate);
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (cohort_id, variant_id, "DATE") values ('C9', 'fk_cart_toprated', sysdate);
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (cohort_id, variant_id, "DATE") values ('C10', 'fk_cart_toprated', sysdate);
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (cohort_id, variant_id, "DATE") values ('C11', 'fk_cart_toprated', sysdate);
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (cohort_id, variant_id, "DATE") values ('C12', 'fk_cart_toprated', sysdate);
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (cohort_id, variant_id, "DATE") values ('C13', 'fk_cart_toprated', sysdate);
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (cohort_id, variant_id, "DATE") values ('C14', 'fk_cart_toprated', sysdate);
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (cohort_id, variant_id, "DATE") values ('C15', 'fk_cart_toprated', sysdate);
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (cohort_id, variant_id, "DATE") values ('C16', 'fk_cart_toprated', sysdate);
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (cohort_id, variant_id, "DATE") values ('C17', 'fk_cart_toprated', sysdate);
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (cohort_id, variant_id, "DATE") values ('C18', 'fk_cart_toprated', sysdate);
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (cohort_id, variant_id, "DATE") values ('C19', 'fk_cart_toprated', sysdate);
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (cohort_id, variant_id, "DATE") values ('C20', 'fk_cart_toprated', sysdate);

INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (cohort_id, variant_id, "DATE") values ('C1', 'fk_cart_most_pop', sysdate);
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (cohort_id, variant_id, "DATE") values ('C2', 'fk_cart_most_pop', sysdate);
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (cohort_id, variant_id, "DATE") values ('C3', 'fk_cart_most_pop', sysdate);
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (cohort_id, variant_id, "DATE") values ('C4', 'fk_cart_most_pop', sysdate);
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (cohort_id, variant_id, "DATE") values ('C5', 'fk_cart_most_pop', sysdate);
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (cohort_id, variant_id, "DATE") values ('C6', 'fk_cart_most_pop', sysdate);
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (cohort_id, variant_id, "DATE") values ('C7', 'fk_cart_most_pop', sysdate);
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (cohort_id, variant_id, "DATE") values ('C8', 'fk_cart_most_pop', sysdate);
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (cohort_id, variant_id, "DATE") values ('C9', 'fk_cart_most_pop', sysdate);
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (cohort_id, variant_id, "DATE") values ('C10', 'fk_cart_most_pop', sysdate);
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (cohort_id, variant_id, "DATE") values ('C11', 'fk_cart_most_pop', sysdate);
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (cohort_id, variant_id, "DATE") values ('C12', 'fk_cart_most_pop', sysdate);
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (cohort_id, variant_id, "DATE") values ('C13', 'fk_cart_most_pop', sysdate);
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (cohort_id, variant_id, "DATE") values ('C14', 'fk_cart_most_pop', sysdate);
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (cohort_id, variant_id, "DATE") values ('C15', 'fk_cart_most_pop', sysdate);
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (cohort_id, variant_id, "DATE") values ('C16', 'fk_cart_most_pop', sysdate);
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (cohort_id, variant_id, "DATE") values ('C17', 'fk_cart_most_pop', sysdate);
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (cohort_id, variant_id, "DATE") values ('C18', 'fk_cart_most_pop', sysdate);
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (cohort_id, variant_id, "DATE") values ('C19', 'fk_cart_most_pop', sysdate);
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (cohort_id, variant_id, "DATE") values ('C20', 'fk_cart_most_pop', sysdate);

INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (cohort_id, variant_id, "DATE") values ('C1', 'fk_cart_deals', sysdate);
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (cohort_id, variant_id, "DATE") values ('C2', 'fk_cart_deals', sysdate);
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (cohort_id, variant_id, "DATE") values ('C3', 'fk_cart_deals', sysdate);
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (cohort_id, variant_id, "DATE") values ('C4', 'fk_cart_deals', sysdate);
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (cohort_id, variant_id, "DATE") values ('C5', 'fk_cart_deals', sysdate);
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (cohort_id, variant_id, "DATE") values ('C6', 'fk_cart_deals', sysdate);
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (cohort_id, variant_id, "DATE") values ('C7', 'fk_cart_deals', sysdate);
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (cohort_id, variant_id, "DATE") values ('C8', 'fk_cart_deals', sysdate);
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (cohort_id, variant_id, "DATE") values ('C9', 'fk_cart_deals', sysdate);
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (cohort_id, variant_id, "DATE") values ('C10', 'fk_cart_deals', sysdate);
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (cohort_id, variant_id, "DATE") values ('C11', 'fk_cart_deals', sysdate);
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (cohort_id, variant_id, "DATE") values ('C12', 'fk_cart_deals', sysdate);
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (cohort_id, variant_id, "DATE") values ('C13', 'fk_cart_deals', sysdate);
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (cohort_id, variant_id, "DATE") values ('C14', 'fk_cart_deals', sysdate);
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (cohort_id, variant_id, "DATE") values ('C15', 'fk_cart_deals', sysdate);
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (cohort_id, variant_id, "DATE") values ('C16', 'fk_cart_deals', sysdate);
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (cohort_id, variant_id, "DATE") values ('C17', 'fk_cart_deals', sysdate);
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (cohort_id, variant_id, "DATE") values ('C18', 'fk_cart_deals', sysdate);
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (cohort_id, variant_id, "DATE") values ('C19', 'fk_cart_deals', sysdate);
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (cohort_id, variant_id, "DATE") values ('C20', 'fk_cart_deals', sysdate);
