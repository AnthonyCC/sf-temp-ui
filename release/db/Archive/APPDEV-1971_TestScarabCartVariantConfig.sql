--
-- Test Cart & Tabs strategy
-- APPDEV-1971
--
-- Apply script to CUST schema

--
-- Create 'tabs_var1f_scrb3' tab strategy
--
INSERT INTO cust.SS_VARIANTS (ID,CONFIG_ID,FEATURE,TYPE,ALIAS_ID,ARCHIVED)
VALUES ('tabs_var1f_scrb3',NULL,'CART_N_TABS','tab-strategy',NULL,'N');

-- Configure strategy priorities. Duplicate entries of tabs_var1f_scrb2
--  and replace 'C_SAVE_YF' with 'SCARAB_CART'
INSERT INTO CUST.SS_TAB_STRATEGY_PRIORITY (TAB_STRATEGY_ID,SITE_FEATURE_ID,PRIMARY_PRIORITY,SECONDARY_PRIORITY)
VALUES ('tabs_var1f_scrb3','SCARAB_CART',10,10);
Insert into cust.SS_TAB_STRATEGY_PRIORITY (TAB_STRATEGY_ID,SITE_FEATURE_ID,PRIMARY_PRIORITY,SECONDARY_PRIORITY)
VALUES ('tabs_var1f_scrb3','DYF',10,20);
Insert into cust.SS_TAB_STRATEGY_PRIORITY (TAB_STRATEGY_ID,SITE_FEATURE_ID,PRIMARY_PRIORITY,SECONDARY_PRIORITY)
VALUES ('tabs_var1f_scrb3','C_SAVE_COS_FDF',10,23);
Insert into cust.SS_TAB_STRATEGY_PRIORITY (TAB_STRATEGY_ID,SITE_FEATURE_ID,PRIMARY_PRIORITY,SECONDARY_PRIORITY)
VALUES ('tabs_var1f_scrb3','COS_FDF',10,24);
Insert into cust.SS_TAB_STRATEGY_PRIORITY (TAB_STRATEGY_ID,SITE_FEATURE_ID,PRIMARY_PRIORITY,SECONDARY_PRIORITY)
VALUES ('tabs_var1f_scrb3','C_SAVE_FDF',10,25);
Insert into cust.SS_TAB_STRATEGY_PRIORITY (TAB_STRATEGY_ID,SITE_FEATURE_ID,PRIMARY_PRIORITY,SECONDARY_PRIORITY)
values ('tabs_var1f_scrb3','FAVORITES',10,30);

Insert into cust.SS_TAB_STRATEGY_PRIORITY (TAB_STRATEGY_ID,SITE_FEATURE_ID,PRIMARY_PRIORITY,SECONDARY_PRIORITY)
VALUES ('tabs_var1f_scrb3','SCARAB_PERSONAL',20,10);
INSERT INTO cust.SS_TAB_STRATEGY_PRIORITY (TAB_STRATEGY_ID,SITE_FEATURE_ID,PRIMARY_PRIORITY,SECONDARY_PRIORITY)
VALUES ('tabs_var1f_scrb3','C_PEAK_FRUIT',20,20);
INSERT INTO cust.SS_TAB_STRATEGY_PRIORITY (TAB_STRATEGY_ID,SITE_FEATURE_ID,PRIMARY_PRIORITY,SECONDARY_PRIORITY)
VALUES ('tabs_var1f_scrb3','C_PEAK_PRODUCE',20,30);
Insert into cust.SS_TAB_STRATEGY_PRIORITY (TAB_STRATEGY_ID,SITE_FEATURE_ID,PRIMARY_PRIORITY,SECONDARY_PRIORITY)
values ('tabs_var1f_scrb3','C_NEW_PRODUCTS',20,40);

Insert into cust.SS_TAB_STRATEGY_PRIORITY (TAB_STRATEGY_ID,SITE_FEATURE_ID,PRIMARY_PRIORITY,SECONDARY_PRIORITY)
VALUES ('tabs_var1f_scrb3','C_HEALTHY_SNACKS',30,10);
INSERT INTO cust.SS_TAB_STRATEGY_PRIORITY (TAB_STRATEGY_ID,SITE_FEATURE_ID,PRIMARY_PRIORITY,SECONDARY_PRIORITY)
VALUES ('tabs_var1f_scrb3','C_DEALS',30,20);
Insert into cust.SS_TAB_STRATEGY_PRIORITY (TAB_STRATEGY_ID,SITE_FEATURE_ID,PRIMARY_PRIORITY,SECONDARY_PRIORITY)
values ('tabs_var1f_scrb3','C_YMAL',30,30);



--
-- SETUP DISTRIBUTIONS
--

-- Tab Distributions (CART_N_TABS)
--   50% tabs_var1fave_c2 (old)
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (cohort_id, variant_id, "DATE") values ('C1', 'tabs_var1fave_c2', sysdate);
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (cohort_id, variant_id, "DATE") values ('C2', 'tabs_var1fave_c2', sysdate);
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (cohort_id, variant_id, "DATE") values ('C3', 'tabs_var1fave_c2', sysdate);
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (cohort_id, variant_id, "DATE") values ('C4', 'tabs_var1fave_c2', sysdate);
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (cohort_id, variant_id, "DATE") values ('C5', 'tabs_var1fave_c2', sysdate);
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (cohort_id, variant_id, "DATE") values ('C6', 'tabs_var1fave_c2', sysdate);
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (cohort_id, variant_id, "DATE") values ('C7', 'tabs_var1fave_c2', sysdate);
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (cohort_id, variant_id, "DATE") values ('C8', 'tabs_var1fave_c2', sysdate);
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (cohort_id, variant_id, "DATE") values ('C9', 'tabs_var1fave_c2', sysdate);
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (COHORT_ID, VARIANT_ID, "DATE") VALUES ('C10', 'tabs_var1fave_c2', SYSDATE);
--   50% tabs_var1f_scrb3 (new)
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (cohort_id, variant_id, "DATE") values ('C11', 'tabs_var1f_scrb3', sysdate);
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (cohort_id, variant_id, "DATE") values ('C12', 'tabs_var1f_scrb3', sysdate);
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (cohort_id, variant_id, "DATE") values ('C13', 'tabs_var1f_scrb3', sysdate);
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (cohort_id, variant_id, "DATE") values ('C14', 'tabs_var1f_scrb3', sysdate);
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (cohort_id, variant_id, "DATE") values ('C15', 'tabs_var1f_scrb3', sysdate);
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (cohort_id, variant_id, "DATE") values ('C16', 'tabs_var1f_scrb3', sysdate);
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (cohort_id, variant_id, "DATE") values ('C17', 'tabs_var1f_scrb3', sysdate);
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (cohort_id, variant_id, "DATE") values ('C18', 'tabs_var1f_scrb3', sysdate);
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (cohort_id, variant_id, "DATE") values ('C19', 'tabs_var1f_scrb3', sysdate);
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (COHORT_ID, VARIANT_ID, "DATE") VALUES ('C20', 'tabs_var1f_scrb3', SYSDATE);


INSERT INTO CUST.SS_VARIANT_PARAMS(ID, KEY, VALUE) VALUES
('sc_cart_recommender', 'prez_title', 'You Might Also Like');
INSERT INTO CUST.SS_VARIANT_PARAMS(ID, KEY, VALUE) VALUES
('sc_cart_recommender', 'prez_desc', 'Based on the items in your cart, we recommend:');

