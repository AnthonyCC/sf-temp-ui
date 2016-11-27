
Insert into TRANSP.MENU
   (MENU_ID, LINK, MENU_TITLE, ORIENTATION, MENU_PARENT_ID)
Values
   ('OPR014', 'etipping.do', 'E-Tip', 'LEFT', 'MNM002');
Insert into TRANSP.MENU
   (MENU_ID, LINK, MENU_TITLE, ORIENTATION, MENU_PARENT_ID)
Values
   ('OPR013', 'fdetipping.do', 'ETipping Approval', 'LEFT', 'MNM002');
Insert into TRANSP.MENU_PAGE
   (MENU_ID, PAGE_ID)
Values
   ('OPR013', 164);
Insert into TRANSP.MENU_PAGE
   (MENU_ID, PAGE_ID)
Values
   ('OPR014', 149);



UPDATE transp.MENU_COMPANY_CODE SET menu_id='OPR013' WHERE menu_id='ADO025';



UPDATE transp.MENU_COMPANY_CODE SET menu_id='OPR014' WHERE menu_id='ADO020';

delete from transp.MENU_ROLE where menu_id='ADO025'
delete from transp.MENU_ROLE where menu_id='ADO020'


insert into TRANSP.MENU_ROLE(menu_id, role_id) values('OPR014', '10');
insert into TRANSP.MENU_ROLE(menu_id, role_id) values('OPR014', '1');
insert into TRANSP.MENU_ROLE(menu_id, role_id) values('OPR014', '5');
insert into TRANSP.MENU_ROLE(menu_id, role_id) values('OPR014', '6');
insert into TRANSP.MENU_ROLE(menu_id, role_id) values('OPR014', '7');
insert into TRANSP.MENU_ROLE(menu_id, role_id) values('OPR013', '10');


insert into transp.property_master(name, type, default_value, property_enabled) values('fdetip.daily.cron.mail.to','LOG','Opsmanagment@freshdirect.com','X');

commit;