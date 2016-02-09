UPDATE transp.menu SET menu_id='OPR013',MENU_PARENT_ID='MNM002' WHERE menu_id='ADO025';

UPDATE transp.menu_page SET menu_id='OPR013' WHERE menu_id='ADO025';

UPDATE transp.MENU_COMPANY_CODE SET menu_id='OPR013' WHERE menu_id='ADO025';

UPDATE transp.menu SET menu_id='OPR014',MENU_PARENT_ID='MNM002' WHERE menu_id='ADO020';

UPDATE transp.menu_page SET menu_id='OPR014' WHERE menu_id='ADO020';

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