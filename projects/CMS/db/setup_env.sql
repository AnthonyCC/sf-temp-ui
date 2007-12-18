/* Create dev_cms user and dev user */

create user dev_cms identified by dev_cms
temporary tablespace temp
default tablespace users;

grant dba to dev_cms;

create user dev identified by dev
temporary tablespace temp
default tablespace users;

grant dba to dev;

/* Create grant scripts */
/*
select 'grant select, insert, update, delete on ' || object_name || ' to dev;'
 from user_objects where object_type = 'TABLE';

select 'grant select on ' || object_name || ' to dev;'
 from user_objects where object_type = 'SEQUENCE';
*/

grant select, insert, update, delete on ALL_NODES to dev;
grant select, insert, update, delete on ATTRIBUTE to dev;
grant select, insert, update, delete on ATTRIBUTEDEFINITION to dev;
grant select, insert, update, delete on CHANGEDETAIL to dev;
grant select, insert, update, delete on CHANGESET to dev;
grant select, insert, update, delete on CMSROLE to dev;
grant select, insert, update, delete on CMSUSER to dev;
grant select, insert, update, delete on CONTENTNODE to dev;
grant select, insert, update, delete on CONTENTNODECHANGE to dev;
grant select, insert, update, delete on CONTENTTYPE to dev;
grant select, insert, update, delete on CONTENTTYPEROLE to dev;
grant select, insert, update, delete on LOOKUP to dev;
grant select, insert, update, delete on LOOKUPTYPE to dev;
grant select, insert, update, delete on MLOG$_RELATIONSHIP to dev;
grant select, insert, update, delete on MLOG$_RELATIONSHIP1 to dev;
grant select, insert, update, delete on MLOG$_RELATIONSHIPDEFINITI to dev;
grant select, insert, update, delete on MLOG$_RELATIONSHIPDEFINITI1 to dev;
grant select, insert, update, delete on PLAN_TABLE to dev;
grant select, insert, update, delete on PUBLISH to dev;
grant select, insert, update, delete on RELATIONSHIP to dev;
grant select, insert, update, delete on RELATIONSHIPDEFINITION to dev;
grant select, insert, update, delete on RELATIONSHIPDESTINATION to dev;
grant select, insert, update, delete on RUPD$_RELATIONSHIP to dev;
grant select, insert, update, delete on RUPD$_RELATIONSHIP1 to dev;
grant select, insert, update, delete on RUPD$_RELATIONSHIPDEFINITI to dev;
grant select, insert, update, delete on RUPD$_RELATIONSHIPDEFINITI1 to dev;
grant select, insert, update, delete on USERROLE to dev;


grant select on SYSTEM_SEQ to dev;

/*  */
/*
select 'create synonym cms_' || object_name || ' for dev_cms.' || object_name || ';'
 from user_objects where object_type in ('TABLE', 'SEQUENCE');
*/

/* Run as new dev user */
create synonym cms_ALL_NODES for dev_cms.ALL_NODES;
create synonym cms_ATTRIBUTE for dev_cms.ATTRIBUTE;
create synonym cms_ATTRIBUTEDEFINITION for dev_cms.ATTRIBUTEDEFINITION;
create synonym cms_CHANGEDETAIL for dev_cms.CHANGEDETAIL;
create synonym cms_CHANGESET for dev_cms.CHANGESET;
create synonym cms_CMSROLE for dev_cms.CMSROLE;
create synonym cms_CMSUSER for dev_cms.CMSUSER;
create synonym cms_CONTENTNODE for dev_cms.CONTENTNODE;
create synonym cms_CONTENTNODECHANGE for dev_cms.CONTENTNODECHANGE;
create synonym cms_CONTENTTYPE for dev_cms.CONTENTTYPE;
create synonym cms_CONTENTTYPEROLE for dev_cms.CONTENTTYPEROLE;
create synonym cms_LOOKUP for dev_cms.LOOKUP;
create synonym cms_LOOKUPTYPE for dev_cms.LOOKUPTYPE;
create synonym cms_MLOG$_RELATIONSHIP for dev_cms.MLOG$_RELATIONSHIP;
create synonym cms_MLOG$_RELATIONSHIP1 for dev_cms.MLOG$_RELATIONSHIP1;
create synonym cms_MLOG$_RELATIONSHIPDEFINITI for dev_cms.MLOG$_RELATIONSHIPDEFINITI;
create synonym cms_MLOG$_RELATIONSHIPDEFINITI1 for dev_cms.MLOG$_RELATIONSHIPDEFINITI1;
create synonym cms_PLAN_TABLE for dev_cms.PLAN_TABLE;
create synonym cms_PUBLISH for dev_cms.PUBLISH;
create synonym cms_RELATIONSHIP for dev_cms.RELATIONSHIP;
create synonym cms_RELATIONSHIPDEFINITION for dev_cms.RELATIONSHIPDEFINITION;
create synonym cms_RELATIONSHIPDESTINATION for dev_cms.RELATIONSHIPDESTINATION;
create synonym cms_RUPD$_RELATIONSHIP for dev_cms.RUPD$_RELATIONSHIP;
create synonym cms_RUPD$_RELATIONSHIP1 for dev_cms.RUPD$_RELATIONSHIP1;
create synonym cms_RUPD$_RELATIONSHIPDEFINITI for dev_cms.RUPD$_RELATIONSHIPDEFINITI;
create synonym cms_RUPD$_RELATIONSHIPDEFINITI1 for dev_cms.RUPD$_RELATIONSHIPDEFINITI1;
create synonym cms_SYSTEM_SEQ for dev_cms.SYSTEM_SEQ;
create synonym cms_USERROLE for dev_cms.USERROLE;


