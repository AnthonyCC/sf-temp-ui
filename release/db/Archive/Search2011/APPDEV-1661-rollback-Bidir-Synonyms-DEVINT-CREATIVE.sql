

DELETE FROM cms.attributedefinition WHERE ID = 'Synonym.bidirectional';
UPDATE cms.attributedefinition SET LABEL = 'Word' WHERE ID = 'Synonym.word';
UPDATE cms.attributedefinition SET LABEL = 'Synonym To' WHERE ID = 'Synonym.synonymValue';


