--1 row inserted
INSERT INTO cms.contenttype (ID,NAME,DESCRIPTION,GENERATE_ID) VALUES ('FAQ','FAQ','Definition of type FAQ','F');

--1 row inserted
INSERT INTO cms.attributedefinition (NAME,ID,CONTENTTYPE_ID,ATTRIBUTETYPE_CODE,INHERITABLE,REQUIRED,LABEL,CARDINALITY_CODE,LOOKUP_CODE) VALUES ('FULL_NAME','FAQ.FULL_NAME','FAQ','S','F','F','Description','One',NULL);

--1 row inserted
INSERT INTO cms.attributedefinition (NAME,ID,CONTENTTYPE_ID,ATTRIBUTETYPE_CODE,INHERITABLE,REQUIRED,LABEL,CARDINALITY_CODE,LOOKUP_CODE) VALUES ('QUESTION','FAQ.QUESTION','FAQ','S','F','F','Question','One',NULL);

--1 row inserted
INSERT INTO cms.attributedefinition (NAME,ID,CONTENTTYPE_ID,ATTRIBUTETYPE_CODE,INHERITABLE,REQUIRED,LABEL,CARDINALITY_CODE,LOOKUP_CODE) VALUES ('ANSWER','FAQ.ANSWER','FAQ','S','F','F','Answer','One',NULL);

--1 row inserted
INSERT INTO cms.attributedefinition (NAME,ID,CONTENTTYPE_ID,ATTRIBUTETYPE_CODE,INHERITABLE,REQUIRED,LABEL,CARDINALITY_CODE,LOOKUP_CODE) VALUES ('KEYWORDS','FAQ.KEYWORDS','FAQ','S','F','F','Keywords','One',NULL);

--1 row inserted
INSERT INTO cms.relationshipdestination (RELATIONSHIPDEFINITION_ID,CONTENTTYPE_ID,LABEL,ID) VALUES ('FDFolder.children','FAQ',NULL,'FDFolder.children.FAQ');

--1 row inserted
INSERT INTO cms.contentnode values('FDFolder:FAQ', 'FDFolder');

--1 row inserted
INSERT INTO cms.ATTRIBUTE (CONTENTNODE_ID,ID,VALUE,ORDINAL,DEF_NAME,DEF_CONTENTTYPE) values ('FDFolder:FAQ',cms.system_seq.nextVal,'FAQ',0,'name','FDFolder');



alter table cms.attribute modify (value varchar2(3999));

