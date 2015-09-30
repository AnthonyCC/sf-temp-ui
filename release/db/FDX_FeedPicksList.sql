alter table lookuptype add (LOOKUP_SOURCE varchar2(8), QUERY varchar2(200));

insert into contenttype values ('Section', 'Section', 'Definition of type Section', 'F');
insert into contenttype values ('TextComponent', 'TextComponent', 'Definition of type TextComponent', 'F');
insert into contenttype values ('ImageBanner', 'ImageBanner', 'Definition of type ImageBanner', 'F');
insert into contenttype values ('Anchor', 'Anchor', 'Definition of type Anchor', 'F');
insert into contenttype values ('Schedule', 'Schedule', 'Definition of type Schedule', 'T');
insert into contenttype values ('PickList', 'PickList', 'Definition of type PickList', 'F');
insert into contenttype values ('PickListItem', 'PickListItem', 'Definition of type PickListItem', 'T');

insert into lookuptype values ('WebPage.Type','WebPage.Type','Type of WebPages', null, null);
insert into attributedefinition values ('WebPageType','WebPage.Type','WebPage','S','F','F','WebPage Type','One','WebPage.Type');

insert into lookup values ('WebPage.Type','Feed','Feed','Feed for FDX',1);
insert into lookup values ('WebPage.Type','TodaysPick','TodaysPick','Todays Pick for FDX',2);


insert into relationshipdefinition values('WebPageSection','WebPage.Section','WebPage','F','F','T','Sections','Many','F');
insert into relationshipdestination values('WebPage.Section', 'Section', 'WebPage.Section.Section', null, null);

insert into relationshipdefinition values ('WebPageSchedule',	'WebPage.Schedule',	'WebPage',	'F',	'F',	'T',	'Schedule',	'Many',	'F');
insert into relationshipdestination values ('WebPage.Schedule',	'Schedule',	'WebPage.Schedule.Schedule',null,null	);



insert into lookuptype values ('Section.Type','Section.Type','Type of Sections', null, null);

insert into lookup values ('Section.Type','Greeting','Greeting','Greeting in Mobile Feed',1);
insert into lookup values ('Section.Type','Trending','Trending','Trending Section in Mobile Feed',2);


insert into relationshipdefinition values('SectionComponent','Section.Component','Section','F','F','T','Components','Many','F');
insert into attributedefinition values ('Type','Section.Type','Section','S','F','F','Type','One','Section.Type');

insert into relationshipdestination values('Section.Component', 'TextComponent', 'Section.Component.TextComponent', null, null);
insert into relationshipdestination values('Section.Component', 'Anchor', 'Section.Component.Anchor', null, null);
insert into relationshipdestination values('Section.Component', 'ImageBanner', 'Section.Component.ImageBanner', null, null);
insert into relationshipdestination values('Section.Component', 'PickList', 'Section.Component.PickList', null, null);
insert into relationshipdestination values('Section.Component',	'Product',	'Section.Component.Product', null,null);		
insert into relationshipdestination values('Section.Component',	'Department',	'Section.Component.Department', null,null);		
insert into relationshipdestination values('Section.Component',	'Category',	'Section.Component.Category', null,null);		


insert into attributedefinition values ('Text','TextComponent.Text','TextComponent','WY','F','F','Text','One',null);
insert into attributedefinition values ('Type','TextComponent.Type','TextComponent','S','F','F','Type','One',null);


insert into attributedefinition values ('Url','Anchor.Url','Anchor','S','F','F','URL','One',null);
insert into attributedefinition values ('Text','Anchor.Text','Anchor','S','F','F','Text','One',null);
insert into attributedefinition values ('Type','Anchor.Type','Anchor','S','F','F','Type','One',null);

insert into relationshipdefinition values('Target','Anchor.Target','Anchor','F','F','T','Target','One','F');
insert into relationshipdestination values('Anchor.Target', 'PickList', 'Anchor.PickList.AnchorTarget', null, null);
insert into relationshipdestination values('Anchor.Target', 'Product', 'Anchor.Product.AnchorTarget', null, null);
insert into relationshipdestination values('Anchor.Target', 'Category', 'Anchor.Category.AnchorTarget', null, null);
insert into relationshipdestination values('Anchor.Target', 'Department', 'Anchor.Department.AnchorTarget', null, null);

insert into attributedefinition values ('Type','ImageBanner.Type','ImageBanner','S','F','F','Type','One',null);
insert into attributedefinition values ('Description','ImageBanner.Description','ImageBanner','WY','F','F','Type','One',null);

insert into relationshipdefinition values('Target','ImageBanner.Target','ImageBanner','F','F','T','Target','One','F');
insert into relationshipdefinition values('ImageBannerImage','ImageBanner.Image','ImageBanner','F','F','T','Image','One','F');
insert into relationshipdefinition values('ImageBannerLink','ImageBanner.Link','ImageBanner','F','F','T','Links','Many','F');


insert into relationshipdestination values('ImageBanner.Image', 'Image', 'ImageBanner.Image.ImageBanner', null, null);
insert into relationshipdestination values('ImageBanner.Link', 'Anchor', 'ImageBanner.Link.ImageBanner', null, null);
insert into relationshipdestination values('ImageBanner.Target', 'PickList', 'ImageBanner.PickList.ImageBannerTarget', null, null);
insert into relationshipdestination values('ImageBanner.Target', 'Product', 'ImageBanner.Product.ImageBannerTarget', null, null);
insert into relationshipdestination values('ImageBanner.Target', 'Category', 'ImageBanner.Category.ImageBannerTarget', null, null);
insert into relationshipdestination values('ImageBanner.Target', 'Department', 'ImageBanner.Department.ImageBannerTarget', null, null);

insert into lookuptype type values ('Schedule.Day', 'Schedule.Day', 'Schedule Day', null, null);

insert into attributedefinition values ('Day','Schedule.Day','Schedule','S','F','F','Day','One','Schedule.Day');
insert into attributedefinition values ('StartDate','Schedule.StartDate','Schedule','DT','F','F','Start Date','One',null);
insert into attributedefinition values ('StartTime','Schedule.StartTime','Schedule','TS','F','F','Start Time','One',null);
insert into attributedefinition values ('EndDate','Schedule.EndDate','Schedule','DT','F','F','End Date','One',null);
insert into attributedefinition values ('EndTime','Schedule.EndTime','Schedule','TS','F','F','End Time','One',null);


insert into lookup values('Schedule.Day','AllDay','All Day','All Day',1);
insert into lookup values('Schedule.Day','Sunday','Sunday','Sunday',2);
insert into lookup values('Schedule.Day','Monday','Monday','Monday',3);
insert into lookup values('Schedule.Day','Tuesday','Tuesday','Tuesday',4);
insert into lookup values('Schedule.Day','Wednesday','Wednesday','Wednesday',5);
insert into lookup values('Schedule.Day','Thursday','Thursday','Thursday',6);
insert into lookup values('Schedule.Day','Friday','Friday','Friday',7);
insert into lookup values('Schedule.Day','Saturday','Saturday','Saturday',8);

insert into lookuptype type values ('PickList.PricingZone', 'PickList.PricingZone', 'Pricing Zone', null, null);
insert into lookuptype type values ('PickList.DistributionChannel', 'PickList.DistributionChannel', 'Distribution Channel', null, null);
insert into lookuptype type values ('PickList.SalesOrganization', 'PickList.SalesOrganization', 'Sales Organization', null, null);
insert into lookuptype type values ('PickList.Type', 'PickList.Type', 'Type', null, null);

insert into attributedefinition values ('Name'	,'PickList.Name'	,'PickList'	,'S',	'F',	'F',	'Name'	,'One', null);	
insert into attributedefinition values ('PricingZone',	'PickList.pricingZone',	'PickList',	'S',	'F',	'F',	'Pricing Zone',	'One',	'PickList.PricingZone');
insert into attributedefinition values ('DistributionChannel',	'PickList.distributionChannel',	'PickList'	,'S',	'F',	'F',	'Distribution Channel',	'One'	,'PickList.DistributionChannel');
insert into attributedefinition values ('SalesOrganization',	'PickList.salesOrganization',	'PickList',	'S',	'F',	'F',	'Sales Organization',	'One',	'PickList.SalesOrganization');
insert into attributedefinition values ('DisplayName',	'PickList.DisplayName'	,'PickList',	'B',	'F',	'F',	'Display Name',	'One', null);	
insert into attributedefinition values ('Description'	,'PickList.Description'	,'PickList'	,'S',	'F',	'F',	'Description'	,'One', null);	
insert into attributedefinition values ('Type',	'PickList.Type',	'PickList',	'S'	,'F',	'F',	'Type',	'One',	'PickList.Type');

insert into relationshipdefinition values('PickListSchedule', 'PickList.PickListSchedule', 'PickList', 'F','F','T','Schedule', 'Many','F');
insert into relationshipdestination values('PickList.PickListSchedule', 'Schedule', 'PickList.PickList.PickListSchedule', null, null);

insert into relationshipdefinition values('PickListPickList', 'PickList.PickListPickList', 'PickList', 'F','F','T','Pick List', 'Many','F');
insert into relationshipdestination values('PickList.PickListPickList', 'PickList', 'PickList.PickList.PickListPickList', null, null);

insert into relationshipdefinition values('PickListPickListItem', 'PickList.PickListPickListItem', 'PickList', 'F','F','T','Items', 'Many','F');
insert into relationshipdestination values('PickList.PickListPickListItem', 'PickListItem', 'PickList.PickList.PickListPickListItem', null, null);

insert into relationshipdefinition values('PickListMedia', 'PickList.Media', 'PickList', 'F','F','T','Media', 'One','F');
insert into relationshipdestination values('PickList.Media', 'ImageBanner', 'PickList.PickList.BannerImage', null, null);

insert into attributedefinition values ('Default','PickListItem.Default','PickListItem','B','F','F','Default','One',null);
insert into relationshipdefinition values('PickListItemProduct', 'PickListItem.PickListItemProduct', 'PickListItem', 'F','F','N','Pick List Item Product', 'One','F');
insert into RELATIONSHIPDESTINATION values('PickListItem.PickListItemProduct', 'Product', 'FDFolder.PickListItemProduct.Product', null,null);

insert into contentnode values('FDFolder:Feed_fdx','FDFolder');
insert into contentnode values('FDFolder:WCMS_fdx','FDFolder');
insert into contentnode values('FDFolder:Page_fdx','FDFolder');
insert into contentnode values('FDFolder:Section_fdx','FDFolder');
insert into contentnode values('FDFolder:ImageBanner_fdx','FDFolder');
insert into contentnode values('FDFolder:Anchor_fdx','FDFolder');
insert into contentnode values('FDFolder:Schedule_fdx','FDFolder');
insert into contentnode values('FDFolder:PickList_fdx','FDFolder');
insert into contentnode values('FDFolder:TextComponent_fdx','FDFolder');

insert into relationshipdestination values('FDFolder.children', 'Anchor', 'FDFolder.children.Anchor', null, null);
insert into relationshipdestination values('FDFolder.children', 'Schedule', 'FDFolder.children.Schedule', null, null);
insert into relationshipdestination values('FDFolder.children', 'PickList', 'FDFolder.children.PickList', null, null);
insert into relationshipdestination values('FDFolder.children', 'ImageBanner', 'FDFolder.children.ImageBanner', null, null);
insert into relationshipdestination values('FDFolder.children', 'Section', 'FDFolder.children.Section', null, null);
insert into relationshipdestination values('FDFolder.children', 'TextComponent', 'FDFolder.children.TextComponent', null, null);

insert into relationship values('Store:FDX','0', SYSTEM_SEQ.nextval, 'folders', 'FDFolder', 'FDFolder:WCMS_fdx');
insert into relationship values('Store:FDX','0', SYSTEM_SEQ.nextval, 'folders', 'FDFolder', 'FDFolder:PickList_fdx');
insert into relationship values('Store:FDX','0', SYSTEM_SEQ.nextval, 'folders', 'FDFolder', 'FDFolder:Feed_fdx');
insert into relationship values('FDFolder:WCMS_fdx','0', SYSTEM_SEQ.nextval, 'children', 'FDFolder', 'FDFolder:Page_fdx');
insert into relationship values('FDFolder:WCMS_fdx','0', SYSTEM_SEQ.nextval, 'children', 'FDFolder', 'FDFolder:Section_fdx');
insert into relationship values('FDFolder:WCMS_fdx','0', SYSTEM_SEQ.nextval, 'children', 'FDFolder', 'FDFolder:Anchor_fdx');
insert into relationship values('FDFolder:WCMS_fdx','0', SYSTEM_SEQ.nextval, 'children', 'FDFolder', 'FDFolder:TextComponent_fdx');
insert into relationship values('FDFolder:WCMS_fdx','0', SYSTEM_SEQ.nextval, 'children', 'FDFolder', 'FDFolder:ImageBanner_fdx');
insert into relationship values('FDFolder:WCMS_fdx','0', SYSTEM_SEQ.nextval, 'children', 'FDFolder', 'FDFolder:Schedule_fdx');
insert into relationship values('FDFolder:Page_fdx','0', SYSTEM_SEQ.nextval, 'children', 'FDFolder', 'FDFolder:Feed_fdx');