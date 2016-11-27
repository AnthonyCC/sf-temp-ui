--Attributes of existing content types (relationships are already deleted by now)
delete CMS.attribute where contentnode_id like 'Department:%' and def_name = 'expand2ndLowestNavigationBox';
delete CMS.attribute where contentnode_id like 'Category:%' and def_name = 'expand2ndLowestNavigationBox';

--Category
delete from cms.attributedefinition where ID='Category.expand2ndLowestNavigationBox';

--Department
delete from cms.attributedefinition where ID='Department.expand2ndLowestNavigationBox';