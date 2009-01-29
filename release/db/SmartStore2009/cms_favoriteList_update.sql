
INSERT INTO relationshipdefinition (NAME,ID,CONTENTTYPE_ID,INHERITABLE,REQUIRED,NAVIGABLE,LABEL,CARDINALITY_CODE) VALUES ('favoriteItems','FavoriteList.favoriteItems','FavoriteList','F','F','F','','Many');

INSERT INTO relationshipdestination (RELATIONSHIPDEFINITION_ID,CONTENTTYPE_ID,LABEL,ID) VALUES ('FavoriteList.favoriteItems','Product',NULL,'FavoriteList.favoriteItems.Product');
INSERT INTO relationshipdestination (RELATIONSHIPDEFINITION_ID,CONTENTTYPE_ID,LABEL,ID) VALUES ('FavoriteList.favoriteItems','ConfiguredProduct',NULL,'FavoriteList.favoriteItems.ConfiguredProduct');

UPDATE relationship SET def_name = 'favoriteItems' WHERE parent_contentnode_id LIKE 'FavoriteList:%' AND def_name = 'items';

DELETE FROM relationshipdestination WHERE relationshipdefinition_id = 'FavoriteList.items';
DELETE FROM relationshipdefinition WHERE relationshipdefinition.id = 'FavoriteList.items';


