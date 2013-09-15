alter table TRANSP.DISPATCH  add (DISPATCH_TYPE  VARCHAR2 (3 BYTE)  DEFAULT 'RGD' NOT NULL);

alter table TRANSP.REGION  add ( DISPVALIDATION_ENABLED  VARCHAR2(1 BYTE)  DEFAULT 'X');

update transp.dispatch d set d.dispatch_type='RGD';