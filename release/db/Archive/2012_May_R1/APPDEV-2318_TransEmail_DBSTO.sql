CREATE TABLE CUST.TRANS_EMAIL_MASTER(
  ID              VARCHAR2(16 BYTE) PRIMARY KEY,
  TARGET_PROG_ID  VARCHAR2(16 BYTE)             NOT NULL,
  ORDER_ID        VARCHAR2(16 BYTE),
  CUSTOMER_ID     VARCHAR2(16 BYTE),
  TRANS_TYPE      VARCHAR2(50 BYTE)             NOT NULL,
  STATUS          VARCHAR2(20 BYTE)             NOT NULL,
  ERROR_TYPE      VARCHAR2(50 BYTE),
  ERROR_DESC      VARCHAR2(500 BYTE),
  CROMOD_DATE     DATE,
  EMAIL_TYPE      VARCHAR2(16 BYTE)             NOT NULL,
  PROVIDER        VARCHAR2(16 BYTE)             NOT NULL
);

CREATE TABLE TRANS_EMAIL_DETAILS(
  ID                VARCHAR2(16 BYTE) PRIMARY KEY,
  TRANS_EMAIL_ID    VARCHAR2(16 BYTE),
  FROM_ADDR         VARCHAR2(50 BYTE)           NOT NULL,
  TO_ADDR           VARCHAR2(50 BYTE)           NOT NULL,
  CC_ADDR           VARCHAR2(250 BYTE),
  BCC_ADDR          VARCHAR2(250 BYTE),
  SUBJECT           VARCHAR2(500 BYTE)          NOT NULL,
  TEMPLATE_CONTENT  CLOB                        NOT NULL,
  SENT_DATE         DATE
);

ALTER TABLE CUST.TRANS_EMAIL_DETAILS ADD (
  CONSTRAINT TEMAILS_DTLS_FK 
 FOREIGN KEY (TRANS_EMAIL_ID) 
 REFERENCES CUST.TRANS_EMAIL_MASTER (ID));


CREATE TABLE TRANS_EMAIL_TYPES(
  ID              VARCHAR2(16 BYTE) PRIMARY KEY,
  PROVIDER        VARCHAR2(20 BYTE)             NOT NULL,
  TEMPLATE_ID     VARCHAR2(16 BYTE)             NOT NULL,
  TRANS_TYPE      VARCHAR2(50 BYTE)             NOT NULL,
  EMAIL_TYPE      VARCHAR2(20 BYTE)             NOT NULL,
  DESCRIPTION     VARCHAR2(100 BYTE),
  ACTIVE          VARCHAR2(1 BYTE),
  FROM_ADDR       VARCHAR2(50 BYTE)             NOT NULL,
  SUBJECT         VARCHAR2(250 BYTE),
  TARGET_PROG_ID  VARCHAR2(20 BYTE),
  IS_PROD_READY   VARCHAR2(1 BYTE)
);


GRANT ALL on CUST.TRANS_EMAIL_TYPES TO FDSTORE_PRDA;

GRANT ALL on CUST.TRANS_EMAIL_MASTER TO FDSTORE_PRDA;

GRANT ALL on CUST.TRANS_EMAIL_DETAILS TO FDSTORE_PRDA;


--SQL Statement which produced this data:
--
--  select * from cust.TRANS_EMAIL_TYPES
--
Insert into CUST.TRANS_EMAIL_TYPES
   (ID, PROVIDER, TEMPLATE_ID, TRANS_TYPE, EMAIL_TYPE, DESCRIPTION, ACTIVE, FROM_ADDR, SUBJECT, TARGET_PROG_ID)
 Values
   ('1', 'CHEETAH', '123123123', 'ORDER_SUBMIT', 'HTML', 
    'some test desc', 'X', 'service@freshdirect.com', 'Your order for {0}', '123123123');
Insert into CUST.TRANS_EMAIL_TYPES
   (ID, PROVIDER, TEMPLATE_ID, TRANS_TYPE, EMAIL_TYPE, DESCRIPTION, ACTIVE, FROM_ADDR, SUBJECT, TARGET_PROG_ID)
 Values
   ('2', 'CHEETAH', '3443686868', 'ORDER_MODIFY', 'HTML', 
    'some test desc', 'X', 'service@freshdirect.com', 'Your order information has been updated', '3443686868');
Insert into CUST.TRANS_EMAIL_TYPES
   (ID, PROVIDER, TEMPLATE_ID, TRANS_TYPE, EMAIL_TYPE, DESCRIPTION, ACTIVE, FROM_ADDR, SUBJECT, TARGET_PROG_ID)
 Values
   ('3', 'CHEETAH', '2197245924', 'CUST_SIGNUP', 'HTML', 
    'some test desc', 'X', 'service@freshdirect.com', 'Welcome to FreshDirect!', '104146');
Insert into CUST.TRANS_EMAIL_TYPES
   (ID, PROVIDER, TEMPLATE_ID, TRANS_TYPE, EMAIL_TYPE, DESCRIPTION, ACTIVE, FROM_ADDR, SUBJECT, TARGET_PROG_ID)
 Values
   ('4', 'CHEETAH', '532434342', 'GC_CANCEL_RECIPENT', 'HTML', 
    'email for giftcard cancel for recipent ', 'X', 'service@freshdirect.com', 'Your Gift Card order Cancellation', '532434342');
Insert into CUST.TRANS_EMAIL_TYPES
   (ID, PROVIDER, TEMPLATE_ID, TRANS_TYPE, EMAIL_TYPE, DESCRIPTION, ACTIVE, FROM_ADDR, SUBJECT, TARGET_PROG_ID)
 Values
   ('5', 'CHEETAH', '6453562367', 'ORDER_CANCEL', 'HTML', 
    'email for order cancel', 'X', 'service@freshdirect.com', 'Cancellation receipt', '6453562367');
Insert into CUST.TRANS_EMAIL_TYPES
   (ID, PROVIDER, TEMPLATE_ID, TRANS_TYPE, EMAIL_TYPE, DESCRIPTION, ACTIVE, FROM_ADDR, SUBJECT, TARGET_PROG_ID)
 Values
   ('6', 'CHEETAH', '72525378', 'CHARGE_ORDER', 'HTML', 
    'email for charge order', 'X', 'service@freshdirect.com', 'Your order has been charged {0}', '72525378');
Insert into CUST.TRANS_EMAIL_TYPES
   (ID, PROVIDER, TEMPLATE_ID, TRANS_TYPE, EMAIL_TYPE, DESCRIPTION, ACTIVE, FROM_ADDR, SUBJECT, TARGET_PROG_ID)
 Values
   ('7', 'CHEETAH', '836753457', 'CREDIT_CONFIRM', 'HTML', 
    'email for credit confirm', 'X', 'service@freshdirect.com', 'We''ve issued your credits', '836753457');
Insert into CUST.TRANS_EMAIL_TYPES
   (ID, PROVIDER, TEMPLATE_ID, TRANS_TYPE, EMAIL_TYPE, DESCRIPTION, ACTIVE, FROM_ADDR, SUBJECT, TARGET_PROG_ID)
 Values
   ('8', 'CHEETAH', '92566279', 'FORGOT_PASSWD', 'HTML', 
    'email for forgot password ', 'X', 'service@freshdirect.com', 'Important message from FreshDirect', '92566279');
Insert into CUST.TRANS_EMAIL_TYPES
   (ID, PROVIDER, TEMPLATE_ID, TRANS_TYPE, EMAIL_TYPE, DESCRIPTION, ACTIVE, FROM_ADDR, SUBJECT, TARGET_PROG_ID)
 Values
   ('9', 'CHEETAH', '643546489', 'AUTH_FAILURE', 'HTML', 
    'email for authorization failure ', 'X', 'service@freshdirect.com', 'Credit Card Authorization Failure', '643546489');
Insert into CUST.TRANS_EMAIL_TYPES
   (ID, PROVIDER, TEMPLATE_ID, TRANS_TYPE, EMAIL_TYPE, DESCRIPTION, ACTIVE, FROM_ADDR, SUBJECT, TARGET_PROG_ID)
 Values
   ('10', 'CHEETAH', '3449542358', 'CUST_REMINDER', 'HTML', 
    'email for authorization failure ', 'X', 'service@freshdirect.com', 'A friendly reminder from FreshDirect.', '3449542358');
Insert into CUST.TRANS_EMAIL_TYPES
   (ID, PROVIDER, TEMPLATE_ID, TRANS_TYPE, EMAIL_TYPE, DESCRIPTION, ACTIVE, FROM_ADDR, SUBJECT, TARGET_PROG_ID)
 Values
   ('11', 'CHEETAH', '235363890', 'RECIPE_MAIL', 'HTML', 
    'email for Recipe ', 'X', 'service@freshdirect.com', 'FreshDirect - Recipe for {0}.', '235363890');
Insert into CUST.TRANS_EMAIL_TYPES
   (ID, PROVIDER, TEMPLATE_ID, TRANS_TYPE, EMAIL_TYPE, DESCRIPTION, ACTIVE, FROM_ADDR, SUBJECT, TARGET_PROG_ID)
 Values
   ('12', 'CHEETAH', '4564545677', 'TELL_A_FRIEND', 'HTML', 
    'email for tell a friend', 'X', 'service@freshdirect.com', 'Your friend {0} would like you to try FreshDirect ', '4564545677');
Insert into CUST.TRANS_EMAIL_TYPES
   (ID, PROVIDER, TEMPLATE_ID, TRANS_TYPE, EMAIL_TYPE, DESCRIPTION, ACTIVE, FROM_ADDR, SUBJECT, TARGET_PROG_ID)
 Values
   ('13', 'CHEETAH', '4564545677', 'TELLAFRIEND_RECIPE', 'HTML', 
    'email for tell a friend', 'X', 'service@freshdirect.com', 'Your friend {0} has sent you {0}', '4564545677');
Insert into CUST.TRANS_EMAIL_TYPES
   (ID, PROVIDER, TEMPLATE_ID, TRANS_TYPE, EMAIL_TYPE, DESCRIPTION, ACTIVE, FROM_ADDR, SUBJECT, TARGET_PROG_ID)
 Values
   ('14', 'CHEETAH', '4564545677', 'TELLAFRIEND_PRODUCT', 'HTML', 
    'email for tell a friend', 'X', 'service@freshdirect.com', 'Your friend {0} has sent you {0} !', '4564545677');
Insert into CUST.TRANS_EMAIL_TYPES
   (ID, PROVIDER, TEMPLATE_ID, TRANS_TYPE, EMAIL_TYPE, DESCRIPTION, ACTIVE, FROM_ADDR, SUBJECT, TARGET_PROG_ID)
 Values
   ('15', 'CHEETAH', '836373890', 'GC_ORDER_SUBMIT', 'HTML', 
    'email for gc order submit', 'X', 'service@freshdirect.com', 'Your Gift Card order for {0} !', '836373890');
Insert into CUST.TRANS_EMAIL_TYPES
   (ID, PROVIDER, TEMPLATE_ID, TRANS_TYPE, EMAIL_TYPE, DESCRIPTION, ACTIVE, FROM_ADDR, SUBJECT, TARGET_PROG_ID)
 Values
   ('16', 'CHEETAH', '836373890', 'GC_BULK_ORDER_SUBMIT', 'HTML', 
    'email for gc bulk order submit', 'X', 'service@freshdirect.com', 'Your Gift Card order for {0} !', '836373890');
Insert into CUST.TRANS_EMAIL_TYPES
   (ID, PROVIDER, TEMPLATE_ID, TRANS_TYPE, EMAIL_TYPE, DESCRIPTION, ACTIVE, FROM_ADDR, SUBJECT, TARGET_PROG_ID)
 Values
   ('17', 'CHEETAH', '643546489', 'GC_AUTH_FAILURE', 'HTML', 
    'email for giftcard authorization failure ', 'X', 'service@freshdirect.com', 'Gift Card Authorization Failure', '643546489');
Insert into CUST.TRANS_EMAIL_TYPES
   (ID, PROVIDER, TEMPLATE_ID, TRANS_TYPE, EMAIL_TYPE, DESCRIPTION, ACTIVE, FROM_ADDR, SUBJECT, TARGET_PROG_ID)
 Values
   ('18', 'CHEETAH', '3433454356', 'GC_CANCEL_PURCHASER', 'HTML', 
    'email for giftcard cancel for purchaser ', 'X', 'service@freshdirect.com', 'Service Advisory: Your recent Gift Card purchase', '4535343564');
Insert into CUST.TRANS_EMAIL_TYPES
   (ID, PROVIDER, TEMPLATE_ID, TRANS_TYPE, EMAIL_TYPE, DESCRIPTION, ACTIVE, FROM_ADDR, SUBJECT, TARGET_PROG_ID)
 Values
   ('19', 'CHEETAH', '532434342', 'GC_CANCEL_RECIPENT', 'HTML', 
    'email for giftcard cancel for recipent ', 'X', 'service@freshdirect.com', 'Your Gift Card order Cancellation', '532434342');
Insert into CUST.TRANS_EMAIL_TYPES
   (ID, PROVIDER, TEMPLATE_ID, TRANS_TYPE, EMAIL_TYPE, DESCRIPTION, ACTIVE, FROM_ADDR, SUBJECT, TARGET_PROG_ID)
 Values
   ('20', 'CHEETAH', '643454354', 'GC_BALANCE_TRANSFER', 'HTML', 
    'email for giftcard balance transfer ', 'X', 'service@freshdirect.com', 'Your Gift Card order Balance Transfer', '643454354');
Insert into CUST.TRANS_EMAIL_TYPES
   (ID, PROVIDER, TEMPLATE_ID, TRANS_TYPE, EMAIL_TYPE, DESCRIPTION, ACTIVE, FROM_ADDR, SUBJECT, TARGET_PROG_ID)
 Values
   ('21', 'CHEETAH', '7436456456', 'GC_CREDIT_CONFIRM', 'HTML', 
    'email for giftcard credit confirm ', 'X', 'service@freshdirect.com', 'We''ve issued your credits', '7436456456');
Insert into CUST.TRANS_EMAIL_TYPES
   (ID, PROVIDER, TEMPLATE_ID, TRANS_TYPE, EMAIL_TYPE, DESCRIPTION, ACTIVE, FROM_ADDR, SUBJECT, TARGET_PROG_ID)
 Values
   ('22', 'CHEETAH', '2232123344', 'RH_ORDER_CONFIRM', 'HTML', 
    'email for Robinhood order confirm ', 'X', 'service@freshdirect.com', 'Thank you for giving to Robin Hood and helping New Yorkers in need.', '2232123344');
Insert into CUST.TRANS_EMAIL_TYPES
   (ID, PROVIDER, TEMPLATE_ID, TRANS_TYPE, EMAIL_TYPE, DESCRIPTION, ACTIVE, FROM_ADDR, SUBJECT, TARGET_PROG_ID)
 Values
   ('23', 'CHEETAH', '183563648', 'GC_RECIPENT_ORDER', 'HTML', 
    'email for GiftCard Recipent order confirm ', 'X', 'service@freshdirect.com', 'A Gift for You.', '183563648');
Insert into CUST.TRANS_EMAIL_TYPES
   (ID, PROVIDER, TEMPLATE_ID, TRANS_TYPE, EMAIL_TYPE, DESCRIPTION, ACTIVE, FROM_ADDR, SUBJECT, TARGET_PROG_ID)
 Values
   ('24', 'CHEETAH', '423427433', 'SMART_STORE_DYF', 'HTML', 
    'email for SmartStore Recommendation ', 'X', 'service@freshdirect.com', 'Recommendation for you from FreshDirect.', '423427433');
commit;

