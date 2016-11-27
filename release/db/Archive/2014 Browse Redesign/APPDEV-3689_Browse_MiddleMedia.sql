-- Banner Location
INSERT INTO cms.lookuptype (CODE,NAME,DESCRIPTION) values ('Category.bannerLocation','Category.bannerLocation',null);
INSERT INTO cms.lookup (LOOKUPTYPE_CODE,CODE,LABEL,DESCRIPTION,ORDINAL) values ('Category.bannerLocation','TOP','TOP','TOP',1);
INSERT INTO cms.lookup (LOOKUPTYPE_CODE,CODE,LABEL,DESCRIPTION,ORDINAL) values ('Category.bannerLocation','BOTTOM','BOTTOM','BOTTOM',2);

-- Category middle media
INSERT INTO cms.relationshipdefinition (NAME,ID,CONTENTTYPE_ID,INHERITABLE,REQUIRED,NAVIGABLE,READONLY,LABEL,CARDINALITY_CODE) VALUES ('middleMedia','Category.middleMedia','Category','T','F','F','F','Category Middle Media','One');
INSERT INTO cms.relationshipdestination (ID, RELATIONSHIPDEFINITION_ID,CONTENTTYPE_ID,REVERSE_ATTRIBUTE_NAME,REVERSE_ATTRIBUTE_LABEL) VALUES ('Category.middleMedia.Html','Category.middleMedia','Html',NULL,NULL);
INSERT INTO cms.attributedefinition (NAME,ID,CONTENTTYPE_ID,ATTRIBUTETYPE_CODE,INHERITABLE,REQUIRED,LABEL,CARDINALITY_CODE,LOOKUP_CODE) VALUES ('bannerLocationCLP','Category.bannerLocationCLP','Category','S','T','F','Banner Location CLP','One','Category.bannerLocation');
INSERT INTO cms.attributedefinition (NAME,ID,CONTENTTYPE_ID,ATTRIBUTETYPE_CODE,INHERITABLE,REQUIRED,LABEL,CARDINALITY_CODE,LOOKUP_CODE) VALUES ('bannerLocationPLP','Category.bannerLocationPLP','Category','S','T','F','Banner Location PLP','One','Category.bannerLocation');

-- Department middle media
INSERT INTO cms.relationshipdefinition (NAME,ID,CONTENTTYPE_ID,INHERITABLE,REQUIRED,NAVIGABLE,READONLY,LABEL,CARDINALITY_CODE) VALUES ('middleMedia','Department.middleMedia','Department','T','F','F','F','Department Middle Media','One');
INSERT INTO cms.relationshipdestination (ID, RELATIONSHIPDEFINITION_ID,CONTENTTYPE_ID,REVERSE_ATTRIBUTE_NAME,REVERSE_ATTRIBUTE_LABEL) VALUES ('Department.middleMedia.Html','Department.middleMedia','Html',NULL,NULL);
INSERT INTO cms.attributedefinition (NAME,ID,CONTENTTYPE_ID,ATTRIBUTETYPE_CODE,INHERITABLE,REQUIRED,LABEL,CARDINALITY_CODE,LOOKUP_CODE) VALUES ('bannerLocation','Department.bannerLocation','Department','S','T','F','Banner Location','One','Category.bannerLocation');

-- SuperDepartment middle media
INSERT INTO cms.relationshipdefinition (NAME,ID,CONTENTTYPE_ID,INHERITABLE,REQUIRED,NAVIGABLE,READONLY,LABEL,CARDINALITY_CODE) VALUES ('middleMedia','SuperDepartment.middleMedia','SuperDepartment','T','F','F','F','SuperDepartment Middle Media','One');
INSERT INTO cms.relationshipdestination (ID, RELATIONSHIPDEFINITION_ID,CONTENTTYPE_ID,REVERSE_ATTRIBUTE_NAME,REVERSE_ATTRIBUTE_LABEL) VALUES ('SuperDepartment.middleMedia.Html','SuperDepartment.middleMedia','Html',NULL,NULL);
INSERT INTO cms.attributedefinition (NAME,ID,CONTENTTYPE_ID,ATTRIBUTETYPE_CODE,INHERITABLE,REQUIRED,LABEL,CARDINALITY_CODE,LOOKUP_CODE) VALUES ('bannerLocation','SuperDepartment.bannerLocation','SuperDepartment','S','T','F','Banner Location','One','Category.bannerLocation');