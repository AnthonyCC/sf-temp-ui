CREATE OR REPLACE PROCEDURE FDSTORE_PRDA.TIMESLOT_SNAPSHOT IS
BEGIN
INSERT INTO DLV.CAPACITY_SNAPSHOT (ID,
SNAPSHOT_TIME,
DELIVERY_DATE,
ZONE_CODE,
TIMESLOT_START,
TIMESLOT_END,
CUTOFF_TIME,
PREMIUM_CUTOFF_TIME,
LOYAL_RELEASE_TIME,
PREMIUM_LOYAL_RELEASE_TIME,
CAPACITY_TOTAL,
CAPACITY_BASE,
CAPACITY_LOYAL,
PREMIUM_CAPACITY,
PREMIUM_CT_CAPACITY,
MANUALLY_CLOSED,
RSV_BASE_REG_STD_COMM,
RSV_BASE_REG_STD_UNCO,
RSV_BASE_REG_PRE_COMM,
RSV_BASE_REG_PRE_UNCO,
RSV_PREMIUM_BASE_REG_STD_COMM,
RSV_PREMIUM_BASE_REG_STD_UNCO,
RSV_BASE_CT_STD_COMM,
RSV_BASE_CT_STD_UNCO,
RSV_PREMIUM_BASE_CT_STD_COMM,
RSV_PREMIUM_BASE_CT_STD_UNCO,
RSV_BASE_CT_PRE_COMM,
RSV_BASE_CT_PRE_UNCO,
RSV_LOYAL_CT_STD_COMM,
RSV_LOYAL_CT_STD_UNCO,
RSV_LOYAL_CTONLY_STD_COMM,
RSV_LOYAL_CTONLY_STD_UNCO,
RSV_LOYAL_REGONLY_STD_COMM,
RSV_LOYAL_REGONLY_STD_UNCO,
RSV_PREMIUM_CT_STD_COMM,
RSV_PREMIUM_CT_STD_UNCO,
RSV_PREMIUM_CTONLY_STD_COMM,
RSV_PREMIUM_CTONLY_STD_UNCO,
RSV_PREMIUMCT_REGONLY_STD_COMM,
RSV_PREMIUMCT_REGONLY_STD_UNCO,
IS_PRIMARY,
SERVICE_TYPE
)
select
DLV.SYSTEM_SEQ.nextval as ID,
sysdate as SNAPSHOT_TIME,
ts.BASE_DATE as DELIVERY_DATE,
z.zone_code as ZONE_CODE,
TO_DATE(TO_CHAR(ts.base_date, 'YYYY-MM-DD')||' '||to_char(ts.start_time, 'HH24:MI:SS'), 'YYYY-MM-DD HH24:MI:SS') as TIMESLOT_START,
TO_DATE(TO_CHAR(ts.base_date, 'YYYY-MM-DD')||' '||to_char(ts.end_time, 'HH24:MI:SS'), 'YYYY-MM-DD HH24:MI:SS') as TIMESLOT_END,
TO_DATE(TO_CHAR(ts.base_date - RELATIVE_CUTOFF_DAYS, 'YYYY-MM-DD')||' '||to_char(ts.cutoff_time, 'HH24:MI:SS'), 'YYYY-MM-DD HH24:MI:SS') as CUTOFF_TIME,
case when ts.premium_cutoff_time is not null then TO_DATE(TO_CHAR(ts.base_date, 'YYYY-MM-DD')||' '||to_char(ts.premium_cutoff_time, 'HH24:MI:SS'), 'YYYY-MM-DD HH24:MI:SS') else null end as PREMIUM_CUTOFF_TIME,
TO_DATE(TO_CHAR(ts.base_date - RELATIVE_CUTOFF_DAYS, 'YYYY-MM-DD')||' '||to_char(ts.cutoff_time - (z.ct_release_time/60/24), 'HH24:MI:SS'), 'YYYY-MM-DD HH24:MI:SS') as LOYAL_RELEASE_TIME,
case when ts.premium_cutoff_time is not null then TO_DATE(TO_CHAR(ts.base_date, 'YYYY-MM-DD')||' '||to_char(ts.premium_cutoff_time - (z.premium_ct_release_time/60/24), 'HH24:MI:SS'), 'YYYY-MM-DD HH24:MI:SS') else null end as PREMIUM_LOYAL_RELEASE_TIME,
ts.CAPACITY as CAPACITY_TOTAL,
ts.CAPACITY - ts.CT_CAPACITY - ts.PREMIUM_CAPACITY as CAPACITY_BASE,
ts.CT_CAPACITY as CAPACITY_LOYAL,
ts.PREMIUM_CAPACITY as PREMIUM_CAPACITY,
ts.PREMIUM_CT_CAPACITY as PREMIUM_CT_CAPACITY,
ts.IS_CLOSED,
(select count(*) from dlv.reservation where timeslot_id=ts.id and status_code = 10 and chefstable = ' ' and type='STD' AND CLASS IS NULL) -(select count(*) from dlv.reservation r, cust.profile p, cust.fdcustomer f where timeslot_id=ts.id and status_code = 10 and chefstable = ' ' and type='STD' AND CLASS IS NULL and p.customer_id=f.id and f.erp_customer_id =r.customer_id and p.profile_name= 'ChefsTable' and profile_value='1') as RSV_BASE_REG_STD_COMM,
(select count(*) from dlv.reservation where timeslot_id=ts.id and status_code = 5 and chefstable = ' ' and type='STD' AND CLASS IS NULL) - (select count(*) from dlv.reservation r, cust.profile p, cust.fdcustomer f where timeslot_id=ts.id and status_code = 5 and chefstable = ' ' and type='STD' AND CLASS IS NULL and p.customer_id=f.id and f.erp_customer_id =r.customer_id and p.profile_name= 'ChefsTable' and profile_value='1')as RSV_BASE_REG_STD_UNCO,
(select count(*) from dlv.reservation where timeslot_id=ts.id and status_code = 10 and type<>'STD') -(select count(*) from dlv.reservation r, cust.profile p, cust.fdcustomer f where timeslot_id=ts.id and status_code = 10 and type<>'STD' and p.customer_id=f.id and f.erp_customer_id =r.customer_id and p.profile_name= 'ChefsTable' and profile_value='1') as RSV_BASE_REG_PRE_COMM,
(select count(*) from dlv.reservation where timeslot_id=ts.id and status_code = 5 and type<>'STD') - (select count(*) from dlv.reservation r, cust.profile p, cust.fdcustomer f where timeslot_id=ts.id and status_code = 5 and type<>'STD' and p.customer_id=f.id and f.erp_customer_id =r.customer_id and p.profile_name= 'ChefsTable' and profile_value='1') as RSV_BASE_REG_PRE_UNCO,
(select count(*) from dlv.reservation where timeslot_id=ts.id and status_code = 10 and chefstable = ' ' and type='STD' AND CLASS = 'P' ) -(select count(*) from dlv.reservation r, cust.profile p, cust.fdcustomer f where timeslot_id=ts.id and status_code = 10 and chefstable = ' ' AND CLASS = 'P' and type='STD' and p.customer_id=f.id and f.erp_customer_id =r.customer_id and p.profile_name= 'ChefsTable' and profile_value='1') as RSV_PREMIUM_BASE_REG_STD_COMM,
(select count(*) from dlv.reservation where timeslot_id=ts.id and status_code = 5 and chefstable = ' ' and type='STD' AND CLASS = 'P' ) - (select count(*) from dlv.reservation r, cust.profile p, cust.fdcustomer f where timeslot_id=ts.id and status_code = 5 and chefstable = ' ' AND CLASS = 'P' and type='STD' and p.customer_id=f.id and f.erp_customer_id =r.customer_id and p.profile_name= 'ChefsTable' and profile_value='1')as RSV_PREMIUM_BASE_REG_STD_UNCO,
(select count(*) from dlv.reservation r, cust.profile p, cust.fdcustomer f where timeslot_id=ts.id and status_code = 10 and chefstable = ' ' AND CLASS IS NULL and type='STD' and p.customer_id=f.id and f.erp_customer_id =r.customer_id and p.profile_name= 'ChefsTable' and profile_value='1') as RSV_BASE_CT_STD_COMM,
(select count(*) from dlv.reservation r, cust.profile p, cust.fdcustomer f where timeslot_id=ts.id and status_code = 5 and chefstable = ' ' AND CLASS IS NULL and type='STD' and p.customer_id=f.id and f.erp_customer_id =r.customer_id and p.profile_name= 'ChefsTable' and profile_value='1') as RSV_BASE_CT_STD_UNCO,
(select count(*) from dlv.reservation r, cust.profile p, cust.fdcustomer f where timeslot_id=ts.id and status_code = 10 and chefstable = ' ' AND CLASS = 'P' and type='STD' and p.customer_id=f.id and f.erp_customer_id =r.customer_id and p.profile_name= 'ChefsTable' and profile_value='1') as RSV_PREMIUM_BASE_CT_STD_COMM,
(select count(*) from dlv.reservation r, cust.profile p, cust.fdcustomer f where timeslot_id=ts.id and status_code = 5 and chefstable = ' ' AND CLASS = 'P' and type='STD' and p.customer_id=f.id and f.erp_customer_id =r.customer_id and p.profile_name= 'ChefsTable' and profile_value='1') as RSV_PREMIUM_BASE_CT_STD_UNCO,
(select count(*) from dlv.reservation r, cust.profile p, cust.fdcustomer f where timeslot_id=ts.id and status_code = 10 and type<>'STD' and p.customer_id=f.id and f.erp_customer_id =r.customer_id and p.profile_name= 'ChefsTable' and profile_value='1') as RSV_BASE_CT_PRE_COMM,
(select count(*) from dlv.reservation r, cust.profile p, cust.fdcustomer f where timeslot_id=ts.id and status_code = 5 and type<>'STD' and p.customer_id=f.id and f.erp_customer_id =r.customer_id and p.profile_name= 'ChefsTable' and profile_value='1') as RSV_BASE_CT_PRE_UNCO,
(select count(*) from dlv.reservation where timeslot_id=ts.id and status_code = 10 and chefstable = 'X' AND CLASS IS NULL and type='STD') as RSV_LOYAL_CT_STD_COMM,
(select count(*) from dlv.reservation where timeslot_id=ts.id and status_code = 5 and chefstable = 'X' AND CLASS IS NULL and type='STD') as RSV_LOYAL_CT_STD_UNCO,
(select count(*) from dlv.reservation where timeslot_id=ts.id and status_code = 10 and chefstable = 'X' AND CLASS IS NULL and type='STD' and CT_DELIVERY_PROFILE is null) as RSV_LOYAL_CTONLY_STD_COMM,
 (select count(*) from dlv.reservation where timeslot_id=ts.id and status_code = 5 and chefstable = 'X' AND CLASS IS NULL and type='STD' and CT_DELIVERY_PROFILE is null) as RSV_LOYAL_CTONLY_STD_UNCO,
 (select count(*) from dlv.reservation where timeslot_id=ts.id and status_code = 10 and chefstable = 'X' AND CLASS IS NULL and type='STD' and CT_DELIVERY_PROFILE is not null) as RSV_LOYAL_REGONLY_STD_COMM,
 (select count(*) from dlv.reservation where timeslot_id=ts.id and status_code = 5 and chefstable = 'X' AND CLASS IS NULL and type='STD' and CT_DELIVERY_PROFILE is not null) as RSV_LOYAL_REGONLY_STD_UNCO,
 (select count(*) from dlv.reservation where timeslot_id=ts.id and status_code = 10 and chefstable = 'X' AND CLASS = 'PC' and type='STD') as RSV_PREMIUM_CT_STD_COMM,
(select count(*) from dlv.reservation where timeslot_id=ts.id and status_code = 5 and chefstable = 'X' AND CLASS = 'PC' and type='STD') as RSV_PREMIUM_CT_STD_UNCO,
(select count(*) from dlv.reservation where timeslot_id=ts.id and status_code = 10 and chefstable = 'X' AND CLASS = 'PC' and type='STD' and CT_DELIVERY_PROFILE is null) as RSV_PREMIUM_CTONLY_STD_COMM,
 (select count(*) from dlv.reservation where timeslot_id=ts.id and status_code = 5 and chefstable = 'X' AND CLASS = 'PC' and type='STD' and CT_DELIVERY_PROFILE is null) as RSV_PREMIUM_CTONLY_STD_UNCO,
 (select count(*) from dlv.reservation where timeslot_id=ts.id and status_code = 10 and chefstable = 'X' AND CLASS = 'PC' and type='STD' and CT_DELIVERY_PROFILE is not null) as RSV_PREMIUMCT_REGONLY_STD_COMM,
 (select count(*) from dlv.reservation where timeslot_id=ts.id and status_code = 5 and chefstable = 'X' AND CLASS = 'PC' and type='STD' and CT_DELIVERY_PROFILE is not null) as RSV_PREMIUMCT_REGONLY_STD_UNCO,
ts.IS_PRIMARY,
ts.SERVICE_TYPE
from
dlv.TIMESLOT ts,
dlv.ZONE z
where
ts.base_date >= trunc(sysdate) and z.id=ts.zone_id;
END TIMESLOT_SNAPSHOT;
/
