alter table DRAFTCHANGE add (temp_value VARCHAR2(4000 CHAR));
update DRAFTCHANGE set temp_value=dbms_lob.substr(value,4000,1);
alter table DRAFTCHANGE drop column value;
alter table DRAFTCHANGE rename column temp_value to value;