--
-- APPDEV-2967 Rollout Script
--


-- Add Variants
--
--  Scarab Personal Promoted
INSERT INTO CUST.SS_VARIANTS(ID, FEATURE, TYPE, CONFIG_ID, ALIAS_ID, ARCHIVED)
  VALUES('srch_pers_promo', 'SRCH', 'alias', NULL, 'sc_personal', 'N');
INSERT INTO CUST.SS_VARIANT_PARAMS(ID, KEY, VALUE)
  VALUES('srch_pers_promo', 'prez_title', 'Other Customers Enjoyed...');
INSERT INTO CUST.SS_VARIANT_PARAMS(ID, KEY, VALUE)
  VALUES('srch_pers_promo', 'srch_promoted', 'true');


INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (COHORT_ID, "DATE", VARIANT_ID) VALUES ('C1',  SYSDATE, 'srch_pers');
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (cohort_id, "DATE", variant_id) VALUES ('C2',  sysdate, 'srch_pers');
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (COHORT_ID, "DATE", VARIANT_ID) VALUES ('C3',  SYSDATE, 'srch_pers');
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (cohort_id, "DATE", variant_id) VALUES ('C4',  sysdate, 'srch_pers');
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (COHORT_ID, "DATE", VARIANT_ID) VALUES ('C5',  SYSDATE, 'srch_pers');
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (cohort_id, "DATE", variant_id) VALUES ('C6',  sysdate, 'srch_pers');
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (COHORT_ID, "DATE", VARIANT_ID) VALUES ('C7',  SYSDATE, 'srch_pers');
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (cohort_id, "DATE", variant_id) VALUES ('C8',  sysdate, 'srch_pers');
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (cohort_id, "DATE", variant_id) VALUES ('C9',  sysdate, 'srch_pers');
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (COHORT_ID, "DATE", VARIANT_ID) VALUES ('C10', SYSDATE, 'srch_pers');
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (cohort_id, "DATE", variant_id) VALUES ('C11', sysdate, 'srch_pers');
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (cohort_id, "DATE", variant_id) VALUES ('C12', sysdate, 'srch_pers');
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (cohort_id, "DATE", variant_id) VALUES ('C13', sysdate, 'srch_pers');
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (cohort_id, "DATE", variant_id) VALUES ('C14', sysdate, 'srch_pers');
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (COHORT_ID, "DATE", VARIANT_ID) VALUES ('C15', SYSDATE, 'srch_pers');

INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (cohort_id, "DATE", variant_id) VALUES ('C16', sysdate, 'srch_pers_promo');
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (cohort_id, "DATE", variant_id) VALUES ('C17', sysdate, 'srch_pers_promo');
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (cohort_id, "DATE", variant_id) VALUES ('C18', sysdate, 'srch_pers_promo');
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (cohort_id, "DATE", variant_id) VALUES ('C19', sysdate, 'srch_pers_promo');
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (COHORT_ID, "DATE", VARIANT_ID) VALUES ('C20', SYSDATE, 'srch_pers_promo');

--
-- YMAL recommender on search page
--
-- Create new Site Feature
INSERT INTO CUST.SS_SITE_FEATURE(ID, TITLE, SMART_SAVING) VALUES ('SRCH_RLTD', 'Related items on Search page', 0);


-- Add a single variant which is just an alias to scarab_related_merch
INSERT INTO CUST.SS_VARIANTS (ID,CONFIG_ID,FEATURE,TYPE,ALIAS_ID) VALUES ('srch_rltd',NULL,'SRCH_RLTD','alias','sc_related_merch');
INSERT INTO CUST.SS_VARIANT_PARAMS(ID, KEY, VALUE) VALUES('srch_rltd', 'prez_title', 'You Might Also Like');


-- Distribute variant to every cohorts
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (cohort_id, variant_id, "DATE") values ('C1', 'srch_rltd', sysdate);
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (cohort_id, variant_id, "DATE") values ('C2', 'srch_rltd', sysdate);
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (cohort_id, variant_id, "DATE") values ('C3', 'srch_rltd', sysdate);
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (cohort_id, variant_id, "DATE") values ('C4', 'srch_rltd', sysdate);
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (cohort_id, variant_id, "DATE") values ('C5', 'srch_rltd', sysdate);
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (cohort_id, variant_id, "DATE") values ('C6', 'srch_rltd', sysdate);
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (cohort_id, variant_id, "DATE") values ('C7', 'srch_rltd', sysdate);
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (cohort_id, variant_id, "DATE") values ('C8', 'srch_rltd', sysdate);
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (cohort_id, variant_id, "DATE") values ('C9', 'srch_rltd', sysdate);
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (cohort_id, variant_id, "DATE") values ('C10', 'srch_rltd', sysdate);
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (cohort_id, variant_id, "DATE") values ('C11', 'srch_rltd', sysdate);
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (cohort_id, variant_id, "DATE") values ('C12', 'srch_rltd', sysdate);
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (cohort_id, variant_id, "DATE") values ('C13', 'srch_rltd', sysdate);
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (cohort_id, variant_id, "DATE") values ('C14', 'srch_rltd', sysdate);
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (cohort_id, variant_id, "DATE") values ('C15', 'srch_rltd', sysdate);
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (cohort_id, variant_id, "DATE") values ('C16', 'srch_rltd', sysdate);
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (cohort_id, variant_id, "DATE") values ('C17', 'srch_rltd', sysdate);
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (cohort_id, variant_id, "DATE") values ('C18', 'srch_rltd', sysdate);
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (cohort_id, variant_id, "DATE") values ('C19', 'srch_rltd', sysdate);
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (cohort_id, variant_id, "DATE") VALUES ('C20', 'srch_rltd', SYSDATE);
