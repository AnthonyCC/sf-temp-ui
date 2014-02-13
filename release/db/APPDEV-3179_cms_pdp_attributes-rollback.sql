-- 
-- APPDEV-3179 ROLLBACK
--
-- Attributes for PDP feature
-- Be sure to apply this changeset to CMS schema!

delete from relationshipdestination where RELATIONSHIPDEFINITION_ID='Product.PDP_XSELL';
delete from relationshipdefinition where id='Product.PDP_XSELL';

delete from relationshipdestination where RELATIONSHIPDEFINITION_ID='Product.PDP_UPSELL';
delete from relationshipdefinition where id='Product.PDP_UPSELL';
