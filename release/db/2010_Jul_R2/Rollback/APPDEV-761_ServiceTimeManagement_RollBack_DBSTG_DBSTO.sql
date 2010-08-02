--AS DLV schema owner

ALTER TABLE DLV.SERVICETIME ADD (
  CONSTRAINT PK_SERVICETIME PRIMARY KEY (SERVICETIME_TYPE, ZONE_TYPE);

ALTER TABLE DLV.SERVICETIME ADD (
  CONSTRAINT ACT_SERVICETIME_FK FOREIGN KEY (SERVICETIME_TYPE) REFERENCES DLV.SERVICETIME_TYPE (CODE));

ALTER TABLE DLV.SERVICETIME_TYPE drop(FIXED_SERVICE_TIME, VARIABLE_SERVICE_TIME);
   
ALTER TABLE DLV.SERVICETIME_SCENARIO ADD (
  CONSTRAINT ACT_SCENARIO_STTYPE_FK FOREIGN KEY (DEFAULT_SERVICETIME_TYPE) REFERENCES DLV.SERVICETIME_TYPE (CODE));

DROP TABLE DLV.SCENARIO_DAYS CASCADE CONSTRAINTS;

DROP SEQUENCE DLV.DELIVERY_SCENARIO_SEQ;

DROP TABLE DLV.SCENARIO_ZONES CASCADE CONSTRAINTS;

ALTER TABLE DLV.DELIVERY_BUILDING
		drop (SERVICETIME_OVERRIDE,SERVICETIME_OPERATOR,SERVICETIME_ADJUSTMENT);

ALTER TABLE DLV.DELIVERY_BUILDING_DTL ADD (
 	 CONSTRAINT ACT_DELIVERY_BUILDING_DTL_FK FOREIGN KEY (DELIVERY_BUILDING_ID) REFERENCES DLV.DELIVERY_BUILDING (ID));

ALTER TABLE DLV.DELIVERY_LOCATION
		drop (SERVICETIME_OVERRIDE,SERVICETIME_OPERATOR,SERVICETIME_ADJUSTMENT);

ALTER TABLE DLV.DELIVERY_LOCATION drop CONSTRAINT ACT_DELIVERY_LOCATIONST_FK;


--AS TRANSP schema owner
ALTER TABLE TRANSP.ZONE drop column SERVICETIME_TYPE;