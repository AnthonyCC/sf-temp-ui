
-- APPDEV-2737
-- Increase size of uom column in drug_nutrition_item table

-- Rollback
-- ALTER TABLE erps.drug_nutrition_item MODIFY ( uom varchar2(10) );

ALTER TABLE erps.drug_nutrition_item MODIFY ( uom varchar2(200) );
