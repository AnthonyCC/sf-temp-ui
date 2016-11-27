--Super department
INSERT INTO cms.contenttype (ID,NAME,DESCRIPTION,GENERATE_ID) VALUES ('SuperDepartment','SuperDepartment','Definition of type Super Department','F');
INSERT INTO cms.attributedefinition (NAME,ID,CONTENTTYPE_ID,ATTRIBUTETYPE_CODE,INHERITABLE,REQUIRED,LABEL,CARDINALITY_CODE,LOOKUP_CODE) VALUES ('name','SuperDepartment.name','SuperDepartment','S','F','F','Name','One',NULL);
INSERT INTO cms.attributedefinition (NAME,ID,CONTENTTYPE_ID,ATTRIBUTETYPE_CODE,INHERITABLE,REQUIRED,LABEL,CARDINALITY_CODE,LOOKUP_CODE) VALUES ('browseName','SuperDepartment.browseName','SuperDepartment','S','F','F','BrowseName','One',NULL);
INSERT INTO cms.attributedefinition (NAME,ID,CONTENTTYPE_ID,ATTRIBUTETYPE_CODE,INHERITABLE,REQUIRED,LABEL,CARDINALITY_CODE,LOOKUP_CODE) VALUES ('sdFeaturedRecommenderTitle','SuperDepartment.sdFeaturedRecommenderTitle','SuperDepartment','S','F','F','Title','One',NULL);
INSERT INTO cms.attributedefinition (NAME,ID,CONTENTTYPE_ID,ATTRIBUTETYPE_CODE,INHERITABLE,REQUIRED,LABEL,CARDINALITY_CODE,LOOKUP_CODE) VALUES ('sdFeaturedRecommenderRandomizeProducts','SuperDepartment.sdFeaturedRecommenderRandomizeProducts','SuperDepartment','B','F','F','Randomize Products (only if Source Category is set)','One',NULL);
INSERT INTO cms.attributedefinition (NAME,ID,CONTENTTYPE_ID,ATTRIBUTETYPE_CODE,INHERITABLE,REQUIRED,LABEL,CARDINALITY_CODE,LOOKUP_CODE) VALUES ('sdFeaturedRecommenderSiteFeature','SuperDepartment.sdFeaturedRecommenderSiteFeature','SuperDepartment','S','F','F','Site Feature (ignored if Source Category is set)','One',NULL);
INSERT INTO cms.attributedefinition (NAME,ID,CONTENTTYPE_ID,ATTRIBUTETYPE_CODE,INHERITABLE,REQUIRED,LABEL,CARDINALITY_CODE,LOOKUP_CODE) VALUES ('sdMerchantRecommenderTitle','SuperDepartment.sdMerchantRecommenderTitle','SuperDepartment','S','F','F','Title','One',NULL);
INSERT INTO cms.attributedefinition (NAME,ID,CONTENTTYPE_ID,ATTRIBUTETYPE_CODE,INHERITABLE,REQUIRED,LABEL,CARDINALITY_CODE,LOOKUP_CODE) VALUES ('sdMerchantRecommenderRandomizeProducts','SuperDepartment.sdMerchantRecommenderRandomizeProducts','SuperDepartment','B','F','F','Randomize Products','One',NULL);
INSERT INTO cms.relationshipdefinition (NAME,ID,CONTENTTYPE_ID,INHERITABLE,REQUIRED,NAVIGABLE,READONLY,LABEL,CARDINALITY_CODE) VALUES ('departments','SuperDepartment.departments','SuperDepartment','F','F','F','F','Departments','Many');
INSERT INTO cms.relationshipdestination (ID, RELATIONSHIPDEFINITION_ID,CONTENTTYPE_ID,REVERSE_ATTRIBUTE_NAME,REVERSE_ATTRIBUTE_LABEL) VALUES ('SuperDepartment.departments.Department','SuperDepartment.departments','Department',NULL,NULL);
INSERT INTO cms.relationshipdefinition (NAME,ID,CONTENTTYPE_ID,INHERITABLE,REQUIRED,NAVIGABLE,READONLY,LABEL,CARDINALITY_CODE) VALUES ('titleBar','SuperDepartment.titleBar','SuperDepartment','F','F','F','F','Title Bar','One');
INSERT INTO cms.relationshipdestination (ID, RELATIONSHIPDEFINITION_ID,CONTENTTYPE_ID,REVERSE_ATTRIBUTE_NAME,REVERSE_ATTRIBUTE_LABEL) VALUES ('SuperDepartment.titleBar.Image','SuperDepartment.titleBar','Image',NULL,NULL);
INSERT INTO cms.relationshipdefinition (NAME,ID,CONTENTTYPE_ID,INHERITABLE,REQUIRED,NAVIGABLE,READONLY,LABEL,CARDINALITY_CODE) VALUES ('superDepartmentBanner','SuperDepartment.superDepartmentBanner','SuperDepartment','F','F','F','F','Super Department Banner','One');
INSERT INTO cms.relationshipdestination (ID, RELATIONSHIPDEFINITION_ID,CONTENTTYPE_ID,REVERSE_ATTRIBUTE_NAME,REVERSE_ATTRIBUTE_LABEL) VALUES ('SuperDepartment.superDepartmentBanner.Html','SuperDepartment.superDepartmentBanner','Html',NULL,NULL);
INSERT INTO cms.relationshipdefinition (NAME,ID,CONTENTTYPE_ID,INHERITABLE,REQUIRED,NAVIGABLE,READONLY,LABEL,CARDINALITY_CODE) VALUES ('sdFeaturedRecommenderSourceCategory','SuperDepartment.sdFeaturedRecommenderSourceCategory','SuperDepartment','F','F','F','F','Source Category','One');
INSERT INTO cms.relationshipdestination (ID, RELATIONSHIPDEFINITION_ID,CONTENTTYPE_ID,REVERSE_ATTRIBUTE_NAME,REVERSE_ATTRIBUTE_LABEL) VALUES ('SuperDepartment.sdFeaturedRecommenderSourceCategory.Category','SuperDepartment.sdFeaturedRecommenderSourceCategory','Category',NULL,NULL);
INSERT INTO cms.relationshipdefinition (NAME,ID,CONTENTTYPE_ID,INHERITABLE,REQUIRED,NAVIGABLE,READONLY,LABEL,CARDINALITY_CODE) VALUES ('sdMerchantRecommenderProducts','SuperDepartment.sdMerchantRecommenderProducts','SuperDepartment','F','F','F','F','Products','Many');
INSERT INTO cms.relationshipdestination (ID, RELATIONSHIPDEFINITION_ID,CONTENTTYPE_ID,REVERSE_ATTRIBUTE_NAME,REVERSE_ATTRIBUTE_LABEL) VALUES ('SuperDepartment.sdMerchantRecommenderProducts.Product','SuperDepartment.sdMerchantRecommenderProducts','Product',NULL,NULL);

--Category sections
INSERT INTO cms.contenttype (ID,NAME,DESCRIPTION,GENERATE_ID) VALUES ('CategorySection','CategorySection','Department specific category sections','F');
INSERT INTO cms.attributedefinition (NAME,ID,CONTENTTYPE_ID,ATTRIBUTETYPE_CODE,INHERITABLE,REQUIRED,LABEL,CARDINALITY_CODE,LOOKUP_CODE) VALUES ('headline','CategorySection.headline','CategorySection','S','F','T','Headline','One',NULL);
INSERT INTO cms.relationshipdefinition (NAME,ID,CONTENTTYPE_ID,INHERITABLE,REQUIRED,NAVIGABLE,READONLY,LABEL,CARDINALITY_CODE) VALUES ('selectedCategories','CategorySection.selectedCategories','CategorySection','F','T','F','F','Selected Categories','Many');
INSERT INTO cms.relationshipdestination (ID, RELATIONSHIPDEFINITION_ID,CONTENTTYPE_ID,REVERSE_ATTRIBUTE_NAME,REVERSE_ATTRIBUTE_LABEL) VALUES ('CategorySection.selectedCategories.Category','CategorySection.selectedCategories','Category',NULL,NULL);
INSERT INTO cms.attributedefinition (NAME,ID,CONTENTTYPE_ID,ATTRIBUTETYPE_CODE,INHERITABLE,REQUIRED,LABEL,CARDINALITY_CODE,LOOKUP_CODE) VALUES ('insertColumnBreak','CategorySection.insertColumnBreak','CategorySection','B','F','F','Insert Column Break (in Global Navigation)','One',NULL);

--Department override
--INSERT INTO cms.contenttype (ID,NAME,DESCRIPTION,GENERATE_ID) VALUES ('DepartmentOverride','DepartmentOverride','Department override container for category sections on GlobalNav','F');
--INSERT INTO cms.relationshipdefinition (NAME,ID,CONTENTTYPE_ID,INHERITABLE,REQUIRED,NAVIGABLE,READONLY,LABEL,CARDINALITY_CODE) VALUES ('categorySectionsCol1','DepartmentOverride.categorySectionsCol1','DepartmentOverride','F','F','T','F','Category Sections Column 1','Many');
--INSERT INTO cms.relationshipdestination (ID, RELATIONSHIPDEFINITION_ID,CONTENTTYPE_ID,REVERSE_ATTRIBUTE_NAME,REVERSE_ATTRIBUTE_LABEL) VALUES ('DepartmentOverride.categorySectionsCol1.GlobalNavCategorySection','DepartmentOverride.categorySectionsCol1','GlobalNavCategorySection',NULL,NULL);
--INSERT INTO cms.relationshipdefinition (NAME,ID,CONTENTTYPE_ID,INHERITABLE,REQUIRED,NAVIGABLE,READONLY,LABEL,CARDINALITY_CODE) VALUES ('categorySectionsCol2','DepartmentOverride.categorySectionsCol2','DepartmentOverride','F','F','T','F','Category Sections Column 2','Many');
--INSERT INTO cms.relationshipdestination (ID, RELATIONSHIPDEFINITION_ID,CONTENTTYPE_ID,REVERSE_ATTRIBUTE_NAME,REVERSE_ATTRIBUTE_LABEL) VALUES ('DepartmentOverride.categorySectionsCol2.GlobalNavCategorySection','DepartmentOverride.categorySectionsCol2','GlobalNavCategorySection',NULL,NULL);
--INSERT INTO cms.relationshipdefinition (NAME,ID,CONTENTTYPE_ID,INHERITABLE,REQUIRED,NAVIGABLE,READONLY,LABEL,CARDINALITY_CODE) VALUES ('categorySectionsCol3','DepartmentOverride.categorySectionsCol3','DepartmentOverride','F','F','T','F','Category Sections Column 3','Many');
--INSERT INTO cms.relationshipdestination (ID, RELATIONSHIPDEFINITION_ID,CONTENTTYPE_ID,REVERSE_ATTRIBUTE_NAME,REVERSE_ATTRIBUTE_LABEL) VALUES ('DepartmentOverride.categorySectionsCol3.GlobalNavCategorySection','DepartmentOverride.categorySectionsCol3','GlobalNavCategorySection',NULL,NULL);
--INSERT INTO cms.relationshipdefinition (NAME,ID,CONTENTTYPE_ID,INHERITABLE,REQUIRED,NAVIGABLE,READONLY,LABEL,CARDINALITY_CODE) VALUES ('relatedDepartment','DepartmentOverride.relatedDepartment','DepartmentOverride','F','T','F','F','Category Sections Applied To (Department)','One');
--INSERT INTO cms.relationshipdestination (ID, RELATIONSHIPDEFINITION_ID,CONTENTTYPE_ID,REVERSE_ATTRIBUTE_NAME,REVERSE_ATTRIBUTE_LABEL) VALUES ('DepartmentOverride.relatedDepartment.Department','DepartmentOverride.relatedDepartment','Department',NULL,NULL);

--Global navigations
INSERT INTO cms.contenttype (ID,NAME,DESCRIPTION,GENERATE_ID) VALUES ('GlobalNavigation','GlobalNavigation','Definition of type Global Navigation','F');
--INSERT INTO cms.relationshipdefinition (NAME,ID,CONTENTTYPE_ID,INHERITABLE,REQUIRED,NAVIGABLE,READONLY,LABEL,CARDINALITY_CODE) VALUES ('departmentOverrides','GlobalNavigation.departmentOverrides','GlobalNavigation','F','F','T','F','DDS Category Overrides','Many');
--INSERT INTO cms.relationshipdestination (ID, RELATIONSHIPDEFINITION_ID,CONTENTTYPE_ID,REVERSE_ATTRIBUTE_NAME,REVERSE_ATTRIBUTE_LABEL) VALUES ('GlobalNavigation.departmentOverrides.DepartmentOverride','GlobalNavigation.departmentOverrides','DepartmentOverride',NULL,NULL);
INSERT INTO cms.relationshipdefinition (NAME,ID,CONTENTTYPE_ID,INHERITABLE,REQUIRED,NAVIGABLE,READONLY,LABEL,CARDINALITY_CODE) VALUES ('items','GlobalNavigation.items','GlobalNavigation','F','T','F','F','Dropdown Data Source (DDS)','Many');
INSERT INTO cms.relationshipdestination (ID, RELATIONSHIPDEFINITION_ID,CONTENTTYPE_ID,REVERSE_ATTRIBUTE_NAME,REVERSE_ATTRIBUTE_LABEL) VALUES ('GlobalNavigation.items.Department','GlobalNavigation.items','Department',NULL,NULL);
INSERT INTO cms.relationshipdestination (ID, RELATIONSHIPDEFINITION_ID,CONTENTTYPE_ID,REVERSE_ATTRIBUTE_NAME,REVERSE_ATTRIBUTE_LABEL) VALUES ('GlobalNavigation.items.SuperDepartment','GlobalNavigation.items','SuperDepartment',NULL,NULL);
INSERT INTO cms.relationshipdefinition (NAME,ID,CONTENTTYPE_ID,INHERITABLE,REQUIRED,NAVIGABLE,READONLY,LABEL,CARDINALITY_CODE) VALUES ('media','GlobalNavigation.media','GlobalNavigation','F','T','F','F','Media','One');
INSERT INTO cms.relationshipdestination (ID, RELATIONSHIPDEFINITION_ID,CONTENTTYPE_ID,REVERSE_ATTRIBUTE_NAME,REVERSE_ATTRIBUTE_LABEL) VALUES ('GlobalNavigation.media.Html','GlobalNavigation.media','Html',NULL,NULL);

--Store
INSERT INTO cms.relationshipdefinition (NAME,ID,CONTENTTYPE_ID,INHERITABLE,REQUIRED,NAVIGABLE,READONLY,LABEL,CARDINALITY_CODE) VALUES ('superDepartments','Store.superDepartments','Store','F','F','T','F','Super Departments','Many');
INSERT INTO cms.relationshipdestination (ID, RELATIONSHIPDEFINITION_ID,CONTENTTYPE_ID,REVERSE_ATTRIBUTE_NAME,REVERSE_ATTRIBUTE_LABEL) VALUES ('Store.superDepartments.SuperDepartment','Store.superDepartments','SuperDepartment',NULL,NULL);
INSERT INTO cms.relationshipdefinition (NAME,ID,CONTENTTYPE_ID,INHERITABLE,REQUIRED,NAVIGABLE,READONLY,LABEL,CARDINALITY_CODE) VALUES ('globalNavigations','Store.globalNavigations','Store','F','F','T','F','Global Navigations','Many');
INSERT INTO cms.relationshipdestination (ID, RELATIONSHIPDEFINITION_ID,CONTENTTYPE_ID,REVERSE_ATTRIBUTE_NAME,REVERSE_ATTRIBUTE_LABEL) VALUES ('Store.globalNavigations.GlobalNavigation','Store.globalNavigations','GlobalNavigation',NULL,NULL);

--Department
INSERT INTO cms.attributedefinition (NAME,ID,CONTENTTYPE_ID,ATTRIBUTETYPE_CODE,INHERITABLE,REQUIRED,LABEL,CARDINALITY_CODE,LOOKUP_CODE) VALUES ('globalNavName','Department.globalNavName','Department','S','F','F','Global Nav Name','One',NULL);
INSERT INTO cms.relationshipdefinition (NAME,ID,CONTENTTYPE_ID,INHERITABLE,REQUIRED,NAVIGABLE,READONLY,LABEL,CARDINALITY_CODE) VALUES ('heroImage','Department.heroImage','Department','F','F','F','F','Hero Image','One');
INSERT INTO cms.relationshipdestination (ID, RELATIONSHIPDEFINITION_ID,CONTENTTYPE_ID,REVERSE_ATTRIBUTE_NAME,REVERSE_ATTRIBUTE_LABEL) VALUES ('Department.heroImage.Image','Department.heroImage','Image',NULL,NULL);
INSERT INTO cms.relationshipdefinition (NAME,ID,CONTENTTYPE_ID,INHERITABLE,REQUIRED,NAVIGABLE,READONLY,LABEL,CARDINALITY_CODE) VALUES ('seasonalMedia','Department.seasonalMedia','Department','F','F','F','F','Seasonal Media','One');
INSERT INTO cms.relationshipdestination (ID, RELATIONSHIPDEFINITION_ID,CONTENTTYPE_ID,REVERSE_ATTRIBUTE_NAME,REVERSE_ATTRIBUTE_LABEL) VALUES ('Department.seasonalMedia.Html','Department.seasonalMedia','Html',NULL,NULL);
INSERT INTO cms.relationshipdefinition (NAME,ID,CONTENTTYPE_ID,INHERITABLE,REQUIRED,NAVIGABLE,READONLY,LABEL,CARDINALITY_CODE) VALUES ('categorySections','Department.categorySections','Department','F','F','T','F','Category Sections','Many');
INSERT INTO cms.relationshipdestination (ID, RELATIONSHIPDEFINITION_ID,CONTENTTYPE_ID,REVERSE_ATTRIBUTE_NAME,REVERSE_ATTRIBUTE_LABEL) VALUES ('Department.categorySections.CategorySection','Department.categorySections','CategorySection',NULL,NULL);

--Category
INSERT INTO cms.relationshipdefinition (NAME,ID,CONTENTTYPE_ID,INHERITABLE,REQUIRED,NAVIGABLE,READONLY,LABEL,CARDINALITY_CODE) VALUES ('globalNavPostNameImage','Category.globalNavPostNameImage','Category','F','F','F','F','Global Nav Post Name Image','One');
INSERT INTO cms.relationshipdestination (ID, RELATIONSHIPDEFINITION_ID,CONTENTTYPE_ID,REVERSE_ATTRIBUTE_NAME,REVERSE_ATTRIBUTE_LABEL) VALUES ('Category.globalNavPostNameImage.Image','Category.globalNavPostNameImage','Image',NULL,NULL);



