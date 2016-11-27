

CREATE MATERIALIZED VIEW ERPS.NEW_PRODUCTS_V1 
TABLESPACE FDERPDAT
PCTUSED    0
PCTFREE    10
INITRANS   2
MAXTRANS   255
STORAGE    (
            INITIAL          64K
            MINEXTENTS       1
            MAXEXTENTS       UNLIMITED
            PCTINCREASE      0
            BUFFER_POOL      DEFAULT
           )
NOCACHE
LOGGING
NOCOMPRESS
NOPARALLEL
BUILD IMMEDIATE
REFRESH FORCE ON DEMAND
WITH PRIMARY KEY
AS 

SELECT sku_code,
       sales_org,
       distribution_channel,
       new_product_date
  FROM (SELECT sku_code,
               sales_org,
               distribution_channel,
               event_date new_product_date
          FROM (SELECT sku_code,
                       sales_org,
                       distribution_channel,
                       version,
                       availability,
                       LEAD (
                          availability,
                          1,
                          NULL)
                       OVER (PARTITION BY sku_code
                             ORDER BY version DESC NULLS LAST)
                          previous_availability,
                       LEAD (
                          availability,
                          2,
                          NULL)
                       OVER (PARTITION BY sku_code
                             ORDER BY version DESC NULLS LAST)
                          previous_previous_availability,
                       event_date,
                       LEAD (
                          event_date,
                          1,
                          NULL)
                       OVER (PARTITION BY sku_code
                             ORDER BY version DESC NULLS LAST)
                          previous_event_date,
                       LEAD (
                          event_date,
                          2,
                          NULL)
                       OVER (PARTITION BY sku_code
                             ORDER BY version DESC NULLS LAST)
                          previous_previous_event_date,
                       ROW_NUMBER ()
                       OVER (PARTITION BY sku_code
                             ORDER BY version DESC NULLS LAST)
                          row_no
                  FROM (SELECT sku_code,
                               sales_org,
                               distribution_channel,
                               version,
                               availability,
                               event_date
                          FROM (SELECT sku_code,
                                       sales_org,
                                       distribution_channel,
                                       version,
                                       availability,
                                       event_date,
                                       LEAD (
                                          availability,
                                          1,
                                          NULL)
                                       OVER (
                                          PARTITION BY sku_code
                                          ORDER BY version DESC NULLS LAST)
                                          previous_availability
                                  FROM (SELECT p2.sku_code,
                                               P2.SALES_ORG,
                                               P2.DISTRIBUTION_CHANNEL,
                                               h2.version,
                                               CASE
                                                  WHEN p2.
                                                       unavailability_status
                                                          IS NULL
                                                  THEN
                                                     1
                                                  ELSE
                                                     0
                                               END
                                                  availability,
                                               h2.date_created event_date
                                          FROM erps.material_sales_area p2
                                               INNER JOIN erps.history h2
                                                  ON p2.version = h2.version
                                               INNER JOIN (  SELECT p1.sku_code,
                                                                    p1.
                                                                    sales_org,
                                                                    p1.
                                                                    distribution_channel,
                                                                    MIN (
                                                                       p1.
                                                                       version)
                                                                       first_available
                                                               FROM    erps.
                                                                       material_sales_area p1
                                                                    INNER JOIN
                                                                       erps.
                                                                       history h1
                                                                    ON p1.
                                                                       version =
                                                                          h1.
                                                                          version
                                                              WHERE h1.
                                                                    date_created >
                                                                       SYSDATE
                                                                       - 30
                                                                    AND p1.
                                                                        unavailability_status
                                                                           IS NULL
                                                           GROUP BY p1.
                                                                    sku_code,
                                                                    p1.
                                                                    sales_org,
                                                                    p1.
                                                                    distribution_channel) sq1
                                                  ON (p2.sku_code =
                                                         sq1.sku_code
                                                      AND P2.SALES_ORG =
                                                             sq1.sales_org
                                                      AND sq1.
                                                          DISTRIBUTION_CHANNEL =
                                                             P2.
                                                             DISTRIBUTION_CHANNEL)
                                         WHERE h2.version <=
                                                  sq1.first_available) sq2) sq3
                         WHERE previous_availability IS NULL
                               OR availability <> previous_availability) sq4) sq5
         WHERE row_no = 1 AND event_date > SYSDATE - 30
               AND (   previous_availability IS NULL
                    OR previous_previous_availability IS NULL
                    OR previous_event_date < event_date - 730)) sq6
       INNER JOIN (  SELECT sku_code,
                            sales_org,
                            distribution_channel,
                            MAX (version) latest_version
                       FROM erps.material_Sales_area
                   GROUP BY sku_code, sales_org, distribution_channel) sq7
          ON (    sq6.sku_code = sq7.sku_code
              AND sq6.sales_org = sq7.sales_org
              AND sq6.distribution_channel = sq7.distribution_channel)
       INNER JOIN erps.material_sales_area p3
          ON     p3.sku_code = sq7.sku_code
             AND p3.version = sq7.latest_version
             AND P3.SALES_ORG = sq7.sales_org
             AND P3.DISTRIBUTION_CHANNEL = sq7.distribution_channel;

COMMENT ON MATERIALIZED VIEW ERPS.NEW_PRODUCTS_V1 IS 'snapshot table for snapshot ERPS.NEW_PRODUCTS_V1';

CREATE MATERIALIZED VIEW ERPS.BACK_IN_STOCK_PRODUCTS_V1 
TABLESPACE FDERPDAT
PCTUSED    0
PCTFREE    10
INITRANS   2
MAXTRANS   255
STORAGE    (
            INITIAL          64K
            MINEXTENTS       1
            MAXEXTENTS       UNLIMITED
            PCTINCREASE      0
            BUFFER_POOL      DEFAULT
           )
NOCACHE
LOGGING
NOCOMPRESS
NOPARALLEL
BUILD IMMEDIATE
REFRESH FORCE ON DEMAND
WITH PRIMARY KEY
AS 
/* Formatted on 11/3/2015 1:49:53 PM (QP5 v5.139.911.3011) */
SELECT sku_code,
       sales_org,
       distribution_channel,
       back_in_stock_product_date
  FROM (  SELECT sku_code,
                 sales_org,
                 distribution_channel,
                 event_date back_in_stock_product_date
            FROM (SELECT sku_code,
                         sales_org,
                         distribution_channel,
                         version,
                         availability,
                         LEAD (
                            availability,
                            1,
                            NULL)
                         OVER (PARTITION BY sku_code
                               ORDER BY version DESC NULLS LAST)
                            previous_availability,
                         LEAD (
                            availability,
                            2,
                            NULL)
                         OVER (PARTITION BY sku_code
                               ORDER BY version DESC NULLS LAST)
                            previous_previous_availability,
                         event_date,
                         LEAD (
                            event_date,
                            1,
                            NULL)
                         OVER (PARTITION BY sku_code
                               ORDER BY version DESC NULLS LAST)
                            previous_event_date,
                         LEAD (
                            event_date,
                            2,
                            NULL)
                         OVER (PARTITION BY sku_code
                               ORDER BY version DESC NULLS LAST)
                            previous_previous_event_date,
                         ROW_NUMBER ()
                         OVER (PARTITION BY sku_code
                               ORDER BY version DESC NULLS LAST)
                            row_no
                    FROM (SELECT sku_code,
                                 sales_org,
                                 distribution_channel,
                                 version,
                                 availability,
                                 event_date
                            FROM (SELECT sku_code,
                                         sales_org,
                                         distribution_channel,
                                         version,
                                         availability,
                                         event_date,
                                         LEAD (
                                            availability,
                                            1,
                                            NULL)
                                         OVER (
                                            PARTITION BY sku_code
                                            ORDER BY version DESC NULLS LAST)
                                            previous_availability
                                    FROM (SELECT p2.sku_code,
                                                 P2.SALES_ORG,
                                                 P2.DISTRIBUTION_CHANNEL,
                                                 h2.version,
                                                 CASE
                                                    WHEN p2.
                                                         unavailability_status
                                                            IS NULL
                                                    THEN
                                                       1
                                                    ELSE
                                                       0
                                                 END
                                                    availability,
                                                 h2.date_created event_date
                                            FROM erps.material_sales_area p2
                                                 INNER JOIN erps.history h2
                                                    ON p2.version = h2.version
                                                 INNER JOIN (  SELECT p1.sku_code,
                                                                      P1.
                                                                      SALES_ORG,
                                                                      P1.
                                                                      DISTRIBUTION_CHANNEL,
                                                                      MIN (
                                                                         p1.
                                                                         version)
                                                                         first_available
                                                                 FROM    erps.
                                                                         material_sales_area p1
                                                                      INNER JOIN
                                                                         erps.
                                                                         history h1
                                                                      ON p1.
                                                                         version =
                                                                            h1.
                                                                            version
                                                                WHERE h1.
                                                                      date_created >
                                                                         SYSDATE
                                                                         - 30
                                                                      AND p1.
                                                                          unavailability_status
                                                                             IS NULL
                                                             GROUP BY p1.
                                                                      sku_code,
                                                                      P1.
                                                                      SALES_ORG,
                                                                      P1.
                                                                      DISTRIBUTION_CHANNEL) sq1
                                                    ON (p2.sku_code =
                                                           sq1.sku_code
                                                        AND P2.SALES_ORG =
                                                               sq1.sales_org
                                                        AND sq1.
                                                            DISTRIBUTION_CHANNEL =
                                                               P2.
                                                               DISTRIBUTION_CHANNEL)
                                           WHERE h2.version <=
                                                    sq1.first_available) sq2) sq3
                           WHERE previous_availability IS NULL
                                 OR availability <> previous_availability) sq4) sq5
           WHERE     row_no = 1
                 AND event_date > SYSDATE - 30
                 AND previous_availability IS NOT NULL
                 AND previous_previous_availability IS NOT NULL
                 AND previous_event_date < event_date - 273.75
                 AND previous_event_date >= event_date - 730
        ORDER BY sku_code) sq6
       INNER JOIN (  SELECT sku_code,
                            sales_org,
                            distribution_channel,
                            MAX (version) latest_version
                       FROM erps.material_Sales_area
                   GROUP BY sku_code, sales_org, distribution_channel) sq7
          ON (    sq6.sku_code = sq7.sku_code
              AND sq6.sales_org = sq7.sales_org
              AND sq6.distribution_channel = sq7.distribution_channel)
       INNER JOIN erps.material_sales_area p3
          ON p3.sku_code = sq7.sku_code AND p3.version = sq7.latest_version
 WHERE p3.unavailability_status IS NULL;

COMMENT ON MATERIALIZED VIEW ERPS.BACK_IN_STOCK_PRODUCTS_V1 IS 'snapshot table for snapshot ERPS.BACK_IN_STOCK_PRODUCTS_V1';

-- Procedure
CREATE OR REPLACE procedure ERPS.refresh_new_and_back
is
begin
    DBMS_MVIEW.REFRESH('erps.new_products_v1');
    DBMS_MVIEW.REFRESH('erps.back_in_stock_products_v1');
end;
/

--Indexes

CREATE  INDEX ERPS.BACK_IN_STOCK_V1_INDEX1 ON ERPS.BACK_IN_STOCK_PRODUCTS_V1
(SKU_CODE)
LOGGING
TABLESPACE FDERPDAT
PCTFREE    10
INITRANS   2
MAXTRANS   255
STORAGE    (
            INITIAL          64K
            MINEXTENTS       1
            MAXEXTENTS       UNLIMITED
            PCTINCREASE      0
            BUFFER_POOL      DEFAULT
           )
NOPARALLEL;

CREATE  INDEX ERPS.NEW_PRODUCTS_V1_INDEX1 ON ERPS.NEW_PRODUCTS_V1
(SKU_CODE)
LOGGING
TABLESPACE FDERPDAT
PCTFREE    10
INITRANS   2
MAXTRANS   255
STORAGE    (
            INITIAL          64K
            MINEXTENTS       1
            MAXEXTENTS       UNLIMITED
            PCTINCREASE      0
            BUFFER_POOL      DEFAULT
           )
NOPARALLEL;

--Grants for DBSTG ONLY
GRANT SELECT ON ERPS.NEW_PRODUCTS_V1 TO APPDEV;
GRANT SELECT ON ERPS.NEW_PRODUCTS_V1 TO FDSTORE_PRDA;
GRANT SELECT ON ERPS.NEW_PRODUCTS_V1 TO FDSTORE_PRDB;
GRANT SELECT ON ERPS.NEW_PRODUCTS_V1 TO FDSTORE_STSTG01;

GRANT SELECT ON ERPS.BACK_IN_STOCK_PRODUCTS_V1 TO APPDEV;
GRANT SELECT ON ERPS.BACK_IN_STOCK_PRODUCTS_V1 TO FDSTORE_PRDA;
GRANT SELECT ON ERPS.BACK_IN_STOCK_PRODUCTS_V1 TO FDSTORE_PRDB;
GRANT SELECT ON ERPS.BACK_IN_STOCK_PRODUCTS_V1 TO FDSTORE_STSTG01;

--Grants for DBSTO ONLY
GRANT SELECT ON ERPS.NEW_PRODUCTS_V1 TO APPDEV;
GRANT SELECT ON ERPS.NEW_PRODUCTS_V1 TO FDSTORE_PRDA;
GRANT SELECT ON ERPS.NEW_PRODUCTS_V1 TO FDSTORE_PRDB;
GRANT SELECT ON ERPS.NEW_PRODUCTS_V1 TO FDSTORE_STPRD01;
GRANT SELECT ON ERPS.NEW_PRODUCTS_V1 TO RO_CUST_ERPS;

GRANT SELECT ON ERPS.BACK_IN_STOCK_PRODUCTS_V1 TO APPDEV;
GRANT SELECT ON ERPS.BACK_IN_STOCK_PRODUCTS_V1 TO FDSTORE_PRDA;
GRANT SELECT ON ERPS.BACK_IN_STOCK_PRODUCTS_V1 TO FDSTORE_PRDB;
GRANT SELECT ON ERPS.BACK_IN_STOCK_PRODUCTS_V1 TO FDSTORE_STPRD01;
GRANT SELECT ON ERPS.BACK_IN_STOCK_PRODUCTS_V1 TO RO_CUST_ERPS;