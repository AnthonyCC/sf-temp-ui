-- remove FDX Store node
delete from cms.contentnode where id='Store:FDX';

-- revert PRIMARY_HOME relationship cardinality
UPDATE cms.relationshipdefinition 
SET CARDINALITY_CODE='One' 
where NAME='PRIMARY_HOME' 
and CONTENTTYPE_ID='Product';

-- remove preview host name attribute from store node
delete from cms.attributedefinition where id='Store.PREVIEW_HOST_NAME'; 
