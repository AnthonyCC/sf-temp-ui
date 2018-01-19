insert into CONTENTNODE(id, contenttype_id) values('Product:test','Product');
insert into CONTENTNODE(id, contenttype_id) values('Sku:testdest','Sku');
insert into CONTENTNODE(id, contenttype_id) values('Domain:product1','Domain');
insert into CONTENTNODE(id, contenttype_id) values('Domain:product2','Domain');
insert into CONTENTNODE(id, contenttype_id) values('Domain:product3','Domain');
insert into CONTENTNODE(id, contenttype_id) values('Domain:product4','Domain');
insert into CONTENTNODE(id, contenttype_id) values('Category:test','Category');
insert into CONTENTNODE(id, contenttype_id) values('Category:parentCategory','Category');
insert into CONTENTNODE(id, contenttype_id) values('Product:multipleAttributed','Product');
insert into CONTENTNODE(id, contenttype_id) values('Sku:GRO0069855','Sku');

insert into ATTRIBUTE(contentnode_id, id, value, ordinal, def_name, def_contenttype) values('Product:test', cms_system_seq.nextVal, 'Test full name', 0, 'FULL_NAME', 'Product');
insert into ATTRIBUTE(contentnode_id, id, value, ordinal, def_name, def_contenttype) values('Product:test', cms_system_seq.nextVal, 'Test nav name', 0, 'NAV_NAME', 'Product');
insert into ATTRIBUTE(contentnode_id, id, value, ordinal, def_name, def_contenttype) values('Product:test', cms_system_seq.nextVal, 'Test random', 0, 'KEYWORDS', 'Product');
insert into ATTRIBUTE(contentnode_id, id, value, ordinal, def_name, def_contenttype) values('Product:test', cms_system_seq.nextVal, true, 0, 'SHOW_TOP_TEN_IMAGE', 'Product');

insert into RELATIONSHIP(parent_contentnode_id, ordinal, id, def_name, def_contenttype, child_contentnode_id) values('Product:test', 0, cms_system_seq.nextVal, 'PREFERRED_SKU', 'Sku', 'Sku:testdest');
insert into RELATIONSHIP(parent_contentnode_id, ordinal, id, def_name, def_contenttype, child_contentnode_id) values('Product:test', 0, cms_system_seq.nextVal, 'VARIATION_MATRIX', 'Domain', 'Domain:product1');
insert into RELATIONSHIP(parent_contentnode_id, ordinal, id, def_name, def_contenttype, child_contentnode_id) values('Product:test', 1, cms_system_seq.nextVal, 'VARIATION_MATRIX', 'Domain', 'Domain:product4');
insert into RELATIONSHIP(parent_contentnode_id, ordinal, id, def_name, def_contenttype, child_contentnode_id) values('Product:test', 2, cms_system_seq.nextVal, 'VARIATION_MATRIX', 'Domain', 'Domain:product3');
insert into RELATIONSHIP(parent_contentnode_id, ordinal, id, def_name, def_contenttype, child_contentnode_id) values('Product:test', 3, cms_system_seq.nextVal, 'VARIATION_MATRIX', 'Domain', 'Domain:product2');

insert into MEDIA(id, uri, width, height, type, mime_type, last_modified) values('media1', '/media/test/media1.jpg', 125, 125, 'Image', 'image/jpeg', now());
insert into MEDIA(id, uri, width, height, type, mime_type, last_modified) values('media2', '/media/test/media2.jpg', 120, 140, 'Image', 'image/jpeg', now());
insert into MEDIA(id, uri, width, height, type, mime_type, last_modified) values('media3', '/media/test/media.html', null, null, 'Html', 'text/plain; charset=us-ascii', now());
