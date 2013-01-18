alter table cust.customerlist add modification_date date null;

update cust.customerlist set modification_date = create_date;
