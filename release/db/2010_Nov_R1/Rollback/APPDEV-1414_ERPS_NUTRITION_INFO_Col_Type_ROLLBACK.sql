--rollback steps (without removing clob column, in case data is needed)

--rename info column for rollback
alter table erps.nutrition_info rename column info to infoclob;

--rename backup column back to the original
alter table erps.nutrition_info rename column info_backup to info;