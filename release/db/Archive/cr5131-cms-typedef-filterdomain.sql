
INSERT INTO cms.relationshipdefinition (NAME,ID,CONTENTTYPE_ID,INHERITABLE,REQUIRED,NAVIGABLE,LABEL,CARDINALITY_CODE) VALUES ('filterByDomains','RecipeSearchPage.filterByDomains','RecipeSearchPage','F','F','F','','Many');
INSERT INTO cms.relationshipdestination (RELATIONSHIPDEFINITION_ID,CONTENTTYPE_ID,LABEL,ID) VALUES ('RecipeSearchPage.filterByDomains','Domain',NULL,'RecipeSearchPage.filterByDomains.Domain');

