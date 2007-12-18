CREATE UNIQUE INDEX IDX_PRD_SKU_VER_UNV ON PRODUCT (SKU_CODE, VERSION, UNAVAILABILITY_STATUS);

CREATE INDEX IDX_ATR_IDS ON ATTRIBUTES (ROOT_ID,CHILD1_ID,CHILD2_ID);

CREATE INDEX IDX_MPX_PID ON MATERIALPROXY (PRODUCT_ID); 

CREATE INDEX IDX_CHR_CLID ON CHARACTERISTIC (CLASS_ID); 

CREATE INDEX IDX_CHV_CHID ON CHARVALUE (CHAR_ID); 

CREATE INDEX IDX_CVP_MID ON CHARVALUEPRICE (MAT_ID);

CREATE INDEX IDX_MPR_MID ON MATERIALPRICE (MAT_ID); 

CREATE INDEX IDX_SLU_MID ON SALESUNIT (MAT_ID);

CREATE UNIQUE INDEX IDX_MATL_SAPID_VER ON  MATERIAL(SAP_ID, VERSION);                                          
                                                                                              
CREATE INDEX IDX_MPX_MID ON MATERIALPROXY(MAT_ID);

CREATE UNIQUE INDEX NINFO_LOCATER ON NUTRITION_INFO (SKUCODE, TYPE, PRIORITY);

CREATE INDEX ERPS.HISTORY_VERSION_DATE_IDX
	ON ERPS.HISTORY
	(VERSION,
	DATE_CREATED);
 
CREATE INDEX ERPS.PRODUCT_VERSION_SKU_AVAIL_IDX
	ON ERPS.PRODUCT
	(VERSION,
	SKU_CODE,
	UNAVAILABILITY_STATUS);

CREATE INDEX ERPS.PRODUCT_SKU_AVAIL_VERSION_IDX
	ON ERPS.PRODUCT
	(SKU_CODE,
	UNAVAILABILITY_STATUS,
	VERSION);
