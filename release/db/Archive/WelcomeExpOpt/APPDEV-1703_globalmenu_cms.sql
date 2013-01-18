insert into CONTENTTYPE (ID,NAME,DESCRIPTION,GENERATE_ID) values ('GlobalMenuItem','GlobalMenuItem','Definition of type GlobalMenuItem','F');
insert into CONTENTTYPE (ID,NAME,DESCRIPTION,GENERATE_ID) values ('GlobalMenuSection','GlobalMenuSection','Definition of type GlobalMenuSection','F');


Insert into LOOKUPTYPE (CODE,NAME,DESCRIPTION) values ('GlobalMenuItem.LAYOUT','GlobalMenuItem.LAYOUT',null);

Insert into LOOKUP (LOOKUPTYPE_CODE,CODE,LABEL,DESCRIPTION,ORDINAL) values ('GlobalMenuItem.LAYOUT','0','Editorial','Editorial',1);
Insert into LOOKUP (LOOKUPTYPE_CODE,CODE,LABEL,DESCRIPTION,ORDINAL) values ('GlobalMenuItem.LAYOUT','1','Single Section Layout','Single Section Layout',2);
Insert into LOOKUP (LOOKUPTYPE_CODE,CODE,LABEL,DESCRIPTION,ORDINAL) values ('GlobalMenuItem.LAYOUT','2','Two Section Layout','Two Section Layout',3);
Insert into LOOKUP (LOOKUPTYPE_CODE,CODE,LABEL,DESCRIPTION,ORDINAL) values ('GlobalMenuItem.LAYOUT','3','Four Section Layout','Four Section Layout',4);


Insert into ATTRIBUTEDEFINITION (NAME,ID,CONTENTTYPE_ID,ATTRIBUTETYPE_CODE,INHERITABLE,REQUIRED,LABEL,CARDINALITY_CODE,LOOKUP_CODE) values ('GLOBAL_MENU_TITLE_LABEL','Department.GLOBAL_MENU_TITLE_LABEL','Department','S','F','F','Global Menu Title Label','One',null);
Insert into ATTRIBUTEDEFINITION (NAME,ID,CONTENTTYPE_ID,ATTRIBUTETYPE_CODE,INHERITABLE,REQUIRED,LABEL,CARDINALITY_CODE,LOOKUP_CODE) values ('GLOBAL_MENU_LINK_LABEL','Department.GLOBAL_MENU_LINK_LABEL','Department','S','F','F','Global Menu Link Label','One',null);
Insert into ATTRIBUTEDEFINITION (NAME,ID,CONTENTTYPE_ID,ATTRIBUTETYPE_CODE,INHERITABLE,REQUIRED,LABEL,CARDINALITY_CODE,LOOKUP_CODE) values ('GLOBAL_MENU_TITLE_LABEL','Category.GLOBAL_MENU_TITLE_LABEL','Category','S','F','F','Global Menu Title Label','One',null);
Insert into ATTRIBUTEDEFINITION (NAME,ID,CONTENTTYPE_ID,ATTRIBUTETYPE_CODE,INHERITABLE,REQUIRED,LABEL,CARDINALITY_CODE,LOOKUP_CODE) values ('GLOBAL_MENU_LINK_LABEL','Category.GLOBAL_MENU_LINK_LABEL','Category','S','F','F','Global Menu Link Label','One',null);

Insert into ATTRIBUTEDEFINITION (NAME,ID,CONTENTTYPE_ID,ATTRIBUTETYPE_CODE,INHERITABLE,REQUIRED,LABEL,CARDINALITY_CODE,LOOKUP_CODE) values ('TITLE_LABEL','GlobalMenuItem.TITLE_LABEL','GlobalMenuItem','S','F','F','Title Label','One',null);
Insert into ATTRIBUTEDEFINITION (NAME,ID,CONTENTTYPE_ID,ATTRIBUTETYPE_CODE,INHERITABLE,REQUIRED,LABEL,CARDINALITY_CODE,LOOKUP_CODE) values ('LAYOUT','GlobalMenuItem.LAYOUT','GlobalMenuItem','I','F','F','Global Menu Item Layout','One','GlobalMenuItem.LAYOUT');

Insert into ATTRIBUTEDEFINITION (NAME,ID,CONTENTTYPE_ID,ATTRIBUTETYPE_CODE,INHERITABLE,REQUIRED,LABEL,CARDINALITY_CODE,LOOKUP_CODE) values ('SHOW_ALL_SUB_CATEGORIES','GlobalMenuSection.SHOW_ALL_SUB_CATEGORIES','GlobalMenuSection','B','F','F','Show All Sub Categories','One',null);


Insert into RELATIONSHIPDEFINITION (NAME,ID,CONTENTTYPE_ID,INHERITABLE,REQUIRED,NAVIGABLE,LABEL,CARDINALITY_CODE,READONLY) values ('subSections','GlobalMenuItem.subSections','GlobalMenuItem','F','F','T','Sub Menu Sections','Many','F');
Insert into RELATIONSHIPDEFINITION (NAME,ID,CONTENTTYPE_ID,INHERITABLE,REQUIRED,NAVIGABLE,LABEL,CARDINALITY_CODE,READONLY) values ('editorial',	'GlobalMenuItem.editorial','GlobalMenuItem','F','F','F','Editorial','One','F');

Insert into RELATIONSHIPDEFINITION (NAME,ID,CONTENTTYPE_ID,INHERITABLE,REQUIRED,NAVIGABLE,LABEL,CARDINALITY_CODE,READONLY) values ('editorial','GlobalMenuSection.editorial','GlobalMenuSection','F','F','F','Editorial','One','F');
Insert into RELATIONSHIPDEFINITION (NAME,ID,CONTENTTYPE_ID,INHERITABLE,REQUIRED,NAVIGABLE,LABEL,CARDINALITY_CODE,READONLY) values ('subCategoryItems','GlobalMenuSection.subCategoryItems','GlobalMenuSection','F','F','F','Sub Category Items','Many','F');
Insert into RELATIONSHIPDEFINITION (NAME,ID,CONTENTTYPE_ID,INHERITABLE,REQUIRED,NAVIGABLE,LABEL,CARDINALITY_CODE,READONLY) values ('linkedProductContainer','GlobalMenuSection.linkedProductContainer','GlobalMenuSection','F','F','F','Linked Product Container','One','F');


Insert into RELATIONSHIPDESTINATION (RELATIONSHIPDEFINITION_ID,CONTENTTYPE_ID,ID,REVERSE_ATTRIBUTE_NAME,REVERSE_ATTRIBUTE_LABEL) values ('FDFolder.children','GlobalMenuItem','FDFolder.children.GlobalMenuItem',null,null);

Insert into RELATIONSHIPDESTINATION (RELATIONSHIPDEFINITION_ID,CONTENTTYPE_ID,ID,REVERSE_ATTRIBUTE_NAME,REVERSE_ATTRIBUTE_LABEL) values ('GlobalMenuItem.subSections','GlobalMenuSection','GlobalMenuItem.subSections.GlobalMenuSection',null,null);
Insert into RELATIONSHIPDESTINATION (RELATIONSHIPDEFINITION_ID,CONTENTTYPE_ID,ID,REVERSE_ATTRIBUTE_NAME,REVERSE_ATTRIBUTE_LABEL) values ('GlobalMenuItem.editorial','Html','GlobalMenuItem.editorial.Html',null,null);

Insert into RELATIONSHIPDESTINATION (RELATIONSHIPDEFINITION_ID,CONTENTTYPE_ID,ID,REVERSE_ATTRIBUTE_NAME,REVERSE_ATTRIBUTE_LABEL) values ('GlobalMenuSection.editorial','Html','GlobalMenuSection.editorial.Html',null,null);
Insert into RELATIONSHIPDESTINATION (RELATIONSHIPDEFINITION_ID,CONTENTTYPE_ID,ID,REVERSE_ATTRIBUTE_NAME,REVERSE_ATTRIBUTE_LABEL) values ('GlobalMenuSection.subCategoryItems','Category','GlobalMenuSection.subCategoryItems.Category',null,null);
Insert into RELATIONSHIPDESTINATION (RELATIONSHIPDEFINITION_ID,CONTENTTYPE_ID,ID,REVERSE_ATTRIBUTE_NAME,REVERSE_ATTRIBUTE_LABEL) values ('GlobalMenuSection.linkedProductContainer','Category','GlobalMenuSection.linkedProductContainer.Category',null,null);
Insert into RELATIONSHIPDESTINATION (RELATIONSHIPDEFINITION_ID,CONTENTTYPE_ID,ID,REVERSE_ATTRIBUTE_NAME,REVERSE_ATTRIBUTE_LABEL) values ('GlobalMenuSection.linkedProductContainer','Department','GlobalMenuSection.linkedProductContainer.Department',null,null);

