create table CUSTOMER_REQUEST (
  ID			VARCHAR2(16),
  REQUEST_DATE		DATE,
  CASE_SUBJECT_CODE	VARCHAR2(8),  /* Mapped to CRM case/subject ... since our customer request page already points to it */
  CUSTOMER_ID		VARCHAR2(16),
  CUSTOMER_EMAIL	VARCHAR2(64),
  LOGGED_BY		VARCHAR2(64),
  SUBJECT       	VARCHAR2(64),
  INFO_LINE1  		VARCHAR2(256),
  INFO_LINE2  		VARCHAR2(256),
  INFO_LINE3  		VARCHAR2(256),
  INFO_LINE4  		VARCHAR2(256),
  INFO_LINE5  		VARCHAR2(256),
  INFO_LINE6  		VARCHAR2(256),
  INFO_LINE7  		VARCHAR2(256),
  INFO_LINE8  		VARCHAR2(256),
  INFO_LINE9  		VARCHAR2(256),
  INFO_OTHER		VARCHAR2(512)
);


grant select, insert, update, delete on CUSTOMER_REQUEST to FDSTORE_PRDA;

grant select, insert, update, delete on CUSTOMER_REQUEST to FDSTORE_PRDB;

/* Create grants and synonyms */
