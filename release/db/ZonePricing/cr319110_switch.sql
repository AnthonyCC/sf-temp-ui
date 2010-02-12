-- switc index to old
select 'alter index '||index_name||' rename to '||index_name||'_old;' from dba_indexes where owner='ERPS'
and table_name in ('CHARACTERISTIC'
,'CHARVALUE'
,'CHARVALUEPRICE'
,'CLASS'
,'HISTORY'
,'MATERIAL'
,'MATERIALPRICE'
,'MATERIALPROXY'
,'MATERIALPROXY_CHARVALUE'
,'MATERIALPROXY_SALESUNIT'
,'MATERIAL_CLASS'
,'PRODUCT'
,'SALESUNIT');
alter index PK_CHR rename to PK_CHR_old;
alter index IDX_CHR_CLID rename to IDX_CHR_CLID_old;
alter index PK_CHV rename to PK_CHV_old;
alter index IDX_CHV_CHID rename to IDX_CHV_CHID_old;
alter index PK_CVP rename to PK_CVP_old;
alter index IDX_CVP_MID rename to IDX_CVP_MID_old;
alter index CHARVALUEPRICE_IDX_017 rename to CHARVALUEPRICE_IDX_017_old;
alter index PK_CLS rename to PK_CLS_old;
alter index PK_HST rename to PK_HST_old;
alter index HISTORY_VERSION_DATE_IDX rename to HISTORY_VERSION_DATE_IDX_old;
alter index PK_MTL rename to PK_MTL_old;
alter index MATERIAL_IDX_002 rename to MATERIAL_IDX_002_old;
alter index IDX_MATL_SAPID_VER rename to IDX_MATL_SAPID_VER_old;
alter index PK_MPR rename to PK_MPR_old;
alter index IDX_MPR_MID rename to IDX_MPR_MID_old;
alter index PK_MPX rename to PK_MPX_old;
alter index IDX_MPX_PID rename to IDX_MPX_PID_old;
alter index IDX_MPX_MID rename to IDX_MPX_MID_old;
alter index PK_MPXCHV rename to PK_MPXCHV_old;
alter index MATERIALPROXY_CHARVA_IDX_016 rename to MATERIALPROXY_CHARVA_IDX_016_old;
alter index PK_MPX_SLU rename to PK_MPX_SLU_old;
alter index MATERIALPROXY_SALESU_IDX_015 rename to MATERIALPROXY_SALESU_IDX_015_old;
alter index PK_MTLCLS rename to PK_MTLCLS_old;
alter index MATERIAL_CLASS_IDX_014 rename to MATERIAL_CLASS_IDX_014_old;
alter index PRODUCT_IDX_001 rename to PRODUCT_IDX_001_old;
alter index PRODUCT_VERSION_SKU_AVAIL_IDX rename to PRODUCT_VERSION_SKU_AVAIL_IDX_old;
alter index PRODUCT_SKU_AVAIL_VERSION_IDX rename to PRODUCT_SKU_AVAIL_VERSION_IDX_old;
alter index IDX_PRD_SKU_VER_UNV rename to IDX_PRD_SKU_VER_UNV_old;
alter index PK_PRD rename to PK_PRD_old;
alter index IDX_PRD_SKU_VER rename to IDX_PRD_SKU_VER_old;
alter index PK_SLU rename to PK_SLU_old;
alter index IDX_SLU_MID rename to IDX_SLU_MID_old;
--there're 4 indexes name too long 
alter index MATERIALPROXY_CHARVA_IDX_016 rename to MCI_016_old;
alter index MATERIALPROXY_SALESU_IDX_015 rename to MSI_015_old;
alter index PRODUCT_VERSION_SKU_AVAIL_IDX rename to PVSAI_old;
alter index PRODUCT_SKU_AVAIL_VERSION_IDX rename to PSAVI_old;


-- switch non-C constraints to old , 
-- those not null constraints were taken care of by creation scripts
select 'alter table '||table_name||' rename constraint '||constraint_name||' to '||constraint_name||'_old;' from dba_constraints
where owner='ERPS'
and constraint_type<>'C'
and table_name in ('CHARACTERISTIC'
,'CHARVALUE'
,'CHARVALUEPRICE'
,'CLASS'
,'HISTORY'
,'MATERIAL'
,'MATERIALPRICE'
,'MATERIALPROXY'
,'MATERIALPROXY_CHARVALUE'
,'MATERIALPROXY_SALESUNIT'
,'MATERIAL_CLASS'
,'PRODUCT'
,'SALESUNIT');
alter table MATERIALPRICE rename constraint PK_MPR to PK_MPR_old;
alter table CHARACTERISTIC rename constraint PK_CHR to PK_CHR_old;
alter table CHARVALUE rename constraint PK_CHV to PK_CHV_old;
alter table CHARVALUEPRICE rename constraint PK_CVP to PK_CVP_old;
alter table CLASS rename constraint PK_CLS to PK_CLS_old;
alter table HISTORY rename constraint PK_HST to PK_HST_old;
alter table MATERIAL rename constraint PK_MTL to PK_MTL_old;
alter table MATERIALPROXY rename constraint PK_MPX to PK_MPX_old;
alter table MATERIALPROXY_CHARVALUE rename constraint PK_MPXCHV to PK_MPXCHV_old;
alter table MATERIALPROXY_SALESUNIT rename constraint PK_MPX_SLU to PK_MPX_SLU_old;
alter table MATERIAL_CLASS rename constraint PK_MTLCLS to PK_MTLCLS_old;
alter table PRODUCT rename constraint PK_PRD to PK_PRD_old;
alter table SALESUNIT rename constraint PK_SLU to PK_SLU_old;
alter table CHARACTERISTIC rename constraint CHR_CLS_FK to CHR_CLS_FK_old;
alter table CHARVALUE rename constraint CHV_CHR_FK to CHV_CHR_FK_old;
alter table CHARVALUEPRICE rename constraint CVP_MTL_FK to CVP_MTL_FK_old;
alter table CHARVALUEPRICE rename constraint CVP_CHV_FK to CVP_CHV_FK_old;
alter table MATERIALPROXY rename constraint MPX_PRD_FK to MPX_PRD_FK_old;
alter table MATERIALPROXY rename constraint MPX_MTL_FK to MPX_MTL_FK_old;
alter table MATERIALPROXY_CHARVALUE rename constraint MPXCHV_MPX_FK to MPXCHV_MPX_FK_old;
alter table MATERIALPROXY_CHARVALUE rename constraint MPXCHV_CHV_FK to MPXCHV_CHV_FK_old;
alter table MATERIALPROXY_SALESUNIT rename constraint MPXSLU_SLU_FK to MPXSLU_SLU_FK_old;
alter table MATERIALPROXY_SALESUNIT rename constraint MPXSLU_MPX_FK to MPXSLU_MPX_FK_old;
alter table MATERIAL_CLASS rename constraint MTLCLS_MTL_FK to MTLCLS_MTL_FK_old;
alter table MATERIAL_CLASS rename constraint MTLCLS_CLS_FK to MTLCLS_CLS_FK_old;
alter table PRODUCT rename constraint PRD_HST_FK to PRD_HST_FK_old;
alter table SALESUNIT rename constraint SLU_MTL_FK to SLU_MTL_FK_old;
alter table MATERIALPRICE rename constraint MPR_MTL_FK to MPR_MTL_FK_old;

-- drop snapshot logs 
select 'drop snapshot log on  '||table_name||';' from dba_tables
where owner='ERPS'
and table_name in ('CHARACTERISTIC'
,'CHARVALUE'
,'CHARVALUEPRICE'
,'CLASS'
,'HISTORY'
,'MATERIAL'
,'MATERIALPRICE'
,'MATERIALPROXY'
,'MATERIALPROXY_CHARVALUE'
,'MATERIALPROXY_SALESUNIT'
,'MATERIAL_CLASS'
,'PRODUCT'
,'SALESUNIT');
drop snapshot log on  SALESUNIT;
drop snapshot log on  PRODUCT;
drop snapshot log on  MATERIAL_CLASS;
drop snapshot log on  MATERIALPROXY_SALESUNIT;
drop snapshot log on  MATERIALPROXY_CHARVALUE;
drop snapshot log on  MATERIALPROXY;
drop snapshot log on  MATERIALPRICE;
drop snapshot log on  MATERIAL;
drop snapshot log on  HISTORY;
drop snapshot log on  CLASS;
drop snapshot log on  CHARVALUEPRICE;
drop snapshot log on  CHARVALUE;
drop snapshot log on  CHARACTERISTIC;


-- switch table_name  to old
select 'rename '||table_name||' to '||table_name||'_old;' from dba_tables
where owner='ERPS'
and table_name in ('CHARACTERISTIC'
,'CHARVALUE'
,'CHARVALUEPRICE'
,'CLASS'
,'HISTORY'
,'MATERIAL'
,'MATERIALPRICE'
,'MATERIALPROXY'
,'MATERIALPROXY_CHARVALUE'
,'MATERIALPROXY_SALESUNIT'
,'MATERIAL_CLASS'
,'PRODUCT'
,'SALESUNIT');
rename SALESUNIT to SALESUNIT_old;
rename PRODUCT to PRODUCT_old;
rename MATERIAL_CLASS to MATERIAL_CLASS_old;
rename MATERIALPROXY_SALESUNIT to MATERIALPROXY_SALESUNIT_old;
rename MATERIALPROXY_CHARVALUE to MATERIALPROXY_CHARVALUE_old;
rename MATERIALPROXY to MATERIALPROXY_old;
rename MATERIALPRICE to MATERIALPRICE_old;
rename MATERIAL to MATERIAL_old;
rename HISTORY to HISTORY_old;
rename CLASS to CLASS_old;
rename CHARVALUEPRICE to CHARVALUEPRICE_old;
rename CHARVALUE to CHARVALUE_old;
rename CHARACTERISTIC to CHARACTERISTIC_old;
-- error : ORA-26563: renaming this table is not allowed


-- switch table_name_new to table_name ;
rename SALESUNIT_new to SALESUNIT;
rename PRODUCT_new to PRODUCT;
rename MATERIAL_CLASS_new to MATERIAL_CLASS;
rename MATERIALPROXY_SALESUNIT_new to MATERIALPROXY_SALESUNIT;
rename MATERIALPROXY_CHARVALUE_new to MATERIALPROXY_CHARVALUE;
rename MATERIALPROXY_new to MATERIALPROXY;
rename MATERIALPRICE_new to MATERIALPRICE;
rename MATERIAL_new to MATERIAL;
rename HISTORY_new to HISTORY;
rename CLASS_new to CLASS;
rename CHARVALUEPRICE_new to CHARVALUEPRICE;
rename CHARVALUE_new to CHARVALUE;
rename CHARACTERISTIC_new to CHARACTERISTIC;

-- switch index_name_new to index_name;
alter index PK_CHR_new rename to PK_CHR;
alter index IDX_CHR_CLID_new rename to IDX_CHR_CLID;
alter index PK_CHV_new rename to PK_CHV;
alter index IDX_CHV_CHID_new rename to IDX_CHV_CHID;
alter index PK_CVP_new rename to PK_CVP;
alter index IDX_CVP_MID_new rename to IDX_CVP_MID;
alter index CHARVALUEPRICE_IDX_017_new rename to CHARVALUEPRICE_IDX_017;
alter index PK_CLS_new rename to PK_CLS;
alter index PK_HST_new rename to PK_HST;
alter index HISTORY_VERSION_DATE_IDX_new rename to HISTORY_VERSION_DATE_IDX;
alter index PK_MTL_new rename to PK_MTL;
alter index MATERIAL_IDX_002_new rename to MATERIAL_IDX_002;
alter index IDX_MATL_SAPID_VER_new rename to IDX_MATL_SAPID_VER;
alter index PK_MPR_new rename to PK_MPR;
alter index IDX_MPR_MID_new rename to IDX_MPR_MID;
alter index PK_MPX_new rename to PK_MPX;
alter index IDX_MPX_PID_new rename to IDX_MPX_PID;
alter index IDX_MPX_MID_new rename to IDX_MPX_MID;
alter index PK_MPXCHV_new rename to PK_MPXCHV;
alter index PK_MPX_SLU_new rename to PK_MPX_SLU;
alter index PK_MTLCLS_new rename to PK_MTLCLS;
alter index MATERIAL_CLASS_IDX_014_new rename to MATERIAL_CLASS_IDX_014;
alter index PRODUCT_IDX_001_new rename to PRODUCT_IDX_001;
alter index IDX_PRD_SKU_VER_UNV_new rename to IDX_PRD_SKU_VER_UNV;
alter index PK_PRD_new rename to PK_PRD;
alter index IDX_PRD_SKU_VER_new rename to IDX_PRD_SKU_VER;
alter index PK_SLU_new rename to PK_SLU;
alter index IDX_SLU_MID_new rename to IDX_SLU_MID;
--alter index MATERIALPROXY_CHARVA_IDX_016_new rename to MATERIALPROXY_CHARVA_IDX_016;
--alter index MATERIALPROXY_SALESU_IDX_015_new rename to MATERIALPROXY_SALESU_IDX_015;
--alter index PRODUCT_VERSION_SKU_AVAIL_IDX_new rename to PRODUCT_VERSION_SKU_AVAIL_IDX;
--alter index PRODUCT_SKU_AVAIL_VERSION_IDX_new rename to PRODUCT_SKU_AVAIL_VERSION_IDX;
alter index MCI_016_new rename to MCI_016_016;
alter index MSI_015_new rename to MSI_015;
alter index PVSAI_new rename to PVSAI;
alter index PSAVI_new rename to PSAVI;


-- switch constraint_name_new to constraint_name
alter table MATERIALPRICE rename constraint PK_MPR_new to PK_MPR;
alter table CHARACTERISTIC rename constraint PK_CHR_new to PK_CHR;
alter table CHARVALUE rename constraint PK_CHV_new to PK_CHV;
alter table CHARVALUEPRICE rename constraint PK_CVP_new to PK_CVP;
alter table CLASS rename constraint PK_CLS_new to PK_CLS;
alter table HISTORY rename constraint PK_HST_new to PK_HST;
alter table MATERIAL rename constraint PK_MTL_new to PK_MTL;
alter table MATERIALPROXY rename constraint PK_MPX_new to PK_MPX;
alter table MATERIALPROXY_CHARVALUE rename constraint PK_MPXCHV_new to PK_MPXCHV;
alter table MATERIALPROXY_SALESUNIT rename constraint PK_MPX_SLU_new to PK_MPX_SLU;
alter table MATERIAL_CLASS rename constraint PK_MTLCLS_new to PK_MTLCLS;
alter table PRODUCT rename constraint PK_PRD_new to PK_PRD;
alter table SALESUNIT rename constraint PK_SLU_new to PK_SLU;
alter table CHARACTERISTIC rename constraint CHR_CLS_FK_new to CHR_CLS_FK;
alter table CHARVALUE rename constraint CHV_CHR_FK_new to CHV_CHR_FK;
alter table CHARVALUEPRICE rename constraint CVP_MTL_FK_new to CVP_MTL_FK;
alter table CHARVALUEPRICE rename constraint CVP_CHV_FK_new to CVP_CHV_FK;
alter table MATERIALPROXY rename constraint MPX_PRD_FK_new to MPX_PRD_FK;
alter table MATERIALPROXY rename constraint MPX_MTL_FK_new to MPX_MTL_FK;
alter table MATERIALPROXY_CHARVALUE rename constraint MPXCHV_MPX_FK_new to MPXCHV_MPX_FK;
alter table MATERIALPROXY_CHARVALUE rename constraint MPXCHV_CHV_FK_new to MPXCHV_CHV_FK;
alter table MATERIALPROXY_SALESUNIT rename constraint MPXSLU_SLU_FK_new to MPXSLU_SLU_FK;
alter table MATERIALPROXY_SALESUNIT rename constraint MPXSLU_MPX_FK_new to MPXSLU_MPX_FK;
alter table MATERIAL_CLASS rename constraint MTLCLS_MTL_FK_new to MTLCLS_MTL_FK;
alter table MATERIAL_CLASS rename constraint MTLCLS_CLS_FK_new to MTLCLS_CLS_FK;
alter table PRODUCT rename constraint PRD_HST_FK_new to PRD_HST_FK;
alter table SALESUNIT rename constraint SLU_MTL_FK_new to SLU_MTL_FK;
alter table MATERIALPRICE rename constraint MPR_MTL_FK_new to MPR_MTL_FK;

-- recreate snapshot log 
select 'create snapshot log on  '||table_name||';' from dba_tables
where owner='ERPS'
and table_name in ('CHARACTERISTIC'
,'CHARVALUE'
,'CHARVALUEPRICE'
,'CLASS'
,'HISTORY'
,'MATERIAL'
,'MATERIALPRICE'
,'MATERIALPROXY'
,'MATERIALPROXY_CHARVALUE'
,'MATERIALPROXY_SALESUNIT'
,'MATERIAL_CLASS'
,'PRODUCT'
,'SALESUNIT');
create snapshot log on  SALESUNIT;
create snapshot log on  PRODUCT;
create snapshot log on  MATERIAL_CLASS;
create snapshot log on  MATERIALPROXY_SALESUNIT;
create snapshot log on  MATERIALPROXY_CHARVALUE;
create snapshot log on  MATERIALPROXY;
create snapshot log on  MATERIALPRICE;
create snapshot log on  MATERIAL;
create snapshot log on  HISTORY;
create snapshot log on  CLASS;
create snapshot log on  CHARVALUEPRICE;
create snapshot log on  CHARVALUE;
create snapshot log on  CHARACTERISTIC;


-- save privileges
spool grant_privs.sql
select 'grant '|| privilege ||' on erps.'||table_name|| ' to '||grantee||';' from dba_tab_privs
where owner = 'ERPS' and table_name in ('CHARACTERISTIC'
,'CHARVALUE'
,'CHARVALUEPRICE'
,'CLASS'
,'HISTORY'
,'MATERIAL'
,'MATERIALPRICE'
,'MATERIALPROXY'
,'MATERIALPROXY_CHARVALUE'
,'MATERIALPROXY_SALESUNIT'
,'MATERIAL_CLASS'
,'PRODUCT'
,'SALESUNIT') ;
spool off
@grant_privs.sql




