CREATE TABLE CUST.PROMO_DCPD_DATA (ID VARCHAR2(16),
PROMOTION_ID VARCHAR2(16) NOT NULL, 
CONTENT_TYPE VARCHAR2(40) NOT NULL, 
CONTENT_ID VARCHAR2(40) NOT NULL,
CONSTRAINT GRPDISCOUNT_PK PRIMARY KEY(ID),
CONSTRAINT PROMO_GRPDISCOUNT_FK FOREIGN KEY(PROMOTION_ID) REFERENCES CUST.PROMOTION(ID)
);

GRANT insert,update,select,delete on CUST.PROMO_DCPD_DATA to FDSTORE_PRDA;
GRANT insert,update,select,delete on CUST.PROMO_DCPD_DATA to FDSTORE_PRDB;




