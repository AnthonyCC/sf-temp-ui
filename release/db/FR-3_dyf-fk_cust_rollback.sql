-- Rollback

Delete from CUST.SS_VARIANT_ASSIGNMENT where variant_id='fk_scarab_cart';

Delete from CUST.SS_VARIANT_PARAMS where id='fk_scarab_cart';

Delete from CUST.SS_VARIANTS where id='fk_scarab_cart';

Delete from CUST.SS_SITE_FEATURE where ID='DYF_FK';
