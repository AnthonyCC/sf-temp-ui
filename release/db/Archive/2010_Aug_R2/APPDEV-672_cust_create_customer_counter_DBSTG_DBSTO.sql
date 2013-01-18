CREATE TABLE CUST.COUNTERS
(
  ID VARCHAR2(16) NOT NULL,
  CUSTOMER_ID VARCHAR2(16) NOT NULL,
  COUNTER_ID VARCHAR2(32) NOT NULL,
  VALUE NUMBER(4, 0) NOT NULL
, CONSTRAINT COUNTERS_PK PRIMARY KEY
  (
    ID
  )
  ENABLE
)
;

grant select, insert, delete, update on CUST.COUNTERS to FDSTORE_PRDA;
grant select, insert, delete, update on CUST.COUNTERS to FDSTORE_PRDB;
grant select, insert, delete, update on CUST.COUNTERS to appdev;

-- rollback
-- drop table cust.counters;
