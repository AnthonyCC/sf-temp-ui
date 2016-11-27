-- GENERATRED by XmlToDbDataDef.xsl
INSERT INTO cms.contenttype (ID,NAME,DESCRIPTION,GENERATE_ID) VALUES ('StarterList','StarterList','Definition of type StarterList','F');
INSERT INTO cms.attributedefinition (NAME,ID,CONTENTTYPE_ID,ATTRIBUTETYPE_CODE,INHERITABLE,REQUIRED,LABEL,CARDINALITY_CODE,LOOKUP_CODE) VALUES ('FULL_NAME','StarterList.FULL_NAME','StarterList','S','F','F','Full Name','One',NULL);
INSERT INTO cms.attributedefinition (NAME,ID,CONTENTTYPE_ID,ATTRIBUTETYPE_CODE,INHERITABLE,REQUIRED,LABEL,CARDINALITY_CODE,LOOKUP_CODE) VALUES ('BLURB','StarterList.BLURB','StarterList','S','F','F','Blurb','One',NULL);
INSERT INTO cms.attributedefinition (NAME,ID,CONTENTTYPE_ID,ATTRIBUTETYPE_CODE,INHERITABLE,REQUIRED,LABEL,CARDINALITY_CODE,LOOKUP_CODE) VALUES ('startDate','StarterList.startDate','StarterList','DT','F','F','','One',NULL);
INSERT INTO cms.attributedefinition (NAME,ID,CONTENTTYPE_ID,ATTRIBUTETYPE_CODE,INHERITABLE,REQUIRED,LABEL,CARDINALITY_CODE,LOOKUP_CODE) VALUES ('endDate','StarterList.endDate','StarterList','DT','F','F','','One',NULL);
INSERT INTO cms.attributedefinition (NAME,ID,CONTENTTYPE_ID,ATTRIBUTETYPE_CODE,INHERITABLE,REQUIRED,LABEL,CARDINALITY_CODE,LOOKUP_CODE) VALUES ('notes','StarterList.notes','StarterList','S','F','F','','One',NULL);
INSERT INTO cms.lookuptype (CODE,NAME,DESCRIPTION) VALUES ('StarterList.productionStatus','StarterList.productionStatus',NULL);
INSERT INTO cms.lookup (LOOKUPTYPE_CODE,CODE,LABEL,DESCRIPTION,ORDINAL) VALUES ('StarterList.productionStatus','PENDING','Pending Approval','Pending Approval',1);
INSERT INTO cms.lookup (LOOKUPTYPE_CODE,CODE,LABEL,DESCRIPTION,ORDINAL) VALUES ('StarterList.productionStatus','ACTIVE','Active','Active',2);
INSERT INTO cms.lookup (LOOKUPTYPE_CODE,CODE,LABEL,DESCRIPTION,ORDINAL) VALUES ('StarterList.productionStatus','COMPLETED','Completed','Completed',3);
INSERT INTO cms.lookup (LOOKUPTYPE_CODE,CODE,LABEL,DESCRIPTION,ORDINAL) VALUES ('StarterList.productionStatus','LIMITED','Limited','Limited',4);
INSERT INTO cms.attributedefinition (NAME,ID,CONTENTTYPE_ID,ATTRIBUTETYPE_CODE,INHERITABLE,REQUIRED,LABEL,CARDINALITY_CODE,LOOKUP_CODE) VALUES ('productionStatus','StarterList.productionStatus','StarterList','S','F','F','','One','StarterList.productionStatus');

INSERT INTO cms.relationshipdefinition (NAME,ID,CONTENTTYPE_ID,INHERITABLE,REQUIRED,NAVIGABLE,LABEL,CARDINALITY_CODE) VALUES ('listContents','StarterList.listContents','StarterList','F','F','T','List Contents','Many');
INSERT INTO cms.relationshipdestination (RELATIONSHIPDEFINITION_ID,CONTENTTYPE_ID,LABEL,ID) VALUES ('FDFolder.children','StarterList',NULL,'FDFolder.children.StarterList');
INSERT INTO cms.relationshipdestination (RELATIONSHIPDEFINITION_ID,CONTENTTYPE_ID,LABEL,ID) VALUES ('StarterList.listContents','ConfiguredProduct',NULL,'StarterList.listContents.ConfiguredProduct');
INSERT INTO cms.relationshipdestination (RELATIONSHIPDEFINITION_ID,CONTENTTYPE_ID,LABEL,ID) VALUES ('StarterList.listContents','ConfiguredProductGroup',NULL,'StarterList.listContents.ConfiguredProductGroup');

-- TO BE SHOWN IN LEFT NAVIGATION TREE
INSERT INTO cms.contentnode (ID,CONTENTTYPE_ID) VALUES ('FDFolder:starterLists', 'FDFolder');
