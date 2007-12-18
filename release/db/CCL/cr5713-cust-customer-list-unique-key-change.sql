
--- This script fixes up the CUSTOMERLIST table, to make sure that 
--- list names for each type are unique for each user.

ALTER TABLE CUST.CUSTOMERLIST DROP CONSTRAINT UNQ_NAME_ID;
ALTER TABLE CUST.CUSTOMERLIST ADD CONSTRAINT UNQ_NAME_TYPE_ID UNIQUE (name, customer_id, type);
