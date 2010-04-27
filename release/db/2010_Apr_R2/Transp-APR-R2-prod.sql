CREATE TABLE TRANSP.EMPLOYEE_STATUS
(
  PERSONNUM  VARCHAR2(15 BYTE)                  NOT NULL,
  STATUS     CHAR(1 BYTE)
);



ALTER TABLE TRANSP.EMPLOYEE_STATUS ADD (
  PRIMARY KEY
 (PERSONNUM));




CREATE TABLE TRANSP.EMPLOYEE_ROLE_SUB_TYPE
(
  CODE         VARCHAR2(16 BYTE)                NOT NULL,
  NAME         VARCHAR2(20 BYTE)                NOT NULL,
  TCODE        VARCHAR2(20 BYTE)                NOT NULL,
  DESCRIPTION  VARCHAR2(60 BYTE)
);


ALTER TABLE TRANSP.EMPLOYEE_ROLE_SUB_TYPE ADD (
  PRIMARY KEY
 (CODE));


ALTER TABLE TRANSP.EMPLOYEE_ROLE_SUB_TYPE ADD (
  CONSTRAINT EMPLOYEE_SUB_TYPE_FK 
 FOREIGN KEY (TCODE) 
 REFERENCES TRANSP.EMPLOYEE_ROLE_TYPE (CODE));

alter table transp.employeerole add( sub_role VARCHAR2(16 BYTE));

ALTER TABLE transp.EMPLOYEEROLE ADD (
  CONSTRAINT ACT_EMPLOYEESUBROLE_FK 
 FOREIGN KEY (SUB_ROLE) 
 REFERENCES transp.EMPLOYEE_ROLE_SUB_TYPE (CODE)); 

Insert into TRANSP.EMPLOYEE_ROLE_SUB_TYPE
   (CODE, NAME, TCODE, DESCRIPTION)
 Values
   ('001', 'Route Driver', '001', 'Route Driver');
Insert into TRANSP.EMPLOYEE_ROLE_SUB_TYPE
   (CODE, NAME, TCODE, DESCRIPTION)
 Values
   ('002', 'Helper', '002', 'Helper');
Insert into TRANSP.EMPLOYEE_ROLE_SUB_TYPE
   (CODE, NAME, TCODE, DESCRIPTION)
 Values
   ('003', 'Runner', '003', 'Runner');
Insert into TRANSP.EMPLOYEE_ROLE_SUB_TYPE
   (CODE, NAME, TCODE, DESCRIPTION)
 Values
   ('004', 'Depot Shuttle Driver', '001', 'Depot Shuttle Driver');
Insert into TRANSP.EMPLOYEE_ROLE_SUB_TYPE
   (CODE, NAME, TCODE, DESCRIPTION)
 Values
   ('005', 'Depot Driver', '001', 'Depot Driver');
Insert into TRANSP.EMPLOYEE_ROLE_SUB_TYPE
   (CODE, NAME, TCODE, DESCRIPTION)
 Values
   ('006', 'Part Time Driver', '001', 'Part Time Driver');
Insert into TRANSP.EMPLOYEE_ROLE_SUB_TYPE
   (CODE, NAME, TCODE, DESCRIPTION)
 Values
   ('007', 'Yard Worker', '001', 'Yard Worker');
Insert into TRANSP.EMPLOYEE_ROLE_SUB_TYPE
   (CODE, NAME, TCODE, DESCRIPTION)
 Values
   ('008', 'MOT', '001', 'MOT');
Insert into TRANSP.EMPLOYEE_ROLE_SUB_TYPE
   (CODE, NAME, TCODE, DESCRIPTION)
 Values
   ('009', 'Vending', '001', 'Vending');
Insert into TRANSP.EMPLOYEE_ROLE_SUB_TYPE
   (CODE, NAME, TCODE, DESCRIPTION)
 Values
   ('010', 'Part Time Helper', '002', 'Part Time Helper');
Insert into TRANSP.EMPLOYEE_ROLE_SUB_TYPE
   (CODE, NAME, TCODE, DESCRIPTION)
 Values
   ('011', 'Part-Time Runner', '003', 'Part-Time Runner');
Insert into TRANSP.EMPLOYEE_ROLE_SUB_TYPE
   (CODE, NAME, TCODE, DESCRIPTION)
 Values
   ('012', 'Manager', '004', 'Manager');



GRANT DELETE, INSERT, SELECT, UPDATE ON  TRANSP.EMPLOYEE_ROLE_SUB_TYPE TO FDTRN_PRDA;
GRANT DELETE, INSERT, SELECT, UPDATE ON  TRANSP.EMPLOYEE_STATUS TO FDTRN_PRDA;


GRANT DELETE, INSERT, SELECT, UPDATE ON  TRANSP.EMPLOYEE_ROLE_SUB_TYPE TO appdev;
GRANT DELETE, INSERT, SELECT, UPDATE ON  TRANSP.EMPLOYEE_STATUS TO appdev;

GRANT DELETE, INSERT, SELECT, UPDATE ON  TRANSP.EMPLOYEE_ROLE_SUB_TYPE TO FDTRN_STPRD01;
GRANT DELETE, INSERT, SELECT, UPDATE ON  TRANSP.EMPLOYEE_STATUS TO FDTRN_STPRD01;

commit;