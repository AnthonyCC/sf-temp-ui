delete CMS.attribute where contentnode_id like 'Department:%' and def_name = 'maxItemsPerColumn';

delete from cms.attributedefinition where ID='Department.maxItemsPerColumn';