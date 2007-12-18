options(errors=2000)
LOAD DATA
INFILE *
APPEND
INTO TABLE PROMO_CUSTOMER
(
email boundFILLER char(100),
customer_id  position(1:16) char(16) "(select id from cust.customer where user_id=ltrim(rtrim(lower(:email))))",
promotion_id position(17:32) char(16) "(select id from cust.promotion where code='FD_MORTEN')"
)
BEGINDATA
morten@jespersenfamily.com 