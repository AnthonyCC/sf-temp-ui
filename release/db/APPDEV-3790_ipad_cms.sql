-- Banner.location lookup
INSERT INTO cms.lookuptype (CODE,NAME,DESCRIPTION) values ('Banner.location','Banner.location',null);
INSERT INTO cms.lookup (LOOKUPTYPE_CODE,CODE,LABEL,DESCRIPTION,ORDINAL) values ('Banner.location','CHECKOUT','Checkout','Checkout',1);
INSERT INTO cms.lookup (LOOKUPTYPE_CODE,CODE,LABEL,DESCRIPTION,ORDINAL) values ('Banner.location','HOMEPAGE','Homepage','Homepage',2);

-- Banner                                                                                                         
INSERT INTO cms.contenttype (ID,NAME,DESCRIPTION,GENERATE_ID) VALUES ('Banner','Banner','Definition of type Banner','F');
INSERT INTO cms.attributedefinition (NAME,ID,CONTENTTYPE_ID,ATTRIBUTETYPE_CODE,INHERITABLE,REQUIRED,LABEL,CARDINALITY_CODE,LOOKUP_CODE) VALUES ('location','Banner.location','Banner','S','F','F','Location','One','Banner.location');
INSERT INTO cms.relationshipdefinition (NAME,ID,CONTENTTYPE_ID,INHERITABLE,REQUIRED,NAVIGABLE,READONLY,LABEL,CARDINALITY_CODE) VALUES ('image','Banner.image','Banner','F','F','F','F','Image','One');
INSERT INTO cms.relationshipdestination (ID, RELATIONSHIPDEFINITION_ID,CONTENTTYPE_ID,REVERSE_ATTRIBUTE_NAME,REVERSE_ATTRIBUTE_LABEL) VALUES ('Banner.image.Image','Banner.image','Image',NULL,NULL);
INSERT INTO cms.relationshipdefinition (NAME,ID,CONTENTTYPE_ID,INHERITABLE,REQUIRED,NAVIGABLE,READONLY,LABEL,CARDINALITY_CODE) VALUES ('link','Banner.link','Banner','F','F','F','F','Link','One');
INSERT INTO cms.relationshipdestination (ID, RELATIONSHIPDEFINITION_ID,CONTENTTYPE_ID,REVERSE_ATTRIBUTE_NAME,REVERSE_ATTRIBUTE_LABEL) VALUES ('Banner.link.SuperDepartment','Banner.link','SuperDepartment',NULL,NULL);
INSERT INTO cms.relationshipdestination (ID, RELATIONSHIPDEFINITION_ID,CONTENTTYPE_ID,REVERSE_ATTRIBUTE_NAME,REVERSE_ATTRIBUTE_LABEL) VALUES ('Banner.link.Department','Banner.link','Department',NULL,NULL);
INSERT INTO cms.relationshipdestination (ID, RELATIONSHIPDEFINITION_ID,CONTENTTYPE_ID,REVERSE_ATTRIBUTE_NAME,REVERSE_ATTRIBUTE_LABEL) VALUES ('Banner.link.Category','Banner.link','Category',NULL,NULL);
INSERT INTO cms.relationshipdestination (ID, RELATIONSHIPDEFINITION_ID,CONTENTTYPE_ID,REVERSE_ATTRIBUTE_NAME,REVERSE_ATTRIBUTE_LABEL) VALUES ('Banner.link.Product','Banner.link','Product',NULL,NULL);
INSERT INTO cms.relationshipdestination (ID, RELATIONSHIPDEFINITION_ID,CONTENTTYPE_ID,REVERSE_ATTRIBUTE_NAME,REVERSE_ATTRIBUTE_LABEL) VALUES ('Banner.link.Recipe','Banner.link','Recipe',NULL,NULL);
INSERT INTO cms.relationshipdestination (ID, RELATIONSHIPDEFINITION_ID,CONTENTTYPE_ID,REVERSE_ATTRIBUTE_NAME,REVERSE_ATTRIBUTE_LABEL) VALUES ('Banner.link.Brand','Banner.link','Brand',NULL,NULL);
INSERT INTO cms.relationshipdestination (ID, RELATIONSHIPDEFINITION_ID,CONTENTTYPE_ID,REVERSE_ATTRIBUTE_NAME,REVERSE_ATTRIBUTE_LABEL) VALUES ('Banner.link.Html','Banner.link','Html',NULL,NULL);

-- SearchSuggestionGroup
INSERT INTO cms.contenttype (ID,NAME,DESCRIPTION,GENERATE_ID) VALUES ('SearchSuggestionGroup','SearchSuggestionGroup','Definition of type SearchSuggestionGroup','F');
INSERT INTO cms.attributedefinition (NAME,ID,CONTENTTYPE_ID,ATTRIBUTETYPE_CODE,INHERITABLE,REQUIRED,LABEL,CARDINALITY_CODE,LOOKUP_CODE) VALUES ('searchTerms','SearchSuggestionGroup.searchTerms','SearchSuggestionGroup','S','F','T','Search Terms (separated by semicolons)','One',NULL);
INSERT INTO cms.relationshipdefinition (NAME,ID,CONTENTTYPE_ID,INHERITABLE,REQUIRED,NAVIGABLE,READONLY,LABEL,CARDINALITY_CODE) VALUES ('tabletImage','SearchSuggestionGroup.tabletImage','SearchSuggestionGroup','F','F','F','F','Tablet Image','One');
INSERT INTO cms.relationshipdestination (ID, RELATIONSHIPDEFINITION_ID,CONTENTTYPE_ID,REVERSE_ATTRIBUTE_NAME,REVERSE_ATTRIBUTE_LABEL) VALUES ('SearchSuggestionGroup.tabletImage.Image','SearchSuggestionGroup.tabletImage','Image',NULL,NULL);


-- FDFolder
INSERT INTO cms.relationshipdestination (ID, RELATIONSHIPDEFINITION_ID,CONTENTTYPE_ID,REVERSE_ATTRIBUTE_NAME,REVERSE_ATTRIBUTE_LABEL) VALUES ('FDFolder.children.Banner','FDFolder.children','Banner',NULL,NULL);
INSERT INTO cms.relationshipdestination (ID, RELATIONSHIPDEFINITION_ID,CONTENTTYPE_ID,REVERSE_ATTRIBUTE_NAME,REVERSE_ATTRIBUTE_LABEL) VALUES ('FDFolder.children.SearchSuggestionGroup','FDFolder.children','SearchSuggestionGroup',NULL,NULL);

-- Store
-- tabletFeaturedCategories
INSERT INTO cms.relationshipdefinition (NAME,ID,CONTENTTYPE_ID,INHERITABLE,REQUIRED,NAVIGABLE,READONLY,LABEL,CARDINALITY_CODE) VALUES ('tabletFeaturedCategories','Store.tabletFeaturedCategories','Store','F','F','F','F','Tablet Featured Categories','Many');
INSERT INTO cms.relationshipdestination (ID, RELATIONSHIPDEFINITION_ID,CONTENTTYPE_ID,REVERSE_ATTRIBUTE_NAME,REVERSE_ATTRIBUTE_LABEL) VALUES ('Store.tabletFeaturedCategories.Category','Store.tabletFeaturedCategories','Category',NULL,NULL);
-- tabletSearchSuggestionGroups
INSERT INTO cms.relationshipdefinition (NAME,ID,CONTENTTYPE_ID,INHERITABLE,REQUIRED,NAVIGABLE,READONLY,LABEL,CARDINALITY_CODE) VALUES ('tabletSearchSuggestionGroups','Store.tabletSearchSuggestionGroups','Store','F','F','F','F','Tablet Search Suggestion Groups','Many');
INSERT INTO cms.relationshipdestination (ID, RELATIONSHIPDEFINITION_ID,CONTENTTYPE_ID,REVERSE_ATTRIBUTE_NAME,REVERSE_ATTRIBUTE_LABEL) VALUES ('Store.tabletSearchSuggestionGroups.SearchSuggestionGroup','Store.tabletSearchSuggestionGroups','SearchSuggestionGroup',NULL,NULL);

-- Department
-- tabletCallToActionBanner
INSERT INTO cms.relationshipdefinition (NAME,ID,CONTENTTYPE_ID,INHERITABLE,REQUIRED,NAVIGABLE,READONLY,LABEL,CARDINALITY_CODE) VALUES ('tabletCallToActionBanner','Department.tabletCallToActionBanner','Department','T','F','F','F','Call To Action Banner','One');
INSERT INTO cms.relationshipdestination (ID, RELATIONSHIPDEFINITION_ID,CONTENTTYPE_ID,REVERSE_ATTRIBUTE_NAME,REVERSE_ATTRIBUTE_LABEL) VALUES ('Department.tabletCallToActionBanner.Banner','Department.tabletCallToActionBanner','Banner',NULL,NULL);
-- tabletNoPurchaseSuggestions
INSERT INTO cms.relationshipdefinition (NAME,ID,CONTENTTYPE_ID,INHERITABLE,REQUIRED,NAVIGABLE,READONLY,LABEL,CARDINALITY_CODE) VALUES ('tabletNoPurchaseSuggestions','Department.tabletNoPurchaseSuggestions','Department','F','F','F','F','No Purchase Suggestions','Many');
INSERT INTO cms.relationshipdestination (ID, RELATIONSHIPDEFINITION_ID,CONTENTTYPE_ID,REVERSE_ATTRIBUTE_NAME,REVERSE_ATTRIBUTE_LABEL) VALUES ('Department.tabletNoPurchaseSuggestions.Banner','Department.tabletNoPurchaseSuggestions','Banner',NULL,NULL);
-- tabletIcon
INSERT INTO cms.relationshipdefinition (NAME,ID,CONTENTTYPE_ID,INHERITABLE,REQUIRED,NAVIGABLE,READONLY,LABEL,CARDINALITY_CODE) VALUES ('tabletIcon','Department.tabletIcon','Department','F','F','F','F','Icon','One');
INSERT INTO cms.relationshipdestination (ID, RELATIONSHIPDEFINITION_ID,CONTENTTYPE_ID,REVERSE_ATTRIBUTE_NAME,REVERSE_ATTRIBUTE_LABEL) VALUES ('Department.tabletIcon.Image','Department.tabletIcon','Image',NULL,NULL);

-- Category
-- tabletCallToActionBanner
INSERT INTO cms.relationshipdefinition (NAME,ID,CONTENTTYPE_ID,INHERITABLE,REQUIRED,NAVIGABLE,READONLY,LABEL,CARDINALITY_CODE) VALUES ('tabletCallToActionBanner','Category.tabletCallToActionBanner','Category','T','F','F','F','Call To Action Banner','One');
INSERT INTO cms.relationshipdestination (ID, RELATIONSHIPDEFINITION_ID,CONTENTTYPE_ID,REVERSE_ATTRIBUTE_NAME,REVERSE_ATTRIBUTE_LABEL) VALUES ('Category.tabletCallToActionBanner.Banner','Category.tabletCallToActionBanner','Banner',NULL,NULL);

-- Product
-- completeTheMeal
INSERT INTO cms.relationshipdefinition (NAME,ID,CONTENTTYPE_ID,INHERITABLE,REQUIRED,NAVIGABLE,READONLY,LABEL,CARDINALITY_CODE) VALUES ('completeTheMeal','Product.completeTheMeal','Product','F','F','F','F','Complete The Meal','Many');
INSERT INTO cms.relationshipdestination (ID, RELATIONSHIPDEFINITION_ID,CONTENTTYPE_ID,REVERSE_ATTRIBUTE_NAME,REVERSE_ATTRIBUTE_LABEL) VALUES ('Product.completeTheMeal.Product','Product.completeTheMeal','Product',NULL,NULL);
INSERT INTO cms.relationshipdestination (ID, RELATIONSHIPDEFINITION_ID,CONTENTTYPE_ID,REVERSE_ATTRIBUTE_NAME,REVERSE_ATTRIBUTE_LABEL) VALUES ('Product.completeTheMeal.ConfiguredProduct','Product.completeTheMeal','ConfiguredProduct',NULL,NULL);


-- Brand
INSERT INTO cms.attributedefinition (NAME,ID,CONTENTTYPE_ID,ATTRIBUTETYPE_CODE,INHERITABLE,REQUIRED,LABEL,CARDINALITY_CODE,LOOKUP_CODE) VALUES ('tabletCopyright','Brand.tabletCopyright','Brand','S','F','T','Tablet Copyright','One',NULL);
-- tabletHeader
INSERT INTO cms.relationshipdefinition (NAME,ID,CONTENTTYPE_ID,INHERITABLE,REQUIRED,NAVIGABLE,READONLY,LABEL,CARDINALITY_CODE) VALUES ('tabletHeader','Brand.tabletHeader','Brand','F','T','F','F','Tablet Header','One');
INSERT INTO cms.relationshipdestination (ID, RELATIONSHIPDEFINITION_ID,CONTENTTYPE_ID,REVERSE_ATTRIBUTE_NAME,REVERSE_ATTRIBUTE_LABEL) VALUES ('Brand.tabletHeader.Image','Brand.tabletHeader','Image',NULL,NULL);
-- tabletImages
INSERT INTO cms.relationshipdefinition (NAME,ID,CONTENTTYPE_ID,INHERITABLE,REQUIRED,NAVIGABLE,READONLY,LABEL,CARDINALITY_CODE) VALUES ('tabletImages','Brand.tabletImages','Brand','F','F','F','F','Tablet Images','Many');
INSERT INTO cms.relationshipdestination (ID, RELATIONSHIPDEFINITION_ID,CONTENTTYPE_ID,REVERSE_ATTRIBUTE_NAME,REVERSE_ATTRIBUTE_LABEL) VALUES ('Brand.tabletImages.Image','Brand.tabletImages','Image',NULL,NULL);
-- tabletAboutTextShort
INSERT INTO cms.relationshipdefinition (NAME,ID,CONTENTTYPE_ID,INHERITABLE,REQUIRED,NAVIGABLE,READONLY,LABEL,CARDINALITY_CODE) VALUES ('tabletAboutTextShort','Brand.tabletAboutTextShort','Brand','F','T','F','F','Tablet About Text Short','One');
INSERT INTO cms.relationshipdestination (ID, RELATIONSHIPDEFINITION_ID,CONTENTTYPE_ID,REVERSE_ATTRIBUTE_NAME,REVERSE_ATTRIBUTE_LABEL) VALUES ('Brand.tabletAboutTextShort.Html','Brand.tabletAboutTextShort','Html',NULL,NULL);
-- tabletAboutTextLong
INSERT INTO cms.relationshipdefinition (NAME,ID,CONTENTTYPE_ID,INHERITABLE,REQUIRED,NAVIGABLE,READONLY,LABEL,CARDINALITY_CODE) VALUES ('tabletAboutTextLong','Brand.tabletAboutTextLong','Brand','F','T','F','F','Tablet About Text Long','One');
INSERT INTO cms.relationshipdestination (ID, RELATIONSHIPDEFINITION_ID,CONTENTTYPE_ID,REVERSE_ATTRIBUTE_NAME,REVERSE_ATTRIBUTE_LABEL) VALUES ('Brand.tabletAboutTextLong.Html','Brand.tabletAboutTextLong','Html',NULL,NULL);
