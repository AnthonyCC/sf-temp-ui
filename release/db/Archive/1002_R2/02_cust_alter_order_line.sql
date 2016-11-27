alter table cust.fduser add ZP_SERVICE_TYPE varchar2(25);

alter table cust.orderline add pricing_zone_id varchar2(16);

alter table cust.LOG_SESSION_IMPRESSIONS add zoneId varchar2(16);

INSERT INTO CUST.PROFILE_ATTR_NAME ( NAME, DESCRIPTION, CATEGORY, ATTR_VALUE_TYPE,
IS_EDITABLE ) VALUES ( 
'siteFeature.zonePricing', 'Zone Pricing Internal', 'Feature', NULL, 'X');

