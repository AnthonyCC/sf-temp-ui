set echo on
set serveroutput on
set pagesize 1000 linesize 250

-- Add new column to store received PayPal Transaction ID
ALTER TABLE CUST.PAYMENT ADD (EWALLET_TX_ID VARCHAR2(100));

ALTER TABLE CUST.PAYMENTINFO_NEW ADD (PAYPAL_ACCOUNT_ID	VARCHAR2(100));

ALTER TABLE CUST.PAYMENTINFO_NEW ADD (DEVICE_ID	VARCHAR2(100));

ALTER TABLE CUST.PAYMENTMETHOD_NEW ADD (PAYPAL_ACCOUNT_ID VARCHAR2(100));

ALTER TABLE CUST.PAYMENTMETHOD_NEW ADD (DEVICE_ID VARCHAR2(100));

--Rebuild objects related to Ingrian:

--Paymentinfo views:
CREATE OR REPLACE FORCE VIEW CUST.PAYMENTINFO_IDV (NAME, ACCOUNT_NUMBER, 
EXPIRATION_DATE, CARD_TYPE, ADDRESS1, ADDRESS2, APARTMENT, CITY,
STATE, ZIP_CODE, COUNTRY, SALESACTION_ID, BILLING_REF, ON_FD_ACCOUNT, 
REFERENCED_ORDER, ABA_ROUTE_NUMBER, PAYMENT_METHOD_TYPE, BANK_NAME, 
BANK_ACCOUNT_TYPE, ING_ROW_ID, PROFILE_ID, ACCOUNT_NUM_MASKED, 
BEST_NUM_BILLING_INQ, EWALLET_ID, VENDOR_EWALLET_ID, EWALLET_TX_ID, 
PAYPAL_ACCOUNT_ID, DEVICE_ID) 
AS
  SELECT NAME,
	  INGRIAN.IngFastDecryptOp (ACCOUNT_NUMBER_NEW,
				    'CUST',
				    'PAYMENTINFO_NEW',
				    'ACCOUNT_NUMBER_NEW',
				    '7AE223F3668C4C9CE2994B04E9774304')
	     ACCOUNT_NUMBER,
	  EXPIRATION_DATE,
	  CARD_TYPE,
	  ADDRESS1,
	  ADDRESS2,
	  APARTMENT,
	  CITY,
	  STATE,
	  ZIP_CODE,
	  COUNTRY,
	  SALESACTION_ID,
	  BILLING_REF,
	  ON_FD_ACCOUNT,
	  REFERENCED_ORDER,
	  ABA_ROUTE_NUMBER,
	  PAYMENT_METHOD_TYPE,
	  BANK_NAME,
	  BANK_ACCOUNT_TYPE,
	  ING_ROW_ID,
	  PROFILE_ID,
	  ACCOUNT_NUM_MASKED,
	  BEST_NUM_BILLING_INQ,
	  EWALLET_ID,
	  VENDOR_EWALLET_ID,
	  EWALLET_TX_ID,
	  PAYPAL_ACCOUNT_ID, 
	  DEVICE_ID
     FROM CUST.PAYMENTINFO_NEW;
     
CREATE OR REPLACE FORCE VIEW CUST.PAYMENTINFO (NAME, ACCOUNT_NUMBER, 
EXPIRATION_DATE, CARD_TYPE, ADDRESS1, ADDRESS2, APARTMENT, CITY, 
STATE, ZIP_CODE, COUNTRY, SALESACTION_ID, BILLING_REF, ON_FD_ACCOUNT, 
REFERENCED_ORDER, ABA_ROUTE_NUMBER, PAYMENT_METHOD_TYPE, BANK_NAME, 
BANK_ACCOUNT_TYPE, PROFILE_ID, ACCOUNT_NUM_MASKED, BEST_NUM_BILLING_INQ, 
EWALLET_ID, VENDOR_EWALLET_ID, EWALLET_TX_ID, PAYPAL_ACCOUNT_ID, DEVICE_ID) 
AS
  SELECT NAME,
	  ACCOUNT_NUMBER,
	  EXPIRATION_DATE,
	  CARD_TYPE,
	  ADDRESS1,
	  ADDRESS2,
	  APARTMENT,
	  CITY,
	  STATE,
	  ZIP_CODE,
	  COUNTRY,
	  SALESACTION_ID,
	  BILLING_REF,
	  ON_FD_ACCOUNT,
	  REFERENCED_ORDER,
	  ABA_ROUTE_NUMBER,
	  PAYMENT_METHOD_TYPE,
	  BANK_NAME,
	  BANK_ACCOUNT_TYPE,
	  PROFILE_ID,
	  ACCOUNT_NUM_MASKED,
	  BEST_NUM_BILLING_INQ,
	  EWALLET_ID,
      VENDOR_EWALLET_ID,
	  EWALLET_TX_ID,
	  PAYPAL_ACCOUNT_ID, 
	  DEVICE_ID
     FROM CUST.PAYMENTINFO_IDV;

--paymentinfo triggers:
CREATE 
OR 
replace TRIGGER CUST.PAYMENTINFO_INS_TRIG instead OF 
INSERT 
ON CUST.PAYMENTINFO_IDV 
REFERENCING NEW AS NEW OLD AS OLD 
FOR EACH ROW 
DECLARE check_21 VARCHAR2(25); 
BEGIN 
  SELECT :NEW.ACCOUNT_NUMBER 
  INTO   check_21 
  FROM   dual; 
   
  INSERT INTO CUST.PAYMENTINFO_NEW 
              ( 
                          NAME, 
                          ACCOUNT_NUMBER, 
                          ACCOUNT_NUMBER_NEW, 
                          EXPIRATION_DATE, 
                          CARD_TYPE, 
                          ADDRESS1, 
                          ADDRESS2, 
                          APARTMENT, 
                          CITY, 
                          STATE, 
                          ZIP_CODE, 
                          COUNTRY, 
                          SALESACTION_ID, 
                          BILLING_REF, 
                          ON_FD_ACCOUNT, 
                          REFERENCED_ORDER, 
                          ABA_ROUTE_NUMBER, 
                          PAYMENT_METHOD_TYPE, 
                          BANK_NAME, 
                          BANK_ACCOUNT_TYPE, 
                          ING_ROW_ID, 
                          PROFILE_ID, 
                          ACCOUNT_NUM_MASKED, 
                          BEST_NUM_BILLING_INQ, 
                          EWALLET_ID, 
                          VENDOR_EWALLET_ID, 
                          EWALLET_TX_ID,
                          PAYPAL_ACCOUNT_ID, 
                          DEVICE_ID
              ) 
              VALUES 
              ( 
                          :NEW.NAME, 
                          ' ', 
                          ingrian.ingfastencryptvarcharbyname(:NEW.ACCOUNT_NUMBER, 'CUST', 'PAYMENTINFO', 'ACCOUNT_NUMBER', '7AE223F3668C4C9CE2994B04E9774304'),
                          :NEW.EXPIRATION_DATE, 
                          :NEW.CARD_TYPE, 
                          :NEW.ADDRESS1, 
                          :NEW.ADDRESS2, 
                          :NEW.APARTMENT, 
                          :NEW.CITY, 
                          :NEW.STATE, 
                          :NEW.ZIP_CODE, 
                          :NEW.COUNTRY, 
                          :NEW.SALESACTION_ID, 
                          :NEW.BILLING_REF, 
                          :NEW.ON_FD_ACCOUNT, 
                          :NEW.REFERENCED_ORDER, 
                          :NEW.ABA_ROUTE_NUMBER, 
                          :NEW.PAYMENT_METHOD_TYPE, 
                          :NEW.BANK_NAME, 
                          :NEW.BANK_ACCOUNT_TYPE, 
                          CUST.ing_seq_21.NEXTVAL, 
                          :NEW.PROFILE_ID, 
                          :NEW.ACCOUNT_NUM_MASKED, 
                          :NEW.BEST_NUM_BILLING_INQ, 
                          :NEW.EWALLET_ID, 
                          :NEW.VENDOR_EWALLET_ID, 
                          :NEW.EWALLET_TX_ID,
                          :NEW.PAYPAL_ACCOUNT_ID, 
                          :NEW.DEVICE_ID
              ); 

END; 
/

show errors trigger cust.paymentinfo_ins_trig;

ALTER TRIGGER CUST.PAYMENTINFO_INS_TRIG ENABLE;

CREATE 
OR 
replace TRIGGER CUST.PAYMENTINFO_UPD_TRIG instead OF 
UPDATE 
ON CUST.PAYMENTINFO_IDV 
REFERENCING NEW AS NEW OLD AS OLD 
FOR EACH ROW 
DECLARE check_21 VARCHAR2(25);
BEGIN 
  SELECT :NEW.ACCOUNT_NUMBER 
  INTO   check_21 
  FROM   dual; 
   
  UPDATE CUST.PAYMENTINFO_NEW 
  SET    NAME = :NEW.NAME, 
         ACCOUNT_NUMBER_NEW = decode (:NEW.ACCOUNT_NUMBER , 
                                        :OLD.ACCOUNT_NUMBER , ACCOUNT_NUMBER_NEW , 
                                        ingrian.ingfastencryptvarcharbyname(:NEW.ACCOUNT_NUMBER, 'CUST', 'PAYMENTINFO ', 'ACCOUNT_NUMBER', '7AE223F3668C4C9CE2994B04E9774304')), 
         EXPIRATION_DATE = :NEW.EXPIRATION_DATE , 
         CARD_TYPE = :NEW.CARD_TYPE , 
         ADDRESS1 = :NEW.ADDRESS1 , 
         ADDRESS2 = :NEW.ADDRESS2 , 
         APARTMENT = :NEW.APARTMENT , 
         CITY = :NEW.CITY , 
         STATE = :NEW.STATE , 
         ZIP_CODE = :NEW.ZIP_CODE , 
         COUNTRY = :NEW.COUNTRY , 
         SALESACTION_ID = :NEW.SALESACTION_ID , 
         BILLING_REF = :NEW.BILLING_REF , 
         ON_FD_ACCOUNT = :NEW.ON_FD_ACCOUNT , 
         REFERENCED_ORDER = :NEW.REFERENCED_ORDER , 
         ABA_ROUTE_NUMBER = :NEW.ABA_ROUTE_NUMBER , 
         PAYMENT_METHOD_TYPE = :NEW.PAYMENT_METHOD_TYPE , 
         BANK_NAME = :NEW.BANK_NAME , 
         BANK_ACCOUNT_TYPE = :NEW.BANK_ACCOUNT_TYPE , 
         PROFILE_ID = :NEW.PROFILE_ID , 
         ACCOUNT_NUM_MASKED = :NEW.ACCOUNT_NUM_MASKED , 
         BEST_NUM_BILLING_INQ = :NEW.BEST_NUM_BILLING_INQ, 
         EWALLET_ID = :NEW.EWALLET_ID, 
         VENDOR_EWALLET_ID = :NEW.VENDOR_EWALLET_ID , 
         EWALLET_TX_ID = :NEW.EWALLET_TX_ID,
         PAYPAL_ACCOUNT_ID = :NEW.PAYPAL_ACCOUNT_ID, 
         DEVICE_ID = :NEW.DEVICE_ID
  WHERE  ing_row_id = :OLD.ing_row_id; 

END;
/

show errors trigger CUST.PAYMENTINFO_UPD_TRIG;

ALTER TRIGGER CUST.PAYMENTINFO_UPD_TRIG ENABLE;


--paymentmethod views:
CREATE OR REPLACE FORCE VIEW CUST.PAYMENTMETHOD_IDV (ID, CUSTOMER_ID,
NAME, ACCOUNT_NUMBER, EXPIRATION_DATE, CARD_TYPE, ADDRESS1, ADDRESS2, 
APARTMENT, CITY, STATE, ZIP_CODE, COUNTRY, ABA_ROUTE_NUMBER, 
PAYMENT_METHOD_TYPE, BANK_NAME, BANK_ACCOUNT_TYPE, ING_ROW_ID, AVS_FAILED,
BYPASS_AVS_CHECK, PROFILE_ID, ACCOUNT_NUM_MASKED, BEST_NUM_BILLING_INQ,
EWALLET_ID, VENDOR_EWALLET_ID, EWALLET_TX_ID, PAYPAL_ACCOUNT_ID, DEVICE_ID) AS
  SELECT ID,
	  CUSTOMER_ID,
	  NAME,
	  INGRIAN.IngFastDecryptOp (ACCOUNT_NUMBER_NEW,
				    'CUST',
				    'PAYMENTMETHOD_NEW',
				    'ACCOUNT_NUMBER_NEW',
				    '7283BFD019B88C08DA3398BFECF45C8E') ACCOUNT_NUMBER,
	  EXPIRATION_DATE,
	  CARD_TYPE,
	  ADDRESS1,
	  ADDRESS2,
	  APARTMENT,
	  CITY,
	  STATE,
	  ZIP_CODE,
	  COUNTRY,
	  ABA_ROUTE_NUMBER,
	  PAYMENT_METHOD_TYPE,
	  BANK_NAME,
	  BANK_ACCOUNT_TYPE,
	  ING_ROW_ID,
	  AVS_FAILED,
	  BYPASS_AVS_CHECK,
	  PROFILE_ID,
	  ACCOUNT_NUM_MASKED,
	  BEST_NUM_BILLING_INQ,
	  EWALLET_ID,
	  VENDOR_EWALLET_ID,
	  EWALLET_TX_ID,
	  PAYPAL_ACCOUNT_ID, 
	  DEVICE_ID
     FROM CUST.PAYMENTMETHOD_NEW;
     
CREATE OR REPLACE FORCE VIEW CUST.PAYMENTMETHOD (ID, CUSTOMER_ID, NAME, 
ACCOUNT_NUMBER, EXPIRATION_DATE, CARD_TYPE, ADDRESS1, ADDRESS2, 
APARTMENT, CITY, STATE, ZIP_CODE, COUNTRY, ABA_ROUTE_NUMBER, 
PAYMENT_METHOD_TYPE, BANK_NAME, BANK_ACCOUNT_TYPE, AVS_FAILED, 
BYPASS_AVS_CHECK, PROFILE_ID, ACCOUNT_NUM_MASKED, BEST_NUM_BILLING_INQ, 
EWALLET_ID, VENDOR_EWALLET_ID, EWALLET_TX_ID, PAYPAL_ACCOUNT_ID, DEVICE_ID) 
AS
  SELECT ID,
	  CUSTOMER_ID,
	  NAME,
	  ACCOUNT_NUMBER,
	  EXPIRATION_DATE,
	  CARD_TYPE,
	  ADDRESS1,
	  ADDRESS2,
	  APARTMENT,
	  CITY,
	  STATE,
	  ZIP_CODE,
	  COUNTRY,
	  ABA_ROUTE_NUMBER,
	  PAYMENT_METHOD_TYPE,
	  BANK_NAME,
	  BANK_ACCOUNT_TYPE,
	  AVS_FAILED,
	  BYPASS_AVS_CHECK,
	  PROFILE_ID,
	  ACCOUNT_NUM_MASKED,
	  BEST_NUM_BILLING_INQ,
	  EWALLET_ID,
	  VENDOR_EWALLET_ID,
	  EWALLET_TX_ID,
	  PAYPAL_ACCOUNT_ID, 
	  DEVICE_ID
     FROM CUST.PAYMENTMETHOD_IDV;
     

--paymentmethod triggers
CREATE 
OR 
replace TRIGGER CUST.PAYMENTMETHOD_INS_TRIG instead OF 
INSERT 
ON CUST.PAYMENTMETHOD_IDV 
REFERENCING NEW AS NEW OLD AS OLD 
FOR EACH ROW 
DECLARE 
  check_41 VARCHAR2(25); 
BEGIN 
  SELECT :NEW.ACCOUNT_NUMBER 
  INTO   check_41 
  FROM   dual; 
   
  INSERT INTO CUST.PAYMENTMETHOD_NEW 
              ( 
                          ID, 
                          CUSTOMER_ID, 
                          NAME, 
                          ACCOUNT_NUMBER, 
                          ACCOUNT_NUMBER_NEW, 
                          EXPIRATION_DATE, 
                          CARD_TYPE, 
                          ADDRESS1, 
                          ADDRESS2, 
                          APARTMENT, 
                          CITY, 
                          STATE, 
                          ZIP_CODE, 
                          COUNTRY, 
                          ABA_ROUTE_NUMBER, 
                          PAYMENT_METHOD_TYPE, 
                          BANK_NAME, 
                          BANK_ACCOUNT_TYPE, 
                          ING_ROW_ID, 
                          AVS_FAILED, 
                          BYPASS_AVS_CHECK, 
                          PROFILE_ID, 
                          ACCOUNT_NUM_MASKED, 
                          BEST_NUM_BILLING_INQ, 
                          EWALLET_ID, 
                          VENDOR_EWALLET_ID, 
                          EWALLET_TX_ID,
                          PAYPAL_ACCOUNT_ID, 
	                  DEVICE_ID
              ) 
              VALUES 
              ( 
                          :NEW.ID, 
                          :NEW.CUSTOMER_ID, 
                          :NEW.NAME, 
                          ' ', 
                          ingrian.ingfastencryptvarcharbyname(:NEW.ACCOUNT_NUMBER, 'CUST', 'PAYMENTMETHOD', 'ACCOUNT_NUMBER', '7283BFD019B88C08DA3398BFECF45C8E'),
                          :NEW.EXPIRATION_DATE, 
                          :NEW.CARD_TYPE, 
                          :NEW.ADDRESS1, 
                          :NEW.ADDRESS2, 
                          :NEW.APARTMENT, 
                          :NEW.CITY, 
                          :NEW.STATE, 
                          :NEW.ZIP_CODE, 
                          :NEW.COUNTRY, 
                          :NEW.ABA_ROUTE_NUMBER, 
                          :NEW.PAYMENT_METHOD_TYPE, 
                          :NEW.BANK_NAME, 
                          :NEW.BANK_ACCOUNT_TYPE, 
                          CUST.ing_seq_41.NEXTVAL, 
                          :NEW.AVS_FAILED, 
                          :NEW.BYPASS_AVS_CHECK, 
                          :NEW.PROFILE_ID, 
                          :NEW.ACCOUNT_NUM_MASKED, 
                          :NEW.BEST_NUM_BILLING_INQ, 
                          :NEW.EWALLET_ID, 
                          :NEW.VENDOR_EWALLET_ID, 
                          :NEW.EWALLET_TX_ID,
                          :NEW.PAYPAL_ACCOUNT_ID, 
	                  :NEW.DEVICE_ID
              ); 

END;
/

show errors trigger CUST.PAYMENTMETHOD_INS_TRIG;

ALTER TRIGGER CUST.PAYMENTMETHOD_INS_TRIG ENABLE;

CREATE 
OR 
replace TRIGGER CUST.PAYMENTMETHOD_UPD_TRIG instead OF 
UPDATE 
ON CUST.PAYMENTMETHOD_IDV 
REFERENCING NEW AS NEW OLD AS OLD FOR EACH ROW 
DECLARE 
  check_41 VARCHAR2(25);
BEGIN 
  SELECT :NEW.ACCOUNT_NUMBER 
  INTO   check_41 
  FROM   dual; 
   
  UPDATE CUST.PAYMENTMETHOD_NEW 
  SET    ID = :NEW.ID , 
         CUSTOMER_ID = :NEW.CUSTOMER_ID , 
         NAME = :NEW.NAME , 
         ACCOUNT_NUMBER_NEW = decode (:NEW.ACCOUNT_NUMBER , 
                                        :OLD.ACCOUNT_NUMBER , ACCOUNT_NUMBER_NEW , 
                                        ingrian.ingfastencryptvarcharbyname(:NEW.ACCOUNT_NUMBER, 'CUST', 'PAYMENTMETHO D', 'ACCOUNT_NUMBER', '7283BFD019B88C08DA3398BFECF45C8E') ) , 
         EXPIRATION_DATE = :NEW.EXPIRATION_DATE , 
         CARD_TYPE = :NEW.CARD_TYPE , 
         ADDRESS1 = :NEW.ADDRESS1 , 
         ADDRESS2 = :NEW.ADDRESS2 , 
         APARTMENT = :NEW.APARTMENT , 
         CITY = :NEW.CITY , 
         STATE = :NEW.STATE , 
         ZIP_CODE = :NEW.ZIP_CODE , 
         COUNTRY = :NEW.COUNTRY , 
         ABA_ROUTE_NUMBER = :NEW.ABA_ROUTE_NUMBER , 
         PAYMENT_METHOD_TYPE = :NEW.PAYMENT_METHOD_TYPE , 
         BANK_NAME = :NEW.BANK_NAME , 
         BANK_ACCOUNT_TYPE = :NEW.BANK_ACCOUNT_TYPE , 
         AVS_FAILED = :NEW.AVS_FAILED , 
         BYPASS_AVS_CHECK = :NEW.BYPASS_AVS_CHECK , 
         PROFILE_ID = :NEW.PROFILE_ID , 
         ACCOUNT_NUM_MASKED = :NEW.ACCOUNT_NUM_MASKED , 
         BEST_NUM_BILLING_INQ = :NEW.BEST_NUM_BILLING_INQ, 
         EWALLET_ID = :NEW.EWALLET_ID, 
         VENDOR_EWALLET_ID = :NEW.VENDOR_EWALLET_ID , 
         EWALLET_TX_ID = :NEW.EWALLET_TX_ID,
         PAYPAL_ACCOUNT_ID = :NEW.PAYPAL_ACCOUNT_ID, 
	 DEVICE_ID = :NEW.DEVICE_ID
  WHERE  ing_row_id = :OLD.ing_row_id; 

END;
/

show errors trigger CUST.PAYMENTMETHOD_UPD_TRIG;

ALTER TRIGGER CUST.PAYMENTMETHOD_UPD_TRIG ENABLE;

-- Insert Into EWALLET table
INSERT INTO CUST.EWALLET(ID, EWALLET_TYPE, EWALLET_STATUS,EWALLET_VERIFY,EWALLETM_STATUS) VALUES('2', 'PP', 'D', 'N', 'Y');
  
COMMIT;