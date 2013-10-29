/* Rollout DML scripts */

update transp.zone set pretrip_time = 15, posttrip_time = 0;

update transp.scrib set zone_code = zone_id;

update transp.scrib_bid sb set sb.dispatch_grouptime = sb.time;

update transp.scrib s set s.truck_dispatchtime = s.start_time, s.dispatch_grouptime = s.start_time,
s.truck_endtime = (s.end_window_time  + decode(s.zone_code, NULL, ((select decode(f.lead_from_time, NULL, decode(f.lead_to_time, NULL, 0, f.lead_to_time), f.lead_from_time)  from transp.trn_facility f where F.ID = s.DESTINATION_FACILITY)  * (1/24/60)),
((select decode(stem_from_time, NULL, decode(stem_to_time, NULL, 0, stem_to_time), stem_from_time)  from transp.zone z where z.zone_code=s.zone_code)  * (1/24/60))));

update transp.plan p set p.truck_dispatchtime = p.start_time, p.dispatch_grouptime = p.start_time,
p.truck_endtime = (p.last_dlv_time  + 
    CASE 
         WHEN (p.zone is NULL AND p.is_bullpen='Y') THEN 0
         WHEN (p.zone is NOT NULL AND (p.is_bullpen='N' OR p.is_bullpen is NULL)) THEN ((select decode(stem_from_time, NULL, decode(stem_to_time, NULL, 0, stem_to_time), stem_from_time)  from transp.zone z where z.zone_code = p.zone)  * (1/24/60))
         WHEN (p.zone is NULL AND (p.is_bullpen='N' OR p.is_bullpen is NULL)) THEN ((select decode(f.lead_from_time, NULL, decode(f.lead_to_time, NULL, 0, f.lead_to_time), f.lead_from_time)  from transp.trn_facility f where F.ID = p.DESTINATION_FACILITY)  * (1/24/60))
    ELSE
         ((select decode(stem_from_time, NULL, decode(stem_to_time, NULL, 0, stem_to_time), stem_from_time)  from transp.zone z where z.zone_code = p.zone)  * (1/24/60))
    END
);

update TRANSP.WAVE_INSTANCE w set w.truck_dispatchtime = w.dispatch_time,
w.truck_endtime = (w.last_dlv_time + ((select decode(stem_from_time, NULL, decode(stem_to_time, NULL, 0, stem_to_time), stem_from_time)  from transp.zone z where z.zone_code=w.area)  * (1/24/60)));

update transp.dispatch d set d.truck_dispatchtime = d.start_time, d.dispatch_grouptime = d.start_time;


Insert into TRANSP.DISPATCH_GROUP  (ID, NAME, DISPATCH_GROUPTIME) Values (transp.DISPATCHGROUPSEQ.nextval, '12:01 AM', TO_DATE('01/01/1970 00:01:00', 'MM/DD/YYYY HH24:MI:SS'));
Insert into TRANSP.DISPATCH_GROUP  (ID, NAME, DISPATCH_GROUPTIME) Values (transp.DISPATCHGROUPSEQ.nextval, '04:30 AM', TO_DATE('01/01/1970 04:30:00', 'MM/DD/YYYY HH24:MI:SS'));
Insert into TRANSP.DISPATCH_GROUP  (ID, NAME, DISPATCH_GROUPTIME) Values (transp.DISPATCHGROUPSEQ.nextval, '05:00 AM', TO_DATE('01/01/1970 05:00:00', 'MM/DD/YYYY HH24:MI:SS'));
Insert into TRANSP.DISPATCH_GROUP  (ID, NAME, DISPATCH_GROUPTIME) Values (transp.DISPATCHGROUPSEQ.nextval, '05:30 AM', TO_DATE('01/01/1970 05:30:00', 'MM/DD/YYYY HH24:MI:SS'));
Insert into TRANSP.DISPATCH_GROUP  (ID, NAME, DISPATCH_GROUPTIME) Values (transp.DISPATCHGROUPSEQ.nextval, '06:00 AM', TO_DATE('01/01/1970 06:00:00', 'MM/DD/YYYY HH24:MI:SS'));
Insert into TRANSP.DISPATCH_GROUP  (ID, NAME, DISPATCH_GROUPTIME) Values (transp.DISPATCHGROUPSEQ.nextval, '06:15 AM', TO_DATE('01/01/1970 06:15:00', 'MM/DD/YYYY HH24:MI:SS'));
Insert into TRANSP.DISPATCH_GROUP  (ID, NAME, DISPATCH_GROUPTIME) Values (transp.DISPATCHGROUPSEQ.nextval, '06:30 AM', TO_DATE('01/01/1970 06:30:00', 'MM/DD/YYYY HH24:MI:SS'));
Insert into TRANSP.DISPATCH_GROUP  (ID, NAME, DISPATCH_GROUPTIME) Values (transp.DISPATCHGROUPSEQ.nextval, '07:00 AM', TO_DATE('01/01/1970 07:00:00', 'MM/DD/YYYY HH24:MI:SS'));
Insert into TRANSP.DISPATCH_GROUP  (ID, NAME, DISPATCH_GROUPTIME) Values (transp.DISPATCHGROUPSEQ.nextval, '08:30 AM', TO_DATE('01/01/1970 08:30:00', 'MM/DD/YYYY HH24:MI:SS'));
Insert into TRANSP.DISPATCH_GROUP  (ID, NAME, DISPATCH_GROUPTIME) Values (transp.DISPATCHGROUPSEQ.nextval, '09:00 AM', TO_DATE('01/01/1970 09:00:00', 'MM/DD/YYYY HH24:MI:SS'));
Insert into TRANSP.DISPATCH_GROUP  (ID, NAME, DISPATCH_GROUPTIME) Values (transp.DISPATCHGROUPSEQ.nextval, '10:30 AM', TO_DATE('01/01/1970 10:30:00', 'MM/DD/YYYY HH24:MI:SS'));
Insert into TRANSP.DISPATCH_GROUP  (ID, NAME, DISPATCH_GROUPTIME) Values (transp.DISPATCHGROUPSEQ.nextval, '11:00 AM', TO_DATE('01/01/1970 11:00:00', 'MM/DD/YYYY HH24:MI:SS'));
Insert into TRANSP.DISPATCH_GROUP  (ID, NAME, DISPATCH_GROUPTIME) Values (transp.DISPATCHGROUPSEQ.nextval, '12:30 PM', TO_DATE('01/01/1970 12:30:00', 'MM/DD/YYYY HH24:MI:SS'));
Insert into TRANSP.DISPATCH_GROUP  (ID, NAME, DISPATCH_GROUPTIME) Values (transp.DISPATCHGROUPSEQ.nextval, '01:00 PM', TO_DATE('01/01/1970 13:00:00', 'MM/DD/YYYY HH24:MI:SS'));
Insert into TRANSP.DISPATCH_GROUP  (ID, NAME, DISPATCH_GROUPTIME) Values (transp.DISPATCHGROUPSEQ.nextval, '01:30 PM', TO_DATE('01/01/1970 13:30:00', 'MM/DD/YYYY HH24:MI:SS'));
Insert into TRANSP.DISPATCH_GROUP  (ID, NAME, DISPATCH_GROUPTIME) Values (transp.DISPATCHGROUPSEQ.nextval, '01:45 PM', TO_DATE('01/01/1970 13:45:00', 'MM/DD/YYYY HH24:MI:SS'));
Insert into TRANSP.DISPATCH_GROUP  (ID, NAME, DISPATCH_GROUPTIME) Values (transp.DISPATCHGROUPSEQ.nextval, '02:00 PM', TO_DATE('01/01/1970 14:00:00', 'MM/DD/YYYY HH24:MI:SS'));
Insert into TRANSP.DISPATCH_GROUP  (ID, NAME, DISPATCH_GROUPTIME) Values (transp.DISPATCHGROUPSEQ.nextval, '02:30 PM', TO_DATE('01/01/1970 14:30:00', 'MM/DD/YYYY HH24:MI:SS'));
Insert into TRANSP.DISPATCH_GROUP  (ID, NAME, DISPATCH_GROUPTIME) Values (transp.DISPATCHGROUPSEQ.nextval, '03:00 PM', TO_DATE('01/01/1970 15:00:00', 'MM/DD/YYYY HH24:MI:SS'));
Insert into TRANSP.DISPATCH_GROUP  (ID, NAME, DISPATCH_GROUPTIME) Values (transp.DISPATCHGROUPSEQ.nextval, '03:30 PM', TO_DATE('01/01/1970 15:30:00', 'MM/DD/YYYY HH24:MI:SS'));
Insert into TRANSP.DISPATCH_GROUP  (ID, NAME, DISPATCH_GROUPTIME) Values (transp.DISPATCHGROUPSEQ.nextval, '04:30 PM', TO_DATE('01/01/1970 16:30:00', 'MM/DD/YYYY HH24:MI:SS'));
Insert into TRANSP.DISPATCH_GROUP  (ID, NAME, DISPATCH_GROUPTIME) Values (transp.DISPATCHGROUPSEQ.nextval, '05:00 PM', TO_DATE('01/01/1970 17:00:00', 'MM/DD/YYYY HH24:MI:SS'));
Insert into TRANSP.DISPATCH_GROUP  (ID, NAME, DISPATCH_GROUPTIME) Values (transp.DISPATCHGROUPSEQ.nextval, '06:30 PM', TO_DATE('01/01/1970 18:30:00', 'MM/DD/YYYY HH24:MI:SS'));

commit;