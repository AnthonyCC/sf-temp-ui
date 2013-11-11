CREATE TABLE DLV.RULES
(
  ID          VARCHAR2(16 BYTE)                 NOT NULL,
  NAME        VARCHAR2(128 BYTE)                NOT NULL,
  START_DATE  DATE                              NOT NULL,
  END_DATE    DATE                              NOT NULL,
  PRIORITY    NUMBER(5)                         NOT NULL,
  CONDITIONS  VARCHAR2(1024 BYTE)               NOT NULL,
  OUTCOME     VARCHAR2(256 BYTE)				NOT NULL,
  SUBSYSTEM   VARCHAR2(16 BYTE)                 NOT NULL
);