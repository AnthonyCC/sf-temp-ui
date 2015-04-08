--
-- Replace old materialized view with the fixed
--
-- The mv solution is a bit tricky
-- based on article found at
-- http://stackoverflow.com/questions/1312980/oracle-materialized-view-not-working-when-using-left-join
--

DROP MATERIALIZED VIEW LOG ON relationship;
DROP MATERIALIZED VIEW LOG ON relationshipdefinition;
drop materialized view all_nodes;


CREATE MATERIALIZED VIEW LOG ON relationship with rowid;
CREATE MATERIALIZED VIEW LOG ON relationshipdefinition with rowid;

create materialized view all_nodes
  build immediate 
  refresh fast on commit 
as select r.*, r.rowid as r_rowid, rdef.rowid as rdef_rowid
  from relationship r, relationshipdefinition rdef
where rdef.name = r.def_name (+)
  and rdef.id = substr(r.parent_contentnode_id,1,instr(r.parent_contentnode_id,':')-1)||'.'||r.def_name
  and  rdef.navigable='T';

--
-- CMS PROD and CMS TEST grants
--
grant select on all_nodes to fdstore;
grant select on all_nodes to cust;
grant select on all_nodes to cssi;
grant select on all_nodes to appdev;

-- DBEU grants
-- grant select on all_nodes to fdstore_stprd01;
-- grant select on all_nodes to cust;
