--Attributes of existing content types
delete CMS.attribute where contentnode_id like 'Department:%' and def_name = 'regularCategoriesNavHeader';
delete CMS.attribute where contentnode_id like 'Department:%' and def_name = 'preferenceCategoriesNavHeader';
delete CMS.attribute where contentnode_id like 'Department:%' and def_name = 'regularCategoriesLeftNavBoxHeader';
delete CMS.attribute where contentnode_id like 'Department:%' and def_name = 'preferenceCategoriesLeftNavBoxHeader';

--Department
delete from cms.attributedefinition where ID='Department.regularCategoriesNavHeader';
delete from cms.attributedefinition where ID='Department.preferenceCategoriesNavHeader';
delete from cms.attributedefinition where ID='Department.regularCategoriesLeftNavBoxHeader';
delete from cms.attributedefinition where ID='Department.preferenceCategoriesLeftNavBoxHeader';
