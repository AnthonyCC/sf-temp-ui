--------------------------------------------------------------------------------
--Content Nodes
--------------------------------------------------------------------------------
--Relationships between existing content types
delete CMS.relationship where parent_contentnode_id like 'Store:%' and def_name = 'searchPageSortOptions';
delete CMS.relationship where parent_contentnode_id like 'Store:%' and def_name = 'newProductsPageSortOptions';
delete CMS.relationship where parent_contentnode_id like 'Store:%' and def_name = 'presidentsPicksPageSortOptions';
delete CMS.relationship where parent_contentnode_id like 'Store:%' and def_name = 'eCouponsPageSortOptions';


--------------------------------------------------------------------------------
--Definitions
--------------------------------------------------------------------------------

--Store
--searchPageSortOptions
delete from cms.relationshipdestination where ID like 'Store.searchPageSortOptions%';
delete from cms.relationshipdefinition where ID = 'Store.searchPageSortOptions';
--newProductsPageSortOptions
delete from cms.relationshipdestination where ID like 'Store.newProductsPageSortOptions%';
delete from cms.relationshipdefinition where ID = 'Store.newProductsPageSortOptions';
--presidentsPicksPageSortOptions
delete from cms.relationshipdestination where ID like 'Store.presidentsPicksPageSortOptions%';
delete from cms.relationshipdefinition where ID = 'Store.presidentsPicksPageSortOptions';
--eCouponsPageSortOptions
delete from cms.relationshipdestination where ID like 'Store.eCouponsPageSortOptions%';
delete from cms.relationshipdefinition where ID = 'Store.eCouponsPageSortOptions';

-- SortOption lookup                                                                                                        
delete from cms.lookup where lookuptype_code like 'SortOption%' and code = 'DEPARTMENT';
delete from cms.lookup where lookuptype_code like 'SortOption%' and code = 'E_COUPON_DOLLAR_DISCOUNT';
delete from cms.lookup where lookuptype_code like 'SortOption%' and code = 'E_COUPON_EXPIRATION_DATE';
delete from cms.lookup where lookuptype_code like 'SortOption%' and code = 'E_COUPON_PERCENT_DISCOUNT';
delete from cms.lookup where lookuptype_code like 'SortOption%' and code = 'E_COUPON_POPULARITY';
delete from cms.lookup where lookuptype_code like 'SortOption%' and code = 'E_COUPON_START_DATE';
delete from cms.lookup where lookuptype_code like 'SortOption%' and code = 'RECENCY';
delete from cms.lookup where lookuptype_code like 'SortOption%' and code = 'SEARCH_RELEVANCY';
