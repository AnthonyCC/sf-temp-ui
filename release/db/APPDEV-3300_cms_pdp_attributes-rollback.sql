-- 
-- APPDEV-3300 ROLLBACK
--
-- Attributes for PDP feature
-- Be sure to apply this changeset on the CMS DB!

delete from cms.relationshipdestination where RELATIONSHIPDEFINITION_ID='Product.PDP_XSELL';
delete from cms.relationshipdefinition where id='Product.PDP_XSELL';

delete from cms.relationshipdestination where RELATIONSHIPDEFINITION_ID='Product.PDP_UPSELL';
delete from cms.relationshipdefinition where id='Product.PDP_UPSELL';

delete from cms.relationshipdestination where RELATIONSHIPDEFINITION_ID='ConfiguredProduct.PDP_XSELL';
delete from cms.relationshipdefinition where id='ConfiguredProduct.PDP_XSELL';

delete from cms.relationshipdestination where RELATIONSHIPDEFINITION_ID='ConfiguredProduct.PDP_UPSELL';
delete from cms.relationshipdefinition where id='ConfiguredProduct.PDP_UPSELL';

-- Heat Rating

delete from cms.attributedefinition where ID='Product.HEAT_RATING';
delete from cms.lookup where LOOKUPTYPE_CODE='Product.HEAT_RATING';
delete from cms.lookuptype where CODE='Product.HEAT_RATING';

-- Additional Images

delete from cms.relationshipdestination where RELATIONSHIPDEFINITION_ID='Product.PROD_IMAGE_JUMBO';
delete from cms.relationshipdefinition where id='Product.PROD_IMAGE_JUMBO';

delete from cms.relationshipdestination where RELATIONSHIPDEFINITION_ID='ConfiguredProduct.PROD_IMAGE_JUMBO';
delete from cms.relationshipdefinition where id='ConfiguredProduct.PROD_IMAGE_JUMBO';

delete from cms.relationshipdestination where RELATIONSHIPDEFINITION_ID='Product.PROD_IMAGE_ITEM';
delete from cms.relationshipdefinition where id='Product.PROD_IMAGE_ITEM';

delete from cms.relationshipdestination where RELATIONSHIPDEFINITION_ID='ConfiguredProduct.PROD_IMAGE_ITEM';
delete from cms.relationshipdefinition where id='ConfiguredProduct.PROD_IMAGE_ITEM';

delete from cms.relationshipdestination where RELATIONSHIPDEFINITION_ID='Product.PROD_IMAGE_EXTRA';
delete from cms.relationshipdefinition where id='Product.PROD_IMAGE_EXTRA';

delete from cms.relationshipdestination where RELATIONSHIPDEFINITION_ID='ConfiguredProduct.PROD_IMAGE_EXTRA';
delete from cms.relationshipdefinition where id='ConfiguredProduct.PROD_IMAGE_EXTRA';

