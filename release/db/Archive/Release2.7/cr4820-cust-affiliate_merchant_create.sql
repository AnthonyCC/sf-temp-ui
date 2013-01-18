   CREATE TABLE CUST.AFFILIATE_MERCHANT
(
  AFFILIATE_CODE  VARCHAR2(8) NOT NULL,
  PAYMENT_TYPE    VARCHAR2(5) NOT NULL,
  MERCHANT        VARCHAR2(64) NOT NULL
);

ALTER TABLE CUST.AFFILIATE_MERCHANT ADD (
  CONSTRAINT AFF_MERC_AFF_FK 
 FOREIGN KEY (AFFILIATE_CODE) 
 REFERENCES CUST.AFFILIATE (CODE));

GRANT SELECT, UPDATE, INSERT, DELETE ON  AFFILIATE_MERCHANT TO FDSTORE_PRDA;

GRANT SELECT, UPDATE, INSERT, DELETE ON  AFFILIATE_MERCHANT TO FDSTORE_PRDB;

insert into affiliate_merchant values('FD', 'VISA', 'Freshdirect');

insert into affiliate_merchant values('FD', 'MC', 'Freshdirect');

insert into affiliate_merchant values('FD', 'ECP', 'Freshdirect');

insert into affiliate_merchant values('FD', 'DISC', 'Freshdirect');

insert into affiliate_merchant values('FD', 'AMEX', 'Freshdirect');

insert into affiliate_merchant values('WBL', 'VISA', 'Freshdirect');

insert into affiliate_merchant values('WBL', 'MC', 'Freshdirect');

insert into affiliate_merchant values('WBL', 'ECP', 'Freshdirect');

insert into affiliate_merchant values('WBL', 'DISC', 'Freshdirect');

insert into affiliate_merchant values('WBL', 'AMEX', 'Freshdirect');

insert into affiliate_merchant values('BC', 'VISA', 'Bestcellar');

insert into affiliate_merchant values('BC', 'MC', 'Bestcellar');

insert into affiliate_merchant values('BC', 'ECP', 'Bestcellar');

insert into affiliate_merchant values('BC', 'DISC', 'Bestcellar');

insert into affiliate_merchant values('BC', 'AMEX', 'Bestcellar');
