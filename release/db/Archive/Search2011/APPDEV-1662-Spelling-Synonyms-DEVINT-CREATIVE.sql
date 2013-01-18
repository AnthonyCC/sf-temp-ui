

INSERT INTO cms.contenttype (ID,NAME,DESCRIPTION,GENERATE_ID) VALUES ('SpellingSynonym','SpellingSynonym','Definition of type Spelling Synonym','T');
INSERT INTO cms.attributedefinition (NAME,ID,CONTENTTYPE_ID,ATTRIBUTETYPE_CODE,INHERITABLE,REQUIRED,LABEL,CARDINALITY_CODE,LOOKUP_CODE) VALUES ('word','SpellingSynonym.word','SpellingSynonym','S','F','T','','One',NULL);
INSERT INTO cms.attributedefinition (NAME,ID,CONTENTTYPE_ID,ATTRIBUTETYPE_CODE,INHERITABLE,REQUIRED,LABEL,CARDINALITY_CODE,LOOKUP_CODE) VALUES ('synonymValue','SpellingSynonym.synonymValue','SpellingSynonym','S','F','T','Synonym To','One',NULL);
INSERT INTO cms.relationshipdestination (RELATIONSHIPDEFINITION_ID,CONTENTTYPE_ID,ID) VALUES ('FDFolder.children','SpellingSynonym','FDFolder.children.SpellingSynonym');

