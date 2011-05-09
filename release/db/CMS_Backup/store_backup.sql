delete from media_backup;
insert into media_backup(id, uri,width, height, type, mime_type, last_modified) SELECT id, uri,width, height, type, mime_type, last_modified from cms.media;

delete from contentnode_backup;
insert into contentnode_backup(id, contenttype_id) select id, contenttype_id from cms.contentnode;

delete from attribute_backup;
insert into attribute_backup(contentnode_id, id, value, ordinal, def_name, def_contenttype) select contentnode_id, id, value, ordinal, def_name, def_contenttype from cms.attribute;

delete from relationship_backup;
insert into relationship_backup(parent_contentnode_id, ordinal, id, def_name, def_contenttype, child_contentnode_id) select parent_contentnode_id, ordinal, id, def_name, def_contenttype, child_contentnode_id from cms.relationship;
