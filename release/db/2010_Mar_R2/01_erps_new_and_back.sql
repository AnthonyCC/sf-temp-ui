-- ROLLOUT

-- create new products materialized view

CREATE materialized VIEW erps.new_products
AS
SELECT sku_code,
  new_product_date
FROM
  (SELECT sku_code,
    event_date new_product_date
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
          (SELECT p2.sku_code,
            h2.version,
            CASE
              WHEN p2.unavailability_status IS NULL
              THEN 1
              ELSE 0
            END availability,
            h2.date_created event_date
          FROM erps.product p2
          INNER JOIN erps.history h2
          ON p2.version = h2.version
          INNER JOIN
            (SELECT p1.sku_code,
              MIN(p1.version) first_available
            FROM erps.product p1
            INNER JOIN erps.history h1
            ON p1.version                 = h1.version
            WHERE h1.date_created         > sysdate - 120
            AND p1.unavailability_status IS NULL
            GROUP BY p1.sku_code
            ) sq1 ON p2.sku_code = sq1.sku_code
          WHERE h2.version      <= sq1.first_available
          ) sq2
        ) sq3
      WHERE previous_availability IS NULL
      OR availability             <> previous_availability
      ) sq4
    ) sq5
  WHERE row_no                       = 1
  AND event_date                     > sysdate - 120
  AND (previous_availability        IS NULL
  OR previous_previous_availability IS NULL
  OR previous_event_date             < event_date - 730)
  ) sq6
INNER JOIN
  (SELECT sku_code,
    MAX(version) latest_version
  FROM erps.product
  GROUP BY sku_code
  ) sq7
ON sq6.sku_code = sq7.sku_code
INNER JOIN erps.product p3
ON p3.sku_code                  = sq7.sku_code
AND p3.version                  = sq7.latest_version
WHERE p3.unavailability_status IS NULL;

-- add index for new products materialized view;

CREATE UNIQUE INDEX new_products_index1 ON erps.new_products (sku_code);

-- force refresh (you don't have to call it on rollout)
-- execute DBMS_MVIEW.REFRESH('erps.new_products');

--DEV
GRANT select on erps.new_products TO fdstore_prda;
GRANT select on erps.new_products TO fdstore_prdb;

--STAGE
GRANT select on erps.new_products TO fdstore_prda;
GRANT select on erps.new_products TO fdstore_prdb;
GRANT select on erps.new_products TO fdstore_ststg01;

--PROD
GRANT select on erps.new_products TO fdstore_prda;
GRANT select on erps.new_products TO fdstore_prdb;
GRANT select on erps.new_products TO fdstore_stprd01;

--CMS
GRANT execute on erps.refresh_new_and_back TO fdstore;
GRANT execute on erps.refresh_new_and_back TO appdev;

-- create back-in-stock products materialized view

CREATE materialized VIEW erps.back_in_stock_products
AS
SELECT sku_code,
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
          (SELECT p2.sku_code,
            h2.version,
            CASE
              WHEN p2.unavailability_status IS NULL
              THEN 1
              ELSE 0
            END availability,
            h2.date_created event_date
          FROM erps.product p2
          INNER JOIN erps.history h2
          ON p2.version = h2.version
          INNER JOIN
            (SELECT p1.sku_code,
              MIN(p1.version) first_available
            FROM erps.product p1
            INNER JOIN erps.history h1
            ON p1.version                 = h1.version
            WHERE h1.date_created         > sysdate - 30
            AND p1.unavailability_status IS NULL
            GROUP BY p1.sku_code
            ) sq1 ON p2.sku_code = sq1.sku_code
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
  (SELECT sku_code,
    MAX(version) latest_version
  FROM erps.product
  GROUP BY sku_code
  ) sq7
ON sq6.sku_code = sq7.sku_code
INNER JOIN erps.product p3
ON p3.sku_code                  = sq7.sku_code
AND p3.version                  = sq7.latest_version
WHERE p3.unavailability_status IS NULL;

-- add index for back-in-stock products materialized view;

CREATE UNIQUE INDEX back_in_stock_index1 ON erps.back_in_stock_products(sku_code);

-- force refresh (you don't have to call it on rollout)
-- execute DBMS_MVIEW.REFRESH('erps.back_in_stock_products');

--DEV
GRANT select on erps.back_in_stock_products TO fdstore_prda;
GRANT select on erps.back_in_stock_products TO fdstore_prdb;

--STAGE
GRANT select on erps.back_in_stock_products TO fdstore_prda;
GRANT select on erps.back_in_stock_products TO fdstore_prdb;
GRANT select on erps.back_in_stock_products TO fdstore_ststg01;

--PROD
GRANT select on erps.back_in_stock_products TO fdstore_prda;
GRANT select on erps.back_in_stock_products TO fdstore_prdb;
GRANT select on erps.back_in_stock_products TO fdstore_stprd01;

--CMS
GRANT execute on erps.refresh_new_and_back TO fdstore;
GRANT execute on erps.refresh_new_and_back TO appdev;

-- create refresh store procedure

create or replace procedure erps.refresh_new_and_back
is
begin
    DBMS_MVIEW.REFRESH('erps.new_products');
    DBMS_MVIEW.REFRESH('erps.back_in_stock_products');
end;

/

--DEV
GRANT execute on erps.refresh_new_and_back TO fdstore_prda;
GRANT execute on erps.refresh_new_and_back TO fdstore_prdb;

--STAGE
GRANT execute on erps.refresh_new_and_back TO fdstore_prda;
GRANT execute on erps.refresh_new_and_back TO fdstore_prdb;
GRANT execute on erps.refresh_new_and_back TO fdstore_ststg01;

--PROD
GRANT execute on erps.refresh_new_and_back TO fdstore_prda;
GRANT execute on erps.refresh_new_and_back TO fdstore_prdb;
GRANT execute on erps.refresh_new_and_back TO fdstore_stprd01;

--CMS
GRANT execute on erps.refresh_new_and_back TO fdstore;
GRANT execute on erps.refresh_new_and_back TO appdev;

-- ROLLBACK

--DROP procedure erps.refresh_new_and_back;
--DROP materialized view erps.back_in_stock_products;
-- DROP materialized view erps.new_products;