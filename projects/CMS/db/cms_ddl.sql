CREATE TABLE ContentNode
(
ID VARCHAR2(128) NOT NULL,
CONTENTTYPE_ID VARCHAR2(40) NOT NULL,
  CONSTRAINT PK_ContentNode PRIMARY KEY (ID)
);


CREATE TABLE Attribute
(
CONTENTNODE_ID VARCHAR2(128) NOT NULL,
ID VARCHAR2(16) NOT NULL,
VALUE VARCHAR2(2048),
ORDINAL NUMBER(10) NOT NULL,
DEF_NAME VARCHAR2(40) NOT NULL,
DEF_CONTENTTYPE VARCHAR2(40) NOT NULL,
  CONSTRAINT PK_Attribute PRIMARY KEY (ID)
);


CREATE TABLE Publish
(
TIMESTAMP DATE NOT NULL UNIQUE,
ID VARCHAR2(16) NOT NULL,
USER_ID VARCHAR2(32) NOT NULL,
DESCRIPTION VARCHAR2(1000),
LAST_MODIFIED DATE,
STATUS VARCHAR2(10),
  CONSTRAINT PK_Publish PRIMARY KEY (ID)
);


CREATE TABLE PublishMessages
(
TIMESTAMP DATE NOT NULL UNIQUE,
ID VARCHAR2(16) NOT NULL,
USER_ID VARCHAR2(32) NOT NULL,
DESCRIPTION VARCHAR2(1000),
LAST_MODIFIED DATE,
STATUS VARCHAR2(10),
  CONSTRAINT PK_Publish PRIMARY KEY (ID)
);

CREATE TABLE RelationshipDefinition
(
NAME VARCHAR2(1000) NOT NULL,
ID VARCHAR2(128) NOT NULL,
CONTENTTYPE_ID VARCHAR2(40) NOT NULL,
INHERITABLE VARCHAR2(1) NOT NULL,
REQUIRED VARCHAR2(1) NOT NULL,
NAVIGABLE VARCHAR2(1) NOT NULL,
LABEL VARCHAR2(64),
CARDINALITY_CODE VARCHAR2(4) NOT NULL,
  CONSTRAINT PK_RelationshipDefinition PRIMARY KEY (ID)
);


CREATE TABLE ContentNodeChange
(
ID VARCHAR2(16) NOT NULL,
CHANGESET_ID VARCHAR2(16) NOT NULL,
CONTENTNODE_ID VARCHAR2(128) NOT NULL,
CHANGETYPE VARCHAR2(4),
CONTENTTYPE VARCHAR2(40),
  CONSTRAINT PK_ContentNodeChange PRIMARY KEY (ID)
);


CREATE TABLE CmsUser
(
ID VARCHAR2(32) NOT NULL,
EMAIL VARCHAR2(40) NOT NULL UNIQUE,
FIRST_NAME VARCHAR2(40) NOT NULL,
LAST_NAME VARCHAR2(40) NOT NULL,
PASSWORD VARCHAR2(40) NOT NULL,
  CONSTRAINT PK_CmsUser PRIMARY KEY (ID)
);


CREATE TABLE AttributeDefinition
(
NAME VARCHAR2(40) NOT NULL,
ID VARCHAR2(128) NOT NULL,
CONTENTTYPE_ID VARCHAR2(40) NOT NULL,
ATTRIBUTETYPE_CODE VARCHAR2(40),
INHERITABLE VARCHAR2(1) NOT NULL,
REQUIRED VARCHAR2(1) NOT NULL,
LABEL VARCHAR2(64),
CARDINALITY_CODE VARCHAR2(4) NOT NULL,
LOOKUP_CODE VARCHAR2(40),
  CONSTRAINT PK_AttributeDefinition PRIMARY KEY (ID)
);


CREATE TABLE ContentType
(
ID VARCHAR2(40) NOT NULL,
NAME VARCHAR2(1000) NOT NULL UNIQUE,
GENERATE_ID VARCHAR2(1) NULL,
DESCRIPTION VARCHAR2(1000),
  CONSTRAINT PK_ContentType PRIMARY KEY (ID)
);


CREATE TABLE RelationshipDestination
(
RELATIONSHIPDEFINITION_ID VARCHAR2(128) NOT NULL,
CONTENTTYPE_ID VARCHAR2(40) NOT NULL,
LABEL VARCHAR2(64),
ID VARCHAR2(128) NOT NULL,
  CONSTRAINT PK_RelationshipDestination PRIMARY KEY (ID)
);


CREATE TABLE ChangeSet
(
ID VARCHAR2(16) NOT NULL,
TIMESTAMP DATE NOT NULL UNIQUE,
USER_ID VARCHAR2(32) NOT NULL,
NOTE VARCHAR2(2000),
  CONSTRAINT PK_ChangeSet PRIMARY KEY (ID)
);


CREATE TABLE UserRole
(
USER_ID VARCHAR2(32) NOT NULL,
ROLE_ID VARCHAR2(16) NOT NULL,
ID VARCHAR2(16) NOT NULL,
  CONSTRAINT PK_UserRole PRIMARY KEY (ID)
);


CREATE TABLE CmsRole
(
ID VARCHAR2(16) NOT NULL,
NAME VARCHAR2(1000) NOT NULL UNIQUE,
DESCRIPTION VARCHAR2(1000),
  CONSTRAINT PK_CmsRole PRIMARY KEY (ID)
);


CREATE TABLE ContentTypeRole
(
ROLE_ID VARCHAR2(16) NOT NULL,
ID VARCHAR2(16) NOT NULL,
CONTENTTYPE_ID VARCHAR2(40) NOT NULL,
  CONSTRAINT PK_ContentTypeRole PRIMARY KEY (ID)
);


CREATE TABLE Relationship
(
PARENT_CONTENTNODE_ID VARCHAR2(128) NOT NULL,
ORDINAL NUMBER(10) NOT NULL,
ID VARCHAR2(40) NOT NULL,
DEF_NAME VARCHAR2(40) NOT NULL,
DEF_CONTENTTYPE VARCHAR2(40) NOT NULL,
CHILD_CONTENTNODE_ID VARCHAR2(128) NOT NULL,
  CONSTRAINT PK_Relationship PRIMARY KEY (ID)
);


CREATE TABLE Lookup
(
LOOKUPTYPE_CODE VARCHAR2(40) NOT NULL,
CODE VARCHAR2(40) NOT NULL,
LABEL VARCHAR2(40),
DESCRIPTION VARCHAR2(256),
ORDINAL NUMBER(10) 
);


CREATE TABLE LookupType
(
CODE VARCHAR2(40) NOT NULL,
NAME VARCHAR2(40) NOT NULL,
DESCRIPTION VARCHAR2(256),
  CONSTRAINT PK_LookupType PRIMARY KEY (CODE)
);


CREATE TABLE ChangeDetail
(
CONTENTNODECHANGE_ID VARCHAR2(16) NOT NULL,
CHANGETYPE VARCHAR2(4) NOT NULL,
ATTRIBUTENAME VARCHAR2(40) NOT NULL,
OLDVALUE VARCHAR2(2000),
NEWVALUE VARCHAR2(2000)
);

CREATE TABLE MEDIA
(
  ID             VARCHAR2(128 BYTE),
  URI            VARCHAR2(255 BYTE)             NOT NULL,
  WIDTH          INTEGER,
  HEIGHT         INTEGER,
  TYPE           VARCHAR2(55 BYTE)              NOT NULL,
  MIME_TYPE      VARCHAR2(55 BYTE),
  LAST_MODIFIED  DATE
);



ALTER TABLE Attribute ADD CONSTRAINT CNODE_ATTR_FK FOREIGN KEY (
CONTENTNODE_ID
)
REFERENCES ContentNode (
ID
);


ALTER TABLE RelationshipDefinition ADD CONSTRAINT CTYPE_RELDEF_FK FOREIGN KEY (
CONTENTTYPE_ID
)
REFERENCES ContentType (
ID
);


ALTER TABLE ContentNodeChange ADD CONSTRAINT CSET_CNODECHG_FK FOREIGN KEY (
CHANGESET_ID
)
REFERENCES ChangeSet (
ID
);


ALTER TABLE AttributeDefinition ADD CONSTRAINT CTYPE_ATTRDEF_FK FOREIGN KEY (
CONTENTTYPE_ID
)
REFERENCES ContentType (
ID
);

ALTER TABLE AttributeDefinition ADD CONSTRAINT ATRDEF_LOOKUP_FK FOREIGN KEY (
LOOKUP_CODE
)
REFERENCES LookupType (
CODE
);

ALTER TABLE RelationshipDestination ADD CONSTRAINT RSHIPDEF_RSHIPDEST_FK FOREIGN KEY (
RELATIONSHIPDEFINITION_ID
)
REFERENCES RelationshipDefinition (
ID
);


ALTER TABLE RelationshipDestination ADD CONSTRAINT CTYPE_RELDEST_FK FOREIGN KEY (
CONTENTTYPE_ID
)
REFERENCES ContentType (
ID
);


ALTER TABLE UserRole ADD CONSTRAINT USER_USERROLE_FK FOREIGN KEY (
USER_ID
)
REFERENCES CmsUser (
ID
);


ALTER TABLE UserRole ADD CONSTRAINT ROLE_USERROLE_FK FOREIGN KEY (
ROLE_ID
)
REFERENCES CmsRole (
ID
);


ALTER TABLE ContentTypeRole ADD CONSTRAINT ROLE_CTYPEROLE_FK FOREIGN KEY (
ROLE_ID
)
REFERENCES CmsRole (
ID
);


ALTER TABLE Relationship ADD CONSTRAINT CNODE_RSHIPCHILD_FK FOREIGN KEY (
CHILD_CONTENTNODE_ID
)
REFERENCES ContentNode (
ID
);


ALTER TABLE Relationship ADD CONSTRAINT CNODE_RSHIPPARENT_FK FOREIGN KEY (
PARENT_CONTENTNODE_ID
)
REFERENCES ContentNode (
ID
);


ALTER TABLE Lookup ADD CONSTRAINT LKTYPE_LOOKUP_FK FOREIGN KEY (
LOOKUPTYPE_CODE
)
REFERENCES LookupType (
CODE
);

ALTER TABLE ChangeDetail ADD CONSTRAINT CNCHG_CNCHGDET_FK FOREIGN KEY (
CONTENTNODECHANGE_ID
)
REFERENCES ContentNodeChange (
ID
);

ALTER TABLE PublishMessages ADD CONSTRAINT PublishMessages_Publish_FK FOREIGN KEY (
PUBLISH_ID
)
REFERENCES Publish (
ID
);

ALTER TABLE MEDIA ADD (
  CONSTRAINT PK_MEDIA PRIMARY KEY (ID));

ALTER TABLE MEDIA ADD (
  CONSTRAINT UNQ_MEDIA_URI UNIQUE (URI));

--
-- type definition uniqueness
--

ALTER TABLE AttributeDefinition ADD (
  CONSTRAINT UNQ_ATTRIBUTEDEFINITION UNIQUE (CONTENTTYPE_ID, NAME));

ALTER TABLE RelationshipDefinition ADD (
  CONSTRAINT UNQ_RELATIONSHIPDEFINITION UNIQUE (CONTENTTYPE_ID, NAME));

ALTER TABLE RelationshipDestination ADD (
  CONSTRAINT UNQ_RELATIONSHIPDESTINATION UNIQUE (RELATIONSHIPDEFINITION_ID, CONTENTTYPE_ID));


grant select,insert,update, delete on CMS.PublishMessages to fdstore;

create sequence CMS.hibernate_sequence
    START WITH 1
    MAXVALUE 999999999999999999999999999
    MINVALUE 1
    NOCYCLE
    NOCACHE
    NOORDER;

grant select on CMS.hibernate_sequence to fdstore;

