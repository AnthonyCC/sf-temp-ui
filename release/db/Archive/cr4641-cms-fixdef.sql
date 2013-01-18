update cms.relationshipdestination
set contenttype_id='Image'
where relationshipdefinition_id=(
 select id from cms.relationshipdefinition
 where contenttype_id='ComponentGroup' and name='HEADER_IMAGE' 
);
