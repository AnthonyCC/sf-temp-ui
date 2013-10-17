/* Rollback DML scripts */

update transp.scrib s set s.start_time = s.dispatch_grouptime
s.first_dlv_time = (s.truck_dispatchtime  +  ((select decode(stem_to_time, NULL, 0, stem_to_time) from transp.zone z where z.zone_code=s.area)  * (1/24/60))),
s.end_window_time = (s.truck_endtime  -  ((select decode(stem_from_time, NULL, decode(stem_to_time, NULL, 0, stem_to_time), stem_from_time)  from transp.zone z where z.zone_code=s.area)  * (1/24/60)));

update transp.plan s set p.start_time = p.dispatch_grouptime
p.first_dlv_time = (p.truck_dispatchtime  +  ((select decode(stem_to_time, NULL, 0, stem_to_time) from transp.zone z where z.zone_code=p.area)  * (1/24/60))),
p.last_dlv_time = (p.truck_endtime  -  ((select decode(stem_from_time, NULL, decode(stem_to_time, NULL, 0, stem_to_time), stem_from_time)  from transp.zone z where z.zone_code=p.zone)  * (1/24/60)));

update TRANSP.WAVE_INSTANCE w set w.dispatch_time = w.truck_dispatchtime, 
w.first_dlv_time = (w.truck_dispatchtime  +  ((select decode(stem_to_time, NULL, 0, stem_to_time) from transp.zone z where z.zone_code=w.area)  * (1/24/60))),
w.last_dlv_time = (w.truck_endtime  -  ((select decode(stem_from_time, NULL, decode(stem_to_time, NULL, 0, stem_to_time), stem_from_time)  from transp.zone z where z.zone_code=w.area)  * (1/24/60)));

update transp.dispatch d set d.start_time = d.dispatch_grouptime 
d.first_dlv_time = (d.truck_dispatchtime  +  ((select decode(stem_to_time, NULL, 0, stem_to_time) from transp.zone z where z.zone_code=d.zone)  * (1/24/60)))
where d.dispatch_date >= trunc(sysdate);

update transp.scrib set zone_id = zone_code where zone_id is null;

update transp.scrib_bid sb set sb.time = sb.dispatch_grouptime where time is null;