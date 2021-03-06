--
-- PROFILE_ATTR_NAME  (Table) 
--
CREATE TABLE "PROFILE_ATTR_NAME"
(
"NAME" VARCHAR2(40) NOT NULL,
"DESCRIPTION" VARCHAR2(80) NOT NULL,
"CATEGORY" VARCHAR2(16),
"ATTR_VALUE_TYPE" VARCHAR2(16),
"IS_EDITABLE" VARCHAR2(255),
CONSTRAINT "PK_PROFILE_ATTR_NAME" PRIMARY KEY ("NAME")
);


GRANT DELETE, INSERT, SELECT, UPDATE on CUST.PROFILE_ATTR_NAME to fdstore_prda;
GRANT DELETE, INSERT, SELECT, UPDATE on CUST.PROFILE_ATTR_NAME to fdstore_prdb;
GRANT SELECT on CUST.PROFILE_ATTR_NAME to appdev;


INSERT INTO CUST.PROFILE_ATTR_NAME (NAME, DESCRIPTION, CATEGORY, IS_EDITABLE) 
SELECT DISTINCT PROFILE_NAME, PROFILE_NAME, 'General', 'X' FROM CUST.PROFILE;

COMMIT;