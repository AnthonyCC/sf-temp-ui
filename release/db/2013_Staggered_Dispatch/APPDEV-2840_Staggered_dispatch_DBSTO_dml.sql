/* Rollout DML scripts */

update transp.scrib set zone_code = zone_id;

update transp.scrib_bid sb set sb.dispatch_grouptime = sb.time;

update transp.scrib s set s.truck_dispatchtime = s.start_time, s.dispatch_grouptime = s.start_time,
s.truck_endtime = (s.end_window_time  + decode(s.zone_code, NULL, ((select decode(f.lead_from_time, NULL, decode(f.lead_to_time, NULL, 0, f.lead_to_time), f.lead_from_time)  from transp.trn_facility f where F.ID = s.DESTINATION_FACILITY)  * (1/24/60)),
((select decode(stem_from_time, NULL, decode(stem_to_time, NULL, 0, stem_to_time), stem_from_time)  from transp.zone z where z.zone_code=s.zone_code)  * (1/24/60))));

update transp.plan p set p.truck_dispatchtime = p.start_time, p.dispatch_grouptime = p.start_time,
p.truck_endtime = (p.last_dlv_time  + decode(p.zone, NULL,  ((select decode(f.lead_from_time, NULL, decode(f.lead_to_time, NULL, 0, f.lead_to_time), f.lead_from_time)  from transp.trn_facility f where F.ID = p.DESTINATION_FACILITY)  * (1/24/60)),
((select decode(stem_from_time, NULL, decode(stem_to_time, NULL, 0, stem_to_time), stem_from_time)  from transp.zone z where z.zone_code=p.zone)  * (1/24/60))));

update TRANSP.WAVE_INSTANCE w set w.truck_dispatchtime = w.dispatch_time,
w.truck_endtime = (w.last_dlv_time +  ((select decode(stem_from_time, NULL, decode(stem_to_time, NULL, 0, stem_to_time), stem_from_time)  from transp.zone z where z.zone_code=w.area)  * (1/24/60))),
w.max_time = (w.last_dlv_time +  ((select decode(stem_from_time, NULL, decode(stem_to_time, NULL, 0, stem_to_time), stem_from_time)  from transp.zone z where z.zone_code=w.area)  * (1/24/60)));

update transp.dispatch d set d.truck_dispatchtime = d.start_time, d.dispatch_grouptime = d.start_time where d.dispatch_date >= trunc(sysdate);