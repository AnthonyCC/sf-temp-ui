--
-- Description: Rollback script to reconfigure brand name deals variant
--
-- Author: Gabor Sebestyen (gabor.sebestyen@euedge.com)
--
-- JIRA Ref: APPDEV-1889
--
-- Affected Tables: CUST.SS_VARIANTS CUST.SS_VARIANT_PARAMS CUST.SS_VARIANT_ASSIGNMENT
-- Note: variant cannot be rollbacked once a reference to it is created.
--


-- Cleanup new variant
--
delete from CUST.SS_VARIANT_ASSIGNMENT where VARIANT_ID in ('brand-name-deals2');
DELETE FROM CUST.SS_VARIANT_PARAMS WHERE ID IN ('brand-name-deals2');
delete from CUST.SS_VARIANTS where id in ('brand-name-deals2');
