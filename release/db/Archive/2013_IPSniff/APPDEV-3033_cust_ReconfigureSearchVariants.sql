--
-- APPDEV-3033 Rollout Script
--

--
--  SRCH Promoted + Control Reconfiguration


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


-- YMAL recommender on search page
--
-- Add a single null variant for srch_rltd

INSERT INTO CUST.SS_VARIANTS ("ID",CONFIG_ID,FEATURE,"TYPE",ALIAS_ID,ARCHIVED) VALUES 
('srch_rltd_ctrl', NULL, 'SRCH_RLTD', 'nil', NULL, 'N');


-- Distribute variant to control like above

INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (COHORT_ID, "DATE", VARIANT_ID) VALUES ('C1',  SYSDATE, 'srch_rltd');
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (cohort_id, "DATE", variant_id) VALUES ('C2',  sysdate, 'srch_rltd');
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (COHORT_ID, "DATE", VARIANT_ID) VALUES ('C3',  SYSDATE, 'srch_rltd');
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (cohort_id, "DATE", variant_id) VALUES ('C4',  sysdate, 'srch_rltd');
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (COHORT_ID, "DATE", VARIANT_ID) VALUES ('C5',  SYSDATE, 'srch_rltd');
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (cohort_id, "DATE", variant_id) VALUES ('C6',  sysdate, 'srch_rltd');
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (COHORT_ID, "DATE", VARIANT_ID) VALUES ('C7',  SYSDATE, 'srch_rltd');
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (cohort_id, "DATE", variant_id) VALUES ('C8',  sysdate, 'srch_rltd');
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (cohort_id, "DATE", variant_id) VALUES ('C9',  sysdate, 'srch_rltd');
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (COHORT_ID, "DATE", VARIANT_ID) VALUES ('C10', SYSDATE, 'srch_rltd');
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (cohort_id, "DATE", variant_id) VALUES ('C11', sysdate, 'srch_rltd');
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (cohort_id, "DATE", variant_id) VALUES ('C12', sysdate, 'srch_rltd');
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (cohort_id, "DATE", variant_id) VALUES ('C13', sysdate, 'srch_rltd');
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (cohort_id, "DATE", variant_id) VALUES ('C14', sysdate, 'srch_rltd');
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (COHORT_ID, "DATE", VARIANT_ID) VALUES ('C15', SYSDATE, 'srch_rltd');
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (cohort_id, "DATE", variant_id) VALUES ('C16', sysdate, 'srch_rltd');

INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (cohort_id, variant_id, "DATE") values ('C17', 'srch_rltd_ctrl', sysdate);
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (cohort_id, variant_id, "DATE") values ('C18', 'srch_rltd_ctrl', sysdate);
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (cohort_id, variant_id, "DATE") values ('C19', 'srch_rltd_ctrl', sysdate);
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (cohort_id, variant_id, "DATE") VALUES ('C20', 'srch_rltd_ctrl', SYSDATE);
