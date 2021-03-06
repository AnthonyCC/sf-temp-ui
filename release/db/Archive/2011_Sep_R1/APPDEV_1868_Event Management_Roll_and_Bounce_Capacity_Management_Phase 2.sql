
CREATE TABLE MIS.TIMESLOT_EVENT_HDR
(
  ORDER_ID                VARCHAR2(16 BYTE)     NOT NULL,
  CUSTOMER_ID             VARCHAR2(16 BYTE),
  EVENTTYPE               VARCHAR2(32 BYTE)     NOT NULL,
  ID                      VARCHAR2(16 BYTE)     NOT NULL,
  EVENT_DTM               DATE                  NOT NULL,
  RESPONSE_TIME           INTEGER,
  COMMENTS                VARCHAR2(512 BYTE),
  RESERVATION_ID          VARCHAR2(16 BYTE),
  TRANSACTIONSOURCE       VARCHAR2(3 BYTE),
  DLVPASSAPPLIED          VARCHAR2(1 BYTE),
  DELIVERYCHARGE          NUMBER(10,2),
  ISDELIVERYCHARGEWAIVED  VARCHAR2(1 BYTE),
  ZONECTACTIVE            VARCHAR2(1 BYTE)
);



ALTER TABLE MIS.TIMESLOT_EVENT_HDR ADD (
  PRIMARY KEY
 (ID));
 
 CREATE INDEX MIS.IDX_TIMESLOT_EVENT_DTM ON MIS.TIMESLOT_EVENT_HDR
(EVENT_DTM);
 
CREATE TABLE MIS.TIMESLOT_EVENT_DTL
(
  TIMESLOT_LOG_ID        VARCHAR2(16 BYTE),
  BASE_DATE              DATE,
  START_TIME             DATE,
  END_TIME               DATE,
  ADD_DISTANCE           NUMBER(10),
  ADD_RUNTIME            NUMBER(10),
  ADD_STOPCOST           NUMBER(10),
  CAPACITY               NUMBER(10),
  COSTPERMILE            NUMBER(10),
  FIXED_RT_COST          NUMBER(10),
  MAXRUNTIME             NUMBER(10),
  OT_HOURLY_WAGE         NUMBER(10),
  PERCENT_AVAIL          NUMBER(5,2),
  PREF_RUNTIME           NUMBER(10),
  REG_HOURLY_WAGE        NUMBER(10),
  REG_WAGE_SECS          NUMBER(10),
  ROUTE_ID               NUMBER(10),
  STOP_SEQ               NUMBER(10),
  TOTAL_DISTANCE         NUMBER(10),
  TOTAL_PU_QTY           NUMBER(10),
  TOTAL_QTY              NUMBER(10),
  TOTAL_ROUTE_COST       NUMBER(10),
  TOTAL_RUNTIME          NUMBER(10),
  TOTAL_SVC_TIME         NUMBER(10),
  TOTAL_TRAVEL_TIME      NUMBER(10),
  TOTAL_WAIT_TIME        NUMBER(10),
  IS_AVAIL               VARCHAR2(1 BYTE),
  IS_FILTERED            VARCHAR2(1 BYTE),
  IS_MISSED_TW           VARCHAR2(1 BYTE),
  ZONE_CODE              VARCHAR2(8 BYTE),
  WS_AMOUNT              NUMBER(10,2),
  ALCOHOL_RESTRICTION    VARCHAR2(1 BYTE),
  HOLIDAY_RESTRICTION    VARCHAR2(1 BYTE),
  ECOFRIENDLYSLOT        VARCHAR2(1 BYTE),
  NEIGHBOURHOODSLOT      VARCHAR2(1 BYTE),
  TOTALCAPACITY          NUMBER(10),
  CTCAPACITY             NUMBER(10),
  MANUALLY_CLOSED        VARCHAR2(1 BYTE),
  CUTOFF                 DATE,
  STOREFRONT_AVL         VARCHAR2(1 BYTE),
  CT_ALLOCATED           NUMBER(10),
  TOTAL_ALLOCATED        NUMBER(10),
  WAVE_VEHICLES          NUMBER(10),
  WAVE_VEHICLES_IN_USE   NUMBER(10),
  WAVE_STARTTIME         DATE,
  UNAVAILABILITY_REASON  VARCHAR2(100 BYTE),
  WAVE_ORDERS_TAKEN      NUMBER(10),
  TOTAL_QUANTITIES       NUMBER(10),
  NEWROUTE               VARCHAR2(1 BYTE),
  CAPACITIES             NUMBER(10),
  GEORESTRICTED          VARCHAR2(1 BYTE),
  IS_EMPTY               VARCHAR2(1 BYTE)       DEFAULT 0
);


ALTER TABLE MIS.TIMESLOT_EVENT_DTL ADD (
CONSTRAINT TIMESLOT_EVENT_FK 
  FOREIGN KEY (TIMESLOT_LOG_ID) 
 REFERENCES MIS.TIMESLOT_EVENT_HDR (ID));
 
 
 CREATE INDEX MIS.TIMESLOT_EVENT_DTL_IDX_01 ON MIS.TIMESLOT_EVENT_DTL
(TIMESLOT_LOG_ID);
 


CREATE TABLE MIS.BOUNCE_EVENT
(
  ID             NUMBER,
  CUSTOMER_ID    VARCHAR2(20 BYTE),
  STATUS         VARCHAR2(10 BYTE),
  LASTUPDATE     DATE,
  CREATEDATE     DATE,
  ZONE           VARCHAR2(8 BYTE),
  CUTOFF         DATE,
  LOG_ID         VARCHAR2(16 BYTE),
  DELIVERY_DATE  DATE,
  TYPE           VARCHAR2(20 BYTE)
);




CREATE INDEX MIS.IDX__BOUNCE_DELIVERYDATE ON MIS.BOUNCE_EVENT
(DELIVERY_DATE);


ALTER TABLE MIS.BOUNCE_EVENT ADD (
  CONSTRAINT BOUNCE_PK
 PRIMARY KEY
 (ID));


CREATE TABLE MIS.ROLL_EVENT
(
  CUSTOMER_ID      VARCHAR2(100 BYTE),
  CREATEDATE       DATE,
  UNAVAILABLE_PCT  FLOAT(126),
  ZONE             VARCHAR2(8 BYTE),
  ID               NUMBER,
  CUTOFF           DATE,
  LOG_ID           VARCHAR2(16 BYTE),
  DELIVERY_DATE    DATE
);


CREATE INDEX MIS.IDX__ROLL_DELIVERYDATE ON MIS.ROLL_EVENT
(DELIVERY_DATE);


ALTER TABLE MIS.ROLL_EVENT ADD (
  PRIMARY KEY
 (ID));


CREATE TABLE MIS.SESSION_EVENT
(
  CUSTOMER_ID        VARCHAR2(16 BYTE),
  LOGIN_TIME         DATE,
  LOGOUT_TIME        DATE,
  CUTOFF             DATE,
  ZONE               VARCHAR2(8 BYTE),
  LAST_GET_TIMESLOT  VARCHAR2(16 BYTE),
  IS_TIMEOUT         VARCHAR2(1 BYTE)           DEFAULT 'Y',
  AVAIL_COUNT        NUMBER(3),
  SOLD_COUNT         NUMBER(3),
  LAST_GETTYPE       VARCHAR2(20 BYTE),
  ORDER_ID           VARCHAR2(16 BYTE),
  HIDDEN_COUNT       NUMBER(3)
);


CREATE INDEX MIS.IDX_01_SESSION_EVENT ON MIS.SESSION_EVENT
(CUSTOMER_ID);


CREATE INDEX MIS.IDX__SESSION_LOGOUT ON MIS.SESSION_EVENT
(LOGOUT_TIME);
 
CREATE TABLE MIS.ORDER_RATE
(
  ZONE                      VARCHAR2(10 BYTE),
  CUTOFF                    DATE,
  TIMESLOT_START            DATE,
  TIMESLOT_END              DATE,
  ORDER_COUNT               FLOAT(126),
  PROJECTED_COUNT           FLOAT(126),
  ACTUAL_SO                 DATE,
  PROJECT_SO                DATE,
  SNAPSHOT_TIME             DATE,
  DELIVERY_DATE             DATE,
  WEIGHTED_PROJECTED_COUNT  FLOAT(126),
  CAPACITY                  NUMBER(10),
  ORDERS_EXPECTED           FLOAT(126)
);


CREATE INDEX MIS.IDX_TIMESLOT_END ON MIS.ORDER_RATE
(TIMESLOT_END);


CREATE INDEX MIS.IDX_TIMESLOT_START ON MIS.ORDER_RATE
(TIMESLOT_START);


CREATE INDEX MIS.IDX_ZONE ON MIS.ORDER_RATE
(ZONE);

CREATE SEQUENCE MIS.EVENT_DETECTION_SEQUENCE;

CREATE SEQUENCE MIS.TIMESLOT_LOG_SEQUENCE;

  
  

 