--add new column
alter TABLE erps.nutrition_info add infoclob clob;

--copy data from info column to new CLOB column
update erps.nutrition_info set infoclob = info;

--rename info column for backup
alter table erps.nutrition_info rename column info to info_backup;

--rename CLOB column to take the place of info column
alter table erps.nutrition_info rename column infoclob to info;