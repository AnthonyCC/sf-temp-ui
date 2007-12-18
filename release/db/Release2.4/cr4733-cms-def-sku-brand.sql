-- add rel Sku.Brands -> Brand

insert into cms.relationshipdefinition (id, contenttype_id, name, navigable, inheritable, required, label, cardinality_code)
values (cms.system_seq.nextval, 'Sku', 'brands','F', 'F', 'F', 'Brands', 'Many');

insert into cms.relationshipdestination (id, relationshipdefinition_id, contenttype_id)
values (cms.system_seq.nextval, (select id from relationshipdefinition where contenttype_id='Sku' and name='brands'), 'Brand');
