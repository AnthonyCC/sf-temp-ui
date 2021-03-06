----------------------------------------------------------
--	Create tables, constraints and indices		--
--	for new generic nutrition panels		--
-- 	with support for multiple panel type		--
----------------------------------------------------------

CREATE TABLE "ERPS"."NUTRITION_PANEL" (
	"ID"			VARCHAR2(16 BYTE) NOT NULL ENABLE, 
	"SKU_CODE"		VARCHAR2(16 BYTE) NOT NULL ENABLE, 
	"TYPE" 			VARCHAR2(64 BYTE) NOT NULL ENABLE, 
	"DATE_MODIFIED" 	DATE, 

	 CONSTRAINT "NUTRITION_PANEL_PK" PRIMARY KEY ("ID")
 );
CREATE UNIQUE INDEX NUTRITION_PANEL_SKU_IDX ON NUTRITION_PANEL (SKU_CODE);
CREATE INDEX NUTRITION_PANEL_DATE_IDX ON NUTRITION_PANEL (DATE_MODIFIED);



CREATE TABLE "ERPS"."NUTRITION_SECTION" (
	"ID" 		VARCHAR2(16 BYTE) NOT NULL ENABLE, 
	"PANEL_ID" 	VARCHAR2(16 BYTE) NOT NULL ENABLE, 
	"TITLE" 	VARCHAR2(500 CHAR), 
	"TYPE" 		VARCHAR2(64 BYTE) NOT NULL ENABLE, 
	"POSITION" 	NUMBER(5,0), 
	"IMPORTANCE" 	NUMBER(5,0), 

	CONSTRAINT "NUTRITION_SECTION_PK" PRIMARY KEY ("ID"),
	CONSTRAINT "NUTRITION_SECTION_FK1" FOREIGN KEY ("PANEL_ID") REFERENCES "ERPS"."NUTRITION_PANEL" ("ID") ON DELETE CASCADE
);
CREATE INDEX NUTRITION_SECTION_FK_IDX ON NUTRITION_SECTION (PANEL_ID);



CREATE TABLE "ERPS"."NUTRITION_ITEM" (
	"ID" 				VARCHAR2(16 BYTE) NOT NULL ENABLE, 
	"SECTION_ID" 			VARCHAR2(16 BYTE) NOT NULL ENABLE, 
	"VALUE1" 			VARCHAR2(2000 CHAR),
	"VALUE2" 			VARCHAR2(2000 CHAR), 
	"INGREDIENT_VALUE" 		NUMBER(10,2), 
	"UOM" 				VARCHAR2(200 CHAR), 
	"POSITION" 			NUMBER(5,0), 
	"FLAGS" 			VARCHAR2(64 CHAR), 

	CONSTRAINT "NUTRITION_ITEM_PK" PRIMARY KEY ("ID"),
	CONSTRAINT "NUTRITION_ITEM_FK1" FOREIGN KEY ("SECTION_ID") REFERENCES "ERPS"."NUTRITION_SECTION" ("ID") ON DELETE CASCADE
);
CREATE INDEX ERPS.NUTRITION_ITEM_FK_IDX ON NUTRITION_ITEM (SECTION_ID);


