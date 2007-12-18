ALTER TABLE ContentType
MODIFY ID VARCHAR2(40);

ALTER TABLE AttributeDefinition
MODIFY CONTENTTYPE_ID VARCHAR2(40);

ALTER TABLE RelationshipDefinition
MODIFY CONTENTTYPE_ID VARCHAR2(40);

ALTER TABLE RelationshipDestination
MODIFY CONTENTTYPE_ID VARCHAR2(40);


ALTER TABLE ContentNode
MODIFY CONTENTTYPE_ID VARCHAR2(40);

ALTER TABLE Attribute
MODIFY DEF_CONTENTTYPE VARCHAR2(40);

ALTER TABLE Relationship
MODIFY DEF_CONTENTTYPE VARCHAR2(40);

ALTER TABLE ContentNodeChange
MODIFY CONTENTTYPE VARCHAR2(40);

ALTER TABLE ContentTypeRole
MODIFY CONTENTTYPE_ID VARCHAR2(40);


-- rebuild ALL_NODES view crazyness

DROP MATERIALIZED VIEW LOG ON relationship;

DROP MATERIALIZED VIEW LOG ON relationshipdefinition;

CREATE MATERIALIZED VIEW LOG ON relationship
 with primary key (def_name, def_contenttype);

CREATE MATERIALIZED VIEW LOG ON relationshipdefinition
 with primary key (name, contenttype_id, navigable);

drop materialized view all_nodes;

create materialized view all_nodes
  build immediate 
  refresh fast on commit 
  as 
select * from relationship r
where exists
  (select 'x' from relationshipdefinition rd
  		  	 where rd.name = r.def_name
			   and rd.navigable = 'T');

create index IDX_ALL_NODES_parent_node_id on all_nodes(parent_contentnode_id);

create index IDX_ALL_NODES_child_node_id on all_nodes(child_contentnode_id);

create index IDX_ALL_NODES_def_name on all_nodes(def_name);

create index IDX_ALL_NODES_def_contenttype on all_nodes(def_contenttype);

grant select on ALL_NODES to FDSTORE;

