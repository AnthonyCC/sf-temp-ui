-- Rollout

-- Cart recommender site feature
Insert into CUST.SS_SITE_FEATURE (ID,TITLE) values ('SCARAB_CART', 'ScarabReseach Cart Recommendations');

-- Cart recommender variant for Cart recommender site feature
Insert into CUST.SS_VARIANTS (ID,CONFIG_ID,FEATURE,TYPE,ALIAS_ID) values ('sc_cart_recommender',null,'SCARAB_CART','scripted',null);

-- Cart recommender parameters
Insert into CUST.SS_VARIANT_PARAMS (ID,KEY,VALUE) values ('sc_cart_recommender','generator','RelatedItems_scarabCart(cartContents)');
Insert into CUST.SS_VARIANT_PARAMS (ID,KEY,VALUE) values ('sc_cart_recommender','exponent','0.4');
Insert into CUST.SS_VARIANT_PARAMS (ID,KEY,VALUE) values ('sc_cart_recommender','sampling_strat','power');

-- Cohort assignment for cart recommender
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (cohort_id, variant_id, "DATE") values ('C1', 'sc_cart_recommender', sysdate);
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (cohort_id, variant_id, "DATE") values ('C2', 'sc_cart_recommender', sysdate);
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (cohort_id, variant_id, "DATE") values ('C3', 'sc_cart_recommender', sysdate);
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (cohort_id, variant_id, "DATE") values ('C4', 'sc_cart_recommender', sysdate);
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (cohort_id, variant_id, "DATE") values ('C5', 'sc_cart_recommender', sysdate);
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (cohort_id, variant_id, "DATE") values ('C6', 'sc_cart_recommender', sysdate);
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (cohort_id, variant_id, "DATE") values ('C7', 'sc_cart_recommender', sysdate);
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (cohort_id, variant_id, "DATE") values ('C8', 'sc_cart_recommender', sysdate);
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (cohort_id, variant_id, "DATE") values ('C9', 'sc_cart_recommender', sysdate);
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (cohort_id, variant_id, "DATE") values ('C10', 'sc_cart_recommender', sysdate);
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (cohort_id, variant_id, "DATE") values ('C11', 'sc_cart_recommender', sysdate);
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (cohort_id, variant_id, "DATE") values ('C12', 'sc_cart_recommender', sysdate);
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (cohort_id, variant_id, "DATE") values ('C13', 'sc_cart_recommender', sysdate);
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (cohort_id, variant_id, "DATE") values ('C14', 'sc_cart_recommender', sysdate);
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (cohort_id, variant_id, "DATE") values ('C15', 'sc_cart_recommender', sysdate);
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (cohort_id, variant_id, "DATE") values ('C16', 'sc_cart_recommender', sysdate);
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (cohort_id, variant_id, "DATE") values ('C17', 'sc_cart_recommender', sysdate);
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (cohort_id, variant_id, "DATE") values ('C18', 'sc_cart_recommender', sysdate);
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (cohort_id, variant_id, "DATE") values ('C19', 'sc_cart_recommender', sysdate);
INSERT INTO CUST.SS_VARIANT_ASSIGNMENT (cohort_id, variant_id, "DATE") values ('C20', 'sc_cart_recommender', sysdate);

-- Scarab alsoBought, alsoViewed and related Variants for YMAL site feature
Insert into CUST.SS_VARIANTS (ID,CONFIG_ID,FEATURE,TYPE,ALIAS_ID) values ('sc_also_bought',null,'YMAL','scripted',null);
Insert into CUST.SS_VARIANTS (ID,CONFIG_ID,FEATURE,TYPE,ALIAS_ID) values ('sc_also_viewed',null,'YMAL','scripted',null);
Insert into CUST.SS_VARIANTS (ID,CONFIG_ID,FEATURE,TYPE,ALIAS_ID) values ('sc_related_new',null,'YMAL','scripted',null);

-- Variant params for alsoBought, alsoViewed, related
Insert into CUST.SS_VARIANT_PARAMS (ID,KEY,VALUE) values ('sc_also_bought','generator','RelatedItems_scarabAlsoBought(currentNode)');
Insert into CUST.SS_VARIANT_PARAMS (ID,KEY,VALUE) values ('sc_also_bought','exponent','0.4');
Insert into CUST.SS_VARIANT_PARAMS (ID,KEY,VALUE) values ('sc_also_bought','sampling_strat','power');

Insert into CUST.SS_VARIANT_PARAMS (ID,KEY,VALUE) values ('sc_also_viewed','generator','RelatedItems_scarabAlsoViewed(currentNode)');
Insert into CUST.SS_VARIANT_PARAMS (ID,KEY,VALUE) values ('sc_also_viewed','exponent','0.4');
Insert into CUST.SS_VARIANT_PARAMS (ID,KEY,VALUE) values ('sc_also_viewed','sampling_strat','power');

Insert into CUST.SS_VARIANT_PARAMS (ID,KEY,VALUE) values ('sc_related_new','generator','RelatedItems_scarabRelated(currentNode)');
Insert into CUST.SS_VARIANT_PARAMS (ID,KEY,VALUE) values ('sc_related_new','exponent','0.4');
Insert into CUST.SS_VARIANT_PARAMS (ID,KEY,VALUE) values ('sc_related_new','sampling_strat','power');
