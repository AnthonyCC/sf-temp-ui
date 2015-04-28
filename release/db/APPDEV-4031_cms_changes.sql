-- change PRIMARY_HOME to to-many relationship
UPDATE cms.relationshipdefinition 
SET CARDINALITY_CODE='Many' 
where NAME='PRIMARY_HOME' 
and CONTENTTYPE_ID='Product';

-- introduce FDX Store content node
insert into cms.CONTENTNODE (ID,CONTENTTYPE_ID) values('Store:FDX', 'Store');

-- preview hostname
INSERT INTO cms.attributedefinition (NAME,ID,CONTENTTYPE_ID,ATTRIBUTETYPE_CODE,INHERITABLE,REQUIRED,LABEL,CARDINALITY_CODE,LOOKUP_CODE)
VALUES ('PREVIEW_HOST_NAME','Store.PREVIEW_HOST_NAME','Store','S','F','F','','One',NULL);

