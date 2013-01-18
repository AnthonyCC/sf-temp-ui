--
-- Delete old survey data
--
delete from cust.profile where profile_name='question4_where';
-- 67112 records removed
commit;

delete from cust.profile where profile_name='question5_cuisine';
-- 67112 records removed
commit;

delete from cust.profile where profile_name='question6_diet';
-- 67112 records removed
commit;

delete from cust.profile where profile_name='question7_household';
-- 67112 records removed
commit;

delete from cust.profile where profile_name='question8_cooking';
-- 67112 records removed
commit;
