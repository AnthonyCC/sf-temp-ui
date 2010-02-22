alter table cust.orderline add pricing_zone_id varchar2(16);

INSERT INTO CUST.PROFILE_ATTR_NAME ( NAME, DESCRIPTION, CATEGORY, ATTR_VALUE_TYPE,
IS_EDITABLE ) VALUES ( 
'siteFeature.zonePricing', 'Zone Pricing Internal', 'Feature', NULL, 'X');
