CREATE TABLE RULES
(
  ID          VARCHAR2(16)                 NOT NULL,
  NAME        VARCHAR2(128)                NOT NULL,
  START_DATE  DATE                              NOT NULL,
  END_DATE    DATE                              NOT NULL,
  PRIORITY    NUMBER(5)                         NOT NULL,
  CONDITIONS  VARCHAR2(512)                NOT NULL,
  OUTCOME     VARCHAR2(256),
  SUBSYSTEM   VARCHAR2(16)                 NOT NULL
)

grant all on rules to FDSTORE_PRDA