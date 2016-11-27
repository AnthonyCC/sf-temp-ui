

INSERT INTO cms.attributedefinition (NAME,ID,CONTENTTYPE_ID,ATTRIBUTETYPE_CODE,INHERITABLE,REQUIRED,LABEL,CARDINALITY_CODE,LOOKUP_CODE) VALUES ('bidirectional','Synonym.bidirectional','Synonym','B','F','F','Bidirectional?','One',NULL);
UPDATE cms.attributedefinition SET LABEL = 'Word(s)' WHERE ID = 'Synonym.word';
UPDATE cms.attributedefinition SET LABEL = 'Equivalent Substitute(s)' WHERE ID = 'Synonym.synonymValue';


