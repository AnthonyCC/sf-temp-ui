DELETE FROM cms.relationshipdestination WHERE ID = 'Department.tile_list.TileList';
DELETE FROM cms.relationshipdefinition WHERE ID = 'Department.tile_list';

DELETE FROM cms.relationshipdestination WHERE ID = 'Tile.quick_buy.ConfiguredProductGroup';
DELETE FROM cms.relationshipdestination WHERE ID = 'Tile.quick_buy.ConfiguredProduct';
DELETE FROM cms.relationshipdestination WHERE ID = 'Tile.quick_buy.Product';
DELETE FROM cms.relationshipdestination WHERE ID = 'Tile.quick_buy.Category';

DELETE FROM cms.relationshipdestination WHERE ID = 'Tile.media.Image';
DELETE FROM cms.relationshipdestination WHERE ID = 'Tile.media.Html';
DELETE FROM cms.relationshipdestination WHERE ID = 'TileList.tiles.Tile';
DELETE FROM cms.relationshipdestination WHERE ID = 'TileList.filter.DomainValue';
DELETE FROM cms.relationshipdestination WHERE ID = 'TileList.filter.Product';
DELETE FROM cms.relationshipdestination WHERE ID = 'TileList.filter.Category';
DELETE FROM cms.relationshipdestination WHERE ID = 'TileList.filter.Department';

DELETE FROM cms.relationshipdestination WHERE ID = 'FDFolder.children.TileList';
DELETE FROM cms.relationshipdestination WHERE ID = 'FDFolder.children.Tile';

DELETE FROM cms.relationshipdefinition WHERE ID = 'Tile.quick_buy';
DELETE FROM cms.relationshipdefinition WHERE ID = 'Tile.media';
DELETE FROM cms.relationshipdefinition WHERE ID = 'TileList.tiles';
DELETE FROM cms.relationshipdefinition WHERE ID = 'TileList.filter';

DELETE FROM cms.attributedefinition WHERE ID = 'Tile.GGF_TYPE';

DELETE FROM cms.contenttype WHERE ID = 'TileList';
DELETE FROM cms.contenttype WHERE ID = 'Tile';
