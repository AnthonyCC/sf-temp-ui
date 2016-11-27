-- RecipeTag
INSERT INTO cms.contenttype (ID,NAME,DESCRIPTION,GENERATE_ID) VALUES ('RecipeTag','RecipeTag','Definition of type RecipeTag','F');
INSERT INTO cms.attributedefinition (NAME,ID,CONTENTTYPE_ID,ATTRIBUTETYPE_CODE,INHERITABLE,REQUIRED,LABEL,CARDINALITY_CODE,LOOKUP_CODE) VALUES ('tagId','RecipeTag.tagId','RecipeTag','S','F','T','Tag ID','One',NULL);
INSERT INTO cms.relationshipdefinition (NAME,ID,CONTENTTYPE_ID,INHERITABLE,REQUIRED,NAVIGABLE,READONLY,LABEL,CARDINALITY_CODE) VALUES ('tabletImage','RecipeTag.tabletImage','RecipeTag','F','F','F','F','Tablet Image','One');
INSERT INTO cms.relationshipdestination (ID, RELATIONSHIPDEFINITION_ID,CONTENTTYPE_ID,REVERSE_ATTRIBUTE_NAME,REVERSE_ATTRIBUTE_LABEL) VALUES ('RecipeTag.tabletImage.Image','RecipeTag.tabletImage','Image',NULL,NULL);

-- FDFolder
INSERT INTO cms.relationshipdestination (ID, RELATIONSHIPDEFINITION_ID,CONTENTTYPE_ID,REVERSE_ATTRIBUTE_NAME,REVERSE_ATTRIBUTE_LABEL) VALUES ('FDFolder.children.RecipeTag','FDFolder.children','RecipeTag',NULL,NULL);

-- Store
-- tabletIdeasBanner
INSERT INTO cms.relationshipdefinition (NAME,ID,CONTENTTYPE_ID,INHERITABLE,REQUIRED,NAVIGABLE,READONLY,LABEL,CARDINALITY_CODE) VALUES ('tabletIdeasBanner','Store.tabletIdeasBanner','Store','F','F','F','F','Tablet Ideas Banner','One');
INSERT INTO cms.relationshipdestination (ID, RELATIONSHIPDEFINITION_ID,CONTENTTYPE_ID,REVERSE_ATTRIBUTE_NAME,REVERSE_ATTRIBUTE_LABEL) VALUES ('Store.tabletIdeasBanner.Banner','Store.tabletIdeasBanner','Banner',NULL,NULL);
-- tabletIdeasFeaturedPicksLists
INSERT INTO cms.relationshipdefinition (NAME,ID,CONTENTTYPE_ID,INHERITABLE,REQUIRED,NAVIGABLE,READONLY,LABEL,CARDINALITY_CODE) VALUES ('tabletIdeasFeaturedPicksLists','Store.tabletIdeasFeaturedPicksLists','Store','F','F','F','F','Tablet Ideas Featured Picks Lists','Many');
INSERT INTO cms.relationshipdestination (ID, RELATIONSHIPDEFINITION_ID,CONTENTTYPE_ID,REVERSE_ATTRIBUTE_NAME,REVERSE_ATTRIBUTE_LABEL) VALUES ('Store.tabletIdeasFeaturedPicksLists.Category','Store.tabletIdeasFeaturedPicksLists','Category',NULL,NULL);
-- tabletIdeasRecipeTags
INSERT INTO cms.relationshipdefinition (NAME,ID,CONTENTTYPE_ID,INHERITABLE,REQUIRED,NAVIGABLE,READONLY,LABEL,CARDINALITY_CODE) VALUES ('tabletIdeasRecipeTags','Store.tabletIdeasRecipeTags','Store','F','F','F','F','Tablet Ideas Recipe Tags','Many');
INSERT INTO cms.relationshipdestination (ID, RELATIONSHIPDEFINITION_ID,CONTENTTYPE_ID,REVERSE_ATTRIBUTE_NAME,REVERSE_ATTRIBUTE_LABEL) VALUES ('Store.tabletIdeasRecipeTags.RecipeTag','Store.tabletIdeasRecipeTags','RecipeTag',NULL,NULL);
