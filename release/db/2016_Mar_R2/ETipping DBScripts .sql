--APPDEV-4912 2 E-Tip Approval Screen for TRN_ADMIN role.

Insert into TRANSP.MENU_ROLE
   (MENU_ID,ROLE_ID)
 Values
   ('OPR013', '1');
   

--APPDEV-4903 Deduct fee % based on payment method



ALTER TABLE TRANSP.TIP_DISTRIBUTION
MODIFY(GROSS_TIP NUMBER(5,2));


ALTER TABLE TRANSP.TIP_DISTRIBUTION
MODIFY(DEDUCTION NUMBER(5,2));

 
 Insert into TRANSP.PROPERTY_MASTER
   (NAME, TYPE, DEFAULT_VALUE, PROPERTY_ENABLED)
 Values
   ('fd.etip.deduction.visa', 'LOG', '0.25', 'X');
   
Insert into TRANSP.PROPERTY_MASTER
   (NAME, TYPE, DEFAULT_VALUE, PROPERTY_ENABLED)
 Values
   ('fd.etip.deduction.mastercard', 'LOG', '0.25', 'X');
   
   Insert into TRANSP.PROPERTY_MASTER
   (NAME, TYPE, DEFAULT_VALUE, PROPERTY_ENABLED)
 Values
   ('fd.etip.deduction.amex', 'LOG', '2.10', 'X');
   
    Insert into TRANSP.PROPERTY_MASTER
   (NAME, TYPE, DEFAULT_VALUE, PROPERTY_ENABLED)
 Values
   ('fd.etip.deduction.discover', 'LOG', '2.40', 'X');
   
    Insert into TRANSP.PROPERTY_MASTER
   (NAME, TYPE, DEFAULT_VALUE, PROPERTY_ENABLED)
 Values
   ('fd.etip.deduction.echeck', 'LOG', '0.10', 'X');
   
    Insert into TRANSP.PROPERTY_MASTER
   (NAME, TYPE, DEFAULT_VALUE, PROPERTY_ENABLED)
 Values
   ('fd.etip.deduction.paypal', 'LOG', '1.80', 'X');