 CREATE TABLE CUST.BIN_HISTORY
(
  VERSION       NUMBER(10)                      NOT NULL,
  DATE_CREATED  DATE,
  STATUS        VARCHAR2(10 BYTE)
);

desc cust.salesaction

CREATE UNIQUE INDEX CUST.BIN_HISTORY_PK ON CUST.BIN_HISTORY
(VERSION);


ALTER TABLE CUST.BIN_HISTORY ADD (
  CONSTRAINT BIN_HISTORY_PK
 PRIMARY KEY
 (VERSION)
      );

GRANT DELETE, INSERT, SELECT, UPDATE ON CUST.BIN_HISTORY TO FDSTORE_PRDA;

GRANT DELETE, INSERT, SELECT, UPDATE ON CUST.BIN_HISTORY TO FDSTORE_PRDB;