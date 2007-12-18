-- Set the new date and message for the Thanksgiving restriction days
-- Expected result: 1 row updated
update dlv.restricted_days set 
	   start_time = to_date('11/20/2007','MM/DD/YYYY'),
	   end_time=to_date('11/21/2007 11:59:59 pm','MM/DD/YYYY hh:mi:ss am'),
	   message='Orders containing special Thanksgiving items may only be placed for Tues, 11/20 and Wed, 11/21.'
where reason='TKG'; 	   


-- set the new date and message for the TMPC restriction to restrict certains platters during Thanksgiving
-- Expected result: 1 row updated
update dlv.restricted_days set 
	   start_time = to_date('11/20/2007','MM/DD/YYYY'),
	   end_time=to_date('11/21/2007 11:59:59 pm','MM/DD/YYYY hh:mi:ss am'),
	   message='This item is not available for delivery on Tues, 11/20 and Wed, 11/21.'
where reason='TMPC';

--- Add the Advance order flag to the turkey items.
-- Expected result: 1 row inserted, per statement.
insert into erps.attributes  values (erps.system_seq.nextval, '000000000300301152',null,null, 'B', 'advance_order_flag','true',sysdate);
insert into erps.attributes  values (erps.system_seq.nextval, '000000000300301153',null,null, 'B', 'advance_order_flag','true',sysdate);
insert into erps.attributes  values (erps.system_seq.nextval, '000000000300301154',null,null, 'B', 'advance_order_flag','true',sysdate);
insert into erps.attributes  values (erps.system_seq.nextval, '000000000300301156',null,null, 'B', 'advance_order_flag','true',sysdate);
insert into erps.attributes  values (erps.system_seq.nextval, '000000000300301155',null,null, 'B', 'advance_order_flag','true',sysdate);
insert into erps.attributes  values (erps.system_seq.nextval, '000000000300301185',null,null, 'B', 'advance_order_flag','true',sysdate);
insert into erps.attributes  values (erps.system_seq.nextval, '000000000300301189',null,null, 'B', 'advance_order_flag','true',sysdate);
insert into erps.attributes  values (erps.system_seq.nextval, '000000000300370518',null,null, 'B', 'advance_order_flag','true',sysdate);
insert into erps.attributes  values (erps.system_seq.nextval, '000000000300301087',null,null, 'B', 'advance_order_flag','true',sysdate);
insert into erps.attributes  values (erps.system_seq.nextval, '000000000300301088',null,null, 'B', 'advance_order_flag','true',sysdate);
insert into erps.attributes  values (erps.system_seq.nextval, '000000000300301048',null,null, 'B', 'advance_order_flag','true',sysdate);
insert into erps.attributes  values (erps.system_seq.nextval, '000000000300301090',null,null, 'B', 'advance_order_flag','true',sysdate);


--- Turn off some catering items for the the Thanksgiving period
--  1) Get number of items that have the TMPC Restriciton...if greater than 0 then we need to run the "Remove restriction query"
select count(1) 
from erps.attributes
where atr_name='restrictions' and atr_value like  '%TMPC%' ;

----2) Remove Restriction Queries ---
  --Remove by Deleting restriction entries that only have the one restriction
-- Expected result: xxxx row deleted ( run select statement with this where clause to get count)
   delete from erps.attributes where atr_value='TMPC' and atr_name='restrictions'

  -- Remove by Updating the attribute value, removing the specified restriction when more than one restriction is in the value column. 
-- Expected result: xxxx row updated ( run select statement with this where clause to get count)
update erps.attributes set atr_value = 
   case
	when length(atr_value)>4  and instr(atr_value,'TMPC')=1 
   	   then substr(atr_value,6) 
	when length(atr_value)>4  and instr(atr_value,'TMPC')>1 
	   then substr(atr_value,1,instr(atr_value,',TMPC')-1)||substr(atr_value,instr(atr_value,',TMPC')+5)
   end
where atr_name='restrictions' and atr_value like  '%TMPC%';



--- 3) Now add restriction to exiting entry for specified root id's  
-- Expected result: xxxx row updated ( run select statement with this where clause to get count)
update erps.attributes set atr_value = 'TMPC,'||atr_value where root_id in 
('000000000300720011','000000000300720012','000000000300720014','000000000300720015','000000000300720021','000000000300720024','000000000300720025',
'000000000300720027','000000000300720028','000000000300720031','000000000300720033','000000000300720034','000000000300720036','000000000300720037',
'000000000300720039','000000000300720041','000000000300720045','000000000300720046','000000000300720047','000000000300720050','000000000300720054',
'000000000300720055','000000000300720056','000000000300720059','000000000300720083','000000000300720086','000000000300720087','000000000300720090',
'000000000300720100','000000000300720105','000000000300720106','000000000300720108','000000000300720109','000000000300720121','000000000300720122',
'000000000300720123','000000000300720130','000000000300720134','000000000300720135','000000000300720136','000000000300720137','000000000300720138',
'000000000300720139','000000000300720140','000000000300720141','000000000300720142','000000000300720143','000000000300720144','000000000300720145',
'000000000300720149','000000000300720150','000000000300720151','000000000300720152','000000000300720153','000000000300720154','000000000300720157',
'000000000300720159','000000000300720161','000000000300720162','000000000300720176','000000000300720177','000000000300720191','000000000300720197',
'000000000300720198','000000000300720199','000000000300720201','000000000300720203','000000000300720204','000000000300720205','000000000300720206',
'000000000300720207','000000000300720209','000000000300720210','000000000300720211','000000000300720214','000000000300720216','000000000300720217',
'000000000300720218','000000000300720220','000000000300720221','000000000300720222','000000000300720223','000000000300720224','000000000300720227',
'000000000300720228','000000000300720229','000000000300720230','000000000300720231','000000000300720232','000000000300740004','000000000300740005',
'000000000300750016','000000000300750017','000000000300750034','000000000300800016','000000000300800017','000000000300800046','000000000300800047',
'000000000300800075','000000000300800109','000000000300800110'
) and atr_name='restrictions' and atr_value not like '%TMPC%';


 
-- 4) Now Insert restriction for root id's that do not have a restriction attribute
-- Expected result: xxxx row inserted ( should be  total num of items - items that were updated)
insert into erps.attributes a1
select erps.system_seq.nextval, sap_id,null,null,'S','restrictions','TMPC',sysdate 
from 
 (select distinct sap_id from erps.material m 
   where m.sap_id in ('000000000300720011','000000000300720012','000000000300720014','000000000300720015','000000000300720021','000000000300720024','000000000300720025',
'000000000300720027','000000000300720028','000000000300720031','000000000300720033','000000000300720034','000000000300720036','000000000300720037',
'000000000300720039','000000000300720041','000000000300720045','000000000300720046','000000000300720047','000000000300720050','000000000300720054',
'000000000300720055','000000000300720056','000000000300720059','000000000300720083','000000000300720086','000000000300720087','000000000300720090',
'000000000300720100','000000000300720105','000000000300720106','000000000300720108','000000000300720109','000000000300720121','000000000300720122',
'000000000300720123','000000000300720130','000000000300720134','000000000300720135','000000000300720136','000000000300720137','000000000300720138',
'000000000300720139','000000000300720140','000000000300720141','000000000300720142','000000000300720143','000000000300720144','000000000300720145',
'000000000300720149','000000000300720150','000000000300720151','000000000300720152','000000000300720153','000000000300720154','000000000300720157',
'000000000300720159','000000000300720161','000000000300720162','000000000300720176','000000000300720177','000000000300720191','000000000300720197',
'000000000300720198','000000000300720199','000000000300720201','000000000300720203','000000000300720204','000000000300720205','000000000300720206',
'000000000300720207','000000000300720209','000000000300720210','000000000300720211','000000000300720214','000000000300720216','000000000300720217',
'000000000300720218','000000000300720220','000000000300720221','000000000300720222','000000000300720223','000000000300720224','000000000300720227',
'000000000300720228','000000000300720229','000000000300720230','000000000300720231','000000000300720232','000000000300740004','000000000300740005',
'000000000300750016','000000000300750017','000000000300750034','000000000300800016','000000000300800017','000000000300800046','000000000300800047',
'000000000300800075','000000000300800109','000000000300800110'
)   and not exists 
     (select 1 from erps.attributes a2 
       where a2.root_id=m.sap_id
       and a2.atr_name='restrictions' )
 );

