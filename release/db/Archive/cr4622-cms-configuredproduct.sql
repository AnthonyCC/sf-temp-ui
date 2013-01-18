-- types

insert into cms.contenttype(id, name, description)
values ('ConfiguredProduct', 'ConfiguredProduct', 'definition of ConfiguredProduct ContentType');

-- attributes

insert into cms.attributedefinition (id, contenttype_id, name, attributetype_code, inheritable, required, label, cardinality_code)
values (cms.system_seq.nextval, 'ConfiguredProduct', 'SALES_UNIT', 'S', 'F', 'F', 'Sales Unit', 'One');

insert into cms.attributedefinition (id, contenttype_id, name, attributetype_code, inheritable, required, label, cardinality_code)
values (cms.system_seq.nextval, 'ConfiguredProduct', 'OPTIONS', 'S', 'F', 'F', 'Configuration Options', 'One');

-- relationships

insert into cms.relationshipdefinition (id, contenttype_id, name, navigable, inheritable, required, label, cardinality_code)
values (cms.system_seq.nextval, 'ConfiguredProduct', 'SKU', 'F', 'F', 'F', 'SKU', 'One');

insert into cms.relationshipdestination (id, relationshipdefinition_id, contenttype_id)
values (cms.system_seq.nextval, (select id from relationshipdefinition where contenttype_id='ConfiguredProduct' and name='SKU'), 'Sku');

-- Department -> ConfiguredProduct

insert into cms.relationshipdestination (id, relationshipdefinition_id, contenttype_id)
values (cms.system_seq.nextval, (select id from relationshipdefinition where contenttype_id='Department' and name='FEATURED_PRODUCTS'), 'ConfiguredProduct');

-- Category -> ConfiguredProduct

insert into cms.relationshipdestination (id, relationshipdefinition_id, contenttype_id)
values (cms.system_seq.nextval, (select id from relationshipdefinition where contenttype_id='Category' and name='products'), 'ConfiguredProduct');

insert into cms.relationshipdestination (id, relationshipdefinition_id, contenttype_id)
values (cms.system_seq.nextval, (select id from relationshipdefinition where contenttype_id='Category' and name='FEATURED_PRODUCTS'), 'ConfiguredProduct');

insert into cms.relationshipdestination (id, relationshipdefinition_id, contenttype_id)
values (cms.system_seq.nextval, (select id from relationshipdefinition where contenttype_id='Category' and name='HOW_TO_COOK_IT_PRODUCTS'), 'ConfiguredProduct');

-- Product -> ConfiguredProduct

insert into cms.relationshipdestination (id, relationshipdefinition_id, contenttype_id)
values (cms.system_seq.nextval, (select id from relationshipdefinition where contenttype_id='Product' and name='RELATED_PRODUCTS'), 'ConfiguredProduct');

insert into cms.relationshipdestination (id, relationshipdefinition_id, contenttype_id)
values (cms.system_seq.nextval, (select id from relationshipdefinition where contenttype_id='Product' and name='PRODUCT_BUNDLE'), 'ConfiguredProduct');

insert into cms.relationshipdestination (id, relationshipdefinition_id, contenttype_id)
values (cms.system_seq.nextval, (select id from relationshipdefinition where contenttype_id='Product' and name='ALSO_SOLD_AS'), 'ConfiguredProduct');
