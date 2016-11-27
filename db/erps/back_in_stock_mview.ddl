CREATE MATERIALIZED VIEW "ERPS"."BACK_IN_STOCK_PRODUCTS"
TABLESPACE "FDERPDAT"
AS SELECT sku_code,
  back_in_stock_product_date
FROM
  (SELECT sku_code,
    event_date back_in_stock_product_date
  FROM
    (SELECT sku_code,
      version,
      availability,
      lead(availability, 1, NULL) over (partition BY sku_code order by version DESC nulls last) previous_availability,
      lead(availability, 2, NULL) over (partition BY sku_code order by version DESC nulls last) previous_previous_availability,
      event_date,
      lead(event_date, 1, NULL) over (partition BY sku_code order by version DESC nulls last) previous_event_date,
      lead(event_date, 2, NULL) over (partition BY sku_code order by version DESC nulls last) previous_previous_event_date,
      ROW_NUMBER() OVER (PARTITION BY sku_code ORDER BY version DESC NULLS LAST) row_no
    FROM
      (SELECT sku_code,
        version,
        availability,
        event_date
      FROM
        (SELECT sku_code,
          version,
          availability,
          event_date,
          lead(availability, 1, NULL) over (partition BY sku_code order by version DESC nulls last) previous_availability
        FROM
          (SELECT m2.skucode as sku_code,
            h2.version,
            CASE
              WHEN ms.unavailability_status IS NULL
              THEN 1
              ELSE 0
            END availability,
            h2.date_created event_date
          FROM erps.material m2
          INNER JOIN erps.history h2
          ON m2.version = h2.version
          JOIN erps.material_sales_area ms
          on ms.mat_id = m2.id
          INNER JOIN
            (SELECT m1.skucode as sku_code,
              MIN(m1.version) first_available
            FROM erps.material m1
            INNER JOIN erps.history h1
            ON m1.version                 = h1.version
            JOIN erps.material_sales_area ms
            ON m1.id = ms.mat_id
            WHERE h1.date_created         > sysdate - 30
            AND ms.unavailability_status IS NULL
            GROUP BY m1.skucode
            ) sq1 ON m2.skucode = sq1.sku_code
          WHERE h2.version      <= sq1.first_available
          ) sq2
        ) sq3
      WHERE previous_availability IS NULL
      OR availability             <> previous_availability
      ) sq4
    ) sq5
  WHERE row_no                        = 1
  AND event_date                      > sysdate - 30
  AND previous_availability          IS NOT NULL
  AND previous_previous_availability IS NOT NULL
  AND previous_event_date             < event_date - 273.75
  AND previous_event_date            >= event_date - 730
  ORDER BY sku_code
  ) sq6
INNER JOIN
  (SELECT skucode as sku_code,
    MAX(version) latest_version
  FROM erps.material
  GROUP BY skucode
  ) sq7
ON sq6.sku_code = sq7.sku_code
JOIN erps.material_sales_area ms
ON sq7.sku_code = ms.SKU_CODE
and sq7.latest_version = ms.VERSION
WHERE ms.unavailability_status IS NULL;


CREATE UNIQUE INDEX "ERPS"."BACK_IN_STOCK_INDEX1" ON "ERPS"."BACK_IN_STOCK_PRODUCTS" ("SKU_CODE") 
TABLESPACE "FDERPDAT" ;


COMMENT ON MATERIALIZED VIEW "ERPS"."BACK_IN_STOCK_PRODUCTS"  IS 'snapshot table for snapshot ERPS.BACK_IN_STOCK_PRODUCTS';
