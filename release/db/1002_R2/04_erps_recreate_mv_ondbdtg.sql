-- step2

--drop snapshot ATTRIBUTES; -- This plus 5 more MV not present currently in DBSTG, check Sai
-- Current Mviews in DBSTG - 14
--Script 03* - part-2 has 20. 
--Missing -  COOL_INFO
--extra - 
 --INGREDIENTS;	
 --INVENTORY;	
 --INVENTORY_ENTRY;
 --NUTRITION;	 
--NUTRITION_INFO;
--UNAVAILABILITY;
--ATTRIBUTES
-- confirmed, cool_info do not need refresh. 2. five Extra MVs to be removed. 

drop snapshot CHARACTERISTIC;
drop snapshot CHARVALUE;
drop snapshot CHARVALUEPRICE;
drop snapshot CLASS;
drop snapshot HISTORY;
drop snapshot MATERIAL;
drop snapshot MATERIALPRICE;
drop snapshot MATERIALPROXY;
drop snapshot MATERIALPROXY_CHARVALUE;
drop snapshot MATERIALPROXY_SALESUNIT;
drop snapshot MATERIAL_CLASS;
drop snapshot PRODUCT;
drop snapshot SALESUNIT;

-- create snapshot is supported for backward compatibility to create materialized view. confirm sai
-- refresh next should be 10 minutes but I did not see that in script so modified it from
-- trunc(sysdate+1)+1/24/60 to sysdate+1/144. confirm sai.

create snapshot CHARACTERISTIC refresh force start with sysdate next sysdate+1/144
as SELECT * FROM "CHARACTERISTIC"@DBSTO.NYC.FRESHDIRECT.COM "CHARACTERISTIC";

create snapshot CHARVALUE refresh force start with sysdate next sysdate+1/144
as SELECT * FROM "CHARVALUE"@DBSTO.NYC.FRESHDIRECT.COM "CHARVALUE";

create snapshot CHARVALUEPRICE refresh force start with sysdate next sysdate+1/144
as SELECT * FROM "CHARVALUEPRICE"@DBSTO.NYC.FRESHDIRECT.COM "CHARVALUEPRICE";

create snapshot CLASS refresh force start with sysdate next sysdate+1/144
as SELECT * FROM "CLASS"@DBSTO.NYC.FRESHDIRECT.COM "CLASS";

create snapshot HISTORY refresh force start with sysdate next sysdate+1/144   
as SELECT * FROM "HISTORY"@DBSTO.NYC.FRESHDIRECT.COM "HISTORY";

create snapshot MATERIAL refresh force start with sysdate next sysdate+1/144   
as SELECT * FROM "MATERIAL"@DBSTO.NYC.FRESHDIRECT.COM "MATERIAL";

----------------------------------------------------
create snapshot MATERIALPRICE refresh force start with sysdate next sysdate+1/144   
as SELECT * FROM "MATERIALPRICE"@DBSTO.NYC.FRESHDIRECT.COM "MATERIALPRICE";

create snapshot MATERIALPROXY refresh force start with sysdate next sysdate+1/144   
as SELECT * FROM "MATERIALPROXY"@DBSTO.NYC.FRESHDIRECT.COM "MATERIALPROXY";

create snapshot MATERIALPROXY_CHARVALUE refresh force start with sysdate next sysdate+1/144   
as SELECT * FROM "MATERIALPROXY_CHARVALUE"@DBSTO.NYC.FRESHDIRECT.COM "MATERIALPROXY_CHARVALUE";

create snapshot MATERIALPROXY_SALESUNIT refresh force start with sysdate next sysdate+1/144   
as SELECT * FROM "MATERIALPROXY_SALESUNIT"@DBSTO.NYC.FRESHDIRECT.COM "MATERIALPROXY_SALESUNIT";

create snapshot MATERIAL_CLASS refresh force start with sysdate next sysdate+1/144   
as SELECT * FROM "MATERIAL_CLASS"@DBSTO.NYC.FRESHDIRECT.COM "MATERIAL_CLASS";

create snapshot PRODUCT refresh force start with sysdate next sysdate+1/144  
as SELECT * FROM "PRODUCT"@DBSTO.NYC.FRESHDIRECT.COM "PRODUCT";

create snapshot SALESUNIT refresh force start with sysdate next sysdate+1/144  
as SELECT * FROM "SALESUNIT"@DBSTO.NYC.FRESHDIRECT.COM "SALESUNIT" ;

-- grant privileges 
grant SELECT on erps.CHARVALUEPRICE to DGERRIDGE;
grant DELETE on erps.CHARVALUEPRICE to FDSTORE_PRDB;
grant INSERT on erps.CHARVALUEPRICE to FDSTORE_PRDB;
grant SELECT on erps.CHARVALUEPRICE to FDSTORE_PRDB;
grant UPDATE on erps.CHARVALUEPRICE to FDSTORE_PRDB;
grant DELETE on erps.CHARVALUEPRICE to FDSTORE_PRDA;
grant INSERT on erps.CHARVALUEPRICE to FDSTORE_PRDA;
grant SELECT on erps.CHARVALUEPRICE to FDSTORE_PRDA;
grant UPDATE on erps.CHARVALUEPRICE to FDSTORE_PRDA;
grant SELECT on erps.CHARVALUEPRICE to APPDEV;
grant DELETE on erps.CHARVALUEPRICE to FDSTORE_STSTG01;
grant INSERT on erps.CHARVALUEPRICE to FDSTORE_STSTG01;
grant SELECT on erps.CHARVALUEPRICE to FDSTORE_STSTG01;
grant UPDATE on erps.CHARVALUEPRICE to FDSTORE_STSTG01;
grant SELECT on erps.CHARVALUE to DGERRIDGE;
grant DELETE on erps.CHARVALUE to FDSTORE_PRDB;
grant INSERT on erps.CHARVALUE to FDSTORE_PRDB;
grant SELECT on erps.CHARVALUE to FDSTORE_PRDB;
grant UPDATE on erps.CHARVALUE to FDSTORE_PRDB;
grant DELETE on erps.CHARVALUE to FDSTORE_PRDA;
grant INSERT on erps.CHARVALUE to FDSTORE_PRDA;
grant SELECT on erps.CHARVALUE to FDSTORE_PRDA;
grant UPDATE on erps.CHARVALUE to FDSTORE_PRDA;
grant SELECT on erps.CHARVALUE to APPDEV;
grant DELETE on erps.CHARVALUE to FDSTORE_STSTG01;
grant INSERT on erps.CHARVALUE to FDSTORE_STSTG01;
grant SELECT on erps.CHARVALUE to FDSTORE_STSTG01;
grant UPDATE on erps.CHARVALUE to FDSTORE_STSTG01;
grant SELECT on erps.MATERIALPRICE to DGERRIDGE;
grant DELETE on erps.MATERIALPRICE to FDSTORE_PRDB;
grant INSERT on erps.MATERIALPRICE to FDSTORE_PRDB;
grant SELECT on erps.MATERIALPRICE to FDSTORE_PRDB;
grant UPDATE on erps.MATERIALPRICE to FDSTORE_PRDB;
grant DELETE on erps.MATERIALPRICE to FDSTORE_PRDA;
grant INSERT on erps.MATERIALPRICE to FDSTORE_PRDA;
grant SELECT on erps.MATERIALPRICE to FDSTORE_PRDA;
grant UPDATE on erps.MATERIALPRICE to FDSTORE_PRDA;
grant SELECT on erps.MATERIALPRICE to APPDEV;
grant DELETE on erps.MATERIALPRICE to FDSTORE_STSTG01;
grant INSERT on erps.MATERIALPRICE to FDSTORE_STSTG01;
grant SELECT on erps.MATERIALPRICE to FDSTORE_STSTG01;
grant UPDATE on erps.MATERIALPRICE to FDSTORE_STSTG01;
grant SELECT on erps.MATERIALPROXY to DGERRIDGE;
grant DELETE on erps.MATERIALPROXY to FDSTORE_PRDB;
grant INSERT on erps.MATERIALPROXY to FDSTORE_PRDB;
grant SELECT on erps.MATERIALPROXY to FDSTORE_PRDB;
grant UPDATE on erps.MATERIALPROXY to FDSTORE_PRDB;
grant DELETE on erps.MATERIALPROXY to FDSTORE_PRDA;
grant INSERT on erps.MATERIALPROXY to FDSTORE_PRDA;
grant SELECT on erps.MATERIALPROXY to FDSTORE_PRDA;
grant UPDATE on erps.MATERIALPROXY to FDSTORE_PRDA;
grant SELECT on erps.MATERIALPROXY to APPDEV;
grant DELETE on erps.MATERIALPROXY to FDSTORE_STSTG01;
grant INSERT on erps.MATERIALPROXY to FDSTORE_STSTG01;
grant SELECT on erps.MATERIALPROXY to FDSTORE_STSTG01;
grant UPDATE on erps.MATERIALPROXY to FDSTORE_STSTG01;
grant SELECT on erps.MATERIALPROXY_CHARVALUE to DGERRIDGE;
grant DELETE on erps.MATERIALPROXY_CHARVALUE to FDSTORE_PRDB;
grant INSERT on erps.MATERIALPROXY_CHARVALUE to FDSTORE_PRDB;
grant SELECT on erps.MATERIALPROXY_CHARVALUE to FDSTORE_PRDB;
grant UPDATE on erps.MATERIALPROXY_CHARVALUE to FDSTORE_PRDB;
grant DELETE on erps.MATERIALPROXY_CHARVALUE to FDSTORE_PRDA;
grant INSERT on erps.MATERIALPROXY_CHARVALUE to FDSTORE_PRDA;
grant SELECT on erps.MATERIALPROXY_CHARVALUE to FDSTORE_PRDA;
grant UPDATE on erps.MATERIALPROXY_CHARVALUE to FDSTORE_PRDA;
grant SELECT on erps.MATERIALPROXY_CHARVALUE to APPDEV;
grant DELETE on erps.MATERIALPROXY_CHARVALUE to FDSTORE_STSTG01;
grant INSERT on erps.MATERIALPROXY_CHARVALUE to FDSTORE_STSTG01;
grant SELECT on erps.MATERIALPROXY_CHARVALUE to FDSTORE_STSTG01;
grant UPDATE on erps.MATERIALPROXY_CHARVALUE to FDSTORE_STSTG01;
grant SELECT on erps.MATERIALPROXY_SALESUNIT to DGERRIDGE;
grant DELETE on erps.MATERIALPROXY_SALESUNIT to FDSTORE_PRDB;
grant INSERT on erps.MATERIALPROXY_SALESUNIT to FDSTORE_PRDB;
grant SELECT on erps.MATERIALPROXY_SALESUNIT to FDSTORE_PRDB;
grant UPDATE on erps.MATERIALPROXY_SALESUNIT to FDSTORE_PRDB;
grant DELETE on erps.MATERIALPROXY_SALESUNIT to FDSTORE_PRDA;
grant INSERT on erps.MATERIALPROXY_SALESUNIT to FDSTORE_PRDA;
grant SELECT on erps.MATERIALPROXY_SALESUNIT to FDSTORE_PRDA;
grant UPDATE on erps.MATERIALPROXY_SALESUNIT to FDSTORE_PRDA;
grant SELECT on erps.MATERIALPROXY_SALESUNIT to APPDEV;
grant DELETE on erps.MATERIALPROXY_SALESUNIT to FDSTORE_STSTG01;
grant INSERT on erps.MATERIALPROXY_SALESUNIT to FDSTORE_STSTG01;
grant SELECT on erps.MATERIALPROXY_SALESUNIT to FDSTORE_STSTG01;
grant UPDATE on erps.MATERIALPROXY_SALESUNIT to FDSTORE_STSTG01;
grant SELECT on erps.MATERIAL_CLASS to DGERRIDGE;
grant DELETE on erps.MATERIAL_CLASS to FDSTORE_PRDB;
grant INSERT on erps.MATERIAL_CLASS to FDSTORE_PRDB;
grant SELECT on erps.MATERIAL_CLASS to FDSTORE_PRDB;
grant UPDATE on erps.MATERIAL_CLASS to FDSTORE_PRDB;
grant DELETE on erps.MATERIAL_CLASS to FDSTORE_PRDA;
grant INSERT on erps.MATERIAL_CLASS to FDSTORE_PRDA;
grant SELECT on erps.MATERIAL_CLASS to FDSTORE_PRDA;
grant UPDATE on erps.MATERIAL_CLASS to FDSTORE_PRDA;
grant SELECT on erps.MATERIAL_CLASS to APPDEV;
grant DELETE on erps.MATERIAL_CLASS to FDSTORE_STSTG01;
grant INSERT on erps.MATERIAL_CLASS to FDSTORE_STSTG01;
grant SELECT on erps.MATERIAL_CLASS to FDSTORE_STSTG01;
grant UPDATE on erps.MATERIAL_CLASS to FDSTORE_STSTG01;
grant SELECT on erps.CHARACTERISTIC to DGERRIDGE;
grant DELETE on erps.CHARACTERISTIC to FDSTORE_PRDB;
grant INSERT on erps.CHARACTERISTIC to FDSTORE_PRDB;
grant SELECT on erps.CHARACTERISTIC to FDSTORE_PRDB;
grant UPDATE on erps.CHARACTERISTIC to FDSTORE_PRDB;
grant DELETE on erps.CHARACTERISTIC to FDSTORE_PRDA;
grant INSERT on erps.CHARACTERISTIC to FDSTORE_PRDA;
grant SELECT on erps.CHARACTERISTIC to FDSTORE_PRDA;
grant UPDATE on erps.CHARACTERISTIC to FDSTORE_PRDA;
grant SELECT on erps.CHARACTERISTIC to APPDEV;
grant DELETE on erps.CHARACTERISTIC to FDSTORE_STSTG01;
grant INSERT on erps.CHARACTERISTIC to FDSTORE_STSTG01;
grant SELECT on erps.CHARACTERISTIC to FDSTORE_STSTG01;
grant UPDATE on erps.CHARACTERISTIC to FDSTORE_STSTG01;
grant SELECT on erps.CLASS to DGERRIDGE;
grant DELETE on erps.CLASS to FDSTORE_PRDB;
grant INSERT on erps.CLASS to FDSTORE_PRDB;
grant SELECT on erps.CLASS to FDSTORE_PRDB;
grant UPDATE on erps.CLASS to FDSTORE_PRDB;
grant DELETE on erps.CLASS to FDSTORE_PRDA;
grant INSERT on erps.CLASS to FDSTORE_PRDA;
grant SELECT on erps.CLASS to FDSTORE_PRDA;
grant UPDATE on erps.CLASS to FDSTORE_PRDA;
grant SELECT on erps.CLASS to APPDEV;
grant DELETE on erps.CLASS to FDSTORE_STSTG01;
grant INSERT on erps.CLASS to FDSTORE_STSTG01;
grant SELECT on erps.CLASS to FDSTORE_STSTG01;
grant UPDATE on erps.CLASS to FDSTORE_STSTG01;
grant SELECT on erps.HISTORY to DGERRIDGE;
grant DELETE on erps.HISTORY to FDSTORE_PRDB;
grant INSERT on erps.HISTORY to FDSTORE_PRDB;
grant SELECT on erps.HISTORY to FDSTORE_PRDB;
grant UPDATE on erps.HISTORY to FDSTORE_PRDB;
grant DELETE on erps.HISTORY to FDSTORE_PRDA;
grant INSERT on erps.HISTORY to FDSTORE_PRDA;
grant SELECT on erps.HISTORY to FDSTORE_PRDA;
grant UPDATE on erps.HISTORY to FDSTORE_PRDA;
grant SELECT on erps.HISTORY to APPDEV;
grant DELETE on erps.HISTORY to FDSTORE_STSTG01;
grant INSERT on erps.HISTORY to FDSTORE_STSTG01;
grant SELECT on erps.HISTORY to FDSTORE_STSTG01;
grant UPDATE on erps.HISTORY to FDSTORE_STSTG01;
grant SELECT on erps.MATERIAL to DGERRIDGE;
grant DELETE on erps.MATERIAL to FDSTORE_PRDB;
grant INSERT on erps.MATERIAL to FDSTORE_PRDB;
grant SELECT on erps.MATERIAL to FDSTORE_PRDB;
grant UPDATE on erps.MATERIAL to FDSTORE_PRDB;
grant DELETE on erps.MATERIAL to FDSTORE_PRDA;
grant INSERT on erps.MATERIAL to FDSTORE_PRDA;
grant SELECT on erps.MATERIAL to FDSTORE_PRDA;
grant UPDATE on erps.MATERIAL to FDSTORE_PRDA;
grant SELECT on erps.MATERIAL to APPDEV;
grant DELETE on erps.MATERIAL to FDSTORE_STSTG01;
grant INSERT on erps.MATERIAL to FDSTORE_STSTG01;
grant SELECT on erps.MATERIAL to FDSTORE_STSTG01;
grant UPDATE on erps.MATERIAL to FDSTORE_STSTG01;
grant DELETE on erps.PRODUCT to FDSTORE_PRDB;
grant INSERT on erps.PRODUCT to FDSTORE_PRDB;
grant SELECT on erps.PRODUCT to FDSTORE_PRDB;
grant UPDATE on erps.PRODUCT to FDSTORE_PRDB;
grant DELETE on erps.PRODUCT to FDSTORE_PRDA;
grant INSERT on erps.PRODUCT to FDSTORE_PRDA;
grant SELECT on erps.PRODUCT to FDSTORE_PRDA;
grant UPDATE on erps.PRODUCT to FDSTORE_PRDA;
grant DELETE on erps.PRODUCT to APPDEV;
grant INSERT on erps.PRODUCT to APPDEV;
grant SELECT on erps.PRODUCT to APPDEV;
grant UPDATE on erps.PRODUCT to APPDEV;
grant DELETE on erps.PRODUCT to FDSTORE_STSTG01;
grant INSERT on erps.PRODUCT to FDSTORE_STSTG01;
grant SELECT on erps.PRODUCT to FDSTORE_STSTG01;
grant UPDATE on erps.PRODUCT to FDSTORE_STSTG01;
grant DELETE on erps.SALESUNIT to FDSTORE_PRDB;
grant INSERT on erps.SALESUNIT to FDSTORE_PRDB;
grant SELECT on erps.SALESUNIT to FDSTORE_PRDB;
grant UPDATE on erps.SALESUNIT to FDSTORE_PRDB;
grant DELETE on erps.SALESUNIT to FDSTORE_PRDA;
grant INSERT on erps.SALESUNIT to FDSTORE_PRDA;
grant SELECT on erps.SALESUNIT to FDSTORE_PRDA;
grant UPDATE on erps.SALESUNIT to FDSTORE_PRDA;
grant DELETE on erps.SALESUNIT to APPDEV;
grant INSERT on erps.SALESUNIT to APPDEV;
grant SELECT on erps.SALESUNIT to APPDEV;
grant UPDATE on erps.SALESUNIT to APPDEV;
grant DELETE on erps.SALESUNIT to FDSTORE_STSTG01;
grant INSERT on erps.SALESUNIT to FDSTORE_STSTG01;
grant SELECT on erps.SALESUNIT to FDSTORE_STSTG01;
grant UPDATE on erps.SALESUNIT to FDSTORE_STSTG01;



-- step3 

create snapshot PRICING_REGION refresh force start with sysdate next sysdate+1/144
as SELECT * FROM "PRICING_REGION"@DBSTO.NYC.FRESHDIRECT.COM "PRICING_REGION" ;

create snapshot PRICING_REGION_ZIPS refresh force start with sysdate next sysdate+1/144
as SELECT * FROM "PRICING_REGION_ZIPS"@DBSTO.NYC.FRESHDIRECT.COM "PRICING_REGION_ZIPS" ;

create snapshot PRICING_ZONE refresh force start with sysdate next sysdate+1/144
as SELECT * FROM "PRICING_ZONE"@DBSTO.NYC.FRESHDIRECT.COM "PRICING_ZONE" ;

create snapshot ZONE_HISTORY refresh force start with sysdate next sysdate+1/144
as SELECT * FROM "ZONE_HISTORY"@DBSTO.NYC.FRESHDIRECT.COM "ZONE_HISTORY" ;

-- check indexes, they're there,
-- grant privileges

 
-- ak. no user called FDSTORE, so following are commented.
--grant select on erps.PRICING_REGION to  FDSTORE;
--grant select on erps.PRICING_REGION_ZIPS to  FDSTORE;grant SELECT on erps.PRICING_REGION to DGERRIDGE;
--grant select on erps.PRICING_ZONE to  FDSTORE;grant DELETE on erps.PRICING_REGION to FDSTORE_PRDB;
--grant select on erps.ZONE_HISTORY to  FDSTORE;grant INSERT on erps.PRICING_REGION to FDSTORE_PRDB;


-- ak - added more grants as per grants in previous snapshots

grant SELECT on erps.PRICING_REGION to DGERRIDGE;
grant SELECT on erps.PRICING_REGION to FDSTORE_PRDB;
grant UPDATE on erps.PRICING_REGION to FDSTORE_PRDB;
grant DELETE on erps.PRICING_REGION to FDSTORE_PRDA;
grant INSERT on erps.PRICING_REGION to FDSTORE_PRDA;
grant SELECT on erps.PRICING_REGION to FDSTORE_PRDA;
grant UPDATE on erps.PRICING_REGION to FDSTORE_PRDA;
grant SELECT on erps.PRICING_REGION to APPDEV;
grant DELETE on erps.PRICING_REGION to FDSTORE_STSTG01;
grant INSERT on erps.PRICING_REGION to FDSTORE_STSTG01;
grant SELECT on erps.PRICING_REGION to FDSTORE_STSTG01;
grant UPDATE on erps.PRICING_REGION to FDSTORE_STSTG01;

grant SELECT on erps.PRICING_REGION_ZIPS to DGERRIDGE;
grant SELECT on erps.PRICING_REGION_ZIPS to FDSTORE_PRDB;
grant UPDATE on erps.PRICING_REGION_ZIPS to FDSTORE_PRDB;
grant DELETE on erps.PRICING_REGION_ZIPS to FDSTORE_PRDA;
grant INSERT on erps.PRICING_REGION_ZIPS to FDSTORE_PRDA;
grant SELECT on erps.PRICING_REGION_ZIPS to FDSTORE_PRDA;
grant UPDATE on erps.PRICING_REGION_ZIPS to FDSTORE_PRDA;
grant SELECT on erps.PRICING_REGION_ZIPS to APPDEV;
grant DELETE on erps.PRICING_REGION_ZIPS to FDSTORE_STSTG01;
grant INSERT on erps.PRICING_REGION_ZIPS to FDSTORE_STSTG01;
grant SELECT on erps.PRICING_REGION_ZIPS to FDSTORE_STSTG01;
grant UPDATE on erps.PRICING_REGION_ZIPS to FDSTORE_STSTG01;

grant SELECT on erps.PRICING_ZONE to DGERRIDGE;
grant SELECT on erps.PRICING_ZONE to FDSTORE_PRDB;
grant UPDATE on erps.PRICING_ZONE to FDSTORE_PRDB;
grant DELETE on erps.PRICING_ZONE to FDSTORE_PRDA;
grant INSERT on erps.PRICING_ZONE to FDSTORE_PRDA;
grant SELECT on erps.PRICING_ZONE to FDSTORE_PRDA;
grant UPDATE on erps.PRICING_ZONE to FDSTORE_PRDA;
grant SELECT on erps.PRICING_ZONE to APPDEV;
grant DELETE on erps.PRICING_ZONE to FDSTORE_STSTG01;
grant INSERT on erps.PRICING_ZONE to FDSTORE_STSTG01;
grant SELECT on erps.PRICING_ZONE to FDSTORE_STSTG01;
grant UPDATE on erps.PRICING_ZONE to FDSTORE_STSTG01;

grant SELECT on erps.ZONE_HISTORY to DGERRIDGE;
grant SELECT on erps.ZONE_HISTORY to FDSTORE_PRDB;
grant UPDATE on erps.ZONE_HISTORY to FDSTORE_PRDB;
grant DELETE on erps.ZONE_HISTORY to FDSTORE_PRDA;
grant INSERT on erps.ZONE_HISTORY to FDSTORE_PRDA;
grant SELECT on erps.ZONE_HISTORY to FDSTORE_PRDA;
grant UPDATE on erps.ZONE_HISTORY to FDSTORE_PRDA;
grant SELECT on erps.ZONE_HISTORY to APPDEV;
grant DELETE on erps.ZONE_HISTORY to FDSTORE_STSTG01;
grant INSERT on erps.ZONE_HISTORY to FDSTORE_STSTG01;
grant SELECT on erps.ZONE_HISTORY to FDSTORE_STSTG01;
grant UPDATE on erps.ZONE_HISTORY to FDSTORE_STSTG01;

