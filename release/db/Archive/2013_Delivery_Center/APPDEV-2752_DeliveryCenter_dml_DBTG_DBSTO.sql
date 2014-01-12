Insert into TRANSP.EVENTLOG_MESSAGEGROUP
   (GROUP_NAME, EMAIL, CREATEDBY, CROMOD_DATE)
 Values
   ('Fire Truck Management', 'firetruckmanagement@freshdirect.com', 'kkanuganti', TO_DATE('12/11/2012 12:51:23', 'MM/DD/YYYY HH24:MI:SS'));
Insert into TRANSP.EVENTLOG_MESSAGEGROUP
   (GROUP_NAME, EMAIL, CREATEDBY, CROMOD_DATE)
 Values
   ('CSG Senior Staff', 'csgseniorstaff@freshdirect.com', 'kkanuganti', TO_DATE('12/11/2012 12:51:23', 'MM/DD/YYYY HH24:MI:SS'));
Insert into TRANSP.EVENTLOG_MESSAGEGROUP
   (GROUP_NAME, EMAIL, CREATEDBY, CROMOD_DATE)
 Values
   ('Field Managers', 'fieldmanagers@freshdirect.com', 'kkanuganti', TO_DATE('12/11/2012 12:51:23', 'MM/DD/YYYY HH24:MI:SS'));
Insert into TRANSP.EVENTLOG_MESSAGEGROUP
   (GROUP_NAME, EMAIL, CREATEDBY, CROMOD_DATE)
 Values
   ('Frank Mansour', 'fmansour@freshdirect.com', 'kkanuganti', TO_DATE('12/11/2012 12:51:23', 'MM/DD/YYYY HH24:MI:SS'));
Insert into TRANSP.EVENTLOG_MESSAGEGROUP
   (GROUP_NAME, EMAIL, CREATEDBY, CROMOD_DATE)
 Values
   ('George Walker', 'gwalker@freshdirect.com', 'kkanuganti', TO_DATE('12/11/2012 12:51:23', 'MM/DD/YYYY HH24:MI:SS'));
Insert into TRANSP.EVENTLOG_MESSAGEGROUP
   (GROUP_NAME, EMAIL, CREATEDBY, CROMOD_DATE)
 Values
   ('Jay Narvasa', 'jnarvasa@freshdirect.com', 'kkanuganti', TO_DATE('12/11/2012 12:51:23', 'MM/DD/YYYY HH24:MI:SS'));
Insert into TRANSP.EVENTLOG_MESSAGEGROUP
   (GROUP_NAME, EMAIL, CREATEDBY, CROMOD_DATE)
 Values
   ('Katie Whalen', 'kwhalen@freshdirect.com', 'kkanuganti', TO_DATE('12/11/2012 12:51:23', 'MM/DD/YYYY HH24:MI:SS'));
Insert into TRANSP.EVENTLOG_MESSAGEGROUP
   (GROUP_NAME, EMAIL, CREATEDBY, CROMOD_DATE)
 Values
   ('Ops Management', 'opsmanagement@freshdirect.com', 'kkanuganti', TO_DATE('12/11/2012 12:51:23', 'MM/DD/YYYY HH24:MI:SS'));
Insert into TRANSP.EVENTLOG_MESSAGEGROUP
   (GROUP_NAME, EMAIL, CREATEDBY, CROMOD_DATE)
 Values
   ('Ops Management Alerts', 'OpsMgmtAlerts@freshdirect.com', 'kkanuganti', TO_DATE('12/11/2012 12:51:23', 'MM/DD/YYYY HH24:MI:SS'));
Insert into TRANSP.EVENTLOG_MESSAGEGROUP
   (GROUP_NAME, EMAIL, CREATEDBY, CROMOD_DATE)
 Values
   ('Returns Center', 'returnscenter@freshdirect.com', 'kkanuganti', TO_DATE('12/11/2012 12:51:23', 'MM/DD/YYYY HH24:MI:SS'));
Insert into TRANSP.EVENTLOG_MESSAGEGROUP
   (GROUP_NAME, EMAIL, CREATEDBY, CROMOD_DATE)
 Values
   ('Transportation', 'TransportationOperationsTeam@freshdirect.com', 'kkanuganti', TO_DATE('12/11/2012 12:51:23', 'MM/DD/YYYY HH24:MI:SS'));
Insert into TRANSP.EVENTLOG_MESSAGEGROUP
   (GROUP_NAME, EMAIL, CREATEDBY, CROMOD_DATE)
 Values
   ('Transportation Admins', 'TransportationAdmins@freshdirect.com', 'kkanuganti', TO_DATE('12/11/2012 12:51:23', 'MM/DD/YYYY HH24:MI:SS'));
Insert into TRANSP.EVENTLOG_MESSAGEGROUP
   (GROUP_NAME, EMAIL, CREATEDBY, CROMOD_DATE)
 Values
   ('Transportation Shipping Yard', 'transportationshippingyard@freshdirect.com', 'kkanuganti', TO_DATE('12/11/2012 12:51:23', 'MM/DD/YYYY HH24:MI:SS'));
Insert into TRANSP.EVENTLOG_MESSAGEGROUP
   (GROUP_NAME, EMAIL, CREATEDBY, CROMOD_DATE)
 Values
   ('Trucks', 'trucks@freshdirect.com', 'kkanuganti', TO_DATE('12/11/2012 12:51:23', 'MM/DD/YYYY HH24:MI:SS'));


Insert into TRANSP.MOTEVENTLOG_MESSAGEGROUP
   (GROUP_NAME, EMAIL, CREATEDBY, CROMOD_DATE)
 Values
   ('CSG Senior Staff', 'csgseniorstaff@freshdirect.com', 'kkanuganti', TO_DATE('01/14/2013 11:26:35', 'MM/DD/YYYY HH24:MI:SS'));
Insert into TRANSP.MOTEVENTLOG_MESSAGEGROUP
   (GROUP_NAME, EMAIL, CREATEDBY, CROMOD_DATE)
 Values
   ('Ops Management', 'OpsManagement@freshdirect.com', 'kkanuganti', TO_DATE('01/14/2013 11:30:06', 'MM/DD/YYYY HH24:MI:SS'));

   
 
Insert into TRANSP.MOTEVENTTYPE
   (ID, MOTEVENTTYPENAME, MOTEVENTTYPEDESCRIPTION, MSGGROUP_ID, CREATEDBY, DATECREATED, ISACTIVE)
 Values
   ('1', 'Delivery Asst.', 'Delivery Asst.', 'CSG Senior Staff', 'kkanuganti', 
    TO_DATE('01/17/2013 16:18:39', 'MM/DD/YYYY HH24:MI:SS'), 'X');
Insert into TRANSP.MOTEVENTTYPE
   (ID, MOTEVENTTYPENAME, MOTEVENTTYPEDESCRIPTION, MSGGROUP_ID, CREATEDBY, DATECREATED, ISACTIVE)
 Values
   ('2', 'Mechanical Issue', 'Mechanical Issue', 'CSG Senior Staff', 'kkanuganti', 
    TO_DATE('01/17/2013 16:18:39', 'MM/DD/YYYY HH24:MI:SS'), 'X');
Insert into TRANSP.MOTEVENTTYPE
   (ID, MOTEVENTTYPENAME, MOTEVENTTYPEDESCRIPTION, MSGGROUP_ID, CREATEDBY, DATECREATED, ISACTIVE)
 Values
   ('3', 'AirClic Equipment', 'AirClic Equipment', 'CSG Senior Staff', 'kkanuganti', 
    TO_DATE('01/17/2013 16:18:39', 'MM/DD/YYYY HH24:MI:SS'), 'X');
Insert into TRANSP.MOTEVENTTYPE
   (ID, MOTEVENTTYPENAME, MOTEVENTTYPEDESCRIPTION, MSGGROUP_ID, CREATEDBY, DATECREATED, ISACTIVE)
 Values
   ('4', 'Special Delivery', 'Special Delivery', 'CSG Senior Staff', 'kkanuganti', 
    TO_DATE('01/17/2013 16:18:39', 'MM/DD/YYYY HH24:MI:SS'), 'X');
Insert into TRANSP.MOTEVENTTYPE
   (ID, MOTEVENTTYPENAME, MOTEVENTTYPEDESCRIPTION, MSGGROUP_ID, CREATEDBY, DATECREATED, ISACTIVE)
 Values
   ('5', 'Other', 'Other', 'CSG Senior Staff', 'kkanuganti', 
    TO_DATE('01/17/2013 16:18:39', 'MM/DD/YYYY HH24:MI:SS'), 'X');
Insert into TRANSP.MOTEVENTTYPE
   (ID, MOTEVENTTYPENAME, MOTEVENTTYPEDESCRIPTION, MSGGROUP_ID, CREATEDBY, DATECREATED, ISACTIVE)
 Values
   ('6', 'Late Box', 'Late Box', 'CSG Senior Staff', 'kkanuganti', 
    TO_DATE('01/17/2013 16:18:39', 'MM/DD/YYYY HH24:MI:SS'), 'X');
Insert into TRANSP.MOTEVENTTYPE
   (ID, MOTEVENTTYPENAME, MOTEVENTTYPEDESCRIPTION, MSGGROUP_ID, CREATEDBY, DATECREATED, ISACTIVE)
 Values
   ('7', 'Note from Ops Ctr', 'Note from Ops Ctr', 'CSG Senior Staff', 'kkanuganti', 
    TO_DATE('01/17/2013 16:18:39', 'MM/DD/YYYY HH24:MI:SS'), 'X');
Insert into TRANSP.MOTEVENTTYPE
   (ID, MOTEVENTTYPENAME, MOTEVENTTYPEDESCRIPTION, MSGGROUP_ID, CREATEDBY, DATECREATED, ISACTIVE)
 Values
   ('8', 'End of Shift', 'End of Shift', 'CSG Senior Staff', 'kkanuganti', 
    TO_DATE('01/17/2013 16:18:40', 'MM/DD/YYYY HH24:MI:SS'), 'X');
Insert into TRANSP.MOTEVENTTYPE
   (ID, MOTEVENTTYPENAME, MOTEVENTTYPEDESCRIPTION, MSGGROUP_ID, CREATEDBY, DATECREATED, ISACTIVE)
 Values
   ('9', 'Risk', 'Risk', 'CSG Senior Staff', 'kkanuganti', 
    TO_DATE('01/17/2013 16:18:40', 'MM/DD/YYYY HH24:MI:SS'), 'X');
Insert into TRANSP.MOTEVENTTYPE
   (ID, MOTEVENTTYPENAME, MOTEVENTTYPEDESCRIPTION, MSGGROUP_ID, CREATEDBY, DATECREATED, ISACTIVE)
 Values
   ('10', 'Pick-Up', 'Pick-Up', 'CSG Senior Staff', 'kkanuganti', 
    TO_DATE('01/17/2013 16:18:40', 'MM/DD/YYYY HH24:MI:SS'), 'X');
Insert into TRANSP.MOTEVENTTYPE
   (ID, MOTEVENTTYPENAME, MOTEVENTTYPEDESCRIPTION, MSGGROUP_ID, CREATEDBY, DATECREATED, ISACTIVE)
 Values
   ('11', 'Route Refused Help', 'Route Refused Help', 'CSG Senior Staff', 'kkanuganti', 
    TO_DATE('01/17/2013 16:18:40', 'MM/DD/YYYY HH24:MI:SS'), 'X');
Insert into TRANSP.MOTEVENTTYPE
   (ID, MOTEVENTTYPENAME, MOTEVENTTYPEDESCRIPTION, MSGGROUP_ID, CREATEDBY, DATECREATED, ISACTIVE)
 Values
   ('12', 'Note on >30 Min Late', 'Note on >30 Min Late', 'CSG Senior Staff', 'kkanuganti', 
    TO_DATE('01/17/2013 16:18:40', 'MM/DD/YYYY HH24:MI:SS'), 'X');
Insert into TRANSP.MOTEVENTTYPE
   (ID, MOTEVENTTYPENAME, MOTEVENTTYPEDESCRIPTION, MSGGROUP_ID, CREATEDBY, DATECREATED, ISACTIVE)
 Values
   ('13', 'Start of Shift', 'Start of Shift', 'CSG Senior Staff', 'kkanuganti', 
    TO_DATE('01/17/2013 16:18:40', 'MM/DD/YYYY HH24:MI:SS'), 'X');
Insert into TRANSP.MOTEVENTTYPE
   (ID, MOTEVENTTYPENAME, MOTEVENTTYPEDESCRIPTION, MSGGROUP_ID, CREATEDBY, DATECREATED, ISACTIVE)
 Values
   ('14', 'MOT Recap', 'MOT Recap', 'CSG Senior Staff', 'kkanuganti', 
    TO_DATE('01/17/2013 16:18:40', 'MM/DD/YYYY HH24:MI:SS'), 'X');
Insert into TRANSP.MOTEVENTTYPE
   (ID, MOTEVENTTYPENAME, MOTEVENTTYPEDESCRIPTION, MSGGROUP_ID, CREATEDBY, DATECREATED)
 Values
   ('15', 'No MOT Available', 'No MOT Available', 'Ops Management', 'kkanuganti', 
    TO_DATE('01/17/2013 16:18:40', 'MM/DD/YYYY HH24:MI:SS'));
Insert into TRANSP.MOTEVENTTYPE
   (ID, MOTEVENTTYPENAME, MOTEVENTTYPEDESCRIPTION, MSGGROUP_ID, CREATEDBY, DATECREATED, ISACTIVE)
 Values
   ('16', 'MOT Recap - Product', 'MOT Recap - Product', 'CSG Senior Staff', 'kkanuganti', 
    TO_DATE('01/17/2013 16:18:40', 'MM/DD/YYYY HH24:MI:SS'), 'X');
Insert into TRANSP.MOTEVENTTYPE
   (ID, MOTEVENTTYPENAME, MOTEVENTTYPEDESCRIPTION, MSGGROUP_ID, CREATEDBY, DATECREATED, ISACTIVE)
 Values
   ('17', 'MOT Recap - People', 'MOT Recap - People', 'CSG Senior Staff', 'kkanuganti', 
    TO_DATE('01/17/2013 16:18:41', 'MM/DD/YYYY HH24:MI:SS'), 'X');
Insert into TRANSP.MOTEVENTTYPE
   (ID, MOTEVENTTYPENAME, MOTEVENTTYPEDESCRIPTION, MSGGROUP_ID, CREATEDBY, DATECREATED, ISACTIVE)
 Values
   ('18', 'MOT Recap - Equipmen', 'MOT Recap - Equipment', 'CSG Senior Staff', 'kkanuganti', 
    TO_DATE('01/17/2013 16:18:41', 'MM/DD/YYYY HH24:MI:SS'), 'X');
Insert into TRANSP.MOTEVENTTYPE
   (ID, MOTEVENTTYPENAME, MOTEVENTTYPEDESCRIPTION, MSGGROUP_ID, CREATEDBY, DATECREATED, ISACTIVE)
 Values
   ('19', 'Request Canceled', 'Request Canceled', 'CSG Senior Staff', 'kkanuganti', 
    TO_DATE('01/17/2013 16:18:41', 'MM/DD/YYYY HH24:MI:SS'), 'X');
Insert into TRANSP.MOTEVENTTYPE
   (ID, MOTEVENTTYPENAME, MOTEVENTTYPEDESCRIPTION, MSGGROUP_ID, CREATEDBY, DATECREATED, ISACTIVE)
 Values
   ('20', 'MOT Not Available', 'MOT Not Available', 'Ops Management', 'kkanuganti', 
    TO_DATE('01/17/2013 16:18:41', 'MM/DD/YYYY HH24:MI:SS'), 'X');
   
 
    
 
Insert into TRANSP.EVENTTYPE
   (ID, EVENTTYPENAME, EVENTTYPEDESCRIPTION, CREATEDBY, DATECREATED, ISEMPLOYEEREQUIRED, ISLOCATIONREQUIRED)
 Values
   ('6', 'Accident', 'Log of incident', 'kkanuganti', TO_DATE('01/17/2013 16:15:01', 'MM/DD/YYYY HH24:MI:SS'), 
    'X', 'X');
Insert into TRANSP.EVENTTYPE
   (ID, EVENTTYPENAME, EVENTTYPEDESCRIPTION, CREATEDBY, DATECREATED)
 Values
   ('7', 'Late Dispatch', 'Truck leaving facility due to various reasons', 'kkanuganti', TO_DATE('01/17/2013 16:15:01', 'MM/DD/YYYY HH24:MI:SS'));
Insert into TRANSP.EVENTTYPE
   (ID, EVENTTYPENAME, EVENTTYPEDESCRIPTION, CREATEDBY, DATECREATED)
 Values
   ('8', 'Out of Service', 'Out of service notifications', 'kkanuganti', TO_DATE('01/17/2013 16:15:02', 'MM/DD/YYYY HH24:MI:SS'));
Insert into TRANSP.EVENTTYPE
   (ID, EVENTTYPENAME, EVENTTYPEDESCRIPTION, CREATEDBY, DATECREATED)
 Values
   ('10', 'Delivery Issues', 'Late delivery and recovery issues', 'kkanuganti', TO_DATE('01/17/2013 16:15:02', 'MM/DD/YYYY HH24:MI:SS'));
Insert into TRANSP.EVENTTYPE
   (ID, EVENTTYPENAME, EVENTTYPEDESCRIPTION, CREATEDBY, DATECREATED, ISEMPLOYEEREQUIRED, ISACTIVE)
 Values
   ('11', 'Employee Issues', 'Log of employee follow-up needed', 'kkanuganti', TO_DATE('01/17/2013 16:15:02', 'MM/DD/YYYY HH24:MI:SS'), 
    'X', 'X');
Insert into TRANSP.EVENTTYPE
   (ID, EVENTTYPENAME, EVENTTYPEDESCRIPTION, CREATEDBY, DATECREATED)
 Values
   ('12', 'Other', 'Various', 'kkanuganti', TO_DATE('01/17/2013 16:15:02', 'MM/DD/YYYY HH24:MI:SS'));
Insert into TRANSP.EVENTTYPE
   (ID, EVENTTYPENAME, EVENTTYPEDESCRIPTION, CREATEDBY, DATECREATED, ISEMPLOYEEREQUIRED)
 Values
   ('13', 'Missing Box Follow-U', 'Entries related to assigned MB follow-up', 'kkanuganti', TO_DATE('01/17/2013 16:15:02', 'MM/DD/YYYY HH24:MI:SS'), 
    'X');
Insert into TRANSP.EVENTTYPE
   (ID, EVENTTYPENAME, EVENTTYPEDESCRIPTION, CREATEDBY, DATECREATED)
 Values
   ('14', 'Scanning', 'Scanning Issues', 'kkanuganti', TO_DATE('01/17/2013 16:15:02', 'MM/DD/YYYY HH24:MI:SS'));
Insert into TRANSP.EVENTTYPE
   (ID, EVENTTYPENAME, EVENTTYPEDESCRIPTION, CREATEDBY, DATECREATED, ISEMPLOYEEREQUIRED, ISACTIVE)
 Values
   ('16', 'Field Audit', 'Field Audit', 'kkanuganti', TO_DATE('01/17/2013 16:15:02', 'MM/DD/YYYY HH24:MI:SS'), 
    'X', 'X');
Insert into TRANSP.EVENTTYPE
   (ID, EVENTTYPENAME, EVENTTYPEDESCRIPTION, CREATEDBY, DATECREATED, ISEMPLOYEEREQUIRED)
 Values
   ('17', 'Late', 'bike tour', 'kkanuganti', TO_DATE('01/17/2013 16:15:02', 'MM/DD/YYYY HH24:MI:SS'), 
    'X');
Insert into TRANSP.EVENTTYPE
   (ID, EVENTTYPENAME, EVENTTYPEDESCRIPTION, CREATEDBY, DATECREATED)
 Values
   ('19', 'Kronos', 'System Not Working', 'kkanuganti', TO_DATE('01/17/2013 16:15:02', 'MM/DD/YYYY HH24:MI:SS'));
Insert into TRANSP.EVENTTYPE
   (ID, EVENTTYPENAME, EVENTTYPEDESCRIPTION, CREATEDBY, DATECREATED)
 Values
   ('21', 'Pro Foods', 'Pro Foods', 'kkanuganti', TO_DATE('01/17/2013 16:15:03', 'MM/DD/YYYY HH24:MI:SS'));
Insert into TRANSP.EVENTTYPE
   (ID, EVENTTYPENAME, EVENTTYPEDESCRIPTION, CREATEDBY, DATECREATED)
 Values
   ('22', 'Returns Truck ProFoods ', 'Returns truck is ready', 'kkanuganti', TO_DATE('01/17/2013 16:15:03', 'MM/DD/YYYY HH24:MI:SS'));
Insert into TRANSP.EVENTTYPE
   (ID, EVENTTYPENAME, EVENTTYPEDESCRIPTION, CREATEDBY, DATECREATED, ISLOCATIONREQUIRED, ISACTIVE)
 Values
   ('23', 'MOT Specials', 'Events concerning MOT/Fire Truck work', 'kkanuganti', TO_DATE('01/17/2013 16:15:03', 'MM/DD/YYYY HH24:MI:SS'), 
    'X', 'X');
Insert into TRANSP.EVENTTYPE
   (ID, EVENTTYPENAME, EVENTTYPEDESCRIPTION, CREATEDBY, DATECREATED, ISACTIVE)
 Values
   ('24', 'Action Taken by Ops', 'Used only for ops DSS Reports', 'kkanuganti', TO_DATE('01/17/2013 16:15:03', 'MM/DD/YYYY HH24:MI:SS'), 
    'X');
Insert into TRANSP.EVENTTYPE
   (ID, EVENTTYPENAME, EVENTTYPEDESCRIPTION, CREATEDBY, DATECREATED, ISACTIVE)
 Values
   ('26', 'Action Taken by Planning', 'Transportation Planning Action', 'kkanuganti', TO_DATE('01/17/2013 16:15:03', 'MM/DD/YYYY HH24:MI:SS'), 
    'X');
Insert into TRANSP.EVENTTYPE
   (ID, EVENTTYPENAME, EVENTTYPEDESCRIPTION, CREATEDBY, DATECREATED, ISEMPLOYEEREQUIRED, ISLOCATIONREQUIRED, ISACTIVE)
 Values
   ('27', 'Ride Along Audit', 'Ride Along Audit', 'kkanuganti', TO_DATE('01/17/2013 16:15:03', 'MM/DD/YYYY HH24:MI:SS'), 
    'X', 'X', 'X');
Insert into TRANSP.EVENTTYPE
   (ID, EVENTTYPENAME, EVENTTYPEDESCRIPTION, CREATEDBY, DATECREATED, ISEMPLOYEEREQUIRED)
 Values
   ('28', 'Interviews', 'Interviews', 'kkanuganti', TO_DATE('01/17/2013 16:15:03', 'MM/DD/YYYY HH24:MI:SS'), 
    'X');
Insert into TRANSP.EVENTTYPE
   (ID, EVENTTYPENAME, EVENTTYPEDESCRIPTION, CREATEDBY, DATECREATED, ISEMPLOYEEREQUIRED)
 Values
   ('29', 'Training', 'Training', 'kkanuganti', TO_DATE('01/17/2013 16:15:03', 'MM/DD/YYYY HH24:MI:SS'), 
    'X');
Insert into TRANSP.EVENTTYPE
   (ID, EVENTTYPENAME, EVENTTYPEDESCRIPTION, CREATEDBY, DATECREATED, ISEMPLOYEEREQUIRED, ISLOCATIONREQUIRED)
 Values
   ('30', 'yard', 'set up', 'kkanuganti', TO_DATE('01/17/2013 16:15:03', 'MM/DD/YYYY HH24:MI:SS'), 
    'X', 'X');
Insert into TRANSP.EVENTTYPE
   (ID, EVENTTYPENAME, EVENTTYPEDESCRIPTION, CREATEDBY, DATECREATED)
 Values
   ('31', 'DSS6', 'DSS screen', 'kkanuganti', TO_DATE('01/17/2013 16:15:04', 'MM/DD/YYYY HH24:MI:SS'));
Insert into TRANSP.EVENTTYPE
   (ID, EVENTTYPENAME, EVENTTYPEDESCRIPTION, CREATEDBY, DATECREATED)
 Values
   ('32', 'Telargo Alert', 'Telargo Alert Email', 'kkanuganti', TO_DATE('01/17/2013 16:15:04', 'MM/DD/YYYY HH24:MI:SS'));
Insert into TRANSP.EVENTTYPE
   (ID, EVENTTYPENAME, EVENTTYPEDESCRIPTION, CREATEDBY, DATECREATED, ISACTIVE)
 Values
   ('33', 'TGO Manual Deactivation', 'TGO Manual Deactivation', 'kkanuganti', TO_DATE('01/17/2013 16:15:04', 'MM/DD/YYYY HH24:MI:SS'), 
    'X');
Insert into TRANSP.EVENTTYPE
   (ID, EVENTTYPENAME, EVENTTYPEDESCRIPTION, CREATEDBY, DATECREATED, ISEMPLOYEEREQUIRED, ISLOCATIONREQUIRED, ISACTIVE)
 Values
   ('34', 'Muni Meter Card', 'Muni Meter Card', 'kkanuganti', TO_DATE('01/17/2013 16:15:04', 'MM/DD/YYYY HH24:MI:SS'), 
    'X', 'X', 'X');
Insert into TRANSP.EVENTTYPE
   (ID, EVENTTYPENAME, EVENTTYPEDESCRIPTION, CREATEDBY, DATECREATED, ISACTIVE)
 Values
   ('35', 'TGO Excessive Back Door', 'TGO Excessive Back Door', 'kkanuganti', TO_DATE('01/17/2013 16:15:04', 'MM/DD/YYYY HH24:MI:SS'), 
    'X');
Insert into TRANSP.EVENTTYPE
   (ID, EVENTTYPENAME, EVENTTYPEDESCRIPTION, CREATEDBY, DATECREATED, ISACTIVE)
 Values
   ('36', 'TGO Excessive Freezer Door', 'TGO Excessive Freezer Door', 'kkanuganti', TO_DATE('01/17/2013 16:15:04', 'MM/DD/YYYY HH24:MI:SS'), 
    'X');
Insert into TRANSP.EVENTTYPE
   (ID, EVENTTYPENAME, EVENTTYPEDESCRIPTION, CREATEDBY, DATECREATED, ISACTIVE)
 Values
   ('37', 'AA Late Box Feedback', 'AA Late Box Feedback', 'kkanuganti', TO_DATE('01/17/2013 16:15:04', 'MM/DD/YYYY HH24:MI:SS'), 
    'X');
Insert into TRANSP.EVENTTYPE
   (ID, EVENTTYPENAME, EVENTTYPEDESCRIPTION, CREATEDBY, DATECREATED)
 Values
   ('38', 'Recommendation by Planning Team', 'Planning Team Recommends', 'kkanuganti', TO_DATE('01/17/2013 16:15:04', 'MM/DD/YYYY HH24:MI:SS'));
Insert into TRANSP.EVENTTYPE
   (ID, EVENTTYPENAME, EVENTTYPEDESCRIPTION, CREATEDBY, DATECREATED, ISACTIVE)
 Values
   ('39', 'Bottom_Routes', 'Bottom_Routes', 'kkanuganti', TO_DATE('01/17/2013 16:15:04', 'MM/DD/YYYY HH24:MI:SS'), 
    'X');
Insert into TRANSP.EVENTTYPE
   (ID, EVENTTYPENAME, EVENTTYPEDESCRIPTION, CREATEDBY, DATECREATED, ISACTIVE)
 Values
   ('40', 'TGO-Door Open While Driving', 'Driving while door open and speed > 10mph', 'kkanuganti', TO_DATE('01/17/2013 16:15:04', 'MM/DD/YYYY HH24:MI:SS'), 
    'X');
Insert into TRANSP.EVENTTYPE
   (ID, EVENTTYPENAME, EVENTTYPEDESCRIPTION, CREATEDBY, DATECREATED, ISACTIVE)
 Values
   ('41', 'TGO DPF Light', 'Diesel Particulate Filters (DPF) Light is on', 'kkanuganti', TO_DATE('01/17/2013 16:15:04', 'MM/DD/YYYY HH24:MI:SS'), 
    'X');
Insert into TRANSP.EVENTTYPE
   (ID, EVENTTYPENAME, EVENTTYPEDESCRIPTION, CREATEDBY, DATECREATED)
 Values
   ('42', 'Emergency Brake', 'Emergency Brake is engaged and truck is out o', 'kkanuganti', TO_DATE('01/17/2013 16:15:04', 'MM/DD/YYYY HH24:MI:SS'));
Insert into TRANSP.EVENTTYPE
   (ID, EVENTTYPENAME, EVENTTYPEDESCRIPTION, CREATEDBY, DATECREATED, ISACTIVE)
 Values
   ('43', 'TGO Emergency Brake', 'Emergency Brake is engaged and truck is out o', 'kkanuganti', TO_DATE('01/17/2013 16:15:04', 'MM/DD/YYYY HH24:MI:SS'), 
    'X');
Insert into TRANSP.EVENTTYPE
   (ID, EVENTTYPENAME, EVENTTYPEDESCRIPTION, CREATEDBY, DATECREATED)
 Values
   ('44', 'Nextel Inbound Log', 'Nextel Inbound Log', 'kkanuganti', TO_DATE('01/17/2013 16:15:04', 'MM/DD/YYYY HH24:MI:SS'));
Insert into TRANSP.EVENTTYPE
   (ID, EVENTTYPENAME, EVENTTYPEDESCRIPTION, CREATEDBY, DATECREATED, ISACTIVE)
 Values
   ('45', 'MOT Assistance', 'MOT Assistance', 'kkanuganti', TO_DATE('01/17/2013 16:15:05', 'MM/DD/YYYY HH24:MI:SS'), 
    'X');
Insert into TRANSP.EVENTTYPE
   (ID, EVENTTYPENAME, EVENTTYPEDESCRIPTION, CREATEDBY, DATECREATED, ISACTIVE)
 Values
   ('46', 'Fraud Review', 'Fraud Review', 'kkanuganti', TO_DATE('01/17/2013 16:15:05', 'MM/DD/YYYY HH24:MI:SS'), 
    'X');
Insert into TRANSP.EVENTTYPE
   (ID, EVENTTYPENAME, EVENTTYPEDESCRIPTION, CREATEDBY, DATECREATED, ISACTIVE)
 Values
   ('47', 'Carton Issues', 'Carton Related Issues', 'kkanuganti', TO_DATE('01/17/2013 16:15:05', 'MM/DD/YYYY HH24:MI:SS'), 
    'X');
Insert into TRANSP.EVENTTYPE
   (ID, EVENTTYPENAME, EVENTTYPEDESCRIPTION, CREATEDBY, DATECREATED, ISLOCATIONREQUIRED)
 Values
   ('48', 'Phone issues', 'Intermec issues', 'kkanuganti', TO_DATE('01/17/2013 16:15:05', 'MM/DD/YYYY HH24:MI:SS'), 
    'X');
Insert into TRANSP.EVENTTYPE
   (ID, EVENTTYPENAME, EVENTTYPEDESCRIPTION, CREATEDBY, DATECREATED, ISEMPLOYEEREQUIRED)
 Values
   ('49', 'Intermec issues', 'Phone issues', 'kkanuganti', TO_DATE('01/17/2013 16:15:05', 'MM/DD/YYYY HH24:MI:SS'), 
    'X');
Insert into TRANSP.EVENTTYPE
   (ID, EVENTTYPENAME, EVENTTYPEDESCRIPTION, CREATEDBY, DATECREATED, ISACTIVE)
 Values
   ('50', 'Ops Supervision', 'Ops Supervision', 'kkanuganti', TO_DATE('01/17/2013 16:15:05', 'MM/DD/YYYY HH24:MI:SS'), 
    'X');
Insert into TRANSP.EVENTTYPE
   (ID, EVENTTYPENAME, EVENTTYPEDESCRIPTION, CREATEDBY, DATECREATED, ISACTIVE)
 Values
   ('51', 'End of Shift Scanner Log', 'End of Shift Scanner Log', 'kkanuganti', TO_DATE('01/17/2013 16:15:05', 'MM/DD/YYYY HH24:MI:SS'), 
    'X');
Insert into TRANSP.EVENTTYPE
   (ID, EVENTTYPENAME, EVENTTYPEDESCRIPTION, CREATEDBY, DATECREATED)
 Values
   ('9', 'Mechanical Failure and Service', 'Mechanical Failure and Service', 'kkanuganti', TO_DATE('01/17/2013 16:15:05', 'MM/DD/YYYY HH24:MI:SS'));

   

Insert into TRANSP.EVENTSUBTYPE
   (ID, EVENTSUBTYPENAME, EVENTSUBTYPEDESCRIPTION, EVENTTYPEID, CREATEDBY, DATECREATED, MSGGROUP_ID, ISACTIVE)
 Values
   ('2', 'Damaged Mirror', 'Log of incident', '6', 'kkanuganti', 
    TO_DATE('01/23/2013 10:37:00', 'MM/DD/YYYY HH24:MI:SS'), 'Transportation', 'X');
Insert into TRANSP.EVENTSUBTYPE
   (ID, EVENTSUBTYPENAME, EVENTSUBTYPEDESCRIPTION, EVENTTYPEID, CREATEDBY, DATECREATED, MSGGROUP_ID, ISACTIVE)
 Values
   ('3', 'Parked Vehicle', 'Log of incident', '6', 'kkanuganti', 
    TO_DATE('01/23/2013 10:37:00', 'MM/DD/YYYY HH24:MI:SS'), 'Transportation', 'X');
Insert into TRANSP.EVENTSUBTYPE
   (ID, EVENTSUBTYPENAME, EVENTSUBTYPEDESCRIPTION, EVENTTYPEID, CREATEDBY, DATECREATED, MSGGROUP_ID, ISACTIVE)
 Values
   ('4', 'Moving Vehicle', 'Log of incident', '6', 'kkanuganti', 
    TO_DATE('01/23/2013 10:37:00', 'MM/DD/YYYY HH24:MI:SS'), 'Transportation', 'X');
Insert into TRANSP.EVENTSUBTYPE
   (ID, EVENTSUBTYPENAME, EVENTSUBTYPEDESCRIPTION, EVENTTYPEID, CREATEDBY, DATECREATED, MSGGROUP_ID, ISACTIVE)
 Values
   ('5', 'Keys', 'Late dispatch due to keys', '7', 'kkanuganti', 
    TO_DATE('01/23/2013 10:37:00', 'MM/DD/YYYY HH24:MI:SS'), 'Transportation', 'X');
Insert into TRANSP.EVENTSUBTYPE
   (ID, EVENTSUBTYPENAME, EVENTSUBTYPEDESCRIPTION, EVENTTYPEID, CREATEDBY, DATECREATED, MSGGROUP_ID, ISACTIVE)
 Values
   ('6', 'Plant', 'Late dispatch due to plant', '7', 'kkanuganti', 
    TO_DATE('01/23/2013 10:37:00', 'MM/DD/YYYY HH24:MI:SS'), 'Transportation', 'X');
Insert into TRANSP.EVENTSUBTYPE
   (ID, EVENTSUBTYPENAME, EVENTSUBTYPEDESCRIPTION, EVENTTYPEID, CREATEDBY, DATECREATED, MSGGROUP_ID, ISACTIVE)
 Values
   ('7', 'Staffing', 'Late dispatch due to staffing', '7', 'kkanuganti', 
    TO_DATE('01/23/2013 10:37:00', 'MM/DD/YYYY HH24:MI:SS'), 'Transportation', 'X');
Insert into TRANSP.EVENTSUBTYPE
   (ID, EVENTSUBTYPENAME, EVENTSUBTYPEDESCRIPTION, EVENTTYPEID, CREATEDBY, DATECREATED, MSGGROUP_ID, ISACTIVE)
 Values
   ('8', 'Bio-Diesel', 'Bio-Diesel Issues', '12', 'kkanuganti', 
    TO_DATE('01/23/2013 10:37:00', 'MM/DD/YYYY HH24:MI:SS'), 'Trucks', 'X');
Insert into TRANSP.EVENTSUBTYPE
   (ID, EVENTSUBTYPENAME, EVENTSUBTYPEDESCRIPTION, EVENTTYPEID, CREATEDBY, DATECREATED, ISACTIVE)
 Values
   ('9', 'Staffing', 'Staffing Issues', '11', 'kkanuganti', 
    TO_DATE('01/23/2013 10:37:00', 'MM/DD/YYYY HH24:MI:SS'), 'X');
Insert into TRANSP.EVENTSUBTYPE
   (ID, EVENTSUBTYPENAME, EVENTSUBTYPEDESCRIPTION, EVENTTYPEID, CREATEDBY, DATECREATED, ISACTIVE)
 Values
   ('10', 'Training', 'Training Class Problems', '11', 'kkanuganti', 
    TO_DATE('01/23/2013 10:37:00', 'MM/DD/YYYY HH24:MI:SS'), 'X');
Insert into TRANSP.EVENTSUBTYPE
   (ID, EVENTSUBTYPENAME, EVENTSUBTYPEDESCRIPTION, EVENTTYPEID, CREATEDBY, DATECREATED, ISACTIVE)
 Values
   ('11', 'Open', 'Open Follow-Up', '13', 'kkanuganti', 
    TO_DATE('01/23/2013 10:37:00', 'MM/DD/YYYY HH24:MI:SS'), 'X');
Insert into TRANSP.EVENTSUBTYPE
   (ID, EVENTSUBTYPENAME, EVENTSUBTYPEDESCRIPTION, EVENTTYPEID, CREATEDBY, DATECREATED, MSGGROUP_ID, ISACTIVE)
 Values
   ('12', 'Completed', 'Closed Follow-Up', '13', 'kkanuganti', 
    TO_DATE('01/23/2013 10:37:00', 'MM/DD/YYYY HH24:MI:SS'), 'Transportation', 'X');
Insert into TRANSP.EVENTSUBTYPE
   (ID, EVENTSUBTYPENAME, EVENTSUBTYPEDESCRIPTION, EVENTTYPEID, CREATEDBY, DATECREATED, MSGGROUP_ID, ISACTIVE)
 Values
   ('13', 'Dead Battery', 'Dead Battery', '9', 'kkanuganti', 
    TO_DATE('01/23/2013 10:37:00', 'MM/DD/YYYY HH24:MI:SS'), 'Returns Center', 'X');
Insert into TRANSP.EVENTSUBTYPE
   (ID, EVENTSUBTYPENAME, EVENTSUBTYPEDESCRIPTION, EVENTTYPEID, CREATEDBY, DATECREATED, MSGGROUP_ID, ISACTIVE)
 Values
   ('14', 'Flat Tire', 'Flat Tire', '9', 'kkanuganti', 
    TO_DATE('01/23/2013 10:37:00', 'MM/DD/YYYY HH24:MI:SS'), 'Returns Center', 'X');
Insert into TRANSP.EVENTSUBTYPE
   (ID, EVENTSUBTYPENAME, EVENTSUBTYPEDESCRIPTION, EVENTTYPEID, CREATEDBY, DATECREATED, ISACTIVE)
 Values
   ('16', 'Freeze', 'Phones Freezes', '14', 'kkanuganti', 
    TO_DATE('01/23/2013 10:37:00', 'MM/DD/YYYY HH24:MI:SS'), 'X');
Insert into TRANSP.EVENTSUBTYPE
   (ID, EVENTSUBTYPENAME, EVENTSUBTYPEDESCRIPTION, EVENTTYPEID, CREATEDBY, DATECREATED, ISACTIVE)
 Values
   ('17', 'Battery Low', 'Low Battery', '14', 'kkanuganti', 
    TO_DATE('01/23/2013 10:37:00', 'MM/DD/YYYY HH24:MI:SS'), 'X');
Insert into TRANSP.EVENTSUBTYPE
   (ID, EVENTSUBTYPENAME, EVENTSUBTYPEDESCRIPTION, EVENTTYPEID, CREATEDBY, DATECREATED, MSGGROUP_ID, ISACTIVE)
 Values
   ('18', 'Driver Unreachable', 'Driver is not responding', '14', 'kkanuganti', 
    TO_DATE('01/23/2013 10:37:00', 'MM/DD/YYYY HH24:MI:SS'), 'Field Managers', 'X');
Insert into TRANSP.EVENTSUBTYPE
   (ID, EVENTSUBTYPENAME, EVENTSUBTYPEDESCRIPTION, EVENTTYPEID, CREATEDBY, DATECREATED, ISACTIVE)
 Values
   ('19', 'Helper Unreachable', 'Helper is not responding', '14', 'kkanuganti', 
    TO_DATE('01/23/2013 10:37:00', 'MM/DD/YYYY HH24:MI:SS'), 'X');
Insert into TRANSP.EVENTSUBTYPE
   (ID, EVENTSUBTYPENAME, EVENTSUBTYPEDESCRIPTION, EVENTTYPEID, CREATEDBY, DATECREATED, ISACTIVE)
 Values
   ('21', 'Training', 'Temp Employee is not scanning', '14', 'kkanuganti', 
    TO_DATE('01/23/2013 10:37:00', 'MM/DD/YYYY HH24:MI:SS'), 'X');
Insert into TRANSP.EVENTSUBTYPE
   (ID, EVENTSUBTYPENAME, EVENTSUBTYPEDESCRIPTION, EVENTTYPEID, CREATEDBY, DATECREATED, ISACTIVE)
 Values
   ('22', 'Laser Problems', 'Laser is Weak/Slow', '14', 'kkanuganti', 
    TO_DATE('01/23/2013 10:37:00', 'MM/DD/YYYY HH24:MI:SS'), 'X');
Insert into TRANSP.EVENTSUBTYPE
   (ID, EVENTSUBTYPENAME, EVENTSUBTYPEDESCRIPTION, EVENTTYPEID, CREATEDBY, DATECREATED, ISACTIVE)
 Values
   ('23', 'Not Scanning', 'Running Late/Refusal to Scan', '14', 'kkanuganti', 
    TO_DATE('01/23/2013 10:37:00', 'MM/DD/YYYY HH24:MI:SS'), 'X');
Insert into TRANSP.EVENTSUBTYPE
   (ID, EVENTSUBTYPENAME, EVENTSUBTYPEDESCRIPTION, EVENTTYPEID, CREATEDBY, DATECREATED, ISACTIVE)
 Values
   ('24', 'No Equipment', 'No Equipment to Scan', '14', 'kkanuganti', 
    TO_DATE('01/23/2013 10:37:00', 'MM/DD/YYYY HH24:MI:SS'), 'X');
Insert into TRANSP.EVENTSUBTYPE
   (ID, EVENTSUBTYPENAME, EVENTSUBTYPEDESCRIPTION, EVENTTYPEID, CREATEDBY, DATECREATED, ISACTIVE)
 Values
   ('25', 'Error - Comm Error', 'Comm Error on Phone', '14', 'kkanuganti', 
    TO_DATE('01/23/2013 10:37:00', 'MM/DD/YYYY HH24:MI:SS'), 'X');
Insert into TRANSP.EVENTSUBTYPE
   (ID, EVENTSUBTYPENAME, EVENTSUBTYPEDESCRIPTION, EVENTTYPEID, CREATEDBY, DATECREATED, MSGGROUP_ID, ISACTIVE)
 Values
   ('26', 'Out/Low Fuel', 'Ran out of Fuel', '10', 'kkanuganti', 
    TO_DATE('01/23/2013 10:37:00', 'MM/DD/YYYY HH24:MI:SS'), 'Transportation', 'X');
Insert into TRANSP.EVENTSUBTYPE
   (ID, EVENTSUBTYPENAME, EVENTSUBTYPEDESCRIPTION, EVENTTYPEID, CREATEDBY, DATECREATED, ISACTIVE)
 Values
   ('27', 'Tardy', 'Phone Call to Report Late to Work', '11', 'kkanuganti', 
    TO_DATE('01/23/2013 10:37:00', 'MM/DD/YYYY HH24:MI:SS'), 'X');
Insert into TRANSP.EVENTSUBTYPE
   (ID, EVENTSUBTYPENAME, EVENTSUBTYPEDESCRIPTION, EVENTTYPEID, CREATEDBY, DATECREATED, MSGGROUP_ID, ISACTIVE)
 Values
   ('28', 'Call-In', 'Emplyoee Called in Absent', '11', 'kkanuganti', 
    TO_DATE('01/23/2013 10:37:00', 'MM/DD/YYYY HH24:MI:SS'), 'Transportation', 'X');
Insert into TRANSP.EVENTSUBTYPE
   (ID, EVENTSUBTYPENAME, EVENTSUBTYPEDESCRIPTION, EVENTTYPEID, CREATEDBY, DATECREATED, MSGGROUP_ID, ISACTIVE)
 Values
   ('29', 'Flaps', 'Flaps are cut', '9', 'kkanuganti', 
    TO_DATE('01/23/2013 10:37:00', 'MM/DD/YYYY HH24:MI:SS'), 'Returns Center', 'X');
Insert into TRANSP.EVENTSUBTYPE
   (ID, EVENTSUBTYPENAME, EVENTSUBTYPEDESCRIPTION, EVENTTYPEID, CREATEDBY, DATECREATED, ISACTIVE)
 Values
   ('30', 'Building Access', 'Delayed', '10', 'kkanuganti', 
    TO_DATE('01/23/2013 10:37:00', 'MM/DD/YYYY HH24:MI:SS'), 'X');
Insert into TRANSP.EVENTSUBTYPE
   (ID, EVENTSUBTYPENAME, EVENTSUBTYPEDESCRIPTION, EVENTTYPEID, CREATEDBY, DATECREATED, ISACTIVE)
 Values
   ('31', 'Combining Windows', 'Combining windows', '10', 'kkanuganti', 
    TO_DATE('01/23/2013 10:37:00', 'MM/DD/YYYY HH24:MI:SS'), 'X');
Insert into TRANSP.EVENTSUBTYPE
   (ID, EVENTSUBTYPENAME, EVENTSUBTYPEDESCRIPTION, EVENTTYPEID, CREATEDBY, DATECREATED, MSGGROUP_ID, ISACTIVE)
 Values
   ('32', 'Freezer Door', 'Freezer Door', '9', 'kkanuganti', 
    TO_DATE('01/23/2013 10:37:00', 'MM/DD/YYYY HH24:MI:SS'), 'Returns Center', 'X');
Insert into TRANSP.EVENTSUBTYPE
   (ID, EVENTSUBTYPENAME, EVENTSUBTYPEDESCRIPTION, EVENTTYPEID, CREATEDBY, DATECREATED, ISACTIVE)
 Values
   ('34', 'Sick', 'Employee was sick on route', '11', 'kkanuganti', 
    TO_DATE('01/23/2013 10:37:00', 'MM/DD/YYYY HH24:MI:SS'), 'X');
Insert into TRANSP.EVENTSUBTYPE
   (ID, EVENTSUBTYPENAME, EVENTSUBTYPEDESCRIPTION, EVENTTYPEID, CREATEDBY, DATECREATED, ISACTIVE)
 Values
   ('35', 'Stops Spread Out', 'distance between stops', '10', 'kkanuganti', 
    TO_DATE('01/23/2013 10:37:00', 'MM/DD/YYYY HH24:MI:SS'), 'X');
Insert into TRANSP.EVENTSUBTYPE
   (ID, EVENTSUBTYPENAME, EVENTSUBTYPEDESCRIPTION, EVENTTYPEID, CREATEDBY, DATECREATED, MSGGROUP_ID, ISACTIVE)
 Values
   ('36', 'Reefer', 'Reefer Issues', '9', 'kkanuganti', 
    TO_DATE('01/23/2013 10:37:00', 'MM/DD/YYYY HH24:MI:SS'), 'Returns Center', 'X');
Insert into TRANSP.EVENTSUBTYPE
   (ID, EVENTSUBTYPENAME, EVENTSUBTYPEDESCRIPTION, EVENTTYPEID, CREATEDBY, DATECREATED, ISACTIVE)
 Values
   ('37', 'Close', 'Field Audit Completed', '16', 'kkanuganti', 
    TO_DATE('01/23/2013 10:37:00', 'MM/DD/YYYY HH24:MI:SS'), 'X');
Insert into TRANSP.EVENTSUBTYPE
   (ID, EVENTSUBTYPENAME, EVENTSUBTYPEDESCRIPTION, EVENTTYPEID, CREATEDBY, DATECREATED, ISACTIVE)
 Values
   ('38', 'Open', 'Assigned Field Audit', '16', 'kkanuganti', 
    TO_DATE('01/23/2013 10:37:00', 'MM/DD/YYYY HH24:MI:SS'), 'X');
Insert into TRANSP.EVENTSUBTYPE
   (ID, EVENTSUBTYPENAME, EVENTSUBTYPEDESCRIPTION, EVENTTYPEID, CREATEDBY, DATECREATED, MSGGROUP_ID, ISACTIVE)
 Values
   ('39', 'Transportation', 'reefer', '10', 'kkanuganti', 
    TO_DATE('01/23/2013 10:37:00', 'MM/DD/YYYY HH24:MI:SS'), 'Trucks', 'X');
Insert into TRANSP.EVENTSUBTYPE
   (ID, EVENTSUBTYPENAME, EVENTSUBTYPEDESCRIPTION, EVENTTYPEID, CREATEDBY, DATECREATED, ISACTIVE)
 Values
   ('40', 'misplaced scanner', 'Went looking for scanner', '12', 'kkanuganti', 
    TO_DATE('01/23/2013 10:37:00', 'MM/DD/YYYY HH24:MI:SS'), 'X');
Insert into TRANSP.EVENTSUBTYPE
   (ID, EVENTSUBTYPENAME, EVENTSUBTYPEDESCRIPTION, EVENTTYPEID, CREATEDBY, DATECREATED, MSGGROUP_ID, ISACTIVE)
 Values
   ('41', 'Truck Towed', 'Towed', '9', 'kkanuganti', 
    TO_DATE('01/23/2013 10:37:00', 'MM/DD/YYYY HH24:MI:SS'), 'Returns Center', 'X');
Insert into TRANSP.EVENTSUBTYPE
   (ID, EVENTSUBTYPENAME, EVENTSUBTYPEDESCRIPTION, EVENTTYPEID, CREATEDBY, DATECREATED, MSGGROUP_ID, ISACTIVE)
 Values
   ('42', 'Brake', 'Brake light', '9', 'kkanuganti', 
    TO_DATE('01/23/2013 10:37:00', 'MM/DD/YYYY HH24:MI:SS'), 'Returns Center', 'X');
Insert into TRANSP.EVENTSUBTYPE
   (ID, EVENTSUBTYPENAME, EVENTSUBTYPEDESCRIPTION, EVENTTYPEID, CREATEDBY, DATECREATED, ISACTIVE)
 Values
   ('43', 'Mechanical Failure', 'Out of service', '8', 'kkanuganti', 
    TO_DATE('01/23/2013 10:37:00', 'MM/DD/YYYY HH24:MI:SS'), 'X');
Insert into TRANSP.EVENTSUBTYPE
   (ID, EVENTSUBTYPENAME, EVENTSUBTYPEDESCRIPTION, EVENTTYPEID, CREATEDBY, DATECREATED, ISACTIVE)
 Values
   ('44', 'late', 'late deliveries', '17', 'kkanuganti', 
    TO_DATE('01/23/2013 10:37:00', 'MM/DD/YYYY HH24:MI:SS'), 'X');
Insert into TRANSP.EVENTSUBTYPE
   (ID, EVENTSUBTYPENAME, EVENTSUBTYPEDESCRIPTION, EVENTTYPEID, CREATEDBY, DATECREATED, ISACTIVE)
 Values
   ('46', 'Reattempts', 'Reattemps', '12', 'kkanuganti', 
    TO_DATE('01/23/2013 10:37:00', 'MM/DD/YYYY HH24:MI:SS'), 'X');
Insert into TRANSP.EVENTSUBTYPE
   (ID, EVENTSUBTYPENAME, EVENTSUBTYPEDESCRIPTION, EVENTTYPEID, CREATEDBY, DATECREATED, ISACTIVE)
 Values
   ('47', 'Customer Service', 'Waiting to call back from customer service', '12', 'kkanuganti', 
    TO_DATE('01/23/2013 10:37:00', 'MM/DD/YYYY HH24:MI:SS'), 'X');
Insert into TRANSP.EVENTSUBTYPE
   (ID, EVENTSUBTYPENAME, EVENTSUBTYPEDESCRIPTION, EVENTTYPEID, CREATEDBY, DATECREATED, ISACTIVE)
 Values
   ('48', 'Error - Order Not Found', 'AirClic order not found', '14', 'kkanuganti', 
    TO_DATE('01/23/2013 10:37:00', 'MM/DD/YYYY HH24:MI:SS'), 'X');
Insert into TRANSP.EVENTSUBTYPE
   (ID, EVENTSUBTYPENAME, EVENTSUBTYPEDESCRIPTION, EVENTTYPEID, CREATEDBY, DATECREATED, ISACTIVE)
 Values
   ('49', 'Experience', 'Driver not familiar with route.', '10', 'kkanuganti', 
    TO_DATE('01/23/2013 10:37:00', 'MM/DD/YYYY HH24:MI:SS'), 'X');
Insert into TRANSP.EVENTSUBTYPE
   (ID, EVENTSUBTYPENAME, EVENTSUBTYPEDESCRIPTION, EVENTTYPEID, CREATEDBY, DATECREATED)
 Values
   ('50', 'Other', 'scanning problem other than usual', '14', 'kkanuganti', 
    TO_DATE('01/23/2013 10:37:00', 'MM/DD/YYYY HH24:MI:SS'));
Insert into TRANSP.EVENTSUBTYPE
   (ID, EVENTSUBTYPENAME, EVENTSUBTYPEDESCRIPTION, EVENTTYPEID, CREATEDBY, DATECREATED, ISACTIVE)
 Values
   ('51', 'Lack of Assets', 'Waiting for Hand trucks', '7', 'kkanuganti', 
    TO_DATE('01/23/2013 10:37:00', 'MM/DD/YYYY HH24:MI:SS'), 'X');
Insert into TRANSP.EVENTSUBTYPE
   (ID, EVENTSUBTYPENAME, EVENTSUBTYPEDESCRIPTION, EVENTTYPEID, CREATEDBY, DATECREATED, ISACTIVE)
 Values
   ('52', 'Towed', 'Truck impounded', '12', 'kkanuganti', 
    TO_DATE('01/23/2013 10:37:00', 'MM/DD/YYYY HH24:MI:SS'), 'X');
Insert into TRANSP.EVENTSUBTYPE
   (ID, EVENTSUBTYPENAME, EVENTSUBTYPEDESCRIPTION, EVENTTYPEID, CREATEDBY, DATECREATED, MSGGROUP_ID, ISACTIVE)
 Values
   ('54', 'Work Performance', 'Poor work performance', '11', 'kkanuganti', 
    TO_DATE('01/23/2013 10:37:00', 'MM/DD/YYYY HH24:MI:SS'), 'Transportation', 'X');
Insert into TRANSP.EVENTSUBTYPE
   (ID, EVENTSUBTYPENAME, EVENTSUBTYPEDESCRIPTION, EVENTTYPEID, CREATEDBY, DATECREATED, MSGGROUP_ID, ISACTIVE)
 Values
   ('55', 'Flat Tire', 'Flat Tire', '8', 'kkanuganti', 
    TO_DATE('01/23/2013 10:37:00', 'MM/DD/YYYY HH24:MI:SS'), 'Transportation', 'X');
Insert into TRANSP.EVENTSUBTYPE
   (ID, EVENTSUBTYPENAME, EVENTSUBTYPEDESCRIPTION, EVENTTYPEID, CREATEDBY, DATECREATED, MSGGROUP_ID, ISACTIVE)
 Values
   ('56', 'Stalling', 'Truck turned off / smoking', '9', 'kkanuganti', 
    TO_DATE('01/23/2013 10:37:00', 'MM/DD/YYYY HH24:MI:SS'), 'Returns Center', 'X');
Insert into TRANSP.EVENTSUBTYPE
   (ID, EVENTSUBTYPENAME, EVENTSUBTYPEDESCRIPTION, EVENTTYPEID, CREATEDBY, DATECREATED, MSGGROUP_ID, ISACTIVE)
 Values
   ('57', 'Malfunction Punch Clock', 'System Malfunction', '19', 'kkanuganti', 
    TO_DATE('01/23/2013 10:37:00', 'MM/DD/YYYY HH24:MI:SS'), 'Transportation', 'X');
Insert into TRANSP.EVENTSUBTYPE
   (ID, EVENTSUBTYPENAME, EVENTSUBTYPEDESCRIPTION, EVENTTYPEID, CREATEDBY, DATECREATED, MSGGROUP_ID, ISACTIVE)
 Values
   ('58', 'Lights', 'Lights are out', '9', 'kkanuganti', 
    TO_DATE('01/23/2013 10:37:00', 'MM/DD/YYYY HH24:MI:SS'), 'Returns Center', 'X');
Insert into TRANSP.EVENTSUBTYPE
   (ID, EVENTSUBTYPENAME, EVENTSUBTYPEDESCRIPTION, EVENTTYPEID, CREATEDBY, DATECREATED, MSGGROUP_ID, ISACTIVE)
 Values
   ('59', 'Skin is Torn', 'Skin is Torn', '8', 'kkanuganti', 
    TO_DATE('01/23/2013 10:37:00', 'MM/DD/YYYY HH24:MI:SS'), 'Transportation', 'X');
Insert into TRANSP.EVENTSUBTYPE
   (ID, EVENTSUBTYPENAME, EVENTSUBTYPEDESCRIPTION, EVENTTYPEID, CREATEDBY, DATECREATED, MSGGROUP_ID, ISACTIVE)
 Values
   ('60', 'Door Damaged', 'Door Damaged', '6', 'kkanuganti', 
    TO_DATE('01/23/2013 10:37:00', 'MM/DD/YYYY HH24:MI:SS'), 'Transportation', 'X');
Insert into TRANSP.EVENTSUBTYPE
   (ID, EVENTSUBTYPENAME, EVENTSUBTYPEDESCRIPTION, EVENTTYPEID, CREATEDBY, DATECREATED, MSGGROUP_ID, ISACTIVE)
 Values
   ('61', 'Reefer is Not Working', 'Reefer is Not Working', '8', 'kkanuganti', 
    TO_DATE('01/23/2013 10:37:00', 'MM/DD/YYYY HH24:MI:SS'), 'Transportation', 'X');
Insert into TRANSP.EVENTSUBTYPE
   (ID, EVENTSUBTYPENAME, EVENTSUBTYPEDESCRIPTION, EVENTTYPEID, CREATEDBY, DATECREATED, MSGGROUP_ID, ISACTIVE)
 Values
   ('62', 'No Specific Informat', 'No Specific Information', '8', 'kkanuganti', 
    TO_DATE('01/23/2013 10:37:00', 'MM/DD/YYYY HH24:MI:SS'), 'Transportation', 'X');
Insert into TRANSP.EVENTSUBTYPE
   (ID, EVENTSUBTYPENAME, EVENTSUBTYPEDESCRIPTION, EVENTTYPEID, CREATEDBY, DATECREATED, MSGGROUP_ID, ISACTIVE)
 Values
   ('63', 'Wrong Truck', 'Wrong Truck', '7', 'kkanuganti', 
    TO_DATE('01/23/2013 10:37:00', 'MM/DD/YYYY HH24:MI:SS'), 'Transportation', 'X');
Insert into TRANSP.EVENTSUBTYPE
   (ID, EVENTSUBTYPENAME, EVENTSUBTYPEDESCRIPTION, EVENTTYPEID, CREATEDBY, DATECREATED, MSGGROUP_ID, ISACTIVE)
 Values
   ('64', 'Transmission Issues', 'Transmission Issues', '9', 'kkanuganti', 
    TO_DATE('01/23/2013 10:37:00', 'MM/DD/YYYY HH24:MI:SS'), 'Returns Center', 'X');
Insert into TRANSP.EVENTSUBTYPE
   (ID, EVENTSUBTYPENAME, EVENTSUBTYPEDESCRIPTION, EVENTTYPEID, CREATEDBY, DATECREATED, MSGGROUP_ID, ISACTIVE)
 Values
   ('65', 'Brakes are Bad', 'Brakes are Bad', '8', 'kkanuganti', 
    TO_DATE('01/23/2013 10:37:00', 'MM/DD/YYYY HH24:MI:SS'), 'Transportation', 'X');
Insert into TRANSP.EVENTSUBTYPE
   (ID, EVENTSUBTYPENAME, EVENTSUBTYPEDESCRIPTION, EVENTTYPEID, CREATEDBY, DATECREATED, MSGGROUP_ID, ISACTIVE)
 Values
   ('66', 'Door Damaged', 'door will not close', '9', 'kkanuganti', 
    TO_DATE('01/23/2013 10:37:00', 'MM/DD/YYYY HH24:MI:SS'), 'Returns Center', 'X');
Insert into TRANSP.EVENTSUBTYPE
   (ID, EVENTSUBTYPENAME, EVENTSUBTYPEDESCRIPTION, EVENTTYPEID, CREATEDBY, DATECREATED, MSGGROUP_ID, ISACTIVE)
 Values
   ('67', 'Damaged Phone', 'damaged phone', '11', 'kkanuganti', 
    TO_DATE('01/23/2013 10:37:00', 'MM/DD/YYYY HH24:MI:SS'), 'Transportation', 'X');
Insert into TRANSP.EVENTSUBTYPE
   (ID, EVENTSUBTYPENAME, EVENTSUBTYPEDESCRIPTION, EVENTTYPEID, CREATEDBY, DATECREATED, MSGGROUP_ID, ISACTIVE)
 Values
   ('68', 'attendance', 'Employee arrived late', '7', 'kkanuganti', 
    TO_DATE('01/23/2013 10:37:00', 'MM/DD/YYYY HH24:MI:SS'), 'Transportation', 'X');
Insert into TRANSP.EVENTSUBTYPE
   (ID, EVENTSUBTYPENAME, EVENTSUBTYPEDESCRIPTION, EVENTTYPEID, CREATEDBY, DATECREATED, MSGGROUP_ID, ISACTIVE)
 Values
   ('69', 'Power Outage', 'Power Outage', '21', 'kkanuganti', 
    TO_DATE('01/23/2013 10:37:00', 'MM/DD/YYYY HH24:MI:SS'), 'Transportation', 'X');
Insert into TRANSP.EVENTSUBTYPE
   (ID, EVENTSUBTYPENAME, EVENTSUBTYPEDESCRIPTION, EVENTTYPEID, CREATEDBY, DATECREATED, MSGGROUP_ID, ISACTIVE)
 Values
   ('70', 'Cracked Windshield', 'Windshield Cracked', '6', 'kkanuganti', 
    TO_DATE('01/23/2013 10:37:00', 'MM/DD/YYYY HH24:MI:SS'), 'Transportation', 'X');
Insert into TRANSP.EVENTSUBTYPE
   (ID, EVENTSUBTYPENAME, EVENTSUBTYPEDESCRIPTION, EVENTTYPEID, CREATEDBY, DATECREATED, MSGGROUP_ID, ISACTIVE)
 Values
   ('71', 'Check Battery', 'Check Battery', '8', 'kkanuganti', 
    TO_DATE('01/23/2013 10:37:00', 'MM/DD/YYYY HH24:MI:SS'), 'Transportation', 'X');
Insert into TRANSP.EVENTSUBTYPE
   (ID, EVENTSUBTYPENAME, EVENTSUBTYPEDESCRIPTION, EVENTTYPEID, CREATEDBY, DATECREATED, ISACTIVE)
 Values
   ('73', 'No Label', 'No Label on Box/Bag, Unable to Scan', '14', 'kkanuganti', 
    TO_DATE('01/23/2013 10:37:00', 'MM/DD/YYYY HH24:MI:SS'), 'X');
Insert into TRANSP.EVENTSUBTYPE
   (ID, EVENTSUBTYPENAME, EVENTSUBTYPEDESCRIPTION, EVENTTYPEID, CREATEDBY, DATECREATED, ISACTIVE)
 Values
   ('74', 'Unable To Process', 'Unable to Process scanning error', '14', 'kkanuganti', 
    TO_DATE('01/23/2013 10:37:00', 'MM/DD/YYYY HH24:MI:SS'), 'X');
Insert into TRANSP.EVENTSUBTYPE
   (ID, EVENTSUBTYPENAME, EVENTSUBTYPEDESCRIPTION, EVENTTYPEID, CREATEDBY, DATECREATED, MSGGROUP_ID, ISACTIVE)
 Values
   ('75', 'Injury', 'Injured delivery personnel', '6', 'kkanuganti', 
    TO_DATE('01/23/2013 10:37:00', 'MM/DD/YYYY HH24:MI:SS'), 'Transportation', 'X');
Insert into TRANSP.EVENTSUBTYPE
   (ID, EVENTSUBTYPENAME, EVENTSUBTYPEDESCRIPTION, EVENTTYPEID, CREATEDBY, DATECREATED, ISACTIVE)
 Values
   ('77', 'Customer Call Feature Failed', 'Customer Call IVR System failed', '14', 'kkanuganti', 
    TO_DATE('01/23/2013 10:37:00', 'MM/DD/YYYY HH24:MI:SS'), 'X');
Insert into TRANSP.EVENTSUBTYPE
   (ID, EVENTSUBTYPENAME, EVENTSUBTYPEDESCRIPTION, EVENTTYPEID, CREATEDBY, DATECREATED, ISACTIVE)
 Values
   ('78', 'Forgot to Scan', 'Forgot to scan', '14', 'kkanuganti', 
    TO_DATE('01/23/2013 10:37:00', 'MM/DD/YYYY HH24:MI:SS'), 'X');
Insert into TRANSP.EVENTSUBTYPE
   (ID, EVENTSUBTYPENAME, EVENTSUBTYPEDESCRIPTION, EVENTTYPEID, CREATEDBY, DATECREATED, MSGGROUP_ID)
 Values
   ('79', 'Refuse Assignment', 'Failure to follow instructions', '23', 'kkanuganti', 
    TO_DATE('01/23/2013 10:37:00', 'MM/DD/YYYY HH24:MI:SS'), 'Transportation');
Insert into TRANSP.EVENTSUBTYPE
   (ID, EVENTSUBTYPENAME, EVENTSUBTYPEDESCRIPTION, EVENTTYPEID, CREATEDBY, DATECREATED)
 Values
   ('81', 'Missing In Action', 'Driver not responding', '23', 'kkanuganti', 
    TO_DATE('01/23/2013 10:37:00', 'MM/DD/YYYY HH24:MI:SS'));
Insert into TRANSP.EVENTSUBTYPE
   (ID, EVENTSUBTYPENAME, EVENTSUBTYPEDESCRIPTION, EVENTTYPEID, CREATEDBY, DATECREATED, ISACTIVE)
 Values
   ('82', 'Phone Crashing', 'Phone shut down', '14', 'kkanuganti', 
    TO_DATE('01/23/2013 10:37:00', 'MM/DD/YYYY HH24:MI:SS'), 'X');
Insert into TRANSP.EVENTSUBTYPE
   (ID, EVENTSUBTYPENAME, EVENTSUBTYPEDESCRIPTION, EVENTTYPEID, CREATEDBY, DATECREATED, ISACTIVE)
 Values
   ('83', 'Wrong S - NH in Error', 'Scanned NH in error', '14', 'kkanuganti', 
    TO_DATE('01/23/2013 10:37:00', 'MM/DD/YYYY HH24:MI:SS'), 'X');
Insert into TRANSP.EVENTSUBTYPE
   (ID, EVENTSUBTYPENAME, EVENTSUBTYPEDESCRIPTION, EVENTTYPEID, CREATEDBY, DATECREATED, ISACTIVE)
 Values
   ('84', 'Wrong S - NH as Delivered', 'NH scanned as delivered', '14', 'kkanuganti', 
    TO_DATE('01/23/2013 10:37:00', 'MM/DD/YYYY HH24:MI:SS'), 'X');
Insert into TRANSP.EVENTSUBTYPE
   (ID, EVENTSUBTYPENAME, EVENTSUBTYPEDESCRIPTION, EVENTTYPEID, CREATEDBY, DATECREATED, ISACTIVE)
 Values
   ('85', 'MOT Dispatched', 'Delivery team sent to assist route', '24', 'kkanuganti', 
    TO_DATE('01/23/2013 10:37:00', 'MM/DD/YYYY HH24:MI:SS'), 'X');
Insert into TRANSP.EVENTSUBTYPE
   (ID, EVENTSUBTYPENAME, EVENTSUBTYPEDESCRIPTION, EVENTTYPEID, CREATEDBY, DATECREATED, ISACTIVE)
 Values
   ('86', 'Called Customer', 'Outbound late notification', '24', 'kkanuganti', 
    TO_DATE('01/23/2013 10:37:00', 'MM/DD/YYYY HH24:MI:SS'), 'X');
Insert into TRANSP.EVENTSUBTYPE
   (ID, EVENTSUBTYPENAME, EVENTSUBTYPEDESCRIPTION, EVENTTYPEID, CREATEDBY, DATECREATED, ISACTIVE)
 Values
   ('87', 'Status Unconfirmed', 'Delivery team unreachable', '24', 'kkanuganti', 
    TO_DATE('01/23/2013 10:37:00', 'MM/DD/YYYY HH24:MI:SS'), 'X');
Insert into TRANSP.EVENTSUBTYPE
   (ID, EVENTSUBTYPENAME, EVENTSUBTYPEDESCRIPTION, EVENTTYPEID, CREATEDBY, DATECREATED, ISACTIVE)
 Values
   ('88', 'MOT/Called Customer', 'Dispatched MOT and called customer about the', '24', 'kkanuganti', 
    TO_DATE('01/23/2013 10:37:00', 'MM/DD/YYYY HH24:MI:SS'), 'X');
Insert into TRANSP.EVENTSUBTYPE
   (ID, EVENTSUBTYPENAME, EVENTSUBTYPEDESCRIPTION, EVENTTYPEID, CREATEDBY, DATECREATED)
 Values
   ('89', 'False/Misc Scan Issue', 'False read on report because of a device issu', '24', 'kkanuganti', 
    TO_DATE('01/23/2013 10:37:00', 'MM/DD/YYYY HH24:MI:SS'));
Insert into TRANSP.EVENTSUBTYPE
   (ID, EVENTSUBTYPENAME, EVENTSUBTYPEDESCRIPTION, EVENTTYPEID, CREATEDBY, DATECREATED)
 Values
   ('90', 'False/Forgot to Scan', 'False entry on report because a delivery team', '24', 'kkanuganti', 
    TO_DATE('01/23/2013 10:37:00', 'MM/DD/YYYY HH24:MI:SS'));
Insert into TRANSP.EVENTSUBTYPE
   (ID, EVENTSUBTYPENAME, EVENTSUBTYPEDESCRIPTION, EVENTTYPEID, CREATEDBY, DATECREATED)
 Values
   ('91', 'False/Report Rules', 'False entry on report because of a report bus', '24', 'kkanuganti', 
    TO_DATE('01/23/2013 10:37:00', 'MM/DD/YYYY HH24:MI:SS'));
Insert into TRANSP.EVENTSUBTYPE
   (ID, EVENTSUBTYPENAME, EVENTSUBTYPEDESCRIPTION, EVENTTYPEID, CREATEDBY, DATECREATED, ISACTIVE)
 Values
   ('92', 'No Action Req', 'Good excpetion however the orders are unlikel', '24', 'kkanuganti', 
    TO_DATE('01/23/2013 10:37:00', 'MM/DD/YYYY HH24:MI:SS'), 'X');
Insert into TRANSP.EVENTSUBTYPE
   (ID, EVENTSUBTYPENAME, EVENTSUBTYPEDESCRIPTION, EVENTTYPEID, CREATEDBY, DATECREATED, ISACTIVE)
 Values
   ('93', 'Misplaced Scanner', 'Misplaced Scanner', '11', 'kkanuganti', 
    TO_DATE('01/23/2013 10:37:00', 'MM/DD/YYYY HH24:MI:SS'), 'X');
Insert into TRANSP.EVENTSUBTYPE
   (ID, EVENTSUBTYPENAME, EVENTSUBTYPEDESCRIPTION, EVENTTYPEID, CREATEDBY, DATECREATED, ISACTIVE)
 Values
   ('94', 'Uniform', 'Uniform', '11', 'kkanuganti', 
    TO_DATE('01/23/2013 10:37:00', 'MM/DD/YYYY HH24:MI:SS'), 'X');
Insert into TRANSP.EVENTSUBTYPE
   (ID, EVENTSUBTYPENAME, EVENTSUBTYPEDESCRIPTION, EVENTTYPEID, CREATEDBY, DATECREATED, ISACTIVE)
 Values
   ('95', 'No Call No Show', 'No Call No Show', '11', 'kkanuganti', 
    TO_DATE('01/23/2013 10:37:00', 'MM/DD/YYYY HH24:MI:SS'), 'X');
Insert into TRANSP.EVENTSUBTYPE
   (ID, EVENTSUBTYPENAME, EVENTSUBTYPEDESCRIPTION, EVENTTYPEID, CREATEDBY, DATECREATED, ISACTIVE)
 Values
   ('96', 'Experience', 'Experience', '11', 'kkanuganti', 
    TO_DATE('01/23/2013 10:37:00', 'MM/DD/YYYY HH24:MI:SS'), 'X');
Insert into TRANSP.EVENTSUBTYPE
   (ID, EVENTSUBTYPENAME, EVENTSUBTYPEDESCRIPTION, EVENTTYPEID, CREATEDBY, DATECREATED, MSGGROUP_ID, ISACTIVE)
 Values
   ('97', 'Battery Inspection', 'Battery Inspection', '9', 'kkanuganti', 
    TO_DATE('01/23/2013 10:37:00', 'MM/DD/YYYY HH24:MI:SS'), 'Returns Center', 'X');
Insert into TRANSP.EVENTSUBTYPE
   (ID, EVENTSUBTYPENAME, EVENTSUBTYPEDESCRIPTION, EVENTTYPEID, CREATEDBY, DATECREATED, MSGGROUP_ID, ISACTIVE)
 Values
   ('98', 'Bio-Diesel', 'Bio-Diesel', '9', 'kkanuganti', 
    TO_DATE('01/23/2013 10:37:00', 'MM/DD/YYYY HH24:MI:SS'), 'Returns Center', 'X');
Insert into TRANSP.EVENTSUBTYPE
   (ID, EVENTSUBTYPENAME, EVENTSUBTYPEDESCRIPTION, EVENTTYPEID, CREATEDBY, DATECREATED, MSGGROUP_ID, ISACTIVE)
 Values
   ('99', 'Engine', 'Engine', '9', 'kkanuganti', 
    TO_DATE('01/23/2013 10:37:00', 'MM/DD/YYYY HH24:MI:SS'), 'Returns Center', 'X');
Insert into TRANSP.EVENTSUBTYPE
   (ID, EVENTSUBTYPENAME, EVENTSUBTYPEDESCRIPTION, EVENTTYPEID, CREATEDBY, DATECREATED, MSGGROUP_ID, ISACTIVE)
 Values
   ('100', 'Regular Maintenance', 'Regular Maintenance', '9', 'kkanuganti', 
    TO_DATE('01/23/2013 10:37:00', 'MM/DD/YYYY HH24:MI:SS'), 'Returns Center', 'X');
Insert into TRANSP.EVENTSUBTYPE
   (ID, EVENTSUBTYPENAME, EVENTSUBTYPEDESCRIPTION, EVENTTYPEID, CREATEDBY, DATECREATED, ISACTIVE)
 Values
   ('102', 'Reattempts', 'Reattempts', '10', 'kkanuganti', 
    TO_DATE('01/23/2013 10:37:00', 'MM/DD/YYYY HH24:MI:SS'), 'X');
Insert into TRANSP.EVENTSUBTYPE
   (ID, EVENTSUBTYPENAME, EVENTSUBTYPEDESCRIPTION, EVENTTYPEID, CREATEDBY, DATECREATED, ISACTIVE)
 Values
   ('103', 'Traffic Congestion', 'Traffic Congestion', '10', 'kkanuganti', 
    TO_DATE('01/23/2013 10:37:00', 'MM/DD/YYYY HH24:MI:SS'), 'X');
Insert into TRANSP.EVENTSUBTYPE
   (ID, EVENTSUBTYPENAME, EVENTSUBTYPEDESCRIPTION, EVENTTYPEID, CREATEDBY, DATECREATED, ISACTIVE)
 Values
   ('104', 'Traffic Accident', 'Traffic Accident', '10', 'kkanuganti', 
    TO_DATE('01/23/2013 10:37:00', 'MM/DD/YYYY HH24:MI:SS'), 'X');
Insert into TRANSP.EVENTSUBTYPE
   (ID, EVENTSUBTYPENAME, EVENTSUBTYPEDESCRIPTION, EVENTTYPEID, CREATEDBY, DATECREATED, ISACTIVE)
 Values
   ('105', 'Traffic Street Event', 'Traffic Street Event', '10', 'kkanuganti', 
    TO_DATE('01/23/2013 10:37:00', 'MM/DD/YYYY HH24:MI:SS'), 'X');
Insert into TRANSP.EVENTSUBTYPE
   (ID, EVENTSUBTYPENAME, EVENTSUBTYPEDESCRIPTION, EVENTTYPEID, CREATEDBY, DATECREATED, ISACTIVE)
 Values
   ('106', 'Traffic Violation - Towed', 'Traffic Violation - Towed', '10', 'kkanuganti', 
    TO_DATE('01/23/2013 10:37:00', 'MM/DD/YYYY HH24:MI:SS'), 'X');
Insert into TRANSP.EVENTSUBTYPE
   (ID, EVENTSUBTYPENAME, EVENTSUBTYPEDESCRIPTION, EVENTTYPEID, CREATEDBY, DATECREATED, ISACTIVE)
 Values
   ('107', 'Traffic Violation - Moving', 'Traffic Violation - Moving', '10', 'kkanuganti', 
    TO_DATE('01/23/2013 10:37:00', 'MM/DD/YYYY HH24:MI:SS'), 'X');
Insert into TRANSP.EVENTSUBTYPE
   (ID, EVENTSUBTYPENAME, EVENTSUBTYPEDESCRIPTION, EVENTTYPEID, CREATEDBY, DATECREATED)
 Values
   ('108', 'Driver Request', 'Driver Request', '23', 'kkanuganti', 
    TO_DATE('01/23/2013 10:37:00', 'MM/DD/YYYY HH24:MI:SS'));
Insert into TRANSP.EVENTSUBTYPE
   (ID, EVENTSUBTYPENAME, EVENTSUBTYPEDESCRIPTION, EVENTTYPEID, CREATEDBY, DATECREATED, ISACTIVE)
 Values
   ('109', 'Misload', 'Misload', '10', 'kkanuganti', 
    TO_DATE('01/23/2013 10:37:00', 'MM/DD/YYYY HH24:MI:SS'), 'X');
Insert into TRANSP.EVENTSUBTYPE
   (ID, EVENTSUBTYPENAME, EVENTSUBTYPEDESCRIPTION, EVENTTYPEID, CREATEDBY, DATECREATED, ISACTIVE)
 Values
   ('110', 'Performance', 'Performance', '14', 'kkanuganti', 
    TO_DATE('01/23/2013 10:37:00', 'MM/DD/YYYY HH24:MI:SS'), 'X');
Insert into TRANSP.EVENTSUBTYPE
   (ID, EVENTSUBTYPENAME, EVENTSUBTYPEDESCRIPTION, EVENTTYPEID, CREATEDBY, DATECREATED, ISACTIVE)
 Values
   ('111', 'Incorrect Scan', 'Incorrect Scan', '14', 'kkanuganti', 
    TO_DATE('01/23/2013 10:37:00', 'MM/DD/YYYY HH24:MI:SS'), 'X');
Insert into TRANSP.EVENTSUBTYPE
   (ID, EVENTSUBTYPENAME, EVENTSUBTYPEDESCRIPTION, EVENTTYPEID, CREATEDBY, DATECREATED, ISACTIVE)
 Values
   ('113', 'Added Route', 'Route Added', '26', 'kkanuganti', 
    TO_DATE('01/23/2013 10:37:00', 'MM/DD/YYYY HH24:MI:SS'), 'X');
Insert into TRANSP.EVENTSUBTYPE
   (ID, EVENTSUBTYPENAME, EVENTSUBTYPEDESCRIPTION, EVENTTYPEID, CREATEDBY, DATECREATED, ISACTIVE)
 Values
   ('114', 'Removed Route', 'Removed Route from Plan', '26', 'kkanuganti', 
    TO_DATE('01/23/2013 10:37:00', 'MM/DD/YYYY HH24:MI:SS'), 'X');
Insert into TRANSP.EVENTSUBTYPE
   (ID, EVENTSUBTYPENAME, EVENTSUBTYPEDESCRIPTION, EVENTTYPEID, CREATEDBY, DATECREATED, ISACTIVE)
 Values
   ('115', 'Phone Out of Service', 'Phone Issue', '8', 'kkanuganti', 
    TO_DATE('01/23/2013 10:37:00', 'MM/DD/YYYY HH24:MI:SS'), 'X');
Insert into TRANSP.EVENTSUBTYPE
   (ID, EVENTSUBTYPENAME, EVENTSUBTYPEDESCRIPTION, EVENTTYPEID, CREATEDBY, DATECREATED, MSGGROUP_ID, ISACTIVE)
 Values
   ('116', 'Route Count', 'Route Count by Driver', '13', 'kkanuganti', 
    TO_DATE('01/23/2013 10:37:00', 'MM/DD/YYYY HH24:MI:SS'), 'Transportation', 'X');
Insert into TRANSP.EVENTSUBTYPE
   (ID, EVENTSUBTYPENAME, EVENTSUBTYPEDESCRIPTION, EVENTTYPEID, CREATEDBY, DATECREATED)
 Values
   ('117', 'False/Battery Low', 'False/Battery Low', '24', 'kkanuganti', 
    TO_DATE('01/23/2013 10:37:00', 'MM/DD/YYYY HH24:MI:SS'));
Insert into TRANSP.EVENTSUBTYPE
   (ID, EVENTSUBTYPENAME, EVENTSUBTYPEDESCRIPTION, EVENTTYPEID, CREATEDBY, DATECREATED)
 Values
   ('118', 'False/Comm Error', 'False/Comm Error', '24', 'kkanuganti', 
    TO_DATE('01/23/2013 10:37:00', 'MM/DD/YYYY HH24:MI:SS'));
Insert into TRANSP.EVENTSUBTYPE
   (ID, EVENTSUBTYPENAME, EVENTSUBTYPEDESCRIPTION, EVENTTYPEID, CREATEDBY, DATECREATED)
 Values
   ('119', 'False/Order Not Found', 'False/Order Not Found', '24', 'kkanuganti', 
    TO_DATE('01/23/2013 10:37:00', 'MM/DD/YYYY HH24:MI:SS'));
Insert into TRANSP.EVENTSUBTYPE
   (ID, EVENTSUBTYPENAME, EVENTSUBTYPEDESCRIPTION, EVENTTYPEID, CREATEDBY, DATECREATED)
 Values
   ('120', 'False/Freeze', 'False/Freeze', '24', 'kkanuganti', 
    TO_DATE('01/23/2013 10:37:00', 'MM/DD/YYYY HH24:MI:SS'));
Insert into TRANSP.EVENTSUBTYPE
   (ID, EVENTSUBTYPENAME, EVENTSUBTYPEDESCRIPTION, EVENTTYPEID, CREATEDBY, DATECREATED)
 Values
   ('121', 'False/Laser Problems', 'False/Laser Problems', '24', 'kkanuganti', 
    TO_DATE('01/23/2013 10:37:00', 'MM/DD/YYYY HH24:MI:SS'));
Insert into TRANSP.EVENTSUBTYPE
   (ID, EVENTSUBTYPENAME, EVENTSUBTYPEDESCRIPTION, EVENTTYPEID, CREATEDBY, DATECREATED, ISACTIVE)
 Values
   ('122', 'Roadnet Map Changes', 'Inform or Request Roadnet Map Changes', '26', 'kkanuganti', 
    TO_DATE('01/23/2013 10:37:00', 'MM/DD/YYYY HH24:MI:SS'), 'X');
Insert into TRANSP.EVENTSUBTYPE
   (ID, EVENTSUBTYPENAME, EVENTSUBTYPEDESCRIPTION, EVENTTYPEID, CREATEDBY, DATECREATED)
 Values
   ('123', 'Redelivery - Dairy', 'Sameday redelivery of product', '24', 'kkanuganti', 
    TO_DATE('01/23/2013 10:37:00', 'MM/DD/YYYY HH24:MI:SS'));
Insert into TRANSP.EVENTSUBTYPE
   (ID, EVENTSUBTYPENAME, EVENTSUBTYPEDESCRIPTION, EVENTTYPEID, CREATEDBY, DATECREATED, ISACTIVE)
 Values
   ('124', 'New Hire', 'Employee', '28', 'kkanuganti', 
    TO_DATE('01/23/2013 10:37:00', 'MM/DD/YYYY HH24:MI:SS'), 'X');
Insert into TRANSP.EVENTSUBTYPE
   (ID, EVENTSUBTYPENAME, EVENTSUBTYPEDESCRIPTION, EVENTTYPEID, CREATEDBY, DATECREATED, MSGGROUP_ID, ISACTIVE)
 Values
   ('125', 'Completed', 'Ride Along Completed', '27', 'kkanuganti', 
    TO_DATE('01/23/2013 10:37:00', 'MM/DD/YYYY HH24:MI:SS'), 'Transportation', 'X');
Insert into TRANSP.EVENTSUBTYPE
   (ID, EVENTSUBTYPENAME, EVENTSUBTYPEDESCRIPTION, EVENTTYPEID, CREATEDBY, DATECREATED, ISACTIVE)
 Values
   ('126', 'Stolen Handtruck', 'Handtruck stolen', '11', 'kkanuganti', 
    TO_DATE('01/23/2013 10:37:00', 'MM/DD/YYYY HH24:MI:SS'), 'X');
Insert into TRANSP.EVENTSUBTYPE
   (ID, EVENTSUBTYPENAME, EVENTSUBTYPEDESCRIPTION, EVENTTYPEID, CREATEDBY, DATECREATED, MSGGROUP_ID, ISACTIVE)
 Values
   ('127', 'Lock', 'Lock', '9', 'kkanuganti', 
    TO_DATE('01/23/2013 10:37:00', 'MM/DD/YYYY HH24:MI:SS'), 'Returns Center', 'X');
Insert into TRANSP.EVENTSUBTYPE
   (ID, EVENTSUBTYPENAME, EVENTSUBTYPEDESCRIPTION, EVENTTYPEID, CREATEDBY, DATECREATED, ISACTIVE)
 Values
   ('128', 'Heavy Window', 'Heavy Window', '10', 'kkanuganti', 
    TO_DATE('01/23/2013 10:37:00', 'MM/DD/YYYY HH24:MI:SS'), 'X');
Insert into TRANSP.EVENTSUBTYPE
   (ID, EVENTSUBTYPENAME, EVENTSUBTYPEDESCRIPTION, EVENTTYPEID, CREATEDBY, DATECREATED, MSGGROUP_ID, ISACTIVE)
 Values
   ('129', 'Other', 'Other', '9', 'kkanuganti', 
    TO_DATE('01/23/2013 10:37:00', 'MM/DD/YYYY HH24:MI:SS'), 'Returns Center', 'X');
Insert into TRANSP.EVENTSUBTYPE
   (ID, EVENTSUBTYPENAME, EVENTSUBTYPEDESCRIPTION, EVENTTYPEID, CREATEDBY, DATECREATED, MSGGROUP_ID, ISACTIVE)
 Values
   ('130', 'Call-Out Personal Day', 'Day Of Call-Out but not sick', '11', 'kkanuganti', 
    TO_DATE('01/23/2013 10:37:00', 'MM/DD/YYYY HH24:MI:SS'), 'Transportation', 'X');
Insert into TRANSP.EVENTSUBTYPE
   (ID, EVENTSUBTYPENAME, EVENTSUBTYPEDESCRIPTION, EVENTTYPEID, CREATEDBY, DATECREATED, MSGGROUP_ID, ISACTIVE)
 Values
   ('131', 'Lost/damaged key', 'Key was lost or damaged', '9', 'kkanuganti', 
    TO_DATE('01/23/2013 10:37:00', 'MM/DD/YYYY HH24:MI:SS'), 'Returns Center', 'X');
Insert into TRANSP.EVENTSUBTYPE
   (ID, EVENTSUBTYPENAME, EVENTSUBTYPEDESCRIPTION, EVENTTYPEID, CREATEDBY, DATECREATED, MSGGROUP_ID, ISACTIVE)
 Values
   ('132', 'shipping yard', 'yard set up', '30', 'kkanuganti', 
    TO_DATE('01/23/2013 10:37:00', 'MM/DD/YYYY HH24:MI:SS'), 'Trucks', 'X');
Insert into TRANSP.EVENTSUBTYPE
   (ID, EVENTSUBTYPENAME, EVENTSUBTYPEDESCRIPTION, EVENTTYPEID, CREATEDBY, DATECREATED, ISACTIVE)
 Values
   ('133', 'Screen error', 'Error on screen', '31', 'kkanuganti', 
    TO_DATE('01/23/2013 10:37:00', 'MM/DD/YYYY HH24:MI:SS'), 'X');
Insert into TRANSP.EVENTSUBTYPE
   (ID, EVENTSUBTYPENAME, EVENTSUBTYPEDESCRIPTION, EVENTTYPEID, CREATEDBY, DATECREATED, ISACTIVE)
 Values
   ('134', 'On break', 'On break', '31', 'kkanuganti', 
    TO_DATE('01/23/2013 10:37:00', 'MM/DD/YYYY HH24:MI:SS'), 'X');
Insert into TRANSP.EVENTSUBTYPE
   (ID, EVENTSUBTYPENAME, EVENTSUBTYPEDESCRIPTION, EVENTTYPEID, CREATEDBY, DATECREATED, ISACTIVE)
 Values
   ('135', 'On a call', 'On a call', '31', 'kkanuganti', 
    TO_DATE('01/23/2013 10:37:00', 'MM/DD/YYYY HH24:MI:SS'), 'X');
Insert into TRANSP.EVENTSUBTYPE
   (ID, EVENTSUBTYPENAME, EVENTSUBTYPEDESCRIPTION, EVENTTYPEID, CREATEDBY, DATECREATED, ISACTIVE)
 Values
   ('136', 'Did not see it', 'Did not see it', '31', 'kkanuganti', 
    TO_DATE('01/23/2013 10:37:00', 'MM/DD/YYYY HH24:MI:SS'), 'X');
Insert into TRANSP.EVENTSUBTYPE
   (ID, EVENTSUBTYPENAME, EVENTSUBTYPEDESCRIPTION, EVENTTYPEID, CREATEDBY, DATECREATED, ISACTIVE)
 Values
   ('137', 'Other', 'Other', '31', 'kkanuganti', 
    TO_DATE('01/23/2013 10:37:00', 'MM/DD/YYYY HH24:MI:SS'), 'X');
Insert into TRANSP.EVENTSUBTYPE
   (ID, EVENTSUBTYPENAME, EVENTSUBTYPEDESCRIPTION, EVENTTYPEID, CREATEDBY, DATECREATED, MSGGROUP_ID, ISACTIVE)
 Values
   ('138', 'Injury', 'Injury', '11', 'kkanuganti', 
    TO_DATE('01/23/2013 10:37:00', 'MM/DD/YYYY HH24:MI:SS'), 'Transportation', 'X');
Insert into TRANSP.EVENTSUBTYPE
   (ID, EVENTSUBTYPENAME, EVENTSUBTYPEDESCRIPTION, EVENTTYPEID, CREATEDBY, DATECREATED, MSGGROUP_ID, ISACTIVE)
 Values
   ('139', 'Windshield wipers', 'Wipers not working', '9', 'kkanuganti', 
    TO_DATE('01/23/2013 10:37:00', 'MM/DD/YYYY HH24:MI:SS'), 'Returns Center', 'X');
Insert into TRANSP.EVENTSUBTYPE
   (ID, EVENTSUBTYPENAME, EVENTSUBTYPEDESCRIPTION, EVENTTYPEID, CREATEDBY, DATECREATED)
 Values
   ('140', 'False/No_Data_Transmit', 'False/No_Data_Transmit', '24', 'kkanuganti', 
    TO_DATE('01/23/2013 10:37:00', 'MM/DD/YYYY HH24:MI:SS'));
Insert into TRANSP.EVENTSUBTYPE
   (ID, EVENTSUBTYPENAME, EVENTSUBTYPEDESCRIPTION, EVENTTYPEID, CREATEDBY, DATECREATED)
 Values
   ('141', 'False/Fusion_Error', 'False/Fusion_Error', '24', 'kkanuganti', 
    TO_DATE('01/23/2013 10:37:00', 'MM/DD/YYYY HH24:MI:SS'));
Insert into TRANSP.EVENTSUBTYPE
   (ID, EVENTSUBTYPENAME, EVENTSUBTYPEDESCRIPTION, EVENTTYPEID, CREATEDBY, DATECREATED, MSGGROUP_ID, ISACTIVE)
 Values
   ('142', 'Manual Deactivation', 'Reefer Manually Turned Off', '32', 'kkanuganti', 
    TO_DATE('01/23/2013 10:37:00', 'MM/DD/YYYY HH24:MI:SS'), 'Field Managers', 'X');
Insert into TRANSP.EVENTSUBTYPE
   (ID, EVENTSUBTYPENAME, EVENTSUBTYPEDESCRIPTION, EVENTTYPEID, CREATEDBY, DATECREATED, MSGGROUP_ID, ISACTIVE)
 Values
   ('143', 'Pedestrian Request', 'Pedestrian Request', '33', 'kkanuganti', 
    TO_DATE('01/23/2013 10:37:00', 'MM/DD/YYYY HH24:MI:SS'), 'Field Managers', 'X');
Insert into TRANSP.EVENTSUBTYPE
   (ID, EVENTSUBTYPENAME, EVENTSUBTYPEDESCRIPTION, EVENTTYPEID, CREATEDBY, DATECREATED, MSGGROUP_ID, ISACTIVE)
 Values
   ('144', 'End of Route - No Returns', 'End of Route - No Returns', '33', 'kkanuganti', 
    TO_DATE('01/23/2013 10:37:00', 'MM/DD/YYYY HH24:MI:SS'), 'Field Managers', 'X');
Insert into TRANSP.EVENTSUBTYPE
   (ID, EVENTSUBTYPENAME, EVENTSUBTYPEDESCRIPTION, EVENTTYPEID, CREATEDBY, DATECREATED, MSGGROUP_ID, ISACTIVE)
 Values
   ('145', 'Route Not Found', 'Route Not Found', '33', 'kkanuganti', 
    TO_DATE('01/23/2013 10:37:00', 'MM/DD/YYYY HH24:MI:SS'), 'George Walker', 'X');
Insert into TRANSP.EVENTSUBTYPE
   (ID, EVENTSUBTYPENAME, EVENTSUBTYPEDESCRIPTION, EVENTTYPEID, CREATEDBY, DATECREATED)
 Values
   ('146', 'Other', 'Other', '33', 'kkanuganti', 
    TO_DATE('01/23/2013 10:37:00', 'MM/DD/YYYY HH24:MI:SS'));
Insert into TRANSP.EVENTSUBTYPE
   (ID, EVENTSUBTYPENAME, EVENTSUBTYPEDESCRIPTION, EVENTTYPEID, CREATEDBY, DATECREATED)
 Values
   ('148', 'Equipment Failure/Reefer Code', 'Equipment Failure/Reefer Code', '33', 'kkanuganti', 
    TO_DATE('01/23/2013 10:37:00', 'MM/DD/YYYY HH24:MI:SS'));
Insert into TRANSP.EVENTSUBTYPE
   (ID, EVENTSUBTYPENAME, EVENTSUBTYPEDESCRIPTION, EVENTTYPEID, CREATEDBY, DATECREATED, ISACTIVE)
 Values
   ('149', 'Dispatch', 'Dispatch', '34', 'kkanuganti', 
    TO_DATE('01/23/2013 10:37:00', 'MM/DD/YYYY HH24:MI:SS'), 'X');
Insert into TRANSP.EVENTSUBTYPE
   (ID, EVENTSUBTYPENAME, EVENTSUBTYPEDESCRIPTION, EVENTTYPEID, CREATEDBY, DATECREATED, ISACTIVE)
 Values
   ('150', 'Check In', 'Check In', '34', 'kkanuganti', 
    TO_DATE('01/23/2013 10:37:00', 'MM/DD/YYYY HH24:MI:SS'), 'X');
Insert into TRANSP.EVENTSUBTYPE
   (ID, EVENTSUBTYPENAME, EVENTSUBTYPEDESCRIPTION, EVENTTYPEID, CREATEDBY, DATECREATED, MSGGROUP_ID, ISACTIVE)
 Values
   ('151', 'Driver Not Responding', 'Driver Not Responding', '33', 'kkanuganti', 
    TO_DATE('01/23/2013 10:37:00', 'MM/DD/YYYY HH24:MI:SS'), 'Field Managers', 'X');
Insert into TRANSP.EVENTSUBTYPE
   (ID, EVENTSUBTYPENAME, EVENTSUBTYPEDESCRIPTION, EVENTTYPEID, CREATEDBY, DATECREATED)
 Values
   ('152', 'New Card', 'New Card', '34', 'kkanuganti', 
    TO_DATE('01/23/2013 10:37:00', 'MM/DD/YYYY HH24:MI:SS'));
Insert into TRANSP.EVENTSUBTYPE
   (ID, EVENTSUBTYPENAME, EVENTSUBTYPEDESCRIPTION, EVENTTYPEID, CREATEDBY, DATECREATED, MSGGROUP_ID, ISACTIVE)
 Values
   ('153', 'Telargo System Problem', 'Telargo System Problem', '33', 'kkanuganti', 
    TO_DATE('01/23/2013 10:37:00', 'MM/DD/YYYY HH24:MI:SS'), 'Katie Whalen', 'X');
Insert into TRANSP.EVENTSUBTYPE
   (ID, EVENTSUBTYPENAME, EVENTSUBTYPEDESCRIPTION, EVENTTYPEID, CREATEDBY, DATECREATED, MSGGROUP_ID, ISACTIVE)
 Values
   ('154', 'Independent Driver Decision', 'Independent Driver Decision', '33', 'kkanuganti', 
    TO_DATE('01/23/2013 10:37:00', 'MM/DD/YYYY HH24:MI:SS'), 'Field Managers', 'X');
Insert into TRANSP.EVENTSUBTYPE
   (ID, EVENTSUBTYPENAME, EVENTSUBTYPEDESCRIPTION, EVENTTYPEID, CREATEDBY, DATECREATED, MSGGROUP_ID, ISACTIVE)
 Values
   ('155', 'End of Route - Returns on Truck', 'End of Route - Returns on Truck', '33', 'kkanuganti', 
    TO_DATE('01/23/2013 10:37:00', 'MM/DD/YYYY HH24:MI:SS'), 'Field Managers', 'X');
Insert into TRANSP.EVENTSUBTYPE
   (ID, EVENTSUBTYPENAME, EVENTSUBTYPEDESCRIPTION, EVENTTYPEID, CREATEDBY, DATECREATED, MSGGROUP_ID, ISACTIVE)
 Values
   ('156', 'Equipment Failure', 'Equipment Failure', '33', 'kkanuganti', 
    TO_DATE('01/23/2013 10:37:00', 'MM/DD/YYYY HH24:MI:SS'), 'Trucks', 'X');
Insert into TRANSP.EVENTSUBTYPE
   (ID, EVENTSUBTYPENAME, EVENTSUBTYPEDESCRIPTION, EVENTTYPEID, CREATEDBY, DATECREATED, MSGGROUP_ID, ISACTIVE)
 Values
   ('157', 'Telargo System Problem', 'Telargo System Problem', '35', 'kkanuganti', 
    TO_DATE('01/23/2013 10:37:00', 'MM/DD/YYYY HH24:MI:SS'), 'Katie Whalen', 'X');
Insert into TRANSP.EVENTSUBTYPE
   (ID, EVENTSUBTYPENAME, EVENTSUBTYPEDESCRIPTION, EVENTTYPEID, CREATEDBY, DATECREATED, MSGGROUP_ID, ISACTIVE)
 Values
   ('158', 'Driver Not Responding', 'Driver Not Responding', '35', 'kkanuganti', 
    TO_DATE('01/23/2013 10:37:00', 'MM/DD/YYYY HH24:MI:SS'), 'Field Managers', 'X');
Insert into TRANSP.EVENTSUBTYPE
   (ID, EVENTSUBTYPENAME, EVENTSUBTYPEDESCRIPTION, EVENTTYPEID, CREATEDBY, DATECREATED, MSGGROUP_ID, ISACTIVE)
 Values
   ('160', 'Off Load - In Field', 'Off Load of Truck that is in the field', '10', 'kkanuganti', 
    TO_DATE('01/23/2013 10:37:00', 'MM/DD/YYYY HH24:MI:SS'), 'Field Managers', 'X');
Insert into TRANSP.EVENTSUBTYPE
   (ID, EVENTSUBTYPENAME, EVENTSUBTYPEDESCRIPTION, EVENTTYPEID, CREATEDBY, DATECREATED, MSGGROUP_ID, ISACTIVE)
 Values
   ('161', 'Off Load - At Facility', 'Off Load that occurs at the facility (prior t', '10', 'kkanuganti', 
    TO_DATE('01/23/2013 10:37:00', 'MM/DD/YYYY HH24:MI:SS'), 'Field Managers', 'X');
Insert into TRANSP.EVENTSUBTYPE
   (ID, EVENTSUBTYPENAME, EVENTSUBTYPEDESCRIPTION, EVENTTYPEID, CREATEDBY, DATECREATED, MSGGROUP_ID, ISACTIVE)
 Values
   ('162', 'Improper Truck Seal', 'Improper Truck Seal', '10', 'kkanuganti', 
    TO_DATE('01/23/2013 10:37:00', 'MM/DD/YYYY HH24:MI:SS'), 'Field Managers', 'X');
Insert into TRANSP.EVENTSUBTYPE
   (ID, EVENTSUBTYPENAME, EVENTSUBTYPEDESCRIPTION, EVENTTYPEID, CREATEDBY, DATECREATED, MSGGROUP_ID, ISACTIVE)
 Values
   ('163', 'Wrong Truck Number', 'Yard failed to correct truck number in syste,', '7', 'kkanuganti', 
    TO_DATE('01/23/2013 10:37:00', 'MM/DD/YYYY HH24:MI:SS'), 'Field Managers', 'X');
Insert into TRANSP.EVENTSUBTYPE
   (ID, EVENTSUBTYPENAME, EVENTSUBTYPEDESCRIPTION, EVENTTYPEID, CREATEDBY, DATECREATED, MSGGROUP_ID, ISACTIVE)
 Values
   ('164', 'Depot Truck Inventory Reorganization', 'Depot Truck Inventory Reorganization', '35', 'kkanuganti', 
    TO_DATE('01/23/2013 10:37:00', 'MM/DD/YYYY HH24:MI:SS'), 'Field Managers', 'X');
Insert into TRANSP.EVENTSUBTYPE
   (ID, EVENTSUBTYPENAME, EVENTSUBTYPEDESCRIPTION, EVENTTYPEID, CREATEDBY, DATECREATED, MSGGROUP_ID, ISACTIVE)
 Values
   ('165', 'Route Truck Inventory Reorganization', 'Route Truck Inventory Reorganization', '35', 'kkanuganti', 
    TO_DATE('01/23/2013 10:37:00', 'MM/DD/YYYY HH24:MI:SS'), 'Field Managers', 'X');
Insert into TRANSP.EVENTSUBTYPE
   (ID, EVENTSUBTYPENAME, EVENTSUBTYPEDESCRIPTION, EVENTTYPEID, CREATEDBY, DATECREATED, ISACTIVE)
 Values
   ('166', 'Modified a Route', 'Modified a Route', '26', 'kkanuganti', 
    TO_DATE('01/23/2013 10:37:00', 'MM/DD/YYYY HH24:MI:SS'), 'X');
Insert into TRANSP.EVENTSUBTYPE
   (ID, EVENTSUBTYPENAME, EVENTSUBTYPEDESCRIPTION, EVENTTYPEID, CREATEDBY, DATECREATED, MSGGROUP_ID, ISACTIVE)
 Values
   ('167', 'A 15 Min Prior to Dispatch', 'A   15 Min Prior to Dispatch', '37', 'kkanuganti', 
    TO_DATE('01/23/2013 10:37:00', 'MM/DD/YYYY HH24:MI:SS'), 'Ops Management Alerts', 'X');
Insert into TRANSP.EVENTSUBTYPE
   (ID, EVENTSUBTYPENAME, EVENTSUBTYPEDESCRIPTION, EVENTTYPEID, CREATEDBY, DATECREATED, MSGGROUP_ID, ISACTIVE)
 Values
   ('169', 'C After Dispatch Exception', 'C    After Dispatch Exception', '37', 'kkanuganti', 
    TO_DATE('01/23/2013 10:37:00', 'MM/DD/YYYY HH24:MI:SS'), 'Ops Management Alerts', 'X');
Insert into TRANSP.EVENTSUBTYPE
   (ID, EVENTSUBTYPENAME, EVENTSUBTYPEDESCRIPTION, EVENTTYPEID, CREATEDBY, DATECREATED, ISACTIVE)
 Values
   ('170', 'Recommendations', 'Recommendations', '38', 'kkanuganti', 
    TO_DATE('01/23/2013 10:37:00', 'MM/DD/YYYY HH24:MI:SS'), 'X');
Insert into TRANSP.EVENTSUBTYPE
   (ID, EVENTSUBTYPENAME, EVENTSUBTYPEDESCRIPTION, EVENTTYPEID, CREATEDBY, DATECREATED, MSGGROUP_ID, ISACTIVE)
 Values
   ('171', 'Depot Offloading to Runners', 'Depot Offloading to Runners', '35', 'kkanuganti', 
    TO_DATE('01/23/2013 10:37:00', 'MM/DD/YYYY HH24:MI:SS'), 'Field Managers', 'X');
Insert into TRANSP.EVENTSUBTYPE
   (ID, EVENTSUBTYPENAME, EVENTSUBTYPEDESCRIPTION, EVENTTYPEID, CREATEDBY, DATECREATED, MSGGROUP_ID, ISACTIVE)
 Values
   ('172', 'Equipment Failure', 'Equipment Failure', '35', 'kkanuganti', 
    TO_DATE('01/23/2013 10:37:00', 'MM/DD/YYYY HH24:MI:SS'), 'Field Managers', 'X');
Insert into TRANSP.EVENTSUBTYPE
   (ID, EVENTSUBTYPENAME, EVENTSUBTYPEDESCRIPTION, EVENTTYPEID, CREATEDBY, DATECREATED, MSGGROUP_ID, ISACTIVE)
 Values
   ('173', 'On Premises 12AM-5AM', 'On Premises 12AM-5AM', '35', 'kkanuganti', 
    TO_DATE('01/23/2013 10:37:00', 'MM/DD/YYYY HH24:MI:SS'), 'Transportation Shipping Yard', 'X');
Insert into TRANSP.EVENTSUBTYPE
   (ID, EVENTSUBTYPENAME, EVENTSUBTYPEDESCRIPTION, EVENTTYPEID, CREATEDBY, DATECREATED, MSGGROUP_ID, ISACTIVE)
 Values
   ('174', 'Route Not Found', 'Route Not Found', '35', 'kkanuganti', 
    TO_DATE('01/23/2013 10:37:00', 'MM/DD/YYYY HH24:MI:SS'), 'George Walker', 'X');
Insert into TRANSP.EVENTSUBTYPE
   (ID, EVENTSUBTYPENAME, EVENTSUBTYPEDESCRIPTION, EVENTTYPEID, CREATEDBY, DATECREATED, MSGGROUP_ID, ISACTIVE)
 Values
   ('175', 'Depot Truck Reorganization', 'Depot Truck Reorganization', '36', 'kkanuganti', 
    TO_DATE('01/23/2013 10:37:00', 'MM/DD/YYYY HH24:MI:SS'), 'Field Managers', 'X');
Insert into TRANSP.EVENTSUBTYPE
   (ID, EVENTSUBTYPENAME, EVENTSUBTYPEDESCRIPTION, EVENTTYPEID, CREATEDBY, DATECREATED, MSGGROUP_ID, ISACTIVE)
 Values
   ('176', 'Route Truck Inventory Reorganization', 'Route Truck Inventory Reorganization', '36', 'kkanuganti', 
    TO_DATE('01/23/2013 10:37:00', 'MM/DD/YYYY HH24:MI:SS'), 'Field Managers', 'X');
Insert into TRANSP.EVENTSUBTYPE
   (ID, EVENTSUBTYPENAME, EVENTSUBTYPEDESCRIPTION, EVENTTYPEID, CREATEDBY, DATECREATED, MSGGROUP_ID, ISACTIVE)
 Values
   ('177', 'Freezer Door Does Not Close', 'Freezer Door Does Not Close', '36', 'kkanuganti', 
    TO_DATE('01/23/2013 10:37:00', 'MM/DD/YYYY HH24:MI:SS'), 'Field Managers', 'X');
Insert into TRANSP.EVENTSUBTYPE
   (ID, EVENTSUBTYPENAME, EVENTSUBTYPEDESCRIPTION, EVENTTYPEID, CREATEDBY, DATECREATED, MSGGROUP_ID, ISACTIVE)
 Values
   ('178', 'Driver Not Responding', 'Driver Not Responding', '36', 'kkanuganti', 
    TO_DATE('01/23/2013 10:37:00', 'MM/DD/YYYY HH24:MI:SS'), 'Field Managers', 'X');
Insert into TRANSP.EVENTSUBTYPE
   (ID, EVENTSUBTYPENAME, EVENTSUBTYPEDESCRIPTION, EVENTTYPEID, CREATEDBY, DATECREATED, MSGGROUP_ID, ISACTIVE)
 Values
   ('179', 'On Premises 12AM-5AM', 'On Premises 12AM-5AM', '36', 'kkanuganti', 
    TO_DATE('01/23/2013 10:37:00', 'MM/DD/YYYY HH24:MI:SS'), 'Transportation Shipping Yard', 'X');
Insert into TRANSP.EVENTSUBTYPE
   (ID, EVENTSUBTYPENAME, EVENTSUBTYPEDESCRIPTION, EVENTTYPEID, CREATEDBY, DATECREATED, MSGGROUP_ID, ISACTIVE)
 Values
   ('180', 'Route Not Found', 'Route Not Found', '36', 'kkanuganti', 
    TO_DATE('01/23/2013 10:37:00', 'MM/DD/YYYY HH24:MI:SS'), 'George Walker', 'X');
Insert into TRANSP.EVENTSUBTYPE
   (ID, EVENTSUBTYPENAME, EVENTSUBTYPEDESCRIPTION, EVENTTYPEID, CREATEDBY, DATECREATED, MSGGROUP_ID, ISACTIVE)
 Values
   ('181', 'Telargo System Problem', 'Telargo System Problem', '36', 'kkanuganti', 
    TO_DATE('01/23/2013 10:37:00', 'MM/DD/YYYY HH24:MI:SS'), 'Katie Whalen', 'X');
Insert into TRANSP.EVENTSUBTYPE
   (ID, EVENTSUBTYPENAME, EVENTSUBTYPEDESCRIPTION, EVENTTYPEID, CREATEDBY, DATECREATED)
 Values
   ('182', 'Other', 'Other', '36', 'kkanuganti', 
    TO_DATE('01/23/2013 10:37:00', 'MM/DD/YYYY HH24:MI:SS'));
Insert into TRANSP.EVENTSUBTYPE
   (ID, EVENTSUBTYPENAME, EVENTSUBTYPEDESCRIPTION, EVENTTYPEID, CREATEDBY, DATECREATED, MSGGROUP_ID)
 Values
   ('183', 'Customer Call Feature Failed', 'Customer Call Feature Failed', '24', 'kkanuganti', 
    TO_DATE('01/23/2013 10:37:00', 'MM/DD/YYYY HH24:MI:SS'), 'Ops Management Alerts');
Insert into TRANSP.EVENTSUBTYPE
   (ID, EVENTSUBTYPENAME, EVENTSUBTYPEDESCRIPTION, EVENTTYPEID, CREATEDBY, DATECREATED, MSGGROUP_ID, ISACTIVE)
 Values
   ('184', 'GPS System Failure', 'GPS System Failure', '9', 'kkanuganti', 
    TO_DATE('01/23/2013 10:37:00', 'MM/DD/YYYY HH24:MI:SS'), 'Returns Center', 'X');
Insert into TRANSP.EVENTSUBTYPE
   (ID, EVENTSUBTYPENAME, EVENTSUBTYPEDESCRIPTION, EVENTTYPEID, CREATEDBY, DATECREATED, ISACTIVE)
 Values
   ('185', 'Summary', 'Summary', '39', 'kkanuganti', 
    TO_DATE('01/23/2013 10:37:00', 'MM/DD/YYYY HH24:MI:SS'), 'X');
Insert into TRANSP.EVENTSUBTYPE
   (ID, EVENTSUBTYPENAME, EVENTSUBTYPEDESCRIPTION, EVENTTYPEID, CREATEDBY, DATECREATED, MSGGROUP_ID, ISACTIVE)
 Values
   ('186', 'Telargo Equipment Failure', 'Telargo Sensor isnt reading open/close prope', '35', 'kkanuganti', 
    TO_DATE('01/23/2013 10:37:00', 'MM/DD/YYYY HH24:MI:SS'), 'Transportation Shipping Yard', 'X');
Insert into TRANSP.EVENTSUBTYPE
   (ID, EVENTSUBTYPENAME, EVENTSUBTYPEDESCRIPTION, EVENTTYPEID, CREATEDBY, DATECREATED, MSGGROUP_ID, ISACTIVE)
 Values
   ('187', 'Telargo Equipment Failure', 'Back Door Event has no (close Values)', '40', 'kkanuganti', 
    TO_DATE('01/23/2013 10:37:00', 'MM/DD/YYYY HH24:MI:SS'), 'Katie Whalen', 'X');
Insert into TRANSP.EVENTSUBTYPE
   (ID, EVENTSUBTYPENAME, EVENTSUBTYPEDESCRIPTION, EVENTTYPEID, CREATEDBY, DATECREATED, MSGGROUP_ID, ISACTIVE)
 Values
   ('188', 'Driver Not Responding', 'OPS Center Agent has sent a minimum of 2 Next', '40', 'kkanuganti', 
    TO_DATE('01/23/2013 10:37:00', 'MM/DD/YYYY HH24:MI:SS'), 'Field Managers', 'X');
Insert into TRANSP.EVENTSUBTYPE
   (ID, EVENTSUBTYPENAME, EVENTSUBTYPEDESCRIPTION, EVENTTYPEID, CREATEDBY, DATECREATED, MSGGROUP_ID, ISACTIVE)
 Values
   ('189', 'Telargo System Problem', 'Examples include: Event: is blank or no alert', '40', 'kkanuganti', 
    TO_DATE('01/23/2013 10:37:00', 'MM/DD/YYYY HH24:MI:SS'), 'Katie Whalen', 'X');
Insert into TRANSP.EVENTSUBTYPE
   (ID, EVENTSUBTYPENAME, EVENTSUBTYPEDESCRIPTION, EVENTTYPEID, CREATEDBY, DATECREATED, MSGGROUP_ID, ISACTIVE)
 Values
   ('190', 'Independent Driver Decision', 'Driver is unaware', '40', 'kkanuganti', 
    TO_DATE('01/23/2013 10:37:00', 'MM/DD/YYYY HH24:MI:SS'), 'Field Managers', 'X');
Insert into TRANSP.EVENTSUBTYPE
   (ID, EVENTSUBTYPENAME, EVENTSUBTYPEDESCRIPTION, EVENTTYPEID, CREATEDBY, DATECREATED, MSGGROUP_ID, ISACTIVE)
 Values
   ('192', 'Driver Not Responding', 'OPS Center Agent has sent a minimum of 2 Next', '41', 'kkanuganti', 
    TO_DATE('01/23/2013 10:37:00', 'MM/DD/YYYY HH24:MI:SS'), 'Field Managers', 'X');
Insert into TRANSP.EVENTSUBTYPE
   (ID, EVENTSUBTYPENAME, EVENTSUBTYPEDESCRIPTION, EVENTTYPEID, CREATEDBY, DATECREATED, MSGGROUP_ID, ISACTIVE)
 Values
   ('193', 'Telargo System Problem', 'TBD', '41', 'kkanuganti', 
    TO_DATE('01/23/2013 10:37:00', 'MM/DD/YYYY HH24:MI:SS'), 'Katie Whalen', 'X');
Insert into TRANSP.EVENTSUBTYPE
   (ID, EVENTSUBTYPENAME, EVENTSUBTYPEDESCRIPTION, EVENTTYPEID, CREATEDBY, DATECREATED, MSGGROUP_ID, ISACTIVE)
 Values
   ('195', 'Other', 'Any reason that does not fall within any of t', '41', 'kkanuganti', 
    TO_DATE('01/23/2013 10:37:00', 'MM/DD/YYYY HH24:MI:SS'), 'Ops Management Alerts', 'X');
Insert into TRANSP.EVENTSUBTYPE
   (ID, EVENTSUBTYPENAME, EVENTSUBTYPEDESCRIPTION, EVENTTYPEID, CREATEDBY, DATECREATED, MSGGROUP_ID, ISACTIVE)
 Values
   ('196', 'Driver Not Responding', 'OPS Center Agent has sent a minimum of 2 Next', '43', 'kkanuganti', 
    TO_DATE('01/23/2013 10:37:00', 'MM/DD/YYYY HH24:MI:SS'), 'Field Managers', 'X');
Insert into TRANSP.EVENTSUBTYPE
   (ID, EVENTSUBTYPENAME, EVENTSUBTYPEDESCRIPTION, EVENTTYPEID, CREATEDBY, DATECREATED, MSGGROUP_ID, ISACTIVE)
 Values
   ('197', 'Telargo System Problem', 'Examples include: Event: is blank or no alert', '43', 'kkanuganti', 
    TO_DATE('01/23/2013 10:37:00', 'MM/DD/YYYY HH24:MI:SS'), 'Katie Whalen', 'X');
Insert into TRANSP.EVENTSUBTYPE
   (ID, EVENTSUBTYPENAME, EVENTSUBTYPEDESCRIPTION, EVENTTYPEID, CREATEDBY, DATECREATED, MSGGROUP_ID, ISACTIVE)
 Values
   ('198', 'Equipment Failure', 'Emergency Brake is engaged and truck is out o', '43', 'kkanuganti', 
    TO_DATE('01/23/2013 10:37:00', 'MM/DD/YYYY HH24:MI:SS'), 'Transportation Shipping Yard', 'X');
Insert into TRANSP.EVENTSUBTYPE
   (ID, EVENTSUBTYPENAME, EVENTSUBTYPEDESCRIPTION, EVENTTYPEID, CREATEDBY, DATECREATED, MSGGROUP_ID, ISACTIVE)
 Values
   ('199', 'Independent Driver Decision', 'Driver is unaware that his Emergency Brake is', '43', 'kkanuganti', 
    TO_DATE('01/23/2013 10:37:00', 'MM/DD/YYYY HH24:MI:SS'), 'Field Managers', 'X');
Insert into TRANSP.EVENTSUBTYPE
   (ID, EVENTSUBTYPENAME, EVENTSUBTYPEDESCRIPTION, EVENTTYPEID, CREATEDBY, DATECREATED, MSGGROUP_ID, ISACTIVE)
 Values
   ('200', 'Other', 'Any reason that does not fall within any of t', '43', 'kkanuganti', 
    TO_DATE('01/23/2013 10:37:00', 'MM/DD/YYYY HH24:MI:SS'), 'Ops Management Alerts', 'X');
Insert into TRANSP.EVENTSUBTYPE
   (ID, EVENTSUBTYPENAME, EVENTSUBTYPEDESCRIPTION, EVENTTYPEID, CREATEDBY, DATECREATED, MSGGROUP_ID, ISACTIVE)
 Values
   ('201', 'Independent Driver Decision', 'Driver reports that he isnt aware of the door', '35', 'kkanuganti', 
    TO_DATE('01/23/2013 10:37:00', 'MM/DD/YYYY HH24:MI:SS'), 'Field Managers', 'X');
Insert into TRANSP.EVENTSUBTYPE
   (ID, EVENTSUBTYPENAME, EVENTSUBTYPEDESCRIPTION, EVENTTYPEID, CREATEDBY, DATECREATED, MSGGROUP_ID, ISACTIVE)
 Values
   ('202', 'Independent Driver Decision', 'Driver reports that he isnt aware of the door', '36', 'kkanuganti', 
    TO_DATE('01/23/2013 10:37:00', 'MM/DD/YYYY HH24:MI:SS'), 'Field Managers', 'X');
Insert into TRANSP.EVENTSUBTYPE
   (ID, EVENTSUBTYPENAME, EVENTSUBTYPEDESCRIPTION, EVENTTYPEID, CREATEDBY, DATECREATED, MSGGROUP_ID, ISACTIVE)
 Values
   ('203', 'Telargo Equipment Failure', 'Sensor Issue open/close within a short period', '36', 'kkanuganti', 
    TO_DATE('01/23/2013 10:37:00', 'MM/DD/YYYY HH24:MI:SS'), 'Transportation Shipping Yard', 'X');
Insert into TRANSP.EVENTSUBTYPE
   (ID, EVENTSUBTYPENAME, EVENTSUBTYPEDESCRIPTION, EVENTTYPEID, CREATEDBY, DATECREATED, ISACTIVE)
 Values
   ('204', 'Emergency Brake OFF', 'While taking action on the alert, event value', '43', 'kkanuganti', 
    TO_DATE('01/23/2013 10:37:00', 'MM/DD/YYYY HH24:MI:SS'), 'X');
Insert into TRANSP.EVENTSUBTYPE
   (ID, EVENTSUBTYPENAME, EVENTSUBTYPEDESCRIPTION, EVENTTYPEID, CREATEDBY, DATECREATED, ISACTIVE)
 Values
   ('205', 'Door Closed', 'While taking action on the alert, event value', '40', 'kkanuganti', 
    TO_DATE('01/23/2013 10:37:00', 'MM/DD/YYYY HH24:MI:SS'), 'X');
Insert into TRANSP.EVENTSUBTYPE
   (ID, EVENTSUBTYPENAME, EVENTSUBTYPEDESCRIPTION, EVENTTYPEID, CREATEDBY, DATECREATED, MSGGROUP_ID, ISACTIVE)
 Values
   ('207', 'Equipment Failure', 'Driver reports equipment issue (i.e.door not', '36', 'kkanuganti', 
    TO_DATE('01/23/2013 10:37:00', 'MM/DD/YYYY HH24:MI:SS'), 'Transportation', 'X');
Insert into TRANSP.EVENTSUBTYPE
   (ID, EVENTSUBTYPENAME, EVENTSUBTYPEDESCRIPTION, EVENTTYPEID, CREATEDBY, DATECREATED, ISACTIVE)
 Values
   ('208', 'EB On and Ignition Off at the base', 'Emergency Brake is ON, and the truck ignition', '43', 'kkanuganti', 
    TO_DATE('01/23/2013 10:37:00', 'MM/DD/YYYY HH24:MI:SS'), 'X');
Insert into TRANSP.EVENTSUBTYPE
   (ID, EVENTSUBTYPENAME, EVENTSUBTYPEDESCRIPTION, EVENTTYPEID, CREATEDBY, DATECREATED, MSGGROUP_ID, ISACTIVE)
 Values
   ('209', 'Incomplete FireTruck-IFS Handoff', 'Incomplete FireTruck-IFS Handoff', '23', 'kkanuganti', 
    TO_DATE('01/23/2013 10:37:00', 'MM/DD/YYYY HH24:MI:SS'), 'Fire Truck Management', 'X');
Insert into TRANSP.EVENTSUBTYPE
   (ID, EVENTSUBTYPENAME, EVENTSUBTYPEDESCRIPTION, EVENTTYPEID, CREATEDBY, DATECREATED, ISACTIVE)
 Values
   ('210', 'Cust Call Requested/IVR Issue', 'Cust Call Requested/IVR Issue', '44', 'kkanuganti', 
    TO_DATE('01/23/2013 10:37:00', 'MM/DD/YYYY HH24:MI:SS'), 'X');
Insert into TRANSP.EVENTSUBTYPE
   (ID, EVENTSUBTYPENAME, EVENTSUBTYPEDESCRIPTION, EVENTTYPEID, CREATEDBY, DATECREATED, ISACTIVE)
 Values
   ('211', 'Scanner Issue', 'Scanner Issue', '44', 'kkanuganti', 
    TO_DATE('01/23/2013 10:37:00', 'MM/DD/YYYY HH24:MI:SS'), 'X');
Insert into TRANSP.EVENTSUBTYPE
   (ID, EVENTSUBTYPENAME, EVENTSUBTYPEDESCRIPTION, EVENTTYPEID, CREATEDBY, DATECREATED, ISACTIVE)
 Values
   ('212', 'Missing/Damaged/Found Carton', 'Missing/Damaged/Found Carton', '44', 'kkanuganti', 
    TO_DATE('01/23/2013 10:37:00', 'MM/DD/YYYY HH24:MI:SS'), 'X');
Insert into TRANSP.EVENTSUBTYPE
   (ID, EVENTSUBTYPENAME, EVENTSUBTYPEDESCRIPTION, EVENTTYPEID, CREATEDBY, DATECREATED, ISACTIVE)
 Values
   ('213', 'Other', 'Other', '44', 'kkanuganti', 
    TO_DATE('01/23/2013 10:37:00', 'MM/DD/YYYY HH24:MI:SS'), 'X');
Insert into TRANSP.EVENTSUBTYPE
   (ID, EVENTSUBTYPENAME, EVENTSUBTYPEDESCRIPTION, EVENTTYPEID, CREATEDBY, DATECREATED, ISACTIVE)
 Values
   ('215', '2nd Attempt Completed', '2nd Attempt Completed', '44', 'kkanuganti', 
    TO_DATE('01/23/2013 10:37:00', 'MM/DD/YYYY HH24:MI:SS'), 'X');
Insert into TRANSP.EVENTSUBTYPE
   (ID, EVENTSUBTYPENAME, EVENTSUBTYPEDESCRIPTION, EVENTTYPEID, CREATEDBY, DATECREATED, ISACTIVE)
 Values
   ('216', 'Confirm/Forgot to scan', 'Confirm/Forgot to scan', '44', 'kkanuganti', 
    TO_DATE('01/23/2013 10:37:00', 'MM/DD/YYYY HH24:MI:SS'), 'X');
Insert into TRANSP.EVENTSUBTYPE
   (ID, EVENTSUBTYPENAME, EVENTSUBTYPEDESCRIPTION, EVENTTYPEID, CREATEDBY, DATECREATED, ISACTIVE)
 Values
   ('217', 'Needs Directions/Address', 'Needs Directions/Address', '44', 'kkanuganti', 
    TO_DATE('01/23/2013 10:37:00', 'MM/DD/YYYY HH24:MI:SS'), 'X');
Insert into TRANSP.EVENTSUBTYPE
   (ID, EVENTSUBTYPENAME, EVENTSUBTYPEDESCRIPTION, EVENTTYPEID, CREATEDBY, DATECREATED, ISACTIVE)
 Values
   ('218', 'Report Late, FT not needed', 'Report Late, FT not needed', '44', 'kkanuganti', 
    TO_DATE('01/23/2013 10:37:00', 'MM/DD/YYYY HH24:MI:SS'), 'X');
Insert into TRANSP.EVENTSUBTYPE
   (ID, EVENTSUBTYPENAME, EVENTSUBTYPEDESCRIPTION, EVENTTYPEID, CREATEDBY, DATECREATED, ISACTIVE)
 Values
   ('219', 'Request Firetruck', 'Request Firetruck', '44', 'kkanuganti', 
    TO_DATE('01/23/2013 10:37:00', 'MM/DD/YYYY HH24:MI:SS'), 'X');
Insert into TRANSP.EVENTSUBTYPE
   (ID, EVENTSUBTYPENAME, EVENTSUBTYPEDESCRIPTION, EVENTTYPEID, CREATEDBY, DATECREATED)
 Values
   ('220', '2nd Attempt Verify Cust is Home', '2nd Attempt Verify Cust is Home', '44', 'kkanuganti', 
    TO_DATE('01/23/2013 10:37:00', 'MM/DD/YYYY HH24:MI:SS'));
Insert into TRANSP.EVENTSUBTYPE
   (ID, EVENTSUBTYPENAME, EVENTSUBTYPEDESCRIPTION, EVENTTYPEID, CREATEDBY, DATECREATED)
 Values
   ('221', 'Nxtl- 2nd Attempt Completed', 'Nxtl- 2nd Attempt Completed', '24', 'kkanuganti', 
    TO_DATE('01/23/2013 10:37:00', 'MM/DD/YYYY HH24:MI:SS'));
Insert into TRANSP.EVENTSUBTYPE
   (ID, EVENTSUBTYPENAME, EVENTSUBTYPEDESCRIPTION, EVENTTYPEID, CREATEDBY, DATECREATED)
 Values
   ('222', 'Nxtl- Confirm/Forgot to Scan', 'Nxtl- Confirm/Forgot to Scan', '24', 'kkanuganti', 
    TO_DATE('01/23/2013 10:37:00', 'MM/DD/YYYY HH24:MI:SS'));
Insert into TRANSP.EVENTSUBTYPE
   (ID, EVENTSUBTYPENAME, EVENTSUBTYPEDESCRIPTION, EVENTTYPEID, CREATEDBY, DATECREATED)
 Values
   ('223', 'Nxtl- Cust Call Requested, no IVR Issue', 'Nxtl- Cust Call Requested, no IVR Issue', '24', 'kkanuganti', 
    TO_DATE('01/23/2013 10:37:00', 'MM/DD/YYYY HH24:MI:SS'));
Insert into TRANSP.EVENTSUBTYPE
   (ID, EVENTSUBTYPENAME, EVENTSUBTYPEDESCRIPTION, EVENTTYPEID, CREATEDBY, DATECREATED)
 Values
   ('224', 'Nxtl- Missing/Damaged/Found Carton', 'Nxtl- Missing/Damaged/Found Carton', '24', 'kkanuganti', 
    TO_DATE('01/23/2013 10:37:00', 'MM/DD/YYYY HH24:MI:SS'));
Insert into TRANSP.EVENTSUBTYPE
   (ID, EVENTSUBTYPENAME, EVENTSUBTYPEDESCRIPTION, EVENTTYPEID, CREATEDBY, DATECREATED)
 Values
   ('225', 'Nxtl- Needs Directions/Address', 'Nxtl- Needs Directions/Address', '24', 'kkanuganti', 
    TO_DATE('01/23/2013 10:37:00', 'MM/DD/YYYY HH24:MI:SS'));
Insert into TRANSP.EVENTSUBTYPE
   (ID, EVENTSUBTYPENAME, EVENTSUBTYPEDESCRIPTION, EVENTTYPEID, CREATEDBY, DATECREATED)
 Values
   ('227', 'Nxtl- Other', 'Nxtl- Other', '24', 'kkanuganti', 
    TO_DATE('01/23/2013 10:37:00', 'MM/DD/YYYY HH24:MI:SS'));
Insert into TRANSP.EVENTSUBTYPE
   (ID, EVENTSUBTYPENAME, EVENTSUBTYPEDESCRIPTION, EVENTTYPEID, CREATEDBY, DATECREATED)
 Values
   ('228', 'Nxtl- Report Late, FT not needed', 'Nxtl- Report Late, FT not needed', '24', 'kkanuganti', 
    TO_DATE('01/23/2013 10:37:00', 'MM/DD/YYYY HH24:MI:SS'));
Insert into TRANSP.EVENTSUBTYPE
   (ID, EVENTSUBTYPENAME, EVENTSUBTYPEDESCRIPTION, EVENTTYPEID, CREATEDBY, DATECREATED)
 Values
   ('229', 'Nxtl- Request FireTruck', 'Nxtl- Request FireTruck', '24', 'kkanuganti', 
    TO_DATE('01/23/2013 10:37:00', 'MM/DD/YYYY HH24:MI:SS'));
Insert into TRANSP.EVENTSUBTYPE
   (ID, EVENTSUBTYPENAME, EVENTSUBTYPEDESCRIPTION, EVENTTYPEID, CREATEDBY, DATECREATED)
 Values
   ('230', 'Nxtl- Scanner Issue', 'Nxtl- Scanner Issue', '24', 'kkanuganti', 
    TO_DATE('01/23/2013 10:37:00', 'MM/DD/YYYY HH24:MI:SS'));
Insert into TRANSP.EVENTSUBTYPE
   (ID, EVENTSUBTYPENAME, EVENTSUBTYPEDESCRIPTION, EVENTTYPEID, CREATEDBY, DATECREATED)
 Values
   ('231', 'Nxtl- 1st Attempt Completed', 'Nxtl- 1st Attempt Completed', '24', 'kkanuganti', 
    TO_DATE('01/23/2013 10:37:00', 'MM/DD/YYYY HH24:MI:SS'));
Insert into TRANSP.EVENTSUBTYPE
   (ID, EVENTSUBTYPENAME, EVENTSUBTYPEDESCRIPTION, EVENTTYPEID, CREATEDBY, DATECREATED)
 Values
   ('232', 'At Risk Recap', 'At Risk Recap', '24', 'kkanuganti', 
    TO_DATE('01/23/2013 10:37:00', 'MM/DD/YYYY HH24:MI:SS'));
Insert into TRANSP.EVENTSUBTYPE
   (ID, EVENTSUBTYPENAME, EVENTSUBTYPEDESCRIPTION, EVENTTYPEID, CREATEDBY, DATECREATED)
 Values
   ('233', 'Nxtl- IVR Issue', 'Nxtl- IVR Issue', '24', 'kkanuganti', 
    TO_DATE('01/23/2013 10:37:00', 'MM/DD/YYYY HH24:MI:SS'));
Insert into TRANSP.EVENTSUBTYPE
   (ID, EVENTSUBTYPENAME, EVENTSUBTYPEDESCRIPTION, EVENTTYPEID, CREATEDBY, DATECREATED, ISACTIVE)
 Values
   ('234', 'At Risk Recap - Driver did not call us abou', 'At Risk Recap - Driver did not call us abou', '24', 'kkanuganti', 
    TO_DATE('01/23/2013 10:37:00', 'MM/DD/YYYY HH24:MI:SS'), 'X');
Insert into TRANSP.EVENTSUBTYPE
   (ID, EVENTSUBTYPENAME, EVENTSUBTYPEDESCRIPTION, EVENTTYPEID, CREATEDBY, DATECREATED, ISACTIVE)
 Values
   ('235', 'At Risk Recap - Spoke with driver, but not', 'At Risk Recap - Spoke with driver, but not', '24', 'kkanuganti', 
    TO_DATE('01/23/2013 10:37:00', 'MM/DD/YYYY HH24:MI:SS'), 'X');
Insert into TRANSP.EVENTSUBTYPE
   (ID, EVENTSUBTYPENAME, EVENTSUBTYPEDESCRIPTION, EVENTTYPEID, CREATEDBY, DATECREATED, ISACTIVE)
 Values
   ('236', 'At Risk Recap - Scanning Issue', 'At Risk Recap - Scanning Issue', '24', 'kkanuganti', 
    TO_DATE('01/23/2013 10:37:00', 'MM/DD/YYYY HH24:MI:SS'), 'X');
Insert into TRANSP.EVENTSUBTYPE
   (ID, EVENTSUBTYPENAME, EVENTSUBTYPEDESCRIPTION, EVENTTYPEID, CREATEDBY, DATECREATED, ISACTIVE)
 Values
   ('237', 'At Risk Recap - Forgot to Scan in window', 'At Risk Recap - Forgot to Scan in window', '24', 'kkanuganti', 
    TO_DATE('01/23/2013 10:37:00', 'MM/DD/YYYY HH24:MI:SS'), 'X');
Insert into TRANSP.EVENTSUBTYPE
   (ID, EVENTSUBTYPENAME, EVENTSUBTYPEDESCRIPTION, EVENTTYPEID, CREATEDBY, DATECREATED, ISACTIVE)
 Values
   ('238', 'At Risk Recap - Other', 'At Risk Recap - Other', '24', 'kkanuganti', 
    TO_DATE('01/23/2013 10:37:00', 'MM/DD/YYYY HH24:MI:SS'), 'X');
Insert into TRANSP.EVENTSUBTYPE
   (ID, EVENTSUBTYPENAME, EVENTSUBTYPEDESCRIPTION, EVENTTYPEID, CREATEDBY, DATECREATED, ISACTIVE)
 Values
   ('239', 'Special Delivery', 'Special Delivery', '23', 'kkanuganti', 
    TO_DATE('01/23/2013 10:37:00', 'MM/DD/YYYY HH24:MI:SS'), 'X');
Insert into TRANSP.EVENTSUBTYPE
   (ID, EVENTSUBTYPENAME, EVENTSUBTYPEDESCRIPTION, EVENTTYPEID, CREATEDBY, DATECREATED, ISACTIVE)
 Values
   ('240', 'Pick-Up', 'Pick-Up', '23', 'kkanuganti', 
    TO_DATE('01/23/2013 10:37:00', 'MM/DD/YYYY HH24:MI:SS'), 'X');
Insert into TRANSP.EVENTSUBTYPE
   (ID, EVENTSUBTYPENAME, EVENTSUBTYPEDESCRIPTION, EVENTTYPEID, CREATEDBY, DATECREATED, ISACTIVE)
 Values
   ('241', 'Window Steering Turned On', 'Activated window steering promotion', '26', 'kkanuganti', 
    TO_DATE('01/23/2013 10:37:00', 'MM/DD/YYYY HH24:MI:SS'), 'X');
Insert into TRANSP.EVENTSUBTYPE
   (ID, EVENTSUBTYPENAME, EVENTSUBTYPEDESCRIPTION, EVENTTYPEID, CREATEDBY, DATECREATED)
 Values
   ('242', 'Food Safety', 'Food Safety', '23', 'kkanuganti', 
    TO_DATE('01/23/2013 10:37:00', 'MM/DD/YYYY HH24:MI:SS'));
Insert into TRANSP.EVENTSUBTYPE
   (ID, EVENTSUBTYPENAME, EVENTSUBTYPEDESCRIPTION, EVENTTYPEID, CREATEDBY, DATECREATED, ISACTIVE)
 Values
   ('243', 'Risk', 'Risk', '23', 'kkanuganti', 
    TO_DATE('01/23/2013 10:37:00', 'MM/DD/YYYY HH24:MI:SS'), 'X');
Insert into TRANSP.EVENTSUBTYPE
   (ID, EVENTSUBTYPENAME, EVENTSUBTYPEDESCRIPTION, EVENTTYPEID, CREATEDBY, DATECREATED, MSGGROUP_ID, ISACTIVE)
 Values
   ('244', 'Mirror', 'Mirror Broken', '9', 'kkanuganti', 
    TO_DATE('01/23/2013 10:37:00', 'MM/DD/YYYY HH24:MI:SS'), 'Trucks', 'X');
Insert into TRANSP.EVENTSUBTYPE
   (ID, EVENTSUBTYPENAME, EVENTSUBTYPEDESCRIPTION, EVENTTYPEID, CREATEDBY, DATECREATED, ISACTIVE)
 Values
   ('245', 'COS Recap 8-11am', 'COS Recap 8-11am', '24', 'kkanuganti', 
    TO_DATE('01/23/2013 10:37:00', 'MM/DD/YYYY HH24:MI:SS'), 'X');
Insert into TRANSP.EVENTSUBTYPE
   (ID, EVENTSUBTYPENAME, EVENTSUBTYPEDESCRIPTION, EVENTTYPEID, CREATEDBY, DATECREATED, ISACTIVE)
 Values
   ('246', 'COS Recap 11-2pm', 'COS Recap 11-2pm', '24', 'kkanuganti', 
    TO_DATE('01/23/2013 10:37:00', 'MM/DD/YYYY HH24:MI:SS'), 'X');
Insert into TRANSP.EVENTSUBTYPE
   (ID, EVENTSUBTYPENAME, EVENTSUBTYPEDESCRIPTION, EVENTTYPEID, CREATEDBY, DATECREATED, ISACTIVE)
 Values
   ('247', 'COS Recap 2-4pm', 'COS Recap 2-4pm', '24', 'kkanuganti', 
    TO_DATE('01/23/2013 10:37:00', 'MM/DD/YYYY HH24:MI:SS'), 'X');
Insert into TRANSP.EVENTSUBTYPE
   (ID, EVENTSUBTYPENAME, EVENTSUBTYPEDESCRIPTION, EVENTTYPEID, CREATEDBY, DATECREATED, ISACTIVE)
 Values
   ('248', 'COS Recap Overall', 'COS Recap Overall', '24', 'kkanuganti', 
    TO_DATE('01/23/2013 10:37:00', 'MM/DD/YYYY HH24:MI:SS'), 'X');
Insert into TRANSP.EVENTSUBTYPE
   (ID, EVENTSUBTYPENAME, EVENTSUBTYPEDESCRIPTION, EVENTTYPEID, CREATEDBY, DATECREATED, ISACTIVE)
 Values
   ('249', 'AirClic Equipment', 'AirClic Equipment', '45', 'kkanuganti', 
    TO_DATE('01/23/2013 10:37:00', 'MM/DD/YYYY HH24:MI:SS'), 'X');
Insert into TRANSP.EVENTSUBTYPE
   (ID, EVENTSUBTYPENAME, EVENTSUBTYPEDESCRIPTION, EVENTTYPEID, CREATEDBY, DATECREATED, ISACTIVE)
 Values
   ('250', 'Delivery Asst.', 'Delivery Asst.', '45', 'kkanuganti', 
    TO_DATE('01/23/2013 10:37:00', 'MM/DD/YYYY HH24:MI:SS'), 'X');
Insert into TRANSP.EVENTSUBTYPE
   (ID, EVENTSUBTYPENAME, EVENTSUBTYPEDESCRIPTION, EVENTTYPEID, CREATEDBY, DATECREATED)
 Values
   ('251', 'Late Box', 'Late Box', '45', 'kkanuganti', 
    TO_DATE('01/23/2013 10:37:00', 'MM/DD/YYYY HH24:MI:SS'));
Insert into TRANSP.EVENTSUBTYPE
   (ID, EVENTSUBTYPENAME, EVENTSUBTYPEDESCRIPTION, EVENTTYPEID, CREATEDBY, DATECREATED, MSGGROUP_ID, ISACTIVE)
 Values
   ('252', 'Mechanical Issue', 'Mechanical Issue', '45', 'kkanuganti', 
    TO_DATE('01/23/2013 10:37:00', 'MM/DD/YYYY HH24:MI:SS'), 'Trucks', 'X');
Insert into TRANSP.EVENTSUBTYPE
   (ID, EVENTSUBTYPENAME, EVENTSUBTYPEDESCRIPTION, EVENTTYPEID, CREATEDBY, DATECREATED, ISACTIVE)
 Values
   ('253', 'Other', 'Other', '45', 'kkanuganti', 
    TO_DATE('01/23/2013 10:37:00', 'MM/DD/YYYY HH24:MI:SS'), 'X');
Insert into TRANSP.EVENTSUBTYPE
   (ID, EVENTSUBTYPENAME, EVENTSUBTYPEDESCRIPTION, EVENTTYPEID, CREATEDBY, DATECREATED)
 Values
   ('254', 'False/Scanner Issue', 'False/Scanner Issue', '24', 'kkanuganti', 
    TO_DATE('01/23/2013 10:37:00', 'MM/DD/YYYY HH24:MI:SS'));
Insert into TRANSP.EVENTSUBTYPE
   (ID, EVENTSUBTYPENAME, EVENTSUBTYPEDESCRIPTION, EVENTTYPEID, CREATEDBY, DATECREATED)
 Values
   ('255', 'Nxtl- Unable to Hear Driver/Ops', 'Nxtl- Unable to Hear Driver/Ops', '24', 'kkanuganti', 
    TO_DATE('01/23/2013 10:37:00', 'MM/DD/YYYY HH24:MI:SS'));
Insert into TRANSP.EVENTSUBTYPE
   (ID, EVENTSUBTYPENAME, EVENTSUBTYPEDESCRIPTION, EVENTTYPEID, CREATEDBY, DATECREATED)
 Values
   ('256', 'Nxtl- MOT/FireTruck Calling In', 'Nxtl- MOT/FireTruck Calling In', '24', 'kkanuganti', 
    TO_DATE('01/23/2013 10:37:00', 'MM/DD/YYYY HH24:MI:SS'));
Insert into TRANSP.EVENTSUBTYPE
   (ID, EVENTSUBTYPENAME, EVENTSUBTYPEDESCRIPTION, EVENTTYPEID, CREATEDBY, DATECREATED, ISACTIVE)
 Values
   ('257', 'No Issues', 'No Issues', '46', 'kkanuganti', 
    TO_DATE('01/23/2013 10:37:00', 'MM/DD/YYYY HH24:MI:SS'), 'X');
Insert into TRANSP.EVENTSUBTYPE
   (ID, EVENTSUBTYPENAME, EVENTSUBTYPEDESCRIPTION, EVENTTYPEID, CREATEDBY, DATECREATED, MSGGROUP_ID, ISACTIVE)
 Values
   ('258', 'Misload - Duplicate Box', 'Misload - Duplicate Box', '47', 'kkanuganti', 
    TO_DATE('01/23/2013 10:37:00', 'MM/DD/YYYY HH24:MI:SS'), 'Returns Center', 'X');
Insert into TRANSP.EVENTSUBTYPE
   (ID, EVENTSUBTYPENAME, EVENTSUBTYPEDESCRIPTION, EVENTTYPEID, CREATEDBY, DATECREATED, MSGGROUP_ID, ISACTIVE)
 Values
   ('259', 'Misload - Not Reported', 'Misload - Not Reported', '47', 'kkanuganti', 
    TO_DATE('01/23/2013 10:37:00', 'MM/DD/YYYY HH24:MI:SS'), 'Returns Center', 'X');
Insert into TRANSP.EVENTSUBTYPE
   (ID, EVENTSUBTYPENAME, EVENTSUBTYPEDESCRIPTION, EVENTTYPEID, CREATEDBY, DATECREATED, MSGGROUP_ID, ISACTIVE)
 Values
   ('260', 'Fraud Found', 'Fraud Found', '46', 'kkanuganti', 
    TO_DATE('01/23/2013 10:37:00', 'MM/DD/YYYY HH24:MI:SS'), 'CSG Senior Staff', 'X');
Insert into TRANSP.EVENTSUBTYPE
   (ID, EVENTSUBTYPENAME, EVENTSUBTYPEDESCRIPTION, EVENTTYPEID, CREATEDBY, DATECREATED, ISACTIVE)
 Values
   ('261', 'Holiday Meal Replacement', 'Holiday Meal Replacement', '23', 'kkanuganti', 
    TO_DATE('01/23/2013 10:37:00', 'MM/DD/YYYY HH24:MI:SS'), 'X');
Insert into TRANSP.EVENTSUBTYPE
   (ID, EVENTSUBTYPENAME, EVENTSUBTYPEDESCRIPTION, EVENTTYPEID, CREATEDBY, DATECREATED, MSGGROUP_ID, ISACTIVE)
 Values
   ('262', 'Intermec issue', 'phone issues', '11', 'kkanuganti', 
    TO_DATE('01/23/2013 10:37:00', 'MM/DD/YYYY HH24:MI:SS'), 'Transportation', 'X');
Insert into TRANSP.EVENTSUBTYPE
   (ID, EVENTSUBTYPENAME, EVENTSUBTYPEDESCRIPTION, EVENTTYPEID, CREATEDBY, DATECREATED, MSGGROUP_ID, ISACTIVE)
 Values
   ('263', 'Intermec issues', 'Phone issues reported', '48', 'kkanuganti', 
    TO_DATE('01/23/2013 10:37:00', 'MM/DD/YYYY HH24:MI:SS'), 'Transportation', 'X');
Insert into TRANSP.EVENTSUBTYPE
   (ID, EVENTSUBTYPENAME, EVENTSUBTYPEDESCRIPTION, EVENTTYPEID, CREATEDBY, DATECREATED, ISACTIVE)
 Values
   ('264', 'End of Shift Recap', 'End of Shift Recap', '50', 'kkanuganti', 
    TO_DATE('01/23/2013 10:37:00', 'MM/DD/YYYY HH24:MI:SS'), 'X');
Insert into TRANSP.EVENTSUBTYPE
   (ID, EVENTSUBTYPENAME, EVENTSUBTYPEDESCRIPTION, EVENTTYPEID, CREATEDBY, DATECREATED, ISACTIVE)
 Values
   ('265', 'Freeze', 'Freeze', '51', 'kkanuganti', 
    TO_DATE('01/23/2013 10:37:00', 'MM/DD/YYYY HH24:MI:SS'), 'X');
Insert into TRANSP.EVENTSUBTYPE
   (ID, EVENTSUBTYPENAME, EVENTSUBTYPEDESCRIPTION, EVENTTYPEID, CREATEDBY, DATECREATED, ISACTIVE)
 Values
   ('266', 'Battery', 'Battery', '51', 'kkanuganti', 
    TO_DATE('01/23/2013 10:37:00', 'MM/DD/YYYY HH24:MI:SS'), 'X');
Insert into TRANSP.EVENTSUBTYPE
   (ID, EVENTSUBTYPENAME, EVENTSUBTYPEDESCRIPTION, EVENTTYPEID, CREATEDBY, DATECREATED, ISACTIVE)
 Values
   ('267', 'Upload', 'Upload', '51', 'kkanuganti', 
    TO_DATE('01/23/2013 10:37:00', 'MM/DD/YYYY HH24:MI:SS'), 'X');
Insert into TRANSP.EVENTSUBTYPE
   (ID, EVENTSUBTYPENAME, EVENTSUBTYPEDESCRIPTION, EVENTTYPEID, CREATEDBY, DATECREATED, ISACTIVE)
 Values
   ('268', 'Other', 'Other', '51', 'kkanuganti', 
    TO_DATE('01/23/2013 10:37:00', 'MM/DD/YYYY HH24:MI:SS'), 'X');
Insert into TRANSP.EVENTSUBTYPE
   (ID, EVENTSUBTYPENAME, EVENTSUBTYPEDESCRIPTION, EVENTTYPEID, CREATEDBY, DATECREATED, MSGGROUP_ID, ISACTIVE)
 Values
   ('269', 'D Shift Recap', 'End of Shift Recap', '37', 'kkanuganti', 
    TO_DATE('01/23/2013 10:37:00', 'MM/DD/YYYY HH24:MI:SS'), 'Ops Management Alerts', 'X');
Insert into TRANSP.EVENTSUBTYPE
   (ID, EVENTSUBTYPENAME, EVENTSUBTYPEDESCRIPTION, EVENTTYPEID, CREATEDBY, DATECREATED, ISACTIVE)
 Values
   ('270', 'Unfulfilled Request', 'Unfulfilled Request', '26', 'kkanuganti', 
    TO_DATE('01/23/2013 10:37:00', 'MM/DD/YYYY HH24:MI:SS'), 'X');
Insert into TRANSP.EVENTSUBTYPE
   (ID, EVENTSUBTYPENAME, EVENTSUBTYPEDESCRIPTION, EVENTTYPEID, CREATEDBY, DATECREATED, ISACTIVE)
 Values
   ('271', 'Volume', 'Volume', '51', 'kkanuganti', 
    TO_DATE('01/23/2013 10:37:00', 'MM/DD/YYYY HH24:MI:SS'), 'X');
Insert into TRANSP.EVENTSUBTYPE
   (ID, EVENTSUBTYPENAME, EVENTSUBTYPEDESCRIPTION, EVENTTYPEID, CREATEDBY, DATECREATED, ISACTIVE)
 Values
   ('272', 'Ops can hear driver', 'Ops can hear driver', '51', 'kkanuganti', 
    TO_DATE('01/23/2013 10:37:00', 'MM/DD/YYYY HH24:MI:SS'), 'X');
Insert into TRANSP.EVENTSUBTYPE
   (ID, EVENTSUBTYPENAME, EVENTSUBTYPEDESCRIPTION, EVENTTYPEID, CREATEDBY, DATECREATED, ISACTIVE)
 Values
   ('273', 'Driver can hear Ops', 'Driver can hear Ops', '51', 'kkanuganti', 
    TO_DATE('01/23/2013 10:37:00', 'MM/DD/YYYY HH24:MI:SS'), 'X');
Insert into TRANSP.EVENTSUBTYPE
   (ID, EVENTSUBTYPENAME, EVENTSUBTYPEDESCRIPTION, EVENTTYPEID, CREATEDBY, DATECREATED, ISACTIVE)
 Values
   ('274', 'Scanner - Freeze', 'Scanner Issues', '24', 'kkanuganti', 
    TO_DATE('01/23/2013 10:37:00', 'MM/DD/YYYY HH24:MI:SS'), 'X');
Insert into TRANSP.EVENTSUBTYPE
   (ID, EVENTSUBTYPENAME, EVENTSUBTYPEDESCRIPTION, EVENTTYPEID, CREATEDBY, DATECREATED, ISACTIVE)
 Values
   ('275', 'Scanner - Battery Issue', 'Scanner', '24', 'kkanuganti', 
    TO_DATE('01/23/2013 10:37:00', 'MM/DD/YYYY HH24:MI:SS'), 'X');
Insert into TRANSP.EVENTSUBTYPE
   (ID, EVENTSUBTYPENAME, EVENTSUBTYPEDESCRIPTION, EVENTTYPEID, CREATEDBY, DATECREATED, ISACTIVE)
 Values
   ('276', 'Scanner - Delayed Upload', 'Scanner', '24', 'kkanuganti', 
    TO_DATE('01/23/2013 10:37:00', 'MM/DD/YYYY HH24:MI:SS'), 'X');
Insert into TRANSP.EVENTSUBTYPE
   (ID, EVENTSUBTYPENAME, EVENTSUBTYPEDESCRIPTION, EVENTTYPEID, CREATEDBY, DATECREATED, ISACTIVE)
 Values
   ('277', 'Scanner - Volume Issue', 'Scanner', '24', 'kkanuganti', 
    TO_DATE('01/23/2013 10:37:00', 'MM/DD/YYYY HH24:MI:SS'), 'X');
Insert into TRANSP.EVENTSUBTYPE
   (ID, EVENTSUBTYPENAME, EVENTSUBTYPEDESCRIPTION, EVENTTYPEID, CREATEDBY, DATECREATED, ISACTIVE)
 Values
   ('278', 'Scanner - Other', 'Scanner', '24', 'kkanuganti', 
    TO_DATE('01/23/2013 10:37:00', 'MM/DD/YYYY HH24:MI:SS'), 'X');
Insert into TRANSP.EVENTSUBTYPE
   (ID, EVENTSUBTYPENAME, EVENTSUBTYPEDESCRIPTION, EVENTTYPEID, CREATEDBY, DATECREATED, ISACTIVE)
 Values
   ('279', 'Scanner ⿿ Phone Shut-off, Cannot turn on', 'Scanner', '24', 'kkanuganti', 
    TO_DATE('01/23/2013 10:37:00', 'MM/DD/YYYY HH24:MI:SS'), 'X');
Insert into TRANSP.EVENTSUBTYPE
   (ID, EVENTSUBTYPENAME, EVENTSUBTYPEDESCRIPTION, EVENTTYPEID, CREATEDBY, DATECREATED, ISACTIVE)
 Values
   ('280', 'IVR Missing/Found Carton', 'Driver Call In', '24', 'kkanuganti', 
    TO_DATE('01/23/2013 10:37:00', 'MM/DD/YYYY HH24:MI:SS'), 'X');
Insert into TRANSP.EVENTSUBTYPE
   (ID, EVENTSUBTYPENAME, EVENTSUBTYPEDESCRIPTION, EVENTTYPEID, CREATEDBY, DATECREATED, ISACTIVE)
 Values
   ('281', 'IVR Reported Missing/Damaged Carton', 'Driver Call In', '26', 'kkanuganti', 
    TO_DATE('01/23/2013 10:37:00', 'MM/DD/YYYY HH24:MI:SS'), 'X');
Insert into TRANSP.EVENTSUBTYPE
   (ID, EVENTSUBTYPENAME, EVENTSUBTYPEDESCRIPTION, EVENTTYPEID, CREATEDBY, DATECREATED, ISACTIVE)
 Values
   ('282', 'IVR Needs Directions/Address', 'Driver Call In', '24', 'kkanuganti', 
    TO_DATE('01/23/2013 10:37:00', 'MM/DD/YYYY HH24:MI:SS'), 'X');
Insert into TRANSP.EVENTSUBTYPE
   (ID, EVENTSUBTYPENAME, EVENTSUBTYPEDESCRIPTION, EVENTTYPEID, CREATEDBY, DATECREATED, ISACTIVE)
 Values
   ('283', 'IVR Reported forgot to scan', 'Driver Call In', '24', 'kkanuganti', 
    TO_DATE('01/23/2013 10:37:00', 'MM/DD/YYYY HH24:MI:SS'), 'X');
Insert into TRANSP.EVENTSUBTYPE
   (ID, EVENTSUBTYPENAME, EVENTSUBTYPEDESCRIPTION, EVENTTYPEID, CREATEDBY, DATECREATED, ISACTIVE)
 Values
   ('284', 'IVR Customer Call Requested', 'Driver Calls In', '24', 'kkanuganti', 
    TO_DATE('01/23/2013 10:37:00', 'MM/DD/YYYY HH24:MI:SS'), 'X');
Insert into TRANSP.EVENTSUBTYPE
   (ID, EVENTSUBTYPENAME, EVENTSUBTYPEDESCRIPTION, EVENTTYPEID, CREATEDBY, DATECREATED, ISACTIVE)
 Values
   ('285', 'IVR Report Lateness', 'Driver Calls In', '24', 'kkanuganti', 
    TO_DATE('01/23/2013 10:37:00', 'MM/DD/YYYY HH24:MI:SS'), 'X');
Insert into TRANSP.EVENTSUBTYPE
   (ID, EVENTSUBTYPENAME, EVENTSUBTYPEDESCRIPTION, EVENTTYPEID, CREATEDBY, DATECREATED, ISACTIVE)
 Values
   ('286', 'IVR - Returning Ops Call', 'Driver Call In', '24', 'kkanuganti', 
    TO_DATE('01/23/2013 10:37:00', 'MM/DD/YYYY HH24:MI:SS'), 'X');
Insert into TRANSP.EVENTSUBTYPE
   (ID, EVENTSUBTYPENAME, EVENTSUBTYPEDESCRIPTION, EVENTTYPEID, CREATEDBY, DATECREATED, ISACTIVE)
 Values
   ('287', 'IVR - Other', 'Driver called in', '24', 'kkanuganti', 
    TO_DATE('01/23/2013 10:37:00', 'MM/DD/YYYY HH24:MI:SS'), 'X');
Insert into TRANSP.EVENTSUBTYPE
   (ID, EVENTSUBTYPENAME, EVENTSUBTYPEDESCRIPTION, EVENTTYPEID, CREATEDBY, DATECREATED, ISACTIVE)
 Values
   ('288', 'IVR Reported Missing/Damaged Box', 'Driver Called In', '24', 'kkanuganti', 
    TO_DATE('01/23/2013 10:37:00', 'MM/DD/YYYY HH24:MI:SS'), 'X');
Insert into TRANSP.EVENTSUBTYPE
   (ID, EVENTSUBTYPENAME, EVENTSUBTYPEDESCRIPTION, EVENTTYPEID, CREATEDBY, DATECREATED, MSGGROUP_ID, ISACTIVE)
 Values
   ('289', 'Reported Issues', 'Reported Issues', '50', 'kkanuganti', 
    TO_DATE('01/23/2013 10:37:00', 'MM/DD/YYYY HH24:MI:SS'), 'Ops Management', 'X');
Insert into TRANSP.EVENTSUBTYPE
   (ID, EVENTSUBTYPENAME, EVENTSUBTYPEDESCRIPTION, EVENTTYPEID, CREATEDBY, DATECREATED, ISACTIVE)
 Values
   ('214', 'Needs Partners Number', 'Needs Partners Number', '44', 'kkanuganti', 
    TO_DATE('01/23/2013 10:37:00', 'MM/DD/YYYY HH24:MI:SS'), 'X');
Insert into TRANSP.EVENTSUBTYPE
   (ID, EVENTSUBTYPENAME, EVENTSUBTYPEDESCRIPTION, EVENTTYPEID, CREATEDBY, DATECREATED)
 Values
   ('226', 'Nxtl- Needs Partners Number', 'Nxtl- Needs Partners Number', '24', 'kkanuganti', 
    TO_DATE('01/23/2013 10:37:00', 'MM/DD/YYYY HH24:MI:SS'));
Insert into TRANSP.EVENTSUBTYPE
   (ID, EVENTSUBTYPENAME, EVENTSUBTYPEDESCRIPTION, EVENTTYPEID, CREATEDBY, DATECREATED, MSGGROUP_ID, ISACTIVE)
 Values
   ('72', 'Shelving Issue', 'Shelves need to be replaced', '9', 'kkanuganti', 
    TO_DATE('01/23/2013 10:37:00', 'MM/DD/YYYY HH24:MI:SS'), 'Returns Center', 'X');
Insert into TRANSP.EVENTSUBTYPE
   (ID, EVENTSUBTYPENAME, EVENTSUBTYPEDESCRIPTION, EVENTTYPEID, CREATEDBY, DATECREATED, MSGGROUP_ID, ISACTIVE)
 Values
   ('101', 'Skin', 'Skin', '9', 'kkanuganti', 
    TO_DATE('01/23/2013 10:37:00', 'MM/DD/YYYY HH24:MI:SS'), 'Returns Center', 'X');
Insert into TRANSP.EVENTSUBTYPE
   (ID, EVENTSUBTYPENAME, EVENTSUBTYPEDESCRIPTION, EVENTTYPEID, CREATEDBY, DATECREATED, MSGGROUP_ID)
 Values
   ('159', 'Other', 'Other', '35', 'kkanuganti', 
    TO_DATE('01/23/2013 10:37:00', 'MM/DD/YYYY HH24:MI:SS'), 'Ops Management');
Insert into TRANSP.EVENTSUBTYPE
   (ID, EVENTSUBTYPENAME, EVENTSUBTYPEDESCRIPTION, EVENTTYPEID, CREATEDBY, DATECREATED, MSGGROUP_ID, ISACTIVE)
 Values
   ('147', 'On Premise 12AM - 5AM', 'On Premise 12AM - 5AM', '33', 'kkanuganti', 
    TO_DATE('01/23/2013 10:37:00', 'MM/DD/YYYY HH24:MI:SS'), 'Transportation Shipping Yard', 'X');
Insert into TRANSP.EVENTSUBTYPE
   (ID, EVENTSUBTYPENAME, EVENTSUBTYPEDESCRIPTION, EVENTTYPEID, CREATEDBY, DATECREATED, MSGGROUP_ID, ISACTIVE)
 Values
   ('194', 'Independent Driver Decision', 'Driver was advised to go through the regenera', '41', 'kkanuganti', 
    TO_DATE('01/23/2013 10:37:00', 'MM/DD/YYYY HH24:MI:SS'), 'Transportation Shipping Yard', 'X');
Insert into TRANSP.EVENTSUBTYPE
   (ID, EVENTSUBTYPENAME, EVENTSUBTYPEDESCRIPTION, EVENTTYPEID, CREATEDBY, DATECREATED, MSGGROUP_ID, ISACTIVE)
 Values
   ('168', 'B At Dispatch', 'B   At Dispatch', '37', 'kkanuganti', 
    TO_DATE('01/23/2013 10:37:00', 'MM/DD/YYYY HH24:MI:SS'), 'Ops Management Alerts', 'X');
Insert into TRANSP.EVENTSUBTYPE
   (ID, EVENTSUBTYPENAME, EVENTSUBTYPEDESCRIPTION, EVENTTYPEID, CREATEDBY, DATECREATED, MSGGROUP_ID, ISACTIVE)
 Values
   ('191', 'Other', 'Any reason that does not fall within any of t', '40', 'kkanuganti', 
    TO_DATE('01/23/2013 10:37:00', 'MM/DD/YYYY HH24:MI:SS'), 'Ops Management Alerts', 'X');
Insert into TRANSP.EVENTSUBTYPE
   (ID, EVENTSUBTYPENAME, EVENTSUBTYPEDESCRIPTION, EVENTTYPEID, CREATEDBY, DATECREATED, MSGGROUP_ID, ISACTIVE)
 Values
   ('206', 'Truck Unassigned/Not Dispatched', 'Truck number has not been assigned to a Route', '40', 'kkanuganti', 
    TO_DATE('01/23/2013 10:37:00', 'MM/DD/YYYY HH24:MI:SS'), 'Transportation Admins', 'X');
    
    

    
Insert into TRANSP.AUTHUSERS  (USERNAME, REALNAME, DATECREATED) Values  ('joakley', 'Jermaine Oakley', TO_DATE('01/31/2013 11:29:41', 'MM/DD/YYYY HH24:MI:SS'));
Insert into TRANSP.AUTHUSERS  (USERNAME, REALNAME, DATECREATED) Values  ('qaadmin', 'qaadmin', TO_DATE('02/01/2013 14:59:30', 'MM/DD/YYYY HH24:MI:SS'));
Insert into TRANSP.AUTHUSERS  (USERNAME, REALNAME, DATECREATED)  Values   ('cchristophorou', 'Christopher Christophorou', TO_DATE('01/31/2013 11:28:48', 'MM/DD/YYYY HH24:MI:SS'));
Insert into TRANSP.AUTHUSERS  (USERNAME, REALNAME, EMPLOYEEID, DATECREATED) Values  ('ndixit', 'Nimish Dixit', '602499', TO_DATE('01/31/2013 11:28:48', 'MM/DD/YYYY HH24:MI:SS'));
Insert into TRANSP.AUTHUSERS  (USERNAME, REALNAME, EMPLOYEEID, DATECREATED) Values  ('awilliams', 'Adrian Williams', '500654', TO_DATE('01/31/2013 11:28:48', 'MM/DD/YYYY HH24:MI:SS'));
Insert into TRANSP.AUTHUSERS  (USERNAME, REALNAME, EMPLOYEEID, DATECREATED)
 Values
   ('anino', 'Albert Nino', '601130', TO_DATE('01/31/2013 11:28:48', 'MM/DD/YYYY HH24:MI:SS'));
Insert into TRANSP.AUTHUSERS
   (USERNAME, REALNAME, EMPLOYEEID, DATECREATED)
 Values
   ('ahassan', 'Ali Hassan', '601881', TO_DATE('01/31/2013 11:28:48', 'MM/DD/YYYY HH24:MI:SS'));
Insert into TRANSP.AUTHUSERS
   (USERNAME, REALNAME, EMPLOYEEID, DATECREATED)
 Values
   ('barbiter', 'Brandon Arbiter', '600732', TO_DATE('01/31/2013 11:28:48', 'MM/DD/YYYY HH24:MI:SS'));
Insert into TRANSP.AUTHUSERS
   (USERNAME, REALNAME, EMPLOYEEID, DATECREATED)
 Values
   ('dalves', 'Daniel Alves', '501028', TO_DATE('01/31/2013 11:28:48', 'MM/DD/YYYY HH24:MI:SS'));
Insert into TRANSP.AUTHUSERS
   (USERNAME, REALNAME, EMPLOYEEID, DATECREATED)
 Values
   ('dvazquez', 'Derek Vazquez', '500948', TO_DATE('01/31/2013 11:28:48', 'MM/DD/YYYY HH24:MI:SS'));
Insert into TRANSP.AUTHUSERS
   (USERNAME, REALNAME, EMPLOYEEID, DATECREATED)
 Values
   ('gwalker', 'George Walker', '500009', TO_DATE('01/31/2013 11:28:49', 'MM/DD/YYYY HH24:MI:SS'));
Insert into TRANSP.AUTHUSERS
   (USERNAME, REALNAME, EMPLOYEEID, DATECREATED)
 Values
   ('glloyd', 'Glenn Lloyd', '601351', TO_DATE('01/31/2013 11:28:49', 'MM/DD/YYYY HH24:MI:SS'));
Insert into TRANSP.AUTHUSERS
   (USERNAME, REALNAME, EMPLOYEEID, DATECREATED)
 Values
   ('jnarvasa', 'Jay Narvasa', '601688', TO_DATE('01/31/2013 11:28:49', 'MM/DD/YYYY HH24:MI:SS'));
Insert into TRANSP.AUTHUSERS
   (USERNAME, REALNAME, EMPLOYEEID, DATECREATED)
 Values
   ('jsantiago', 'Jeff Santiago', '500802', TO_DATE('01/31/2013 11:28:49', 'MM/DD/YYYY HH24:MI:SS'));
Insert into TRANSP.AUTHUSERS
   (USERNAME, REALNAME, EMPLOYEEID, DATECREATED)
 Values
   ('jmohammed', 'Joel Mohammed', '601869', TO_DATE('01/31/2013 11:28:49', 'MM/DD/YYYY HH24:MI:SS'));
Insert into TRANSP.AUTHUSERS
   (USERNAME, REALNAME, EMPLOYEEID, DATECREATED)
 Values
   ('jmatos', 'Juan Matos', '500167', TO_DATE('01/31/2013 11:28:49', 'MM/DD/YYYY HH24:MI:SS'));
Insert into TRANSP.AUTHUSERS
   (USERNAME, REALNAME, EMPLOYEEID, DATECREATED)
 Values
   ('kgarcia', 'Kristine Garcia', '601444', TO_DATE('01/31/2013 11:28:49', 'MM/DD/YYYY HH24:MI:SS'));
Insert into TRANSP.AUTHUSERS
   (USERNAME, REALNAME, EMPLOYEEID, DATECREATED)
 Values
   ('lschmerz', 'Lyle Schmerz', '501050', TO_DATE('01/31/2013 11:28:49', 'MM/DD/YYYY HH24:MI:SS'));
Insert into TRANSP.AUTHUSERS
   (USERNAME, REALNAME, EMPLOYEEID, DATECREATED)
 Values
   ('mdiarrassouba', 'Madje Diarrassouba', '500274', TO_DATE('01/31/2013 11:28:50', 'MM/DD/YYYY HH24:MI:SS'));
Insert into TRANSP.AUTHUSERS
   (USERNAME, REALNAME, EMPLOYEEID, DATECREATED)
 Values
   ('pgualdo', 'Pablo Gualdo', '500012', TO_DATE('01/31/2013 11:28:50', 'MM/DD/YYYY HH24:MI:SS'));
Insert into TRANSP.AUTHUSERS
   (USERNAME, REALNAME, EMPLOYEEID, DATECREATED)
 Values
   ('rstein', 'Ron Stein', '602489', TO_DATE('01/31/2013 11:28:50', 'MM/DD/YYYY HH24:MI:SS'));
Insert into TRANSP.AUTHUSERS
   (USERNAME, REALNAME, EMPLOYEEID, DATECREATED)
 Values
   ('sboccia', 'Scott Boccia', '600995', TO_DATE('01/31/2013 11:28:50', 'MM/DD/YYYY HH24:MI:SS'));
Insert into TRANSP.AUTHUSERS
   (USERNAME, REALNAME, EMPLOYEEID, DATECREATED)
 Values
   ('sphillips', 'Sean Phillips', '501062', TO_DATE('01/31/2013 11:28:50', 'MM/DD/YYYY HH24:MI:SS'));
Insert into TRANSP.AUTHUSERS
   (USERNAME, REALNAME, EMPLOYEEID, DATECREATED)
 Values
   ('swortman', 'Steve Wortman', '501083', TO_DATE('01/31/2013 11:28:50', 'MM/DD/YYYY HH24:MI:SS'));
Insert into TRANSP.AUTHUSERS
   (USERNAME, REALNAME, EMPLOYEEID, DATECREATED)
 Values
   ('tdonovan', 'Terry Donovan', '600793', TO_DATE('01/31/2013 11:28:50', 'MM/DD/YYYY HH24:MI:SS'));
Insert into TRANSP.AUTHUSERS
   (USERNAME, REALNAME, EMPLOYEEID, DATECREATED)
 Values
   ('table', 'Tessa Able', '500713', TO_DATE('01/31/2013 11:28:50', 'MM/DD/YYYY HH24:MI:SS'));
Insert into TRANSP.AUTHUSERS
   (USERNAME, REALNAME, EMPLOYEEID, DATECREATED)
 Values
   ('tbaltimore', 'Tracy Baltimore', '600192', TO_DATE('01/31/2013 11:28:51', 'MM/DD/YYYY HH24:MI:SS'));
Insert into TRANSP.AUTHUSERS
   (USERNAME, REALNAME, EMPLOYEEID, DATECREATED)
 Values
   ('awint', 'Ade Wint', '603260', TO_DATE('01/31/2013 11:28:51', 'MM/DD/YYYY HH24:MI:SS'));
Insert into TRANSP.AUTHUSERS
   (USERNAME, REALNAME, EMPLOYEEID, DATECREATED)
 Values
   ('arivera', 'Ali Rivera', '601959', TO_DATE('01/31/2013 11:28:51', 'MM/DD/YYYY HH24:MI:SS'));
Insert into TRANSP.AUTHUSERS
   (USERNAME, REALNAME, EMPLOYEEID, DATECREATED)
 Values
   ('aacevedo', 'Amanda Acevedo', '600156', TO_DATE('01/31/2013 11:28:51', 'MM/DD/YYYY HH24:MI:SS'));
Insert into TRANSP.AUTHUSERS
   (USERNAME, REALNAME, EMPLOYEEID, DATECREATED)
 Values
   ('chodge', 'Christine Hodge', '104469', TO_DATE('01/31/2013 11:28:51', 'MM/DD/YYYY HH24:MI:SS'));
Insert into TRANSP.AUTHUSERS
   (USERNAME, REALNAME, EMPLOYEEID, DATECREATED)
 Values
   ('cwingate', 'Christopher Wingate', '5268', TO_DATE('01/31/2013 11:28:51', 'MM/DD/YYYY HH24:MI:SS'));
Insert into TRANSP.AUTHUSERS
   (USERNAME, REALNAME, EMPLOYEEID, DATECREATED)
 Values
   ('fhalim', 'Fatima Halim', '103552', TO_DATE('01/31/2013 11:28:51', 'MM/DD/YYYY HH24:MI:SS'));
Insert into TRANSP.AUTHUSERS
   (USERNAME, REALNAME, EMPLOYEEID, DATECREATED)
 Values
   ('helenarivera', 'Helena Rivera', '602535', TO_DATE('01/31/2013 11:28:51', 'MM/DD/YYYY HH24:MI:SS'));
Insert into TRANSP.AUTHUSERS
   (USERNAME, REALNAME, EMPLOYEEID, DATECREATED)
 Values
   ('jroude', 'Jessica Roude', '102844', TO_DATE('01/31/2013 11:28:52', 'MM/DD/YYYY HH24:MI:SS'));
Insert into TRANSP.AUTHUSERS
   (USERNAME, REALNAME, EMPLOYEEID, DATECREATED)
 Values
   ('jmonserrate', 'Julian Monserrate', '100260', TO_DATE('01/31/2013 11:28:52', 'MM/DD/YYYY HH24:MI:SS'));
Insert into TRANSP.AUTHUSERS
   (USERNAME, REALNAME, EMPLOYEEID, DATECREATED)
 Values
   ('kwjames', 'Kenwayne James', '101309', TO_DATE('01/31/2013 11:28:52', 'MM/DD/YYYY HH24:MI:SS'));
Insert into TRANSP.AUTHUSERS
   (USERNAME, REALNAME, EMPLOYEEID, DATECREATED)
 Values
   ('kslade', 'Khalilah Slade', '5541', TO_DATE('01/31/2013 11:28:52', 'MM/DD/YYYY HH24:MI:SS'));
Insert into TRANSP.AUTHUSERS
   (USERNAME, REALNAME, DATECREATED)
 Values
   ('lcraft', 'Lacey Craft', TO_DATE('01/31/2013 11:28:52', 'MM/DD/YYYY HH24:MI:SS'));
Insert into TRANSP.AUTHUSERS
   (USERNAME, REALNAME, EMPLOYEEID, DATECREATED)
 Values
   ('mcanas', 'Manny Canas', '100345', TO_DATE('01/31/2013 11:28:52', 'MM/DD/YYYY HH24:MI:SS'));
Insert into TRANSP.AUTHUSERS
   (USERNAME, REALNAME, EMPLOYEEID, DATECREATED)
 Values
   ('mwan', 'Michael Wan', '104853', TO_DATE('01/31/2013 11:28:52', 'MM/DD/YYYY HH24:MI:SS'));
Insert into TRANSP.AUTHUSERS
   (USERNAME, REALNAME, EMPLOYEEID, DATECREATED)
 Values
   ('rscharf', 'Rachel Scharf', '102704', TO_DATE('01/31/2013 11:28:53', 'MM/DD/YYYY HH24:MI:SS'));
Insert into TRANSP.AUTHUSERS
   (USERNAME, REALNAME, EMPLOYEEID, DATECREATED)
 Values
   ('thaywood', 'Tamika Haywood', '600890', TO_DATE('01/31/2013 11:28:53', 'MM/DD/YYYY HH24:MI:SS'));
Insert into TRANSP.AUTHUSERS
   (USERNAME, REALNAME, EMPLOYEEID, DATECREATED)
 Values
   ('thill', 'Tracy Hill', '101494', TO_DATE('01/31/2013 11:28:53', 'MM/DD/YYYY HH24:MI:SS'));
Insert into TRANSP.AUTHUSERS
   (USERNAME, REALNAME, EMPLOYEEID, DATECREATED)
 Values
   ('valva', 'Veronica Alva', '102195', TO_DATE('01/31/2013 11:28:53', 'MM/DD/YYYY HH24:MI:SS'));
Insert into TRANSP.AUTHUSERS
   (USERNAME, REALNAME, EMPLOYEEID, DATECREATED)
 Values
   ('horenstein', 'Harry Orenstein', '600804', TO_DATE('01/31/2013 11:28:53', 'MM/DD/YYYY HH24:MI:SS'));
Insert into TRANSP.AUTHUSERS
   (USERNAME, REALNAME, EMPLOYEEID, DATECREATED)
 Values
   ('sali', 'Sultan Ali', '600894', TO_DATE('01/31/2013 11:28:53', 'MM/DD/YYYY HH24:MI:SS'));
Insert into TRANSP.AUTHUSERS
   (USERNAME, REALNAME, EMPLOYEEID, DATECREATED)
 Values
   ('skhatana', 'Saher Khatana', '603258', TO_DATE('01/31/2013 11:28:53', 'MM/DD/YYYY HH24:MI:SS'));
Insert into TRANSP.AUTHUSERS
   (USERNAME, REALNAME, EMPLOYEEID, DATECREATED)
 Values
   ('rahmed', 'Rahat Ahmed', '602506', TO_DATE('01/31/2013 11:28:53', 'MM/DD/YYYY HH24:MI:SS'));
Insert into TRANSP.AUTHUSERS
   (USERNAME, REALNAME, EMPLOYEEID, DATECREATED)
 Values
   ('fmansour', 'Frank Mansour', '104090', TO_DATE('01/31/2013 11:28:53', 'MM/DD/YYYY HH24:MI:SS'));
Insert into TRANSP.AUTHUSERS
   (USERNAME, REALNAME, EMPLOYEEID, DATECREATED)
 Values
   ('krobinson', 'Kurt Robinson', '500234', TO_DATE('01/31/2013 11:28:54', 'MM/DD/YYYY HH24:MI:SS'));
Insert into TRANSP.AUTHUSERS
   (USERNAME, REALNAME, EMPLOYEEID, DATECREATED)
 Values
   ('rdaniels', 'Ryan Daniels', '601340', TO_DATE('01/31/2013 11:28:54', 'MM/DD/YYYY HH24:MI:SS'));
Insert into TRANSP.AUTHUSERS
   (USERNAME, REALNAME, EMPLOYEEID, DATECREATED)
 Values
   ('tlazaro', 'Tom Lazaro', '602540', TO_DATE('01/31/2013 11:28:54', 'MM/DD/YYYY HH24:MI:SS'));
Insert into TRANSP.AUTHUSERS
   (USERNAME, REALNAME, EMPLOYEEID, DATECREATED)
 Values
   ('sfrancois', 'Sheldon Francois', '600660', TO_DATE('01/31/2013 11:28:54', 'MM/DD/YYYY HH24:MI:SS'));
Insert into TRANSP.AUTHUSERS
   (USERNAME, REALNAME, DATECREATED)
 Values
   ('dboothe', 'Derisha Boothe', TO_DATE('01/31/2013 11:28:54', 'MM/DD/YYYY HH24:MI:SS'));
Insert into TRANSP.AUTHUSERS
   (USERNAME, REALNAME, EMPLOYEEID, DATECREATED)
 Values
   ('dguttridge', 'David Guttridge', '602595', TO_DATE('01/31/2013 11:28:54', 'MM/DD/YYYY HH24:MI:SS'));
Insert into TRANSP.AUTHUSERS
   (USERNAME, REALNAME, EMPLOYEEID, DATECREATED)
 Values
   ('adavidson', 'Anita Davidson', '602900', TO_DATE('01/31/2013 11:28:54', 'MM/DD/YYYY HH24:MI:SS'));
Insert into TRANSP.AUTHUSERS
   (USERNAME, REALNAME, DATECREATED)
 Values
   ('ysantos', 'Yasivick Santos', TO_DATE('01/31/2013 11:28:54', 'MM/DD/YYYY HH24:MI:SS'));
Insert into TRANSP.AUTHUSERS
   (USERNAME, REALNAME, EMPLOYEEID, DATECREATED)
 Values
   ('jyeh', 'Jason Yeh', '602718', TO_DATE('01/31/2013 11:28:55', 'MM/DD/YYYY HH24:MI:SS'));
Insert into TRANSP.AUTHUSERS
   (USERNAME, REALNAME, EMPLOYEEID, DATECREATED)
 Values
   ('yhan', 'Ying Han', '104433', TO_DATE('01/31/2013 11:28:55', 'MM/DD/YYYY HH24:MI:SS'));
Insert into TRANSP.AUTHUSERS
   (USERNAME, REALNAME, EMPLOYEEID, DATECREATED)
 Values
   ('abrowne', 'Adrienne Browne', '602865', TO_DATE('01/31/2013 11:28:55', 'MM/DD/YYYY HH24:MI:SS'));
Insert into TRANSP.AUTHUSERS
   (USERNAME, REALNAME, EMPLOYEEID, DATECREATED)
 Values
   ('ltorres', 'Lizette Torres', '602714', TO_DATE('01/31/2013 11:28:55', 'MM/DD/YYYY HH24:MI:SS'));
Insert into TRANSP.AUTHUSERS
   (USERNAME, REALNAME, EMPLOYEEID, DATECREATED)
 Values
   ('tdaniel', 'Teron Daniel', '602907', TO_DATE('01/31/2013 11:28:55', 'MM/DD/YYYY HH24:MI:SS'));
Insert into TRANSP.AUTHUSERS
   (USERNAME, REALNAME, EMPLOYEEID, DATECREATED)
 Values
   ('rbarnes', 'Ricardo Barnes', '601182', TO_DATE('01/31/2013 11:28:55', 'MM/DD/YYYY HH24:MI:SS'));
Insert into TRANSP.AUTHUSERS
   (USERNAME, REALNAME, EMPLOYEEID, DATECREATED)
 Values
   ('gpuzo', 'Gerard Puzo', '500958', TO_DATE('01/31/2013 11:28:55', 'MM/DD/YYYY HH24:MI:SS'));
Insert into TRANSP.AUTHUSERS
   (USERNAME, REALNAME, EMPLOYEEID, DATECREATED)
 Values
   ('ejaramillo', 'Erwin Jaramillo', '602708', TO_DATE('01/31/2013 11:28:55', 'MM/DD/YYYY HH24:MI:SS'));
Insert into TRANSP.AUTHUSERS
   (USERNAME, REALNAME, EMPLOYEEID, DATECREATED)
 Values
   ('scliff', 'Solonge Cliff', '500462', TO_DATE('01/31/2013 11:28:56', 'MM/DD/YYYY HH24:MI:SS'));
Insert into TRANSP.AUTHUSERS
   (USERNAME, REALNAME, EMPLOYEEID, DATECREATED)
 Values
   ('tmartin', 'Taisha Martin', '601951', TO_DATE('01/31/2013 11:28:56', 'MM/DD/YYYY HH24:MI:SS'));
Insert into TRANSP.AUTHUSERS
   (USERNAME, REALNAME, EMPLOYEEID, DATECREATED)
 Values
   ('gswaby', 'Garnett Swaby', '601974', TO_DATE('01/31/2013 11:28:56', 'MM/DD/YYYY HH24:MI:SS'));
Insert into TRANSP.AUTHUSERS
   (USERNAME, REALNAME, EMPLOYEEID, DATECREATED)
 Values
   ('sgraham', 'Sharrod Graham', '500252', TO_DATE('01/31/2013 11:28:56', 'MM/DD/YYYY HH24:MI:SS'));
Insert into TRANSP.AUTHUSERS
   (USERNAME, REALNAME, EMPLOYEEID, DATECREATED)
 Values
   ('bthomas', 'Brent Thomas', '602713', TO_DATE('01/31/2013 11:28:56', 'MM/DD/YYYY HH24:MI:SS'));
Insert into TRANSP.AUTHUSERS
   (USERNAME, REALNAME, EMPLOYEEID, DATECREATED)
 Values
   ('dswatson', 'Dameion Watson', '601828', TO_DATE('01/31/2013 11:28:56', 'MM/DD/YYYY HH24:MI:SS'));
Insert into TRANSP.AUTHUSERS
   (USERNAME, REALNAME, EMPLOYEEID, DATECREATED)
 Values
   ('rwashington', 'Rashamel Washington', '603259', TO_DATE('01/31/2013 11:28:56', 'MM/DD/YYYY HH24:MI:SS'));
Insert into TRANSP.AUTHUSERS
   (USERNAME, REALNAME, EMPLOYEEID, DATECREATED)
 Values
   ('landreoli', 'Leo Andreoli', '101010', TO_DATE('01/31/2013 11:28:56', 'MM/DD/YYYY HH24:MI:SS'));
Insert into TRANSP.AUTHUSERS
   (USERNAME, REALNAME, EMPLOYEEID, DATECREATED)
 Values
   ('vmalave', 'Vicente Malave', '100150', TO_DATE('01/31/2013 11:28:57', 'MM/DD/YYYY HH24:MI:SS'));
Insert into TRANSP.AUTHUSERS
   (USERNAME, REALNAME, EMPLOYEEID, DATECREATED)
 Values
   ('dweinstein', 'David Weinstein', '103584', TO_DATE('01/31/2013 11:28:57', 'MM/DD/YYYY HH24:MI:SS'));
Insert into TRANSP.AUTHUSERS
   (USERNAME, REALNAME, EMPLOYEEID, DATECREATED)
 Values
   ('kmadry', 'Kaz Madry', '600806', TO_DATE('01/31/2013 11:28:57', 'MM/DD/YYYY HH24:MI:SS'));
Insert into TRANSP.AUTHUSERS
   (USERNAME, REALNAME, EMPLOYEEID, DATECREATED)
 Values
   ('jackerman', 'Jason Ackerman', '100178', TO_DATE('01/31/2013 11:28:57', 'MM/DD/YYYY HH24:MI:SS'));
Insert into TRANSP.AUTHUSERS
   (USERNAME, REALNAME, EMPLOYEEID, DATECREATED)
 Values
   ('marbitman', 'Mike Arbitman', '601754', TO_DATE('01/31/2013 11:28:57', 'MM/DD/YYYY HH24:MI:SS'));
Insert into TRANSP.AUTHUSERS
   (USERNAME, REALNAME, EMPLOYEEID, DATECREATED)
 Values
   ('apeters', 'Andrea Peters', '603026', TO_DATE('01/31/2013 11:28:57', 'MM/DD/YYYY HH24:MI:SS'));
Insert into TRANSP.AUTHUSERS
   (USERNAME, REALNAME, EMPLOYEEID, DATECREATED)
 Values
   ('kbmaxwell', 'Kamisha Barrett-Maxwell', '603107', TO_DATE('01/31/2013 11:28:57', 'MM/DD/YYYY HH24:MI:SS'));
Insert into TRANSP.AUTHUSERS
   (USERNAME, REALNAME, EMPLOYEEID, DATECREATED)
 Values
   ('jrodriguez', 'Juan Rodriguez', '500260', TO_DATE('01/31/2013 11:28:57', 'MM/DD/YYYY HH24:MI:SS'));
Insert into TRANSP.AUTHUSERS
   (USERNAME, REALNAME, EMPLOYEEID, DATECREATED)
 Values
   ('jheskey', 'James Heskey', '600652', TO_DATE('01/31/2013 11:28:58', 'MM/DD/YYYY HH24:MI:SS'));
Insert into TRANSP.AUTHUSERS
   (USERNAME, REALNAME, EMPLOYEEID, DATECREATED)
 Values
   ('ainniss', 'Andrea Inniss', '105274', TO_DATE('01/31/2013 11:28:58', 'MM/DD/YYYY HH24:MI:SS'));
Insert into TRANSP.AUTHUSERS
   (USERNAME, REALNAME, EMPLOYEEID, DATECREATED)
 Values
   ('eatature', 'Engin Atature', '601352', TO_DATE('01/31/2013 11:28:58', 'MM/DD/YYYY HH24:MI:SS'));
Insert into TRANSP.AUTHUSERS
   (USERNAME, REALNAME, EMPLOYEEID, DATECREATED)
 Values
   ('rmakarian', 'Raisa Makarian', '103141', TO_DATE('01/31/2013 11:28:58', 'MM/DD/YYYY HH24:MI:SS'));
Insert into TRANSP.AUTHUSERS
   (USERNAME, REALNAME, EMPLOYEEID, DATECREATED)
 Values
   ('kconnaughton', 'Karen Connaughton', '103867', TO_DATE('01/31/2013 11:28:58', 'MM/DD/YYYY HH24:MI:SS'));
Insert into TRANSP.AUTHUSERS
   (USERNAME, REALNAME, EMPLOYEEID, DATECREATED)
 Values
   ('jwest', 'Jacqulench West', '603153', TO_DATE('01/31/2013 11:28:58', 'MM/DD/YYYY HH24:MI:SS'));
Insert into TRANSP.AUTHUSERS
   (USERNAME, REALNAME, EMPLOYEEID, DATECREATED)
 Values
   ('mlefcovich', 'Mark Lefcovich', '603154', TO_DATE('01/31/2013 11:28:58', 'MM/DD/YYYY HH24:MI:SS'));
Insert into TRANSP.AUTHUSERS
   (USERNAME, REALNAME, EMPLOYEEID, DATECREATED)
 Values
   ('rmathey', 'Roderic Mathey', '105107', TO_DATE('01/31/2013 11:28:58', 'MM/DD/YYYY HH24:MI:SS'));
Insert into TRANSP.AUTHUSERS
   (USERNAME, REALNAME, EMPLOYEEID, DATECREATED)
 Values
   ('ltoral', 'Luis Toral', '601278', TO_DATE('01/31/2013 11:28:59', 'MM/DD/YYYY HH24:MI:SS'));
Insert into TRANSP.AUTHUSERS
   (USERNAME, REALNAME, EMPLOYEEID, DATECREATED)
 Values
   ('jalaimo', 'Jack Alaimo', '603479', TO_DATE('01/31/2013 11:28:59', 'MM/DD/YYYY HH24:MI:SS'));
Insert into TRANSP.AUTHUSERS
   (USERNAME, REALNAME, EMPLOYEEID, DATECREATED)
 Values
   ('cclarke', 'Conroy Clarke', '603477', TO_DATE('01/31/2013 11:28:59', 'MM/DD/YYYY HH24:MI:SS'));
Insert into TRANSP.AUTHUSERS
   (USERNAME, REALNAME, EMPLOYEEID, DATECREATED)
 Values
   ('dwald', 'Daniel Wald', '602619', TO_DATE('01/31/2013 11:28:59', 'MM/DD/YYYY HH24:MI:SS'));
Insert into TRANSP.AUTHUSERS
   (USERNAME, REALNAME, EMPLOYEEID, DATECREATED)
 Values
   ('ibellamy', 'Iman Bellamy', '604099', TO_DATE('01/31/2013 11:28:59', 'MM/DD/YYYY HH24:MI:SS'));
Insert into TRANSP.AUTHUSERS
   (USERNAME, REALNAME, EMPLOYEEID, DATECREATED)
 Values
   ('lstewart', 'Luvasia Stewart', '604100', TO_DATE('01/31/2013 11:28:59', 'MM/DD/YYYY HH24:MI:SS'));
Insert into TRANSP.AUTHUSERS
   (USERNAME, REALNAME, EMPLOYEEID, DATECREATED)
 Values
   ('mstauffer', 'Matthew Stauffer', '602545', TO_DATE('01/31/2013 11:28:59', 'MM/DD/YYYY HH24:MI:SS'));
Insert into TRANSP.AUTHUSERS
   (USERNAME, REALNAME, EMPLOYEEID, DATECREATED)
 Values
   ('vstinson', 'Vera Stinson', '604196', TO_DATE('01/31/2013 11:28:59', 'MM/DD/YYYY HH24:MI:SS'));
Insert into TRANSP.AUTHUSERS
   (USERNAME, REALNAME, EMPLOYEEID, DATECREATED)
 Values
   ('sfanfan', 'Steve Fanfan', '604197', TO_DATE('01/31/2013 11:28:59', 'MM/DD/YYYY HH24:MI:SS'));
Insert into TRANSP.AUTHUSERS
   (USERNAME, REALNAME, EMPLOYEEID, DATECREATED)
 Values
   ('krattan', 'Kendra Rattan', '604198', TO_DATE('01/31/2013 11:29:00', 'MM/DD/YYYY HH24:MI:SS'));
Insert into TRANSP.AUTHUSERS
   (USERNAME, REALNAME, EMPLOYEEID, DATECREATED)
 Values
   ('mmoore', 'Melinda Moore', '604351', TO_DATE('01/31/2013 11:29:00', 'MM/DD/YYYY HH24:MI:SS'));
Insert into TRANSP.AUTHUSERS
   (USERNAME, REALNAME, EMPLOYEEID, DATECREATED)
 Values
   ('tvega', 'Tamika Vega', '604380', TO_DATE('01/31/2013 11:29:00', 'MM/DD/YYYY HH24:MI:SS'));
Insert into TRANSP.AUTHUSERS
   (USERNAME, REALNAME, EMPLOYEEID, DATECREATED)
 Values
   ('rofarrell', 'Rachel OFarrell', '102704', TO_DATE('01/31/2013 11:29:00', 'MM/DD/YYYY HH24:MI:SS'));
Insert into TRANSP.AUTHUSERS
   (USERNAME, REALNAME, DATECREATED)
 Values
   ('rmarte', 'Robert Marte', TO_DATE('01/31/2013 11:29:00', 'MM/DD/YYYY HH24:MI:SS'));
Insert into TRANSP.AUTHUSERS
   (USERNAME, REALNAME, EMPLOYEEID, DATECREATED)
 Values
   ('fgomez', 'Francisco Gomez', '600776', TO_DATE('01/31/2013 11:29:00', 'MM/DD/YYYY HH24:MI:SS'));
Insert into TRANSP.AUTHUSERS
   (USERNAME, REALNAME, EMPLOYEEID, DATECREATED)
 Values
   ('eguachichullca', 'Enrique Guachichullca', '600945', TO_DATE('01/31/2013 11:29:00', 'MM/DD/YYYY HH24:MI:SS'));
Insert into TRANSP.AUTHUSERS
   (USERNAME, REALNAME, EMPLOYEEID, DATECREATED)
 Values
   ('kwhalen', 'Katie Whalen', '601441', TO_DATE('01/31/2013 11:29:01', 'MM/DD/YYYY HH24:MI:SS'));
Insert into TRANSP.AUTHUSERS
   (USERNAME, REALNAME, EMPLOYEEID, DATECREATED)
 Values
   ('chaws', 'Christopher Haws', '604647', TO_DATE('01/31/2013 11:29:01', 'MM/DD/YYYY HH24:MI:SS'));
Insert into TRANSP.AUTHUSERS
   (USERNAME, REALNAME, EMPLOYEEID, DATECREATED)
 Values
   ('oellis', 'Orealis Ellis', '604646', TO_DATE('01/31/2013 11:29:01', 'MM/DD/YYYY HH24:MI:SS'));
Insert into TRANSP.AUTHUSERS
   (USERNAME, REALNAME, EMPLOYEEID, DATECREATED)
 Values
   ('jquinones', 'Janet Quinones', '604086', TO_DATE('01/31/2013 11:29:01', 'MM/DD/YYYY HH24:MI:SS'));
Insert into TRANSP.AUTHUSERS
   (USERNAME, REALNAME, EMPLOYEEID, DATECREATED)
 Values
   ('asarro', 'Andrea Sarro', '604268', TO_DATE('01/31/2013 11:29:01', 'MM/DD/YYYY HH24:MI:SS'));
Insert into TRANSP.AUTHUSERS
   (USERNAME, REALNAME, EMPLOYEEID, DATECREATED)
 Values
   ('bshapiro', 'Benjamin Shapiro', '604617', TO_DATE('01/31/2013 11:29:01', 'MM/DD/YYYY HH24:MI:SS'));
Insert into TRANSP.AUTHUSERS
   (USERNAME, REALNAME, EMPLOYEEID, DATECREATED)
 Values
   ('bgueorguieva', 'Boriana Gueorguieva', '604689', TO_DATE('01/31/2013 11:29:01', 'MM/DD/YYYY HH24:MI:SS'));
Insert into TRANSP.AUTHUSERS
   (USERNAME, REALNAME, EMPLOYEEID, DATECREATED)
 Values
   ('chiguita', 'Catherine Higuita', '604057', TO_DATE('01/31/2013 11:29:01', 'MM/DD/YYYY HH24:MI:SS'));
Insert into TRANSP.AUTHUSERS
   (USERNAME, REALNAME, EMPLOYEEID, DATECREATED)
 Values
   ('jcartolano', 'James Cartolano', '604739', TO_DATE('01/31/2013 11:29:02', 'MM/DD/YYYY HH24:MI:SS'));
Insert into TRANSP.AUTHUSERS
   (USERNAME, REALNAME, EMPLOYEEID, DATECREATED)
 Values
   ('ehernandez', 'Edward Hernandez', '102029', TO_DATE('01/31/2013 11:29:02', 'MM/DD/YYYY HH24:MI:SS'));
Insert into TRANSP.AUTHUSERS
   (USERNAME, REALNAME, EMPLOYEEID, DATECREATED)
 Values
   ('cburrell', 'Camille Burrell', '604814', TO_DATE('01/31/2013 11:29:02', 'MM/DD/YYYY HH24:MI:SS'));
Insert into TRANSP.AUTHUSERS
   (USERNAME, REALNAME, EMPLOYEEID, DATECREATED)
 Values
   ('credd', 'Chad Redd', '604763', TO_DATE('01/31/2013 11:29:02', 'MM/DD/YYYY HH24:MI:SS'));
Insert into TRANSP.AUTHUSERS
   (USERNAME, REALNAME, EMPLOYEEID, DATECREATED)
 Values
   ('scoby', 'Steven Coby', '603090', TO_DATE('01/31/2013 11:29:02', 'MM/DD/YYYY HH24:MI:SS'));
Insert into TRANSP.AUTHUSERS
   (USERNAME, REALNAME, EMPLOYEEID, DATECREATED)
 Values
   ('lking', 'Leslie King', '604912', TO_DATE('01/31/2013 11:29:02', 'MM/DD/YYYY HH24:MI:SS'));
Insert into TRANSP.AUTHUSERS
   (USERNAME, REALNAME, EMPLOYEEID, DATECREATED)
 Values
   ('twheeler', 'Timothy Wheeler', '604933', TO_DATE('01/31/2013 11:29:02', 'MM/DD/YYYY HH24:MI:SS'));
Insert into TRANSP.AUTHUSERS
   (USERNAME, REALNAME, EMPLOYEEID, DATECREATED)
 Values
   ('Cwilson', 'Courtney Wilson', '103023', TO_DATE('01/31/2013 11:29:02', 'MM/DD/YYYY HH24:MI:SS'));
Insert into TRANSP.AUTHUSERS
   (USERNAME, REALNAME, EMPLOYEEID, DATECREATED)
 Values
   ('wperdue', 'Whitney Perdue', '604960', TO_DATE('01/31/2013 11:29:03', 'MM/DD/YYYY HH24:MI:SS'));
Insert into TRANSP.AUTHUSERS
   (USERNAME, REALNAME, EMPLOYEEID, DATECREATED)
 Values
   ('tpope', 'Tricia Pope', '604959', TO_DATE('01/31/2013 11:29:03', 'MM/DD/YYYY HH24:MI:SS'));
Insert into TRANSP.AUTHUSERS
   (USERNAME, REALNAME, EMPLOYEEID, DATECREATED)
 Values
   ('mmunnlyn', 'Miguel Munnlyn', '604961', TO_DATE('01/31/2013 11:29:03', 'MM/DD/YYYY HH24:MI:SS'));
Insert into TRANSP.AUTHUSERS
   (USERNAME, REALNAME, EMPLOYEEID, DATECREATED)
 Values
   ('acrabb', 'Alanna Crabb', '604551', TO_DATE('01/31/2013 11:29:03', 'MM/DD/YYYY HH24:MI:SS'));
Insert into TRANSP.AUTHUSERS
   (USERNAME, REALNAME, EMPLOYEEID, DATECREATED)
 Values
   ('tboulin', 'Tashanah Boulin', '604958', TO_DATE('01/31/2013 11:29:03', 'MM/DD/YYYY HH24:MI:SS'));
Insert into TRANSP.AUTHUSERS
   (USERNAME, REALNAME, EMPLOYEEID, DATECREATED)
 Values
   ('aburk', 'Andrea Burk', '605904', TO_DATE('01/31/2013 11:29:03', 'MM/DD/YYYY HH24:MI:SS'));
Insert into TRANSP.AUTHUSERS
   (USERNAME, REALNAME, EMPLOYEEID, DATECREATED)
 Values
   ('angarcia', 'Anderson Garcia', '604645', TO_DATE('01/31/2013 11:29:03', 'MM/DD/YYYY HH24:MI:SS'));
Insert into TRANSP.AUTHUSERS
   (USERNAME, REALNAME, EMPLOYEEID, DATECREATED)
 Values
   ('srobinson', 'Shawn D Robinson', '605942', TO_DATE('01/31/2013 11:29:03', 'MM/DD/YYYY HH24:MI:SS'));
Insert into TRANSP.AUTHUSERS
   (USERNAME, REALNAME, EMPLOYEEID, DATECREATED)
 Values
   ('rbarnett', 'Richard F Barnett', '605902', TO_DATE('01/31/2013 11:29:04', 'MM/DD/YYYY HH24:MI:SS'));
Insert into TRANSP.AUTHUSERS
   (USERNAME, REALNAME, EMPLOYEEID, DATECREATED)
 Values
   ('ksingh', 'Khemraj Singh', '605903', TO_DATE('01/31/2013 11:29:04', 'MM/DD/YYYY HH24:MI:SS'));
Insert into TRANSP.AUTHUSERS
   (USERNAME, REALNAME, DATECREATED)
 Values
   ('kkanuganti', 'Krishna Reddy', TO_DATE('01/31/2013 11:29:04', 'MM/DD/YYYY HH24:MI:SS'));
Insert into TRANSP.AUTHUSERS
   (USERNAME, REALNAME, EMPLOYEEID, DATECREATED)
 Values
   ('nmiles', 'Nisha Miles', '606078', TO_DATE('01/31/2013 11:29:04', 'MM/DD/YYYY HH24:MI:SS'));
Insert into TRANSP.AUTHUSERS
   (USERNAME, REALNAME, EMPLOYEEID, DATECREATED)
 Values
   ('mlittlejohn', 'Melvin Littlejohn', '605993', TO_DATE('01/31/2013 11:29:04', 'MM/DD/YYYY HH24:MI:SS'));
Insert into TRANSP.AUTHUSERS
   (USERNAME, REALNAME, EMPLOYEEID, DATECREATED)
 Values
   ('aanderson', 'Adelle Anderson', '606003', TO_DATE('01/31/2013 11:29:04', 'MM/DD/YYYY HH24:MI:SS'));
Insert into TRANSP.AUTHUSERS
   (USERNAME, REALNAME, EMPLOYEEID, DATECREATED)
 Values
   ('wurena', 'William Urena', '603071', TO_DATE('01/31/2013 11:29:04', 'MM/DD/YYYY HH24:MI:SS'));
Insert into TRANSP.AUTHUSERS
   (USERNAME, REALNAME, EMPLOYEEID, DATECREATED)
 Values
   ('kbrown', 'Kisha Brown', '104338', TO_DATE('01/31/2013 11:29:04', 'MM/DD/YYYY HH24:MI:SS'));
Insert into TRANSP.AUTHUSERS
   (USERNAME, REALNAME, EMPLOYEEID, DATECREATED)
 Values
   ('aclark', 'Anthony Clark', '606194', TO_DATE('01/31/2013 11:29:05', 'MM/DD/YYYY HH24:MI:SS'));
Insert into TRANSP.AUTHUSERS
   (USERNAME, REALNAME, EMPLOYEEID, DATECREATED)
 Values
   ('mroper', 'Melvin Roper', '606223', TO_DATE('01/31/2013 11:29:05', 'MM/DD/YYYY HH24:MI:SS'));
Insert into TRANSP.AUTHUSERS
   (USERNAME, REALNAME, EMPLOYEEID, DATECREATED)
 Values
   ('rcoger', 'Randi Coger', '604064', TO_DATE('01/31/2013 11:29:05', 'MM/DD/YYYY HH24:MI:SS'));
Insert into TRANSP.AUTHUSERS
   (USERNAME, REALNAME, DATECREATED)
 Values
   ('rdrabo', 'Roven Drabo', TO_DATE('01/31/2013 11:29:05', 'MM/DD/YYYY HH24:MI:SS'));
Insert into TRANSP.AUTHUSERS
   (USERNAME, REALNAME, EMPLOYEEID, DATECREATED)
 Values
   ('dfainbain', 'Daniel Fainbain', '606484', TO_DATE('01/31/2013 11:29:05', 'MM/DD/YYYY HH24:MI:SS'));
Insert into TRANSP.AUTHUSERS
   (USERNAME, REALNAME, EMPLOYEEID, DATECREATED)
 Values
   ('gmartinez', 'Gisselle Martinez', '606580', TO_DATE('01/31/2013 11:29:05', 'MM/DD/YYYY HH24:MI:SS'));
Insert into TRANSP.AUTHUSERS
   (USERNAME, REALNAME, EMPLOYEEID, DATECREATED)
 Values
   ('lgrant', 'Leslie Grant', '606571', TO_DATE('01/31/2013 11:29:05', 'MM/DD/YYYY HH24:MI:SS'));
Insert into TRANSP.AUTHUSERS
   (USERNAME, REALNAME, EMPLOYEEID, DATECREATED)
 Values
   ('hjiles', 'Howard Jiles', '606686', TO_DATE('01/31/2013 11:29:16', 'MM/DD/YYYY HH24:MI:SS'));
Insert into TRANSP.AUTHUSERS
   (USERNAME, REALNAME, DATECREATED)
 Values
   ('alorzeme', 'Anderson Lorzeme', TO_DATE('01/31/2013 11:29:17', 'MM/DD/YYYY HH24:MI:SS'));
Insert into TRANSP.AUTHUSERS
   (USERNAME, REALNAME, EMPLOYEEID, DATECREATED)
 Values
   ('errodriguez', 'Ernesto Rodriguez', '606014', TO_DATE('01/31/2013 11:29:17', 'MM/DD/YYYY HH24:MI:SS'));
Insert into TRANSP.AUTHUSERS
   (USERNAME, REALNAME, EMPLOYEEID, DATECREATED)
 Values
   ('vmedina', 'Victor Medina', '602991', TO_DATE('01/31/2013 11:29:17', 'MM/DD/YYYY HH24:MI:SS'));
Insert into TRANSP.AUTHUSERS
   (USERNAME, REALNAME, EMPLOYEEID, DATECREATED)
 Values
   ('ssantiago', 'Stephanie Santiago', '606028', TO_DATE('01/31/2013 11:29:17', 'MM/DD/YYYY HH24:MI:SS'));
Insert into TRANSP.AUTHUSERS
   (USERNAME, REALNAME, EMPLOYEEID, DATECREATED)
 Values
   ('jroman', 'Jimmy Roman', '606827', TO_DATE('01/31/2013 11:29:17', 'MM/DD/YYYY HH24:MI:SS'));
Insert into TRANSP.AUTHUSERS
   (USERNAME, REALNAME, EMPLOYEEID, DATECREATED)
 Values
   ('jeruiz', 'Jennyffer Ruiz', '606829', TO_DATE('01/31/2013 11:29:17', 'MM/DD/YYYY HH24:MI:SS'));
Insert into TRANSP.AUTHUSERS
   (USERNAME, REALNAME, EMPLOYEEID, DATECREATED)
 Values
   ('mchandler', 'Michael Chandler', '606796', TO_DATE('01/31/2013 11:29:17', 'MM/DD/YYYY HH24:MI:SS'));
Insert into TRANSP.AUTHUSERS
   (USERNAME, REALNAME, DATECREATED)
 Values
   ('kshiller', 'Kristin Shiller', TO_DATE('01/31/2013 11:29:17', 'MM/DD/YYYY HH24:MI:SS'));
Insert into TRANSP.AUTHUSERS
   (USERNAME, REALNAME, EMPLOYEEID, DATECREATED)
 Values
   ('pleepoy', 'Paula LeePoy', '140296', TO_DATE('01/31/2013 11:29:18', 'MM/DD/YYYY HH24:MI:SS'));
Insert into TRANSP.AUTHUSERS
   (USERNAME, REALNAME, EMPLOYEEID, DATECREATED)
 Values
   ('jsatizabal', 'Juan Satizabal', '500661', TO_DATE('01/31/2013 11:29:18', 'MM/DD/YYYY HH24:MI:SS'));
Insert into TRANSP.AUTHUSERS
   (USERNAME, REALNAME, EMPLOYEEID, DATECREATED)
 Values
   ('cwhite', 'Candice White', '604299', TO_DATE('01/31/2013 11:29:18', 'MM/DD/YYYY HH24:MI:SS'));
Insert into TRANSP.AUTHUSERS
   (USERNAME, REALNAME, EMPLOYEEID, DATECREATED)
 Values
   ('alazaro', 'Andrew Lazaro', '607256', TO_DATE('01/31/2013 11:29:18', 'MM/DD/YYYY HH24:MI:SS'));
Insert into TRANSP.AUTHUSERS
   (USERNAME, REALNAME, EMPLOYEEID, DATECREATED)
 Values
   ('llopez', 'Lizbeth Lopez', '6087267', TO_DATE('01/31/2013 11:29:18', 'MM/DD/YYYY HH24:MI:SS'));
Insert into TRANSP.AUTHUSERS
   (USERNAME, REALNAME, EMPLOYEEID, DATECREATED)
 Values
   ('jramirez', 'Jessica Ramirez', '607255', TO_DATE('01/31/2013 11:29:18', 'MM/DD/YYYY HH24:MI:SS'));
Insert into TRANSP.AUTHUSERS
   (USERNAME, REALNAME, EMPLOYEEID, DATECREATED)
 Values
   ('tknoll', 'Tim Knoll', '607199', TO_DATE('01/31/2013 11:29:18', 'MM/DD/YYYY HH24:MI:SS'));
Insert into TRANSP.AUTHUSERS
   (USERNAME, REALNAME, EMPLOYEEID, DATECREATED)
 Values
   ('pelizer', 'Prevenet Elizer', '606862', TO_DATE('01/31/2013 11:29:18', 'MM/DD/YYYY HH24:MI:SS'));
Insert into TRANSP.AUTHUSERS
   (USERNAME, REALNAME, EMPLOYEEID, DATECREATED)
 Values
   ('bkolakaluri', 'Bhavna Kolakaluri', '607112', TO_DATE('01/31/2013 11:29:18', 'MM/DD/YYYY HH24:MI:SS'));
Insert into TRANSP.AUTHUSERS
   (USERNAME, REALNAME, EMPLOYEEID, DATECREATED)
 Values
   ('ndavis', 'Nathan Davis', '607425', TO_DATE('01/31/2013 11:29:19', 'MM/DD/YYYY HH24:MI:SS'));
Insert into TRANSP.AUTHUSERS
   (USERNAME, REALNAME, EMPLOYEEID, DATECREATED)
 Values
   ('lgonzague', 'Leslieann Gonzague', '607514', TO_DATE('01/31/2013 11:29:19', 'MM/DD/YYYY HH24:MI:SS'));
Insert into TRANSP.AUTHUSERS
   (USERNAME, REALNAME, DATECREATED)
 Values
   ('lmclean', 'Latoya Mclean', TO_DATE('01/31/2013 11:29:19', 'MM/DD/YYYY HH24:MI:SS'));
Insert into TRANSP.AUTHUSERS
   (USERNAME, REALNAME, DATECREATED)
 Values
   ('mzapata', 'Miriam Zapata', TO_DATE('01/31/2013 11:29:19', 'MM/DD/YYYY HH24:MI:SS'));
Insert into TRANSP.AUTHUSERS
   (USERNAME, REALNAME, DATECREATED)
 Values
   ('hmontrose', 'Hugo Montrose', TO_DATE('01/31/2013 11:29:19', 'MM/DD/YYYY HH24:MI:SS'));
Insert into TRANSP.AUTHUSERS
   (USERNAME, REALNAME, DATECREATED)
 Values
   ('cgomez', 'Carlos Gomez', TO_DATE('01/31/2013 11:29:19', 'MM/DD/YYYY HH24:MI:SS'));
Insert into TRANSP.AUTHUSERS
   (USERNAME, REALNAME, DATECREATED)
 Values
   ('wdahne', 'William Dahne', TO_DATE('01/31/2013 11:29:19', 'MM/DD/YYYY HH24:MI:SS'));
Insert into TRANSP.AUTHUSERS
   (USERNAME, REALNAME, DATECREATED)
 Values
   ('jmcartolano', 'jeffrey cartolano', TO_DATE('01/31/2013 11:29:19', 'MM/DD/YYYY HH24:MI:SS'));
Insert into TRANSP.AUTHUSERS
   (USERNAME, REALNAME, DATECREATED)
 Values
   ('sbaldino', 'Steve Baldino', TO_DATE('01/31/2013 11:29:19', 'MM/DD/YYYY HH24:MI:SS'));

   

Insert into TRANSP.AUTHUSER_PRIVILEGE (USERNAME, PRIVILEGE_NAME) values ('yhan', 'EVENTLOG_ADD');
Insert into TRANSP.AUTHUSER_PRIVILEGE (USERNAME, PRIVILEGE_NAME) Values ('yhan', 'MOTEVENTLOG_ADD');   
Insert into TRANSP.AUTHUSER_PRIVILEGE (USERNAME, PRIVILEGE_NAME) Values ('yhan', 'MOTEVENTLOG_VERIFY');   
Insert into TRANSP.AUTHUSER_PRIVILEGE (USERNAME, PRIVILEGE_NAME) Values ('yhan', 'SHIFTLOG_ADD'); 
Insert into TRANSP.AUTHUSER_PRIVILEGE (USERNAME, PRIVILEGE_NAME) values ('jramirez', 'EVENTLOG_ADD');
Insert into TRANSP.AUTHUSER_PRIVILEGE (USERNAME, PRIVILEGE_NAME) Values ('jramirez', 'MOTEVENTLOG_ADD');   
Insert into TRANSP.AUTHUSER_PRIVILEGE (USERNAME, PRIVILEGE_NAME) Values ('jramirez', 'MOTEVENTLOG_VERIFY');   
Insert into TRANSP.AUTHUSER_PRIVILEGE (USERNAME, PRIVILEGE_NAME) Values ('jramirez', 'SHIFTLOG_ADD');    
Insert into TRANSP.AUTHUSER_PRIVILEGE (USERNAME, PRIVILEGE_NAME) values ('chodge', 'EVENTLOG_ADD');
Insert into TRANSP.AUTHUSER_PRIVILEGE (USERNAME, PRIVILEGE_NAME) Values ('chodge', 'MOTEVENTLOG_ADD');   
Insert into TRANSP.AUTHUSER_PRIVILEGE (USERNAME, PRIVILEGE_NAME) Values ('chodge', 'MOTEVENTLOG_VERIFY');   
Insert into TRANSP.AUTHUSER_PRIVILEGE (USERNAME, PRIVILEGE_NAME) Values ('chodge', 'SHIFTLOG_ADD');   
Insert into TRANSP.AUTHUSER_PRIVILEGE (USERNAME, PRIVILEGE_NAME) values ('oellis', 'EVENTLOG_ADD');
Insert into TRANSP.AUTHUSER_PRIVILEGE (USERNAME, PRIVILEGE_NAME) Values ('oellis', 'MOTEVENTLOG_ADD');   
Insert into TRANSP.AUTHUSER_PRIVILEGE (USERNAME, PRIVILEGE_NAME) Values ('oellis', 'MOTEVENTLOG_VERIFY');   
Insert into TRANSP.AUTHUSER_PRIVILEGE (USERNAME, PRIVILEGE_NAME) Values ('oellis', 'SHIFTLOG_ADD');   
Insert into TRANSP.AUTHUSER_PRIVILEGE (USERNAME, PRIVILEGE_NAME) values ('hjiles', 'EVENTLOG_ADD');
Insert into TRANSP.AUTHUSER_PRIVILEGE (USERNAME, PRIVILEGE_NAME) Values ('hjiles', 'MOTEVENTLOG_ADD');   
Insert into TRANSP.AUTHUSER_PRIVILEGE (USERNAME, PRIVILEGE_NAME) Values ('hjiles', 'MOTEVENTLOG_VERIFY');   
Insert into TRANSP.AUTHUSER_PRIVILEGE (USERNAME, PRIVILEGE_NAME) Values ('hjiles', 'SHIFTLOG_ADD');    
Insert into TRANSP.AUTHUSER_PRIVILEGE (USERNAME, PRIVILEGE_NAME) values ('kbrown', 'EVENTLOG_ADD');
Insert into TRANSP.AUTHUSER_PRIVILEGE (USERNAME, PRIVILEGE_NAME) Values ('kbrown', 'MOTEVENTLOG_ADD');   
Insert into TRANSP.AUTHUSER_PRIVILEGE (USERNAME, PRIVILEGE_NAME) Values ('kbrown', 'MOTEVENTLOG_VERIFY');   
Insert into TRANSP.AUTHUSER_PRIVILEGE (USERNAME, PRIVILEGE_NAME) Values ('kbrown', 'SHIFTLOG_ADD');   
Insert into TRANSP.AUTHUSER_PRIVILEGE (USERNAME, PRIVILEGE_NAME) values ('kwjames', 'EVENTLOG_ADD');
Insert into TRANSP.AUTHUSER_PRIVILEGE (USERNAME, PRIVILEGE_NAME) Values ('kwjames', 'MOTEVENTLOG_ADD');   
Insert into TRANSP.AUTHUSER_PRIVILEGE (USERNAME, PRIVILEGE_NAME) Values ('kwjames', 'MOTEVENTLOG_VERIFY');   
Insert into TRANSP.AUTHUSER_PRIVILEGE (USERNAME, PRIVILEGE_NAME) Values ('kwjames', 'SHIFTLOG_ADD');
Insert into TRANSP.AUTHUSER_PRIVILEGE (USERNAME, PRIVILEGE_NAME) values ('rofarrell', 'EVENTLOG_ADD');
Insert into TRANSP.AUTHUSER_PRIVILEGE (USERNAME, PRIVILEGE_NAME) Values ('rofarrell', 'MOTEVENTLOG_ADD');   
Insert into TRANSP.AUTHUSER_PRIVILEGE (USERNAME, PRIVILEGE_NAME) Values ('rofarrell', 'MOTEVENTLOG_VERIFY');   
Insert into TRANSP.AUTHUSER_PRIVILEGE (USERNAME, PRIVILEGE_NAME) Values ('rofarrell', 'SHIFTLOG_ADD');
Insert into TRANSP.AUTHUSER_PRIVILEGE (USERNAME, PRIVILEGE_NAME) values ('adavidson', 'EVENTLOG_ADD');
Insert into TRANSP.AUTHUSER_PRIVILEGE (USERNAME, PRIVILEGE_NAME) Values ('adavidson', 'SHIFTLOG_ADD');
Insert into TRANSP.AUTHUSER_PRIVILEGE (USERNAME, PRIVILEGE_NAME) values ('llopez', 'EVENTLOG_ADD');
Insert into TRANSP.AUTHUSER_PRIVILEGE (USERNAME, PRIVILEGE_NAME) Values ('llopez', 'SHIFTLOG_ADD');
Insert into TRANSP.AUTHUSER_PRIVILEGE (USERNAME, PRIVILEGE_NAME) values ('tvega', 'EVENTLOG_ADD');
Insert into TRANSP.AUTHUSER_PRIVILEGE (USERNAME, PRIVILEGE_NAME) Values ('tvega', 'SHIFTLOG_ADD');
Insert into TRANSP.AUTHUSER_PRIVILEGE (USERNAME, PRIVILEGE_NAME) values ('ehernandez', 'EVENTLOG_ADD');
Insert into TRANSP.AUTHUSER_PRIVILEGE (USERNAME, PRIVILEGE_NAME) Values ('ehernandez', 'SHIFTLOG_ADD');
Insert into TRANSP.AUTHUSER_PRIVILEGE (USERNAME, PRIVILEGE_NAME) values ('ssantiago', 'EVENTLOG_ADD');
Insert into TRANSP.AUTHUSER_PRIVILEGE (USERNAME, PRIVILEGE_NAME) Values ('ssantiago', 'SHIFTLOG_ADD');
Insert into TRANSP.AUTHUSER_PRIVILEGE (USERNAME, PRIVILEGE_NAME) values ('gmartinez', 'EVENTLOG_ADD');
Insert into TRANSP.AUTHUSER_PRIVILEGE (USERNAME, PRIVILEGE_NAME) Values ('gmartinez', 'SHIFTLOG_ADD');
Insert into TRANSP.AUTHUSER_PRIVILEGE (USERNAME, PRIVILEGE_NAME) values ('tboulin', 'EVENTLOG_ADD');
Insert into TRANSP.AUTHUSER_PRIVILEGE (USERNAME, PRIVILEGE_NAME) Values ('tboulin', 'SHIFTLOG_ADD');
Insert into TRANSP.AUTHUSER_PRIVILEGE (USERNAME, PRIVILEGE_NAME) values ('jroman', 'EVENTLOG_ADD');
Insert into TRANSP.AUTHUSER_PRIVILEGE (USERNAME, PRIVILEGE_NAME) Values ('jroman', 'SHIFTLOG_ADD');
Insert into TRANSP.AUTHUSER_PRIVILEGE (USERNAME, PRIVILEGE_NAME) values ('lking', 'EVENTLOG_ADD');
Insert into TRANSP.AUTHUSER_PRIVILEGE (USERNAME, PRIVILEGE_NAME) Values ('lking', 'SHIFTLOG_ADD');
Insert into TRANSP.AUTHUSER_PRIVILEGE (USERNAME, PRIVILEGE_NAME) values ('vmedina', 'EVENTLOG_ADD');
Insert into TRANSP.AUTHUSER_PRIVILEGE (USERNAME, PRIVILEGE_NAME) Values ('vmedina', 'SHIFTLOG_ADD');
Insert into TRANSP.AUTHUSER_PRIVILEGE (USERNAME, PRIVILEGE_NAME) values ('valva', 'EVENTLOG_ADD');
Insert into TRANSP.AUTHUSER_PRIVILEGE (USERNAME, PRIVILEGE_NAME) Values ('valva', 'SHIFTLOG_ADD'); 

Insert into TRANSP.AUTHUSER_PRIVILEGE (USERNAME, PRIVILEGE_NAME) values ('qaadmin', 'EVENTLOG_ADD');
Insert into TRANSP.AUTHUSER_PRIVILEGE (USERNAME, PRIVILEGE_NAME) Values ('qaadmin', 'MOTEVENTLOG_ADD');   
Insert into TRANSP.AUTHUSER_PRIVILEGE (USERNAME, PRIVILEGE_NAME) Values ('qaadmin', 'MOTEVENTLOG_VERIFY');   
Insert into TRANSP.AUTHUSER_PRIVILEGE (USERNAME, PRIVILEGE_NAME) Values ('qaadmin', 'SHIFTLOG_ADD'); 
    
    
    
    