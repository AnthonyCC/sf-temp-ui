-- APPDEV-1758
-- Add archived flag to variants

-- To be executed in all environments

alter table cust.ss_variants
add archived char(1) default 'N' 
NOT NULL check (archived in ( 'Y', 'N' ));
