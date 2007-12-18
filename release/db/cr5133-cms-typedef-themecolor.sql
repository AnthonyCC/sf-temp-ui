INSERT INTO cms.lookuptype (CODE,NAME,DESCRIPTION) VALUES ('Recipe.theme_color','Recipe.theme_color',NULL);
INSERT INTO cms.lookup (LOOKUPTYPE_CODE,CODE,LABEL,DESCRIPTION,ORDINAL) VALUES ('Recipe.theme_color','DEFAULT','Default','Default',50);
INSERT INTO cms.lookup (LOOKUPTYPE_CODE,CODE,LABEL,DESCRIPTION,ORDINAL) VALUES ('Recipe.theme_color','6699CC','Real Simple','Real Simple',60);
INSERT INTO cms.attributedefinition (NAME,ID,CONTENTTYPE_ID,ATTRIBUTETYPE_CODE,INHERITABLE,REQUIRED,LABEL,CARDINALITY_CODE,LOOKUP_CODE) VALUES ('theme_color','Recipe.theme_color','Recipe','S','F','F','Theme','One','Recipe.theme_color');
