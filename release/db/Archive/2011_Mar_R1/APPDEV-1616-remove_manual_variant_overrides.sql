-- APPDEV-1616
-- Clean up deprecated manual variant overrides

-- To be executed in all environments


delete from cust.profile where profile_name = 'OverrideVariants';
