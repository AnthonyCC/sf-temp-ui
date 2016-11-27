INSERT INTO cms.contenttype (ID,NAME,DESCRIPTION,GENERATE_ID) VALUES ('SearchRelevancyList','SearchRelevancyList','Definition of type SearchRelevancyList','T');
INSERT INTO cms.contenttype (ID,NAME,DESCRIPTION,GENERATE_ID) VALUES ('SearchRelevancyHint','SearchRelevancyHint','Definition of type SearchRelevancyHint','T');

INSERT INTO cms.attributedefinition (NAME,ID,CONTENTTYPE_ID,ATTRIBUTETYPE_CODE,INHERITABLE,REQUIRED,LABEL,CARDINALITY_CODE,LOOKUP_CODE) VALUES ('Keywords','SearchRelevancyList.Keywords','SearchRelevancyList','S','F','T','','One',NULL);
INSERT INTO cms.attributedefinition (NAME,ID,CONTENTTYPE_ID,ATTRIBUTETYPE_CODE,INHERITABLE,REQUIRED,LABEL,CARDINALITY_CODE,LOOKUP_CODE) VALUES ('score','SearchRelevancyHint.score','SearchRelevancyHint','I','F','F','','One',NULL);


INSERT INTO cms.relationshipdefinition (NAME,ID,CONTENTTYPE_ID,INHERITABLE,REQUIRED,NAVIGABLE,LABEL,CARDINALITY_CODE) VALUES ('categoryHints','SearchRelevancyList.categoryHints','SearchRelevancyList','F','F','T','Categories','Many');
INSERT INTO cms.relationshipdefinition (NAME,ID,CONTENTTYPE_ID,INHERITABLE,REQUIRED,NAVIGABLE,LABEL,CARDINALITY_CODE) VALUES ('category','SearchRelevancyHint.category','SearchRelevancyHint','F','T','F','','One');


INSERT INTO cms.relationshipdestination (RELATIONSHIPDEFINITION_ID,CONTENTTYPE_ID,LABEL,ID) VALUES ('SearchRelevancyList.categoryHints','SearchRelevancyHint',NULL,'SearchRelevancyList.categoryHints.SearchRelevancyHint');
INSERT INTO cms.relationshipdestination (RELATIONSHIPDEFINITION_ID,CONTENTTYPE_ID,LABEL,ID) VALUES ('SearchRelevancyHint.category','Category',NULL,'SearchRelevancyHint.category.Category');

INSERT INTO cms.relationshipdestination (RELATIONSHIPDEFINITION_ID,CONTENTTYPE_ID,LABEL,ID) VALUES ('FDFolder.children','SearchRelevancyList',NULL,'FDFolder.children.SearchRelevancyList');

INSERT INTO cms.CONTENTNODE (ID,CONTENTTYPE_ID) values ('FDFolder:searchRelevancyList','FDFolder');

INSERT INTO cms.ATTRIBUTE (CONTENTNODE_ID,ID,VALUE,ORDINAL,DEF_NAME,DEF_CONTENTTYPE) values ('FDFolder:searchRelevancyList',cms.system_seq.nextVal,'Search Relevancy Score List',0,'name','FDFolder');


