-- extend TRKD field to 128 characters
alter table cust.log_cart_events modify(trkd VARCHAR2(128));
