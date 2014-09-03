-------------------------------------------------------------------------------
--
-- Site Feature: SRCH
-- Override Config: APPDEV-3033
--
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT VALUES ('C1',  sysdate, 'srch_pers_promo');
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT VALUES ('C3',  sysdate, 'srch_pers_promo');
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT VALUES ('C4',  sysdate, 'srch_pers_promo');
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT VALUES ('C5',  sysdate, 'srch_pers');
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT VALUES ('C7',  sysdate, 'srch_pers');
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT VALUES ('C8',  sysdate, 'srch_pers');
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT VALUES ('C13', sysdate, 'srch_pers');
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT VALUES ('C14', sysdate, 'srch_pers');
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT VALUES ('C15', sysdate, 'srch_pers');
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT VALUES ('C16', sysdate, 'srch_pers');
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT VALUES ('C17', sysdate, 'srch_ctrl');

insert into cust.ss_variant_assignment values ('C2', sysdate, 'srch_pers');
insert into cust.ss_variant_assignment values ('C6', sysdate, 'srch_pers');
insert into cust.ss_variant_assignment values ('C9', sysdate, 'srch_pers');
insert into cust.ss_variant_assignment values ('C10', sysdate, 'srch_pers');
insert into cust.ss_variant_assignment values ('C11', sysdate, 'srch_pers');
insert into cust.ss_variant_assignment values ('C12', sysdate, 'srch_pers');
insert into cust.ss_variant_assignment values ('C18', sysdate, 'srch_pers');
insert into cust.ss_variant_assignment values ('C19', sysdate, 'srch_pers');
insert into cust.ss_variant_assignment values ('C20', sysdate, 'srch_pers');


-------------------------------------------------------------------------------
--
-- Site Feature: YMAL
-- Override Config: APPDEV-3204
--
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT VALUES ('C1', sysdate, 'sc_related2');
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT VALUES ('C3', sysdate, 'sc_related2');
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT VALUES ('C4', sysdate, 'sc_related2');
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT VALUES ('C5', sysdate, 'sc_related2');
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT VALUES ('C7', sysdate, 'sc_related2');
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT VALUES ('C8', sysdate, 'sc_related2');
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT VALUES ('C13', sysdate, 'sc_related2');
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT VALUES ('C14', sysdate, 'sc_related2');
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT VALUES ('C15', sysdate, 'sc_related2');
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT VALUES ('C16', sysdate, 'sc_related2');
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT VALUES ('C17', sysdate, 'sc_related2');

insert into cust.ss_variant_assignment values ('C2', sysdate, 'sc_related2');
insert into cust.ss_variant_assignment values ('C6', sysdate, 'sc_related2');
insert into cust.ss_variant_assignment values ('C9', sysdate, 'sc_related2');
insert into cust.ss_variant_assignment values ('C10', sysdate, 'sc_related2');
insert into cust.ss_variant_assignment values ('C11', sysdate, 'sc_related2');
insert into cust.ss_variant_assignment values ('C12', sysdate, 'sc_related2');
insert into cust.ss_variant_assignment values ('C18', sysdate, 'sc_related2');
insert into cust.ss_variant_assignment values ('C19', sysdate, 'sc_related2');
insert into cust.ss_variant_assignment values ('C20', sysdate, 'sc_related2');


-------------------------------------------------------------------------------
--
-- Site Feature: YMAL_PDTL
-- Override Config: APPDEV-2241
--
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT values ('C1', sysdate, 'ymal_pdtl');
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT values ('C3', sysdate, 'ymal_pdtl');
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT values ('C4', sysdate, 'ymal_pdtl');
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT values ('C5', sysdate, 'ymal_pdtl');
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT values ('C7', sysdate, 'ymal_pdtl');
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT values ('C8', sysdate, 'ymal_pdtl');
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT values ('C13', sysdate, 'ymal_pdtl');
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT values ('C14', sysdate, 'ymal_pdtl');
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT values ('C15', sysdate, 'ymal_pdtl');
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT values ('C16', sysdate, 'ymal_pdtl');
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT values ('C17', sysdate, 'ymal_pdtl');

insert into cust.ss_variant_assignment values ('C2', sysdate, 'ymal_pdtl');
insert into cust.ss_variant_assignment values ('C6', sysdate, 'ymal_pdtl');
insert into cust.ss_variant_assignment values ('C9', sysdate, 'ymal_pdtl');
insert into cust.ss_variant_assignment values ('C10', sysdate, 'ymal_pdtl');
insert into cust.ss_variant_assignment values ('C11', sysdate, 'ymal_pdtl');
insert into cust.ss_variant_assignment values ('C12', sysdate, 'ymal_pdtl');
insert into cust.ss_variant_assignment values ('C18', sysdate, 'ymal_pdtl');
insert into cust.ss_variant_assignment values ('C19', sysdate, 'ymal_pdtl');
insert into cust.ss_variant_assignment values ('C20', sysdate, 'ymal_pdtl');


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
