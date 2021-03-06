CREATE TABLE CUST.DLV_PASS_TYPE
(
SKU_CODE VARCHAR2(18) NOT NULL,
NAME VARCHAR2(32) NOT NULL,
NO_OF_DLVS NUMBER(5),
DURATION NUMBER(5),
IS_UNLIMITED CHAR(1) NOT NULL,
CONSTRAINT DLV_PASS_TYPE_PK PRIMARY KEY (SKU_CODE)
);

GRANT select, insert, update , delete on CUST.DLV_PASS_TYPE to FDSTORE_PRDA; 
GRANT select, insert, update , delete on CUST.DLV_PASS_TYPE to FDSTORE_PRDB; 

INSERT INTO CUST.DLV_PASS_TYPE COLUMNS (SKU_CODE, NAME, NO_OF_DLVS, DURATION, IS_UNLIMITED) 
VALUES ('MKT0070536', 'BUY 5 GET 2 FREE', 7, null, 'N');

INSERT INTO CUST.DLV_PASS_TYPE COLUMNS (SKU_CODE, NAME, NO_OF_DLVS, DURATION, IS_UNLIMITED) 
VALUES ('MKT0070537', 'BUY 10 GET 5 FREE', 15, null, 'N');

INSERT INTO CUST.DLV_PASS_TYPE COLUMNS (SKU_CODE, NAME, NO_OF_DLVS, DURATION, IS_UNLIMITED) 
VALUES ('MKT0070538', 'BUY 15 GET 10 FREE', 25, null, 'N');

INSERT INTO CUST.DLV_PASS_TYPE COLUMNS (SKU_CODE, NAME, NO_OF_DLVS, DURATION, IS_UNLIMITED) 
VALUES ('MKT0070539', '6 MONTHS UNLIMITED', null, 183, 'Y');



