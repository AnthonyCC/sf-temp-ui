DROP TABLE CUST.PROMO_DOLLAR_DISCOUNT;

CREATE TABLE CUST.PROMO_DOLLAR_DISCOUNT
(
	ID				VARCHAR2(16) PRIMARY KEY,	-- UNIQUE SEQUENCE KEY
	PROMOTION_ID	VARCHAR2(16),			-- PROMOTION ID
	DATE_ADDED		DATE,					-- ROW ADDED DATE
	DOLLAR_OFF		NUMBER,
	ORDER_TOTAL		NUMBER
)
/

ALTER TABLE CUST.PROMO_DOLLAR_DISCOUNT ADD (
  CONSTRAINT PROMO_DOLLAR_DISCOUNT_FK
 FOREIGN KEY (PROMOTION_ID) 
 REFERENCES CUST.PROMOTION_NEW (ID) );
 
ALTER TABLE CUST.PROMO_DOLLAR_DISCOUNT
add CONSTRAINT PROMO_DOLLAR_DISCOUNT_C1 UNIQUE (PROMOTION_ID, DOLLAR_OFF, ORDER_TOTAL);

GRANT DELETE, INSERT, SELECT, UPDATE ON CUST.PROMO_DOLLAR_DISCOUNT TO FDSTORE_PRDA;
 
GRANT DELETE, INSERT, SELECT, UPDATE ON CUST.PROMO_DOLLAR_DISCOUNT TO FDSTORE_STPRD01;
 
GRANT SELECT ON CUST.PROMOTION_STATE_COUNTY TO APPDEV;

