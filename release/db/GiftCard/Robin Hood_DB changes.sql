--Adding a new columns 'CHARITY_NAME', 'COMPANY_NAME', 'OPTIN_IND' to the table 'CUST.DELIVERYINFO' related to Robin Hood feature[APPDEV-].

alter table CUST.DELIVERYINFO add CHARITY_NAME varchar2(55) null;
alter table CUST.DELIVERYINFO add COMPANY_NAME varchar2(55) null;
alter table CUST.DELIVERYINFO add OPTIN_IND char(1) null;--This column need to be removed once we clean up the code.


CREATE TABLE CUST.DONATION_OPT_IND(CUSTOMER_ID VARCHAR2(16) not null, SALE_ID VARCHAR2(16) not null, OPTIN_IND CHAR(1));
