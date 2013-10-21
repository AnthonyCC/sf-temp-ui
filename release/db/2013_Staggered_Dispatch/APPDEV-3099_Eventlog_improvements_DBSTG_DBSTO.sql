alter table transp.moteventtype add (ORDERNUM_REQ varchar2(1 BYTE));

alter table transp.eventtype add (ORDERNUM_REQ varchar2(1 BYTE));

update  transp.moteventtype set ORDERNUM_REQ='X' where id in ('4','9','10');

update  transp.eventtype set ORDERNUM_REQ='X' where id in ('45','23');