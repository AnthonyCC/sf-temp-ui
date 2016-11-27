ALTER TABLE CUST.PROMO_customer ADD customer_email VARCHAR2(80);

ALTER TABLE CUST.PROMO_customer  modify (CUSTOMER_ID  VARCHAR2(16 BYTE)       NULL);

DROP INDEX CUST.PROMO_CUST_PROMO_CUST_IDX;

CREATE UNIQUE INDEX CUST.PROMO_CUST_PROMO_CUST_IDX ON CUST.PROMO_CUSTOMER(PROMOTION_ID, CUSTOMER_ID, customer_email);
