-- Fix departments since we load from multiple files
delete from relationship where parent_contentnode_id = 'Store:FreshDirect'
and def_name = 'departments';

insert into relationship
select 'Store:FreshDirect', rownum, cms_system_seq.nextval, 'departments', 'Department', id
from contentnode where contenttype_id = 'Department';

-- Find list of relations with no child contentnode id's
select * from relationship where not exists
(select id from contentnode where contentnode.id = relationship.child_contentnode_id)

-- enable constraints
alter table relationship enable constraint CNODE_RSHIPCHILD_FK;

-- Now grab latest media and insert build MEDIA table.

-- Fix attributedefinitions for Media objects.
delete from ATTRIBUTEDEFINITION where contenttype_id in ('Html', 'Image');

Insert into ATTRIBUTEDEFINITION
   (name, id, contenttype_id, attributetype_code, inheritable, required, cardinality_code)
 Values
   ('popupSize', system_seq.nextval, 'Html', 'S', 'F', 'F', 'One');
Insert into ATTRIBUTEDEFINITION
   (name, id, contenttype_id, attributetype_code, inheritable, required, cardinality_code)
 Values
   ('title', system_seq.nextval, 'Html', 'S', 'F', 'F', 'One');
Insert into ATTRIBUTEDEFINITION
   (name, id, contenttype_id, attributetype_code, inheritable, required, cardinality_code)
 Values
   ('path', system_seq.nextval, 'Html', 'S', 'F', 'T', 'One');
Insert into ATTRIBUTEDEFINITION
   (name, id, contenttype_id, attributetype_code, inheritable, required, cardinality_code)
 Values
   ('width', system_seq.nextval, 'Image', 'I', 'F', 'T', 'One');
Insert into ATTRIBUTEDEFINITION
   (name, id, contenttype_id, attributetype_code, inheritable, required, cardinality_code)
 Values
   ('height', system_seq.nextval, 'Image', 'I', 'F', 'T', 'One');
Insert into ATTRIBUTEDEFINITION
   (name, id, contenttype_id, attributetype_code, inheritable, required, cardinality_code)
 Values
   ('path', system_seq.nextval, 'Image', 'S', 'F', 'T', 'One');

-- Create FDFolders for Brands and Domains

insert into contentnode(id,contenttype_id) values ('FDFolder:domains','FDFolder');

insert into contentnode(id,contenttype_id) values ('FDFolder:brands','FDFolder');

insert into relationship (parent_contentnode_id, ordinal, id, def_name, def_contenttype, child_contentnode_id)
values ('Store:FreshDirect', 0, cms_system_seq.nextval, 'folders', 'FDFolder', 'FDFolder:domains'); 

insert into relationship (parent_contentnode_id, ordinal, id, def_name, def_contenttype, child_contentnode_id)
values ('Store:FreshDirect', 1, cms_system_seq.nextval, 'folders', 'FDFolder', 'FDFolder:brands'); 

insert into relationship (
 select 'FDFolder:brands' as parent_contentnode_id, rownum-1 as ordinal, cms_system_seq.nextval as id, 'children', contenttype_id as def_contenttype, id as child_contentnode_id from (
  select * from contentnode where contenttype_id='Brand' order by id)
);

insert into relationship (
 select 'FDFolder:domains' as parent_contentnode_id, rownum-1 as ordinal, cms_system_seq.nextval as id, 'children', contenttype_id as def_contenttype, id as child_contentnode_id from (
  select * from contentnode where contenttype_id='Domain' order by id)
);

COMMIT;



