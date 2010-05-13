---[APPDEV-995]-Click 2 call 

CREATE OR REPLACE TYPE cust.click2CallZoneCodes AS VARRAY(1000)  OF varchar2(5);
create table cust.Click2Call(id varchar2(16) primary key, status char(1),eligible_customers varchar2(200),delivery_zones click2CallZoneCodes,nextday_timeslot char(1),userId varchar2(100),cro_mod_date Date);
create table cust.Click2Call_TIME(day_name varchar2(15),start_time varchar2(15), end_time varchar2(15), show_flag char(1), click2call_id references cust.Click2Call(id));

grant execute on cust.click2CallZoneCodes to fdstore_stprd01;
grant execute on cust.click2CallZoneCodes to fdstore_ststg01;
grant execute on cust.click2CallZoneCodes to appdev;

grant insert,select,delete,update on cust.Click2Call to fdstore_stprd01;
grant insert,select,delete,update on cust.Click2Call to fdstore_ststg01;
grant select on cust.Click2Call to appdev;

grant insert,select,delete,update on cust.Click2Call_TIME to fdstore_stprd01;
grant insert,select,delete,update on cust.Click2Call_TIME to fdstore_ststg01;
grant select on cust.Click2Call_TIME to appdev;

insert into cust.click2call(id,status,eligible_customers,delivery_zones,nextday_timeslot,userid,cro_mod_date) values(cust.system_seq.nextval,'Y','nct_dp,nct_ndp',cust.click2CallZoneCodes('090','070','560','505','506','501','040'),'Y','admin',sysdate);
insert into cust.click2Call_time(day_name,start_time,end_time,show_flag,click2call_id) values('SUN','9','22','Y',(select max(id) from cust.click2call));
insert into cust.click2Call_time(day_name,start_time,end_time,show_flag,click2call_id) values('MON','9','22','Y',(select max(id) from cust.click2call));
insert into cust.click2Call_time(day_name,start_time,end_time,show_flag,click2call_id) values('TUE','9','22','Y',(select max(id) from cust.click2call));
insert into cust.click2Call_time(day_name,start_time,end_time,show_flag,click2call_id) values('WED','9','22','Y',(select max(id) from cust.click2call));
insert into cust.click2Call_time(day_name,start_time,end_time,show_flag,click2call_id) values('THU','9','22','Y',(select max(id) from cust.click2call));
insert into cust.click2Call_time(day_name,start_time,end_time,show_flag,click2call_id) values('FRI','9','20','Y',(select max(id) from cust.click2call));
insert into cust.click2Call_time(day_name,start_time,end_time,show_flag,click2call_id) values('SAT','9','22','Y',(select max(id) from cust.click2call));