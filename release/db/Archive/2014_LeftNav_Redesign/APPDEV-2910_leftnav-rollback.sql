--Attributes of existing content types (relationships are already deleted by now)
delete CMS.attribute where contentnode_id like 'Department:%' and def_name = 'showAllByDefault';
delete CMS.attribute where contentnode_id like 'Category:%' and def_name = 'showAllByDefault';

--Category
delete from cms.attributedefinition where ID='Category.showAllByDefault';

--Department
delete from cms.attributedefinition where ID='Department.showAllByDefault';

-- ProductFilter
delete cms.lookup WHERE LOOKUPTYPE_CODE='ProductFilter.type' AND CODE='ORGANIC';
UPDATE cms.lookup set ORDINAL=14 WHERE LOOKUPTYPE_CODE='ProductFilter.type' AND CODE='PRICE';                 --revert from ordinal 15
UPDATE cms.lookup set ORDINAL=15 WHERE LOOKUPTYPE_CODE='ProductFilter.type' AND CODE='SUSTAINABILITY_RATING'; --revert from ordinal 16
UPDATE cms.lookup set ORDINAL=16 WHERE LOOKUPTYPE_CODE='ProductFilter.type' AND CODE='TAG';                   --revert from ordinal 17