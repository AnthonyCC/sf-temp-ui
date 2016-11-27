--------------------------------------------------------------------------------
--Content Nodes
--------------------------------------------------------------------------------

--Relationships between existing content types
delete CMS.relationship where parent_contentnode_id like 'Store:%' and def_name = 'tabletHomeScreenPopUpShopBanners';
delete CMS.relationship where parent_contentnode_id like 'Store:%' and def_name = 'tabletIdeasBrands';
delete CMS.relationship where parent_contentnode_id like 'Store:%' and def_name = 'tabletIdeasRecipes';
delete CMS.relationship where parent_contentnode_id like 'Department:%' and def_name = 'tabletHeaderBanner';
delete CMS.relationship where parent_contentnode_id like 'Category:%' and def_name = 'tabletThumbnailImage';
delete CMS.relationship where parent_contentnode_id like 'Brand:%' and def_name = 'tabletThumbnailImage';
delete CMS.relationship where parent_contentnode_id like 'Recipe:%' and def_name = 'tabletThumbnailImage';

--------------------------------------------------------------------------------
--Definitions
--------------------------------------------------------------------------------

--Store
--tabletHomeScreenPopUpShopBanners
delete from cms.relationshipdestination where ID like 'Store.tabletHomeScreenPopUpShopBanners%';
delete from cms.relationshipdefinition where ID = 'Store.tabletHomeScreenPopUpShopBanners';
--tabletIdeasBrands
delete from cms.relationshipdestination where ID like 'Store.tabletIdeasBrands%';
delete from cms.relationshipdefinition where ID = 'Store.tabletIdeasBrands';
--tabletIdeasRecipes
delete from cms.relationshipdestination where ID like 'Store.tabletIdeasRecipes%';
delete from cms.relationshipdefinition where ID = 'Store.tabletIdeasRecipes';
-- tabletIdeasRecipeTags
UPDATE cms.relationshipdefinition SET LABEL='Tablet Ideas Recipe Tags' WHERE ID='Store.tabletIdeasRecipeTags';

--Department
--tabletHeaderBanner
delete from cms.relationshipdestination where ID like 'Department.tabletHeaderBanner%';
delete from cms.relationshipdefinition where ID = 'Department.tabletHeaderBanner';

--Category
--tabletThumbnailImage
delete from cms.relationshipdestination where ID like 'Category.tabletThumbnailImage%';
delete from cms.relationshipdefinition where ID = 'Category.tabletThumbnailImage';

--Brand
--tabletThumbnailImage
delete from cms.relationshipdestination where ID like 'Brand.tabletThumbnailImage%';
delete from cms.relationshipdefinition where ID = 'Brand.tabletThumbnailImage';

--Recipe
--tabletThumbnailImage
delete from cms.relationshipdestination where ID like 'Recipe.tabletThumbnailImage%';
delete from cms.relationshipdefinition where ID = 'Recipe.tabletThumbnailImage';