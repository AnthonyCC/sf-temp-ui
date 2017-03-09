DELETE FROM cms.relationshipdestination WHERE ID = 'ModuleContainer.modulesAndGroups.Module';
DELETE FROM cms.relationshipdestination WHERE ID = 'ModuleContainer.modulesAndGroups.ModuleGroup';
DELETE FROM cms.relationshipdestination WHERE ID = 'ModuleGroup.modules.Module';
DELETE FROM cms.relationshipdestination WHERE ID = 'ModuleGroup.viewAllSourceNode.Category';
DELETE FROM cms.relationshipdestination WHERE ID = 'ModuleGroup.viewAllSourceNode.Department';
DELETE FROM cms.relationshipdestination WHERE ID = 'Module.imageGrid.ImageBanner';
DELETE FROM cms.relationshipdestination WHERE ID = 'Module.openHTML.Html';
DELETE FROM cms.relationshipdestination WHERE ID = 'Module.iconList.ImageBanner';
DELETE FROM cms.relationshipdestination WHERE ID = 'Module.heroGraphic.Image';
DELETE FROM cms.relationshipdestination WHERE ID = 'Module.headerGraphic.Image';
DELETE FROM cms.relationshipdestination WHERE ID = 'Module.editorialContent.Html';
DELETE FROM cms.relationshipdestination WHERE ID = 'Module.sourceNode.Category';
DELETE FROM cms.relationshipdestination WHERE ID = 'Module.sourceNode.Department';
DELETE FROM cms.relationshipdestination WHERE ID = 'Module.sourceNode.Brand';

DELETE FROM cms.relationshipdestination WHERE ID = 'FDFolder.children.ModuleContainer';
DELETE FROM cms.relationshipdestination WHERE ID = 'FDFolder.children.ModuleGroup';
DELETE FROM cms.relationshipdestination WHERE ID = 'FDFolder.children.Module';

DELETE FROM cms.relationshipdefinition WHERE ID = 'ModuleContainer.modulesAndGroups';
DELETE FROM cms.relationshipdefinition WHERE ID = 'ModuleGroup.modules';
DELETE FROM cms.relationshipdefinition WHERE ID = 'ModuleGroup.viewAllSourceNode';
DELETE FROM cms.relationshipdefinition WHERE ID = 'Module.imageGrid';
DELETE FROM cms.relationshipdefinition WHERE ID = 'Module.openHTML';
DELETE FROM cms.relationshipdefinition WHERE ID = 'Module.iconList';
DELETE FROM cms.relationshipdefinition WHERE ID = 'Module.heroGraphic';
DELETE FROM cms.relationshipdefinition WHERE ID = 'Module.headerGraphic';
DELETE FROM cms.relationshipdefinition WHERE ID = 'Module.editorialContent';
DELETE FROM cms.relationshipdefinition WHERE ID = 'Module.sourceNode';

DELETE FROM cms.attributedefinition WHERE ID = 'Module.productSourceType';
DELETE FROM cms.attributedefinition WHERE ID = 'Module.displayType';
DELETE FROM cms.attributedefinition WHERE ID = 'Module.productListRowMax';

DELETE FROM cms.lookup WHERE LOOKUPTYPE_CODE = 'Module.productSourceType';
DELETE FROM cms.lookup WHERE LOOKUPTYPE_CODE = 'Module.displayType';
DELETE FROM cms.lookup WHERE LOOKUPTYPE_CODE = 'Module.productListRowMax';

DELETE FROM cms.lookuptype WHERE CODE = 'Module.productSourceType';
DELETE FROM cms.lookuptype WHERE CODE = 'Module.displayType';
DELETE FROM cms.lookuptype WHERE CODE = 'Module.productListRowMax';

DELETE FROM cms.attributedefinition WHERE ID = 'ImageBanner.bannerURL';
DELETE FROM cms.attributedefinition WHERE ID = 'ModuleContainer.name';
DELETE FROM cms.attributedefinition WHERE ID = 'ModuleGroup.name';
DELETE FROM cms.attributedefinition WHERE ID = 'ModuleGroup.moduleGroupTitle';
DELETE FROM cms.attributedefinition WHERE ID = 'ModuleGroup.moduleGroupTitleTextBanner';
DELETE FROM cms.attributedefinition WHERE ID = 'ModuleGroup.viewAllButtonURL';
DELETE FROM cms.attributedefinition WHERE ID = 'ModuleGroup.hideViewAllButton';
DELETE FROM cms.attributedefinition WHERE ID = 'Module.name';
DELETE FROM cms.attributedefinition WHERE ID = 'Module.moduleTitle';
DELETE FROM cms.attributedefinition WHERE ID = 'Module.moduleTitleTextBanner';
DELETE FROM cms.attributedefinition WHERE ID = 'Module.viewAllButtonLink';
DELETE FROM cms.attributedefinition WHERE ID = 'Module.contentTitle';
DELETE FROM cms.attributedefinition WHERE ID = 'Module.heroTitle';
DELETE FROM cms.attributedefinition WHERE ID = 'Module.heroSubtitle';
DELETE FROM cms.attributedefinition WHERE ID = 'Module.headerTitle';
DELETE FROM cms.attributedefinition WHERE ID = 'Module.headerSubtitle';
DELETE FROM cms.attributedefinition WHERE ID = 'Module.hideViewAllButton';
DELETE FROM cms.attributedefinition WHERE ID = 'Module.hideProductName';
DELETE FROM cms.attributedefinition WHERE ID = 'Module.hideProductPrice';
DELETE FROM cms.attributedefinition WHERE ID = 'Module.hideProductBadge';
DELETE FROM cms.attributedefinition WHERE ID = 'Module.useViewAllPopup';

DELETE FROM cms.contenttype WHERE ID = 'ModuleContainer';
DELETE FROM cms.contenttype WHERE ID = 'ModuleGroup';
DELETE FROM cms.contenttype WHERE ID = 'Module';
