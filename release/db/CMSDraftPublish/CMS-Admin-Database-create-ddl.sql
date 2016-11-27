create sequence PERSONA_ID_SEQ start with 1 increment by 1;
create sequence DRAFT_CHANGE_SEQUENCE start with 1 increment by 1;
create sequence DRAFT_SEQUENCE start with 1 increment by 1;

create table Draft (id number(19,0) not null, created_date timestamp not null, draftStatus varchar2(255 char), last_modified_date timestamp, name varchar2(255 char), primary key (id));
create table DraftChange (id number(19,0) not null, attributeName varchar2(40 char), contentKey varchar2(128 char), createdAt number(19,0) not null, userName varchar2(32 char), value varchar2(4000 char), draft_id number(19,0), primary key (id));

create table Permission (id number(19,0) not null, name varchar2(255 char), primary key (id));
create table Persona (id number(19,0) not null, name varchar2(255 char), primary key (id));
create table Persona_Permission (Persona_id number(19,0) not null, permissions_id number(19,0) not null);
create table UserPersona (userId varchar2(255 char) not null, name varchar2(255 char), persona_id number(19,0), primary key (userId));

alter table DraftChange add constraint FK_DRAFTCHANGE_DRAFT_ID foreign key (draft_id) references Draft;

alter table Persona_Permission add constraint FK_PERSPERM_PERMISSION_ID foreign key (permissions_id) references Permission;
alter table Persona_Permission add constraint FK_PERSPERM_PERSONA_ID foreign key (Persona_id) references Persona;
alter table UserPersona add constraint FK_USERPERM_PERSONA_ID foreign key (persona_id) references Persona;
