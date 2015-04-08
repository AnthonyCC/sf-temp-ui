-- Category
INSERT INTO cms.attributedefinition (NAME,ID,CONTENTTYPE_ID,ATTRIBUTETYPE_CODE,INHERITABLE,REQUIRED,LABEL,CARDINALITY_CODE,LOOKUP_CODE) VALUES ('showAllByDefault','Category.showAllByDefault','Category','B','T','F','Show All By Default','One',NULL);

-- Department
INSERT INTO cms.attributedefinition (NAME,ID,CONTENTTYPE_ID,ATTRIBUTETYPE_CODE,INHERITABLE,REQUIRED,LABEL,CARDINALITY_CODE,LOOKUP_CODE) VALUES ('showAllByDefault','Department.showAllByDefault','Department','B','T','F','Show All By Default','One',NULL);

-- ProductFilter
INSERT INTO cms.lookup (LOOKUPTYPE_CODE,CODE,LABEL,DESCRIPTION,ORDINAL) values ('ProductFilter.type','ORGANIC','Organic (ERPSy Flag)','Organic (ERPSy Flag)',14);
UPDATE cms.lookup set ORDINAL=15 WHERE LOOKUPTYPE_CODE='ProductFilter.type' AND CODE='PRICE';                 --orinially ordinal was 14
UPDATE cms.lookup set ORDINAL=16 WHERE LOOKUPTYPE_CODE='ProductFilter.type' AND CODE='SUSTAINABILITY_RATING'; --orinially ordinal was 15
UPDATE cms.lookup set ORDINAL=17 WHERE LOOKUPTYPE_CODE='ProductFilter.type' AND CODE='TAG';                   --orinially ordinal was 16
