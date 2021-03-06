
INSERT INTO "ERPS"."NUTRITION_PANEL" 
	(ID, SKU_CODE, TYPE, DATE_MODIFIED)
SELECT 
	ID, SKU_CODE, 'DRUG' as TYPE, DATE_MODIFIED 
FROM "ERPS"."DRUG_PANEL";


INSERT INTO "ERPS"."NUTRITION_SECTION" 
	(ID, PANEL_ID, TITLE, TYPE, POSITION, IMPORTANCE)
SELECT 
	ID, PANEL_ID, TITLE, UPPER(TYPE), POSITION, IMPORTANCE
FROM "ERPS"."DRUG_NUTRITION_SECTION";


INSERT INTO "ERPS"."NUTRITION_ITEM" 
	(ID, SECTION_ID, VALUE1, VALUE2, INGREDIENT_VALUE, UOM, POSITION, FLAGS)
SELECT 
	ID, SECTION_ID, VALUE1, VALUE2, INGREDIENT_VALUE, UOM, POSITION,
	(NVL2(BULLETED, 'B', '') || NVL2(IMPORTANT, 'I', '') || NVL2(NEWLINE, 'N', '') || NVL2(SEPARATOR, 'S', '')) as FLAGS 
FROM "ERPS"."DRUG_NUTRITION_ITEM";

