

INSERT INTO cms.contenttype (ID,NAME,DESCRIPTION,GENERATE_ID) VALUES ('WordStemmingException','WordStemmingException','Definition of type WordStemmingException','F');
INSERT INTO cms.attributedefinition (NAME,ID,CONTENTTYPE_ID,ATTRIBUTETYPE_CODE,INHERITABLE,REQUIRED,LABEL,CARDINALITY_CODE,LOOKUP_CODE) VALUES ('word','WordStemmingException.word','WordStemmingException','S','F','T','Bad Singular Form','One',NULL);
INSERT INTO cms.relationshipdestination (RELATIONSHIPDEFINITION_ID,CONTENTTYPE_ID,LABEL,ID) VALUES ('FDFolder.children','WordStemmingException',NULL,'FDFolder.children.WordStemmingException');

