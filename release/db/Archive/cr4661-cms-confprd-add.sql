
-- attributes

insert into cms.attributedefinition (id, contenttype_id, name, attributetype_code, inheritable, required, label, cardinality_code)
values (cms.system_seq.nextval, 'ConfiguredProduct', 'FULL_NAME', 'S', 'F', 'F', 'Full Name', 'One');

insert into cms.attributedefinition (id, contenttype_id, name, attributetype_code, inheritable, required, label, cardinality_code)
values (cms.system_seq.nextval, 'ConfiguredProduct', 'GLANCE_NAME', 'S', 'F', 'F', 'Glance Name', 'One');

insert into cms.attributedefinition (id, contenttype_id, name, attributetype_code, inheritable, required, label, cardinality_code)
values (cms.system_seq.nextval, 'ConfiguredProduct', 'NAV_NAME', 'S', 'F', 'F', 'Nav Name', 'One');

insert into cms.attributedefinition (id, contenttype_id, name, attributetype_code, inheritable, required, label, cardinality_code)
values (cms.system_seq.nextval, 'ConfiguredProduct', 'BLURB', 'S', 'F', 'F', 'Blurb', 'One');

insert into cms.attributedefinition (id, contenttype_id, name, attributetype_code, inheritable, required, label, cardinality_code)
values (cms.system_seq.nextval, 'ConfiguredProduct', 'ALSO_SOLD_AS_NAME', 'S', 'F', 'F', 'Also Sold As Name', 'One');

-- relationships

insert into cms.relationshipdefinition (id, contenttype_id, name, navigable, inheritable, required, label, cardinality_code)
values (cms.system_seq.nextval, 'ConfiguredProduct', 'PROD_DESCR', 'F', 'F', 'F', 'Prod Description', 'One');

insert into cms.relationshipdestination (id, relationshipdefinition_id, contenttype_id)
values (cms.system_seq.nextval, (select id from relationshipdefinition where contenttype_id='ConfiguredProduct' and name='PROD_DESCR'), 'Html');


insert into cms.relationshipdefinition (id, contenttype_id, name, navigable, inheritable, required, label, cardinality_code)
values (cms.system_seq.nextval, 'ConfiguredProduct', 'PROD_DESCRIPTION_NOTE', 'F', 'F', 'F', 'Prod Description Note', 'One');

insert into cms.relationshipdestination (id, relationshipdefinition_id, contenttype_id)
values (cms.system_seq.nextval, (select id from relationshipdefinition where contenttype_id='ConfiguredProduct' and name='PROD_DESCRIPTION_NOTE'), 'Html');


insert into cms.relationshipdefinition (id, contenttype_id, name, navigable, inheritable, required, label, cardinality_code)
values (cms.system_seq.nextval, 'ConfiguredProduct', 'PRODUCT_QUALITY_NOTE', 'F', 'F', 'F', 'Prod Quality Note', 'One');

insert into cms.relationshipdestination (id, relationshipdefinition_id, contenttype_id)
values (cms.system_seq.nextval, (select id from relationshipdefinition where contenttype_id='ConfiguredProduct' and name='PRODUCT_QUALITY_NOTE'), 'Html');


insert into cms.relationshipdefinition (id, contenttype_id, name, navigable, inheritable, required, label, cardinality_code)
values (cms.system_seq.nextval, 'ConfiguredProduct', 'PROD_IMAGE', 'F', 'F', 'F', 'Prod Image', 'One');

insert into cms.relationshipdestination (id, relationshipdefinition_id, contenttype_id)
values (cms.system_seq.nextval, (select id from relationshipdefinition where contenttype_id='ConfiguredProduct' and name='PROD_IMAGE'), 'Image');


insert into cms.relationshipdefinition (id, contenttype_id, name, navigable, inheritable, required, label, cardinality_code)
values (cms.system_seq.nextval, 'ConfiguredProduct', 'PROD_IMAGE_CONFIRM', 'F', 'F', 'F', 'Prod Image Confirm', 'One');

insert into cms.relationshipdestination (id, relationshipdefinition_id, contenttype_id)
values (cms.system_seq.nextval, (select id from relationshipdefinition where contenttype_id='ConfiguredProduct' and name='PROD_IMAGE_CONFIRM'), 'Image');


insert into cms.relationshipdefinition (id, contenttype_id, name, navigable, inheritable, required, label, cardinality_code)
values (cms.system_seq.nextval, 'ConfiguredProduct', 'PROD_IMAGE_DETAIL', 'F', 'F', 'F', 'Prod Image Detail', 'One');

insert into cms.relationshipdestination (id, relationshipdefinition_id, contenttype_id)
values (cms.system_seq.nextval, (select id from relationshipdefinition where contenttype_id='ConfiguredProduct' and name='PROD_IMAGE_DETAIL'), 'Image');


insert into cms.relationshipdefinition (id, contenttype_id, name, navigable, inheritable, required, label, cardinality_code)
values (cms.system_seq.nextval, 'ConfiguredProduct', 'PROD_IMAGE_FEATURE', 'F', 'F', 'F', 'Prod Image Feature', 'One');

insert into cms.relationshipdestination (id, relationshipdefinition_id, contenttype_id)
values (cms.system_seq.nextval, (select id from relationshipdefinition where contenttype_id='ConfiguredProduct' and name='PROD_IMAGE_FEATURE'), 'Image');


insert into cms.relationshipdefinition (id, contenttype_id, name, navigable, inheritable, required, label, cardinality_code)
values (cms.system_seq.nextval, 'ConfiguredProduct', 'PROD_IMAGE_ZOOM', 'F', 'F', 'F', 'Prod Image Zoom', 'One');

insert into cms.relationshipdestination (id, relationshipdefinition_id, contenttype_id)
values (cms.system_seq.nextval, (select id from relationshipdefinition where contenttype_id='ConfiguredProduct' and name='PROD_IMAGE_ZOOM'), 'Image');


insert into cms.relationshipdefinition (id, contenttype_id, name, navigable, inheritable, required, label, cardinality_code)
values (cms.system_seq.nextval, 'ConfiguredProduct', 'ALTERNATE_IMAGE', 'F', 'F', 'F', 'Alternate Image', 'One');

insert into cms.relationshipdestination (id, relationshipdefinition_id, contenttype_id)
values (cms.system_seq.nextval, (select id from relationshipdefinition where contenttype_id='ConfiguredProduct' and name='ALTERNATE_IMAGE'), 'Image');



insert into cms.relationshipdefinition (id, contenttype_id, name, navigable, inheritable, required, label, cardinality_code)
values (cms.system_seq.nextval, 'ConfiguredProduct', 'RELATED_PRODUCTS', 'F', 'F', 'F', 'Related Prods', 'Many');

insert into cms.relationshipdestination (id, relationshipdefinition_id, contenttype_id)
values (cms.system_seq.nextval, (select id from relationshipdefinition where contenttype_id='ConfiguredProduct' and name='RELATED_PRODUCTS'), 'Product');

insert into cms.relationshipdestination (id, relationshipdefinition_id, contenttype_id)
values (cms.system_seq.nextval, (select id from relationshipdefinition where contenttype_id='ConfiguredProduct' and name='RELATED_PRODUCTS'), 'ConfiguredProduct');

insert into cms.relationshipdestination (id, relationshipdefinition_id, contenttype_id)
values (cms.system_seq.nextval, (select id from relationshipdefinition where contenttype_id='ConfiguredProduct' and name='RELATED_PRODUCTS'), 'Category');


