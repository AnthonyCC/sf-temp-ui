--Attributes of existing content types (relationships are already deleted by now)
delete CMS.attribute where contentnode_id like 'Product:%' and def_name = 'disableAtpFailureRecommendation';
delete CMS.attribute where contentnode_id like 'Category:%' and def_name = 'disableAtpFailureRecommendation';
delete CMS.attribute where contentnode_id like 'Department:%' and def_name = 'disableAtpFailureRecommendation';

--Department
delete from cms.attributedefinition where ID='Department.disableAtpFailureRecommendation';

--Category
delete from cms.attributedefinition where ID='Category.disableAtpFailureRecommendation';

--Product
delete from cms.attributedefinition where ID='Product.disableAtpFailureRecommendation';
