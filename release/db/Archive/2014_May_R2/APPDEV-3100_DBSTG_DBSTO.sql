Insert into CUST.AFFILIATE
   (CODE, NAME, DESCRIPTION, COND_TAX, COND_DEPOSIT, PAYMENTECH_DIVISION)
 Values
   ('FDW', 'FD Wines', 'Fresh Direct Wines', 'ZT03', 'ZBD3', 
    '238313');
Insert into CUST.AFFILIATE_MERCHANT
   (AFFILIATE_CODE, PAYMENT_TYPE, MERCHANT)
 Values
   ('FDW', 'VISA', 'FreshdirectWines');
Insert into CUST.AFFILIATE_MERCHANT
   (AFFILIATE_CODE, PAYMENT_TYPE, MERCHANT)
 Values
   ('FDW', 'MC', 'FreshdirectWines');
Insert into CUST.AFFILIATE_MERCHANT
   (AFFILIATE_CODE, PAYMENT_TYPE, MERCHANT)
 Values
   ('FDW', 'ECP', 'FreshdirectWines');
Insert into CUST.AFFILIATE_MERCHANT
   (AFFILIATE_CODE, PAYMENT_TYPE, MERCHANT)
 Values
   ('FDW', 'DISC', 'FreshdirectWines');
Insert into CUST.AFFILIATE_MERCHANT
   (AFFILIATE_CODE, PAYMENT_TYPE, MERCHANT)
 Values
   ('FDW', 'AMEX', 'FreshdirectWines');