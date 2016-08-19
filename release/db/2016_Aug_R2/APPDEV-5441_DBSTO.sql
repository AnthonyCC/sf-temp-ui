insert into transp.property_master(name, type, default_value, property_enabled) values('deliv.url', 'LOG', 'https://api.deliv.co/v2/', 'X');  
insert into transp.property_master(name, type, default_value, property_enabled) values('deliv.storeId', 'LOG', '0c40f697-dfb2-4a59-87c4-dab56b5797c8', 'X');
insert into transp.property_master(name, type, default_value, property_enabled) values('deliv.maxorder.price', 'LOG', '49.00', 'X');
insert into transp.property_master(name, type, default_value, property_enabled) values('deliv.webhook.url', 'LOG', 'https://mobileapi.freshdirect.com/mobileapi/v/1/ext/t005/deliv', 'X');
insert into transp.property_master(name, type, default_value, property_enabled) values('deliv.apikey', 'LOG', '396e3626ca6d5f9ab9701631f497e172c0e6', 'X');
insert into transp.FDX_TRAVELMODE_VEHICLE_MAPPING(VEHICLE_TYPE,TRAVEL_MODE) values('DELIV','DRIVING')
insert into TRANSP.FDX_EQUIPMENT_TYPE(Equipment_type,sov,id) values('DELIV','99999','3')
ALTER TABLE TRANSP.FDX_ORDER
ADD (DELIVERY_ID  VARCHAR(200 BYTE));
ALTER TABLE MIS.SPRINT_ACTIVITY_LOG
ADD (CARRIER  VARCHAR(50 BYTE));

