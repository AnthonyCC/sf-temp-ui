-- CREATE PERMISSION
insert into permission values (1, 'Can modify FD storedata');
insert into permission values (2, 'Can modify FDX storedata');
insert into permission values (3, 'Can utilize the "Bulk Load" tab');
insert into permission values (4, 'Can utilize the "Changes" tab');
insert into permission values (5, 'Can utilize the "Publish" tab');
insert into permission values (6, 'Can utilize the "Feed Publish" tab');
insert into permission values (7, 'Can utilize the "Administration" tab');
insert into permission values (8, 'Can access the permission manager');
insert into permission values (9, 'FDFolder content type permission');
insert into permission values (10, 'Schedule content type permission');
insert into permission values (11, 'Section content type permission');
insert into permission values (12, 'WebPage content type permission');
insert into permission values (13, 'Can modify Other storedata');
insert into permission values (14, 'Can manage Draft branches');
insert into permission values (15, 'Can edit Draft branches');

-- CREATE PERSONA
insert into persona values(PERSONA_ID_SEQ.nextVal, 'Admin');
insert into persona values(PERSONA_ID_SEQ.nextVal, 'Global Editor');
insert into persona values(PERSONA_ID_SEQ.nextVal, 'Content Editor');
insert into persona values(PERSONA_ID_SEQ.nextVal, 'Foodkick Editor');

-- SETUP PERSONA
insert into persona_permission values(1,1);
insert into persona_permission values(1,2);
insert into persona_permission values(1,3);
insert into persona_permission values(1,4);
insert into persona_permission values(1,5);
insert into persona_permission values(1,6);
insert into persona_permission values(1,7);
insert into persona_permission values(1,8);
insert into persona_permission values(1,13);
insert into persona_permission values(1,14);
insert into persona_permission values(1,15);
insert into persona_permission values(2,1);
insert into persona_permission values(2,2);
insert into persona_permission values(2,3);
insert into persona_permission values(2,4);
insert into persona_permission values(2,5);
insert into persona_permission values(2,6);
insert into persona_permission values(2,13);
insert into persona_permission values(2,14);
insert into persona_permission values(2,15);
insert into persona_permission values(3,1);
insert into persona_permission values(3,2);
insert into persona_permission values(3,3);
insert into persona_permission values(3,4);
insert into persona_permission values(3,13);
insert into persona_permission values(3,14);
insert into persona_permission values(3,15);
insert into persona_permission values(4,6);
insert into persona_permission values(4,9);
insert into persona_permission values(4,10);
insert into persona_permission values(4,11);
insert into persona_permission values(4,12);

-- SETUP USER-PERSONA ASSOCIATION
insert into userpersona values('kcarney','Kristin Carney',1);
insert into userpersona values('wheinzelman','Bill Heinzelman',1);
insert into userpersona values('efox','Emily Fox',2);
insert into userpersona values('wreynolds','Whitney Reynolds',2);
insert into userpersona values('akim','Angela Kim',3);
insert into userpersona values('efeigenbaum','Ethan Feigenbaum',3);
insert into userpersona values('jlepes','Jason Lepes',4);
insert into userpersona values('rkempf','Rachel Kempf',4);
insert into userpersona values('lkos','Lindsay Kos',4);
