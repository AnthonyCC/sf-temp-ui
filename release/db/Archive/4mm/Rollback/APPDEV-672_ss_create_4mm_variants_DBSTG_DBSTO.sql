-- rollback

DELETE FROM cust.ss_variant_params WHERE id = 'prod-grp-yf-1';
DELETE FROM cust.ss_variant_params WHERE id = 'prod-grp-pop-1';

DELETE FROM cust.SS_VARIANT_ASSIGNMENT WHERE variant_id = 'prod-grp-yf-1';
DELETE FROM cust.SS_VARIANT_ASSIGNMENT WHERE variant_id = 'prod-grp-pop-1';

DELETE FROM cust.ss_variants WHERE id = 'prod-grp-yf-1';
DELETE FROM cust.ss_variants WHERE id = 'prod-grp-pop-1';

DELETE FROM cust.ss_site_feature WHERE id = 'PROD_GRP_YF';
DELETE FROM cust.ss_site_feature WHERE id = 'PROD_GRP_POPULAR';


