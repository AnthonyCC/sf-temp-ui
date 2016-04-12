create sequence PERSONA_ID_SEQ start with 1 increment by 1;
create table Permission (id number(19,0) not null, name varchar2(255 char), primary key (id));
create table Persona (id number(19,0) not null, name varchar2(255 char), primary key (id));
create table Persona_Permission (Persona_id number(19,0) not null, permissions_id number(19,0) not null);
create table UserPersona (userId varchar2(255 char) not null, name varchar2(255 char), persona_id number(19,0), primary key (userId));
alter table Persona_Permission add constraint FK3b2su379nguu0xmlxs57fwayh foreign key (permissions_id) references Permission;
alter table Persona_Permission add constraint FKp77hxeqgbq1e7q3kacj04j91l foreign key (Persona_id) references Persona;
alter table UserPersona add constraint FK6oaolax3psuph7v2yg8oqxu00 foreign key (persona_id) references Persona;
