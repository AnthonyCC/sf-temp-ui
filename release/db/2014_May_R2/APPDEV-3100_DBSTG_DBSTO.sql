Insert into CUST.AFFILIATE
   (CODE, NAME, DESCRIPTION, COND_TAX, COND_DEPOSIT, PAYMENTECH_DIVISION)
 Values
   ('FDW', 'FD Wines', 'Fresh Direct Wines', 'ZT03', 'ZBD3', 
    '238313');
Insert into CUST.AFFILIATE_MERCHANT
   (AFFILIATE_CODE, PAYMENT_TYPE, MERCHANT)
 Values
   ('FDW', 'VISA', 'Bestcellar');
Insert into CUST.AFFILIATE_MERCHANT
   (AFFILIATE_CODE, PAYMENT_TYPE, MERCHANT)
 Values
   ('FDW', 'MC', 'Bestcellar');
Insert into CUST.AFFILIATE_MERCHANT
   (AFFILIATE_CODE, PAYMENT_TYPE, MERCHANT)
 Values
   ('FDW', 'ECP', 'Bestcellar');
Insert into CUST.AFFILIATE_MERCHANT
   (AFFILIATE_CODE, PAYMENT_TYPE, MERCHANT)
 Values
   ('FDW', 'DISC', 'Bestcellar');
Insert into CUST.AFFILIATE_MERCHANT
   (AFFILIATE_CODE, PAYMENT_TYPE, MERCHANT)
 Values
   ('FDW', 'AMEX', 'Bestcellar');