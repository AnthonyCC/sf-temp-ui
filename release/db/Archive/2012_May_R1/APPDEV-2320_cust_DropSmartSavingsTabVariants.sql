--
-- This script removes smart saving tab variants
-- from tab strategies so cart'n'tabs no longer
-- offer them
--
-- @author gsebestyen
-- @ticket APPDEV-2320
--

DELETE
FROM CUST.SS_TAB_STRATEGY_PRIORITY STRAT
WHERE STRAT.SITE_FEATURE_ID IN (
  SELECT ID FROM CUST.SS_SITE_FEATURE
  WHERE SMART_SAVING = '1'
);

