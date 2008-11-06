SELECT count(*) from erps.attributes where ROOT_ID in ('0', '000000000300740012', '000000000300720104') and restr IN ('TMPC');

-- 7 records will be removed.

DELETE from erps.attributes where ROOT_ID in ('0', '000000000300740012', '000000000300720104') and restr in ('TMPC');


update erps.attributes set restr = 'tmpa,tmpb' where restr ='tmpb,tmpa'; 
update erps.attributes set restr = 'tmpb,tmpc' where restr ='tmpc,tmpa'; 


SELECT count(*) from erps.attribute from erps.attributes where ROOT_ID in ('0', '000000000300740012', '000000000300720104') and restr in('TMPA,TMPC', 'TMPC,TMPA');

-- n records will be updated

update erps.attributes set restr = 'TMPA' where ROOT_ID in ('0', '000000000300740012', '000000000300720104') and restr in('TMPA,TMPC', 'TMPC,TMPA');


