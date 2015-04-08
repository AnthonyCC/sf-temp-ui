delete CMS.attribute where contentnode_id like 'SuperDepartment:%' and def_name = 'hideGlobalNavDropDown';
delete CMS.attribute where contentnode_id like 'Department:%' and def_name = 'hideGlobalNavDropDown';

delete from cms.attributedefinition where ID='SuperDepartment.hideGlobalNavDropDown';
delete from cms.attributedefinition where ID='Department.hideGlobalNavDropDown';
