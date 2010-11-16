-- Rollback
Delete from CUST.SS_VARIANT_PARAMS where ID = 'sc_also_bought';
Delete from CUST.SS_VARIANT_PARAMS where ID = 'sc_also_viewed';

Delete from CUST.SS_VARIANTS where ID = 'sc_also_bought';
Delete from CUST.SS_VARIANTS where ID = 'sc_also_viewed';
