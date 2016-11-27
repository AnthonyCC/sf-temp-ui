DROP TABLE TRANSP.HANDOFF_BATCHDEPOTSCHEDULE_EX CASCADE CONSTRAINTS;

CREATE TABLE TRANSP.HANDOFF_BATCHDEPOTSCHEDULE_EX
(  
  DAY_OF_WEEK          VARCHAR2(5 BYTE)             NOT NULL,
  CUTOFF_DATETIME     DATE             NOT NULL,
  AREA     VARCHAR2(10 BYTE)             NOT NULL,
  DEPOTARRIVALTIME     DATE             NOT NULL,
  TRUCKDEPARTURETIME     DATE             NOT NULL,
  ORIGIN_ID VARCHAR2(40) NOT NULL
);

ALTER TABLE TRANSP.HANDOFF_BATCHDEPOTSCHEDULE_EX ADD (CONSTRAINT PK_HANDOFF_BATDPTSCHEDULETMP  PRIMARY KEY (DAY_OF_WEEK, CUTOFF_DATETIME, AREA, DEPOTARRIVALTIME, TRUCKDEPARTURETIME, ORIGIN_ID));

CREATE INDEX TRANSP.IDX_HF_BATCHDPTSCHTMP_PKID ON TRANSP.HANDOFF_BATCHDEPOTSCHEDULE_EX(DAY_OF_WEEK, CUTOFF_DATETIME);


