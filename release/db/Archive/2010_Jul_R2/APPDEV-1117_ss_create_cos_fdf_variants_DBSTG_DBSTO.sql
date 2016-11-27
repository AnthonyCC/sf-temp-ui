INSERT INTO cust.ss_site_feature (id, title, smart_saving) VALUES ('COS_FDF', 'Corporate FreshDirect Favorites', 0);
INSERT INTO cust.ss_site_feature (id, title, smart_saving) VALUES ('C_SAVE_COS_FDF', 'C''n''T Save on COS FDF', 1);

INSERT INTO cust.ss_variants (id, feature, type) VALUES ('cos-favorites-1', 'COS_FDF', 'scripted');
INSERT INTO cust.ss_variants (id, feature, type, alias_id) VALUES ('cos-favorites-1_s', 'C_SAVE_COS_FDF', 'alias', 'cos-favorites-1');

INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (cohort_id, "DATE", variant_id) VALUES ('C1', sysdate, 'cos-favorites-1');
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (cohort_id, "DATE", variant_id) VALUES ('C2', sysdate, 'cos-favorites-1');
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (cohort_id, "DATE", variant_id) VALUES ('C3', sysdate, 'cos-favorites-1');
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (cohort_id, "DATE", variant_id) VALUES ('C4', sysdate, 'cos-favorites-1');
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (cohort_id, "DATE", variant_id) VALUES ('C5', sysdate, 'cos-favorites-1');
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (cohort_id, "DATE", variant_id) VALUES ('C6', sysdate, 'cos-favorites-1');
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (cohort_id, "DATE", variant_id) VALUES ('C7', sysdate, 'cos-favorites-1');
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (cohort_id, "DATE", variant_id) VALUES ('C8', sysdate, 'cos-favorites-1');
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (cohort_id, "DATE", variant_id) VALUES ('C9', sysdate, 'cos-favorites-1');
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (cohort_id, "DATE", variant_id) VALUES ('C10', sysdate, 'cos-favorites-1');
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (cohort_id, "DATE", variant_id) VALUES ('C11', sysdate, 'cos-favorites-1');
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (cohort_id, "DATE", variant_id) VALUES ('C12', sysdate, 'cos-favorites-1');
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (cohort_id, "DATE", variant_id) VALUES ('C13', sysdate, 'cos-favorites-1');
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (cohort_id, "DATE", variant_id) VALUES ('C14', sysdate, 'cos-favorites-1');
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (cohort_id, "DATE", variant_id) VALUES ('C15', sysdate, 'cos-favorites-1');
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (cohort_id, "DATE", variant_id) VALUES ('C16', sysdate, 'cos-favorites-1');
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (cohort_id, "DATE", variant_id) VALUES ('C17', sysdate, 'cos-favorites-1');
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (cohort_id, "DATE", variant_id) VALUES ('C18', sysdate, 'cos-favorites-1');
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (cohort_id, "DATE", variant_id) VALUES ('C19', sysdate, 'cos-favorites-1');
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (cohort_id, "DATE", variant_id) VALUES ('C20', sysdate, 'cos-favorites-1');

INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (cohort_id, "DATE", variant_id) VALUES ('C1', sysdate, 'cos-favorites-1_s');
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (cohort_id, "DATE", variant_id) VALUES ('C2', sysdate, 'cos-favorites-1_s');
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (cohort_id, "DATE", variant_id) VALUES ('C3', sysdate, 'cos-favorites-1_s');
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (cohort_id, "DATE", variant_id) VALUES ('C4', sysdate, 'cos-favorites-1_s');
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (cohort_id, "DATE", variant_id) VALUES ('C5', sysdate, 'cos-favorites-1_s');
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (cohort_id, "DATE", variant_id) VALUES ('C6', sysdate, 'cos-favorites-1_s');
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (cohort_id, "DATE", variant_id) VALUES ('C7', sysdate, 'cos-favorites-1_s');
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (cohort_id, "DATE", variant_id) VALUES ('C8', sysdate, 'cos-favorites-1_s');
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (cohort_id, "DATE", variant_id) VALUES ('C9', sysdate, 'cos-favorites-1_s');
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (cohort_id, "DATE", variant_id) VALUES ('C10', sysdate, 'cos-favorites-1_s');
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (cohort_id, "DATE", variant_id) VALUES ('C11', sysdate, 'cos-favorites-1_s');
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (cohort_id, "DATE", variant_id) VALUES ('C12', sysdate, 'cos-favorites-1_s');
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (cohort_id, "DATE", variant_id) VALUES ('C13', sysdate, 'cos-favorites-1_s');
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (cohort_id, "DATE", variant_id) VALUES ('C14', sysdate, 'cos-favorites-1_s');
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (cohort_id, "DATE", variant_id) VALUES ('C15', sysdate, 'cos-favorites-1_s');
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (cohort_id, "DATE", variant_id) VALUES ('C16', sysdate, 'cos-favorites-1_s');
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (cohort_id, "DATE", variant_id) VALUES ('C17', sysdate, 'cos-favorites-1_s');
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (cohort_id, "DATE", variant_id) VALUES ('C18', sysdate, 'cos-favorites-1_s');
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (cohort_id, "DATE", variant_id) VALUES ('C19', sysdate, 'cos-favorites-1_s');
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (cohort_id, "DATE", variant_id) VALUES ('C20', sysdate, 'cos-favorites-1_s');

INSERT INTO cust.ss_variant_params (id, key, value) VALUES ('cos-favorites-1', 'prez_title', 'FRESHDIRECT FAVORITES');
INSERT INTO cust.ss_variant_params (id, key, value) VALUES ('cos-favorites-1', 'prez_desc', 'These are just a few of our most-loved products.');
INSERT INTO cust.ss_variant_params (id, key, value) VALUES ('cos-favorites-1', 'generator', 'RecursiveNodes("fd_favs_cos")');
INSERT INTO cust.ss_variant_params (id, key, value) VALUES ('cos-favorites-1', 'scoring', 'Popularity_Discretized;QualityRating_Discretized2');
INSERT INTO cust.ss_variant_params (id, key, value) VALUES ('cos-favorites-1', 'sampling_strat', 'power');
INSERT INTO cust.ss_variant_params (id, key, value) VALUES ('cos-favorites-1', 'exponent', '0.5');
INSERT INTO cust.ss_variant_params (id, key, value) VALUES ('cos-favorites-1', 'top_perc', '100');
INSERT INTO cust.ss_variant_params (id, key, value) VALUES ('cos-favorites-1', 'cos_filter', 'CORPORATE');

INSERT INTO cust.ss_variant_params (id, key, value)
(SELECT 'cos-favorites-1_s', key, value FROM cust.ss_variant_params WHERE id = 'favorites-4_s');

INSERT INTO cust.ss_tab_strategy_priority (tab_strategy_id, site_feature_id, primary_priority, secondary_priority)
(SELECT tab_strategy_id, 'COS_FDF', primary_priority, secondary_priority - 1 FROM cust.ss_tab_strategy_priority WHERE site_feature_id = 'C_SAVE_FDF');

INSERT INTO cust.ss_tab_strategy_priority (tab_strategy_id, site_feature_id, primary_priority, secondary_priority)
(SELECT tab_strategy_id, 'C_SAVE_COS_FDF', primary_priority, secondary_priority - 1 FROM cust.ss_tab_strategy_priority
WHERE site_feature_id = 'COS_FDF');

