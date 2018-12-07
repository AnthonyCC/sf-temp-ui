-- Rollout

-- FK cart recommender site features
Insert into CUST.SS_SITE_FEATURE (ID,TITLE,PREZ_TITLE,PREZ_DESC,SMART_SAVING,E_STORE) values ('FK_CART_YMAL','YMAL on FoodKick',null,null,'0','FDX');

-- Variant definitions
Insert into CUST.SS_VARIANTS (ID,CONFIG_ID,FEATURE,TYPE,ALIAS_ID,ARCHIVED) values ('fk_cart_ymal',null,'FK_CART_YMAL','scripted',null,'N');

-- Variant configurations
Insert into CUST.SS_VARIANT_PARAMS (ID,KEY,VALUE) values ('fk_cart_ymal','exponent','0.4');
Insert into CUST.SS_VARIANT_PARAMS (ID,KEY,VALUE) values ('fk_cart_ymal','generator','RelatedItems_scarabAlsoBought(cartContents)');
Insert into CUST.SS_VARIANT_PARAMS (ID,KEY,VALUE) values ('fk_cart_ymal','prez_title','You Might Also Like');
Insert into CUST.SS_VARIANT_PARAMS (ID,KEY,VALUE) values ('fk_cart_ymal','sampling_strat','power');

-- Variant distribution
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (cohort_id, variant_id, "DATE") values ('C1', 'fk_cart_ymal', sysdate);
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (cohort_id, variant_id, "DATE") values ('C2', 'fk_cart_ymal', sysdate);
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (cohort_id, variant_id, "DATE") values ('C3', 'fk_cart_ymal', sysdate);
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (cohort_id, variant_id, "DATE") values ('C4', 'fk_cart_ymal', sysdate);
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (cohort_id, variant_id, "DATE") values ('C5', 'fk_cart_ymal', sysdate);
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (cohort_id, variant_id, "DATE") values ('C6', 'fk_cart_ymal', sysdate);
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (cohort_id, variant_id, "DATE") values ('C7', 'fk_cart_ymal', sysdate);
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (cohort_id, variant_id, "DATE") values ('C8', 'fk_cart_ymal', sysdate);
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (cohort_id, variant_id, "DATE") values ('C9', 'fk_cart_ymal', sysdate);
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (cohort_id, variant_id, "DATE") values ('C10', 'fk_cart_ymal', sysdate);
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (cohort_id, variant_id, "DATE") values ('C11', 'fk_cart_ymal', sysdate);
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (cohort_id, variant_id, "DATE") values ('C12', 'fk_cart_ymal', sysdate);
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (cohort_id, variant_id, "DATE") values ('C13', 'fk_cart_ymal', sysdate);
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (cohort_id, variant_id, "DATE") values ('C14', 'fk_cart_ymal', sysdate);
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (cohort_id, variant_id, "DATE") values ('C15', 'fk_cart_ymal', sysdate);
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (cohort_id, variant_id, "DATE") values ('C16', 'fk_cart_ymal', sysdate);
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (cohort_id, variant_id, "DATE") values ('C17', 'fk_cart_ymal', sysdate);
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (cohort_id, variant_id, "DATE") values ('C18', 'fk_cart_ymal', sysdate);
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (cohort_id, variant_id, "DATE") values ('C19', 'fk_cart_ymal', sysdate);
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (cohort_id, variant_id, "DATE") values ('C20', 'fk_cart_ymal', sysdate);
