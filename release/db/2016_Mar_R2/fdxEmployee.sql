CREATE TABLE TRANSP.FDX_EMPLOYEE
(
  EMPLOYEE_ID  VARCHAR2(25 BYTE),
  FIRST_NAME   VARCHAR2(55 BYTE),
  LAST_NAME    VARCHAR2(55 BYTE),
  JOB_TYPE     VARCHAR2(25 BYTE),
  COMPANY      VARCHAR2(25 BYTE),
  ID           VARCHAR2(55 BYTE)
);

Insert into TRANSP.MENU
   (MENU_ID, LINK, MENU_TITLE, ORIENTATION, MENU_PARENT_ID)
 Values
   ('ADO025', 'fdxEmployee.do', 'Manage FDX Employee', 'RIGHT', 'MNM006');

Insert into TRANSP.PAGES
   (PAGE_ID, PAGE_URI)
 Values
   (165, 'fdxEmployee.do');

Insert into TRANSP.MENU_PAGE
   (MENU_ID, PAGE_ID)
 Values
   ('ADO025', 165);

Insert into TRANSP.MENU_COMPANY_CODE
   (MENU_ID, COMPANY_CODE)
 Values
   ('ADO025', 'fdx');

Insert into TRANSP.MENU_ROLE
   (MENU_ID, ROLE_ID)
 Values
   ('ADO025', 6);



 