DELETE FROM CMS.ATTRIBUTE WHERE CONTENTNODE_ID = 'SortOption:Sort_FullFavorites';
DELETE FROM CMS.CONTENTNODE WHERE ID = 'SortOption:Sort_FullFavorites' AND CONTENTTYPE_ID = 'SortOption';
