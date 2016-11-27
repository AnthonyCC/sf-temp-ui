-- Store
-- tabletHomeScreenPopUpShopBanners
INSERT INTO cms.relationshipdefinition (NAME,ID,CONTENTTYPE_ID,INHERITABLE,REQUIRED,NAVIGABLE,READONLY,LABEL,CARDINALITY_CODE) VALUES ('tabletHomeScreenPopUpShopBanners','Store.tabletHomeScreenPopUpShopBanners','Store','F','F','F','F','Tablet Home Screen Pop-up Shop Banners','Many');
INSERT INTO cms.relationshipdestination (ID, RELATIONSHIPDEFINITION_ID,CONTENTTYPE_ID,REVERSE_ATTRIBUTE_NAME,REVERSE_ATTRIBUTE_LABEL) VALUES ('Store.tabletHomeScreenPopUpShopBanners.Banner','Store.tabletHomeScreenPopUpShopBanners','Banner',NULL,NULL);
-- tabletIdeasBrands
INSERT INTO cms.relationshipdefinition (NAME,ID,CONTENTTYPE_ID,INHERITABLE,REQUIRED,NAVIGABLE,READONLY,LABEL,CARDINALITY_CODE) VALUES ('tabletIdeasBrands','Store.tabletIdeasBrands','Store','F','F','F','F','Tablet Ideas Brands','Many');
INSERT INTO cms.relationshipdestination (ID, RELATIONSHIPDEFINITION_ID,CONTENTTYPE_ID,REVERSE_ATTRIBUTE_NAME,REVERSE_ATTRIBUTE_LABEL) VALUES ('Store.tabletIdeasBrands.Brand','Store.tabletIdeasBrands','Brand',NULL,NULL);
-- tabletIdeasRecipes
INSERT INTO cms.relationshipdefinition (NAME,ID,CONTENTTYPE_ID,INHERITABLE,REQUIRED,NAVIGABLE,READONLY,LABEL,CARDINALITY_CODE) VALUES ('tabletIdeasRecipes','Store.tabletIdeasRecipes','Store','F','F','F','F','Tablet Ideas Recipes','Many');
INSERT INTO cms.relationshipdestination (ID, RELATIONSHIPDEFINITION_ID,CONTENTTYPE_ID,REVERSE_ATTRIBUTE_NAME,REVERSE_ATTRIBUTE_LABEL) VALUES ('Store.tabletIdeasRecipes.Recipe','Store.tabletIdeasRecipes','Recipe',NULL,NULL);
-- tabletIdeasRecipeTags
UPDATE cms.relationshipdefinition SET LABEL='Tablet Ideas Recipe Tags (Theme Page)' WHERE ID='Store.tabletIdeasRecipeTags';

-- Department
-- tabletHeaderBanner
INSERT INTO cms.relationshipdefinition (NAME,ID,CONTENTTYPE_ID,INHERITABLE,REQUIRED,NAVIGABLE,READONLY,LABEL,CARDINALITY_CODE) VALUES ('tabletHeaderBanner','Department.tabletHeaderBanner','Department','F','F','F','F','Tablet Header Banner','One');
INSERT INTO cms.relationshipdestination (ID, RELATIONSHIPDEFINITION_ID,CONTENTTYPE_ID,REVERSE_ATTRIBUTE_NAME,REVERSE_ATTRIBUTE_LABEL) VALUES ('Department.tabletHeaderBanner.Banner','Department.tabletHeaderBanner','Banner',NULL,NULL);

-- Category
-- tabletThumbnailImage
INSERT INTO cms.relationshipdefinition (NAME,ID,CONTENTTYPE_ID,INHERITABLE,REQUIRED,NAVIGABLE,READONLY,LABEL,CARDINALITY_CODE) VALUES ('tabletThumbnailImage','Category.tabletThumbnailImage','Category','F','F','F','F','Tablet Thumbnail Image','One');
INSERT INTO cms.relationshipdestination (ID, RELATIONSHIPDEFINITION_ID,CONTENTTYPE_ID,REVERSE_ATTRIBUTE_NAME,REVERSE_ATTRIBUTE_LABEL) VALUES ('Category.tabletThumbnailImage.Image','Category.tabletThumbnailImage','Image',NULL,NULL);

-- Brand
-- tabletThumbnailImage
INSERT INTO cms.relationshipdefinition (NAME,ID,CONTENTTYPE_ID,INHERITABLE,REQUIRED,NAVIGABLE,READONLY,LABEL,CARDINALITY_CODE) VALUES ('tabletThumbnailImage','Brand.tabletThumbnailImage','Brand','F','F','F','F','Tablet Thumbnail Image','One');
INSERT INTO cms.relationshipdestination (ID, RELATIONSHIPDEFINITION_ID,CONTENTTYPE_ID,REVERSE_ATTRIBUTE_NAME,REVERSE_ATTRIBUTE_LABEL) VALUES ('Brand.tabletThumbnailImage.Image','Brand.tabletThumbnailImage','Image',NULL,NULL);

-- Recipe
-- tabletThumbnailImage
INSERT INTO cms.relationshipdefinition (NAME,ID,CONTENTTYPE_ID,INHERITABLE,REQUIRED,NAVIGABLE,READONLY,LABEL,CARDINALITY_CODE) VALUES ('tabletThumbnailImage','Recipe.tabletThumbnailImage','Recipe','F','F','F','F','Tablet Thumbnail Image','One');
INSERT INTO cms.relationshipdestination (ID, RELATIONSHIPDEFINITION_ID,CONTENTTYPE_ID,REVERSE_ATTRIBUTE_NAME,REVERSE_ATTRIBUTE_LABEL) VALUES ('Recipe.tabletThumbnailImage.Image','Recipe.tabletThumbnailImage','Image',NULL,NULL);
