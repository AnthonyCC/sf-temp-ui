--------------------------------------------------------------------------------
--Content Nodes
--------------------------------------------------------------------------------

--RecipeTag relationships, attributes
delete CMS.relationship where CHILD_CONTENTNODE_ID like 'RecipeTag:%';
delete CMS.relationship where PARENT_CONTENTNODE_ID like 'RecipeTag:%';
delete CMS.attribute where CONTENTNODE_ID like 'RecipeTag:%';

--Content Nodes
delete CMS.contentnode where id like 'RecipeTag:%';

--Relationships between existing content types
delete CMS.relationship where parent_contentnode_id like 'Store:%' and def_name = 'tabletIdeasBanner';
delete CMS.relationship where parent_contentnode_id like 'Store:%' and def_name = 'tabletIdeasFeaturedPicksLists';

--------------------------------------------------------------------------------
--Definitions
--------------------------------------------------------------------------------
--FDFolder
delete from cms.relationshipdestination where ID='FDFolder.children.RecipeTag';

--Store
--tabletIdeasBanner
delete from cms.relationshipdestination where ID like 'Store.tabletIdeasBanner%';
delete from cms.relationshipdefinition where ID = 'Store.tabletIdeasBanner';
--tabletIdeasFeaturedPicksLists
delete from cms.relationshipdestination where ID like 'Store.tabletIdeasFeaturedPicksLists%';
delete from cms.relationshipdefinition where ID = 'Store.tabletIdeasFeaturedPicksLists';
--tabletIdeasRecipeTags
delete from cms.relationshipdestination where ID like 'Store.tabletIdeasRecipeTags%';
delete from cms.relationshipdefinition where ID = 'Store.tabletIdeasRecipeTags';

--RecipeTag
delete from cms.attributedefinition where ID like 'RecipeTag%';
delete from cms.relationshipdestination where ID like 'RecipeTag%';
delete from cms.relationshipdefinition where ID like 'RecipeTag%';
delete from cms.contenttype where ID='RecipeTag';