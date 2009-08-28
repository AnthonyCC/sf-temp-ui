CREATE TABLE cust.RM_CANNED_TEXT
(
  ID VARCHAR2(10) NOT NULL,
  NAME VARCHAR2(40) NOT NULL,
  CATEGORY VARCHAR2(15) NOT NULL,
  CANNED_TEXT VARCHAR2(1024) NOT NULL,
  CONSTRAINT CRM_CANNED_TEXT_PK PRIMARY KEY ( ID )
  ENABLE
);


CREATE INDEX CRM_CANNED_TEXT_IDX_CAT ON cust.CRM_CANNED_TEXT (CATEGORY);

CREATE INDEX CRM_CANNED_TEXT_IDX_NAME ON cust.CRM_CANNED_TEXT (NAME);

GRANT SELECT, INSERT, UPDATE, DELETE ON cust.CRM_CANNED_TEXT TO FDSTORE_PRDA;

GRANT SELECT, INSERT, UPDATE, DELETE ON cust.CRM_CANNED_TEXT TO FDSTORE_PRDB;
