--
-- POST-MIGRATION (once code in both clusters is in sync)
--

-- drop obsolete columns
alter table cust.profile drop (profile_type, priority);
