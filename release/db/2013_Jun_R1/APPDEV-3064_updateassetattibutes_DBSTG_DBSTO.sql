alter table TRANSP.ASSET_ATTRIBUTETYPE drop column data_type;

delete from TRANSP.ASSET_ATTRIBUTE where attribute_type in 
(
'Assumed Residual',
'Current Monthly LeaseCost',
'Current_Plate',
'Freezers',
'Insurance Expiration Date',
'Internal Truck #',
'Lease 3 Cost',
'Lease 3 End',
'Lease 3 Start',
'Lease 3 Term',
'Lease Maintenance',
'Lease(Current) Term',
'Leasing Company',
'Load Capacity',
'Model',
'Model Year',
'Maintenance Rate',
'Plate #1',
'Plate #1 End Date',
'Plate #2',
'Plate #2 End Date',
'Previous Lease Cost',
'Previous Lease End',
'Previous Lease Start',
'Previous Lease Term',
'Reefer #1 Serial#/ Unit #',
'Reefer #2 InService Date',
'Reefer #2 Serial#/Unit #',
'Reefer Type #1',
'Reefer Type #2',
'Type',
'Wgt (Weight?)',
'Year InService',
'Body InService Date',
'Cost',
'Included Mileage',
'INSURANCE COMPANY',
'Reefer Type',
'Reefer InService Date'
);

delete from TRANSP.ASSET_ATTRIBUTETYPE where code in (
'Assumed Residual',
'Current Monthly LeaseCost',
'Current_Plate',
'Freezers',
'Insurance Expiration Date',
'Internal Truck #',
'Lease 3 Cost',
'Lease 3 End',
'Lease 3 Start',
'Lease 3 Term',
'Lease Maintenance',
'Lease(Current) Term',
'Leasing Company',
'Load Capacity',
'Model',
'Model Year',
'Maintenance Rate',
'Plate #1',
'Plate #1 End Date',
'Plate #2',
'Plate #2 End Date',
'Previous Lease Cost',
'Previous Lease End',
'Previous Lease Start',
'Previous Lease Term',
'Reefer #1 Serial#/ Unit #',
'Reefer #2 InService Date',
'Reefer #2 Serial#/Unit #',
'Reefer Type #1',
'Reefer Type #2',
'Type',
'Wgt (Weight?)',
'Year InService',
'Body InService Date',
'Cost',
'Included Mileage',
'INSURANCE COMPANY',
'Reefer Type',
'Reefer InService Date'
);

insert into TRANSP.ASSET_ATTRIBUTETYPE (CODE, DESCRIPTION, ASSET_TYPE) Values ('Reefer Model', 'Reefer Model', 'TRUCK');    
insert into TRANSP.ASSET_ATTRIBUTETYPE (CODE, DESCRIPTION, ASSET_TYPE) Values ('Date in Service', 'Date in Service', 'TRUCK');
insert into TRANSP.ASSET_ATTRIBUTETYPE (CODE, DESCRIPTION, ASSET_TYPE) Values ('GVW', 'GVW', 'TRUCK');
insert into TRANSP.ASSET_ATTRIBUTETYPE (CODE, DESCRIPTION, ASSET_TYPE) Values ('License Plate (Current)', 'License Plate (Current)', 'TRUCK');
insert into TRANSP.ASSET_ATTRIBUTETYPE (CODE, DESCRIPTION, ASSET_TYPE) Values ('Monthly Lease Cost', 'Monthly Lease Cost', 'TRUCK');
insert into TRANSP.ASSET_ATTRIBUTETYPE (CODE, DESCRIPTION, ASSET_TYPE) Values ('Chassis Model', 'Model', 'TRUCK');
insert into TRANSP.ASSET_ATTRIBUTETYPE (CODE, DESCRIPTION, ASSET_TYPE) Values ('Chassis Year', 'Year', 'TRUCK');

insert into TRANSP.ASSET_ATTRIBUTETYPE (CODE, DESCRIPTION, ASSET_TYPE) Values ('Reefer Model', 'Reefer Model', 'TRAILER');    
insert into TRANSP.ASSET_ATTRIBUTETYPE (CODE, DESCRIPTION, ASSET_TYPE) Values ('Date in Service', 'Date in Service', 'TRAILER');
insert into TRANSP.ASSET_ATTRIBUTETYPE (CODE, DESCRIPTION, ASSET_TYPE) Values ('GVW', 'GVW', 'TRAILER');
insert into TRANSP.ASSET_ATTRIBUTETYPE (CODE, DESCRIPTION, ASSET_TYPE) Values ('License Plate (Current)', 'License Plate (Current)', 'TRAILER');
insert into TRANSP.ASSET_ATTRIBUTETYPE (CODE, DESCRIPTION, ASSET_TYPE) Values ('Monthly Lease Cost', 'Monthly Lease Cost', 'TRAILER');
insert into TRANSP.ASSET_ATTRIBUTETYPE (CODE, DESCRIPTION, ASSET_TYPE) Values ('Chassis Model', 'Model', 'TRAILER');
insert into TRANSP.ASSET_ATTRIBUTETYPE (CODE, DESCRIPTION, ASSET_TYPE) Values ('Chassis Year', 'Year', 'TRAILER');

update transp.asset_attributetype set code='Contract-Type' where code='Rental Flag';
update transp.asset_attributetype set code='Chassis Make' where code='Make';
update transp.asset_attributetype set code='Vendor - Truck' where code='Vendor';
update transp.asset_attributetype set code='Chassis Engine Type' where code='Electric Drive Flag';
update transp.asset_attributetype set code='Reefer Type' where code='Electric Reefer Flag';
update transp.asset_attributetype set code='Reefer Unit#' where code='Reefer UnitNumber';
update transp.asset_attributetype set code='Vendor Number#' where code='Vendor Number #';

update transp.asset_attribute set attribute_type='Contract-Type' where attribute_type='Rental Flag';
update transp.asset_attribute set attribute_type='Chassis Make' where attribute_type='Make';
update transp.asset_attribute set attribute_type='Vendor - Truck' where attribute_type='Vendor';
update transp.asset_attribute set attribute_type='Vendor Number#' where attribute_type='Vendor Number #';
update transp.asset_attribute set attribute_type='Chassis Engine Type' where attribute_type='Electric Drive Flag';
update transp.asset_attribute set attribute_type='Reefer Type' where attribute_type='Electric Reefer Flag';
update transp.asset_attribute set attribute_type='Reefer Unit#' where attribute_type='Reefer UnitNumber';

update transp.asset set asset_status = 'INC' where asset_status='IAC';

update transp.maintenanceissue set servicestatus = 'Active' where servicestatus = 'In-Service';
update transp.maintenanceissue set servicestatus = 'Out of Service' where servicestatus = 'Out-Service';

ALTER TABLE TRANSP.MAINTENANCEISSUE  MODIFY  servicestatus DEFAULT 'Active';
