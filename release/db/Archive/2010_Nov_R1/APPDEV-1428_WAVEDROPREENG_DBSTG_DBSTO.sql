alter table TRANSP.SCRIB add  (CUTOFF_DATETIME DATE);
alter table TRANSP.PLAN add  (LAST_DLV_TIME DATE , CUTOFF_DATETIME DATE DEFAULT to_date('1970/01/01:06:00:00PM', 'yyyy/mm/dd:hh:mi:ssam'));
alter table TRANSP.ZONE add (LOADING_PRIORITY INTEGER);
alter table TRANSP.HANDOFF_BATCHROUTE add (DISPATCHTIME   DATE, DISPATCHSEQUENCE INTEGER);

CREATE TABLE TRANSP.HANDOFF_BATCHDISPATCH
(
  DELIVERY_DATE   DATE             NOT NULL,
  DISPATCHTIME   DATE             NOT NULL,
  PLANNED_RESOURCES INTEGER,
  ACTUAL_RESOURCES INTEGER,
  STATUS VARCHAR2(10 BYTE)
);

ALTER TABLE TRANSP.HANDOFF_BATCHDISPATCH ADD (CONSTRAINT PK_HANDOFF_BATCHDISPATCH  PRIMARY KEY (DELIVERY_DATE, DISPATCHTIME));

update transp.scrib s set S.CUTOFF_DATETIME = (select t.CUTOFF_TIME from dlv.timeslot t, dlv.zone z where t.BASE_DATE = S.SCRIB_DATE and t.ZONE_ID = z.ID and Z.ZONE_CODE = S.ZONE_ID 
        and t.START_TIME = S.FIRST_WINDOW_TIME );
        
update transp.plan s set S.CUTOFF_DATETIME = (select t.CUTOFF_TIME from dlv.timeslot t, dlv.zone z where t.BASE_DATE = S.PLAN_DATE and t.ZONE_ID = z.ID and Z.ZONE_CODE = S.ZONE 
        and t.START_TIME = S.FIRST_DLV_TIME );
        
update transp.plan s set S.LAST_DLV_TIME = (select t.END_WINDOW_TIME from transp.scrib t where t.SCRIB_DATE = S.PLAN_DATE and t.ZONE_ID = s.ZONE and t.START_TIME = S.START_TIME 
        and t.FIRST_WINDOW_TIME = S.FIRST_DLV_TIME and rownum = 1 );         


