---------------------- ROLLBACK STEPS -------------------------
--
-- Original Source: 2012_May_R1/APPDEV-2320_cust_FlatTabVariants-ROLLBACK.sql
--
-- Restore previous cart'n'tabs distribution
--

INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (COHORT_ID, VARIANT_ID, "DATE") VALUES ('C1', 'tabs_var1fave_c2', SYSDATE);
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (cohort_id, variant_id, "DATE") values ('C2', 'tabs_var1fave_c2', sysdate);
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (cohort_id, variant_id, "DATE") values ('C3', 'tabs_var1f_scrb2', sysdate);
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (cohort_id, variant_id, "DATE") values ('C4', 'tabs_var1fave_c2', sysdate);
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (cohort_id, variant_id, "DATE") values ('C5', 'tabs_var1f_scrb2', sysdate);
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (cohort_id, variant_id, "DATE") values ('C6', 'tabs_var1fave_c2', sysdate);
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (cohort_id, variant_id, "DATE") values ('C7', 'tabs_var1fave_c2', sysdate);
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (cohort_id, variant_id, "DATE") values ('C8', 'tabs_var1fave_c2', sysdate);
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (cohort_id, variant_id, "DATE") values ('C9', 'tabs_var1f_scrb2', sysdate);
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (cohort_id, variant_id, "DATE") values ('C10', 'tabs_var1fave_c2', sysdate);
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (cohort_id, variant_id, "DATE") values ('C11', 'tabs_var1fave_c2', sysdate);
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (cohort_id, variant_id, "DATE") values ('C12', 'tabs_var1f_scrb2', sysdate);
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (cohort_id, variant_id, "DATE") values ('C13', 'tabs_var1f_scrb2', sysdate);
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (cohort_id, variant_id, "DATE") values ('C14', 'tabs_var1f_scrb2', sysdate);
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (cohort_id, variant_id, "DATE") values ('C15', 'tabs_var1fave_c2', sysdate);
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (cohort_id, variant_id, "DATE") values ('C16', 'tabs_var1fave_c2', sysdate);
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (cohort_id, variant_id, "DATE") values ('C17', 'tabs_var1fave_c2', sysdate);
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (cohort_id, variant_id, "DATE") values ('C18', 'tabs_var1f_scrb2', sysdate);
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (cohort_id, variant_id, "DATE") values ('C19', 'tabs_var1fave_c2', sysdate);
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (COHORT_ID, VARIANT_ID, "DATE") VALUES ('C20', 'tabs_var1f_scrb2', SYSDATE);


-- Revert tab strategies
--
DELETE FROM CUST.SS_TAB_STRATEGY_PRIORITY
where TAB_STRATEGY_ID in ('tabs_var1fave_c2_test', 'tabs_var1f_scrb2_test');


-- restore tab variant column size
--
ALTER TABLE CUST.SS_TAB_STRATEGY_PRIORITY
modify (TAB_STRATEGY_ID VARCHAR2(16));


-- Last step would be to delete 'tabs_var1fave_c2_test' and 'tabs_var1f_scrb2_test'
--   tab variants and their params too.
-- Once they are referred by other entities such as LOG entries they no longer be removed
-- So leave them as-is.
