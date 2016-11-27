INSERT INTO cms.relationshipdefinition (NAME,ID,CONTENTTYPE_ID,INHERITABLE,REQUIRED,NAVIGABLE,LABEL,CARDINALITY_CODE) VALUES ('logo','Recipe.logo','Recipe','F','F','F','','One');
INSERT INTO cms.relationshipdestination (RELATIONSHIPDEFINITION_ID,CONTENTTYPE_ID,LABEL,ID) VALUES ('Recipe.logo','Image',NULL,'Recipe.logo.Image');
