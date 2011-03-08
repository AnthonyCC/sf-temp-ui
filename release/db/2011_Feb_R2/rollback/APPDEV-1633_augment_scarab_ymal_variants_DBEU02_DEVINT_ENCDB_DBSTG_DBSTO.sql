--
-- APPDEV-1633 Rollback Script
--

-- Drop new variants
delete from CUST.SS_VARIANT_ASSIGNMENT where VARIANT_ID in ('ymal_ss_c3', 'sc_related_2');
delete from CUST.SS_VARIANT_PARAMS where id in ('ymal_ss_c3', 'sc_related_2', 'sc_related_merch');
delete from CUST.SS_VARIANTS where id in ('ymal_ss_c3', 'sc_related_2', 'sc_related_merch');


--
-- Restore variants
--
Insert into SS_VARIANTS (ID,CONFIG_ID,FEATURE,TYPE,ALIAS_ID) values ('sc_also_bought',null,'YMAL','scripted',null);
Insert into SS_VARIANTS (ID,CONFIG_ID,FEATURE,TYPE,ALIAS_ID) values ('sc_also_viewed',null,'YMAL','scripted',null);
insert into SS_VARIANTS (id,CONFIG_ID,FEATURE,type,ALIAS_ID) values ('sc_related_new',null,'YMAL','scripted',null);

Insert into SS_VARIANT_PARAMS (ID,KEY,VALUE) values ('sc_also_bought','generator','RelatedItems_scarabAlsoBought(currentNode)');
Insert into SS_VARIANT_PARAMS (ID,KEY,VALUE) values ('sc_also_bought','exponent','0.4');
Insert into SS_VARIANT_PARAMS (ID,KEY,VALUE) values ('sc_also_bought','sampling_strat','power');
Insert into SS_VARIANT_PARAMS (ID,KEY,VALUE) values ('sc_also_viewed','generator','RelatedItems_scarabAlsoViewed(currentNode)');
Insert into SS_VARIANT_PARAMS (ID,KEY,VALUE) values ('sc_also_viewed','exponent','0.4');
Insert into SS_VARIANT_PARAMS (ID,KEY,VALUE) values ('sc_also_viewed','sampling_strat','power');
Insert into SS_VARIANT_PARAMS (ID,KEY,VALUE) values ('sc_related_new','generator','RelatedItems_scarabRelated(currentNode)');
Insert into SS_VARIANT_PARAMS (ID,KEY,VALUE) values ('sc_related_new','exponent','0.4');
insert into SS_VARIANT_PARAMS (id,key,value) values ('sc_related_new','sampling_strat','power');
