/* Rollback DML scripts */

update transp.scrib set zone_id = zone_code;

update transp.scrib_bid sb set sb.time = sb.dispatch_grouptime where time is null;

update transp.scrib s set s.start_time = s.dispatch_grouptime,
s.first_window_time = (s.truck_dispatchtime  + decode(s.zone_code, NULL, ((select decode(lead_to_time, NULL, 0, lead_to_time) from transp.trn_facility f where F.ID = s.DESTINATION_FACILITY)  * (1/24/60)), ((select decode(stem_to_time, NULL, 0, stem_to_time) from transp.zone z where z.zone_code=s.zone_code)  * (1/24/60)))),
s.end_window_time = (s.truck_endtime  -  decode(s.zone_code, NULL, ((select decode(f.lead_from_time, NULL, decode(f.lead_to_time, NULL, 0, f.lead_to_time), f.lead_from_time)  from transp.trn_facility f where F.ID = s.DESTINATION_FACILITY)  * (1/24/60)), ((select decode(stem_from_time, NULL, decode(stem_to_time, NULL, 0, stem_to_time), stem_from_time)  from transp.zone z where z.zone_code=s.zone_code)  * (1/24/60))));
where s.start_time is NULL;

update transp.plan p set p.start_time = p.dispatch_grouptime,
p.first_dlv_time = (p.truck_dispatchtime  +
    CASE 
         WHEN (p.zone is NULL AND p.is_bullpen='Y') THEN 0
         WHEN (p.zone is NOT NULL AND (p.is_bullpen='N' OR p.is_bullpen is NULL)) THEN ((select decode(stem_to_time, NULL, 0, stem_to_time) from transp.zone z where z.zone_code=p.zone)  * (1/24/60))
         WHEN (p.zone is NULL AND (p.is_bullpen='N' OR p.is_bullpen is NULL)) THEN ((select decode(lead_to_time, NULL, 0, lead_to_time) from transp.trn_facility f where F.ID = p.DESTINATION_FACILITY)  * (1/24/60))
    ELSE
         ((select decode(stem_to_time, NULL, 0, stem_to_time) from transp.zone z where z.zone_code=p.zone)  * (1/24/60))
    END
),
p.last_dlv_time = (p.truck_endtime  - 
    CASE 
         WHEN (p.zone is NULL AND p.is_bullpen='Y') THEN 0
         WHEN (p.zone is NOT NULL AND (p.is_bullpen='N' OR p.is_bullpen is NULL)) THEN ((select decode(stem_from_time, NULL, decode(stem_to_time, NULL, 0, stem_to_time), stem_from_time)  from transp.zone z where z.zone_code=p.zone)  * (1/24/60))
         WHEN (p.zone is NULL AND (p.is_bullpen='N' OR p.is_bullpen is NULL)) THEN ((select decode(f.lead_from_time, NULL, decode(f.lead_to_time, NULL, 0, f.lead_to_time), f.lead_from_time)  from transp.trn_facility f where F.ID = p.DESTINATION_FACILITY)  * (1/24/60))
    ELSE
         ((select decode(stem_from_time, NULL, decode(stem_to_time, NULL, 0, stem_to_time), stem_from_time)  from transp.zone z where z.zone_code=p.zone)  * (1/24/60))
    END
)
where p.start_time is NULL;

update TRANSP.WAVE_INSTANCE w set w.dispatch_time = w.truck_dispatchtime, 
w.first_dlv_time = (w.truck_dispatchtime  +  ((select decode(stem_to_time, NULL, 0, stem_to_time) from transp.zone z where z.zone_code=w.area)  * (1/24/60))),
w.last_dlv_time = (w.truck_endtime  -  ((select decode(stem_from_time, NULL, decode(stem_to_time, NULL, 0, stem_to_time), stem_from_time)  from transp.zone z where z.zone_code=w.area)  * (1/24/60)))
where w.dispatch_time is NULL;

update transp.dispatch d set d.start_time = d.dispatch_grouptime, 
d.first_dlv_time = (d.truck_dispatchtime  +  
    CASE 
         WHEN (d.zone is NULL AND d.isbullpen='Y') THEN ((select decode(stem_to_time, NULL, 0, stem_to_time) from transp.zone z where z.zone_code=d.zone)  * (1/24/60))
         WHEN (d.zone is NOT NULL AND (d.isbullpen='N' OR d.isbullpen is NULL)) THEN ((select decode(stem_to_time, NULL, 0, stem_to_time) from transp.zone z where z.zone_code=d.zone)  * (1/24/60))
         WHEN (d.zone is NULL AND (d.isbullpen='N' OR d.isbullpen is NULL)) THEN ((select decode(lead_to_time, NULL, 0, lead_to_time) from transp.trn_facility f where F.ID = d.DESTINATION_FACILITY)  * (1/24/60))
    ELSE
         ((select decode(stem_to_time, NULL, 0, stem_to_time) from transp.zone z where z.zone_code=d.zone)  * (1/24/60))
    END
)
where d.start_time is NULL;