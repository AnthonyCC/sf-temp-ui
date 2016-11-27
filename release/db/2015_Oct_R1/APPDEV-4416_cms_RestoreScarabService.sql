--
--  Certona DB Rollback Package
--  ===========================
--
--
-- Site Features involved in Certona project
-- 
-- 	"RIGHT_NAV_PERS", "RIGHT_NAV_RLTD",
-- 	"BRWS_CAT_LST", "BRWS_PRD_LST",
-- 	"YMAL", "YMAL_PDTL",
-- 	"SRCH"
--

--
-- ### Part #1 - restore old site features ###
-- ### Original roll-back script: release/db/Archive/2014_Sep_R1/APPDEV-3528_cust_CertonaVariantDistribution_ROLLBACK.sql ###
--
-- ... ... ... ... ... ... ... ... ... ... ... ... ... ... ... ... ... ... ... ... ... ... --


--
-- [APPDEV-3528] Rollback Script
-- Revert to variant distributions of legacy site features
--


-------------------------------------------------------------------------------
--
-- Site Feature: SRCH
-- Revert to Config: APPDEV-3033
--
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (COHORT_ID, "DATE", VARIANT_ID) VALUES ('C1',  SYSDATE, 'srch_pers_promo');
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (cohort_id, "DATE", variant_id) VALUES ('C2',  sysdate, 'srch_pers_promo');
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (COHORT_ID, "DATE", VARIANT_ID) VALUES ('C3',  SYSDATE, 'srch_pers_promo');
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (cohort_id, "DATE", variant_id) VALUES ('C4',  sysdate, 'srch_pers_promo');
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
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (cohort_id, "DATE", variant_id) VALUES ('C16', sysdate, 'srch_pers');

INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (cohort_id, "DATE", variant_id) VALUES ('C17', sysdate, 'srch_ctrl');
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (cohort_id, "DATE", variant_id) VALUES ('C18', sysdate, 'srch_ctrl');
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (cohort_id, "DATE", variant_id) VALUES ('C19', sysdate, 'srch_ctrl');
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (COHORT_ID, "DATE", VARIANT_ID) VALUES ('C20', SYSDATE, 'srch_ctrl');



-------------------------------------------------------------------------------
--
-- Site Feature: YMAL
-- Revert to Config: APPDEV-3204
--
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (cohort_id, "DATE", variant_id) VALUES ('C1', sysdate, 'sc_related2');
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (cohort_id, "DATE", variant_id) VALUES ('C2', sysdate, 'sc_related2');
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (cohort_id, "DATE", variant_id) VALUES ('C3', sysdate, 'sc_related2');
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (cohort_id, "DATE", variant_id) VALUES ('C4', sysdate, 'sc_related2');
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (cohort_id, "DATE", variant_id) VALUES ('C5', sysdate, 'sc_related2');
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (cohort_id, "DATE", variant_id) VALUES ('C6', sysdate, 'sc_related_merch');
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (cohort_id, "DATE", variant_id) VALUES ('C7', sysdate, 'sc_related2');
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (cohort_id, "DATE", variant_id) VALUES ('C8', sysdate, 'sc_related2');
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (cohort_id, "DATE", variant_id) VALUES ('C9', sysdate, 'sc_related2');
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (cohort_id, "DATE", variant_id) VALUES ('C10', sysdate, 'sc_related2');
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (cohort_id, "DATE", variant_id) VALUES ('C11', sysdate, 'sc_related2');
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (cohort_id, "DATE", variant_id) VALUES ('C12', sysdate, 'sc_related2');
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (cohort_id, "DATE", variant_id) VALUES ('C13', sysdate, 'sc_related2');
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (cohort_id, "DATE", variant_id) VALUES ('C14', sysdate, 'sc_related2');
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (cohort_id, "DATE", variant_id) VALUES ('C15', sysdate, 'sc_related2');
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (cohort_id, "DATE", variant_id) VALUES ('C16', sysdate, 'sc_related2');
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (cohort_id, "DATE", variant_id) VALUES ('C17', sysdate, 'sc_related2');
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (cohort_id, "DATE", variant_id) VALUES ('C18', sysdate, 'sc_related2');
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (cohort_id, "DATE", variant_id) VALUES ('C19', sysdate, 'sc_related2');
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (cohort_id, "DATE", variant_id) VALUES ('C20', sysdate, 'sc_related2');



-------------------------------------------------------------------------------
--
-- Site Feature: YMAL_PDTL
-- Revert to Config: APPDEV-2241
--
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (cohort_id, variant_id, "DATE") values ('C1', 'ymal_pdtl', sysdate);
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (cohort_id, variant_id, "DATE") values ('C2', 'ymal_pdtl', sysdate);
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (cohort_id, variant_id, "DATE") values ('C3', 'ymal_pdtl', sysdate);
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (cohort_id, variant_id, "DATE") values ('C4', 'ymal_pdtl', sysdate);
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (cohort_id, variant_id, "DATE") values ('C5', 'ymal_pdtl', sysdate);
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (cohort_id, variant_id, "DATE") values ('C6', 'ymal_pdtl', sysdate);
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (cohort_id, variant_id, "DATE") values ('C7', 'ymal_pdtl', sysdate);
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (cohort_id, variant_id, "DATE") values ('C8', 'ymal_pdtl', sysdate);
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (cohort_id, variant_id, "DATE") values ('C9', 'ymal_pdtl', sysdate);
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (cohort_id, variant_id, "DATE") values ('C10', 'ymal_pdtl', sysdate);
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (cohort_id, variant_id, "DATE") values ('C11', 'ymal_pdtl', sysdate);
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (cohort_id, variant_id, "DATE") values ('C12', 'ymal_pdtl', sysdate);
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (cohort_id, variant_id, "DATE") values ('C13', 'ymal_pdtl', sysdate);
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (cohort_id, variant_id, "DATE") values ('C14', 'ymal_pdtl', sysdate);
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (cohort_id, variant_id, "DATE") values ('C15', 'ymal_pdtl', sysdate);
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (cohort_id, variant_id, "DATE") values ('C16', 'ymal_pdtl', sysdate);
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (cohort_id, variant_id, "DATE") values ('C17', 'ymal_pdtl', sysdate);
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (cohort_id, variant_id, "DATE") values ('C18', 'ymal_pdtl', sysdate);
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (cohort_id, variant_id, "DATE") values ('C19', 'ymal_pdtl', sysdate);
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (cohort_id, variant_id, "DATE") VALUES ('C20', 'ymal_pdtl', SYSDATE);

-- ... ... ... ... ... ... ... ... ... ... ... ... ... ... ... ... ... ... ... ... ... ... --
--
-- ### Part #2 - distribute Scarab to the entire world ###
--

-------------------------------------------------------------------------------
--
-- Site Feature: RIGHT_NAV_PERS
-- C1 and C4 cohorts fall back to scarab
--
insert into cust.ss_variant_assignment values ('C1', sysdate, 'right_nav_scarab');
insert into cust.ss_variant_assignment values ('C4', sysdate, 'right_nav_scarab');
insert into cust.ss_variant_assignment values ('C3', sysdate, 'right_nav_scarab');
insert into cust.ss_variant_assignment values ('C5', sysdate, 'right_nav_scarab');
insert into cust.ss_variant_assignment values ('C7', sysdate, 'right_nav_scarab');
insert into cust.ss_variant_assignment values ('C8', sysdate, 'right_nav_scarab');
insert into cust.ss_variant_assignment values ('C13', sysdate, 'right_nav_scarab');
insert into cust.ss_variant_assignment values ('C14', sysdate, 'right_nav_scarab');
insert into cust.ss_variant_assignment values ('C15', sysdate, 'right_nav_scarab');
insert into cust.ss_variant_assignment values ('C16', sysdate, 'right_nav_scarab');
insert into cust.ss_variant_assignment values ('C17', sysdate, 'right_nav_scarab');

insert into cust.ss_variant_assignment values ('C2', sysdate, 'right_nav_scarab');
insert into cust.ss_variant_assignment values ('C6', sysdate, 'right_nav_scarab');
insert into cust.ss_variant_assignment values ('C9', sysdate, 'right_nav_scarab');
insert into cust.ss_variant_assignment values ('C10', sysdate, 'right_nav_scarab');
insert into cust.ss_variant_assignment values ('C11', sysdate, 'right_nav_scarab');
insert into cust.ss_variant_assignment values ('C12', sysdate, 'right_nav_scarab');
insert into cust.ss_variant_assignment values ('C18', sysdate, 'right_nav_scarab');
insert into cust.ss_variant_assignment values ('C19', sysdate, 'right_nav_scarab');
insert into cust.ss_variant_assignment values ('C20', sysdate, 'right_nav_scarab');

-------------------------------------------------------------------------------
--
-- Site Feature: RIGHT_NAV_RLTD
-- C1 and C4 cohorts fall back to scarab
--
insert into cust.ss_variant_assignment values ('C1', sysdate, 'right_nav_scarab_pop');
insert into cust.ss_variant_assignment values ('C4', sysdate, 'right_nav_scarab_pop');
insert into cust.ss_variant_assignment values ('C3', sysdate, 'right_nav_scarab_pop');
insert into cust.ss_variant_assignment values ('C5', sysdate, 'right_nav_scarab_pop');
insert into cust.ss_variant_assignment values ('C7', sysdate, 'right_nav_scarab_pop');
insert into cust.ss_variant_assignment values ('C8', sysdate, 'right_nav_scarab_pop');
insert into cust.ss_variant_assignment values ('C13', sysdate, 'right_nav_scarab_pop');
insert into cust.ss_variant_assignment values ('C14', sysdate, 'right_nav_scarab_pop');
insert into cust.ss_variant_assignment values ('C15', sysdate, 'right_nav_scarab_pop');
insert into cust.ss_variant_assignment values ('C16', sysdate, 'right_nav_scarab_pop');
insert into cust.ss_variant_assignment values ('C17', sysdate, 'right_nav_scarab_pop');

insert into cust.ss_variant_assignment values ('C2', sysdate, 'right_nav_scarab_pop');
insert into cust.ss_variant_assignment values ('C6', sysdate, 'right_nav_scarab_pop');
insert into cust.ss_variant_assignment values ('C9', sysdate, 'right_nav_scarab_pop');
insert into cust.ss_variant_assignment values ('C10', sysdate, 'right_nav_scarab_pop');
insert into cust.ss_variant_assignment values ('C11', sysdate, 'right_nav_scarab_pop');
insert into cust.ss_variant_assignment values ('C12', sysdate, 'right_nav_scarab_pop');
insert into cust.ss_variant_assignment values ('C18', sysdate, 'right_nav_scarab_pop');
insert into cust.ss_variant_assignment values ('C19', sysdate, 'right_nav_scarab_pop');
insert into cust.ss_variant_assignment values ('C20', sysdate, 'right_nav_scarab_pop');


-------------------------------------------------------------------------------
--
-- Site Feature: BRWS_CAT_LST
-- C1 and C4 cohorts fall back to scarab
--
insert into cust.ss_variant_assignment values ('C1', sysdate, 'brws_cat_list_scr_pers');
insert into cust.ss_variant_assignment values ('C4', sysdate, 'brws_cat_list_scr_pers');
insert into cust.ss_variant_assignment values ('C3', sysdate, 'brws_cat_list_scr_pers');
insert into cust.ss_variant_assignment values ('C5', sysdate, 'brws_cat_list_scr_pers');
insert into cust.ss_variant_assignment values ('C7', sysdate, 'brws_cat_list_scr_pers');
insert into cust.ss_variant_assignment values ('C8', sysdate, 'brws_cat_list_scr_pers');
insert into cust.ss_variant_assignment values ('C13', sysdate, 'brws_cat_list_scr_pers');
insert into cust.ss_variant_assignment values ('C14', sysdate, 'brws_cat_list_scr_pers');
insert into cust.ss_variant_assignment values ('C15', sysdate, 'brws_cat_list_scr_pers');
insert into cust.ss_variant_assignment values ('C16', sysdate, 'brws_cat_list_scr_pers');
insert into cust.ss_variant_assignment values ('C17', sysdate, 'brws_cat_list_scr_pers');

insert into cust.ss_variant_assignment values ('C2', sysdate, 'brws_cat_list_scr_pers');
insert into cust.ss_variant_assignment values ('C6', sysdate, 'brws_cat_list_scr_pers');
insert into cust.ss_variant_assignment values ('C9', sysdate, 'brws_cat_list_scr_pers');
insert into cust.ss_variant_assignment values ('C10', sysdate, 'brws_cat_list_scr_pers');
insert into cust.ss_variant_assignment values ('C11', sysdate, 'brws_cat_list_scr_pers');
insert into cust.ss_variant_assignment values ('C12', sysdate, 'brws_cat_list_scr_pers');
insert into cust.ss_variant_assignment values ('C18', sysdate, 'brws_cat_list_scr_pers');
insert into cust.ss_variant_assignment values ('C19', sysdate, 'brws_cat_list_scr_pers');
insert into cust.ss_variant_assignment values ('C20', sysdate, 'brws_cat_list_scr_pers');


-------------------------------------------------------------------------------
--
-- Site Feature: BRWS_PRD_LST
-- C1 and C4 cohorts fall back to scarab
--
insert into cust.ss_variant_assignment values ('C1', sysdate, 'brws_prd_list_scr_pers');
insert into cust.ss_variant_assignment values ('C4', sysdate, 'brws_prd_list_scr_pers');
insert into cust.ss_variant_assignment values ('C3', sysdate, 'brws_prd_list_scr_pers');
insert into cust.ss_variant_assignment values ('C5', sysdate, 'brws_prd_list_scr_pers');
insert into cust.ss_variant_assignment values ('C7', sysdate, 'brws_prd_list_scr_pers');
insert into cust.ss_variant_assignment values ('C8', sysdate, 'brws_prd_list_scr_pers');
insert into cust.ss_variant_assignment values ('C13', sysdate, 'brws_prd_list_scr_pers');
insert into cust.ss_variant_assignment values ('C14', sysdate, 'brws_prd_list_scr_pers');
insert into cust.ss_variant_assignment values ('C15', sysdate, 'brws_prd_list_scr_pers');
insert into cust.ss_variant_assignment values ('C16', sysdate, 'brws_prd_list_scr_pers');
insert into cust.ss_variant_assignment values ('C17', sysdate, 'brws_prd_list_scr_pers');

insert into cust.ss_variant_assignment values ('C2', sysdate, 'brws_prd_list_scr_pers');
insert into cust.ss_variant_assignment values ('C6', sysdate, 'brws_prd_list_scr_pers');
insert into cust.ss_variant_assignment values ('C9', sysdate, 'brws_prd_list_scr_pers');
insert into cust.ss_variant_assignment values ('C10', sysdate, 'brws_prd_list_scr_pers');
insert into cust.ss_variant_assignment values ('C11', sysdate, 'brws_prd_list_scr_pers');
insert into cust.ss_variant_assignment values ('C12', sysdate, 'brws_prd_list_scr_pers');
insert into cust.ss_variant_assignment values ('C18', sysdate, 'brws_prd_list_scr_pers');
insert into cust.ss_variant_assignment values ('C19', sysdate, 'brws_prd_list_scr_pers');
insert into cust.ss_variant_assignment values ('C20', sysdate, 'brws_prd_list_scr_pers');


-------------------------------------------------------------------------------
--
-- Site Feature: SRCH_RLTD
-- Revert to Config: APPDEV-2067
--
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
