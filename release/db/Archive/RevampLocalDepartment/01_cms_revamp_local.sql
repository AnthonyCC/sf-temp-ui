ALTER TABLE cms.RELATIONSHIPDEFINITION ADD ("READONLY" VARCHAR2(1) DEFAULT 'F' NOT NULL);

ALTER TABLE cms.RELATIONSHIPDESTINATION ADD ("REVERSE_ATTRIBUTE_NAME" VARCHAR2(128));

ALTER TABLE cms.RELATIONSHIPDESTINATION DROP COLUMN "LABEL";
ALTER TABLE cms.RELATIONSHIPDESTINATION ADD ("REVERSE_ATTRIBUTE_LABEL" VARCHAR2(128));


INSERT INTO cms.contenttype (ID,NAME,DESCRIPTION,GENERATE_ID) VALUES ('Producer','Producer','Definition of type Producer','F');
INSERT INTO cms.contenttype (ID,NAME,DESCRIPTION,GENERATE_ID) VALUES ('ProducerType','ProducerType','Definition of type ProducerType','F');

INSERT INTO cms.attributedefinition (NAME,ID,CONTENTTYPE_ID,ATTRIBUTETYPE_CODE,INHERITABLE,REQUIRED,LABEL,CARDINALITY_CODE,LOOKUP_CODE) VALUES ('FULL_NAME','Producer.FULL_NAME','Producer','S','F','F','Name','One',NULL);
INSERT INTO cms.attributedefinition (NAME,ID,CONTENTTYPE_ID,ATTRIBUTETYPE_CODE,INHERITABLE,REQUIRED,LABEL,CARDINALITY_CODE,LOOKUP_CODE) VALUES ('ADDRESS','Producer.ADDRESS','Producer','TXT','F','F','Address','One',NULL);
INSERT INTO cms.attributedefinition (NAME,ID,CONTENTTYPE_ID,ATTRIBUTETYPE_CODE,INHERITABLE,REQUIRED,LABEL,CARDINALITY_CODE,LOOKUP_CODE) VALUES ('GMAPS_LOCATION','Producer.GMAPS_LOCATION','Producer','S','F','F','Google Maps Location','One',NULL);
INSERT INTO cms.attributedefinition (NAME,ID,CONTENTTYPE_ID,ATTRIBUTETYPE_CODE,INHERITABLE,REQUIRED,LABEL,CARDINALITY_CODE,LOOKUP_CODE) VALUES ('FULL_NAME','ProducerType.FULL_NAME','ProducerType','S','F','F','Name','One',NULL);

INSERT INTO cms.relationshipdefinition (NAME,ID,CONTENTTYPE_ID,INHERITABLE,REQUIRED,NAVIGABLE,READONLY,LABEL,CARDINALITY_CODE) VALUES ('bubble_content','Producer.bubble_content','Producer','F','F','F','F','Bubble Content','One');
INSERT INTO cms.relationshipdefinition (NAME,ID,CONTENTTYPE_ID,INHERITABLE,REQUIRED,NAVIGABLE,READONLY,LABEL,CARDINALITY_CODE) VALUES ('icon','Producer.icon','Producer','F','F','F','F','Icon','One');
INSERT INTO cms.relationshipdefinition (NAME,ID,CONTENTTYPE_ID,INHERITABLE,REQUIRED,NAVIGABLE,READONLY,LABEL,CARDINALITY_CODE) VALUES ('icon_shadow','Producer.icon_shadow','Producer','F','F','F','F','Icon Shadow','One');
INSERT INTO cms.relationshipdefinition (NAME,ID,CONTENTTYPE_ID,INHERITABLE,REQUIRED,NAVIGABLE,READONLY,LABEL,CARDINALITY_CODE) VALUES ('producer_type','Producer.producer_type','Producer','F','F','F','F','Producer Type','One');
INSERT INTO cms.relationshipdefinition (NAME,ID,CONTENTTYPE_ID,INHERITABLE,REQUIRED,NAVIGABLE,READONLY,LABEL,CARDINALITY_CODE) VALUES ('brand_category','Producer.brand_category','Producer','F','F','F','F','Brand Category','One');
INSERT INTO cms.relationshipdefinition (NAME,ID,CONTENTTYPE_ID,INHERITABLE,REQUIRED,NAVIGABLE,READONLY,LABEL,CARDINALITY_CODE) VALUES ('brand','Producer.brand','Producer','F','F','F','F','Brand','One');
INSERT INTO cms.relationshipdefinition (NAME,ID,CONTENTTYPE_ID,INHERITABLE,REQUIRED,NAVIGABLE,READONLY,LABEL,CARDINALITY_CODE) VALUES ('icon','ProducerType.icon','ProducerType','F','F','F','F','Icon','One');
INSERT INTO cms.relationshipdefinition (NAME,ID,CONTENTTYPE_ID,INHERITABLE,REQUIRED,NAVIGABLE,READONLY,LABEL,CARDINALITY_CODE) VALUES ('icon_shadow','ProducerType.icon_shadow','ProducerType','F','F','F','F','Icon Shadow','One');


INSERT INTO cms.relationshipdestination (RELATIONSHIPDEFINITION_ID,CONTENTTYPE_ID,REVERSE_ATTRIBUTE_NAME,ID) VALUES ('Producer.bubble_content','Html',NULL,'Producer.bubble_content.Html');
INSERT INTO cms.relationshipdestination (RELATIONSHIPDEFINITION_ID,CONTENTTYPE_ID,REVERSE_ATTRIBUTE_NAME,ID) VALUES ('Producer.icon','Image',NULL,'Producer.icon.Image');
INSERT INTO cms.relationshipdestination (RELATIONSHIPDEFINITION_ID,CONTENTTYPE_ID,REVERSE_ATTRIBUTE_NAME,ID) VALUES ('Producer.icon_shadow','Image',NULL,'Producer.icon_shadow.Image');
INSERT INTO cms.relationshipdestination (RELATIONSHIPDEFINITION_ID,CONTENTTYPE_ID,REVERSE_ATTRIBUTE_NAME,ID) VALUES ('Producer.producer_type','ProducerType',NULL,'Producer.producer_type.ProducerType');
INSERT INTO cms.relationshipdestination (RELATIONSHIPDEFINITION_ID,CONTENTTYPE_ID,REVERSE_ATTRIBUTE_NAME,ID) VALUES ('Producer.brand_category','Category',NULL,'Producer.brand_category.Category');
INSERT INTO cms.relationshipdestination (ID, RELATIONSHIPDEFINITION_ID,CONTENTTYPE_ID,REVERSE_ATTRIBUTE_NAME,REVERSE_ATTRIBUTE_LABEL) VALUES ('Producer.brand.Brand','Producer.brand','Brand','producer','Brand Producer');

INSERT INTO cms.relationshipdestination (RELATIONSHIPDEFINITION_ID,CONTENTTYPE_ID,REVERSE_ATTRIBUTE_NAME,ID) VALUES ('ProducerType.icon','Image',NULL,'ProducerType.icon.Image');
INSERT INTO cms.relationshipdestination (RELATIONSHIPDEFINITION_ID,CONTENTTYPE_ID,REVERSE_ATTRIBUTE_NAME,ID) VALUES ('ProducerType.icon_shadow','Image',NULL,'ProducerType.icon_shadow.Image');


INSERT INTO cms.relationshipdestination (RELATIONSHIPDEFINITION_ID,CONTENTTYPE_ID,REVERSE_ATTRIBUTE_NAME,ID) VALUES ('FDFolder.children','Producer',NULL,'FDFolder.children.Producer');
INSERT INTO cms.relationshipdestination (RELATIONSHIPDEFINITION_ID,CONTENTTYPE_ID,REVERSE_ATTRIBUTE_NAME,ID) VALUES ('FDFolder.children','ProducerType',NULL,'FDFolder.children.ProducerType');

INSERT INTO cms.relationshipdestination (ID, RELATIONSHIPDEFINITION_ID,CONTENTTYPE_ID,REVERSE_ATTRIBUTE_NAME,REVERSE_ATTRIBUTE_LABEL) VALUES ('Recommender.scope.Brand','Recommender.scope','Brand',NULL,NULL);


INSERT INTO cms.attributedefinition (NAME,ID,CONTENTTYPE_ID,ATTRIBUTETYPE_CODE,INHERITABLE,REQUIRED,LABEL,CARDINALITY_CODE,LOOKUP_CODE) VALUES ('show_temp_unavailable','RecommenderStrategy.show_temp_unavailable','RecommenderStrategy','B','F','F','Show Temporary Unavailable','One',NULL);

INSERT INTO cms.attributedefinition (NAME,ID,CONTENTTYPE_ID,ATTRIBUTETYPE_CODE,INHERITABLE,REQUIRED,LABEL,CARDINALITY_CODE,LOOKUP_CODE) VALUES ('HIDE_INACTIVE_SIDE_NAV','Category.HIDE_INACTIVE_SIDE_NAV','Category','B','T','F','Hide Inactive Category in Side Nav','One',NULL);

