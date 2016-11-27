-- rollback

DELETE FROM cust.ss_tab_strategy_priority WHERE site_feature_id = 'C_SAVE_COS_FDF';
DELETE FROM cust.ss_tab_strategy_priority WHERE site_feature_id = 'COS_FDF';

DELETE FROM cust.ss_variant_params WHERE id = 'cos-favorites-1_s';
DELETE FROM cust.ss_variant_params WHERE id = 'cos-favorites-1';

DELETE FROM cust.SS_VARIANT_ASSIGNMENT WHERE variant_id = 'cos-favorites-1_s';
DELETE FROM cust.SS_VARIANT_ASSIGNMENT WHERE variant_id = 'cos-favorites-1';

DELETE FROM cust.ss_variants WHERE id = 'cos-favorites-1_s';
DELETE FROM cust.ss_variants WHERE id = 'cos-favorites-1';

DELETE FROM cust.ss_site_feature WHERE id = 'C_SAVE_COS_FDF';
DELETE FROM cust.ss_site_feature WHERE id = 'COS_FDF';


