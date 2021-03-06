DROP TYPE CUST.PROMO_STATE_COUNTY_LIST;

CREATE OR REPLACE TYPE CUST.PROMO_STATE_COUNTY_LIST AS VARRAY(1000)  OF varchar2(30);

GRANT EXECUTE ON CUST.PROMO_STATE_COUNTY_LIST TO FDSTORE_PRDA;

GRANT EXECUTE ON CUST.PROMO_STATE_COUNTY_LIST TO FDSTORE_PRDB;

DROP TABLE CUST.PROMOTION_STATE_COUNTY CASCADE CONSTRAINTS;

CREATE TABLE CUST.PROMOTION_STATE_COUNTY
(
	ID				VARCHAR2(16) PRIMARY KEY,	-- UNIQUE SEQUENCE KEY
	PROMOTION_ID	VARCHAR2(16),			-- PROMOTION ID
	DATE_ADDED		DATE,					-- ROW ADDED DATE
	STATES			CUST.PROMO_STATE_COUNTY_LIST,			-- NY,NJ,CT
	STATE_OPTION	VARCHAR2(1),			-- A - ALLEXCEPT, O - ONLY
	COUNTY			CUST.PROMO_STATE_COUNTY_LIST,			-- NY_ESSEX,COUNTY2,COUNTY3|CT_FAIRFIELD,NEWHAVEN
	COUNTY_OPTION	VARCHAR2(1)				-- A - ALLEXCEPT, O - ONLY
)	

ALTER TABLE CUST.PROMOTION_STATE_COUNTY ADD (
  CONSTRAINT PROMOTION_STATE_COUNTY_FK
 FOREIGN KEY (PROMOTION_ID) 
 REFERENCES CUST.PROMOTION_NEW (ID) );

GRANT DELETE, INSERT, SELECT, UPDATE ON CUST.PROMOTION_STATE_COUNTY TO FDSTORE_PRDA;
 
 GRANT DELETE, INSERT, SELECT, UPDATE ON CUST.PROMOTION_STATE_COUNTY TO FDSTORE_PRDB;
 
-- GRANT ALL ON CUST.PROMOTION_STATE_COUNTY TO fdstore;
-- GRANT READ ON CUST.PROMOTION_STATE_COUNTY TO APPDEV;

