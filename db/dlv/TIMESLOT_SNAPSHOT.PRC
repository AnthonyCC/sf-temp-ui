CREATE OR REPLACE PROCEDURE TIMESLOT_SNAPSHOT IS
BEGIN
INSERT INTO DLV.CAPACITY_SNAPSHOT 
	   (ID,SNAPSHOT_TIME,DELIVERY_DATE,ZONE_CODE,TIMESLOT_START,TIMESLOT_END,CUTOFF_TIME,CT_RELEASE_TIME,
	   CAPACITY_TOTAL,CAPACITY_BASE,CAPACITY_CT,RSV_STD_BASE_COMM,RSV_STD_BASE_NCOM,RSV_STD_CT_COMM,
	   RSV_STD_CT_NCOM,RSV_PRE_BASE_COMM,RSV_PRE_BASE_NCOM,RSV_PRE_CT_COMM,RSV_PRE_CT_NCOM)
select 
  DLV.SYSTEM_SEQ.nextval as ID,
  sysdate as SNAPSHOT_TIME,
  ts.BASE_DATE as DELIVERY_DATE,
  z.zone_code as ZONE_CODE,
  TO_DATE(TO_CHAR(ts.base_date, 'YYYY-MM-DD')||' '||to_char(ts.start_time, 'HH24:MI:SS'), 'YYYY-MM-DD HH24:MI:SS') as TIMESLOT_START,
  TO_DATE(TO_CHAR(ts.base_date, 'YYYY-MM-DD')||' '||to_char(ts.end_time, 'HH24:MI:SS'), 'YYYY-MM-DD HH24:MI:SS') as TIMESLOT_END,
  TO_DATE(TO_CHAR(ts.base_date - 1, 'YYYY-MM-DD')||' '||to_char(ts.cutoff_time, 'HH24:MI:SS'), 'YYYY-MM-DD HH24:MI:SS') as CUTOFF_TIME,
  TO_DATE(TO_CHAR(ts.base_date - 1, 'YYYY-MM-DD')||' '||to_char(ts.cutoff_time - (z.ct_release_time/60/24), 'HH24:MI:SS'), 'YYYY-MM-DD HH24:MI:SS') as CT_RELEASE_TIME,
  ts.CAPACITY as CAPACITY_TOTAL,
  ts.CAPACITY - ts.CT_CAPACITY as CAPACITY_BASE,
  ts.CT_CAPACITY as CAPACITY_CT,
  (select count(*) from dlv.reservation where timeslot_id=ts.id and status_code = 10 and chefstable = ' ' and type='STD') as RSV_STD_BASE_COMM,
  (select count(*) from dlv.reservation where timeslot_id=ts.id and status_code = 5  and chefstable = ' ' and type='STD') as RSV_STD_BASE_NCOMM,
  (select count(*) from dlv.reservation where timeslot_id=ts.id and status_code = 10 and chefstable = 'X' and type='STD') as RSV_STD_CT_COMM,
  (select count(*) from dlv.reservation where timeslot_id=ts.id and status_code = 5  and chefstable = 'X' and type='STD') as RSV_STD_CT_NCOMM,
  (select count(*) from dlv.reservation where timeslot_id=ts.id and status_code = 10 and chefstable = ' ' and type<>'STD') as RSV_PRE_BASE_COMM,
  (select count(*) from dlv.reservation where timeslot_id=ts.id and status_code = 5  and chefstable = ' ' and type<>'STD') as RSV_PRE_BASE_NCOMM,
  (select count(*) from dlv.reservation where timeslot_id=ts.id and status_code = 10 and chefstable = 'X' and type<>'STD') as RSV_PRE_CT_COMM,
  (select count(*) from dlv.reservation where timeslot_id=ts.id and status_code = 5  and chefstable = 'X' and type<>'STD') as RSV_PRE_CT_NCOMM
from 
  dlv.TIMESLOT ts,
  dlv.ZONE z
where
  ts.base_date >= trunc(sysdate)+1 and z.id=ts.zone_id;
END TIMESLOT_SNAPSHOT;
/