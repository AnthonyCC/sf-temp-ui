ALTER TABLE DLV.SERVICETIME_TYPE add (FIXED_SERVICE_TIME  NUMBER(10,2), VARIABLE_SERVICE_TIME  NUMBER(10,2));
    
CREATE TABLE DLV.SCENARIO_DAYS
(
  ID             VARCHAR2(16 BYTE)              NOT NULL,
  SCENARIO_CODE  VARCHAR2(8 BYTE)               NOT NULL,
  DAY_OF_WEEK    NUMBER(22),
  SCENARIO_DATE  DATE
);

ALTER TABLE DLV.SCENARIO_DAYS ADD ( CONSTRAINT ACT_SCENARIO_FK 
  		FOREIGN KEY (SCENARIO_CODE) REFERENCES DLV.SERVICETIME_SCENARIO (CODE));

CREATE SEQUENCE DEL_SCENDAYS_SEQ
  START WITH 1
  MAXVALUE 1000000000000000000000000000
  MINVALUE 1
  NOCYCLE
  CACHE 200
  NOORDER;

ALTER TABLE DLV.SCENARIO_DAYS ADD (CONSTRAINT SVC_SCENARIO_UNIQUE_DAY_DATE
 				UNIQUE (DAY_OF_WEEK, SCENARIO_DATE));

CREATE TABLE DLV.SCENARIO_ZONES
(
  CODE                    VARCHAR2(8 BYTE)      NOT NULL,
  ZONE_CODE               VARCHAR2(8 BYTE)      NOT NULL,
  SERVICETIME_TYPE        VARCHAR2(8 BYTE),
  SERVICETIME_OVERRIDE    NUMBER(10,2),
  SERVICETIME_OPERATOR    VARCHAR2(1 BYTE),
  SERVICETIME_ADJUSTMENT  NUMBER(10,2)
);

ALTER TABLE DLV.SCENARIO_ZONES ADD (CONSTRAINT ACT_SERVICETIMETYPE_ZONEST_FK FOREIGN KEY (SERVICETIME_TYPE) REFERENCES DLV.SERVICETIME_TYPE (CODE), 
			CONSTRAINT ACT_SERVICETIME_SCENARIO_FK FOREIGN KEY (CODE) REFERENCES DLV.SERVICETIME_SCENARIO (CODE));

ALTER TABLE TRANSP.ZONE add (SERVICETIME_TYPE    VARCHAR2(8 BYTE)  NOT NULL default 'DEF');

ALTER TABLE DLV.DELIVERY_LOCATION add (
        SERVICETIME_OVERRIDE NUMBER(10,2),
        SERVICETIME_OPERATOR VARCHAR2(1 BYTE),
        SERVICETIME_ADJUSTMENT NUMBER(10,2) 
    );

ALTER TABLE DLV. DLV.DELIVERY_BUILDING add (
        SERVICETIME_OVERRIDE NUMBER(10,2),
        SERVICETIME_OPERATOR VARCHAR2(1 BYTE),
        SERVICETIME_ADJUSTMENT NUMBER(10,2) 
    );

ALTER TABLE DLV.DELIVERY_BUILDING ADD ( CONSTRAINT ACT_BUILDING_STTYPE_FK 
		 FOREIGN KEY (SERVICETIME_TYPE) REFERENCES DLV.SERVICETIME_TYPE (CODE));

ALTER TABLE DLV.DELIVERY_LOCATION ADD ( CONSTRAINT ACT_DELIVERY_LOCATIONST_FK
		 FOREIGN KEY (SERVICETIME_TYPE) REFERENCES DLV.SERVICETIME_TYPE (CODE));

GRANT ALL ON DLV.SCENARIO_DAYS TO fdstore_stprd01;
GRANT ALL ON DLV.SCENARIO_DAYS TO fdtrn_stprd01;
GRANT SELECT ON DLV.SCENARIO_DAYS TO appdev;

GRANT ALL ON DLV.SCENARIO_ZONES TO fdstore_stprd01;
GRANT ALL ON DLV.SCENARIO_ZONES TO fdtrn_stprd01;
GRANT SELECT ON DLV.SCENARIO_ZONES TO appdev;

GRANT ALL ON  DEL_SCENDAYS_SEQ TO fdstore_stprd01;
GRANT ALL ON DEL_SCENDAYS_SEQ TO fdtrn_stprd01;
GRANT SELECT ON  DEL_SCENDAYS_SEQ TO appdev;

ALTER TABLE DLV.SERVICETIME DROP CONSTRAINT ACT_SERVICETIME_FK;

ALTER TABLE DLV.SERVICETIME_SCENARIO DROP CONSTRAINT ACT_SCENARIO_STTYPE_FK;

ALTER TABLE DLV.DELIVERY_BUILDING_DTL DROP CONSTRAINT ACT_DELIVERY_BUILDING_DTL_FK;




