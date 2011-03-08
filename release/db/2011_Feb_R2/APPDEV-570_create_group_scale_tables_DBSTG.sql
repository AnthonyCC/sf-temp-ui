create snapshot ERPS.GRP_HISTORY refresh force start with sysdate next sysdate+1/144
as SELECT * FROM "GRP_HISTORY"@DBSTO.NYC.FRESHDIRECT.COM "GRP_HISTORY";

create snapshot ERPS.GRP_SCALE_MASTER refresh force start with sysdate next sysdate+1/144
as SELECT * FROM "GRP_SCALE_MASTER"@DBSTO.NYC.FRESHDIRECT.COM "GRP_SCALE_MASTER";

CREATE INDEX ERPS.GRP_SCLE_SAP_ID_IDX  ON ERPS.GRP_SCALE_MASTER(SAP_ID) LOGGING NOPARALLEL tablespace fderpidx;

CREATE UNIQUE INDEX ERPS.GRP_SCLE_SAP_ID_VERSION_IDX  ON ERPS.GRP_SCALE_MASTER(SAP_ID,VERSION) LOGGING NOPARALLEL tablespace fderpidx;

create snapshot ERPS.GRP_PRICING refresh force start with sysdate next sysdate+1/144
as SELECT * FROM "GRP_PRICING"@DBSTO.NYC.FRESHDIRECT.COM "GRP_PRICING";

 CREATE INDEX ERPS.GRP_PRI_GRP_ID_IDX  ON ERPS.GRP_PRICING(GRP_ID) LOGGING NOPARALLEL tablespace fderpidx;
 
 CREATE INDEX ERPS.GRP_PRI_GRP_VERSION_IDX  ON ERPS.GRP_PRICING(VERSION) LOGGING NOPARALLEL tablespace fderpidx;
 
create snapshot ERPS.MATERIAL_GRP refresh force start with sysdate next sysdate+1/144
as SELECT * FROM "MATERIAL_GRP"@DBSTO.NYC.FRESHDIRECT.COM "MATERIAL_GRP";

  CREATE INDEX ERPS.GRP_MAT_MAT_ID_IDX  ON ERPS.MATERIAL_GRP(MAT_ID) LOGGING NOPARALLEL tablespace fderpidx;
 
  CREATE INDEX ERPS.GRP_MAT_GRP_ID_IDX  ON ERPS.MATERIAL_GRP(GRP_ID) LOGGING NOPARALLEL tablespace fderpidx;
 
  CREATE INDEX ERPS.GRP_MAT_GRP_VERSION_IDX  ON ERPS.MATERIAL_GRP(VERSION) LOGGING NOPARALLEL tablespace fderpidx;


CREATE SEQUENCE ERPS.GRPBATCH_SEQ
  START WITH 10000
  MAXVALUE 999999999999999999999999999
  MINVALUE 1
  NOCYCLE
  NOCACHE
  NOORDER;

GRANT DELETE, INSERT, SELECT, UPDATE ON ERPS.GRP_HISTORY TO fdstore_ststg01;

GRANT SELECT ON ERPS.GRP_HISTORY TO APPDEV;

GRANT ALTER, SELECT ON ERPS.GRPBATCH_SEQ TO fdstore_ststg01; 

GRANT SELECT ON ERPS.GRPBATCH_SEQ TO APPDEV; 

GRANT DELETE, INSERT, SELECT, UPDATE ON ERPS.GRP_SCALE_MASTER TO fdstore_ststg01;

GRANT SELECT ON ERPS.GRP_SCALE_MASTER TO APPDEV;
 
GRANT DELETE, INSERT, SELECT, UPDATE ON  ERPS.GRP_PRICING TO fdstore_ststg01;

GRANT SELECT ON  ERPS.GRP_PRICING TO APPDEV;
 
GRANT DELETE, INSERT, SELECT, UPDATE ON  ERPS.MATERIAL_GRP TO fdstore_ststg01;

GRANT SELECT ON  ERPS.MATERIAL_GRP TO APPDEV;


