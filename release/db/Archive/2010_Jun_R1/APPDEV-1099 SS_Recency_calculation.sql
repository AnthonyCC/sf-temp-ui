SELECT
    CUSTOMER_ID,
    PRODUCT_ID,
    SUM(FREQUENCY) AS FREQUENCY,
    SUM(RECENT_FREQUENCY) AS RECENT_FREQUENCY,
    SUM(QUANTITY) AS QUANTITY,
    SUM(AMOUNT) AS AMOUNT
FROM
(     SELECT
        CUSTOMER_ID,
        PARENT_CONTENT_ID AS PRODUCT_ID,
        COUNT(DISTINCT SAL.ID) AS FREQUENCY,
        NULL AS RECENT_FREQUENCY,
        SUM(OL.QUANTITY) AS QUANTITY,
        SUM(OL.PRICE) AS AMOUNT
    FROM
        SS_ANALYSIS.ORDERLINE OL,
        (select add_months(max(REQUESTED_DATE) over (partition by CUSTOMER_ID),-13) m_r_d, REQUESTED_DATE, customer_id, id
         from ss_analysis.sale) SAL ,
        SS_ANALYSIS.STORE ST
    WHERE
        OL.SALE_ID = SAL.ID AND
        ST.CONTENT_TYPE = 'Sku' AND
        ST.CONTENT_ID = OL.SKU_CODE AND
---        SAL.CUSTOMER_ID='1015031848'  AND
        SAL.REQUESTED_DATE >= SAL.m_r_d
    GROUP BY CUSTOMER_ID, PARENT_CONTENT_ID

    UNION ALL

    SELECT
        CUSTOMER_ID,
        PRODUCT_ID,
        NULL AS FREQUENCY,
        COUNT(SALE_ID) AS RECENT_FREQUENCY,
        NULL AS QUANTITY,
        NULL AS AMOUNT
    FROM (
        SELECT X.CUSTOMER_ID, X.PRODUCT_ID, X.SALE_ID FROM (
            SELECT DISTINCT
            CUSTOMER_ID,
            PARENT_CONTENT_ID AS PRODUCT_ID,
            SAL.ID AS SALE_ID,
            DENSE_RANK() OVER (
                    PARTITION BY CUSTOMER_ID ORDER BY REQUESTED_DATE DESC, SALE_ID DESC)
                AS RANK
        FROM
            SS_ANALYSIS.ORDERLINE OL,
            --SS_ANALYSIS.SALE SAL,
            (select add_months(max(REQUESTED_DATE) over (partition by CUSTOMER_ID),-13) m_r_d, REQUESTED_DATE, customer_id, id
         from ss_analysis.sale) SAL ,
            SS_ANALYSIS.STORE ST
        WHERE
            OL.SALE_ID = SAL.ID AND
            ST.CONTENT_TYPE = 'Sku' AND
            ST.CONTENT_ID = OL.SKU_CODE AND
--            SAL.CUSTOMER_ID='1015031848'  AND
            SAL.REQUESTED_DATE > SAL.m_r_d
        ) X INNER JOIN (
        SELECT CUSTOMER_ID, CEIL(COUNT(ID) / 3) AS LMT FROM
        (select add_months(max(REQUESTED_DATE) over (partition by CUSTOMER_ID),-13) m_r_d, REQUESTED_DATE, customer_id, id
         from ss_analysis.sale)
         where REQUESTED_DATE > m_r_d
        GROUP BY CUSTOMER_ID) Y
        ON Y.CUSTOMER_ID = X.CUSTOMER_ID AND X.RANK <= Y.LMT)
    GROUP BY PRODUCT_ID, CUSTOMER_ID
) GROUP BY CUSTOMER_ID, PRODUCT_ID
order by product_id