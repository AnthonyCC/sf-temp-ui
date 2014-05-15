--Category
DELETE FROM cms.relationshipdestination WHERE id = 'Category.globalNavPostNameImage.Image';

DELETE FROM cms.relationshipdefinition WHERE id = 'Category.globalNavPostNameImage';

--Department
DELETE FROM cms.relationshipdestination WHERE id = 'Department.heroImage.Image';
DELETE FROM cms.relationshipdestination WHERE id = 'Department.seasonalMedia.Html';
DELETE FROM cms.relationshipdestination WHERE id = 'Department.categorySections.CategorySection';

DELETE FROM cms.relationshipdefinition WHERE id = 'Department.heroImage';
DELETE FROM cms.relationshipdefinition WHERE id = 'Department.seasonalMedia';
DELETE FROM cms.relationshipdefinition WHERE id = 'Department.categorySections';

DELETE FROM cms.attributedefinition WHERE id = 'Department.globalNavName';

--Store
DELETE FROM cms.relationshipdestination WHERE id = 'Store.globalNavigations.GlobalNavigation';
DELETE FROM cms.relationshipdestination WHERE id = 'Store.superDepartments.SuperDepartment';

DELETE FROM cms.relationshipdefinition WHERE id = 'Store.globalNavigations';
DELETE FROM cms.relationshipdefinition WHERE id = 'Store.superDepartments';

--Global navigations
DELETE FROM cms.relationshipdestination WHERE id = 'GlobalNavigation.media.Html';
DELETE FROM cms.relationshipdestination WHERE id = 'GlobalNavigation.items.SuperDepartment';
DELETE FROM cms.relationshipdestination WHERE id = 'GlobalNavigation.items.Department';
--DELETE FROM cms.relationshipdestination WHERE id = 'GlobalNavigation.departmentOverrides.DepartmentOverride';
--DELETE FROM cms.relationshipdefinition WHERE id = 'GlobalNavigation.departmentOverrides';
DELETE FROM cms.relationshipdefinition WHERE id = 'GlobalNavigation.media';
DELETE FROM cms.relationshipdefinition WHERE id = 'GlobalNavigation.items';

DELETE FROM cms.contenttype WHERE id = 'GlobalNavigation';

--Super departments
DELETE FROM cms.relationshipdestination WHERE id = 'SuperDepartment.superDepartmentBanner.Html';
DELETE FROM cms.relationshipdestination WHERE id = 'SuperDepartment.titleBar.Image';
DELETE FROM cms.relationshipdestination WHERE id = 'SuperDepartment.departments.Department';
DELETE FROM cms.relationshipdestination WHERE id = 'SuperDepartment.sdFeaturedRecommenderSourceCategory.Category';
DELETE FROM cms.relationshipdestination WHERE id = 'SuperDepartment.sdMerchantRecommenderProducts.Product';

DELETE FROM cms.relationshipdefinition WHERE id = 'SuperDepartment.superDepartmentBanner';
DELETE FROM cms.relationshipdefinition WHERE id = 'SuperDepartment.titleBar';
DELETE FROM cms.relationshipdefinition WHERE id = 'SuperDepartment.departments';
DELETE FROM cms.relationshipdefinition WHERE id = 'SuperDepartment.sdMerchantRecommenderProducts';
DELETE FROM cms.relationshipdefinition WHERE id = 'SuperDepartment.sdFeaturedRecommenderSourceCategory';

DELETE FROM cms.attributedefinition WHERE id = 'SuperDepartment.sdFeaturedRecommenderTitle';
DELETE FROM cms.attributedefinition WHERE id = 'SuperDepartment.sdFeaturedRecommenderRandomizeProducts';
DELETE FROM cms.attributedefinition WHERE id = 'SuperDepartment.sdFeaturedRecommenderSiteFeature';
DELETE FROM cms.attributedefinition WHERE id = 'SuperDepartment.sdMerchantRecommenderTitle';
DELETE FROM cms.attributedefinition WHERE id = 'SuperDepartment.sdMerchantRecommenderRandomizeProducts';
DELETE FROM cms.attributedefinition WHERE id = 'SuperDepartment.browseName';
DELETE FROM cms.attributedefinition WHERE id = 'SuperDepartment.name';

DELETE FROM cms.contenttype WHERE id = 'SuperDepartment';

--Department override
--DELETE FROM cms.relationshipdestination WHERE id = 'DepartmentOverride.categorySectionsCol1.GlobalNavCategorySection';
--DELETE FROM cms.relationshipdefinition WHERE id = 'DepartmentOverride.categorySectionsCol1';
--DELETE FROM cms.relationshipdestination WHERE id = 'DepartmentOverride.categorySectionsCol2.GlobalNavCategorySection';
--DELETE FROM cms.relationshipdefinition WHERE id = 'DepartmentOverride.categorySectionsCol2';
--DELETE FROM cms.relationshipdestination WHERE id = 'DepartmentOverride.categorySectionsCol3.GlobalNavCategorySection';
--DELETE FROM cms.relationshipdefinition WHERE id = 'DepartmentOverride.categorySectionsCol3';
--DELETE FROM cms.relationshipdestination WHERE id = 'DepartmentOverride.relatedDepartment.Department';
--DELETE FROM cms.relationshipdefinition WHERE id = 'DepartmentOverride.relatedDepartment';

--DELETE FROM cms.contenttype WHERE id = 'DepartmentOverride';

--Category sections
DELETE FROM cms.relationshipdestination WHERE id = 'CategorySection.selectedCategories.Category';
DELETE FROM cms.relationshipdefinition WHERE id = 'CategorySection.selectedCategories';
DELETE FROM cms.attributedefinition WHERE id = 'CategorySection.headline';
DELETE FROM cms.attributedefinition WHERE id = 'CategorySection.insertColumnBreak';

DELETE FROM cms.contenttype WHERE id = 'CategorySection';
