-- add rel Product.PREFERRED_SKU -> Sku

insert into cms.relationshipdefinition (id, contenttype_id, name, navigable, inheritable, required, label, cardinality_code)
values (cms.system_seq.nextval, 'Product', 'PREFERRED_SKU','F', 'F', 'F', 'Preferred SKU', 'One');

insert into cms.relationshipdestination (id, relationshipdefinition_id, contenttype_id)
values (cms.system_seq.nextval, (select id from relationshipdefinition where contenttype_id='Product' and name='PREFERRED_SKU'), 'Sku');

