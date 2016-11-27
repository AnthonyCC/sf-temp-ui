-- Store
-- searchPageSortOptions
INSERT INTO cms.relationshipdefinition (NAME,ID,CONTENTTYPE_ID,INHERITABLE,REQUIRED,NAVIGABLE,READONLY,LABEL,CARDINALITY_CODE) VALUES ('searchPageSortOptions','Store.searchPageSortOptions','Store','F','F','F','F','Search Page Sort Options','Many');
INSERT INTO cms.relationshipdestination (ID, RELATIONSHIPDEFINITION_ID,CONTENTTYPE_ID,REVERSE_ATTRIBUTE_NAME,REVERSE_ATTRIBUTE_LABEL) VALUES ('Store.searchPageSortOptions.SortOption','Store.searchPageSortOptions','SortOption',NULL,NULL);
-- newProductsPageSortOptions
INSERT INTO cms.relationshipdefinition (NAME,ID,CONTENTTYPE_ID,INHERITABLE,REQUIRED,NAVIGABLE,READONLY,LABEL,CARDINALITY_CODE) VALUES ('newProductsPageSortOptions','Store.newProductsPageSortOptions','Store','F','F','F','F','New Products Page Sort Options','Many');
INSERT INTO cms.relationshipdestination (ID, RELATIONSHIPDEFINITION_ID,CONTENTTYPE_ID,REVERSE_ATTRIBUTE_NAME,REVERSE_ATTRIBUTE_LABEL) VALUES ('Store.newProductsPageSortOptions.SortOption','Store.newProductsPageSortOptions','SortOption',NULL,NULL);
-- presidentsPicksPageSortOptions
INSERT INTO cms.relationshipdefinition (NAME,ID,CONTENTTYPE_ID,INHERITABLE,REQUIRED,NAVIGABLE,READONLY,LABEL,CARDINALITY_CODE) VALUES ('presidentsPicksPageSortOptions','Store.presidentsPicksPageSortOptions','Store','F','F','F','F','President''s Picks Page Sort Options','Many');
INSERT INTO cms.relationshipdestination (ID, RELATIONSHIPDEFINITION_ID,CONTENTTYPE_ID,REVERSE_ATTRIBUTE_NAME,REVERSE_ATTRIBUTE_LABEL) VALUES ('Store.presidentsPicksPageSortOptions.SortOption','Store.presidentsPicksPageSortOptions','SortOption',NULL,NULL);
-- eCouponsPageSortOptions
INSERT INTO cms.relationshipdefinition (NAME,ID,CONTENTTYPE_ID,INHERITABLE,REQUIRED,NAVIGABLE,READONLY,LABEL,CARDINALITY_CODE) VALUES ('eCouponsPageSortOptions','Store.eCouponsPageSortOptions','Store','F','F','F','F','e-Coupons Page Sort Options','Many');
INSERT INTO cms.relationshipdestination (ID, RELATIONSHIPDEFINITION_ID,CONTENTTYPE_ID,REVERSE_ATTRIBUTE_NAME,REVERSE_ATTRIBUTE_LABEL) VALUES ('Store.eCouponsPageSortOptions.SortOption','Store.eCouponsPageSortOptions','SortOption',NULL,NULL);


-- SortOption lookup                                                                                                        
INSERT INTO cms.lookup (LOOKUPTYPE_CODE,CODE,LABEL,DESCRIPTION,ORDINAL) values ('SortOption.strategy','DEPARTMENT','Department','Department',8);
INSERT INTO cms.lookup (LOOKUPTYPE_CODE,CODE,LABEL,DESCRIPTION,ORDINAL) values ('SortOption.strategy','E_COUPON_DOLLAR_DISCOUNT','e-Coupon Dollar Discount','e-Coupon Dollar Discount',9);
INSERT INTO cms.lookup (LOOKUPTYPE_CODE,CODE,LABEL,DESCRIPTION,ORDINAL) values ('SortOption.strategy','E_COUPON_EXPIRATION_DATE','e-Coupon Expiration Date','e-Coupon Expiration Date',10);
INSERT INTO cms.lookup (LOOKUPTYPE_CODE,CODE,LABEL,DESCRIPTION,ORDINAL) values ('SortOption.strategy','E_COUPON_PERCENT_DISCOUNT','e-Coupon Percent Discount','e-Coupon Percent Discount',11);
INSERT INTO cms.lookup (LOOKUPTYPE_CODE,CODE,LABEL,DESCRIPTION,ORDINAL) values ('SortOption.strategy','E_COUPON_POPULARITY','e-Coupon Popularity','e-Coupon Popularity',12);
INSERT INTO cms.lookup (LOOKUPTYPE_CODE,CODE,LABEL,DESCRIPTION,ORDINAL) values ('SortOption.strategy','E_COUPON_START_DATE','e-Coupon Start Date','e-Coupon Start Date',13);
INSERT INTO cms.lookup (LOOKUPTYPE_CODE,CODE,LABEL,DESCRIPTION,ORDINAL) values ('SortOption.strategy','RECENCY','Recency','Recency',14);
INSERT INTO cms.lookup (LOOKUPTYPE_CODE,CODE,LABEL,DESCRIPTION,ORDINAL) values ('SortOption.strategy','SEARCH_RELEVANCY','Search Relevancy','Search Relevancy',15);
