-- 
-- APPDEV-3300
-- Attributes for PDP feature
-- Be sure to apply this changeset on the CMS DB!

-- Product definitions
--
INSERT INTO cms.relationshipdefinition (NAME,ID,CONTENTTYPE_ID,INHERITABLE,REQUIRED,NAVIGABLE,READONLY,LABEL,CARDINALITY_CODE)
 	VALUES ('PDP_UPSELL','Product.PDP_UPSELL','Product','F','F','F','F','Upsell','Many');
INSERT INTO cms.relationshipdefinition (NAME,ID,CONTENTTYPE_ID,INHERITABLE,REQUIRED,NAVIGABLE,READONLY,LABEL,CARDINALITY_CODE)
	VALUES ('PDP_XSELL','Product.PDP_XSELL','Product','F','F','F','F','Cross-Sell','Many');


INSERT INTO cms.relationshipdestination (ID, RELATIONSHIPDEFINITION_ID,CONTENTTYPE_ID,REVERSE_ATTRIBUTE_NAME,REVERSE_ATTRIBUTE_LABEL)
	VALUES ('Product.PDP_UPSELL.Product','Product.PDP_UPSELL','Product',NULL,NULL);
INSERT INTO cms.relationshipdestination (ID, RELATIONSHIPDEFINITION_ID,CONTENTTYPE_ID,REVERSE_ATTRIBUTE_NAME,REVERSE_ATTRIBUTE_LABEL)
	VALUES ('Product.PDP_UPSELL.ConfiguredProduct','Product.PDP_UPSELL','ConfiguredProduct',NULL,NULL);

INSERT INTO cms.relationshipdestination (ID, RELATIONSHIPDEFINITION_ID,CONTENTTYPE_ID,REVERSE_ATTRIBUTE_NAME,REVERSE_ATTRIBUTE_LABEL)
	VALUES ('Product.PDP_XSELL.Product','Product.PDP_XSELL','Product',NULL,NULL);
INSERT INTO cms.relationshipdestination (ID, RELATIONSHIPDEFINITION_ID,CONTENTTYPE_ID,REVERSE_ATTRIBUTE_NAME,REVERSE_ATTRIBUTE_LABEL)
	VALUES ('Product.PDP_XSELL.ConfiguredProduct','Product.PDP_XSELL','ConfiguredProduct',NULL,NULL);

-- ConfiguredProduct definitions
--
INSERT INTO cms.relationshipdefinition (NAME,ID,CONTENTTYPE_ID,INHERITABLE,REQUIRED,NAVIGABLE,READONLY,LABEL,CARDINALITY_CODE)
 	VALUES ('PDP_UPSELL','ConfiguredProduct.PDP_UPSELL','ConfiguredProduct','F','F','F','F','Upsell','Many');
INSERT INTO cms.relationshipdefinition (NAME,ID,CONTENTTYPE_ID,INHERITABLE,REQUIRED,NAVIGABLE,READONLY,LABEL,CARDINALITY_CODE)
	VALUES ('PDP_XSELL','ConfiguredProduct.PDP_XSELL','ConfiguredProduct','F','F','F','F','Cross-Sell','Many');


INSERT INTO cms.relationshipdestination (ID, RELATIONSHIPDEFINITION_ID,CONTENTTYPE_ID,REVERSE_ATTRIBUTE_NAME,REVERSE_ATTRIBUTE_LABEL)
	VALUES ('ConfiguredProduct.PDP_UPSELL.Product','ConfiguredProduct.PDP_UPSELL','Product',NULL,NULL);
INSERT INTO cms.relationshipdestination (ID, RELATIONSHIPDEFINITION_ID,CONTENTTYPE_ID,REVERSE_ATTRIBUTE_NAME,REVERSE_ATTRIBUTE_LABEL)
	VALUES ('ConfiguredProduct.PDP_UPSELL.ConfiguredProduct','ConfiguredProduct.PDP_UPSELL','ConfiguredProduct',NULL,NULL);

INSERT INTO cms.relationshipdestination (ID, RELATIONSHIPDEFINITION_ID,CONTENTTYPE_ID,REVERSE_ATTRIBUTE_NAME,REVERSE_ATTRIBUTE_LABEL)
	VALUES ('ConfiguredProduct.PDP_XSELL.Product','ConfiguredProduct.PDP_XSELL','Product',NULL,NULL);
INSERT INTO cms.relationshipdestination (ID, RELATIONSHIPDEFINITION_ID,CONTENTTYPE_ID,REVERSE_ATTRIBUTE_NAME,REVERSE_ATTRIBUTE_LABEL)
	VALUES ('ConfiguredProduct.PDP_XSELL.ConfiguredProduct','ConfiguredProduct.PDP_XSELL','ConfiguredProduct',NULL,NULL);


-- Heat Rating
--

-- define type
INSERT INTO cms.lookuptype (CODE,NAME,DESCRIPTION) VALUES ('Product.HEAT_RATING','Product.HEAT_RATING',NULL);

-- enum values
INSERT INTO cms.lookup (LOOKUPTYPE_CODE,CODE,LABEL,DESCRIPTION,ORDINAL) VALUES ('Product.HEAT_RATING','-1','N/A','-1',1);
INSERT INTO cms.lookup (LOOKUPTYPE_CODE,CODE,LABEL,DESCRIPTION,ORDINAL) VALUES ('Product.HEAT_RATING','0','No Heat','0',2);
INSERT INTO cms.lookup (LOOKUPTYPE_CODE,CODE,LABEL,DESCRIPTION,ORDINAL) VALUES ('Product.HEAT_RATING','1','Minor Heat','1',3);
INSERT INTO cms.lookup (LOOKUPTYPE_CODE,CODE,LABEL,DESCRIPTION,ORDINAL) VALUES ('Product.HEAT_RATING','2','Subtle Heat','2',4);
INSERT INTO cms.lookup (LOOKUPTYPE_CODE,CODE,LABEL,DESCRIPTION,ORDINAL) VALUES ('Product.HEAT_RATING','3','Medium Heat','3',5);
INSERT INTO cms.lookup (LOOKUPTYPE_CODE,CODE,LABEL,DESCRIPTION,ORDINAL) VALUES ('Product.HEAT_RATING','4','Strong Heat','4',6);
INSERT INTO cms.lookup (LOOKUPTYPE_CODE,CODE,LABEL,DESCRIPTION,ORDINAL) VALUES ('Product.HEAT_RATING','5','Fiery Heat','5',7);

-- append to product attributes
INSERT INTO cms.attributedefinition (NAME,ID,CONTENTTYPE_ID,ATTRIBUTETYPE_CODE,INHERITABLE,REQUIRED,LABEL,CARDINALITY_CODE,LOOKUP_CODE)
VALUES ('HEAT_RATING','Product.HEAT_RATING','Product','I','F','F','Heat Rating','One','Product.HEAT_RATING');


--
-- Additional Product Images
--
INSERT INTO cms.relationshipdefinition (NAME,ID,CONTENTTYPE_ID,INHERITABLE,REQUIRED,NAVIGABLE,READONLY,LABEL,CARDINALITY_CODE)
VALUES ('PROD_IMAGE_JUMBO','Product.PROD_IMAGE_JUMBO','Product','F','F','F','F','Prod Image Jumbo','One');
INSERT INTO cms.relationshipdefinition (NAME,ID,CONTENTTYPE_ID,INHERITABLE,REQUIRED,NAVIGABLE,READONLY,LABEL,CARDINALITY_CODE)
VALUES ('PROD_IMAGE_ITEM','Product.PROD_IMAGE_ITEM','Product','F','F','F','F','Prod Image Item','One');
INSERT INTO cms.relationshipdefinition (NAME,ID,CONTENTTYPE_ID,INHERITABLE,REQUIRED,NAVIGABLE,READONLY,LABEL,CARDINALITY_CODE)
VALUES ('PROD_IMAGE_EXTRA','Product.PROD_IMAGE_EXTRA','Product','F','F','F','F','Prod Image Extra','One');

INSERT INTO cms.relationshipdestination (ID, RELATIONSHIPDEFINITION_ID,CONTENTTYPE_ID,REVERSE_ATTRIBUTE_NAME,REVERSE_ATTRIBUTE_LABEL)
VALUES ('Product.PROD_IMAGE_JUMBO.Image','Product.PROD_IMAGE_JUMBO','Image',NULL,NULL);
INSERT INTO cms.relationshipdestination (ID, RELATIONSHIPDEFINITION_ID,CONTENTTYPE_ID,REVERSE_ATTRIBUTE_NAME,REVERSE_ATTRIBUTE_LABEL)
VALUES ('Product.PROD_IMAGE_ITEM.Image','Product.PROD_IMAGE_ITEM','Image',NULL,NULL);
INSERT INTO cms.relationshipdestination (ID, RELATIONSHIPDEFINITION_ID,CONTENTTYPE_ID,REVERSE_ATTRIBUTE_NAME,REVERSE_ATTRIBUTE_LABEL)
VALUES ('Product.PROD_IMAGE_EXTRA.Image','Product.PROD_IMAGE_EXTRA','Image',NULL,NULL);

INSERT INTO cms.relationshipdefinition (NAME,ID,CONTENTTYPE_ID,INHERITABLE,REQUIRED,NAVIGABLE,READONLY,LABEL,CARDINALITY_CODE)
VALUES ('PROD_IMAGE_JUMBO','ConfiguredProduct.PROD_IMAGE_JUMBO','ConfiguredProduct','F','F','F','F','Prod Image Jumbo','One');
INSERT INTO cms.relationshipdefinition (NAME,ID,CONTENTTYPE_ID,INHERITABLE,REQUIRED,NAVIGABLE,READONLY,LABEL,CARDINALITY_CODE)
VALUES ('PROD_IMAGE_ITEM','ConfiguredProduct.PROD_IMAGE_ITEM','ConfiguredProduct','F','F','F','F','Prod Image Item','One');
INSERT INTO cms.relationshipdefinition (NAME,ID,CONTENTTYPE_ID,INHERITABLE,REQUIRED,NAVIGABLE,READONLY,LABEL,CARDINALITY_CODE)
VALUES ('PROD_IMAGE_EXTRA','ConfiguredProduct.PROD_IMAGE_EXTRA','ConfiguredProduct','F','F','F','F','Prod Image Extra','One');

INSERT INTO cms.relationshipdestination (ID, RELATIONSHIPDEFINITION_ID,CONTENTTYPE_ID,REVERSE_ATTRIBUTE_NAME,REVERSE_ATTRIBUTE_LABEL)
VALUES ('ConfiguredProduct.PROD_IMAGE_JUMBO.Image','ConfiguredProduct.PROD_IMAGE_JUMBO','Image',NULL,NULL);
INSERT INTO cms.relationshipdestination (ID, RELATIONSHIPDEFINITION_ID,CONTENTTYPE_ID,REVERSE_ATTRIBUTE_NAME,REVERSE_ATTRIBUTE_LABEL)
VALUES ('ConfiguredProduct.PROD_IMAGE_ITEM.Image','ConfiguredProduct.PROD_IMAGE_ITEM','Image',NULL,NULL);
INSERT INTO cms.relationshipdestination (ID, RELATIONSHIPDEFINITION_ID,CONTENTTYPE_ID,REVERSE_ATTRIBUTE_NAME,REVERSE_ATTRIBUTE_LABEL)
VALUES ('ConfiguredProduct.PROD_IMAGE_EXTRA.Image','ConfiguredProduct.PROD_IMAGE_EXTRA','Image',NULL,NULL);


