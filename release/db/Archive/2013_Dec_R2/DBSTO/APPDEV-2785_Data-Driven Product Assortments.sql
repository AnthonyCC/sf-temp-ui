--[APPDEV-2785]-Data-Driven Product Assortments

alter table  erps.product_promotion_group add(ERP_CATEGORY varchar2(50), ERP_CAT_POSITION varchar2(5));
alter table  erps.product_promotion_group add(ERP_PP_ID varchar2(16));
CREATE INDEX erps.product_promotion_id_IDX ON erps.product_promotion_group (ERP_PP_ID);
