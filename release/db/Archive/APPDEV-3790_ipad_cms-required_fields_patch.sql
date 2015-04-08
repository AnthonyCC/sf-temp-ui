-- Brand required fields patch
UPDATE cms.attributedefinition    SET REQUIRED='F' WHERE ID='Brand.tabletCopyright';
UPDATE cms.relationshipdefinition SET REQUIRED='F' WHERE ID='Brand.tabletHeader';
UPDATE cms.relationshipdefinition SET REQUIRED='F' WHERE ID='Brand.tabletAboutTextShort';
UPDATE cms.relationshipdefinition SET REQUIRED='F' WHERE ID='Brand.tabletAboutTextLong';
