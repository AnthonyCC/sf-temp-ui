CREATE TABLE SS_VARIANT_PARAMS(ID VARCHAR2(16) NOT NULL, KEY VARCHAR2(16) NOT NULL,VALUE VARCHAR2(256), 
 CONSTRAINT SS_VARIANT_PARMS_PK PRIMARY KEY (ID,KEY) ENABLE,
 CONSTRAINT SS_VARIANT_PARMS_SS_VARS_FK1 FOREIGN KEY ("ID") REFERENCES "SS_VARIANTS" ("ID") ENABLE);

-- CUSTOMIZE
GRANT SELECT ON SS_VARIANT_PARAMS TO FDSTORE;
GRANT SELECT ON SS_VARIANT_PARAMS TO FDSTORE_PRDA;
GRANT SELECT ON SS_VARIANT_PARAMS TO FDSTORE_PRDB;

