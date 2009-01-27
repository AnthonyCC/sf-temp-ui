// Add CANDIDATE_LIST to Category

INSERT INTO cms.relationshipdefinition (NAME,ID,CONTENTTYPE_ID,INHERITABLE,REQUIRED,NAVIGABLE,LABEL,CARDINALITY_CODE) VALUES ('CANDIDATE_LIST','Category.CANDIDATE_LIST','Category','F','F','F','','Many');

INSERT INTO cms.relationshipdestination (RELATIONSHIPDEFINITION_ID,CONTENTTYPE_ID,LABEL,ID) VALUES ('Category.CANDIDATE_LIST','Category',NULL,'Category.CANDIDATE_LIST.Category');
INSERT INTO cms.relationshipdestination (RELATIONSHIPDEFINITION_ID,CONTENTTYPE_ID,LABEL,ID) VALUES ('Category.CANDIDATE_LIST','Product',NULL,'Category.CANDIDATE_LIST.Product');


// Add MANUAL_SELECTION_SLOTS to Category

INSERT INTO cms.attributedefinition (NAME,ID,CONTENTTYPE_ID,ATTRIBUTETYPE_CODE,INHERITABLE,REQUIRED,LABEL,CARDINALITY_CODE,LOOKUP_CODE) VALUES ('MANUAL_SELECTION_SLOTS', 'Category.MANUAL_SELECTION_SLOTS', 'Category','I','F','F','Manual Selection Slots','One',NULL);

// Add FavoriteList

INSERT INTO cms.contenttype (ID,NAME,DESCRIPTION,GENERATE_ID) VALUES ('FavoriteList','FavoriteList','Definition of type FavoriteList','F');

INSERT INTO cms.attributedefinition (NAME,ID,CONTENTTYPE_ID,ATTRIBUTETYPE_CODE,INHERITABLE,REQUIRED,LABEL,CARDINALITY_CODE,LOOKUP_CODE) VALUES ('full_name','FavoriteList.full_name','FavoriteList','S','F','F','','One',NULL);
INSERT INTO cms.relationshipdefinition (NAME,ID,CONTENTTYPE_ID,INHERITABLE,REQUIRED,NAVIGABLE,LABEL,CARDINALITY_CODE) VALUES ('items','FavoriteList.items','FavoriteList','F','F','F','','Many');

INSERT INTO cms.relationshipdestination (RELATIONSHIPDEFINITION_ID,CONTENTTYPE_ID,LABEL,ID) VALUES ('FavoriteList.items','Product',NULL,'FavoriteList.items.Product');
INSERT INTO cms.relationshipdestination (RELATIONSHIPDEFINITION_ID,CONTENTTYPE_ID,LABEL,ID) VALUES ('FavoriteList.items','ConfiguredProduct',NULL,'FavoriteList.items.ConfiguredProduct');

INSERT INTO cms.relationshipdestination (RELATIONSHIPDEFINITION_ID,CONTENTTYPE_ID,LABEL,ID) VALUES ('FDFolder.children','FavoriteList',NULL,'FDFolder.children.FavoriteList');
