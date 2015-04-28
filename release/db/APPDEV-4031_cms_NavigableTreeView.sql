-- create view
create or replace view navtree
as
select r.id,
  r.parent_contentnode_id,
  r.ordinal,
  r.def_name,
  r.def_contenttype,
  r.child_contentnode_id
from relationship r, relationshipdefinition rdef
where rdef.name = r.def_name (+)
  and rdef.id = substr(r.parent_contentnode_id,1,instr(r.parent_contentnode_id,':')-1)||'.'||r.def_name
  and rdef.navigable='T'
with
  read only;


-- grant rights

--
-- CMS PROD and CMS TEST grants
--
grant select on navtree to fdstore;
grant select on navtree to cust;
grant select on navtree to cssi;
grant select on navtree to appdev;

