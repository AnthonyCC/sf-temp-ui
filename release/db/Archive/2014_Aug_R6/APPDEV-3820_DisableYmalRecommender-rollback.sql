delete CMS.attribute where contentnode_id like 'Category:%' and def_name = 'disableCategoryYmalRecommender';
delete CMS.attribute where contentnode_id like 'Department:%' and def_name = 'disableCategoryYmalRecommender';

delete from cms.attributedefinition where ID='Category.disableCategoryYmalRecommender';
delete from cms.attributedefinition where ID='Department.disableCategoryYmalRecommender';