INSERT INTO CUST.PROMOTION (ID, CAMPAIGN_CODE, CODE, NAME, DESCRIPTION, MAX_USAGE, START_DATE, EXPIRATION_DATE)
VALUES (cust.system_seq.nextval, 'SIGNUP', 'SIGNUP_FRESHFOOD', 'Signup Fresh Food', 'Signup Fresh Food', 1, 
TO_Date( '06/14/2005 12:00:00 AM', 'MM/DD/YYYY HH:MI:SS AM'),
TO_Date( '06/14/2006 12:00:00 AM', 'MM/DD/YYYY HH:MI:SS AM') ); 
