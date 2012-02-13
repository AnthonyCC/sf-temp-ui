--
-- Description: Rollback script.
--
-- Author: Gabor Sebestyen (gabor.sebestyen@euedge.com)
--
-- JIRA Ref: APPDEV-2241
--
-- Affected Tables: CUST.SS_SITE_FEATURE CUST.SS_VARIANTS CUST.SS_VARIANT_ASSIGNMENT
-- Note: variant cannot be rolled back once a back-reference is created to it.
--


-- Drop new variant
--
delete from CUST.SS_VARIANT_ASSIGNMENT where VARIANT_ID='ymal_pdtl';
delete from CUST.SS_VARIANTS where id='ymal_pdtl';
delete from CUST.SS_SITE_FEATURE where id='YMAL_PDTL';
