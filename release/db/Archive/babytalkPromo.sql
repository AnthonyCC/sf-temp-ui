INSERT INTO PROMOTION ( ID, CODE, NAME, DESCRIPTION, MAX_USAGE, START_DATE, EXPIRATION_DATE
, CAMPAIGN_CODE, CATEGORY_NAME, PRODUCT_NAME, ORDERTYPE_HOME, ORDERTYPE_PICKUP, ORDERTYPE_DEPOT
, NEEDDRYGOODS, NEEDITEMSFROM ) VALUES ( 
CUST.system_seq.nextval, 'FD_BABYTALK', 'Complimentary BabyTalk Magazine', 'Complimentary BabyTalk Magazine'
, 1,  TO_Date( '1/21/2005 12:00:00 AM', 'MM/DD/YYYY HH:MI:SS AM'),  TO_Date( '2/2/2005 11:59:59 PM', 'MM/DD/YYYY HH:MI:SS AM')
, 'SAMPLE', 'mkt_wlcomkit', 'mkt_promo_babytalk', 'X', 'X', 'X', 'X', 'gro_baby')  

