--
-- Description: Rollout script to reconfigure brand name deals variant
--
-- Author: Gabor Sebestyen (gabor.sebestyen@euedge.com)
--
-- JIRA Ref: APPDEV-1889
--
-- Affected Tables: CUST.SS_VARIANTS CUST.SS_VARIANT_PARAMS CUST.SS_VARIANT_ASSIGNMENT
--


-- Create next version of brand name deals variant
--
insert into cust.ss_variants(ID, FEATURE, TYPE, ALIAS_ID) values('brand-name-deals2', 'BRAND_NAME_DEALS', 'alias', 'brand-name-deals');


-- Amend configuration
--
INSERT INTO CUST.SS_VARIANT_PARAMS (ID, KEY, VALUE) VALUES ('brand-name-deals2', 'generator', 'FeaturedItems:atLeast(DealsPercentage, 0.05):prioritize() + RecursiveNodes(currentNode):atLeast(DealsPercentage, 0.05)');
INSERT INTO CUST.SS_VARIANT_PARAMS (ID, KEY, VALUE) VALUES ('brand-name-deals2', 'scoring', 'Popularity_Discretized');


-- Replace old variant in cohort distribution
--
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (cohort_id, "DATE", variant_id) VALUES ('C1', sysdate, 'brand-name-deals2');
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (cohort_id, "DATE", variant_id) VALUES ('C2', sysdate, 'brand-name-deals2');
insert into CUST.SS_VARIANT_ASSIGNMENT (COHORT_ID, "DATE", VARIANT_ID) values ('C3', sysdate, 'brand-name-deals2');
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (cohort_id, "DATE", variant_id) VALUES ('C4', sysdate, 'brand-name-deals2');
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (cohort_id, "DATE", variant_id) VALUES ('C5', sysdate, 'brand-name-deals2');
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (cohort_id, "DATE", variant_id) VALUES ('C6', sysdate, 'brand-name-deals2');
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (cohort_id, "DATE", variant_id) VALUES ('C7', sysdate, 'brand-name-deals2');
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (cohort_id, "DATE", variant_id) VALUES ('C8', sysdate, 'brand-name-deals2');
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (cohort_id, "DATE", variant_id) VALUES ('C9', sysdate, 'brand-name-deals2');
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (cohort_id, "DATE", variant_id) VALUES ('C10', sysdate, 'brand-name-deals2');
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (cohort_id, "DATE", variant_id) VALUES ('C11', sysdate, 'brand-name-deals2');
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (cohort_id, "DATE", variant_id) VALUES ('C12', sysdate, 'brand-name-deals2');
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (cohort_id, "DATE", variant_id) VALUES ('C13', sysdate, 'brand-name-deals2');
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (cohort_id, "DATE", variant_id) VALUES ('C14', sysdate, 'brand-name-deals2');
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (cohort_id, "DATE", variant_id) VALUES ('C15', sysdate, 'brand-name-deals2');
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (cohort_id, "DATE", variant_id) VALUES ('C16', sysdate, 'brand-name-deals2');
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (cohort_id, "DATE", variant_id) VALUES ('C17', sysdate, 'brand-name-deals2');
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (cohort_id, "DATE", variant_id) VALUES ('C18', sysdate, 'brand-name-deals2');
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (cohort_id, "DATE", variant_id) VALUES ('C19', sysdate, 'brand-name-deals2');
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (COHORT_ID, "DATE", VARIANT_ID) VALUES ('C20', SYSDATE, 'brand-name-deals2');
