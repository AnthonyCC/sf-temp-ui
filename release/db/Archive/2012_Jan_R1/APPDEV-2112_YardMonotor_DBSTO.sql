CREATE TABLE TRANSP.PARKING_LOCATION
(
  LOCATION_NAME  VARCHAR2(50 BYTE)              NOT NULL,
  LOCATION_DESC  VARCHAR2(50 BYTE)              NOT NULL
);

ALTER TABLE TRANSP.PARKING_LOCATION ADD ( CONSTRAINT PK_PARKING_LOCATION PRIMARY KEY (LOCATION_NAME));

CREATE TABLE TRANSP.PARKING_SLOT
(
  SLOT_NUMBER         VARCHAR2(30 BYTE)         NOT NULL,
  SLOT_DESC           VARCHAR2(50 BYTE),
  PARKINGLOCATION_ID  VARCHAR2(50 BYTE)         NOT NULL,
  BARCODE_STATUS      VARCHAR2(30 BYTE),
  PAVED_STATUS        VARCHAR2(15 BYTE)
);

ALTER TABLE TRANSP.PARKING_SLOT ADD ( CONSTRAINT PK_PARKINGLOCATIONSLOT PRIMARY KEY (SLOT_NUMBER));

ALTER TABLE TRANSP.PARKING_SLOT ADD ( CONSTRAINT PARKINGLOC_FK FOREIGN KEY (PARKINGLOCATION_ID) REFERENCES TRANSP.PARKING_LOCATION (LOCATION_NAME));

GRANT DELETE, INSERT, SELECT, UPDATE ON TRANSP.PARKING_LOCATION TO fdtrn_stprd01;
GRANT SELECT ON TRANSP.PARKING_LOCATION TO appdev;
GRANT DELETE, INSERT, SELECT, UPDATE ON TRANSP.PARKING_SLOT TO fdtrn_stprd01;
GRANT SELECT ON TRANSP.PARKING_SLOT TO appdev;