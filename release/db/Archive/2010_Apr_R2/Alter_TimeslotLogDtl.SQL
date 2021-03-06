alter table   DLV.TIMESLOT_LOG_DTL
add
   (
   ADD_DISTANCE       NUMBER(10),
  ADD_RUNTIME        NUMBER(10),
  ADD_STOPCOST       NUMBER(10),
  CAPACITY           NUMBER(10),
  COSTPERMILE        NUMBER(10),
  FIXED_RT_COST      NUMBER(10),
  MAXRUNTIME         NUMBER(10),
  OT_HOURLY_WAGE     NUMBER(10),
  PERCENT_AVAIL      NUMBER(5,2),
  PREF_RUNTIME       NUMBER(10),
  REG_HOURLY_WAGE    NUMBER(10),
  REG_WAGE_SECS      NUMBER(10),
  ROUTE_ID           NUMBER(10),
  STOP_SEQ           NUMBER(10),
  TOTAL_DISTANCE     NUMBER(10),
  TOTAL_PU_QTY       NUMBER(10),
  TOTAL_QTY          NUMBER(10),
  TOTAL_ROUTE_COST   NUMBER(10),
  TOTAL_RUNTIME      NUMBER(10),
  TOTAL_SVC_TIME     NUMBER(10),
  TOTAL_TRAVEL_TIME  NUMBER(10),
  TOTAL_WAIT_TIME    NUMBER(10),
  IS_AVAIL           VARCHAR2(1 BYTE),
   IS_FILTERED        VARCHAR2(1 BYTE),
  IS_MISSED_TW       VARCHAR2(1 BYTE)
   );