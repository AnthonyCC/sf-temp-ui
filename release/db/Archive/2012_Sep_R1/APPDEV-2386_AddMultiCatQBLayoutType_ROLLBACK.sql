--
-- Drop augmented Multi Category Layout enum
-- from CMS database
--
-- [APPDEV-2386]
--
delete from CMS.LOOKUP
where CODE='111';
