-- APPDEV-1758
-- Drop archived flag from variants

-- To be executed in all environments

alter table cust.ss_variants
drop column archived;
