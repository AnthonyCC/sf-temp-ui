INSERT INTO cms.contenttype (ID,NAME,DESCRIPTION,GENERATE_ID) VALUES ('Recommender','Recommender','Definition of type Recommender','F');
INSERT INTO cms.contenttype (ID,NAME,DESCRIPTION,GENERATE_ID) VALUES ('RecommenderStrategy','RecommenderStrategy','Definition of type RecommenderStrategy','F');


INSERT INTO cms.lookuptype (CODE,NAME,DESCRIPTION) VALUES ('RecommenderStrategy.sampling','RecommenderStrategy.sampling',NULL);


INSERT INTO cms.attributedefinition (NAME,ID,CONTENTTYPE_ID,ATTRIBUTETYPE_CODE,INHERITABLE,REQUIRED,LABEL,CARDINALITY_CODE,LOOKUP_CODE) VALUES ('generator','RecommenderStrategy.generator','RecommenderStrategy','S','F','T','Generator expression','One',NULL);
INSERT INTO cms.attributedefinition (NAME,ID,CONTENTTYPE_ID,ATTRIBUTETYPE_CODE,INHERITABLE,REQUIRED,LABEL,CARDINALITY_CODE,LOOKUP_CODE) VALUES ('scoring','RecommenderStrategy.scoring','RecommenderStrategy','S','F','F','Scoring (ranking) expression','One',NULL);
INSERT INTO cms.attributedefinition (NAME,ID,CONTENTTYPE_ID,ATTRIBUTETYPE_CODE,INHERITABLE,REQUIRED,LABEL,CARDINALITY_CODE,LOOKUP_CODE) VALUES ('top_n','RecommenderStrategy.top_n','RecommenderStrategy','I','F','F','Top N','One',NULL);
INSERT INTO cms.attributedefinition (NAME,ID,CONTENTTYPE_ID,ATTRIBUTETYPE_CODE,INHERITABLE,REQUIRED,LABEL,CARDINALITY_CODE,LOOKUP_CODE) VALUES ('top_percent','RecommenderStrategy.top_percent','RecommenderStrategy','D','F','F','Top percent','One',NULL);
INSERT INTO cms.attributedefinition (NAME,ID,CONTENTTYPE_ID,ATTRIBUTETYPE_CODE,INHERITABLE,REQUIRED,LABEL,CARDINALITY_CODE,LOOKUP_CODE) VALUES ('exponent','RecommenderStrategy.exponent','RecommenderStrategy','D','F','F','Exponent (for power sampling)','One',NULL);
INSERT INTO cms.attributedefinition (NAME,ID,CONTENTTYPE_ID,ATTRIBUTETYPE_CODE,INHERITABLE,REQUIRED,LABEL,CARDINALITY_CODE,LOOKUP_CODE) VALUES ('FULL_NAME','Recommender.FULL_NAME','Recommender','S','F','F','Description','One',NULL);
INSERT INTO cms.attributedefinition (NAME,ID,CONTENTTYPE_ID,ATTRIBUTETYPE_CODE,INHERITABLE,REQUIRED,LABEL,CARDINALITY_CODE,LOOKUP_CODE) VALUES  ('FULL_NAME','RecommenderStrategy.FULL_NAME','RecommenderStrategy','S','F','F','Description','One',NULL);
INSERT INTO cms.attributedefinition (NAME,ID,CONTENTTYPE_ID,ATTRIBUTETYPE_CODE,INHERITABLE,REQUIRED,LABEL,CARDINALITY_CODE,LOOKUP_CODE) VALUES ('sampling','RecommenderStrategy.sampling','RecommenderStrategy','S','F','F','Sampling strategy','One','RecommenderStrategy.sampling');


INSERT INTO cms.lookup (LOOKUPTYPE_CODE,CODE,LABEL,DESCRIPTION,ORDINAL) VALUES ('RecommenderStrategy.sampling','deterministic','Deterministic','Deterministic',1);
INSERT INTO cms.lookup (LOOKUPTYPE_CODE,CODE,LABEL,DESCRIPTION,ORDINAL) VALUES ('RecommenderStrategy.sampling','uniform','Uniform','Uniform',2);
INSERT INTO cms.lookup (LOOKUPTYPE_CODE,CODE,LABEL,DESCRIPTION,ORDINAL) VALUES ('RecommenderStrategy.sampling','linear','Linear','Linear',3);
INSERT INTO cms.lookup (LOOKUPTYPE_CODE,CODE,LABEL,DESCRIPTION,ORDINAL) VALUES ('RecommenderStrategy.sampling','quadratic','Quadratic','Quadratic',4);
INSERT INTO cms.lookup (LOOKUPTYPE_CODE,CODE,LABEL,DESCRIPTION,ORDINAL) VALUES ('RecommenderStrategy.sampling','cubic','Cubic','Cubic',5);
INSERT INTO cms.lookup (LOOKUPTYPE_CODE,CODE,LABEL,DESCRIPTION,ORDINAL) VALUES ('RecommenderStrategy.sampling','harmonic','Harmonic','Harmonic',6);
INSERT INTO cms.lookup (LOOKUPTYPE_CODE,CODE,LABEL,DESCRIPTION,ORDINAL) VALUES ('RecommenderStrategy.sampling','sqrt','Square root','Square root',7);
INSERT INTO cms.lookup (LOOKUPTYPE_CODE,CODE,LABEL,DESCRIPTION,ORDINAL) VALUES ('RecommenderStrategy.sampling','power','Power CDF','Power CDF',8);
INSERT INTO cms.lookup (LOOKUPTYPE_CODE,CODE,LABEL,DESCRIPTION,ORDINAL) VALUES ('RecommenderStrategy.sampling','complicated','Complicated','Complicated',9);




INSERT INTO cms.relationshipdefinition (NAME,ID,CONTENTTYPE_ID,INHERITABLE,REQUIRED,NAVIGABLE,LABEL,CARDINALITY_CODE) VALUES ('strategy','Recommender.strategy','Recommender','F','T','F','Recommender Strategy','One');
INSERT INTO cms.relationshipdefinition (NAME,ID,CONTENTTYPE_ID,INHERITABLE,REQUIRED,NAVIGABLE,LABEL,CARDINALITY_CODE) VALUES ('scope','Recommender.scope','Recommender','F','F','F','Scope','Many');


INSERT INTO cms.relationshipdestination (RELATIONSHIPDEFINITION_ID,CONTENTTYPE_ID,LABEL,ID) VALUES ('Recommender.strategy','RecommenderStrategy',NULL,'Recommender.strategy.RecommenderStrategy');
INSERT INTO cms.relationshipdestination (RELATIONSHIPDEFINITION_ID,CONTENTTYPE_ID,LABEL,ID) VALUES ('Recommender.scope','Product',NULL,'Recommender.scope.Product');
INSERT INTO cms.relationshipdestination (RELATIONSHIPDEFINITION_ID,CONTENTTYPE_ID,LABEL,ID) VALUES ('Recommender.scope','ConfiguredProduct',NULL,'Recommender.scope.ConfiguredProduct');
INSERT INTO cms.relationshipdestination (RELATIONSHIPDEFINITION_ID,CONTENTTYPE_ID,LABEL,ID) VALUES ('Recommender.scope','ConfiguredProductGroup',NULL,'Recommender.scope.ConfiguredProductGroup');
INSERT INTO cms.relationshipdestination (RELATIONSHIPDEFINITION_ID,CONTENTTYPE_ID,LABEL,ID) VALUES ('Recommender.scope','Category',NULL,'Recommender.scope.Category');
INSERT INTO cms.relationshipdestination (RELATIONSHIPDEFINITION_ID,CONTENTTYPE_ID,LABEL,ID) VALUES ('Recommender.scope','Department',NULL,'Recommender.scope.Department');
INSERT INTO cms.relationshipdestination (RELATIONSHIPDEFINITION_ID,CONTENTTYPE_ID,LABEL,ID) VALUES ('Recommender.scope','FavoriteList',NULL,'Recommender.scope.FavoriteList');


INSERT INTO cms.relationshipdestination (RELATIONSHIPDEFINITION_ID,CONTENTTYPE_ID,LABEL,ID) VALUES ('FDFolder.children','Recommender',NULL,'FDFolder.children.Recommender');
INSERT INTO cms.relationshipdestination (RELATIONSHIPDEFINITION_ID,CONTENTTYPE_ID,LABEL,ID) VALUES ('FDFolder.children','RecommenderStrategy',NULL,'FDFolder.children.RecommenderStrategy');


INSERT INTO cms.relationshipdefinition (NAME,ID,CONTENTTYPE_ID,INHERITABLE,REQUIRED,NAVIGABLE,LABEL,CARDINALITY_CODE) VALUES ('recommenders','YmalSet.recommenders','YmalSet','F','F','F','Recommenders','Many');
INSERT INTO cms.relationshipdestination (RELATIONSHIPDEFINITION_ID,CONTENTTYPE_ID,LABEL,ID) VALUES ('YmalSet.recommenders','Recommender',NULL,'YmalSet.recommenders.Recommender');

