exec dbms_stats.gather_table_stats(ownname=>'ERPS',tabname=>'CLASS',estimate_percent => 30, cascade=>true);
exec dbms_stats.gather_table_stats(ownname=>'ERPS',tabname=>'CHARACTERISTIC',estimate_percent => 30, cascade=>true);
exec dbms_stats.gather_table_stats(ownname=>'ERPS',tabname=>'CHARVALUE',estimate_percent => 30, cascade=>true);
exec dbms_stats.gather_table_stats(ownname=>'ERPS',tabname=>'MATERIAL',estimate_percent => 30, cascade=>true);
exec dbms_stats.gather_table_stats(ownname=>'ERPS',tabname=>'CHARVALUEPRICE',estimate_percent => 30, cascade=>true);
exec dbms_stats.gather_table_stats(ownname=>'ERPS',tabname=>'HISTORY',estimate_percent => 30, cascade=>true);
exec dbms_stats.gather_table_stats(ownname=>'ERPS',tabname=>'MATERIALPRICE',estimate_percent => 30, cascade=>true);
exec dbms_stats.gather_table_stats(ownname=>'ERPS',tabname=>'PRODUCT',estimate_percent => 30, cascade=>true);
exec dbms_stats.gather_table_stats(ownname=>'ERPS',tabname=>'MATERIALPROXY',estimate_percent => 30, cascade=>true);
exec dbms_stats.gather_table_stats(ownname=>'ERPS',tabname=>'MATERIALPROXY_CHARVALUE',estimate_percent => 30, cascade=>true);
exec dbms_stats.gather_table_stats(ownname=>'ERPS',tabname=>'SALESUNIT',estimate_percent => 30, cascade=>true);
exec dbms_stats.gather_table_stats(ownname=>'ERPS',tabname=>'MATERIALPROXY_SALESUNIT',estimate_percent => 30, cascade=>true);
exec dbms_stats.gather_table_stats(ownname=>'ERPS',tabname=>'MATERIAL_CLASS',estimate_percent => 30, cascade=>true);
-- new tables
exec dbms_stats.gather_table_stats(ownname=>'ERPS',tabname=>'PRICING_ZONE',estimate_percent => 30, cascade=>true);
exec dbms_stats.gather_table_stats(ownname=>'ERPS',tabname=>'PRICING_REGION',estimate_percent => 30, cascade=>true);
exec dbms_stats.gather_table_stats(ownname=>'ERPS',tabname=>'PRICING_REGION_ZIPS',estimate_percent => 30, cascade=>true);
exec dbms_stats.gather_table_stats(ownname=>'ERPS',tabname=>'ZONE_HISTORY',estimate_percent => 30, cascade=>true);
