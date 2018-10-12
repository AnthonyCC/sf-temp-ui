-- Rollback

-- delete distributions
Delete from CUST.SS_VARIANT_ASSIGNMENT where variant_id='fk_scarab_cart';
Delete from CUST.SS_VARIANT_ASSIGNMENT where variant_id='fk_cart_toprated';
Delete from CUST.SS_VARIANT_ASSIGNMENT where variant_id='fk_cart_most_pop';
Delete from CUST.SS_VARIANT_ASSIGNMENT where variant_id='fk_cart_deals';

-- delete variant config
Delete from CUST.SS_VARIANT_PARAMS where id='fk_scarab_cart';
Delete from CUST.SS_VARIANT_PARAMS where id='fk_cart_toprated';
Delete from CUST.SS_VARIANT_PARAMS where id='fk_cart_most_pop';
Delete from CUST.SS_VARIANT_PARAMS where id='fk_cart_deals';

-- delete variant definitions
Delete from CUST.SS_VARIANTS where id='fk_scarab_cart';
Delete from CUST.SS_VARIANTS where id='fk_cart_toprated';
Delete from CUST.SS_VARIANTS where id='fk_cart_most_pop';
Delete from CUST.SS_VARIANTS where id='fk_cart_deals';

-- delete site features
Delete from CUST.SS_SITE_FEATURE where ID='FK_DYF';
Delete from CUST.SS_SITE_FEATURE where ID='FK_CART_TOPRATED';
Delete from CUST.SS_SITE_FEATURE where ID='FK_CART_MOSTPOP';
Delete from CUST.SS_SITE_FEATURE where ID='FK_CART_DEALS';
