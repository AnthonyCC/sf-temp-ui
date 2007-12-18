
INSERT INTO cms.lookuptype (CODE,NAME,DESCRIPTION) VALUES ('RecipeSource.productionStatus','RecipeSource.productionStatus',NULL);
INSERT INTO cms.lookup (LOOKUPTYPE_CODE,CODE,LABEL,DESCRIPTION,ORDINAL) VALUES ('RecipeSource.productionStatus','PENDING','Pending Approval','Pending Approval',64);
INSERT INTO cms.lookup (LOOKUPTYPE_CODE,CODE,LABEL,DESCRIPTION,ORDINAL) VALUES ('RecipeSource.productionStatus','ACTIVE','Active','Active',65);
INSERT INTO cms.lookup (LOOKUPTYPE_CODE,CODE,LABEL,DESCRIPTION,ORDINAL) VALUES ('RecipeSource.productionStatus','LIMITED','Limited','Limited',66);
INSERT INTO cms.lookup (LOOKUPTYPE_CODE,CODE,LABEL,DESCRIPTION,ORDINAL) VALUES ('RecipeSource.productionStatus','COMPLETED','Completed','Completed',67);
INSERT INTO cms.attributedefinition (NAME,ID,CONTENTTYPE_ID,ATTRIBUTETYPE_CODE,INHERITABLE,REQUIRED,LABEL,CARDINALITY_CODE,LOOKUP_CODE) VALUES ('productionStatus','RecipeSource.productionStatus','RecipeSource','S','F','F','','One','RecipeSource.productionStatus');
