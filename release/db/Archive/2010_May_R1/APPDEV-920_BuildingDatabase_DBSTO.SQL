CREATE TABLE DLV.DELIVERY_BUILDING_DETAIL
(
  DELIVERY_BUILDING_ID  VARCHAR2(16 BYTE)       NOT NULL,
  ADDR_TYPE             VARCHAR2(16 BYTE),
  COMPANY_NAME          VARCHAR2(255 BYTE),
  DIFFICULT_TO_DELIVER  VARCHAR2(1 BYTE),
  DIFFICULT_REASON      VARCHAR2(255 BYTE),
  SVC_TIME_OVERRIDE     VARCHAR2(255 BYTE),
  ADDITIONAL            VARCHAR2(255 BYTE),
  IS_DOORMAN            VARCHAR2(1 BYTE),
  IS_WALKUP             VARCHAR2(1 BYTE),
  IS_ELEVATOR           VARCHAR2(1 BYTE),
  IS_HOUSE              VARCHAR2(1 BYTE),
  IS_FREIGHT_ELEVATOR   VARCHAR2(1 BYTE),
  SVC_ENT               VARCHAR2(1 BYTE),
  SVC_SCRUBBED_STREET   VARCHAR2(255 BYTE),
  SVC_CITY              VARCHAR2(16 BYTE),
  SVC_STATE             VARCHAR2(16 BYTE),
  SVC_ZIP               VARCHAR2(10 BYTE),
  HAND_TRUCK_ALLOWED    VARCHAR2(1 BYTE),
  WALK_UP_FLOORS        NUMBER(2),
  CREATED_BY            VARCHAR2(16 BYTE),
  MODTIME               DATE,
  SVC_CROSS_STREET      VARCHAR2(255 BYTE),
  CROSS_STREET          VARCHAR2(255 BYTE),
  OTHER                 VARCHAR2(255 BYTE)
);

ALTER TABLE DLV.DELIVERY_BUILDING_DETAIL ADD (
  PRIMARY KEY
 (DELIVERY_BUILDING_ID)
    USING INDEX 
    TABLESPACE FDDLVDAT
    PCTFREE    10
    INITRANS   2
    MAXTRANS   255
    STORAGE    (
                INITIAL          64K
                MINEXTENTS       1
                MAXEXTENTS       UNLIMITED
                PCTINCREASE      0
               ));

ALTER TABLE DLV.DELIVERY_BUILDING_DETAIL ADD (
  CONSTRAINT ACT_DLV_BUILDING_DETAIL_FK 
 FOREIGN KEY (DELIVERY_BUILDING_ID) 
 REFERENCES DLV.DELIVERY_BUILDING (ID));

grant insert,select,delete,update on DLV.DELIVERY_BUILDING_DETAIL to fdstore_stprd01;
grant insert,select,delete,update on DLV.DELIVERY_BUILDING_DETAIL to fdtrn_stprd01;
grant select on DLV.DELIVERY_BUILDING_DETAIL to appdev;



CREATE TABLE DLV.DELIVERY_BUILDING_DETAIL_OPS
(
  BLDG_START_HOUR       DATE,
  BLDG_END_HOUR         DATE,
  BLDG_COMMENTS         VARCHAR2(30 BYTE),
  SERVICE_START_HOUR    DATE,
  SERVICE_END_HOUR      DATE,
  SERVICE_COMMENTS      VARCHAR2(30 BYTE),
  DAY_OF_WEEK           VARCHAR2(1 BYTE),
  DELIVERY_BUILDING_ID  VARCHAR2(16 BYTE)       NOT NULL
);

CREATE UNIQUE INDEX DLV.DELIVERY_BUILDING_DETAIL_OPSPK ON DLV.DELIVERY_BUILDING_DETAIL_OPS
(DELIVERY_BUILDING_ID, DAY_OF_WEEK)
LOGGING
TABLESPACE FDDLVDAT
PCTFREE    10
INITRANS   2
MAXTRANS   255
STORAGE    (
            INITIAL          64K
            MINEXTENTS       1
            MAXEXTENTS       UNLIMITED
            PCTINCREASE      0
            BUFFER_POOL      DEFAULT
           )
NOPARALLEL;


ALTER TABLE DLV.DELIVERY_BUILDING_DETAIL_OPS ADD (
  CONSTRAINT DELIVERY_BUILDING_DETAIL_OPSPK
 PRIMARY KEY
 (DELIVERY_BUILDING_ID, DAY_OF_WEEK)
    USING INDEX 
    TABLESPACE FDDLVDAT
    PCTFREE    10
    INITRANS   2
    MAXTRANS   255
    STORAGE    (
                INITIAL          64K
                MINEXTENTS       1
                MAXEXTENTS       UNLIMITED
                PCTINCREASE      0
               ));

ALTER TABLE DLV.DELIVERY_BUILDING_DETAIL_OPS ADD (
  CONSTRAINT ACT_DLV_BLDG_DETAIL_OPS_FK 
 FOREIGN KEY (DELIVERY_BUILDING_ID) 
 REFERENCES DLV.DELIVERY_BUILDING (ID));

grant insert,select,delete,update on DLV.DELIVERY_BUILDING_DETAIL_OPS to fdstore_stprd01;
grant insert,select,delete,update on DLV.DELIVERY_BUILDING_DETAIL_OPS to fdtrn_stprd01;
grant select on DLV.DELIVERY_BUILDING_DETAIL_OPS to appdev;