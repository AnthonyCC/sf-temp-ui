

DELETE FROM cms.relationshipdestination WHERE ID = 'FDFolder.children.SpellingSynonym';
DELETE FROM cms.attributedefinition WHERE ID = 'SpellingSynonym.synonymValue';
DELETE FROM cms.attributedefinition WHERE ID = 'SpellingSynonym.word';
DELETE FROM cms.contenttype WHERE ID = 'SpellingSynonym';

