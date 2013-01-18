alter table cust.ss_variant_params modify key varchar2(256);

delete from cust.ss_variants where type = 'composite';

INSERT INTO CUST.SS_VARIANTS ( ID, CONFIG_ID, FEATURE, TYPE ) VALUES ( 
'dyf_scr_calc1', NULL, 'DYF', 'scripted'); 
INSERT INTO CUST.SS_VARIANTS ( ID, CONFIG_ID, FEATURE, TYPE ) VALUES ( 
'dyf_scr_ord1', NULL, 'DYF', 'scripted'); 
INSERT INTO CUST.SS_VARIANTS ( ID, CONFIG_ID, FEATURE, TYPE ) VALUES ( 
'dyf_scr_ord2', NULL, 'DYF', 'scripted'); 
INSERT INTO CUST.SS_VARIANTS ( ID, CONFIG_ID, FEATURE, TYPE ) VALUES ( 
'dyf_spnd', NULL, 'DYF', 'scripted'); 


INSERT INTO CUST.SS_VARIANT_PARAMS ( ID, KEY, VALUE ) VALUES ( 
'dyf_scr_calc1', 'prez_title', 'YOUR FAVORITES'); 
INSERT INTO CUST.SS_VARIANT_PARAMS ( ID, KEY, VALUE ) VALUES ( 
'dyf_scr_calc1', 'prez_desc', 'These are some of the items you''ve purchased most often'); 
INSERT INTO CUST.SS_VARIANT_PARAMS ( ID, KEY, VALUE ) VALUES ( 
'dyf_scr_calc1', 'sampling_strat', 'complicated'); 
INSERT INTO CUST.SS_VARIANT_PARAMS ( ID, KEY, VALUE ) VALUES ( 
'dyf_scr_calc1', 'cat_aggr', 'FALSE'); 
INSERT INTO CUST.SS_VARIANT_PARAMS ( ID, KEY, VALUE ) VALUES ( 
'dyf_scr_calc1', 'include_cart_items', 'FALSE'); 
INSERT INTO CUST.SS_VARIANT_PARAMS ( ID, KEY, VALUE ) VALUES ( 
'dyf_scr_calc1', 'top_n', '50'); 
INSERT INTO CUST.SS_VARIANT_PARAMS ( ID, KEY, VALUE ) VALUES ( 
'dyf_scr_calc1', 'top_perc', '20'); 
INSERT INTO CUST.SS_VARIANT_PARAMS ( ID, KEY, VALUE ) VALUES ( 
'dyf_scr_calc1', 'generator', 'PurchaseHistory:atLeast(QualityRating_Discretized2,0)'); 
INSERT INTO CUST.SS_VARIANT_PARAMS ( ID, KEY, VALUE ) VALUES ( 
'dyf_scr_calc1', 'scoring', '(55 * Recency_Normalized) + (25 * Frequency_Normalized) + (10 * QualityRating_Normalized) + (5* DealsPercentage) + (5* ReorderRate_DepartmentNormalized)'); 

INSERT INTO CUST.SS_VARIANT_PARAMS ( ID, KEY, VALUE ) VALUES ( 
'dyf_scr_ord1', 'prez_title', 'YOUR FAVORITES'); 
INSERT INTO CUST.SS_VARIANT_PARAMS ( ID, KEY, VALUE ) VALUES ( 
'dyf_scr_ord1', 'prez_desc', 'These are some of the items you''ve purchased most often'); 
INSERT INTO CUST.SS_VARIANT_PARAMS ( ID, KEY, VALUE ) VALUES ( 
'dyf_scr_ord1', 'sampling_strat', 'power'); 
INSERT INTO CUST.SS_VARIANT_PARAMS ( ID, KEY, VALUE ) VALUES ( 
'dyf_scr_ord1', 'exponent', '0.4'); 
INSERT INTO CUST.SS_VARIANT_PARAMS ( ID, KEY, VALUE ) VALUES ( 
'dyf_scr_ord1', 'cat_aggr', 'FALSE'); 
INSERT INTO CUST.SS_VARIANT_PARAMS ( ID, KEY, VALUE ) VALUES ( 
'dyf_scr_ord1', 'include_cart_items', 'FALSE'); 
INSERT INTO CUST.SS_VARIANT_PARAMS ( ID, KEY, VALUE ) VALUES ( 
'dyf_scr_ord1', 'top_n', '50'); 
INSERT INTO CUST.SS_VARIANT_PARAMS ( ID, KEY, VALUE ) VALUES ( 
'dyf_scr_ord1', 'top_perc', '20'); 
INSERT INTO CUST.SS_VARIANT_PARAMS ( ID, KEY, VALUE ) VALUES ( 
'dyf_scr_ord1', 'generator', 'PurchaseHistory:atLeast(QualityRating_Discretized2,0)'); 
INSERT INTO CUST.SS_VARIANT_PARAMS ( ID, KEY, VALUE ) VALUES ( 
'dyf_scr_ord1', 'scoring', 'Recency_Discretized;Frequency_Discretized;QualityRating_Discretized2;DealsPercentage_Discretized;ReorderRate_DepartmentNormalized'); 

INSERT INTO CUST.SS_VARIANT_PARAMS ( ID, KEY, VALUE ) VALUES ( 
'dyf_scr_ord2', 'prez_title', 'YOUR FAVORITES'); 
INSERT INTO CUST.SS_VARIANT_PARAMS ( ID, KEY, VALUE ) VALUES ( 
'dyf_scr_ord2', 'prez_desc', 'These are some of the items you''ve purchased most often'); 
INSERT INTO CUST.SS_VARIANT_PARAMS ( ID, KEY, VALUE ) VALUES ( 
'dyf_scr_ord2', 'sampling_strat', 'power'); 
INSERT INTO CUST.SS_VARIANT_PARAMS ( ID, KEY, VALUE ) VALUES ( 
'dyf_scr_ord2', 'exponent', '0.4'); 
INSERT INTO CUST.SS_VARIANT_PARAMS ( ID, KEY, VALUE ) VALUES ( 
'dyf_scr_ord2', 'cat_aggr', 'FALSE'); 
INSERT INTO CUST.SS_VARIANT_PARAMS ( ID, KEY, VALUE ) VALUES ( 
'dyf_scr_ord2', 'include_cart_items', 'FALSE'); 
INSERT INTO CUST.SS_VARIANT_PARAMS ( ID, KEY, VALUE ) VALUES ( 
'dyf_scr_ord2', 'top_n', '50'); 
INSERT INTO CUST.SS_VARIANT_PARAMS ( ID, KEY, VALUE ) VALUES ( 
'dyf_scr_ord2', 'top_perc', '20'); 
INSERT INTO CUST.SS_VARIANT_PARAMS ( ID, KEY, VALUE ) VALUES ( 
'dyf_scr_ord2', 'generator', 'PurchaseHistory:atLeast(QualityRating_Discretized2,0)'); 
INSERT INTO CUST.SS_VARIANT_PARAMS ( ID, KEY, VALUE ) VALUES ( 
'dyf_scr_ord2', 'scoring', 'Recency_Discretized;DealsPercentage_Discretized;Frequency_Discretized;0-(AmountSpent/Quantity)'); 

INSERT INTO CUST.SS_VARIANT_PARAMS ( ID, KEY, VALUE ) VALUES ( 
'dyf_spnd', 'prez_title', 'YOUR FAVORITES'); 
INSERT INTO CUST.SS_VARIANT_PARAMS ( ID, KEY, VALUE ) VALUES ( 
'dyf_spnd', 'prez_desc', 'These are some of the items you''ve purchased most often'); 
INSERT INTO CUST.SS_VARIANT_PARAMS ( ID, KEY, VALUE ) VALUES ( 
'dyf_spnd', 'sampling_strat', 'power'); 
INSERT INTO CUST.SS_VARIANT_PARAMS ( ID, KEY, VALUE ) VALUES ( 
'dyf_spnd', 'exponent', '0.4'); 
INSERT INTO CUST.SS_VARIANT_PARAMS ( ID, KEY, VALUE ) VALUES ( 
'dyf_spnd', 'cat_aggr', 'FALSE'); 
INSERT INTO CUST.SS_VARIANT_PARAMS ( ID, KEY, VALUE ) VALUES ( 
'dyf_spnd', 'include_cart_items', 'FALSE'); 
INSERT INTO CUST.SS_VARIANT_PARAMS ( ID, KEY, VALUE ) VALUES ( 
'dyf_spnd', 'top_n', '50'); 
INSERT INTO CUST.SS_VARIANT_PARAMS ( ID, KEY, VALUE ) VALUES ( 
'dyf_spnd', 'top_perc', '20'); 
INSERT INTO CUST.SS_VARIANT_PARAMS ( ID, KEY, VALUE ) VALUES ( 
'dyf_spnd', 'generator', 'PurchaseHistory:atLeast(QualityRating_Discretized2,0)'); 
INSERT INTO CUST.SS_VARIANT_PARAMS ( ID, KEY, VALUE ) VALUES ( 
'dyf_spnd', 'scoring', 'Recency_Discretized;Frequency_Discretized;QualityRating_Discretized1;AmountSpent_Discretized'); 

COMMIT;
