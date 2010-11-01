      Create table CUST.MODIFY_ORDERS (SALE_ID varchar2(16) Primary key,
      ERP_CUSTOMER_ID varchar2(16), FIRST_NAME varchar2(55),
      LAST_NAME varchar2(55), EMAIL varchar2(80),
      HOME_PHONE varchar2(15), ALT_PHONE varchar2(15),
      requested_date date,sale_status CHAR(3), status varchar2(15), error_desc varchar2(500), CREATE_DATE DATE );
      
      GRANT INSERT,SELECT,DELETE,UPDATE on  CUST.MODIFY_ORDERS to FDSTORE_PRDA;
      
      GRANT INSERT,SELECT,DELETE,UPDATE on  CUST.MODIFY_ORDERS to FDSTORE_PRDB;
      