
--Step 1:  DBA needs to Copy the dlv.zone_worktable from Stage to production ----

-- Step 2:  Create the zip code boundries for suffolk county  ---
insert into dlv.zipcode_worktab(zipcode, geoloc) 
    select n.l_postcode, sdo_aggr_convexhull(MDSYS.SDOAGGRTYPE(geoloc, 0.5)) 
      FROM dlv.navtech_geocode n,
	  (SELECt zipcode, county from dlv.zipplusfour zpf,dlv.city_state cs where zpf.city_state_key = cs.city_state_key
       and county = 'SUFFOLK' group by zipcode,county) cty
    where n.L_POSTCODE=cty.zipcode or r_postcode =cty.zipcode  
 group by n.l_postcode; 


-- Step 3: create the region and region data for the hamptons and hamptons pickup  ---
insert into dlv.region      values (dlv.system_seq.nextval, 'Hamptons','X','HOME');
insert into dlv.region_data values (dlv.system_seq.nextval, (select id from dlv.region where name='Hamptons'),sysdate+30,12.95);
insert into dlv.region      values (dlv.system_seq.nextval, 'Hamptons Pickup',null,null);
insert into dlv.region_data values (dlv.system_seq.nextval, (select id from dlv.region where name='Hamptons Pickup'),sysdate+30,12.95);


-- Step 4: get the Zones from the zone worktable hamptons --
insert into dlv.zone 
 select dlv.system_seq.nextval, name,geoloc, code,
 (select id from dlv.region_data  where region_id = (select id from dlv.region where name='Hamptons'))
 ,null 
from dlv.zone_worktable
where  code in ('810','820','830','840','850','860');

-- Step 5: create zones for pickup in hampton, using the existing hamptons zones --
insert into dlv.zone 
  select system_seq.nextval, name,geoloc, to_char(to_number(code)+5) ,
 (select id from dlv.region_data  where region_id = (select id from dlv.region where name='Hamptons Pickup'))
  ,null 
  from dlv.zone_worktable
  where  code in ('810','830','840','860');