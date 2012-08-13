ALTER TABLE TRANSP.ZONE add (STEERING_RADIUS   NUMBER);

ALTER TABLE CUST.PROMO_DLV_TIMESLOT  add (DLV_WINDOWTYPE CUST.PROMO_DLV_WINDOWTYPES);

CREATE OR REPLACE TYPE CUST.PROMO_DLV_WINDOWTYPES AS VARRAY(1000)  OF varchar2(5);

GRANT EXECUTE ON CUST.PROMO_DLV_WINDOWTYPES TO fdstore_stprd01;
GRANT EXECUTE ON CUST.PROMO_DLV_WINDOWTYPES TO APPDEV;

alter table cust.promotion_new modify(name varchar2(50));