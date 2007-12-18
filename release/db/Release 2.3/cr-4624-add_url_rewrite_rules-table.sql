CREATE TABLE URL_REWRITE_RULES
(
  ID        VARCHAR2(16 BYTE)                   NOT NULL,
  NAME      VARCHAR2(64 BYTE)                   NOT NULL,
  DISABLED  VARCHAR2(1 BYTE),
  FROM_URL  VARCHAR2(256 BYTE)                  NOT NULL,
  REDIRECT  VARCHAR2(256 BYTE)                  NOT NULL,
  COMMENTS  VARCHAR2(512 BYTE),
  OPTIONS   VARCHAR2(64 BYTE),
  PRIORITY  NUMBER(5),
  CONSTRAINT PK_URL_REWRITE_RULES PRIMARY KEY (ID)
);

GRANT DELETE, INSERT, SELECT, UPDATE on CUST.URL_REWRITE_RULES to fdstore_prda;
GRANT DELETE, INSERT, SELECT, UPDATE on CUST.URL_REWRITE_RULES to fdstore_prdb;
GRANT SELECT on CUST.URL_REWRITE_RULES to appdev;
