delete CMS.relationship where parent_contentnode_id like 'Category:%' and def_name = 'popularCategories';
delete from cms.relationshipdestination where ID like 'Category.popularCategories%';
delete from cms.relationshipdefinition where ID = 'Category.popularCategories';

delete CMS.attribute where contentnode_id like 'Category:%' and def_name = 'showPopularCategories';
delete CMS.attribute where contentnode_id like 'Department:%' and def_name = 'showPopularCategories';

delete from cms.attributedefinition where ID='Category.showPopularCategories';
delete from cms.attributedefinition where ID='Department.showPopularCategories';