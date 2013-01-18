--
-- Add missing CUST.PROFILE_ATTR_NAME entries
--
INSERT INTO CUST.PROFILE_ATTR_NAME (NAME, DESCRIPTION, CATEGORY, IS_EDITABLE) 
VALUES ('siteFeature.newSearch', 'siteFeature.newSearch', 'General', 'X');

INSERT INTO CUST.PROFILE_ATTR_NAME (NAME, DESCRIPTION, CATEGORY, IS_EDITABLE) 
VALUES ('ManualRetention-TEN4A', 'ManualRetention-TEN4A', 'General', 'X');

--
-- Remove obsolete records
--
delete from cust.profile p
where not exists (select 1 from cust.profile_attr_name where name=p.profile_name);
-- Expected: STG 2, PRD 0

delete from cust.profile_attr_name pan
where not exists (select 1 from cust.profile where profile_name=pan.name);
-- Expected: STG 5, PRD 4

--
-- allow null in obsolete profile_type column (will drop post-migration)
--
alter table cust.profile modify (profile_type varchar2(1));

--
-- enforce PK constraint
--
ALTER TABLE CUST.PROFILE ADD (
  CONSTRAINT PK_PROFILE PRIMARY KEY (CUSTOMER_ID, PROFILE_NAME));


--
-- enforce referential integrity
--
ALTER TABLE CUST.PROFILE ADD (
  CONSTRAINT FK_PROFILE_NAME FOREIGN KEY (PROFILE_NAME) 
    REFERENCES CUST.PROFILE_ATTR_NAME (NAME));

