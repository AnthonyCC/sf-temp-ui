-- Carousel Location
INSERT INTO cms.lookuptype (CODE,NAME,DESCRIPTION) values ('Category.carouselPosition','Category.carouselPosition',null);
INSERT INTO cms.lookup (LOOKUPTYPE_CODE,CODE,LABEL,DESCRIPTION,ORDINAL) values ('Category.carouselPosition','TOP','TOP','TOP',1);
INSERT INTO cms.lookup (LOOKUPTYPE_CODE,CODE,LABEL,DESCRIPTION,ORDINAL) values ('Category.carouselPosition','BOTTOM','BOTTOM','BOTTOM',2);

-- Carousel Ratio
INSERT INTO cms.lookuptype (CODE,NAME,DESCRIPTION) values ('Category.carouselRatio','Category.carouselRatio',null);
INSERT INTO cms.lookup (LOOKUPTYPE_CODE,CODE,LABEL,DESCRIPTION,ORDINAL) values ('Category.carouselRatio','FULL_WIDTH','FULL WIDTH','FULL WIDTH',1);
INSERT INTO cms.lookup (LOOKUPTYPE_CODE,CODE,LABEL,DESCRIPTION,ORDINAL) values ('Category.carouselRatio','TWO_TWO','2/2','2/2',2);
INSERT INTO cms.lookup (LOOKUPTYPE_CODE,CODE,LABEL,DESCRIPTION,ORDINAL) values ('Category.carouselRatio','THREE_ONE','3/1','3/1',3);

-- Category Carousel Setup
INSERT INTO cms.attributedefinition (NAME,ID,CONTENTTYPE_ID,ATTRIBUTETYPE_CODE,INHERITABLE,REQUIRED,LABEL,CARDINALITY_CODE,LOOKUP_CODE) VALUES ('carouselPositionCLP','Category.carouselPositionCLP','Category','S','T','F','Carousel Position CLP','One','Category.carouselPosition');
INSERT INTO cms.attributedefinition (NAME,ID,CONTENTTYPE_ID,ATTRIBUTETYPE_CODE,INHERITABLE,REQUIRED,LABEL,CARDINALITY_CODE,LOOKUP_CODE) VALUES ('carouselPositionPLP','Category.carouselPositionPLP','Category','S','T','F','Carousel Position PLP','One','Category.carouselPosition');
INSERT INTO cms.attributedefinition (NAME,ID,CONTENTTYPE_ID,ATTRIBUTETYPE_CODE,INHERITABLE,REQUIRED,LABEL,CARDINALITY_CODE,LOOKUP_CODE) VALUES ('carouselRatioCLP','Category.carouselRatioCLP','Category','S','T','F','Carousel Ratio CLP','One','Category.carouselRatio');
INSERT INTO cms.attributedefinition (NAME,ID,CONTENTTYPE_ID,ATTRIBUTETYPE_CODE,INHERITABLE,REQUIRED,LABEL,CARDINALITY_CODE,LOOKUP_CODE) VALUES ('carouselRatioPLP','Category.carouselRatioPLP','Category','S','T','F','Carousel Ratio PLP','One','Category.carouselRatio');

-- Department Carousel Setup
INSERT INTO cms.attributedefinition (NAME,ID,CONTENTTYPE_ID,ATTRIBUTETYPE_CODE,INHERITABLE,REQUIRED,LABEL,CARDINALITY_CODE,LOOKUP_CODE) VALUES ('carouselPosition','Department.carouselPosition','Department','S','T','F','Carousel Position','One','Category.carouselPosition');
INSERT INTO cms.attributedefinition (NAME,ID,CONTENTTYPE_ID,ATTRIBUTETYPE_CODE,INHERITABLE,REQUIRED,LABEL,CARDINALITY_CODE,LOOKUP_CODE) VALUES ('carouselRatio','Department.carouselRatio','Department','S','T','F','Carousel Ratio','One','Category.carouselRatio');

-- SuperDepartment Carousel Setup
INSERT INTO cms.attributedefinition (NAME,ID,CONTENTTYPE_ID,ATTRIBUTETYPE_CODE,INHERITABLE,REQUIRED,LABEL,CARDINALITY_CODE,LOOKUP_CODE) VALUES ('carouselPosition','SuperDepartment.carouselPosition','SuperDepartment','S','T','F','Carousel Position','One','Category.carouselPosition');
INSERT INTO cms.attributedefinition (NAME,ID,CONTENTTYPE_ID,ATTRIBUTETYPE_CODE,INHERITABLE,REQUIRED,LABEL,CARDINALITY_CODE,LOOKUP_CODE) VALUES ('carouselRatio','SuperDepartment.carouselRatio','SuperDepartment','S','T','F','Carousel Ratio','One','Category.carouselRatio');
