
alter table TRANSP.ASSET_ATTRIBUTETYPE add (DATA_TYPE VARCHAR2(40 BYTE) NOT NULL);
ALTER TABLE TRANSP.MAINTENANCEISSUE  MODIFY  servicestatus DEFAULT 'In-Service';

update transp.asset set asset_status = 'IAC' where asset_status='INC';

update transp.maintenanceissue set servicestatus = 'In-Service' where servicestatus = 'Active';

delete from TRANSP.ASSET_ATTRIBUTETYPE where CODE =  'Reefer Model';
delete from TRANSP.ASSET_ATTRIBUTETYPE where CODE =  'Date in Service';
delete from TRANSP.ASSET_ATTRIBUTETYPE where CODE =  'GVW';
delete from TRANSP.ASSET_ATTRIBUTETYPE where CODE =  'License Plate (Current)';
delete from TRANSP.ASSET_ATTRIBUTETYPE where CODE =  'Monthly Lease Cost';
delete from TRANSP.ASSET_ATTRIBUTETYPE where CODE =  'Chassis Model';
delete from TRANSP.ASSET_ATTRIBUTETYPE where CODE =  'Chassis Year';
delete from TRANSP.ASSET_ATTRIBUTETYPE where CODE =  'Reefer Model';    
delete from TRANSP.ASSET_ATTRIBUTETYPE where CODE =  'Date in Service';
delete from TRANSP.ASSET_ATTRIBUTETYPE where CODE =  'GVW';
delete from TRANSP.ASSET_ATTRIBUTETYPE where CODE =  'License Plate (Current)';
delete from TRANSP.ASSET_ATTRIBUTETYPE where CODE =  'Monthly Lease Cost';
delete from TRANSP.ASSET_ATTRIBUTETYPE where CODE =  'Chassis Model';
delete from TRANSP.ASSET_ATTRIBUTETYPE where CODE =  'Chassis Year';

Insert into TRANSP.ASSET_ATTRIBUTETYPE
   (CODE, DESCRIPTION, DATA_TYPE, ASSET_TYPE)
 Values
   ('Model', 'Model', 'String', 'TRUCK');
Insert into TRANSP.ASSET_ATTRIBUTETYPE
   (CODE, DESCRIPTION, DATA_TYPE, ASSET_TYPE)
 Values
   ('Model Year', 'Model Year', 'String', 'TRUCK');
Insert into TRANSP.ASSET_ATTRIBUTETYPE
   (CODE, DESCRIPTION, DATA_TYPE, ASSET_TYPE)
 Values
   ('Type', 'Type', 'String', 'TRUCK');
Insert into TRANSP.ASSET_ATTRIBUTETYPE
   (CODE, DESCRIPTION, DATA_TYPE, ASSET_TYPE)
 Values
   ('Internal Truck #', 'Internal Truck #', 'String', 'TRUCK');
Insert into TRANSP.ASSET_ATTRIBUTETYPE
   (CODE, DESCRIPTION, DATA_TYPE, ASSET_TYPE)
 Values
   ('Current_Plate', 'Current_Plate', 'String', 'TRUCK');
Insert into TRANSP.ASSET_ATTRIBUTETYPE
   (CODE, DESCRIPTION, DATA_TYPE, ASSET_TYPE)
 Values
   ('Lease Maintenance', 'Lease Maintenance', 'String', 'TRUCK');
Insert into TRANSP.ASSET_ATTRIBUTETYPE
   (CODE, DESCRIPTION, DATA_TYPE, ASSET_TYPE)
 Values
   ('Included Mileage', 'Included Mileage', 'String', 'TRUCK');
Insert into TRANSP.ASSET_ATTRIBUTETYPE
   (CODE, DESCRIPTION, DATA_TYPE, ASSET_TYPE)
 Values
   ('Maintenance Rate', 'Maintenance Rate', 'String', 'TRUCK');
Insert into TRANSP.ASSET_ATTRIBUTETYPE
   (CODE, DESCRIPTION, DATA_TYPE, ASSET_TYPE)
 Values
   ('Wgt (Weight?)', 'Wgt (Weight?)', 'String', 'TRUCK');
Insert into TRANSP.ASSET_ATTRIBUTETYPE
   (CODE, DESCRIPTION, DATA_TYPE, ASSET_TYPE)
 Values
   ('Load Capacity', 'Load Capacity', 'String', 'TRUCK');
Insert into TRANSP.ASSET_ATTRIBUTETYPE
   (CODE, DESCRIPTION, DATA_TYPE, ASSET_TYPE)
 Values
   ('Reefer Type', 'Reefer Type', 'String', 'TRUCK');
Insert into TRANSP.ASSET_ATTRIBUTETYPE
   (CODE, DESCRIPTION, DATA_TYPE, ASSET_TYPE)
 Values
   ('Body InService Date', 'Body InService Date', 'String', 'TRUCK');
Insert into TRANSP.ASSET_ATTRIBUTETYPE
   (CODE, DESCRIPTION, DATA_TYPE, ASSET_TYPE)
 Values
   ('Freezers', 'Freezers', 'String', 'TRUCK');
Insert into TRANSP.ASSET_ATTRIBUTETYPE
   (CODE, DESCRIPTION, DATA_TYPE, ASSET_TYPE)
 Values
   ('Reefer InService Date', 'Reefer InService Date', 'String', 'TRUCK');
Insert into TRANSP.ASSET_ATTRIBUTETYPE
   (CODE, DESCRIPTION, DATA_TYPE, ASSET_TYPE)
 Values
   ('Reefer Type #1', 'Reefer Type #1', 'String', 'TRUCK');
Insert into TRANSP.ASSET_ATTRIBUTETYPE
   (CODE, DESCRIPTION, DATA_TYPE, ASSET_TYPE)
 Values
   ('Reefer #1 Serial#/ Unit #', 'Reefer #1 Serial#/ Unit #', 'String', 'TRUCK');
Insert into TRANSP.ASSET_ATTRIBUTETYPE
   (CODE, DESCRIPTION, DATA_TYPE, ASSET_TYPE)
 Values
   ('Reefer #2 InService Date', 'Reefer #2 InService Date', 'String', 'TRUCK');
Insert into TRANSP.ASSET_ATTRIBUTETYPE
   (CODE, DESCRIPTION, DATA_TYPE, ASSET_TYPE)
 Values
   ('Reefer Type #2', 'Reefer Type #2', 'String', 'TRUCK');
Insert into TRANSP.ASSET_ATTRIBUTETYPE
   (CODE, DESCRIPTION, DATA_TYPE, ASSET_TYPE)
 Values
   ('Reefer #2 Serial#/Unit #', 'Reefer #2 Serial#/Unit #', 'String', 'TRUCK');
Insert into TRANSP.ASSET_ATTRIBUTETYPE
   (CODE, DESCRIPTION, DATA_TYPE, ASSET_TYPE)
 Values
   ('Cost', 'Cost', 'String', 'TRUCK');
Insert into TRANSP.ASSET_ATTRIBUTETYPE
   (CODE, DESCRIPTION, DATA_TYPE, ASSET_TYPE)
 Values
   ('Assumed Residual', 'Assumed Residual', 'String', 'TRUCK');
Insert into TRANSP.ASSET_ATTRIBUTETYPE
   (CODE, DESCRIPTION, DATA_TYPE, ASSET_TYPE)
 Values
   ('Leasing Company', 'Leasing Company', 'String', 'TRUCK');
Insert into TRANSP.ASSET_ATTRIBUTETYPE
   (CODE, DESCRIPTION, DATA_TYPE, ASSET_TYPE)
 Values
   ('Lease(Current) Term', 'Lease(Current) Term', 'String', 'TRUCK');
Insert into TRANSP.ASSET_ATTRIBUTETYPE
   (CODE, DESCRIPTION, DATA_TYPE, ASSET_TYPE)
 Values
   ('Current Monthly LeaseCost', 'Current Monthly LeaseCost', 'String', 'TRUCK');
Insert into TRANSP.ASSET_ATTRIBUTETYPE
   (CODE, DESCRIPTION, DATA_TYPE, ASSET_TYPE)
 Values
   ('Previous Lease Start', 'Previous Lease Start', 'String', 'TRUCK');
Insert into TRANSP.ASSET_ATTRIBUTETYPE
   (CODE, DESCRIPTION, DATA_TYPE, ASSET_TYPE)
 Values
   ('Previous Lease End', 'Previous Lease End', 'String', 'TRUCK');
Insert into TRANSP.ASSET_ATTRIBUTETYPE
   (CODE, DESCRIPTION, DATA_TYPE, ASSET_TYPE)
 Values
   ('Previous Lease Term', 'Previous Lease Term', 'String', 'TRUCK');
Insert into TRANSP.ASSET_ATTRIBUTETYPE
   (CODE, DESCRIPTION, DATA_TYPE, ASSET_TYPE)
 Values
   ('Previous Lease Cost', 'Previous Lease Cost', 'String', 'TRUCK');
Insert into TRANSP.ASSET_ATTRIBUTETYPE
   (CODE, DESCRIPTION, DATA_TYPE, ASSET_TYPE)
 Values
   ('Lease 3 Start', 'Lease 3 Start', 'String', 'TRUCK');
Insert into TRANSP.ASSET_ATTRIBUTETYPE
   (CODE, DESCRIPTION, DATA_TYPE, ASSET_TYPE)
 Values
   ('Lease 3 End', 'Lease 3 End', 'String', 'TRUCK');
Insert into TRANSP.ASSET_ATTRIBUTETYPE
   (CODE, DESCRIPTION, DATA_TYPE, ASSET_TYPE)
 Values
   ('Lease 3 Term', 'Lease 3 Term', 'String', 'TRUCK');
Insert into TRANSP.ASSET_ATTRIBUTETYPE
   (CODE, DESCRIPTION, DATA_TYPE, ASSET_TYPE)
 Values
   ('Lease 3 Cost', 'Lease 3 Cost', 'String', 'TRUCK');
Insert into TRANSP.ASSET_ATTRIBUTETYPE
   (CODE, DESCRIPTION, DATA_TYPE, ASSET_TYPE)
 Values
   ('INSURANCE COMPANY', 'INSURANCE COMPANY', 'String', 'TRUCK');
Insert into TRANSP.ASSET_ATTRIBUTETYPE
   (CODE, DESCRIPTION, DATA_TYPE, ASSET_TYPE)
 Values
   ('Insurance Expiration Date', 'Insurance Expiration Date', 'String', 'TRUCK');
Insert into TRANSP.ASSET_ATTRIBUTETYPE
   (CODE, DESCRIPTION, DATA_TYPE, ASSET_TYPE)
 Values
   ('Year InService', 'Year InService', 'String', 'TRUCK');
Insert into TRANSP.ASSET_ATTRIBUTETYPE
   (CODE, DESCRIPTION, DATA_TYPE, ASSET_TYPE)
 Values
   ('Plate #1', 'Plate #1', 'String', 'TRUCK');
Insert into TRANSP.ASSET_ATTRIBUTETYPE
   (CODE, DESCRIPTION, DATA_TYPE, ASSET_TYPE)
 Values
   ('Plate #1 End Date', 'Plate #1 End Date', 'String', 'TRUCK');
Insert into TRANSP.ASSET_ATTRIBUTETYPE
   (CODE, DESCRIPTION, DATA_TYPE, ASSET_TYPE)
 Values
   ('Plate #2', 'Plate #2', 'String', 'TRUCK');
Insert into TRANSP.ASSET_ATTRIBUTETYPE
   (CODE, DESCRIPTION, DATA_TYPE, ASSET_TYPE)
 Values
   ('Plate #2 End Date', 'Plate #2 End Date', 'String', 'TRUCK');

update transp.asset_attributetype set code='Rental Flag' where code='Contract-Type';
update transp.asset_attributetype set code='Make' where code='Chassis Make';
update transp.asset_attributetype set code='Vendor' where code='Vendor - Truck';
update transp.asset_attributetype set code='Electric Drive Flag' where code='Chassis Engine Type';
update transp.asset_attributetype set code='Electric Reefer Flag' where code='Reefer Type';
update transp.asset_attributetype set code='Reefer UnitNumber' where code='Reefer Unit#';
update transp.asset_attributetype set code='Vendor Number #' where code='Vendor Number#';

update transp.asset_attribute set attribute_type='Rental Flag' where attribute_type='Contract-Type';
update transp.asset_attribute set attribute_type='Make' where attribute_type='Chassis Make';
update transp.asset_attribute set attribute_type='Vendor' where attribute_type='Vendor - Truck';
update transp.asset_attribute set attribute_type='Electric Drive Flag' where attribute_type='Chassis Engine Type';
update transp.asset_attribute set attribute_type='Electric Reefer Flag' where attribute_type='Reefer Type';
update transp.asset_attribute set attribute_type='Reefer UnitNumber' where attribute_type='Reefer Unit#';
update transp.asset_attribute set attribute_type='Vendor Number #' where attribute_type='Vendor Number#';

update transp.maintenanceissue set servicestatus = 'Out-Service' where servicestatus = 'Out of Service';
