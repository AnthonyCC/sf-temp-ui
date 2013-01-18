--
-- APPDEV-1633 - Rollout Script
--


-- Alias 'ymal_ss_c2' -> 'ymal_ss'
insert into cust.ss_variants(ID, FEATURE, TYPE, ALIAS_ID) values('ymal_ss_c3', 'YMAL', 'alias', 'ymal_ss');

-- Alias 'sc_related' -> 'sc_related2'
insert into cust.ss_variants(ID, FEATURE, TYPE, ALIAS_ID) values('sc_related2', 'YMAL', 'alias', 'sc_related');

-- ... and augment scarab recommendation with smart ymal
INSERT INTO cust.ss_variant_params (id, key, value) VALUES ('sc_related2', 'generator', 'RelatedItems_scarab1(currentNode)+SmartYMAL():deprioritize()');

-- Introduce new 'sc_related_merch' variant but don't assign to cohorts yet.
insert into cust.ss_variants(ID, FEATURE, TYPE) values('sc_related_merch', 'YMAL', 'scripted');

-- ... and configure it
INSERT INTO cust.ss_variant_params (id, key, value) VALUES ('sc_related_merch', 'generator', 'RelatedItems_scarabAlsoViewed(currentNode)+SmartYMAL():deprioritize()');
INSERT INTO cust.ss_variant_params (id, key, value) VALUES ('sc_related_merch', 'exponent', '0.4');
INSERT INTO cust.ss_variant_params (id, key, value) VALUES ('sc_related_merch', 'sampling_strat', 'power');

-- Amend cohort distribution
--
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (cohort_id, "DATE", variant_id) VALUES ('C1', sysdate, 'ymal_ss_c3');
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (cohort_id, "DATE", variant_id) VALUES ('C2', sysdate, 'ymal_ss_c3');
insert into CUST.SS_VARIANT_ASSIGNMENT (COHORT_ID, "DATE", VARIANT_ID) values ('C3', sysdate, 'sc_related2');
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (cohort_id, "DATE", variant_id) VALUES ('C4', sysdate, 'sc_related2');
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (cohort_id, "DATE", variant_id) VALUES ('C5', sysdate, 'ymal_ss_c3');
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (cohort_id, "DATE", variant_id) VALUES ('C6', sysdate, 'ymal_ss_c3');
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (cohort_id, "DATE", variant_id) VALUES ('C7', sysdate, 'ymal_ss_c3');
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (cohort_id, "DATE", variant_id) VALUES ('C8', sysdate, 'ymal_ss_c3');
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (cohort_id, "DATE", variant_id) VALUES ('C9', sysdate, 'ymal_ss_c3');
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (cohort_id, "DATE", variant_id) VALUES ('C10', sysdate, 'ymal_ss_c3');
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (cohort_id, "DATE", variant_id) VALUES ('C11', sysdate, 'ymal_ss_c3');
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (cohort_id, "DATE", variant_id) VALUES ('C12', sysdate, 'ymal_ss_c3');
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (cohort_id, "DATE", variant_id) VALUES ('C13', sysdate, 'ymal_ss_c3');
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (cohort_id, "DATE", variant_id) VALUES ('C14', sysdate, 'ymal_ss_c3');
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (cohort_id, "DATE", variant_id) VALUES ('C15', sysdate, 'ymal_ss_c3');
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (cohort_id, "DATE", variant_id) VALUES ('C16', sysdate, 'ymal_ss_c3');
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (cohort_id, "DATE", variant_id) VALUES ('C17', sysdate, 'ymal_ss_c3');
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (cohort_id, "DATE", variant_id) VALUES ('C18', sysdate, 'ymal_ss_c3');
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (cohort_id, "DATE", variant_id) VALUES ('C19', sysdate, 'sc_related2');
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (cohort_id, "DATE", variant_id) VALUES ('C20', sysdate, 'ymal_ss_c3');


--
-- Misc fixes
--
-- Drop variants never used
--
DELETE FROM cust.ss_variant_params where id='sc_also_bought';
DELETE FROM cust.ss_variants where id='sc_also_bought';

DELETE FROM cust.ss_variant_params where id='sc_also_viewed';
DELETE FROM cust.ss_variants where id='sc_also_viewed';

DELETE FROM cust.ss_variant_params where id='sc_related_new';
DELETE FROM cust.ss_variants where id='sc_related_new';

DELETE FROM cust.ss_variant_params where id='sc_cart_recom';
DELETE FROM cust.ss_variants where id='sc_cart_recom';

