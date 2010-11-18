-- Rollback

-- Removing Variant params for alsoBough, alsoViewed, related
Delete from CUST.SS_VARIANT_PARAMS where ID = 'sc_also_bought';
Delete from CUST.SS_VARIANT_PARAMS where ID = 'sc_also_viewed';
Delete from CUST.SS_VARIANT_PARAMS where ID = 'sc_related_new';

-- Removing alsoBought, alsoViewed, related Variants for YMAL
Delete from CUST.SS_VARIANTS where ID = 'sc_also_bought';
Delete from CUST.SS_VARIANTS where ID = 'sc_also_viewed';
Delete from CUST.SS_VARIANTS where ID = 'sc_related_new';

-- Removing Scarab Cart recommender Variant from Scarab Cart Recommender Site feature
Delete from CUST.SS_VARIANTS where ID = 'sc_cart_recommender';

-- Removing Scarab Cart Recommender Variant Parameters
Delete from CUST.SS_VARIANT_PARAMS where ID = 'sc_cart_recommender';

-- Removing Scarab Cart recommender Cohort assignments
Delete from CUST.SS_VARIANT_ASSIGNMENT where variant_id = 'sc_cart_recommender';

-- Removing Scarab Cart recommender Site feature
Delete from CUST.SS_SITE_FEATURE where id = 'SCARAB_CART';
