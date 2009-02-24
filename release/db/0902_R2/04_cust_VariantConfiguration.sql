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

INSERT INTO CUST.SS_VARIANT_ASSIGNMENT ( COHORT_ID, "DATE", VARIANT_ID ) VALUES ('C1',  sysdate, 'featured-popular');
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT ( COHORT_ID, "DATE", VARIANT_ID ) VALUES ('C2',  sysdate, 'featured-popular');
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT ( COHORT_ID, "DATE", VARIANT_ID ) VALUES ('C3',  sysdate, 'featured-cms');
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT ( COHORT_ID, "DATE", VARIANT_ID ) VALUES ('C4',  sysdate, 'featured-popular');
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT ( COHORT_ID, "DATE", VARIANT_ID ) VALUES ('C5',  sysdate, 'featured-popular');
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT ( COHORT_ID, "DATE", VARIANT_ID ) VALUES ('C6',  sysdate, 'featured-popular');
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT ( COHORT_ID, "DATE", VARIANT_ID ) VALUES ('C7',  sysdate, 'featured-popular');
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT ( COHORT_ID, "DATE", VARIANT_ID ) VALUES ('C8',  sysdate, 'featured-cms');
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT ( COHORT_ID, "DATE", VARIANT_ID ) VALUES ('C9',  sysdate, 'featured-popular');
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT ( COHORT_ID, "DATE", VARIANT_ID ) VALUES ('C10',  sysdate, 'featured-cms');
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT ( COHORT_ID, "DATE", VARIANT_ID ) VALUES ('C11',  sysdate, 'featured-popular');
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT ( COHORT_ID, "DATE", VARIANT_ID ) VALUES ('C12',  sysdate, 'featured-cms');
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT ( COHORT_ID, "DATE", VARIANT_ID ) VALUES ('C13',  sysdate, 'featured-popular');
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT ( COHORT_ID, "DATE", VARIANT_ID ) VALUES ('C14',  sysdate, 'featured-cms');
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT ( COHORT_ID, "DATE", VARIANT_ID ) VALUES ('C15',  sysdate, 'featured-popular');
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT ( COHORT_ID, "DATE", VARIANT_ID ) VALUES ('C16',  sysdate, 'featured-popular');
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT ( COHORT_ID, "DATE", VARIANT_ID ) VALUES ('C17',  sysdate, 'featured-cms');
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT ( COHORT_ID, "DATE", VARIANT_ID ) VALUES ('C18',  sysdate, 'featured-popular');
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT ( COHORT_ID, "DATE", VARIANT_ID ) VALUES ('C19',  sysdate, 'featured-cms');
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT ( COHORT_ID, "DATE", VARIANT_ID ) VALUES ('C20',  sysdate, 'featured-cms');


INSERT INTO CUST.SS_VARIANT_ASSIGNMENT ( COHORT_ID, "DATE", VARIANT_ID ) VALUES ('C2',  sysdate, 'dyf_scr_calc1');
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT ( COHORT_ID, "DATE", VARIANT_ID ) VALUES ('C5',  sysdate, 'dyf_scr_calc1');
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT ( COHORT_ID, "DATE", VARIANT_ID ) VALUES ('C17',  sysdate, 'dyf_scr_calc1');
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT ( COHORT_ID, "DATE", VARIANT_ID ) VALUES ('C10',  sysdate, 'dyf_scr_calc1');
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT ( COHORT_ID, "DATE", VARIANT_ID ) VALUES ('C12',  sysdate, 'dyf_scr_ord1');
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT ( COHORT_ID, "DATE", VARIANT_ID ) VALUES ('C14',  sysdate, 'dyf_scr_ord1');
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT ( COHORT_ID, "DATE", VARIANT_ID ) VALUES ('C16',  sysdate, 'dyf_scr_ord1');
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT ( COHORT_ID, "DATE", VARIANT_ID ) VALUES ('C1',  sysdate, 'dyf_scr_ord1'); 
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT ( COHORT_ID, "DATE", VARIANT_ID ) VALUES ('C9',  sysdate, 'dyf_scr_ord2');
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT ( COHORT_ID, "DATE", VARIANT_ID ) VALUES ('C8',  sysdate, 'dyf_scr_ord2');
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT ( COHORT_ID, "DATE", VARIANT_ID ) VALUES ('C6',  sysdate, 'dyf_spnd');
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT ( COHORT_ID, "DATE", VARIANT_ID ) VALUES ('C3',  sysdate, 'dyf_spnd'); 


INSERT INTO CUST.SS_VARIANT_ASSIGNMENT ( COHORT_ID, "DATE", VARIANT_ID ) VALUES ('C18',  sysdate, 'dyf-freqbought1'); 
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT ( COHORT_ID, "DATE", VARIANT_ID ) VALUES ('C20',  sysdate, 'dyf-freqbought1'); 
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT ( COHORT_ID, "DATE", VARIANT_ID ) VALUES ('C19',  sysdate, 'dyf-freqbought1');
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT ( COHORT_ID, "DATE", VARIANT_ID ) VALUES ('C4',  sysdate, 'dyf-freqbought3');
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT ( COHORT_ID, "DATE", VARIANT_ID ) VALUES ('C7',  sysdate, 'dyf-freqbought3');
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT ( COHORT_ID, "DATE", VARIANT_ID ) VALUES ('C13',  sysdate, 'dyf-freqbought3');
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT ( COHORT_ID, "DATE", VARIANT_ID ) VALUES ('C15',  sysdate, 'dyf-freqbought3');
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT ( COHORT_ID, "DATE", VARIANT_ID ) VALUES ('C11',  sysdate, 'dyf-freqbought3');


COMMIT;
