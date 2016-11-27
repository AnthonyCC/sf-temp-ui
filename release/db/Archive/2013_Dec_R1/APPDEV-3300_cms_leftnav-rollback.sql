--------------------------------------------------------------------------------
--Content Nodes
--------------------------------------------------------------------------------

--ProductFilter relationships, attributes
delete CMS.relationship where CHILD_CONTENTNODE_ID like 'ProductFilter:%';
delete CMS.relationship where PARENT_CONTENTNODE_ID like 'ProductFilter:%';
delete CMS.attribute where CONTENTNODE_ID like 'ProductFilter:%';

--ProductGrabber relationships, attributes
delete CMS.relationship where CHILD_CONTENTNODE_ID like 'ProductGrabber:%';
delete CMS.relationship where PARENT_CONTENTNODE_ID like 'ProductGrabber:%';
delete CMS.attribute where CONTENTNODE_ID like 'ProductGrabber:%';

--ProductFilterGroup relationships, attributes
delete CMS.relationship where CHILD_CONTENTNODE_ID like 'ProductFilterGroup:%';
delete CMS.relationship where PARENT_CONTENTNODE_ID like 'ProductFilterGroup:%';
delete CMS.attribute where CONTENTNODE_ID like 'ProductFilterGroup:%';

--ProductFilterMultiGroup relationships, attributes
delete CMS.relationship where CHILD_CONTENTNODE_ID like 'ProductFilterMultiGroup:%';
delete CMS.relationship where PARENT_CONTENTNODE_ID like 'ProductFilterMultiGroup:%';
delete CMS.attribute where CONTENTNODE_ID like 'ProductFilterMultiGroup:%';


--Tag relationships, attributes
delete CMS.relationship where CHILD_CONTENTNODE_ID like 'Tag:%';
delete CMS.relationship where PARENT_CONTENTNODE_ID like 'Tag:%';
delete CMS.attribute where CONTENTNODE_ID like 'Tag:%';

--Tag relationships, attributes
delete CMS.relationship where CHILD_CONTENTNODE_ID like 'SortOption:%';
delete CMS.relationship where PARENT_CONTENTNODE_ID like 'SortOption:%';
delete CMS.attribute where CONTENTNODE_ID like 'SortOption:%';

--Content Nodes
delete CMS.contentnode where id like 'ProductFilterGroup:%';
delete CMS.contentnode where id like 'ProductFilterMultiGroup:%';
delete CMS.contentnode where id like 'ProductGrabber:%';
delete CMS.contentnode where id like 'ProductFilter:%';
delete CMS.contentnode where id like 'Tag:%';
delete CMS.contentnode where id like 'SortOption:%';

--Attributes of existing content types (relationships are already deleted by now)
delete CMS.attribute where contentnode_id like 'Department:%' and def_name = 'noGroupingByCategory';
delete CMS.attribute where contentnode_id like 'Department:%' and def_name = 'browseRecommenderType';
delete CMS.attribute where contentnode_id like 'Department:%' and def_name = 'featuredRecommenderTitle';
delete CMS.attribute where contentnode_id like 'Department:%' and def_name = 'featuredRecommenderRandomizeProducts';
delete CMS.attribute where contentnode_id like 'Department:%' and def_name = 'featuredRecommenderSiteFeature';
delete CMS.attribute where contentnode_id like 'Department:%' and def_name = 'merchantRecommenderTitle';
delete CMS.attribute where contentnode_id like 'Department:%' and def_name = 'merchantRecommenderRandomizeProducts';
delete CMS.attribute where contentnode_id like 'Category:%' and def_name = 'preferenceCategory';
delete CMS.attribute where contentnode_id like 'Category:%' and def_name = 'hideIfFilteringIsSupported';
delete CMS.attribute where contentnode_id like 'Category:%' and def_name = 'noGroupingByCategory';
delete CMS.attribute where contentnode_id like 'Category:%' and def_name = 'browseRecommenderType';
delete CMS.attribute where contentnode_id like 'Category:%' and def_name = 'catMerchantRecommenderTitle';
delete CMS.attribute where contentnode_id like 'Category:%' and def_name = 'catMerchantRecommenderRandomizeProducts';
delete CMS.attribute where contentnode_id like 'Product:%' and def_name = 'browseRecommenderType';

--Relationships between existing content types
delete CMS.relationship where parent_contentnode_id like 'Department:%' and def_name = 'departmentBanner';
delete CMS.relationship where parent_contentnode_id like 'Department:%' and def_name = 'categoryBanner';
delete CMS.relationship where parent_contentnode_id like 'Department:%' and def_name = 'popularCategories';
delete CMS.relationship where parent_contentnode_id like 'Department:%' and def_name = 'titleBar';
delete CMS.relationship where parent_contentnode_id like 'Department:%' and def_name = 'featuredRecommenderSourceCategory';
delete CMS.relationship where parent_contentnode_id like 'Department:%' and def_name = 'merchantRecommenderProducts';
delete CMS.relationship where parent_contentnode_id like 'Category:%' and def_name = 'categoryBanner';
delete CMS.relationship where parent_contentnode_id like 'Category:%' and def_name = 'description';
delete CMS.relationship where parent_contentnode_id like 'Category:%' and def_name = 'nameImage';
delete CMS.relationship where parent_contentnode_id like 'Category:%' and def_name = 'catMerchantRecommenderProducts';


--------------------------------------------------------------------------------
--Definitions
--------------------------------------------------------------------------------

--FDFolder
delete from cms.relationshipdestination where ID='FDFolder.children.ProductFilterMultiGroup';
delete from cms.relationshipdestination where ID='FDFolder.children.ProductFilterGroup';
delete from cms.relationshipdestination where ID='FDFolder.children.ProductFilter';
delete from cms.relationshipdestination where ID='FDFolder.children.Tag';
delete from cms.relationshipdestination where ID='FDFolder.children.SortOption';

--Product
delete from cms.attributedefinition where ID='Product.tags';
delete from cms.attributedefinition where ID='Product.browseRecommenderType';
delete from cms.relationshipdestination where ID like 'Product.tags%';
delete from cms.relationshipdefinition where ID = 'Product.tags';

--Category
delete from cms.attributedefinition where ID='Category.preferenceCategory';
delete from cms.attributedefinition where ID='Category.hideIfFilteringIsSupported';
delete from cms.attributedefinition where ID='Category.noGroupingByCategory';
delete from cms.attributedefinition where ID='Category.browseRecommenderType';
delete from cms.attributedefinition where ID='Category.catMerchantRecommenderTitle';
delete from cms.attributedefinition where ID='Category.catMerchantRecommenderRandomizeProducts';
delete from cms.relationshipdestination where ID like 'Category.productGrabbers%';
delete from cms.relationshipdefinition where ID = 'Category.productGrabbers';
delete from cms.relationshipdestination where ID like 'Category.productFilterGroups%';
delete from cms.relationshipdefinition where ID = 'Category.productFilterGroups';
delete from cms.relationshipdestination where ID like 'Category.sortOptions%';
delete from cms.relationshipdefinition where ID = 'Category.sortOptions';
delete from cms.relationshipdestination where ID like 'Category.productTags%';
delete from cms.relationshipdefinition where ID = 'Category.productTags';
delete from cms.relationshipdestination where ID like 'Category.categoryBanner%';
delete from cms.relationshipdefinition where ID = 'Category.categoryBanner';
delete from cms.relationshipdestination where ID like 'Category.description%';
delete from cms.relationshipdefinition where ID = 'Category.description';
delete from cms.relationshipdestination where ID like 'Category.nameImage%';
delete from cms.relationshipdefinition where ID = 'Category.nameImage';
delete from cms.relationshipdestination where ID like 'Category.catMerchantRecommenderProducts%';
delete from cms.relationshipdefinition where ID = 'Category.catMerchantRecommenderProducts';

--Department
delete from cms.attributedefinition where ID='Department.noGroupingByCategory';
delete from cms.attributedefinition where ID='Department.browseRecommenderType';
delete from cms.attributedefinition where ID='Department.featuredRecommenderTitle';
delete from cms.attributedefinition where ID='Department.featuredRecommenderRandomizeProducts';
delete from cms.attributedefinition where ID='Department.featuredRecommenderSiteFeature';
delete from cms.attributedefinition where ID='Department.merchantRecommenderTitle';
delete from cms.attributedefinition where ID='Department.merchantRecommenderRandomizeProducts';
delete from cms.relationshipdestination where ID like 'Department.productFilterGroups%';
delete from cms.relationshipdefinition where ID = 'Department.productFilterGroups';
delete from cms.relationshipdestination where ID like 'Department.sortOptions%';
delete from cms.relationshipdefinition where ID = 'Department.sortOptions';
delete from cms.relationshipdestination where ID like 'Department.popularCategories%';
delete from cms.relationshipdefinition where ID = 'Department.popularCategories';
delete from cms.relationshipdestination where ID like 'Department.titleBar%';
delete from cms.relationshipdefinition where ID = 'Department.titleBar';
delete from cms.relationshipdestination where ID like 'Department.productTags%';
delete from cms.relationshipdefinition where ID = 'Department.productTags';
delete from cms.relationshipdestination where ID like 'Department.categoryBanner%';
delete from cms.relationshipdefinition where ID = 'Department.categoryBanner';
delete from cms.relationshipdestination where ID like 'Department.departmentBanner%';
delete from cms.relationshipdefinition where ID = 'Department.departmentBanner';
delete from cms.relationshipdestination where ID like 'Department.featuredRecommenderSourceCategory%';
delete from cms.relationshipdefinition where ID = 'Department.featuredRecommenderSourceCategory';
delete from cms.relationshipdestination where ID like 'Department.merchantRecommenderProducts%';
delete from cms.relationshipdefinition where ID = 'Department.merchantRecommenderProducts';



--ProductGrabber
delete from cms.attributedefinition where ID like 'ProductGrabber%';
delete from cms.relationshipdestination where ID like 'ProductGrabber%';
delete from cms.relationshipdefinition where ID like 'ProductGrabber%';
delete from cms.contenttype where ID='ProductGrabber';

--ProductFilterGroup
delete from cms.attributedefinition where ID like 'ProductFilterGroup%';
delete from cms.relationshipdestination where ID like 'ProductFilterGroup%';
delete from cms.relationshipdefinition where ID like 'ProductFilterGroup%';
delete from cms.contenttype where ID='ProductFilterGroup';

--ProductFilterMultiGroup
delete from cms.attributedefinition where ID like 'ProductFilterMultiGroup%';
delete from cms.relationshipdestination where ID like 'ProductFilterMultiGroup%';
delete from cms.relationshipdefinition where ID like 'ProductFilterMultiGroup%';
delete from cms.contenttype where ID='ProductFilterMultiGroup';

--ProductFilterGroup lookup
delete from cms.lookup where lookuptype_code like 'ProductFilterGroup%';
delete from cms.lookuptype where code like 'ProductFilterGroup%';

--ProductFilterMultiGroup lookup
delete from cms.lookup where lookuptype_code like 'ProductFilterMultiGroup%';
delete from cms.lookuptype where code like 'ProductFilterMultiGroup%';

--ProductFilter
delete from cms.attributedefinition where ID like 'ProductFilter.%';
delete from cms.relationshipdestination where ID like 'ProductFilter.%';
delete from cms.relationshipdefinition where ID like 'ProductFilter.%';
delete from cms.contenttype where ID='ProductFilter';
delete from cms.lookup where lookuptype_code like 'ProductFilter.%';
delete from cms.lookuptype where code like 'ProductFilter.%';

--Tag
delete from cms.attributedefinition where ID like 'Tag%';
delete from cms.relationshipdestination where ID like 'Tag%';
delete from cms.relationshipdefinition where ID like 'Tag%';
delete from cms.contenttype where ID='Tag';

-- SortOption                                                                                                         
delete from cms.attributedefinition where ID like 'SortOption%';
delete from cms.relationshipdestination where ID like 'SortOption%';
delete from cms.relationshipdefinition where ID like 'SortOption%';
delete from cms.contenttype where ID='SortOption';

-- SortOption lookup                                                                                                        
delete from cms.lookup where lookuptype_code like 'SortOption%';
delete from cms.lookuptype where code like 'SortOption%';

-- Product.browseRecommenderType lookup                                                                                                        
delete from cms.lookup where lookuptype_code like 'Product.browseRecommenderType';
delete from cms.lookuptype where code like 'Product.browseRecommenderType';
