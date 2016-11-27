--
-- SmartStore Analysis
-- Migration Scripts
--


-- Product Information
INSERT INTO CUST.SSA_SKU (ID)
SELECT
  SUBSTR(ID,INSTR(ID,':')+1) AS ID
FROM CMS.CONTENTNODE
WHERE
  CONTENTTYPE_ID = 'Sku';


-- Customer Information
INSERT INTO CUST.SSA_CUSTOMER (ID)
SELECT ID AS CUSTOMER_ID FROM CUST.CUSTOMER;


-- Sale
INSERT INTO CUST.SSA_SALE (ID,CUSTOMER_ID,REQUESTED_DATE)
SELECT S.ID, S.CUSTOMER_ID, SA.REQUESTED_DATE
FROM CUST.SALE S, CUST.SALESACTION SA
WHERE
  S.STATUS NOT IN ('CAN','NEW','SUB')
AND
  SA.SALE_ID = S.ID
AND
  S.CROMOD_DATE = SA.ACTION_DATE
AND
  SA.ACTION_TYPE IN ('CRO','MOD');


-- Orderlines
INSERT INTO CUST.SSA_ORDERLINE (ID, SALE_ID, SKU_CODE)
SELECT OL.ID, S.ID AS SALE_ID, OL.SKU_CODE
FROM CUST.SALE S, CUST.SALESACTION SA, CUST.ORDERLINE OL
WHERE
  S.STATUS NOT IN ('CAN','NEW','SUB')
AND
  SA.SALE_ID = S.ID
AND
  S.CROMOD_DATE = SA.ACTION_DATE
AND
  SA.ACTION_TYPE IN ('CRO','MOD')
AND
  OL.SALESACTION_ID = SA.ID;


-- Store Content (CMS)
insert into cust.ssa_store(PARENT_CONTENT_TYPE, PARENT_CONTENT_ID, CONTENT_TYPE, CONTENT_ID)
select
  SUBSTR(PARENT_ID,1,INSTR(PARENT_ID,':')-1) AS PARENT_CONTENT_TYPE,
  SUBSTR(PARENT_ID,INSTR(PARENT_ID,':')+1) AS PARENT_CONTENT_ID,
  SUBSTR(CHILD_ID,1,INSTR(CHILD_ID,':')-1) AS CONTENT_TYPE,
  SUBSTR(CHILD_ID,INSTR(CHILD_ID,':')+1) AS CONTENT_ID
from (
  select
    a.parent_contentnode_id as parent_id,
    a.child_contentnode_id as child_id
  from cms.all_nodes a
  where a.DEF_CONTENTTYPE in ('Sku','Category','Department','Store')
  union all
  -- get products in their primary_homes (note: reverse relationship)
  select
    r.child_contentnode_id as parent_id,
    r.parent_contentnode_id as child_id
  from cms.relationship r
  where r.DEF_NAME = 'PRIMARY_HOME'
  union all
  -- add root element
  select
    null as parent_id,
    id as child_id
  from cms.contentnode c
  where c.contenttype_id='Store'
)
