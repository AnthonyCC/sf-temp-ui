-- rename index
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
--alter index MATERIALPROXY_CHARVA_IDX_016 rename to MATERIALPROXY_CHARVA_IDX_016_old;
alter index PK_MPX_SLU rename to PK_MPX_SLU_old;
--alter index MATERIALPROXY_SALESU_IDX_015 rename to MATERIALPROXY_SALESU_IDX_015_old;
alter index PK_MTLCLS rename to PK_MTLCLS_old;
alter index MATERIAL_CLASS_IDX_014 rename to MATERIAL_CLASS_IDX_014_old;
alter index PRODUCT_IDX_001 rename to PRODUCT_IDX_001_old;
--alter index PRODUCT_VERSION_SKU_AVAIL_IDX rename to PRODUCT_VERSION_SKU_AVAIL_IDX_old;
--alter index PRODUCT_SKU_AVAIL_VERSION_IDX rename to PRODUCT_SKU_AVAIL_VERSION_IDX_old;
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

-- rename constraints 
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

-- rename table 
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

-- rename index 
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

-- rename constraints 
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

-- grant privileges 
grant DELETE on erps.SALESUNIT to FDSTORE_STPRD01;
grant DELETE on erps.SALESUNIT to FDSTORE_PRDB;
grant DELETE on erps.SALESUNIT to FDSTORE_PRDA;
grant DELETE on erps.PRODUCT to FDSTORE_STPRD01;
grant DELETE on erps.PRODUCT to FDSTORE_PRDB;
grant DELETE on erps.PRODUCT to FDSTORE_PRDA;
grant DELETE on erps.MATERIAL_CLASS to FDSTORE_STPRD01;
grant DELETE on erps.MATERIAL_CLASS to FDSTORE_PRDB;
grant DELETE on erps.MATERIAL_CLASS to FDSTORE_PRDA;
grant DELETE on erps.MATERIALPROXY_SALESUNIT to FDSTORE_STPRD01;
grant DELETE on erps.MATERIALPROXY_SALESUNIT to FDSTORE_PRDB;
grant DELETE on erps.MATERIALPROXY_SALESUNIT to FDSTORE_PRDA;
grant DELETE on erps.MATERIALPROXY_CHARVALUE to FDSTORE_STPRD01;
grant DELETE on erps.MATERIALPROXY_CHARVALUE to FDSTORE_PRDB;
grant DELETE on erps.MATERIALPROXY_CHARVALUE to FDSTORE_PRDA;
grant DELETE on erps.MATERIALPROXY to FDSTORE_STPRD01;
grant DELETE on erps.MATERIALPROXY to FDSTORE_PRDB;
grant DELETE on erps.MATERIALPROXY to FDSTORE_PRDA;
grant DELETE on erps.MATERIALPRICE to FDSTORE_STPRD01;
grant DELETE on erps.MATERIALPRICE to FDSTORE_PRDB;
grant DELETE on erps.MATERIALPRICE to FDSTORE_PRDA;
grant DELETE on erps.MATERIAL to FDSTORE_STPRD01;
grant DELETE on erps.MATERIAL to FDSTORE_PRDB;
grant DELETE on erps.MATERIAL to FDSTORE_PRDA;
grant DELETE on erps.HISTORY to FDSTORE_STPRD01;
grant DELETE on erps.HISTORY to FDSTORE_PRDB;
grant DELETE on erps.HISTORY to FDSTORE_PRDA;
grant DELETE on erps.CLASS to FDSTORE_STPRD01;
grant DELETE on erps.CLASS to FDSTORE_PRDB;
grant DELETE on erps.CLASS to FDSTORE_PRDA;
grant DELETE on erps.CHARVALUEPRICE to FDSTORE_STPRD01;
grant DELETE on erps.CHARVALUEPRICE to FDSTORE_PRDB;
grant DELETE on erps.CHARVALUEPRICE to FDSTORE_PRDA;
grant DELETE on erps.CHARVALUE to FDSTORE_STPRD01;
grant DELETE on erps.CHARVALUE to FDSTORE_PRDB;
grant DELETE on erps.CHARVALUE to FDSTORE_PRDA;
grant DELETE on erps.CHARACTERISTIC to FDSTORE_STPRD01;
grant DELETE on erps.CHARACTERISTIC to FDSTORE_PRDB;
grant DELETE on erps.CHARACTERISTIC to FDSTORE_PRDA;
grant INSERT on erps.SALESUNIT to FDSTORE_STPRD01;
grant INSERT on erps.SALESUNIT to FDSTORE_PRDB;
grant INSERT on erps.SALESUNIT to FDSTORE_PRDA;
grant INSERT on erps.PRODUCT to FDSTORE_STPRD01;
grant INSERT on erps.PRODUCT to FDSTORE_PRDB;
grant INSERT on erps.PRODUCT to FDSTORE_PRDA;
grant INSERT on erps.MATERIAL_CLASS to FDSTORE_STPRD01;
grant INSERT on erps.MATERIAL_CLASS to FDSTORE_PRDB;
grant INSERT on erps.MATERIAL_CLASS to FDSTORE_PRDA;
grant INSERT on erps.MATERIALPROXY_SALESUNIT to FDSTORE_STPRD01;
grant INSERT on erps.MATERIALPROXY_SALESUNIT to FDSTORE_PRDB;
grant INSERT on erps.MATERIALPROXY_SALESUNIT to FDSTORE_PRDA;
grant INSERT on erps.MATERIALPROXY_CHARVALUE to FDSTORE_STPRD01;
grant INSERT on erps.MATERIALPROXY_CHARVALUE to FDSTORE_PRDB;
grant INSERT on erps.MATERIALPROXY_CHARVALUE to FDSTORE_PRDA;
grant INSERT on erps.MATERIALPROXY to FDSTORE_STPRD01;
grant INSERT on erps.MATERIALPROXY to FDSTORE_PRDB;
grant INSERT on erps.MATERIALPROXY to FDSTORE_PRDA;
grant INSERT on erps.MATERIALPRICE to FDSTORE_STPRD01;
grant INSERT on erps.MATERIALPRICE to FDSTORE_PRDB;
grant INSERT on erps.MATERIALPRICE to FDSTORE_PRDA;
grant INSERT on erps.MATERIAL to FDSTORE_STPRD01;
grant INSERT on erps.MATERIAL to FDSTORE_PRDB;
grant INSERT on erps.MATERIAL to FDSTORE_PRDA;
grant INSERT on erps.HISTORY to FDSTORE_STPRD01;
grant INSERT on erps.HISTORY to FDSTORE_PRDB;
grant INSERT on erps.HISTORY to FDSTORE_PRDA;
grant INSERT on erps.CLASS to FDSTORE_STPRD01;
grant INSERT on erps.CLASS to FDSTORE_PRDB;
grant INSERT on erps.CLASS to FDSTORE_PRDA;
grant INSERT on erps.CHARVALUEPRICE to FDSTORE_STPRD01;
grant INSERT on erps.CHARVALUEPRICE to FDSTORE_PRDB;
grant INSERT on erps.CHARVALUEPRICE to FDSTORE_PRDA;
grant INSERT on erps.CHARVALUE to FDSTORE_STPRD01;
grant INSERT on erps.CHARVALUE to FDSTORE_PRDB;
grant INSERT on erps.CHARVALUE to FDSTORE_PRDA;
grant INSERT on erps.CHARACTERISTIC to FDSTORE_STPRD01;
grant INSERT on erps.CHARACTERISTIC to FDSTORE_PRDB;
grant INSERT on erps.CHARACTERISTIC to FDSTORE_PRDA;
grant SELECT on erps.SALESUNIT to FDSTORE_STPRD01;
grant SELECT on erps.SALESUNIT to CSSI;
grant SELECT on erps.SALESUNIT to DWHUSER_ROLE;
grant SELECT on erps.SALESUNIT to APPDEV_ROLE;
grant SELECT on erps.SALESUNIT to FDSTORE_PRDB;
grant SELECT on erps.SALESUNIT to FDSTORE_PRDA;
grant SELECT on erps.SALESUNIT to MYLES;
grant SELECT on erps.SALESUNIT to DWUSER;
grant SELECT on erps.SALESUNIT to PROD_RO;
grant SELECT on erps.SALESUNIT to STORE_READ;
grant SELECT on erps.PRODUCT to FDSTORE_STPRD01;
grant SELECT on erps.PRODUCT to CSSI;
grant SELECT on erps.PRODUCT to DWHUSER_ROLE;
grant SELECT on erps.PRODUCT to APPDEV_ROLE;
grant SELECT on erps.PRODUCT to CUST;
grant SELECT on erps.PRODUCT to FDSTORE_PRDB;
grant SELECT on erps.PRODUCT to FDSTORE_PRDA;
grant SELECT on erps.PRODUCT to MYLES;
grant SELECT on erps.PRODUCT to DWUSER;
grant SELECT on erps.PRODUCT to PROD_RO;
grant SELECT on erps.PRODUCT to STORE_READ;
grant SELECT on erps.MATERIAL_CLASS to FDSTORE_STPRD01;
grant SELECT on erps.MATERIAL_CLASS to CSSI;
grant SELECT on erps.MATERIAL_CLASS to DWHUSER_ROLE;
grant SELECT on erps.MATERIAL_CLASS to APPDEV_ROLE;
grant SELECT on erps.MATERIAL_CLASS to FDSTORE_PRDB;
grant SELECT on erps.MATERIAL_CLASS to FDSTORE_PRDA;
grant SELECT on erps.MATERIAL_CLASS to MYLES;
grant SELECT on erps.MATERIAL_CLASS to DWUSER;
grant SELECT on erps.MATERIAL_CLASS to PROD_RO;
grant SELECT on erps.MATERIAL_CLASS to STORE_READ;
grant SELECT on erps.MATERIALPROXY_SALESUNIT to FDSTORE_STPRD01;
grant SELECT on erps.MATERIALPROXY_SALESUNIT to CSSI;
grant SELECT on erps.MATERIALPROXY_SALESUNIT to DWHUSER_ROLE;
grant SELECT on erps.MATERIALPROXY_SALESUNIT to APPDEV_ROLE;
grant SELECT on erps.MATERIALPROXY_SALESUNIT to FDSTORE_PRDB;
grant SELECT on erps.MATERIALPROXY_SALESUNIT to FDSTORE_PRDA;
grant SELECT on erps.MATERIALPROXY_SALESUNIT to MYLES;
grant SELECT on erps.MATERIALPROXY_SALESUNIT to DWUSER;
grant SELECT on erps.MATERIALPROXY_SALESUNIT to PROD_RO;
grant SELECT on erps.MATERIALPROXY_SALESUNIT to STORE_READ;
grant SELECT on erps.MATERIALPROXY_CHARVALUE to FDSTORE_STPRD01;
grant SELECT on erps.MATERIALPROXY_CHARVALUE to CSSI;
grant SELECT on erps.MATERIALPROXY_CHARVALUE to DWHUSER_ROLE;
grant SELECT on erps.MATERIALPROXY_CHARVALUE to APPDEV_ROLE;
grant SELECT on erps.MATERIALPROXY_CHARVALUE to FDSTORE_PRDB;
grant SELECT on erps.MATERIALPROXY_CHARVALUE to FDSTORE_PRDA;
grant SELECT on erps.MATERIALPROXY_CHARVALUE to MYLES;
grant SELECT on erps.MATERIALPROXY_CHARVALUE to DWUSER;
grant SELECT on erps.MATERIALPROXY_CHARVALUE to PROD_RO;
grant SELECT on erps.MATERIALPROXY_CHARVALUE to STORE_READ;
grant SELECT on erps.MATERIALPROXY to FDSTORE_STPRD01;
grant SELECT on erps.MATERIALPROXY to CSSI;
grant SELECT on erps.MATERIALPROXY to DWHUSER_ROLE;
grant SELECT on erps.MATERIALPROXY to APPDEV_ROLE;
grant SELECT on erps.MATERIALPROXY to CUST;
grant SELECT on erps.MATERIALPROXY to FDSTORE_PRDB;
grant SELECT on erps.MATERIALPROXY to FDSTORE_PRDA;
grant SELECT on erps.MATERIALPROXY to MYLES;
grant SELECT on erps.MATERIALPROXY to DWUSER;
grant SELECT on erps.MATERIALPROXY to PROD_RO;
grant SELECT on erps.MATERIALPROXY to STORE_READ;
grant SELECT on erps.MATERIALPRICE to FDSTORE_STPRD01;
grant SELECT on erps.MATERIALPRICE to CSSI;
grant SELECT on erps.MATERIALPRICE to DWHUSER_ROLE;
grant SELECT on erps.MATERIALPRICE to APPDEV_ROLE;
grant SELECT on erps.MATERIALPRICE to FDSTORE_PRDB;
grant SELECT on erps.MATERIALPRICE to FDSTORE_PRDA;
grant SELECT on erps.MATERIALPRICE to MYLES;
grant SELECT on erps.MATERIALPRICE to DWUSER;
grant SELECT on erps.MATERIALPRICE to PROD_RO;
grant SELECT on erps.MATERIALPRICE to STORE_READ;
grant SELECT on erps.MATERIAL to FDSTORE_STPRD01;
grant SELECT on erps.MATERIAL to CSSI;
grant SELECT on erps.MATERIAL to DWHUSER_ROLE;
grant SELECT on erps.MATERIAL to APPDEV_ROLE;
grant SELECT on erps.MATERIAL to CUST;
grant SELECT on erps.MATERIAL to FDSTORE_PRDB;
grant SELECT on erps.MATERIAL to FDSTORE_PRDA;
grant SELECT on erps.MATERIAL to MYLES;
grant SELECT on erps.MATERIAL to DWUSER;
grant SELECT on erps.MATERIAL to PROD_RO;
grant SELECT on erps.MATERIAL to STORE_READ;
grant SELECT on erps.HISTORY to FDSTORE_STPRD01;
grant SELECT on erps.HISTORY to CSSI;
grant SELECT on erps.HISTORY to DWHUSER_ROLE;
grant SELECT on erps.HISTORY to APPDEV_ROLE;
grant SELECT on erps.HISTORY to FDSTORE_PRDB;
grant SELECT on erps.HISTORY to FDSTORE_PRDA;
grant SELECT on erps.HISTORY to MYLES;
grant SELECT on erps.HISTORY to DWUSER;
grant SELECT on erps.HISTORY to PROD_RO;
grant SELECT on erps.HISTORY to STORE_READ;
grant SELECT on erps.CLASS to FDSTORE_STPRD01;
grant SELECT on erps.CLASS to CSSI;
grant SELECT on erps.CLASS to DWHUSER_ROLE;
grant SELECT on erps.CLASS to APPDEV_ROLE;
grant SELECT on erps.CLASS to FDSTORE_PRDB;
grant SELECT on erps.CLASS to FDSTORE_PRDA;
grant SELECT on erps.CLASS to MYLES;
grant SELECT on erps.CLASS to DWUSER;
grant SELECT on erps.CLASS to PROD_RO;
grant SELECT on erps.CLASS to STORE_READ;
grant SELECT on erps.CHARVALUEPRICE to FDSTORE_STPRD01;
grant SELECT on erps.CHARVALUEPRICE to CSSI;
grant SELECT on erps.CHARVALUEPRICE to DWHUSER_ROLE;
grant SELECT on erps.CHARVALUEPRICE to APPDEV_ROLE;
grant SELECT on erps.CHARVALUEPRICE to FDSTORE_PRDB;
grant SELECT on erps.CHARVALUEPRICE to FDSTORE_PRDA;
grant SELECT on erps.CHARVALUEPRICE to MYLES;
grant SELECT on erps.CHARVALUEPRICE to DWUSER;
grant SELECT on erps.CHARVALUEPRICE to PROD_RO;
grant SELECT on erps.CHARVALUEPRICE to STORE_READ;
grant SELECT on erps.CHARVALUE to FDSTORE_STPRD01;
grant SELECT on erps.CHARVALUE to CSSI;
grant SELECT on erps.CHARVALUE to DWHUSER_ROLE;
grant SELECT on erps.CHARVALUE to APPDEV_ROLE;
grant SELECT on erps.CHARVALUE to FDSTORE_PRDB;
grant SELECT on erps.CHARVALUE to FDSTORE_PRDA;
grant SELECT on erps.CHARVALUE to MYLES;
grant SELECT on erps.CHARVALUE to DWUSER;
grant SELECT on erps.CHARVALUE to PROD_RO;
grant SELECT on erps.CHARVALUE to STORE_READ;
grant SELECT on erps.CHARACTERISTIC to FDSTORE_STPRD01;
grant SELECT on erps.CHARACTERISTIC to CSSI;
grant SELECT on erps.CHARACTERISTIC to DWHUSER_ROLE;
grant SELECT on erps.CHARACTERISTIC to APPDEV_ROLE;
grant SELECT on erps.CHARACTERISTIC to FDSTORE_PRDB;
grant SELECT on erps.CHARACTERISTIC to FDSTORE_PRDA;
grant SELECT on erps.CHARACTERISTIC to MYLES;
grant SELECT on erps.CHARACTERISTIC to DWUSER;
grant SELECT on erps.CHARACTERISTIC to PROD_RO;
grant SELECT on erps.CHARACTERISTIC to STORE_READ;
grant UPDATE on erps.SALESUNIT to FDSTORE_STPRD01;
grant UPDATE on erps.SALESUNIT to FDSTORE_PRDB;
grant UPDATE on erps.SALESUNIT to FDSTORE_PRDA;
grant UPDATE on erps.PRODUCT to FDSTORE_STPRD01;
grant UPDATE on erps.PRODUCT to FDSTORE_PRDB;
grant UPDATE on erps.PRODUCT to FDSTORE_PRDA;
grant UPDATE on erps.MATERIAL_CLASS to FDSTORE_STPRD01;
grant UPDATE on erps.MATERIAL_CLASS to FDSTORE_PRDB;
grant UPDATE on erps.MATERIAL_CLASS to FDSTORE_PRDA;
grant UPDATE on erps.MATERIALPROXY_SALESUNIT to FDSTORE_STPRD01;
grant UPDATE on erps.MATERIALPROXY_SALESUNIT to FDSTORE_PRDB;
grant UPDATE on erps.MATERIALPROXY_SALESUNIT to FDSTORE_PRDA;
grant UPDATE on erps.MATERIALPROXY_CHARVALUE to FDSTORE_STPRD01;
grant UPDATE on erps.MATERIALPROXY_CHARVALUE to FDSTORE_PRDB;
grant UPDATE on erps.MATERIALPROXY_CHARVALUE to FDSTORE_PRDA;
grant UPDATE on erps.MATERIALPROXY to FDSTORE_STPRD01;
grant UPDATE on erps.MATERIALPROXY to FDSTORE_PRDB;
grant UPDATE on erps.MATERIALPROXY to FDSTORE_PRDA;
grant UPDATE on erps.MATERIALPRICE to FDSTORE_STPRD01;
grant UPDATE on erps.MATERIALPRICE to FDSTORE_PRDB;
grant UPDATE on erps.MATERIALPRICE to FDSTORE_PRDA;
grant UPDATE on erps.MATERIAL to FDSTORE_STPRD01;
grant UPDATE on erps.MATERIAL to FDSTORE_PRDB;
grant UPDATE on erps.MATERIAL to FDSTORE_PRDA;
grant UPDATE on erps.HISTORY to FDSTORE_STPRD01;
grant UPDATE on erps.HISTORY to FDSTORE_PRDB;
grant UPDATE on erps.HISTORY to FDSTORE_PRDA;
grant UPDATE on erps.CLASS to FDSTORE_STPRD01;
grant UPDATE on erps.CLASS to FDSTORE_PRDB;
grant UPDATE on erps.CLASS to FDSTORE_PRDA;
grant UPDATE on erps.CHARVALUEPRICE to FDSTORE_STPRD01;
grant UPDATE on erps.CHARVALUEPRICE to FDSTORE_PRDB;
grant UPDATE on erps.CHARVALUEPRICE to FDSTORE_PRDA;
grant UPDATE on erps.CHARVALUE to FDSTORE_STPRD01;
grant UPDATE on erps.CHARVALUE to FDSTORE_PRDB;
grant UPDATE on erps.CHARVALUE to FDSTORE_PRDA;
grant UPDATE on erps.CHARACTERISTIC to FDSTORE_STPRD01;
grant UPDATE on erps.CHARACTERISTIC to FDSTORE_PRDB;
grant UPDATE on erps.CHARACTERISTIC to FDSTORE_PRDA;


-- step2

drop snapshot ATTRIBUTES;
drop snapshot CHARACTERISTIC;
drop snapshot CHARVALUE;
drop snapshot CHARVALUEPRICE;
drop snapshot CLASS;
drop snapshot HISTORY;
drop snapshot INGREDIENTS;
drop snapshot INVENTORY;
drop snapshot INVENTORY_ENTRY;
drop snapshot MATERIAL;
drop snapshot MATERIALPRICE;
drop snapshot MATERIALPROXY;
drop snapshot MATERIALPROXY_CHARVALUE;
drop snapshot MATERIALPROXY_SALESUNIT;
drop snapshot MATERIAL_CLASS;
drop snapshot NUTRITION;
drop snapshot NUTRITION_INFO;
drop snapshot PRODUCT;
drop snapshot SALESUNIT;
drop snapshot UNAVAILABILITY;

create snapshot ATTRIBUTES refresh force start with sysdate next trunc(sysdate+1)+1/24/60
as SELECT * FROM "ATTRIBUTES"@DBSTO.NYC.FRESHDIRECT.COM "ATTRIBUTES" ;

create snapshot CHARACTERISTIC refresh force start with sysdate next trunc(sysdate+1)+2/24/60
as SELECT * FROM "CHARACTERISTIC"@DBSTO.NYC.FRESHDIRECT.COM "CHARACTERISTIC";

create snapshot CHARVALUE refresh force start with sysdate next trunc(sysdate+1)+3/24/60
as SELECT * FROM "CHARVALUE"@DBSTO.NYC.FRESHDIRECT.COM "CHARVALUE";

create snapshot CHARVALUEPRICE refresh force start with sysdate next trunc(sysdate+1)+4/24/60
as SELECT * FROM "CHARVALUEPRICE"@DBSTO.NYC.FRESHDIRECT.COM "CHARVALUEPRICE";

create snapshot CLASS refresh force start with sysdate next trunc(sysdate+1)+5/24/60
as SELECT * FROM "CLASS"@DBSTO.NYC.FRESHDIRECT.COM "CLASS";

create snapshot HISTORY refresh force start with sysdate next trunc(sysdate+1)+6/24/60   
as SELECT * FROM "HISTORY"@DBSTO.NYC.FRESHDIRECT.COM "HISTORY";

create snapshot INGREDIENTS refresh force start with sysdate next trunc(sysdate+1)+7/24/60   
as SELECT * FROM "INGREDIENTS"@DBSTO.NYC.FRESHDIRECT.COM "INGREDIENTS";

create snapshot INVENTORY refresh force start with sysdate next trunc(sysdate+1)+8/24/60   
as SELECT * FROM "INVENTORY"@DBSTO.NYC.FRESHDIRECT.COM "INVENTORY";

create snapshot INVENTORY_ENTRY refresh force start with sysdate next trunc(sysdate+1)+9/24/60   
as SELECT * FROM "INVENTORY_ENTRY"@DBSTO.NYC.FRESHDIRECT.COM "INVENTORY_ENTRY";

create snapshot MATERIAL refresh force start with sysdate next trunc(sysdate+1)+10/24/60   
as SELECT * FROM "MATERIAL"@DBSTO.NYC.FRESHDIRECT.COM "MATERIAL";

----------------------------------------------------
create snapshot MATERIALPRICE refresh force start with sysdate next trunc(sysdate+1)+11/24/60   
as SELECT * FROM "MATERIALPRICE"@DBSTO.NYC.FRESHDIRECT.COM "MATERIALPRICE";

create snapshot MATERIALPROXY refresh force start with sysdate next trunc(sysdate+1)+12/24/60   
as SELECT * FROM "MATERIALPROXY"@DBSTO.NYC.FRESHDIRECT.COM "MATERIALPROXY";

create snapshot MATERIALPROXY_CHARVALUE refresh force start with sysdate next trunc(sysdate+1)+13/24/60   
as SELECT * FROM "MATERIALPROXY_CHARVALUE"@DBSTO.NYC.FRESHDIRECT.COM "MATERIALPROXY_CHARVALUE";

create snapshot MATERIALPROXY_SALESUNIT refresh force start with sysdate next trunc(sysdate+1)+14/24/60   
as SELECT * FROM "MATERIALPROXY_SALESUNIT"@DBSTO.NYC.FRESHDIRECT.COM "MATERIALPROXY_SALESUNIT";

create snapshot MATERIAL_CLASS refresh force start with sysdate next trunc(sysdate+1)+15/24/60   
as SELECT * FROM "MATERIAL_CLASS"@DBSTO.NYC.FRESHDIRECT.COM "MATERIAL_CLASS";

create snapshot NUTRITION refresh force start with sysdate next trunc(sysdate+1)+16/24/60
as SELECT * FROM "NUTRITION"@DBSTO.NYC.FRESHDIRECT.COM "NUTRITION";

create snapshot NUTRITION_INFO refresh force start with sysdate next trunc(sysdate+1)+17/24/60   
as SELECT * FROM "NUTRITION_INFO"@DBSTO.NYC.FRESHDIRECT.COM "NUTRITION_INFO";

create snapshot PRODUCT refresh force start with sysdate next trunc(sysdate+1)+18/24/60  
as SELECT * FROM "PRODUCT"@DBSTO.NYC.FRESHDIRECT.COM "PRODUCT";

create snapshot SALESUNIT refresh force start with sysdate next trunc(sysdate+1)+19/24/60  
as SELECT * FROM "SALESUNIT"@DBSTO.NYC.FRESHDIRECT.COM "SALESUNIT" ;

create snapshot UNAVAILABILITY refresh force start with sysdate next trunc(sysdate+1)+20/24/60  
as SELECT * FROM "UNAVAILABILITY"@DBSTO.NYC.FRESHDIRECT.COM "UNAVAILABILITY";

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
grant DELETE on erps.COOL_INFO to FDSTORE_PRDA;
grant INSERT on erps.COOL_INFO to FDSTORE_PRDA;
grant SELECT on erps.COOL_INFO to FDSTORE_PRDA;
grant UPDATE on erps.COOL_INFO to FDSTORE_PRDA;
grant SELECT on erps.COOL_INFO to APPDEV;
grant DELETE on erps.COOL_INFO to FDSTORE_STSTG01;
grant INSERT on erps.COOL_INFO to FDSTORE_STSTG01;
grant SELECT on erps.COOL_INFO to FDSTORE_STSTG01;
grant UPDATE on erps.COOL_INFO to FDSTORE_STSTG01;
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
-- step 3 is done

create snapshot PRICING_REGION refresh force start with sysdate next trunc(sysdate+1)+1/24/60
as SELECT * FROM "PRICING_REGION"@DBSTO.NYC.FRESHDIRECT.COM "PRICING_REGION" ;

create snapshot PRICING_REGION_ZIPS refresh force start with sysdate next trunc(sysdate+1)+1/24/60
as SELECT * FROM "PRICING_REGION_ZIPS"@DBSTO.NYC.FRESHDIRECT.COM "PRICING_REGION_ZIPS" ;

create snapshot PRICING_ZONE refresh force start with sysdate next trunc(sysdate+1)+1/24/60
as SELECT * FROM "PRICING_ZONE"@DBSTO.NYC.FRESHDIRECT.COM "PRICING_ZONE" ;

create snapshot ZONE_HISTORY refresh force start with sysdate next trunc(sysdate+1)+1/24/60
as SELECT * FROM "ZONE_HISTORY"@DBSTO.NYC.FRESHDIRECT.COM "ZONE_HISTORY" ;

-- check indexes, they're there,
-- grant privileges
grant select on erps.PRICING_REGION to  FDSTORE;
grant select on erps.PRICING_REGION_ZIPS to  FDSTORE;
grant select on erps.PRICING_ZONE to  FDSTORE;
grant select on erps.ZONE_HISTORY to  FDSTORE;

