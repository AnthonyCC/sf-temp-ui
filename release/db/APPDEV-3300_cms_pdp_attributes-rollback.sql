-- 
-- APPDEV-3179 ROLLBACK
--
-- Attributes for PDP feature
-- Be sure to apply this changeset to CMS schema!

delete from relationshipdestination where RELATIONSHIPDEFINITION_ID='Product.PDP_XSELL';
delete from relationshipdefinition where id='Product.PDP_XSELL';

delete from relationshipdestination where RELATIONSHIPDEFINITION_ID='Product.PDP_UPSELL';
delete from relationshipdefinition where id='Product.PDP_UPSELL';

delete from relationshipdestination where RELATIONSHIPDEFINITION_ID='ConfiguredProduct.PDP_XSELL';
delete from relationshipdefinition where id='ConfiguredProduct.PDP_XSELL';

delete from relationshipdestination where RELATIONSHIPDEFINITION_ID='ConfiguredProduct.PDP_UPSELL';
delete from relationshipdefinition where id='ConfiguredProduct.PDP_UPSELL';

-- Heat Rating

delete from attributedefinition where ID='Product.HEAT_RATING';
delete from lookup where LOOKUPTYPE_CODE='Product.HEAT_RATING';
delete from lookuptype where CODE='Product.HEAT_RATING';

-- Additional Images

delete from relationshipdestination where RELATIONSHIPDEFINITION_ID='Product.PROD_IMAGE_JUMBO';
delete from relationshipdefinition where id='Product.PROD_IMAGE_JUMBO';

delete from relationshipdestination where RELATIONSHIPDEFINITION_ID='ConfiguredProduct.PROD_IMAGE_JUMBO';
delete from relationshipdefinition where id='ConfiguredProduct.PROD_IMAGE_JUMBO';

delete from relationshipdestination where RELATIONSHIPDEFINITION_ID='Product.PROD_IMAGE_ITEM';
delete from relationshipdefinition where id='Product.PROD_IMAGE_ITEM';

delete from relationshipdestination where RELATIONSHIPDEFINITION_ID='ConfiguredProduct.PROD_IMAGE_ITEM';
delete from relationshipdefinition where id='ConfiguredProduct.PROD_IMAGE_ITEM';

delete from relationshipdestination where RELATIONSHIPDEFINITION_ID='Product.PROD_IMAGE_EXTRA';
delete from relationshipdefinition where id='Product.PROD_IMAGE_EXTRA';

delete from relationshipdestination where RELATIONSHIPDEFINITION_ID='ConfiguredProduct.PROD_IMAGE_EXTRA';
delete from relationshipdefinition where id='ConfiguredProduct.PROD_IMAGE_EXTRA';

