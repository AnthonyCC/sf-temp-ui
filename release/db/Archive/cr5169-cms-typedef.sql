
---------- RecipeDepartment.featuredSource
INSERT INTO cms.relationshipdefinition (NAME,ID,CONTENTTYPE_ID,INHERITABLE,REQUIRED,NAVIGABLE,LABEL,CARDINALITY_CODE) VALUES ('featuredSource','RecipeDepartment.featuredSource','RecipeDepartment','F','F','F','','Many');
INSERT INTO cms.relationshipdestination (RELATIONSHIPDEFINITION_ID,CONTENTTYPE_ID,LABEL,ID) VALUES ('RecipeDepartment.featuredSource','RecipeSource',NULL,'RecipeDepartment.featuredSource.RecipeSource');

