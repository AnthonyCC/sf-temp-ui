
/** Run this in DEV, DBSTO**/
CREATE TABLE ERPS.COOL_INFO
(
  ID             VARCHAR2(16 BYTE)              NOT NULL,
  MAT_SAP_ID     VARCHAR2(18 BYTE)              NOT NULL,
  COUNTRY1       VARCHAR2(15 BYTE),
  COUNTRY2       VARCHAR2(15 BYTE),
  COUNTRY3       VARCHAR2(15 BYTE),
  COUNTRY4       VARCHAR2(15 BYTE),
  COUNTRY5       VARCHAR2(15 BYTE),
  DATE_MODIFIED  DATE                           NOT NULL,
  MAT_SAP_DESC   VARCHAR2(40 BYTE),
  PRIMARY KEY
 (ID),
  UNIQUE (MAT_SAP_ID)
);



GRANT DELETE, INSERT, SELECT, UPDATE ON  ERPS.COOL_INFO TO FDSTORE_PRDA;
GRANT DELETE, INSERT, SELECT, UPDATE ON  ERPS.COOL_INFO TO FDSTORE_PRDB;
GRANT SELECT ON ERPS.COOL_INFO TO APPDEV;

/** Stage will have a view from DBSTO . Run this only in DBSTG**/
DROP MATERIALIZED VIEW ERPS.COOL_INFO;

CREATE MATERIALIZED VIEW ERPS.COOL_INFO 
TABLESPACE FDERPDAT
NOCACHE
LOGGING
NOCOMPRESS
NOPARALLEL
BUILD IMMEDIATE
USING INDEX
            TABLESPACE FDERPDAT
REFRESH FAST ON DEMAND
WITH PRIMARY KEY
AS 
SELECT "COOL_INFO"."ID" "ID","COOL_INFO"."MAT_SAP_ID" "MAT_SAP_ID","COOL_INFO"."COUNTRY1" "COUNTRY1","COOL_INFO"."COUNTRY2" "COUNTRY2","COOL_INFO"."COUNTRY3" "COUNTRY3","COOL_INFO"."COUNTRY4" "COUNTRY4","COOL_INFO"."COUNTRY5" "COUNTRY5","COOL_INFO"."DATE_MODIFIED" "DATE_MODIFIED","COOL_INFO"."MAT_SAP_DESC" "MAT_SAP_DESC" FROM "COOL_INFO"@DBSTO.NYC.FRESHDIRECT.COM "COOL_INFO";

CREATE UNIQUE INDEX ERPS.PK_COOL_INFO ON ERPS.COOL_INFO
(ID)
LOGGING
TABLESPACE FDERPDAT
NOPARALLEL;

CREATE UNIQUE INDEX ERPS.COOLINFO_IDX_002 ON ERPS.COOL_INFO
(MAT_SAP_ID)
LOGGING
TABLESPACE FDERPIDX
NOPARALLEL;

GRANT SELECT ON  ERPS.COOL_INFO TO APPDEV;

GRANT SELECT ON  ERPS.COOL_INFO TO DGERRIDGE;

GRANT DELETE, INSERT, SELECT, UPDATE ON  ERPS.COOL_INFO TO FDSTORE_PRDA;