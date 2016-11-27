CREATE TABLE MIS.UNAV_ITEMS_INV
(
  RUN_DATE         DATE                         NOT NULL,
  RUN_INSTANCE     NUMBER                       NOT NULL,
  DELIVERY_DATE    DATE                         NOT NULL,
  SO_TEMPLATE_ID   VARCHAR2(30 BYTE)            NOT NULL,
  ORDER_ID         VARCHAR2(30 BYTE)            NOT NULL,
  CUSTOMER_ID      VARCHAR2(30 BYTE)            NOT NULL,
  SKU_CODE         VARCHAR2(20 BYTE)            NOT NULL,
  MATERIAL_NUM     VARCHAR2(20 BYTE)            NOT NULL,
  QUANTITY         NUMBER                       NOT NULL,
  SALES_UNIT       VARCHAR2(30 BYTE)            NOT NULL,
  INSERT_DATETIME  DATE                         NOT NULL,
  REASON           VARCHAR2(50 BYTE)            NOT NULL,
  ALT_SKUCODE      VARCHAR2(200 BYTE),
  PRODUCT_NAME     VARCHAR2(256 BYTE)
);
