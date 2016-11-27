-- types
insert into cms.contenttype(id, name, description)
values ('ComponentGroup', 'ComponentGroup', 'definition of ComponentGroup ContentType');

insert into cms.contenttype(id, name, description)
values ('ErpCharacteristic', 'ErpCharacteristic', 'definition of ErpCharacteristic ContentType');

-- attributes
insert into cms.attributedefinition (id, contenttype_id, name, attributetype_code, inheritable, required, label, cardinality_code)
values (cms.system_seq.nextval, 'ComponentGroup', 'FULL_NAME', 'S', 'F', 'F', 'Full Name', 'One');

insert into cms.attributedefinition (id, contenttype_id, name, attributetype_code, inheritable, required, label, cardinality_code)
values (cms.system_seq.nextval, 'ComponentGroup', 'SHOW_OPTIONS', 'B', 'F', 'F', 'Show Options', 'One');


-- relationships

insert into cms.relationshipdefinition (id, contenttype_id, name, navigable, inheritable, required, label, cardinality_code)
values (cms.system_seq.nextval, 'Product', 'COMPONENT_GROUPS','T', 'F', 'F', 'Component Groups', 'Many');

insert into cms.relationshipdestination (id, relationshipdefinition_id, contenttype_id)
values (cms.system_seq.nextval, (select id from relationshipdefinition where contenttype_id='Product' and name='COMPONENT_GROUPS'), 'ComponentGroup');


insert into cms.relationshipdefinition (id, contenttype_id, name, navigable, inheritable, required, label, cardinality_code)
values (cms.system_seq.nextval, 'ComponentGroup', 'HEADER_IMAGE','F', 'F', 'F', 'Header Image', 'One');

insert into cms.relationshipdestination (id, relationshipdefinition_id, contenttype_id)
values (cms.system_seq.nextval, (select id from relationshipdefinition where contenttype_id='ComponentGroup' and name='HEADER_IMAGE'), 'Html');


insert into cms.relationshipdefinition (id, contenttype_id, name, navigable, inheritable, required, label, cardinality_code)
values (cms.system_seq.nextval, 'ComponentGroup', 'EDITORIAL','F', 'F', 'F', 'Editorial', 'One');

insert into cms.relationshipdestination (id, relationshipdefinition_id, contenttype_id)
values (cms.system_seq.nextval, (select id from relationshipdefinition where contenttype_id='ComponentGroup' and name='EDITORIAL'), 'Html');


insert into cms.relationshipdefinition (id, contenttype_id, name, navigable, inheritable, required, label, cardinality_code)
values (cms.system_seq.nextval, 'ComponentGroup', 'CHARACTERISTICS','F', 'F', 'F', 'Characteristics', 'Many');

insert into cms.relationshipdestination (id, relationshipdefinition_id, contenttype_id)
values (cms.system_seq.nextval, (select id from relationshipdefinition where contenttype_id='ComponentGroup' and name='CHARACTERISTICS'), 'ErpCharacteristic');



insert into cms.relationshipdefinition (id, contenttype_id, name, navigable, inheritable, required, label, cardinality_code)
values (cms.system_seq.nextval, 'ComponentGroup', 'OPTIONAL_PRODUCTS','F', 'F', 'F', 'Optional Products', 'Many');

insert into cms.relationshipdestination (id, relationshipdefinition_id, contenttype_id)
values (cms.system_seq.nextval, (select id from relationshipdefinition where contenttype_id='ComponentGroup' and name='OPTIONAL_PRODUCTS'), 'Product');


