-- Rollback

-- delete distributions
Delete from CUST.SS_VARIANT_ASSIGNMENT where variant_id='fk_cart_ymal';

-- delete variant config
Delete from CUST.SS_VARIANT_PARAMS where id='fk_cart_ymal';

-- delete variant definitions
Delete from CUST.SS_VARIANTS where id='fk_cart_ymal';

-- delete site features
Delete from CUST.SS_SITE_FEATURE where ID='FK_CART_YMAL';
