CREATE TABLE ERPS.AVAILABILITY_DELIVERY_DATES(
MATERIAL_SAP_ID VARCHAR2(18),
DATE_AVAILABLE DATE, 
constraint pk_available_rest PRIMARY KEY(MATERIAL_SAP_ID, DATE_RESTRICTED));

GRANT insert,delete,select,update on ERPS.AVAILABILITY_DELIVERY_DATES to FDSTORE_STPRD01;