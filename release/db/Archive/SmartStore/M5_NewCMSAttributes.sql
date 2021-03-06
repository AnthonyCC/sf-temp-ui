--
-- Expert Weighting attribute
--
INSERT INTO cms.attributedefinition (NAME,ID,CONTENTTYPE_ID,ATTRIBUTETYPE_CODE,INHERITABLE,REQUIRED,LABEL,CARDINALITY_CODE,LOOKUP_CODE)
VALUES ('SS_EXPERT_WEIGHTING','Product.SS_EXPERT_WEIGHTING','Product','I','T','F','Expert Weighting','One','Product.SS_EXPERT_WEIGHTING');

INSERT INTO cms.lookuptype(CODE,NAME)
VALUES('Product.SS_EXPERT_WEIGHTING','Product.SS_EXPERT_WEIGHTING');

INSERT INTO cms.lookup(LOOKUPTYPE_CODE,CODE,LABEL,DESCRIPTION,ORDINAL)
VALUES('Product.SS_EXPERT_WEIGHTING','-5','-5','-5',1);
INSERT INTO cms.lookup(LOOKUPTYPE_CODE,CODE,LABEL,DESCRIPTION,ORDINAL)
VALUES('Product.SS_EXPERT_WEIGHTING','-4','-4','-4',2);
INSERT INTO cms.lookup(LOOKUPTYPE_CODE,CODE,LABEL,DESCRIPTION,ORDINAL)
VALUES('Product.SS_EXPERT_WEIGHTING','-3','-3','-3',3);
INSERT INTO cms.lookup(LOOKUPTYPE_CODE,CODE,LABEL,DESCRIPTION,ORDINAL)
VALUES('Product.SS_EXPERT_WEIGHTING','-2','-2','-2',4);
INSERT INTO cms.lookup(LOOKUPTYPE_CODE,CODE,LABEL,DESCRIPTION,ORDINAL)
VALUES('Product.SS_EXPERT_WEIGHTING','-1','-1','-1',5);
INSERT INTO cms.lookup(LOOKUPTYPE_CODE,CODE,LABEL,DESCRIPTION,ORDINAL)
VALUES('Product.SS_EXPERT_WEIGHTING','0','0','0',6);
INSERT INTO cms.lookup(LOOKUPTYPE_CODE,CODE,LABEL,DESCRIPTION,ORDINAL)
VALUES('Product.SS_EXPERT_WEIGHTING','1','1','1',7);
INSERT INTO cms.lookup(LOOKUPTYPE_CODE,CODE,LABEL,DESCRIPTION,ORDINAL)
VALUES('Product.SS_EXPERT_WEIGHTING','2','2','2',8);
INSERT INTO cms.lookup(LOOKUPTYPE_CODE,CODE,LABEL,DESCRIPTION,ORDINAL)
VALUES('Product.SS_EXPERT_WEIGHTING','3','3','3',9);
INSERT INTO cms.lookup(LOOKUPTYPE_CODE,CODE,LABEL,DESCRIPTION,ORDINAL)
VALUES('Product.SS_EXPERT_WEIGHTING','4','4','4',10);
INSERT INTO cms.lookup(LOOKUPTYPE_CODE,CODE,LABEL,DESCRIPTION,ORDINAL)
VALUES('Product.SS_EXPERT_WEIGHTING','5','5','5',11);

--
-- Category Level Aggregation attribute
--
INSERT INTO cms.attributedefinition (NAME,ID,CONTENTTYPE_ID,ATTRIBUTETYPE_CODE,INHERITABLE,REQUIRED,LABEL,CARDINALITY_CODE,LOOKUP_CODE)
VALUES ('SS_LEVEL_AGGREGATION','Category.SS_LEVEL_AGGREGATION','Category','B','F','F','Level Aggregation','One',NULL);

