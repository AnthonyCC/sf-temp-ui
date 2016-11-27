

INSERT INTO cms.contenttype (ID,NAME,DESCRIPTION,GENERATE_ID) VALUES ('Synonym','Synonym','Definition of type Synonym','T');
INSERT INTO cms.attributedefinition (NAME,ID,CONTENTTYPE_ID,ATTRIBUTETYPE_CODE,INHERITABLE,REQUIRED,LABEL,CARDINALITY_CODE,LOOKUP_CODE) VALUES ('word','Synonym.word','Synonym','S','F','T','','One',NULL);
INSERT INTO cms.attributedefinition (NAME,ID,CONTENTTYPE_ID,ATTRIBUTETYPE_CODE,INHERITABLE,REQUIRED,LABEL,CARDINALITY_CODE,LOOKUP_CODE) VALUES ('synonymValue','Synonym.synonymValue','Synonym','S','F','T','Synonym To','One',NULL);
INSERT INTO cms.relationshipdestination (RELATIONSHIPDEFINITION_ID,CONTENTTYPE_ID,LABEL,ID) VALUES ('FDFolder.children','Synonym',NULL,'FDFolder.children.Synonym');
