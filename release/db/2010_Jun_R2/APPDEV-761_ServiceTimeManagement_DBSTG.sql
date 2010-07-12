--Adding FIXED_SERVICE_TIME, VARIABLE_SERVICE_TIME of servicetime to dlv.servicetime_type
ALTER TABLE 
    DLV.SERVICETIME_TYPE
add
   (
        FIXED_SERVICE_TIME     NUMBER(10,2),
        VARIABLE_SERVICE_TIME  NUMBER(10,2)
   );
    
--SERVICETIME_SCENARIO
ALTER TABLE DLV.SERVICETIME_SCENARIO
    drop CONSTRAINT ACT_SCENARIO_STTYPE_FK;

ALTER TABLE DLV.SERVICETIME_SCENARIO
    drop (DEFAULT_SERVICETIME_TYPE, DEFAULT_ZONE_TYPE);

--New Table (SCENARIO_DAYS)
CREATE TABLE DLV.SCENARIO_DAYS
(
  ID             VARCHAR2(16 BYTE)              NOT NULL,
  SCENARIO_CODE  VARCHAR2(8 BYTE)               NOT NULL,
  DAY_OF_WEEK    NUMBER(22),
  SCENARIO_DATE  DATE
)

ALTER TABLE DLV.SCENARIO_DAYS ADD (
  CONSTRAINT ACT_SCENARIO_FK 
  FOREIGN KEY (SCENARIO_CODE) REFERENCES DLV.SERVICETIME_SCENARIO (CODE));

GRANT ALL ON DLV.SCENARIO_DAYS TO fdstore_ststg01;
GRANT ALL ON DLV.SCENARIO_DAYS TO fdtrn_ststg01;
GRANT SELECT ON DLV.SCENARIO_DAYS TO appdev;

CREATE SEQUENCE DLV.DELIVERY_SCENARIO_SEQ
  START WITH 7000
  MAXVALUE 1000000000000000000000000000
  MINVALUE 1
  NOCYCLE
  CACHE 200
  NOORDER;

GRANT ALL ON  DLV.DELIVERY_SCENARIO_SEQ TO fdstore_ststg01;
GRANT ALL ON DLV.DELIVERY_SCENARIO_SEQ TO fdtrn_ststg01;
GRANT SELECT ON  DLV.DELIVERY_SCENARIO_SEQ TO appdev;

ALTER TABLE DLV.SCENARIO_DAYS ADD (
  CONSTRAINT SVC_SCENARIO_UNIQUE_DAY_DATE
 UNIQUE (DAY_OF_WEEK, SCENARIO_DATE));

GRANT ALL ON SVC_SCENARIO_UNIQUE_DAY_DATE TO fdstore_ststg01;
GRANT ALL ON SVC_SCENARIO_UNIQUE_DAY_DATE TO fdtrn_ststg01;
GRANT SELECT ON SVC_SCENARIO_UNIQUE_DAY_DATE TO appdev;


CREATE TABLE DLV.SCENARIO_ZONES
(
  CODE                    VARCHAR2(8 BYTE)      NOT NULL,
  ZONE_CODE               VARCHAR2(8 BYTE),
  SERVICETIME_TYPE        VARCHAR2(8 BYTE),
  SERVICETIME_OVERRIDE    NUMBER(10,2),
  SERVICETIME_OPERATOR    VARCHAR2(1 BYTE),
  SERVICETIME_ADJUSTMENT  NUMBER(10,2)
);

ALTER TABLE DLV.SCENARIO_ZONES ADD (
  CONSTRAINT ACT_SERVICETIMETYPE_ZONEST_FK 
 FOREIGN KEY (CODE) 
 REFERENCES DLV.SERVICETIME_SCENARIO (CODE));

GRANT ALL ON DLV.SCENARIO_ZONES TO fdstore_ststg01;
GRANT ALL ON DLV.SCENARIO_ZONES TO fdtrn_ststg01;
GRANT SELECT ON DLV.SCENARIO_ZONES TO appdev;


--Alter Zone table

ALTER TABLE 
    TRANSP.ZONE
add   (
            SERVICETIME_TYPE    VARCHAR2(8 BYTE)  NOT NULL
       );

--Alter DELIVERY_BUILDING table
ALTER TABLE DLV.DELIVERY_BUILDING
add (
        SERVICETIME_OVERRIDE NUMBER(10,2),
        SERVICETIME_OPERATOR VARCHAR2(1 BYTE),
        SERVICETIME_ADJUSTMENT NUMBER(10,2) 
      );

Alter table DLV.DELIVERY_BUILDING_DETAIL drop
column  SVC_TIME_OVERRIDE;

Alter table DLV.DELIVERY_BUILDING_DETAIL 
add(
     SVC_TIME_TYPE  VARCHAR2(8 BYTE),
     SVC_TIME_OVERRIDE    NUMBER(10,2),
     SVC_TIME_OPERATOR    VARCHAR2(1 BYTE),
     SVC_TIME_ADJUSTMENT  NUMBER(10,2)     
    );

--Alter DELIVERY_LOCATION table
ALTER TABLE DLV.DELIVERY_LOCATION
add (
        SERVICETIME_OVERRIDE NUMBER(10,2),
        SERVICETIME_OPERATOR VARCHAR2(1 BYTE),
        SERVICETIME_ADJUSTMENT NUMBER(10,2) 
    );


