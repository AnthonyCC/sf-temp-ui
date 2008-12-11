CREATE TABLE DLV.DELIVERY_BUILDING_DTL
(
  DELIVERY_BUILDING_ID  VARCHAR2(16 BYTE)       NOT NULL,
  ADDR_TYPE             VARCHAR2(16 BYTE),
  COMPANY_NAME          VARCHAR2(255 BYTE),
  DIFFICULT_TO_DELIVER  VARCHAR2(1 BYTE),
  DIFFICULT_REASON      VARCHAR2(255 BYTE),
  EXTRA_TIME_NEEDED     NUMBER(2),
  DOORMAN               VARCHAR2(1 BYTE),
  WALKUP                VARCHAR2(1 BYTE),
  ELEVATOR              VARCHAR2(1 BYTE),
  HOUSE                 VARCHAR2(1 BYTE),
  SVC_ENT               VARCHAR2(1 BYTE),
  SVC_SCRUBBED_STREET   VARCHAR2(255 BYTE),
  SVC_CITY              VARCHAR2(16 BYTE),
  SVC_STATE             VARCHAR2(16 BYTE),
  SVC_ZIP               VARCHAR2(10 BYTE),
  INCLUDE_MON           VARCHAR2(1 Byte),
  INCLUDE_TUE           VARCHAR2(1 Byte),
  INCLUDE_WED           VARCHAR2(1 Byte),
  INCLUDE_THU           VARCHAR2(1 Byte),
  INCLUDE_FRI           VARCHAR2(1 Byte),
  INCLUDE_SAT           VARCHAR2(1 Byte),
  INCLUDE_SUN           VARCHAR2(1 Byte),
  SVC_INCLUDE_MON       VARCHAR2(1 Byte),
  SVC_INCLUDE_TUE       VARCHAR2(1 Byte),
  SVC_INCLUDE_WED       VARCHAR2(1 Byte),
  SVC_INCLUDE_THU       VARCHAR2(1 Byte),
  SVC_INCLUDE_FRI       VARCHAR2(1 Byte),
  SVC_INCLUDE_SAT       VARCHAR2(1 Byte),
  SVC_INCLUDE_SUN       VARCHAR2(1 Byte),
  HOURS_OPEN_MON        DATE,
  HOURS_OPEN_TUE        DATE,
  HOURS_OPEN_WED        DATE,
  HOURS_OPEN_THU        DATE,
  HOURS_OPEN_FRI        DATE,
  HOURS_OPEN_SAT        DATE,
  HOURS_OPEN_SUN        DATE,
  HOURS_CLOSE_MON       DATE,
  HOURS_CLOSE_TUE       DATE,
  HOURS_CLOSE_WED       DATE,
  HOURS_CLOSE_THU       DATE,
  HOURS_CLOSE_FRI       DATE,
  HOURS_CLOSE_SAT       DATE,
  HOURS_CLOSE_SUN       DATE,  
  SVC_HOURS_OPEN_MON    DATE,
  SVC_HOURS_OPEN_TUE    DATE,
  SVC_HOURS_OPEN_WED    DATE,
  SVC_HOURS_OPEN_THU    DATE,
  SVC_HOURS_OPEN_FRI    DATE,
  SVC_HOURS_OPEN_SAT    DATE,
  SVC_HOURS_OPEN_SUN    DATE,
  SVC_HOURS_CLOSE_MON   DATE,
  SVC_HOURS_CLOSE_TUE   DATE,
  SVC_HOURS_CLOSE_WED   DATE,
  SVC_HOURS_CLOSE_THU   DATE,
  SVC_HOURS_CLOSE_FRI   DATE,
  SVC_HOURS_CLOSE_SAT   DATE,
  SVC_HOURS_CLOSE_SUN   DATE,
  
  COMMENT_MON           VARCHAR2(30 Byte),
  COMMENT_TUE           VARCHAR2(30 Byte),
  COMMENT_WED           VARCHAR2(30 Byte),
  COMMENT_THU           VARCHAR2(30 Byte),
  COMMENT_FRI           VARCHAR2(30 Byte),
  COMMENT_SAT           VARCHAR2(30 Byte),
  COMMENT_SUN           VARCHAR2(30 Byte),

  SVC_COMMENT_MON       VARCHAR2(30 Byte),
  SVC_COMMENT_TUE       VARCHAR2(30 Byte),
  SVC_COMMENT_WED       VARCHAR2(30 Byte),
  SVC_COMMENT_THU       VARCHAR2(30 Byte),
  SVC_COMMENT_FRI       VARCHAR2(30 Byte),
  SVC_COMMENT_SAT       VARCHAR2(30 Byte),
  SVC_COMMENT_SUN       VARCHAR2(30 Byte),
  
  HAND_TRUCK_ALLOWED    VARCHAR2(1 BYTE),
  APT_DLV_ALLOWED       VARCHAR2(1 BYTE),
  WALK_UP_FLOORS        NUMBER(2),
  CREATED_BY            VARCHAR2(16 BYTE),
  MODTIME               DATE
);


ALTER TABLE DLV.DELIVERY_BUILDING_DTL ADD (
  PRIMARY KEY
 (DELIVERY_BUILDING_ID));

ALTER TABLE DLV.DELIVERY_BUILDING_DTL ADD (
  CONSTRAINT ACT_DELIVERY_BUILDING_DTL_FK 
 FOREIGN KEY (DELIVERY_BUILDING_ID) 
 REFERENCES DLV.DELIVERY_BUILDING (ID));

GRANT DELETE, INSERT, SELECT, UPDATE ON  DLV.DELIVERY_BUILDING_DTL TO FDSTORE_PRDA;

GRANT DELETE, INSERT, SELECT, UPDATE ON  DLV.DELIVERY_BUILDING_DTL TO FDSTORE_PRDB;

GRANT SELECT ON  DLV.DELIVERY_BUILDING_DTL TO APPDEV;


GRANT DELETE, INSERT, SELECT, UPDATE ON  DLV.DELIVERY_BUILDING_DTL TO FDTRN_PRDA;




