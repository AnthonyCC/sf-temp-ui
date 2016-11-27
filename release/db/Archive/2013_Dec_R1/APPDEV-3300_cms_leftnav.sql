-- Tag                                                                                                         
INSERT INTO cms.contenttype (ID,NAME,DESCRIPTION,GENERATE_ID) VALUES ('Tag','Tag','Definition of type Tag','F');
INSERT INTO cms.attributedefinition (NAME,ID,CONTENTTYPE_ID,ATTRIBUTETYPE_CODE,INHERITABLE,REQUIRED,LABEL,CARDINALITY_CODE,LOOKUP_CODE) VALUES ('name','Tag.name','Tag','S','F','T','Name','One',NULL);
INSERT INTO cms.relationshipdefinition (NAME,ID,CONTENTTYPE_ID,INHERITABLE,REQUIRED,NAVIGABLE,READONLY,LABEL,CARDINALITY_CODE) VALUES ('children','Tag.children','Tag','F','F','T','F','Children','Many');
INSERT INTO cms.relationshipdestination (ID, RELATIONSHIPDEFINITION_ID,CONTENTTYPE_ID,REVERSE_ATTRIBUTE_NAME,REVERSE_ATTRIBUTE_LABEL) VALUES ('Tag.children.Tag','Tag.children','Tag',NULL,NULL);


-- ProductFilter
INSERT INTO cms.lookuptype (CODE,NAME,DESCRIPTION) values ('ProductFilter.type','ProductFilter.type',null);
INSERT INTO cms.lookup (LOOKUPTYPE_CODE,CODE,LABEL,DESCRIPTION,ORDINAL) values ('ProductFilter.type','AND','AND (Filter Combination)','AND (Filter Combination)',1);
INSERT INTO cms.lookup (LOOKUPTYPE_CODE,CODE,LABEL,DESCRIPTION,ORDINAL) values ('ProductFilter.type','OR','OR (Filter Combination)','OR (Filter Combination)',2);
INSERT INTO cms.lookup (LOOKUPTYPE_CODE,CODE,LABEL,DESCRIPTION,ORDINAL) values ('ProductFilter.type','ALLERGEN','Allergen (ERPSy Flag)','Allergen (ERPSy Flag)',3);
INSERT INTO cms.lookup (LOOKUPTYPE_CODE,CODE,LABEL,DESCRIPTION,ORDINAL) values ('ProductFilter.type','BACK_IN_STOCK','Back In Stock','Back In Stock',4);
INSERT INTO cms.lookup (LOOKUPTYPE_CODE,CODE,LABEL,DESCRIPTION,ORDINAL) values ('ProductFilter.type','BRAND','Brand','Brand',4);
INSERT INTO cms.lookup (LOOKUPTYPE_CODE,CODE,LABEL,DESCRIPTION,ORDINAL) values ('ProductFilter.type','CLAIM','Claim (ERPSy Flag)','Claim (ERPSy Flag)',5);
INSERT INTO cms.lookup (LOOKUPTYPE_CODE,CODE,LABEL,DESCRIPTION,ORDINAL) values ('ProductFilter.type','CUSTOMER_RATING','Customer Rating (Range)','Customer Rating (Range)',6);
INSERT INTO cms.lookup (LOOKUPTYPE_CODE,CODE,LABEL,DESCRIPTION,ORDINAL) values ('ProductFilter.type','DOMAIN_VALUE','Domain Value','Domain Value',7);
INSERT INTO cms.lookup (LOOKUPTYPE_CODE,CODE,LABEL,DESCRIPTION,ORDINAL) values ('ProductFilter.type','EXPERT_RATING','Expert Rating (Range)','Expert Rating (Range)',8);
INSERT INTO cms.lookup (LOOKUPTYPE_CODE,CODE,LABEL,DESCRIPTION,ORDINAL) values ('ProductFilter.type','FRESHNESS','Freshness (Range)','Freshness (Range)',9);
INSERT INTO cms.lookup (LOOKUPTYPE_CODE,CODE,LABEL,DESCRIPTION,ORDINAL) values ('ProductFilter.type','KOSHER','Kosher','Kosher',10);
INSERT INTO cms.lookup (LOOKUPTYPE_CODE,CODE,LABEL,DESCRIPTION,ORDINAL) values ('ProductFilter.type','NEW','New','New',11);
INSERT INTO cms.lookup (LOOKUPTYPE_CODE,CODE,LABEL,DESCRIPTION,ORDINAL) values ('ProductFilter.type','NUTRITION','Nutrition (Range)','Nutrition (Range)',12);
INSERT INTO cms.lookup (LOOKUPTYPE_CODE,CODE,LABEL,DESCRIPTION,ORDINAL) values ('ProductFilter.type','ON_SALE','On Sale','On Sale',13);
INSERT INTO cms.lookup (LOOKUPTYPE_CODE,CODE,LABEL,DESCRIPTION,ORDINAL) values ('ProductFilter.type','PRICE','Price (Range)','Price (Range)',14);
INSERT INTO cms.lookup (LOOKUPTYPE_CODE,CODE,LABEL,DESCRIPTION,ORDINAL) values ('ProductFilter.type','SUSTAINABILITY_RATING','Sustainability Rating (Range)','Sustainability Rating (Range)',15);
INSERT INTO cms.lookup (LOOKUPTYPE_CODE,CODE,LABEL,DESCRIPTION,ORDINAL) values ('ProductFilter.type','TAG','Tag','Tag',16);

INSERT INTO cms.contenttype (ID,NAME,DESCRIPTION,GENERATE_ID) VALUES ('ProductFilter','ProductFilter','Definition of type ProductFilter','F');
INSERT INTO cms.attributedefinition (NAME,ID,CONTENTTYPE_ID,ATTRIBUTETYPE_CODE,INHERITABLE,REQUIRED,LABEL,CARDINALITY_CODE,LOOKUP_CODE) VALUES ('name','ProductFilter.name','ProductFilter','S','F','T','Name','One',NULL);
INSERT INTO cms.attributedefinition (NAME,ID,CONTENTTYPE_ID,ATTRIBUTETYPE_CODE,INHERITABLE,REQUIRED,LABEL,CARDINALITY_CODE,LOOKUP_CODE) values ('type','ProductFilter.type','ProductFilter','S','F','T','Type','One','ProductFilter.type');
INSERT INTO cms.attributedefinition (NAME,ID,CONTENTTYPE_ID,ATTRIBUTETYPE_CODE,INHERITABLE,REQUIRED,LABEL,CARDINALITY_CODE,LOOKUP_CODE) VALUES ('invert','ProductFilter.invert','ProductFilter','B','F','F','Invert','One',NULL);
--range
INSERT INTO cms.attributedefinition (NAME,ID,CONTENTTYPE_ID,ATTRIBUTETYPE_CODE,INHERITABLE,REQUIRED,LABEL,CARDINALITY_CODE,LOOKUP_CODE) VALUES ('fromValue','ProductFilter.fromValue','ProductFilter','D','F','F','From Value','One',NULL);
INSERT INTO cms.attributedefinition (NAME,ID,CONTENTTYPE_ID,ATTRIBUTETYPE_CODE,INHERITABLE,REQUIRED,LABEL,CARDINALITY_CODE,LOOKUP_CODE) VALUES ('toValue','ProductFilter.toValue','ProductFilter','D','F','F','To Value','One',NULL);
INSERT INTO cms.attributedefinition (NAME,ID,CONTENTTYPE_ID,ATTRIBUTETYPE_CODE,INHERITABLE,REQUIRED,LABEL,CARDINALITY_CODE,LOOKUP_CODE) VALUES ('nutritionCode','ProductFilter.nutritionCode','ProductFilter','S','F','F','Nutrition Code (only for Nutrition filter)','One',NULL);
--brand
INSERT INTO cms.relationshipdefinition (NAME,ID,CONTENTTYPE_ID,INHERITABLE,REQUIRED,NAVIGABLE,READONLY,LABEL,CARDINALITY_CODE) VALUES ('brand','ProductFilter.brand','ProductFilter','F','F','F','F','Brand','One');
INSERT INTO cms.relationshipdestination (ID, RELATIONSHIPDEFINITION_ID,CONTENTTYPE_ID,REVERSE_ATTRIBUTE_NAME,REVERSE_ATTRIBUTE_LABEL) VALUES ('ProductFilter.brand.Brand','ProductFilter.brand','Brand',NULL,NULL);
--domainvalue
INSERT INTO cms.relationshipdefinition (NAME,ID,CONTENTTYPE_ID,INHERITABLE,REQUIRED,NAVIGABLE,READONLY,LABEL,CARDINALITY_CODE) VALUES ('domainValue','ProductFilter.domainValue','ProductFilter','F','F','F','F','Domain Value','One');
INSERT INTO cms.relationshipdestination (ID, RELATIONSHIPDEFINITION_ID,CONTENTTYPE_ID,REVERSE_ATTRIBUTE_NAME,REVERSE_ATTRIBUTE_LABEL) VALUES ('ProductFilter.domainValue.DomainValue','ProductFilter.domainValue','DomainValue',NULL,NULL);
--tag
INSERT INTO cms.relationshipdefinition (NAME,ID,CONTENTTYPE_ID,INHERITABLE,REQUIRED,NAVIGABLE,READONLY,LABEL,CARDINALITY_CODE) VALUES ('tag','ProductFilter.tag','ProductFilter','F','F','F','F','Tag','One');
INSERT INTO cms.relationshipdestination (ID, RELATIONSHIPDEFINITION_ID,CONTENTTYPE_ID,REVERSE_ATTRIBUTE_NAME,REVERSE_ATTRIBUTE_LABEL) VALUES ('ProductFilter.tag.Tag','ProductFilter.tag','Tag',NULL,NULL);
--erpsy
INSERT INTO cms.attributedefinition (NAME,ID,CONTENTTYPE_ID,ATTRIBUTETYPE_CODE,INHERITABLE,REQUIRED,LABEL,CARDINALITY_CODE,LOOKUP_CODE) VALUES ('erpsyFlagCode','ProductFilter.erpsyFlagCode','ProductFilter','S','F','F','ERPSy Flag Code','One',NULL);
--filter combination
INSERT INTO cms.relationshipdefinition (NAME,ID,CONTENTTYPE_ID,INHERITABLE,REQUIRED,NAVIGABLE,READONLY,LABEL,CARDINALITY_CODE) VALUES ('filters','ProductFilter.filters','ProductFilter','F','F','F','F','Filters','Many');
INSERT INTO cms.relationshipdestination (ID, RELATIONSHIPDEFINITION_ID,CONTENTTYPE_ID,REVERSE_ATTRIBUTE_NAME,REVERSE_ATTRIBUTE_LABEL) VALUES ('ProductFilter.filters.ProductFilter','ProductFilter.filters','ProductFilter',NULL,NULL);

-- ProductGrabber                                                                                                         
INSERT INTO cms.contenttype (ID,NAME,DESCRIPTION,GENERATE_ID) VALUES ('ProductGrabber','ProductGrabber','Definition of type ProductGrabber','F');
INSERT INTO cms.relationshipdefinition (NAME,ID,CONTENTTYPE_ID,INHERITABLE,REQUIRED,NAVIGABLE,READONLY,LABEL,CARDINALITY_CODE) VALUES ('productFilters','ProductGrabber.productFilters','ProductGrabber','F','F','F','F','Product Filters','Many');
INSERT INTO cms.relationshipdestination (ID, RELATIONSHIPDEFINITION_ID,CONTENTTYPE_ID,REVERSE_ATTRIBUTE_NAME,REVERSE_ATTRIBUTE_LABEL) VALUES ('ProductGrabber.productFilters.ProductFilter','ProductGrabber.productFilters','ProductFilter',NULL,NULL);

INSERT INTO cms.relationshipdefinition (NAME,ID,CONTENTTYPE_ID,INHERITABLE,REQUIRED,NAVIGABLE,READONLY,LABEL,CARDINALITY_CODE) VALUES ('scope','ProductGrabber.scope','ProductGrabber','F','F','F','F','Scope','Many');
INSERT INTO cms.relationshipdestination (ID, RELATIONSHIPDEFINITION_ID,CONTENTTYPE_ID,REVERSE_ATTRIBUTE_NAME,REVERSE_ATTRIBUTE_LABEL) VALUES ('ProductGrabber.scope.Category','ProductGrabber.scope','Category',NULL,NULL);
INSERT INTO cms.relationshipdestination (ID, RELATIONSHIPDEFINITION_ID,CONTENTTYPE_ID,REVERSE_ATTRIBUTE_NAME,REVERSE_ATTRIBUTE_LABEL) VALUES ('ProductGrabber.scope.Department','ProductGrabber.scope','Department',NULL,NULL);


-- ProductFilterGroup lookup                                                                                                        
INSERT INTO cms.lookuptype (CODE,NAME,DESCRIPTION) values ('ProductFilterGroup.type','ProductFilterGroup.type',null);
INSERT INTO cms.lookup (LOOKUPTYPE_CODE,CODE,LABEL,DESCRIPTION,ORDINAL) values ('ProductFilterGroup.type','SINGLE','Single Select','Single Select',1);
INSERT INTO cms.lookup (LOOKUPTYPE_CODE,CODE,LABEL,DESCRIPTION,ORDINAL) values ('ProductFilterGroup.type','POPUP','Popup','Popup',2); 
INSERT INTO cms.lookup (LOOKUPTYPE_CODE,CODE,LABEL,DESCRIPTION,ORDINAL) values ('ProductFilterGroup.type','MULTI','Multi-select (additive)','Multi-select (additive)',3);

-- ProductFilterGroup   
INSERT INTO cms.contenttype (ID,NAME,DESCRIPTION,GENERATE_ID) VALUES ('ProductFilterGroup','ProductFilterGroup','Definition of type ProductFilterGroup','F');
INSERT INTO cms.attributedefinition (NAME,ID,CONTENTTYPE_ID,ATTRIBUTETYPE_CODE,INHERITABLE,REQUIRED,LABEL,CARDINALITY_CODE,LOOKUP_CODE) VALUES ('name','ProductFilterGroup.name','ProductFilterGroup','S','F','T','Name','One',NULL);
INSERT INTO cms.attributedefinition (NAME,ID,CONTENTTYPE_ID,ATTRIBUTETYPE_CODE,INHERITABLE,REQUIRED,LABEL,CARDINALITY_CODE,LOOKUP_CODE) VALUES ('allSelectedLabel','ProductFilterGroup.allSelectedLabel','ProductFilterGroup','S','F','F','All Selected Label','One',NULL);
INSERT INTO cms.attributedefinition (NAME,ID,CONTENTTYPE_ID,ATTRIBUTETYPE_CODE,INHERITABLE,REQUIRED,LABEL,CARDINALITY_CODE,LOOKUP_CODE) VALUES ('displayOnCategoryListingPage','ProductFilterGroup.displayOnCategoryListingPage','ProductFilterGroup','B','F','F','Display On Category Listing Page','One',NULL);
INSERT INTO cms.attributedefinition (NAME,ID,CONTENTTYPE_ID,ATTRIBUTETYPE_CODE,INHERITABLE,REQUIRED,LABEL,CARDINALITY_CODE,LOOKUP_CODE) values ('type','ProductFilterGroup.type','ProductFilterGroup','S','F','T','Type','One','ProductFilterGroup.type');

INSERT INTO cms.relationshipdefinition (NAME,ID,CONTENTTYPE_ID,INHERITABLE,REQUIRED,NAVIGABLE,READONLY,LABEL,CARDINALITY_CODE) VALUES ('productFilters','ProductFilterGroup.productFilters','ProductFilterGroup','F','T','T','F','Product Filters','Many');
INSERT INTO cms.relationshipdestination (ID, RELATIONSHIPDEFINITION_ID,CONTENTTYPE_ID,REVERSE_ATTRIBUTE_NAME,REVERSE_ATTRIBUTE_LABEL) VALUES ('ProductFilterGroup.productFilters.ProductFilter','ProductFilterGroup.productFilters','ProductFilter',NULL,NULL);

-- ProductFilterMultiGroup lookup - must be a sub-set of ProductFilterGroup lookup                                                                                                        
INSERT INTO cms.lookuptype (CODE,NAME,DESCRIPTION) values ('ProductFilterMultiGroup.type','ProductFilterMultiGroup.type',null);
INSERT INTO cms.lookup (LOOKUPTYPE_CODE,CODE,LABEL,DESCRIPTION,ORDINAL) values ('ProductFilterMultiGroup.type','SINGLE','Single Select','Single Select',1);
INSERT INTO cms.lookup (LOOKUPTYPE_CODE,CODE,LABEL,DESCRIPTION,ORDINAL) values ('ProductFilterMultiGroup.type','POPUP','Popup','Popup',2); 


-- ProductFilterMultiGroup                                                                                                         
INSERT INTO cms.contenttype (ID,NAME,DESCRIPTION,GENERATE_ID) VALUES ('ProductFilterMultiGroup','ProductFilterMultiGroup','Definition of type ProductFilterMultiGroup','F');
INSERT INTO cms.attributedefinition (NAME,ID,CONTENTTYPE_ID,ATTRIBUTETYPE_CODE,INHERITABLE,REQUIRED,LABEL,CARDINALITY_CODE,LOOKUP_CODE) VALUES ('level1Name','ProductFilterMultiGroup.level1Name','ProductFilterMultiGroup','S','F','T','Level 1 Name','One',NULL);
INSERT INTO cms.attributedefinition (NAME,ID,CONTENTTYPE_ID,ATTRIBUTETYPE_CODE,INHERITABLE,REQUIRED,LABEL,CARDINALITY_CODE,LOOKUP_CODE) VALUES ('level1AllSelectedLabel','ProductFilterMultiGroup.level1AllSelectedLabel','ProductFilterMultiGroup','S','F','F','Level 1 All Selected Label','One',NULL);
INSERT INTO cms.attributedefinition (NAME,ID,CONTENTTYPE_ID,ATTRIBUTETYPE_CODE,INHERITABLE,REQUIRED,LABEL,CARDINALITY_CODE,LOOKUP_CODE) values ('level1Type','ProductFilterMultiGroup.level1Type','ProductFilterMultiGroup','S','F','T','Level 1 Type','One','ProductFilterMultiGroup.type');

INSERT INTO cms.attributedefinition (NAME,ID,CONTENTTYPE_ID,ATTRIBUTETYPE_CODE,INHERITABLE,REQUIRED,LABEL,CARDINALITY_CODE,LOOKUP_CODE) VALUES ('level2Name','ProductFilterMultiGroup.level2Name','ProductFilterMultiGroup','S','F','F','Level 2 Name','One',NULL);
INSERT INTO cms.attributedefinition (NAME,ID,CONTENTTYPE_ID,ATTRIBUTETYPE_CODE,INHERITABLE,REQUIRED,LABEL,CARDINALITY_CODE,LOOKUP_CODE) VALUES ('level2AllSelectedLabel','ProductFilterMultiGroup.level2AllSelectedLabel','ProductFilterMultiGroup','S','F','F','Level 2 All Selected Label','One',NULL);
INSERT INTO cms.attributedefinition (NAME,ID,CONTENTTYPE_ID,ATTRIBUTETYPE_CODE,INHERITABLE,REQUIRED,LABEL,CARDINALITY_CODE,LOOKUP_CODE) values ('level2Type','ProductFilterMultiGroup.level2Type','ProductFilterMultiGroup','S','F','F','Level 2 Type','One','ProductFilterMultiGroup.type');

INSERT INTO cms.relationshipdefinition (NAME,ID,CONTENTTYPE_ID,INHERITABLE,REQUIRED,NAVIGABLE,READONLY,LABEL,CARDINALITY_CODE) VALUES ('rootTag','ProductFilterMultiGroup.rootTag','ProductFilterMultiGroup','F','T','F','F','Root Tag','One');
INSERT INTO cms.relationshipdestination (ID, RELATIONSHIPDEFINITION_ID,CONTENTTYPE_ID,REVERSE_ATTRIBUTE_NAME,REVERSE_ATTRIBUTE_LABEL) VALUES ('ProductFilterMultiGroup.rootTag.Tag','ProductFilterMultiGroup.rootTag','Tag',NULL,NULL);


-- SortOption lookup                                                                                                        
INSERT INTO cms.lookuptype (CODE,NAME,DESCRIPTION) values ('SortOption.strategy','SortOption.strategy',null);
INSERT INTO cms.lookup (LOOKUPTYPE_CODE,CODE,LABEL,DESCRIPTION,ORDINAL) values ('SortOption.strategy','CUSTOMER_RATING','Customer Rating','Customer Rating',1);
INSERT INTO cms.lookup (LOOKUPTYPE_CODE,CODE,LABEL,DESCRIPTION,ORDINAL) values ('SortOption.strategy','EXPERT_RATING','Expert Rating','Expert Rating',2);
INSERT INTO cms.lookup (LOOKUPTYPE_CODE,CODE,LABEL,DESCRIPTION,ORDINAL) values ('SortOption.strategy','NAME','Name','Name',3);
INSERT INTO cms.lookup (LOOKUPTYPE_CODE,CODE,LABEL,DESCRIPTION,ORDINAL) values ('SortOption.strategy','POPULARITY','Popularity','Popularity',4);
INSERT INTO cms.lookup (LOOKUPTYPE_CODE,CODE,LABEL,DESCRIPTION,ORDINAL) values ('SortOption.strategy','PRICE','Price','Price',5);
INSERT INTO cms.lookup (LOOKUPTYPE_CODE,CODE,LABEL,DESCRIPTION,ORDINAL) values ('SortOption.strategy','SALE','Sale','Sale',6);
INSERT INTO cms.lookup (LOOKUPTYPE_CODE,CODE,LABEL,DESCRIPTION,ORDINAL) values ('SortOption.strategy','SUSTAINABILITY_RATING','Sustainability Rating','Sustainability Rating',7);

-- SortOption                                                                                                         
INSERT INTO cms.contenttype (ID,NAME,DESCRIPTION,GENERATE_ID) VALUES ('SortOption','SortOption','Definition of type SortOption','F');
INSERT INTO cms.attributedefinition (NAME,ID,CONTENTTYPE_ID,ATTRIBUTETYPE_CODE,INHERITABLE,REQUIRED,LABEL,CARDINALITY_CODE,LOOKUP_CODE) VALUES ('label','SortOption.label','SortOption','S','F','T','Label','One',NULL);
INSERT INTO cms.attributedefinition (NAME,ID,CONTENTTYPE_ID,ATTRIBUTETYPE_CODE,INHERITABLE,REQUIRED,LABEL,CARDINALITY_CODE,LOOKUP_CODE) VALUES ('selectedLabel','SortOption.selectedLabel','SortOption','S','F','F','Selected Label','One',NULL);
INSERT INTO cms.attributedefinition (NAME,ID,CONTENTTYPE_ID,ATTRIBUTETYPE_CODE,INHERITABLE,REQUIRED,LABEL,CARDINALITY_CODE,LOOKUP_CODE) VALUES ('selectedLabelReverseOrder','SortOption.selectedLabelReverseOrder','SortOption','S','F','F','Selected Label Reverse Order','One',NULL);
INSERT INTO cms.attributedefinition (NAME,ID,CONTENTTYPE_ID,ATTRIBUTETYPE_CODE,INHERITABLE,REQUIRED,LABEL,CARDINALITY_CODE,LOOKUP_CODE) values ('strategy','SortOption.strategy','SortOption','S','F','T','Strategy','One','SortOption.strategy');

-- Product.browseRecommenderType
INSERT INTO cms.lookuptype (CODE,NAME,DESCRIPTION) values ('Product.browseRecommenderType','Product.browseRecommenderType',null);
INSERT INTO cms.lookup (LOOKUPTYPE_CODE,CODE,LABEL,DESCRIPTION,ORDINAL) values ('Product.browseRecommenderType','NONE','None','None',1);
INSERT INTO cms.lookup (LOOKUPTYPE_CODE,CODE,LABEL,DESCRIPTION,ORDINAL) values ('Product.browseRecommenderType','PDP_XSELL','Cross-sell (first item)','Cross-sell (first item)',2);
INSERT INTO cms.lookup (LOOKUPTYPE_CODE,CODE,LABEL,DESCRIPTION,ORDINAL) values ('Product.browseRecommenderType','PDP_UPSELL','Upsell (first item)','Upsell (first item)',3);

-- Category
INSERT INTO cms.attributedefinition (NAME,ID,CONTENTTYPE_ID,ATTRIBUTETYPE_CODE,INHERITABLE,REQUIRED,LABEL,CARDINALITY_CODE,LOOKUP_CODE) VALUES ('preferenceCategory','Category.preferenceCategory','Category','B','F','F','Preference Category','One',NULL);
INSERT INTO cms.attributedefinition (NAME,ID,CONTENTTYPE_ID,ATTRIBUTETYPE_CODE,INHERITABLE,REQUIRED,LABEL,CARDINALITY_CODE,LOOKUP_CODE) VALUES ('noGroupingByCategory','Category.noGroupingByCategory','Category','B','T','F','Disable Product Grouping By Category','One',NULL);
INSERT INTO cms.attributedefinition (NAME,ID,CONTENTTYPE_ID,ATTRIBUTETYPE_CODE,INHERITABLE,REQUIRED,LABEL,CARDINALITY_CODE,LOOKUP_CODE) VALUES ('hideIfFilteringIsSupported','Category.hideIfFilteringIsSupported','Category','B','F','F','Hide If Filtering Is Supported','One',NULL);
INSERT INTO cms.attributedefinition (NAME,ID,CONTENTTYPE_ID,ATTRIBUTETYPE_CODE,INHERITABLE,REQUIRED,LABEL,CARDINALITY_CODE,LOOKUP_CODE) VALUES ('browseRecommenderType','Category.browseRecommenderType','Category','S','T','F','Browse Recommender Type','One','Product.browseRecommenderType');
INSERT INTO cms.attributedefinition (NAME,ID,CONTENTTYPE_ID,ATTRIBUTETYPE_CODE,INHERITABLE,REQUIRED,LABEL,CARDINALITY_CODE,LOOKUP_CODE) VALUES ('catMerchantRecommenderTitle','Category.catMerchantRecommenderTitle','Category','S','T','F','Title','One',NULL);
INSERT INTO cms.attributedefinition (NAME,ID,CONTENTTYPE_ID,ATTRIBUTETYPE_CODE,INHERITABLE,REQUIRED,LABEL,CARDINALITY_CODE,LOOKUP_CODE) VALUES ('catMerchantRecommenderRandomizeProducts','Category.catMerchantRecommenderRandomizeProducts','Category','B','T','F','Randomize Products','One',NULL);
INSERT INTO cms.relationshipdefinition (NAME,ID,CONTENTTYPE_ID,INHERITABLE,REQUIRED,NAVIGABLE,READONLY,LABEL,CARDINALITY_CODE) VALUES ('productGrabbers','Category.productGrabbers','Category','F','F','T','F','Product Grabbers','Many');
INSERT INTO cms.relationshipdestination (ID, RELATIONSHIPDEFINITION_ID,CONTENTTYPE_ID,REVERSE_ATTRIBUTE_NAME,REVERSE_ATTRIBUTE_LABEL) VALUES ('Category.productGrabbers.ProductGrabber','Category.productGrabbers','ProductGrabber',NULL,NULL);
INSERT INTO cms.relationshipdefinition (NAME,ID,CONTENTTYPE_ID,INHERITABLE,REQUIRED,NAVIGABLE,READONLY,LABEL,CARDINALITY_CODE) VALUES ('productFilterGroups','Category.productFilterGroups','Category','T','F','F','F','Product Filter Groups','Many');
INSERT INTO cms.relationshipdestination (ID, RELATIONSHIPDEFINITION_ID,CONTENTTYPE_ID,REVERSE_ATTRIBUTE_NAME,REVERSE_ATTRIBUTE_LABEL) VALUES ('Category.productFilterGroups.ProductFilterGroup','Category.productFilterGroups','ProductFilterGroup',NULL,NULL);
INSERT INTO cms.relationshipdestination (ID, RELATIONSHIPDEFINITION_ID,CONTENTTYPE_ID,REVERSE_ATTRIBUTE_NAME,REVERSE_ATTRIBUTE_LABEL) VALUES ('Category.productFilterGroups.ProductFilterMultiGroup','Category.productFilterGroups','ProductFilterMultiGroup',NULL,NULL);
INSERT INTO cms.relationshipdefinition (NAME,ID,CONTENTTYPE_ID,INHERITABLE,REQUIRED,NAVIGABLE,READONLY,LABEL,CARDINALITY_CODE) VALUES ('sortOptions','Category.sortOptions','Category','T','F','F','F','Sort Options','Many');
INSERT INTO cms.relationshipdestination (ID, RELATIONSHIPDEFINITION_ID,CONTENTTYPE_ID,REVERSE_ATTRIBUTE_NAME,REVERSE_ATTRIBUTE_LABEL) VALUES ('Category.sortOptions.SortOption','Category.sortOptions','SortOption',NULL,NULL);
INSERT INTO cms.relationshipdefinition (NAME,ID,CONTENTTYPE_ID,INHERITABLE,REQUIRED,NAVIGABLE,READONLY,LABEL,CARDINALITY_CODE) VALUES ('productTags','Category.productTags','Category','T','F','F','F','Tags For Products','Many');
INSERT INTO cms.relationshipdestination (ID, RELATIONSHIPDEFINITION_ID,CONTENTTYPE_ID,REVERSE_ATTRIBUTE_NAME,REVERSE_ATTRIBUTE_LABEL) VALUES ('Category.productTags.Tag','Category.productTags','Tag',NULL,NULL);
INSERT INTO cms.relationshipdefinition (NAME,ID,CONTENTTYPE_ID,INHERITABLE,REQUIRED,NAVIGABLE,READONLY,LABEL,CARDINALITY_CODE) VALUES ('categoryBanner','Category.categoryBanner','Category','T','F','F','F','Category Banner','One');
INSERT INTO cms.relationshipdestination (ID, RELATIONSHIPDEFINITION_ID,CONTENTTYPE_ID,REVERSE_ATTRIBUTE_NAME,REVERSE_ATTRIBUTE_LABEL) VALUES ('Category.categoryBanner.Html','Category.categoryBanner','Html',NULL,NULL);
INSERT INTO cms.relationshipdefinition (NAME,ID,CONTENTTYPE_ID,INHERITABLE,REQUIRED,NAVIGABLE,READONLY,LABEL,CARDINALITY_CODE) VALUES ('description','Category.description','Category','F','F','F','F','Description','One');
INSERT INTO cms.relationshipdestination (ID, RELATIONSHIPDEFINITION_ID,CONTENTTYPE_ID,REVERSE_ATTRIBUTE_NAME,REVERSE_ATTRIBUTE_LABEL) VALUES ('Category.description.Html','Category.description','Html',NULL,NULL);
INSERT INTO cms.relationshipdefinition (NAME,ID,CONTENTTYPE_ID,INHERITABLE,REQUIRED,NAVIGABLE,READONLY,LABEL,CARDINALITY_CODE) VALUES ('nameImage','Category.nameImage','Category','F','F','F','F','Name Image','One');
INSERT INTO cms.relationshipdestination (ID, RELATIONSHIPDEFINITION_ID,CONTENTTYPE_ID,REVERSE_ATTRIBUTE_NAME,REVERSE_ATTRIBUTE_LABEL) VALUES ('Category.nameImage.Image','Category.nameImage','Image',NULL,NULL);
INSERT INTO cms.relationshipdefinition (NAME,ID,CONTENTTYPE_ID,INHERITABLE,REQUIRED,NAVIGABLE,READONLY,LABEL,CARDINALITY_CODE) VALUES ('catMerchantRecommenderProducts','Category.catMerchantRecommenderProducts','Category','T','F','F','F','Products','Many');
INSERT INTO cms.relationshipdestination (ID, RELATIONSHIPDEFINITION_ID,CONTENTTYPE_ID,REVERSE_ATTRIBUTE_NAME,REVERSE_ATTRIBUTE_LABEL) VALUES ('Category.catMerchantRecommenderProducts.Product','Category.catMerchantRecommenderProducts','Product',NULL,NULL);


-- Department
INSERT INTO cms.attributedefinition (NAME,ID,CONTENTTYPE_ID,ATTRIBUTETYPE_CODE,INHERITABLE,REQUIRED,LABEL,CARDINALITY_CODE,LOOKUP_CODE) VALUES ('noGroupingByCategory','Department.noGroupingByCategory','Department','B','T','F','Disable Product Grouping By Category','One',NULL);
INSERT INTO cms.attributedefinition (NAME,ID,CONTENTTYPE_ID,ATTRIBUTETYPE_CODE,INHERITABLE,REQUIRED,LABEL,CARDINALITY_CODE,LOOKUP_CODE) VALUES ('browseRecommenderType','Department.browseRecommenderType','Department','S','T','F','Browse Recommender Type','One','Product.browseRecommenderType');
INSERT INTO cms.attributedefinition (NAME,ID,CONTENTTYPE_ID,ATTRIBUTETYPE_CODE,INHERITABLE,REQUIRED,LABEL,CARDINALITY_CODE,LOOKUP_CODE) VALUES ('featuredRecommenderTitle','Department.featuredRecommenderTitle','Department','S','F','F','Title','One',NULL);
INSERT INTO cms.attributedefinition (NAME,ID,CONTENTTYPE_ID,ATTRIBUTETYPE_CODE,INHERITABLE,REQUIRED,LABEL,CARDINALITY_CODE,LOOKUP_CODE) VALUES ('featuredRecommenderRandomizeProducts','Department.featuredRecommenderRandomizeProducts','Department','B','F','F','Randomize Products (only if Source Category is set)','One',NULL);
INSERT INTO cms.attributedefinition (NAME,ID,CONTENTTYPE_ID,ATTRIBUTETYPE_CODE,INHERITABLE,REQUIRED,LABEL,CARDINALITY_CODE,LOOKUP_CODE) VALUES ('featuredRecommenderSiteFeature','Department.featuredRecommenderSiteFeature','Department','S','F','F','Site Feature (ignored if Source Category is set)','One',NULL);
INSERT INTO cms.attributedefinition (NAME,ID,CONTENTTYPE_ID,ATTRIBUTETYPE_CODE,INHERITABLE,REQUIRED,LABEL,CARDINALITY_CODE,LOOKUP_CODE) VALUES ('merchantRecommenderTitle','Department.merchantRecommenderTitle','Department','S','F','F','Title','One',NULL);
INSERT INTO cms.attributedefinition (NAME,ID,CONTENTTYPE_ID,ATTRIBUTETYPE_CODE,INHERITABLE,REQUIRED,LABEL,CARDINALITY_CODE,LOOKUP_CODE) VALUES ('merchantRecommenderRandomizeProducts','Department.merchantRecommenderRandomizeProducts','Department','B','F','F','Randomize Products','One',NULL);
INSERT INTO cms.relationshipdefinition (NAME,ID,CONTENTTYPE_ID,INHERITABLE,REQUIRED,NAVIGABLE,READONLY,LABEL,CARDINALITY_CODE) VALUES ('productFilterGroups','Department.productFilterGroups','Department','T','F','F','F','Product Filter Groups','Many');
INSERT INTO cms.relationshipdestination (ID, RELATIONSHIPDEFINITION_ID,CONTENTTYPE_ID,REVERSE_ATTRIBUTE_NAME,REVERSE_ATTRIBUTE_LABEL) VALUES ('Department.productFilterGroups.ProductFilterGroup','Department.productFilterGroups','ProductFilterGroup',NULL,NULL);
INSERT INTO cms.relationshipdestination (ID, RELATIONSHIPDEFINITION_ID,CONTENTTYPE_ID,REVERSE_ATTRIBUTE_NAME,REVERSE_ATTRIBUTE_LABEL) VALUES ('Department.productFilterGroups.ProductFilterMultiGroup','Department.productFilterGroups','ProductFilterMultiGroup',NULL,NULL);
INSERT INTO cms.relationshipdefinition (NAME,ID,CONTENTTYPE_ID,INHERITABLE,REQUIRED,NAVIGABLE,READONLY,LABEL,CARDINALITY_CODE) VALUES ('sortOptions','Department.sortOptions','Department','T','F','F','F','Sort Options','Many');
INSERT INTO cms.relationshipdestination (ID, RELATIONSHIPDEFINITION_ID,CONTENTTYPE_ID,REVERSE_ATTRIBUTE_NAME,REVERSE_ATTRIBUTE_LABEL) VALUES ('Department.sortOptions.SortOption','Department.sortOptions','SortOption',NULL,NULL);
INSERT INTO cms.relationshipdefinition (NAME,ID,CONTENTTYPE_ID,INHERITABLE,REQUIRED,NAVIGABLE,READONLY,LABEL,CARDINALITY_CODE) VALUES ('popularCategories','Department.popularCategories','Department','F','F','F','F','Popular Categories','Many');
INSERT INTO cms.relationshipdestination (ID, RELATIONSHIPDEFINITION_ID,CONTENTTYPE_ID,REVERSE_ATTRIBUTE_NAME,REVERSE_ATTRIBUTE_LABEL) VALUES ('Department.popularCategories.Category','Department.popularCategories','Category',NULL,NULL);
INSERT INTO cms.relationshipdefinition (NAME,ID,CONTENTTYPE_ID,INHERITABLE,REQUIRED,NAVIGABLE,READONLY,LABEL,CARDINALITY_CODE) VALUES ('titleBar','Department.titleBar','Department','F','F','F','F','Title Bar','One');
INSERT INTO cms.relationshipdestination (ID, RELATIONSHIPDEFINITION_ID,CONTENTTYPE_ID,REVERSE_ATTRIBUTE_NAME,REVERSE_ATTRIBUTE_LABEL) VALUES ('Department.titleBar.Image','Department.titleBar','Image',NULL,NULL);
INSERT INTO cms.relationshipdefinition (NAME,ID,CONTENTTYPE_ID,INHERITABLE,REQUIRED,NAVIGABLE,READONLY,LABEL,CARDINALITY_CODE) VALUES ('productTags','Department.productTags','Department','T','F','F','F','Tags For Products','Many');
INSERT INTO cms.relationshipdestination (ID, RELATIONSHIPDEFINITION_ID,CONTENTTYPE_ID,REVERSE_ATTRIBUTE_NAME,REVERSE_ATTRIBUTE_LABEL) VALUES ('Department.productTags.Tag','Department.productTags','Tag',NULL,NULL);
INSERT INTO cms.relationshipdefinition (NAME,ID,CONTENTTYPE_ID,INHERITABLE,REQUIRED,NAVIGABLE,READONLY,LABEL,CARDINALITY_CODE) VALUES ('categoryBanner','Department.categoryBanner','Department','T','F','F','F','Category Banner','One');
INSERT INTO cms.relationshipdestination (ID, RELATIONSHIPDEFINITION_ID,CONTENTTYPE_ID,REVERSE_ATTRIBUTE_NAME,REVERSE_ATTRIBUTE_LABEL) VALUES ('Department.categoryBanner.Html','Department.categoryBanner','Html',NULL,NULL);
INSERT INTO cms.relationshipdefinition (NAME,ID,CONTENTTYPE_ID,INHERITABLE,REQUIRED,NAVIGABLE,READONLY,LABEL,CARDINALITY_CODE) VALUES ('departmentBanner','Department.departmentBanner','Department','F','F','F','F','Department Banner','One');
INSERT INTO cms.relationshipdestination (ID, RELATIONSHIPDEFINITION_ID,CONTENTTYPE_ID,REVERSE_ATTRIBUTE_NAME,REVERSE_ATTRIBUTE_LABEL) VALUES ('Department.departmentBanner.Html','Department.departmentBanner','Html',NULL,NULL);
INSERT INTO cms.relationshipdefinition (NAME,ID,CONTENTTYPE_ID,INHERITABLE,REQUIRED,NAVIGABLE,READONLY,LABEL,CARDINALITY_CODE) VALUES ('featuredRecommenderSourceCategory','Department.featuredRecommenderSourceCategory','Department','F','F','F','F','Source Category','One');
INSERT INTO cms.relationshipdestination (ID, RELATIONSHIPDEFINITION_ID,CONTENTTYPE_ID,REVERSE_ATTRIBUTE_NAME,REVERSE_ATTRIBUTE_LABEL) VALUES ('Department.featuredRecommenderSourceCategory.Category','Department.featuredRecommenderSourceCategory','Category',NULL,NULL);
INSERT INTO cms.relationshipdefinition (NAME,ID,CONTENTTYPE_ID,INHERITABLE,REQUIRED,NAVIGABLE,READONLY,LABEL,CARDINALITY_CODE) VALUES ('merchantRecommenderProducts','Department.merchantRecommenderProducts','Department','F','F','F','F','Products','Many');
INSERT INTO cms.relationshipdestination (ID, RELATIONSHIPDEFINITION_ID,CONTENTTYPE_ID,REVERSE_ATTRIBUTE_NAME,REVERSE_ATTRIBUTE_LABEL) VALUES ('Department.merchantRecommenderProducts.Product','Department.merchantRecommenderProducts','Product',NULL,NULL);

-- Product
INSERT INTO cms.attributedefinition (NAME,ID,CONTENTTYPE_ID,ATTRIBUTETYPE_CODE,INHERITABLE,REQUIRED,LABEL,CARDINALITY_CODE,LOOKUP_CODE) VALUES ('browseRecommenderType','Product.browseRecommenderType','Product','S','T','F','Browse Recommender Type','One','Product.browseRecommenderType');
INSERT INTO cms.relationshipdefinition (NAME,ID,CONTENTTYPE_ID,INHERITABLE,REQUIRED,NAVIGABLE,READONLY,LABEL,CARDINALITY_CODE) VALUES ('tags','Product.tags','Product','F','F','F','F','Tags','Many');
INSERT INTO cms.relationshipdestination (ID, RELATIONSHIPDEFINITION_ID,CONTENTTYPE_ID,REVERSE_ATTRIBUTE_NAME,REVERSE_ATTRIBUTE_LABEL) VALUES ('Product.tags.Tag','Product.tags','Tag',NULL,NULL);


-- FDFolder
INSERT INTO cms.relationshipdestination (ID, RELATIONSHIPDEFINITION_ID,CONTENTTYPE_ID,REVERSE_ATTRIBUTE_NAME,REVERSE_ATTRIBUTE_LABEL) VALUES ('FDFolder.children.ProductFilterGroup','FDFolder.children','ProductFilterGroup',NULL,NULL);
INSERT INTO cms.relationshipdestination (ID, RELATIONSHIPDEFINITION_ID,CONTENTTYPE_ID,REVERSE_ATTRIBUTE_NAME,REVERSE_ATTRIBUTE_LABEL) VALUES ('FDFolder.children.ProductFilterMultiGroup','FDFolder.children','ProductFilterMultiGroup',NULL,NULL);
INSERT INTO cms.relationshipdestination (ID, RELATIONSHIPDEFINITION_ID,CONTENTTYPE_ID,REVERSE_ATTRIBUTE_NAME,REVERSE_ATTRIBUTE_LABEL) VALUES ('FDFolder.children.ProductFilter','FDFolder.children','ProductFilter',NULL,NULL);
INSERT INTO cms.relationshipdestination (ID, RELATIONSHIPDEFINITION_ID,CONTENTTYPE_ID,REVERSE_ATTRIBUTE_NAME,REVERSE_ATTRIBUTE_LABEL) VALUES ('FDFolder.children.Tag','FDFolder.children','Tag',NULL,NULL);
INSERT INTO cms.relationshipdestination (ID, RELATIONSHIPDEFINITION_ID,CONTENTTYPE_ID,REVERSE_ATTRIBUTE_NAME,REVERSE_ATTRIBUTE_LABEL) VALUES ('FDFolder.children.SortOption','FDFolder.children','SortOption',NULL,NULL);
