CREATE TABLE LOG_CUSTOMER_VARIANTS(
  "TIMESTAMP" DATE NOT NULL,
  "CUSTOMER_ID" VARCHAR2(16 BYTE) NOT NULL,
  "SALE_ID" VARCHAR2(16 BYTE) NOT NULL,
  "VARIANT_ID" VARCHAR2(16) NOT NULL,
  "FEATURE" VARCHAR2(16) NOT NULL
) TABLESPACE "FDCUSDAT";

CREATE INDEX "IDX_LOG_CUSTOMER_VARIANTS_1" ON LOG_CUSTOMER_VARIANTS ("TIMESTAMP") TABLESPACE "FDCUSDAT";

GRANT SELECT, INSERT, DELETE, UPDATE ON LOG_CUSTOMER_VARIANTS TO FDSTORE;
