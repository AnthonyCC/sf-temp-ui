
Insert into CUST.SS_VARIANTS (ID,CONFIG_ID,FEATURE,TYPE,ALIAS_ID) values ('sc_cart_recom',null,'SCARAB_PERSONAL','scripted',null);

Insert into CUST.SS_VARIANTS (ID,CONFIG_ID,FEATURE,TYPE,ALIAS_ID) values ('sc_personal',null,'SCARAB_PERSONAL','scripted',null);
Insert into CUST.SS_VARIANTS (ID,CONFIG_ID,FEATURE,TYPE,ALIAS_ID) values ('sc_related',null,'YMAL','scripted',null);

Insert into CUST.SS_VARIANT_PARAMS (ID,KEY,VALUE) values ('sc_personal','generator','PersonalizedItems_scarab1()');
Insert into CUST.SS_VARIANT_PARAMS (ID,KEY,VALUE) values ('sc_personal','exponent','0.4');
Insert into CUST.SS_VARIANT_PARAMS (ID,KEY,VALUE) values ('sc_personal','sampling_strat','power');

Insert into CUST.SS_VARIANT_PARAMS (ID,KEY,VALUE) values ('sc_related','generator','RelatedItems_scarab1(currentNode)');
Insert into CUST.SS_VARIANT_PARAMS (ID,KEY,VALUE) values ('sc_related','exponent','0.4');
Insert into CUST.SS_VARIANT_PARAMS (ID,KEY,VALUE) values ('sc_related','sampling_strat','power');

Insert into CUST.SS_VARIANT_PARAMS (ID,KEY,VALUE) values ('sc_cart_recom','generator','RelatedItems_scarabCart1(cartContents)');
Insert into CUST.SS_VARIANT_PARAMS (ID,KEY,VALUE) values ('sc_cart_recom','exponent','0.4');
Insert into CUST.SS_VARIANT_PARAMS (ID,KEY,VALUE) values ('sc_cart_recom','sampling_strat','power');


Insert into CUST.SS_VARIANT_PARAMS (ID,KEY,VALUE) values ('sc_personal','prez_title','RECOMMENDED FOR YOU');
Insert into CUST.SS_VARIANT_PARAMS (ID,KEY,VALUE) values ('sc_personal','prez_desc','People who bought the same items as you enjoyed...');

Insert into CUST.SS_VARIANT_PARAMS (ID,KEY,VALUE) values ('sc_related','prez_title','You Might Also Like');
Insert into CUST.SS_VARIANT_PARAMS (ID,KEY,VALUE) values ('sc_related','prez_desc','Customers who bought this item also bought...');

Insert into CUST.SS_VARIANT_PARAMS (ID,KEY,VALUE) values ('sc_cart_recom','prez_title','RECOMMENDED FOR YOU');
Insert into CUST.SS_VARIANT_PARAMS (ID,KEY,VALUE) values ('sc_cart_recom','prez_desc','Customers with similar carts also bought...');


INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (cohort_id, variant_id, "DATE") values ('C1', 'sc_personal', sysdate);
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (cohort_id, variant_id, "DATE") values ('C2', 'sc_personal', sysdate);
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (cohort_id, variant_id, "DATE") values ('C3', 'sc_personal', sysdate);
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (cohort_id, variant_id, "DATE") values ('C4', 'sc_personal', sysdate);
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (cohort_id, variant_id, "DATE") values ('C5', 'sc_personal', sysdate);
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (cohort_id, variant_id, "DATE") values ('C6', 'sc_personal', sysdate);
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (cohort_id, variant_id, "DATE") values ('C7', 'sc_personal', sysdate);
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (cohort_id, variant_id, "DATE") values ('C8', 'sc_personal', sysdate);
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (cohort_id, variant_id, "DATE") values ('C9', 'sc_personal', sysdate);
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (cohort_id, variant_id, "DATE") values ('C10', 'sc_personal', sysdate);
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (cohort_id, variant_id, "DATE") values ('C11', 'sc_personal', sysdate);
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (cohort_id, variant_id, "DATE") values ('C12', 'sc_personal', sysdate);
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (cohort_id, variant_id, "DATE") values ('C13', 'sc_personal', sysdate);
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (cohort_id, variant_id, "DATE") values ('C14', 'sc_personal', sysdate);
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (cohort_id, variant_id, "DATE") values ('C15', 'sc_personal', sysdate);
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (cohort_id, variant_id, "DATE") values ('C16', 'sc_personal', sysdate);
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (cohort_id, variant_id, "DATE") values ('C17', 'sc_personal', sysdate);
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (cohort_id, variant_id, "DATE") values ('C18', 'sc_personal', sysdate);
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (cohort_id, variant_id, "DATE") values ('C19', 'sc_personal', sysdate);
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (cohort_id, variant_id, "DATE") values ('C20', 'sc_personal', sysdate);


Insert into CUST.SS_VARIANTS (ID,CONFIG_ID,FEATURE,TYPE,ALIAS_ID) values ('ymal_ss_c2',null,'YMAL','alias','ymal_ss');

Insert into CUST.SS_VARIANTS (ID,CONFIG_ID,FEATURE,TYPE,ALIAS_ID) values ('tabs_var1fave_c2',null,'CART_N_TABS','tab-strategy',null);
Insert into CUST.SS_VARIANTS (ID,CONFIG_ID,FEATURE,TYPE,ALIAS_ID) values ('tabs_var1f_scrb2',null,'CART_N_TABS','tab-strategy',null);


Insert into CUST.SS_TAB_STRATEGY_PRIORITY (TAB_STRATEGY_ID,SITE_FEATURE_ID,PRIMARY_PRIORITY,SECONDARY_PRIORITY) values ('tabs_var1fave_c2','C_SAVE_YF',10,10);
Insert into CUST.SS_TAB_STRATEGY_PRIORITY (TAB_STRATEGY_ID,SITE_FEATURE_ID,PRIMARY_PRIORITY,SECONDARY_PRIORITY) values ('tabs_var1fave_c2','DYF',10,20);
Insert into CUST.SS_TAB_STRATEGY_PRIORITY (TAB_STRATEGY_ID,SITE_FEATURE_ID,PRIMARY_PRIORITY,SECONDARY_PRIORITY) values ('tabs_var1fave_c2','C_SAVE_FDF',10,25);
Insert into CUST.SS_TAB_STRATEGY_PRIORITY (TAB_STRATEGY_ID,SITE_FEATURE_ID,PRIMARY_PRIORITY,SECONDARY_PRIORITY) values ('tabs_var1fave_c2','FAVORITES',10,30);
Insert into CUST.SS_TAB_STRATEGY_PRIORITY (TAB_STRATEGY_ID,SITE_FEATURE_ID,PRIMARY_PRIORITY,SECONDARY_PRIORITY) values ('tabs_var1fave_c2','C_HEALTHY_SNACKS',20,10);
Insert into CUST.SS_TAB_STRATEGY_PRIORITY (TAB_STRATEGY_ID,SITE_FEATURE_ID,PRIMARY_PRIORITY,SECONDARY_PRIORITY) values ('tabs_var1fave_c2','C_DEALS',20,20);
Insert into CUST.SS_TAB_STRATEGY_PRIORITY (TAB_STRATEGY_ID,SITE_FEATURE_ID,PRIMARY_PRIORITY,SECONDARY_PRIORITY) values ('tabs_var1fave_c2','C_YMAL',20,30);
Insert into CUST.SS_TAB_STRATEGY_PRIORITY (TAB_STRATEGY_ID,SITE_FEATURE_ID,PRIMARY_PRIORITY,SECONDARY_PRIORITY) values ('tabs_var1fave_c2','C_PEAK_FRUIT',30,10);
Insert into CUST.SS_TAB_STRATEGY_PRIORITY (TAB_STRATEGY_ID,SITE_FEATURE_ID,PRIMARY_PRIORITY,SECONDARY_PRIORITY) values ('tabs_var1fave_c2','C_PEAK_PRODUCE',30,20);
Insert into CUST.SS_TAB_STRATEGY_PRIORITY (TAB_STRATEGY_ID,SITE_FEATURE_ID,PRIMARY_PRIORITY,SECONDARY_PRIORITY) values ('tabs_var1fave_c2','C_NEW_PRODUCTS',30,30);

Insert into CUST.SS_TAB_STRATEGY_PRIORITY (TAB_STRATEGY_ID,SITE_FEATURE_ID,PRIMARY_PRIORITY,SECONDARY_PRIORITY) values ('tabs_var1f_scrb2','C_SAVE_YF',10,10);
Insert into CUST.SS_TAB_STRATEGY_PRIORITY (TAB_STRATEGY_ID,SITE_FEATURE_ID,PRIMARY_PRIORITY,SECONDARY_PRIORITY) values ('tabs_var1f_scrb2','DYF',10,20);
Insert into CUST.SS_TAB_STRATEGY_PRIORITY (TAB_STRATEGY_ID,SITE_FEATURE_ID,PRIMARY_PRIORITY,SECONDARY_PRIORITY) values ('tabs_var1f_scrb2','C_SAVE_FDF',10,25);
Insert into CUST.SS_TAB_STRATEGY_PRIORITY (TAB_STRATEGY_ID,SITE_FEATURE_ID,PRIMARY_PRIORITY,SECONDARY_PRIORITY) values ('tabs_var1f_scrb2','FAVORITES',10,30);
Insert into CUST.SS_TAB_STRATEGY_PRIORITY (TAB_STRATEGY_ID,SITE_FEATURE_ID,PRIMARY_PRIORITY,SECONDARY_PRIORITY) values ('tabs_var1f_scrb2','SCARAB_PERSONAL',20,10);
Insert into CUST.SS_TAB_STRATEGY_PRIORITY (TAB_STRATEGY_ID,SITE_FEATURE_ID,PRIMARY_PRIORITY,SECONDARY_PRIORITY) values ('tabs_var1f_scrb2','C_PEAK_FRUIT',20,20);
Insert into CUST.SS_TAB_STRATEGY_PRIORITY (TAB_STRATEGY_ID,SITE_FEATURE_ID,PRIMARY_PRIORITY,SECONDARY_PRIORITY) values ('tabs_var1f_scrb2','C_PEAK_PRODUCE',20,30);
Insert into CUST.SS_TAB_STRATEGY_PRIORITY (TAB_STRATEGY_ID,SITE_FEATURE_ID,PRIMARY_PRIORITY,SECONDARY_PRIORITY) values ('tabs_var1f_scrb2','C_NEW_PRODUCTS',20,40);
Insert into CUST.SS_TAB_STRATEGY_PRIORITY (TAB_STRATEGY_ID,SITE_FEATURE_ID,PRIMARY_PRIORITY,SECONDARY_PRIORITY) values ('tabs_var1f_scrb2','C_HEALTHY_SNACKS',30,10);
Insert into CUST.SS_TAB_STRATEGY_PRIORITY (TAB_STRATEGY_ID,SITE_FEATURE_ID,PRIMARY_PRIORITY,SECONDARY_PRIORITY) values ('tabs_var1f_scrb2','C_DEALS',30,20);
Insert into CUST.SS_TAB_STRATEGY_PRIORITY (TAB_STRATEGY_ID,SITE_FEATURE_ID,PRIMARY_PRIORITY,SECONDARY_PRIORITY) values ('tabs_var1f_scrb2','C_YMAL',30,30);




INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (cohort_id, variant_id, "DATE") values ('C1', 'tabs_var1fave_c2', sysdate);
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (cohort_id, variant_id, "DATE") values ('C2', 'tabs_var1f_scrb2', sysdate);
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (cohort_id, variant_id, "DATE") values ('C3', 'tabs_var1fave_c2', sysdate);
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (cohort_id, variant_id, "DATE") values ('C4', 'tabs_var1fave_c2', sysdate);
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (cohort_id, variant_id, "DATE") values ('C5', 'tabs_var1fave_c2', sysdate);
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (cohort_id, variant_id, "DATE") values ('C6', 'tabs_var1fave_c2', sysdate);
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (cohort_id, variant_id, "DATE") values ('C7', 'tabs_var1fave_c2', sysdate);
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (cohort_id, variant_id, "DATE") values ('C8', 'tabs_var1fave_c2', sysdate);
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (cohort_id, variant_id, "DATE") values ('C9', 'tabs_var1f_scrb2', sysdate);
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (cohort_id, variant_id, "DATE") values ('C10', 'tabs_var1fave_c2', sysdate);
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (cohort_id, variant_id, "DATE") values ('C11', 'tabs_var1fave_c2', sysdate);
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (cohort_id, variant_id, "DATE") values ('C12', 'tabs_var1fave_c2', sysdate);
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (cohort_id, variant_id, "DATE") values ('C13', 'tabs_var1fave_c2', sysdate);
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (cohort_id, variant_id, "DATE") values ('C14', 'tabs_var1fave_c2', sysdate);
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (cohort_id, variant_id, "DATE") values ('C15', 'tabs_var1fave_c2', sysdate);
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (cohort_id, variant_id, "DATE") values ('C16', 'tabs_var1fave_c2', sysdate);
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (cohort_id, variant_id, "DATE") values ('C17', 'tabs_var1fave_c2', sysdate);
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (cohort_id, variant_id, "DATE") values ('C18', 'tabs_var1fave_c2', sysdate);
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (cohort_id, variant_id, "DATE") values ('C19', 'tabs_var1fave_c2', sysdate);
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (cohort_id, variant_id, "DATE") values ('C20', 'tabs_var1f_scrb2', sysdate);





