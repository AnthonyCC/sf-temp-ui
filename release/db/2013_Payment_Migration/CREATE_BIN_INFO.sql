
DROP TABLE CUST.BIN_INFO;
 CREATE TABLE CUST.BIN_INFO
(
  ID       VARCHAR2(16 BYTE)                     NOT NULL,
  RECORD_SEQ VARCHAR2(16 BYTE) ,
  VERSION NUMBER(10)                      NOT NULL,
  LOW_RANGE VARCHAR2(16 BYTE),
  HIGH_RANGE VARCHAR2(16 BYTE) ,
  CARD_TYPE VARCHAR2(10 BYTE)
   
);



CREATE UNIQUE INDEX CUST.BIN_INFO_PK ON CUST.BIN_INFO
(ID);


ALTER TABLE CUST.BIN_INFO ADD (
  CONSTRAINT BIN_INFO_PK
 PRIMARY KEY
 (ID)
      );

GRANT DELETE, INSERT, SELECT, UPDATE ON CUST.BIN_INFO TO FDSTORE_PRDA;

GRANT DELETE, INSERT, SELECT, UPDATE ON CUST.BIN_INFO TO FDSTORE_PRDB;