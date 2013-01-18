  create table erps.class_new
   (	version number(10,0) not null enable, 
	sap_id varchar2(18) not null enable, 
	id varchar2(16) not null enable, 
	 constraint pk_cls_new primary key (id)
  using index  tablespace fderpidx  enable
   )  tablespace fderpdat;
   insert into erps.class_new
   select * from erps.class        ;

  create table erps.characteristic_new
   (	id varchar2(16) not null enable, 
	version number(10,0) not null enable, 
	class_id varchar2(16) not null enable, 
	name varchar2(30) not null enable, 
	 constraint pk_chr_new primary key (id)
  using index tablespace fderpidx  enable,
  constraint chr_cls_fk_new foreign key (class_id)
	  references erps.class_new (id) enable
   )  tablespace fderpdat ;
   insert into erps.characteristic_new
   select * from erps.characteristic;
   create index IDX_CHR_CLID_new on characteristic_new(CLASS_ID) tablespace FDERPIDX;

  create table erps.charvalue_new
   (	version number(10,0) not null enable, 
	char_id varchar2(16) not null enable, 
	id varchar2(16) not null enable, 
	name varchar2(30) not null enable, 
	description varchar2(40) not null enable, 
	 constraint pk_chv_new primary key (id)
  using index tablespace fderpidx  enable,
  constraint chv_chr_fk_new foreign key (char_id)
	  references erps.characteristic_new (id) enable
   ) tablespace fderpdat;
   insert into erps.charvalue_new
   select * from erps.charvalue ;
 create index IDX_CHV_CHID_new on charvalue_new(CHAR_ID) tablespace FDERPIDX;

  create table erps.material_new
   (	id varchar2(16) not null enable,
	version number(10,0) not null enable, 
	sap_id varchar2(18) not null enable, 
	base_unit varchar2(3) not null enable, 
	description varchar2(40) not null enable, 
	lead_time number(2,0) not null enable, 
	atp_rule number(1,0) not null enable, 
	char_quantity varchar2(30), 
	char_salesunit varchar2(30), 
	upc varchar2(20), 
	alcoholic_content varchar2(1), 
	taxable char(1), 
	kosher_production char(1), 
	platter char(1), 
	blocked_days varchar2(7), 
	 constraint pk_mtl_new primary key (id)
  using index
  tablespace fderpidx  enable
   )   tablespace fderpdat;
   insert into erps.material_new
   select * from erps.material;
   create nonunique index MATERIAL_IDX_002_new  on erps.material_new (ID ,SAP_ID ) tablespace  FDERPIDX;
   create unique index IDX_MATL_SAPID_VER_new on erps.material_new (SAP_ID ,VERSION ) tablespace  FDERPIDX;

  create table erps.charvalueprice_new
   (	version number(10,0) not null enable,
	mat_id varchar2(16) not null enable,
	id varchar2(16) not null enable,
	cv_id varchar2(16) not null enable,
	sap_id varchar2(18) not null enable,
	price number(10,2) not null enable,
	pricing_unit varchar2(3) not null enable,
	condition_type varchar2(4) not null enable,
	 constraint pk_cvp_new primary key (id)
  using index  tablespace fderpidx  enable,
	 constraint cvp_mtl_fk_new foreign key (mat_id)
	  references erps.material_new (id) enable,
	 constraint cvp_chv_fk_new foreign key (cv_id)
	  references erps.charvalue_new (id) enable
   )  tablespace fderpdat;
   insert into erps.charvalueprice_new
   select * from  erps.charvalueprice;
 create nonunique index IDX_CVP_MID_new on charvalueprice_new(MAT_ID);
 create nonunique index CHARVALUEPRICE_IDX_017_new on charvalueprice_new(CV_ID);


  create table erps.history_new 
   (	version number(10,0) not null enable, 
	date_created date not null enable, 
	created_by varchar2(40) not null enable, 
	approval_status char(1) not null enable, 
	date_approved date, 
	approved_by varchar2(40), 
	description varchar2(255), 
	 constraint pk_hst_new primary key (version)
  using index  tablespace fderpidx  enable
   ) tablespace fderpdat;
   insert into erps.history_new
   select * from erps.history   ;
  create nonunique index HISTORY_VERSION_DATE_IDX_new on history_new(VERSION ,DATE_CREATED ) tablespace   FDERPIDX;


  create table erps.materialprice_new 
   (	id varchar2(16) not null enable,
	version number(10,0) not null enable, 
	price number(10,2) not null enable, 
	pricing_unit varchar2(3) not null enable, 
	scale_quantity number(10,2) not null enable, 
	scale_unit varchar2(3) not null enable, 
	sap_id varchar2(18) not null enable, 
	mat_id varchar2(16) not null enable, 
	 constraint pk_mpr_new primary key (id)
  using index
  tablespace fderpidx  enable, 
	 constraint mpr_mtl_fk_new foreign key (mat_id)
	  references erps.material_new (id) enable
   )  tablespace fderpdat;
   insert into erps.materialprice_new
   select * from  erps.materialprice  ;
 create index IDX_MPR_MID_new on materialprice_new(MAT_ID) tablespace  FDERPIDX;

--
 create table erps.product_new
   (	id varchar2(16) not null enable,
	version number(10,0) not null enable,
	sku_code varchar2(18) not null enable,
	default_price number(10,2) not null enable,
	default_unit varchar2(3) not null enable,
	unavailability_status varchar2(4), 
	unavailability_date date, 
	unavailability_reason varchar2(255), 
	rating varchar2(3), 
	base_price number(10,2), 
	base_pricing_unit varchar2(3), 
  constraint pk_prd_new primary key (id)
  using index
  tablespace fderpidx  enable,
	 constraint prd_hst_fk_new foreign key (version)
	  references erps.history_new (version) enable
   )
  tablespace fderpdat ;
  insert into erps.product_new
  select * from erps.product ;
create unique index erps.PRODUCT_IDX_001_new on erps.product_new (ID,SKU_CODE,VERSION) tablespace fderpidx ;
-- following 2 indexes name too long
-- create index erps.PRODUCT_VERSION_SKU_AVAIL_IDX_new on erps.product_new (VERSION,SKU_CODE,UNAVAILABILITY_STATUS ) tablespace fderpidx ;
-- create index erps.PRODUCT_SKU_AVAIL_VERSION_IDX_new on erps.product_new (SKU_CODE,UNAVAILABILITY_STATUS,VERSION) tablespace fderpidx ;
create index erps.PVSAI_new on erps.product_new (VERSION,SKU_CODE,UNAVAILABILITY_STATUS ) tablespace fderpidx ;
create index erps.PSAVI_new on erps.product_new (SKU_CODE,UNAVAILABILITY_STATUS,VERSION) tablespace fderpidx ;

create unique index erps.IDX_PRD_SKU_VER_UNV_new  on erps.product_new (SKU_CODE,VERSION,UNAVAILABILITY_STATUS) tablespace fderpidx ;
create unique index erps.IDX_PRD_SKU_VER_new on erps.product_new (SKU_CODE,VERSION) tablespace fderpidx ;


create table erps.materialproxy_new
   (	id varchar2(16) not null enable, 
	version number(10,0) not null enable,
	mat_id varchar2(16) not null enable, 
	product_id varchar2(16) not null enable, 
	 constraint pk_mpx_new primary key (id)
  using index
  tablespace fderpidx  enable,
	 constraint mpx_prd_fk_new foreign key (product_id)
	  references erps.product_new (id) enable,
	 constraint mpx_mtl_fk_new foreign key (mat_id)
	  references erps.material_new (id) enable
   )
  tablespace fderpdat;
  insert into erps.materialproxy_new
 select * from erps.materialproxy;
 create  index IDX_MPX_PID_new on materialproxy_new(PRODUCT_ID);
 create  index IDX_MPX_MID_new on materialproxy_new(MAT_ID);


  create table erps.materialproxy_charvalue_new
   (	cv_id varchar2(16) not null enable, 
	matproxy_id varchar2(16) not null enable,
	 constraint pk_mpxchv_new primary key (cv_id, matproxy_id)
  using index
  tablespace fderpidx  enable, 
	 constraint mpxchv_mpx_fk_new foreign key (matproxy_id)
	  references erps.materialproxy_new (id) enable,
	 constraint mpxchv_chv_fk_new foreign key (cv_id)
	  references erps.charvalue_new (id) enable
   )  tablespace fderpdat ;
   insert into erps.materialproxy_charvalue_new
  select * from erps.materialproxy_charvalue
 ;
-- index name too long
-- create index MATERIALPROXY_CHARVA_IDX_016_new on materialproxy_charvalue_new(MATPROXY_ID) tablespace fderpidx ;
   create index MCI_016_new on materialproxy_charvalue_new(MATPROXY_ID) tablespace fderpidx ;

  create table erps.salesunit_new
   (	id varchar2(16) not null enable,
	version number(10,0) not null enable,
	mat_id varchar2(16) not null enable,
	alternative_unit varchar2(3) not null enable,
	base_unit varchar2(3) not null enable,
	numerator number(10,0) not null enable, 
	denominator number(10,0) not null enable, 
	description varchar2(40) not null enable, 
	display_ind char(1) default 'n', 
	 constraint pk_slu_new primary key (id)
  using index
  tablespace fderpidx  enable, 
	 constraint slu_mtl_fk_new foreign key (mat_id)
	  references erps.material_new (id) enable
   )
  tablespace fderpdat ;
  insert into erps.salesunit_new
  select * from erps.salesunit ;
 create index IDX_SLU_MID_new on salesunit_new (MAT_ID) tablespace fderpidx ;


  create table erps.materialproxy_salesunit_new
   (	matproxy_id varchar2(16) not null enable,
	salesunit_id varchar2(16) not null enable,
	 constraint pk_mpx_slu_new primary key (matproxy_id, salesunit_id)
  using index
  tablespace fderpidx  enable, 
	 constraint mpxslu_slu_fk_new foreign key (salesunit_id)
	  references erps.salesunit_new (id) enable,
	 constraint mpxslu_mpx_fk_new foreign key (matproxy_id)
	  references erps.materialproxy_new (id) enable
   )
  tablespace fderpdat ;
  insert into erps.materialproxy_salesunit_new
  select * from  erps.materialproxy_salesunit ;
-- index name too long
-- create index MATERIALPROXY_SALESU_IDX_015_new on materialproxy_salesunit_new(SALESUNIT_ID) tablespace fderpidx ;
   create index MSI_015_new on materialproxy_salesunit_new(SALESUNIT_ID) tablespace fderpidx ;

  create table erps.material_class_new 
   (	mat_id varchar2(16) not null enable,
	class_id varchar2(16) not null enable, 
	 constraint pk_mtlcls_new primary key (mat_id, class_id)
  using index
  tablespace fderpidx  enable, 
	 constraint mtlcls_mtl_fk_new foreign key (mat_id)
	  references erps.material_new (id) enable,
	 constraint mtlcls_cls_fk_new foreign key (class_id)
	  references erps.class_new (id) enable
   )
  tablespace fderpdat ;
  insert into erps.material_class_new
  select * from erps.material_class  ;
 create  index MATERIAL_CLASS_IDX_014_new on material_class_new(CLASS_ID)  tablespace fderpidx ;

--------------------------------------------------------------------------------------------------------------------
-- Add following columns to ERPS.MATERIALPRICE table with default values
alter table erps.MATERIALPRICE_new add
(SAP_ZONE_ID varchar2(16)  default '0000100000' not null,
  PROMO_PRICE   NUMBER(10,2)  default 0.0);

-- Drop columns from ERPS.PRODUCT_NEW table.
alter table ERPS.PRODUCT_NEW
  drop (DEFAULT_PRICE, DEFAULT_UNIT, BASE_PRICE , BASE_PRICING_UNIT);
  





 
