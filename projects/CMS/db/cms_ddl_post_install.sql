/* Post install script for CMS - RUN AS CMS USER */

DROP TABLE MEDIA CASCADE CONSTRAINTS;

CREATE TABLE MEDIA
(
  ID      VARCHAR2(128)                     NOT NULL,
  URI     VARCHAR2(255)                    NOT NULL,
  WIDTH   INTEGER,
  HEIGHT  INTEGER,
  TYPE    VARCHAR2(55)
);

drop sequence system_seq;

create sequence	system_seq start with 1;

/* the following query creates the index creations statement */
/*select 'create index IDX_' || uc.constraint_name || ' on ' || uc.table_name || '(' || ucc.column_name || ');'
from user_constraints uc, user_cons_columns ucc
where uc.constraint_type = 'R' and uc.r_owner = 'CMS'
and uc.constraint_name = ucc.constraint_name  */

create index IDX_CNODE_ATTR_FK on ATTRIBUTE(CONTENTNODE_ID);
create index IDX_CTYPE_RELDEF_FK on RELATIONSHIPDEFINITION(CONTENTTYPE_ID);
create index IDX_CSET_CNODECHG_FK on CONTENTNODECHANGE(CHANGESET_ID);
create index IDX_CTYPE_ATTRDEF_FK on ATTRIBUTEDEFINITION(CONTENTTYPE_ID);
create index IDX_ATRDEF_LOOKUP_FK on ATTRIBUTEDEFINITION(LOOKUP_CODE);
create index IDX_RSHIPDEF_RSHIPDEST_FK on RELATIONSHIPDESTINATION(RELATIONSHIPDEFINITION_ID);
create index IDX_CTYPE_RELDEST_FK on RELATIONSHIPDESTINATION(CONTENTTYPE_ID);
create index IDX_RLS_CHNGSET_FK on CHANGESET(PUBLISH_ID);
create index IDX_USER_USERROLE_FK on USERROLE(USER_ID);
create index IDX_ROLE_USERROLE_FK on USERROLE(ROLE_ID);
create index IDX_ROLE_CTYPEROLE_FK on CONTENTTYPEROLE(ROLE_ID);
create index IDX_CNODE_RSHIPCHILD_FK on RELATIONSHIP(CHILD_CONTENTNODE_ID);
create index IDX_CNODE_RSHIPPARENT_FK on RELATIONSHIP(PARENT_CONTENTNODE_ID);
create index IDX_LKTYPE_LOOKUP_FK on LOOKUP(LOOKUPTYPE_CODE);
create index IDX_CNCHG_CNCHGDET_FK on CHANGEDETAIL(CONTENTNODECHANGE_ID);
create index IDX_RELATIONSHIP_DEF_NAME ON RELATIONSHIP(DEF_NAME);

create index IDX_RELATIONSHIP_DEF_CNTTYPE ON RELATIONSHIP(DEF_CONTENTTYPE);

create index IDX_RELDEFN_NAME ON RELATIONSHIPDEFINITION(NAME);

create index IDX_RELDEFN_NAVIGABLE ON RELATIONSHIPDEFINITION(NAVIGABLE);

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
 (select 'x' from cms.relationshipdefinition rd
  where rd.name = r.def_name
   and rd.navigable = 'T');
  

create index IDX_ALL_NODES_parent_node_id on all_nodes(parent_contentnode_id);

create index IDX_ALL_NODES_child_node_id on all_nodes(child_contentnode_id);

create index IDX_ALL_NODES_def_name on all_nodes(def_name);

create index IDX_ALL_NODES_def_contenttype on all_nodes(def_contenttype);

create index IDX_ATTRIBUTE_DEF_NAME ON ATTRIBUTE(DEF_NAME);

create index IDX_ATTRIBUTE_DEF_CNTTYPE ON ATTRIBUTE(DEF_CONTENTTYPE);

CREATE UNIQUE INDEX UNQ_MEDIA_URI ON MEDIA
(URI)
;


CREATE UNIQUE INDEX PK_MEDIA ON MEDIA
(ID)


CREATE INDEX IDX_MEDIA_TYPE ON MEDIA
(TYPE)



/*   this is a query for testing purposes 
select * from relationship r
where exists
  (select 'x' from relationshipdefinition rd
  		  	 where rd.name = r.def_name
			   and rd.navigable = 'T')
start with parent_contentnode_id = 'Department:mea'
connect by prior child_contentnode_id = parent_contentnode_id   */
