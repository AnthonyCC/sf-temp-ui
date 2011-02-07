
INSERT INTO cust.ss_site_feature (id, title, smart_saving) VALUES ('BRAND_NAME_DEALS', 'Brand Name Deals', 0);
INSERT INTO cust.ss_variants (id, feature, type) VALUES ('brand-name-deals', 'BRAND_NAME_DEALS', 'scripted');


INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (cohort_id, "DATE", variant_id) VALUES ('C1', sysdate, 'brand-name-deals');
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (cohort_id, "DATE", variant_id) VALUES ('C2', sysdate, 'brand-name-deals');
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (cohort_id, "DATE", variant_id) VALUES ('C3', sysdate, 'brand-name-deals');
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (cohort_id, "DATE", variant_id) VALUES ('C4', sysdate, 'brand-name-deals');
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (cohort_id, "DATE", variant_id) VALUES ('C5', sysdate, 'brand-name-deals');
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (cohort_id, "DATE", variant_id) VALUES ('C6', sysdate, 'brand-name-deals');
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (cohort_id, "DATE", variant_id) VALUES ('C7', sysdate, 'brand-name-deals');
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (cohort_id, "DATE", variant_id) VALUES ('C8', sysdate, 'brand-name-deals');
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (cohort_id, "DATE", variant_id) VALUES ('C9', sysdate, 'brand-name-deals');
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (cohort_id, "DATE", variant_id) VALUES ('C10', sysdate, 'brand-name-deals');
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (cohort_id, "DATE", variant_id) VALUES ('C11', sysdate, 'brand-name-deals');
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (cohort_id, "DATE", variant_id) VALUES ('C12', sysdate, 'brand-name-deals');
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (cohort_id, "DATE", variant_id) VALUES ('C13', sysdate, 'brand-name-deals');
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (cohort_id, "DATE", variant_id) VALUES ('C14', sysdate, 'brand-name-deals');
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (cohort_id, "DATE", variant_id) VALUES ('C15', sysdate, 'brand-name-deals');
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (cohort_id, "DATE", variant_id) VALUES ('C16', sysdate, 'brand-name-deals');
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (cohort_id, "DATE", variant_id) VALUES ('C17', sysdate, 'brand-name-deals');
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (cohort_id, "DATE", variant_id) VALUES ('C18', sysdate, 'brand-name-deals');
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (cohort_id, "DATE", variant_id) VALUES ('C19', sysdate, 'brand-name-deals');
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (cohort_id, "DATE", variant_id) VALUES ('C20', sysdate, 'brand-name-deals');


INSERT INTO cust.ss_variant_params (id, key, value) VALUES ('brand-name-deals', 'generator', 'FeaturedItems:atLeast(HighestDealsPercentage, 0.05):prioritize() + RecursiveNodes(currentNode):atLeast(HighestDealsPercentage, 0.05)');
INSERT INTO cust.ss_variant_params (id, key, value) VALUES ('brand-name-deals', 'scoring', 'HighestDealsPercentage');
INSERT INTO cust.ss_variant_params (id, key, value) VALUES ('brand-name-deals', 'sampling_strat', 'deterministic');
INSERT INTO cust.ss_variant_params (id, key, value) VALUES ('brand-name-deals', 'exponent', '0.5');
INSERT INTO cust.ss_variant_params (id, key, value) VALUES ('brand-name-deals', 'top_perc', '10');
INSERT INTO cust.ss_variant_params (id, key, value) VALUES ('brand-name-deals', 'top_n', '20');
INSERT INTO cust.ss_variant_params (id, key, value) VALUES ('brand-name-deals', 'fi_label', 'Brand Name Deals');
INSERT INTO cust.ss_variant_params (id, key, value) VALUES ('brand-name-deals', 'brand_uniq_sort', 'true');

