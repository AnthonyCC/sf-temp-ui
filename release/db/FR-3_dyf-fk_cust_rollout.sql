-- Rollout

-- DYF recommender site feature on FoodKick
Insert into CUST.SS_SITE_FEATURE (ID,TITLE,SMART_SAVING,E_STORE) values ('DYF_FK','Did You Forget on FoodKick',0,'FDX');

-- Basic Purchase History based recommender variant for DYF_FK site feature
Insert into CUST.SS_VARIANTS (ID,CONFIG_ID,FEATURE,TYPE,ALIAS_ID,ARCHIVED) values ('fk_cart_recommender',null,'DYF_FK','scripted',null,'N');

-- Variant configuration
Insert into CUST.SS_VARIANT_PARAMS (ID,KEY,VALUE) values ('fk_cart_recommender','generator','RelatedItems_scarabCart(cartContents)');
Insert into CUST.SS_VARIANT_PARAMS (ID,KEY,VALUE) values ('fk_cart_recommender','sampling_strat','power');
Insert into CUST.SS_VARIANT_PARAMS (ID,KEY,VALUE) values ('fk_cart_recommender','exponent','0.4');
-- Insert into CUST.SS_VARIANT_PARAMS (ID,KEY,VALUE) values ('fk_cart_recommender','top_n','20');
-- Insert into CUST.SS_VARIANT_PARAMS (ID,KEY,VALUE) values ('fk_cart_recommender','top_perc','20');
-- Insert into CUST.SS_VARIANT_PARAMS (ID,KEY,VALUE) values ('fk_cart_recommender','brand_uniq_sort','false');
-- Insert into CUST.SS_VARIANT_PARAMS (ID,KEY,VALUE) values ('fk_cart_recommender','cat_aggr','false');
-- Insert into CUST.SS_VARIANT_PARAMS (ID,KEY,VALUE) values ('fk_cart_recommender','include_cart_items','false');
-- Insert into CUST.SS_VARIANT_PARAMS (ID,KEY,VALUE) values ('fk_cart_recommender','use_alternatives','true');

-- Variant distribution
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (cohort_id, variant_id, "DATE") values ('C1', 'fk_cart_recommender', sysdate);
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (cohort_id, variant_id, "DATE") values ('C2', 'fk_cart_recommender', sysdate);
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (cohort_id, variant_id, "DATE") values ('C3', 'fk_cart_recommender', sysdate);
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (cohort_id, variant_id, "DATE") values ('C4', 'fk_cart_recommender', sysdate);
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (cohort_id, variant_id, "DATE") values ('C5', 'fk_cart_recommender', sysdate);
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (cohort_id, variant_id, "DATE") values ('C6', 'fk_cart_recommender', sysdate);
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (cohort_id, variant_id, "DATE") values ('C7', 'fk_cart_recommender', sysdate);
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (cohort_id, variant_id, "DATE") values ('C8', 'fk_cart_recommender', sysdate);
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (cohort_id, variant_id, "DATE") values ('C9', 'fk_cart_recommender', sysdate);
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (cohort_id, variant_id, "DATE") values ('C10', 'fk_cart_recommender', sysdate);
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (cohort_id, variant_id, "DATE") values ('C11', 'fk_cart_recommender', sysdate);
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (cohort_id, variant_id, "DATE") values ('C12', 'fk_cart_recommender', sysdate);
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (cohort_id, variant_id, "DATE") values ('C13', 'fk_cart_recommender', sysdate);
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (cohort_id, variant_id, "DATE") values ('C14', 'fk_cart_recommender', sysdate);
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (cohort_id, variant_id, "DATE") values ('C15', 'fk_cart_recommender', sysdate);
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (cohort_id, variant_id, "DATE") values ('C16', 'fk_cart_recommender', sysdate);
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (cohort_id, variant_id, "DATE") values ('C17', 'fk_cart_recommender', sysdate);
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (cohort_id, variant_id, "DATE") values ('C18', 'fk_cart_recommender', sysdate);
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (cohort_id, variant_id, "DATE") values ('C19', 'fk_cart_recommender', sysdate);
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (cohort_id, variant_id, "DATE") values ('C20', 'fk_cart_recommender', sysdate);

