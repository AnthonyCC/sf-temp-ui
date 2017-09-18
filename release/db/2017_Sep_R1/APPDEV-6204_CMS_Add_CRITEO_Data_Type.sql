--1 row inserted
INSERT INTO cms.lookup (LOOKUPTYPE_CODE,CODE,LABEL,DESCRIPTION,ORDINAL) VALUES ('Module.productSourceType','CRITEO','CRITEO','CRITEO',(select max(l.ordinal)+1 from cms.lookup l where L.LOOKUPTYPE_CODE = 'Module.productSourceType'));
