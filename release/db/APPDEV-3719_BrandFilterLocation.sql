-- Brand filter location
INSERT INTO cms.lookuptype (CODE,NAME,DESCRIPTION) values ('Category.brandFilterLocation','Category.brandFilterLocation',null);
INSERT INTO cms.lookup (LOOKUPTYPE_CODE,CODE,LABEL,DESCRIPTION,ORDINAL) values ('Category.brandFilterLocation','BELOW_DEPARTMENT','On/Appearing below depratment','On/Appearing below depratment',1);
INSERT INTO cms.lookup (LOOKUPTYPE_CODE,CODE,LABEL,DESCRIPTION,ORDINAL) values ('Category.brandFilterLocation','BELOW_LOWEST_LEVEL_CATEGROY','On/Appearing below lowest level category','On/Appearing below lowest level category',2);
INSERT INTO cms.lookup (LOOKUPTYPE_CODE,CODE,LABEL,DESCRIPTION,ORDINAL) values ('Category.brandFilterLocation','OFF','Turned off','Turned off',3);

-- Department Brand Filter Location
INSERT INTO cms.attributedefinition (NAME,ID,CONTENTTYPE_ID,ATTRIBUTETYPE_CODE,INHERITABLE,REQUIRED,LABEL,CARDINALITY_CODE,LOOKUP_CODE) VALUES ('brandFilterLocation','Category.brandFilterLocation','Category','S','T','F','Brand Filter Location','One','Category.brandFilterLocation');