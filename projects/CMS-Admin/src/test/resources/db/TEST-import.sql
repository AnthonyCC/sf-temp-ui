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
insert into permission values (16, 'DarkStore content type permission');

-- CREATE PERSONA
insert into persona values(PERSONA_ID_SEQ.nextVal, 'Admin');
insert into persona values(PERSONA_ID_SEQ.nextVal, 'Global Editor');
insert into persona values(PERSONA_ID_SEQ.nextVal, 'Content Editor');
insert into persona values(PERSONA_ID_SEQ.nextVal, 'Foodkick Editor');
