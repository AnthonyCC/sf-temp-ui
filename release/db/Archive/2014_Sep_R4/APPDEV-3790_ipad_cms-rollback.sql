--------------------------------------------------------------------------------
--Content Nodes
--------------------------------------------------------------------------------

--Banner relationships, attributes
delete CMS.relationship where CHILD_CONTENTNODE_ID like 'Banner:%';
delete CMS.relationship where PARENT_CONTENTNODE_ID like 'Banner:%';
delete CMS.attribute where CONTENTNODE_ID like 'Banner:%';

--SearchSuggestionGroup relationships, attributes
delete CMS.relationship where CHILD_CONTENTNODE_ID like 'SearchSuggestionGroup:%';
delete CMS.relationship where PARENT_CONTENTNODE_ID like 'SearchSuggestionGroup:%';
delete CMS.attribute where CONTENTNODE_ID like 'SearchSuggestionGroup:%';

--Content Nodes
delete CMS.contentnode where id like 'Banner:%';
delete CMS.contentnode where id like 'SearchSuggestionGroup:%';

--Attributes of existing content types (relationships are already deleted by now)
delete CMS.attribute where contentnode_id like 'Brand:%' and def_name = 'tabletCopyright';

--Relationships between existing content types
delete CMS.relationship where parent_contentnode_id like 'Store:%' and def_name = 'tabletFeaturedCategories';
delete CMS.relationship where parent_contentnode_id like 'Department:%' and def_name = 'tabletIcon';
delete CMS.relationship where parent_contentnode_id like 'Product:%' and def_name = 'completeTheMeal';
delete CMS.relationship where parent_contentnode_id like 'Brand:%' and def_name = 'tabletHeader';
delete CMS.relationship where parent_contentnode_id like 'Brand:%' and def_name = 'tabletImages';
delete CMS.relationship where parent_contentnode_id like 'Brand:%' and def_name = 'tabletAboutTextShort';
delete CMS.relationship where parent_contentnode_id like 'Brand:%' and def_name = 'tabletAboutTextLong';


--------------------------------------------------------------------------------
--Definitions
--------------------------------------------------------------------------------

--FDFolder
delete from cms.relationshipdestination where ID='FDFolder.children.Banner';
delete from cms.relationshipdestination where ID='FDFolder.children.SearchSuggestionGroup';

--Store
--tabletFeaturedCategories
delete from cms.relationshipdestination where ID like 'Store.tabletFeaturedCategories%';
delete from cms.relationshipdefinition where ID = 'Store.tabletFeaturedCategories';
--tabletSearchSuggestionGroups
delete from cms.relationshipdestination where ID like 'Store.tabletSearchSuggestionGroups%';
delete from cms.relationshipdefinition where ID = 'Store.tabletSearchSuggestionGroups';

--Department
--tabletCallToActionBanner
delete from cms.relationshipdestination where ID like 'Department.tabletCallToActionBanner%';
delete from cms.relationshipdefinition where ID = 'Department.tabletCallToActionBanner';
--tabletNoPurchaseSuggestions
delete from cms.relationshipdestination where ID like 'Department.tabletNoPurchaseSuggestions%';
delete from cms.relationshipdefinition where ID = 'Department.tabletNoPurchaseSuggestions';
-- tabletIcon
delete from cms.relationshipdestination where ID like 'Department.tabletIcon%';
delete from cms.relationshipdefinition where ID = 'Department.tabletIcon';

--Category
delete from cms.relationshipdestination where ID like 'Category.tabletCallToActionBanner%';
delete from cms.relationshipdefinition where ID = 'Category.tabletCallToActionBanner';

--Product
delete from cms.relationshipdestination where ID like 'Product.completeTheMeal%';
delete from cms.relationshipdefinition where ID = 'Product.completeTheMeal';

--Brand
delete from cms.attributedefinition where ID = 'Brand.tabletCopyright';
--tabletHeader
delete from cms.relationshipdestination where ID like 'Brand.tabletHeader%';
delete from cms.relationshipdefinition where ID = 'Brand.tabletHeader';
--tabletImages
delete from cms.relationshipdestination where ID like 'Brand.tabletImages%';
delete from cms.relationshipdefinition where ID = 'Brand.tabletImages';
--tabletAboutTextShort
delete from cms.relationshipdestination where ID like 'Brand.tabletAboutTextShort%';
delete from cms.relationshipdefinition where ID = 'Brand.tabletAboutTextShort';
--tabletAboutTextLong
delete from cms.relationshipdestination where ID like 'Brand.tabletAboutTextLong%';
delete from cms.relationshipdefinition where ID = 'Brand.tabletAboutTextLong';

--Banner
delete from cms.attributedefinition where ID like 'Banner%';
delete from cms.relationshipdestination where ID like 'Banner%';
delete from cms.relationshipdefinition where ID like 'Banner%';
delete from cms.contenttype where ID='Banner';

-- Product.browseRecommenderType lookup                                                                                                        
delete from cms.lookup where lookuptype_code = 'Banner.location';
delete from cms.lookuptype where code = 'Banner.location';

--SearchSuggestionGroup
delete from cms.attributedefinition where ID like 'SearchSuggestionGroup%';
delete from cms.relationshipdestination where ID like 'SearchSuggestionGroup%';
delete from cms.relationshipdefinition where ID like 'SearchSuggestionGroup%';
delete from cms.contenttype where ID='SearchSuggestionGroup';