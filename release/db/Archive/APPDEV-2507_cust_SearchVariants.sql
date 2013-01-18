--
-- APPDEV-2507 Rollout Script
--


-- New Site Feature
--
INSERT INTO CUST.SS_SITE_FEATURE("ID", "TITLE", "PREZ_TITLE", "PREZ_DESC", "SMART_SAVING")
  VALUES('SRCH', 'Recommendations on Search Page', NULL, NULL, 0);


-- Add Variants
--
--   1. Scarab YMAL
INSERT INTO CUST.SS_VARIANTS(ID, FEATURE, TYPE, CONFIG_ID, ALIAS_ID, ARCHIVED)
  VALUES('srch_ymal', 'SRCH', 'alias', NULL, 'sc_cart_recommender', 'N');
-- INSERT INTO CUST.SS_VARIANT_PARAMS(ID, KEY, VALUE)
--  VALUES('srch_ymal', 'prez_title', 'YOU MIGHT ALSO LIKE...');

--   2. DYF
INSERT INTO CUST.SS_VARIANTS(ID, FEATURE, TYPE, CONFIG_ID, ALIAS_ID, ARCHIVED)
  VALUES('srch_dyf', 'SRCH', 'alias', NULL, 'dyf_scr_ord1', 'N');
-- INSERT INTO CUST.SS_VARIANT_PARAMS(ID, KEY, VALUE)
--  VALUES('srch_dyf', 'prez_title', 'Your Favorites');

--   3. Scarab Personal
INSERT INTO CUST.SS_VARIANTS(ID, FEATURE, TYPE, CONFIG_ID, ALIAS_ID, ARCHIVED)
  VALUES('srch_pers', 'SRCH', 'alias', NULL, 'sc_personal', 'N');
-- INSERT INTO CUST.SS_VARIANT_PARAMS(ID, KEY, VALUE)
--  VALUES('srch_pers', 'prez_title', 'People who bought the same items as you enjoyed...');


-- Distribute variants
--
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (COHORT_ID, "DATE", VARIANT_ID) VALUES ('C1',  SYSDATE, 'srch_ymal');
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (cohort_id, "DATE", variant_id) VALUES ('C2',  sysdate, 'srch_ymal');
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (COHORT_ID, "DATE", VARIANT_ID) VALUES ('C3',  SYSDATE, 'srch_ymal');
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (cohort_id, "DATE", variant_id) VALUES ('C4',  sysdate, 'srch_ymal');
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (COHORT_ID, "DATE", VARIANT_ID) VALUES ('C5',  SYSDATE, 'srch_ymal');
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (cohort_id, "DATE", variant_id) VALUES ('C6',  sysdate, 'srch_ymal');
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (COHORT_ID, "DATE", VARIANT_ID) VALUES ('C7',  SYSDATE, 'srch_ymal');

INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (cohort_id, "DATE", variant_id) VALUES ('C8',  sysdate, 'srch_dyf');
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (cohort_id, "DATE", variant_id) VALUES ('C9',  sysdate, 'srch_dyf');
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (cohort_id, "DATE", variant_id) VALUES ('C10', sysdate, 'srch_dyf');
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (cohort_id, "DATE", variant_id) VALUES ('C11', sysdate, 'srch_dyf');
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (cohort_id, "DATE", variant_id) VALUES ('C12', sysdate, 'srch_dyf');
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (cohort_id, "DATE", variant_id) VALUES ('C13', sysdate, 'srch_dyf');

INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (cohort_id, "DATE", variant_id) VALUES ('C14', sysdate, 'srch_pers');
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (cohort_id, "DATE", variant_id) VALUES ('C15', sysdate, 'srch_pers');
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (cohort_id, "DATE", variant_id) VALUES ('C16', sysdate, 'srch_pers');
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (cohort_id, "DATE", variant_id) VALUES ('C17', sysdate, 'srch_pers');
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (cohort_id, "DATE", variant_id) VALUES ('C18', sysdate, 'srch_pers');
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (cohort_id, "DATE", variant_id) VALUES ('C19', sysdate, 'srch_pers');
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (COHORT_ID, "DATE", VARIANT_ID) VALUES ('C20', SYSDATE, 'srch_pers');
