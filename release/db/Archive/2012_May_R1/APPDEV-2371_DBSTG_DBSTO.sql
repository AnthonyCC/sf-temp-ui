update cust.customerinfo set mobile_preference_flag = 2 where mobile_preference_flag in ('Y','U');

update cust.customerinfo set mobile_preference_flag = null where mobile_preference_flag = 'N';


ALTER TABLE CUST.CUSTOMERINFO ADD (NO_THANKS_FLAG           VARCHAR2(1 BYTE));