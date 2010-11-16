--Rollout 
Insert into CUST.SS_VARIANTS (ID,CONFIG_ID,FEATURE,TYPE,ALIAS_ID) values ('sc_also_bought',null,'YMAL','scripted',null);
Insert into CUST.SS_VARIANTS (ID,CONFIG_ID,FEATURE,TYPE,ALIAS_ID) values ('sc_also_viewed',null,'YMAL','scripted',null);

Insert into CUST.SS_VARIANT_PARAMS (ID,KEY,VALUE) values ('sc_also_bought','generator','RelatedItems_scarabAlsoBought(currentNode)');
Insert into CUST.SS_VARIANT_PARAMS (ID,KEY,VALUE) values ('sc_also_bought','exponent','0.4');
Insert into CUST.SS_VARIANT_PARAMS (ID,KEY,VALUE) values ('sc_also_bought','sampling_strat','power');

Insert into CUST.SS_VARIANT_PARAMS (ID,KEY,VALUE) values ('sc_also_viewed','generator','RelatedItems_scarabAlsoViewed(currentNode)');
Insert into CUST.SS_VARIANT_PARAMS (ID,KEY,VALUE) values ('sc_also_viewed','exponent','0.4');
Insert into CUST.SS_VARIANT_PARAMS (ID,KEY,VALUE) values ('sc_also_viewed','sampling_strat','power');
