-- Store
-- tabletIdeasRecipes
INSERT INTO cms.relationshipdestination (ID, RELATIONSHIPDEFINITION_ID,CONTENTTYPE_ID,REVERSE_ATTRIBUTE_NAME,REVERSE_ATTRIBUTE_LABEL) VALUES ('Store.tabletIdeasRecipes.RecipeTag','Store.tabletIdeasRecipes','RecipeTag',NULL,NULL);

-- Banner.location
INSERT INTO cms.lookup (LOOKUPTYPE_CODE,CODE,LABEL,DESCRIPTION,ORDINAL) values ('Banner.location','EMPTY','Empty','Empty',1);
UPDATE cms.lookup set ORDINAL=2 WHERE LOOKUPTYPE_CODE='Banner.location' AND CODE='CHECKOUT'; --orinially ordinal was 1
UPDATE cms.lookup set ORDINAL=3 WHERE LOOKUPTYPE_CODE='Banner.location' AND CODE='HOMEPAGE'; --orinially ordinal was 2