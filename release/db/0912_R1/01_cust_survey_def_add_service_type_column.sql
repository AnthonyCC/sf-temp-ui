ALTER TABLE CUST.SURVEY_DEF ADD ("SERVICE_TYPE" VARCHAR2(20) DEFAULT 'HOME' NOT NULL);


ALTER TABLE CUST.SURVEY_DEF DROP CONSTRAINT "SYS_C00123906";

ALTER TABLE CUST.SURVEY_DEF ADD CONSTRAINT SYS_C00123906 UNIQUE ("NAME","SERVICE_TYPE") ENABLE;


ALTER TABLE CUST.SURVEY ADD ("SERVICE_TYPE" VARCHAR2(20) DEFAULT 'HOME' NOT NULL);

UPDATE CUST.SURVEY_DEF SET SERVICE_TYPE = 'HOME';
