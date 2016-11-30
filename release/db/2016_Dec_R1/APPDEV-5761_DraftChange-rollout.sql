alter table DRAFTCHANGE add (temp_value CLOB);
update DRAFTCHANGE set temp_value=value;
alter table DRAFTCHANGE drop column value;
alter table DRAFTCHANGE rename column temp_value to value;