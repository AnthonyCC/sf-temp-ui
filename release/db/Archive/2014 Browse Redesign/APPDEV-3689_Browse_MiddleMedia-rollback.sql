delete cms.attributedefinition where id = 'Category.bannerLocationCLP';
delete cms.attributedefinition where id = 'Category.bannerLocationPLP';
delete cms.attributedefinition where id = 'Department.bannerLocation';
delete cms.attributedefinition where id = 'SuperDepartment.bannerLocation';

delete from cms.relationshipdestination where ID='Category.middleMedia.Html';
delete from cms.relationshipdestination where ID='Department.middleMedia.Html';
delete from cms.relationshipdestination where ID='SuperDepartment.middleMedia.Html';

delete from cms.relationshipdefinition where ID = 'Category.middleMedia';
delete from cms.relationshipdefinition where ID = 'Department.middleMedia';
delete from cms.relationshipdefinition where ID='SuperDepartment.middleMedia';

delete from cms.lookup where lookuptype_code like 'Category.bannerLocation';
delete from cms.lookuptype where code like 'Category.bannerLocation';

