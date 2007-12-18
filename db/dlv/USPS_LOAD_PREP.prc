CREATE OR REPLACE PROCEDURE USPS_LOAD_PREP authid current_user AS
    BEGIN	
		DECLARE
			   v_exist  PLS_INTEGER;
		BEGIN	 	
			select count(*)
			into v_exist
			from user_tables
			where table_name = 'CITY_STATE_LOAD';
			
			if v_exist = 1 then
			execute immediate
			'drop table CITY_STATE_LOAD PURGE';
			end if;
			
			select count(*)
			into v_exist
			from user_tables
			where table_name = 'ZIPPLUSFOUR_LOAD';
			
			if v_exist = 1 then
			execute immediate
			'drop table ZIPPLUSFOUR_LOAD PURGE';
			end if;
			
				execute immediate 'CREATE TABLE CITY_STATE_LOAD' ||
				'(' ||
				  'STATE           VARCHAR2(2 BYTE)              NOT NULL,' ||
				  'CITY            VARCHAR2(28 BYTE)             NOT NULL,' ||
				  'CITY_STATE_KEY  VARCHAR2(6 BYTE)              NOT NULL,' ||
				  'COUNTY          VARCHAR2(28 BYTE)' ||
				')';
			
			execute immediate 'ALTER TABLE DLV.CITY_STATE_LOAD ADD CONSTRAINT PK_CITYSTATEL PRIMARY KEY (CITY_STATE_KEY)';
	
			execute immediate 'CREATE TABLE DLV.ZIPPLUSFOUR_LOAD' ||
				'(' ||
				  'BLDG_NUM_LOW     VARCHAR2(10 BYTE)            NOT NULL,' ||
				  'BLDG_NUM_HIGH    VARCHAR2(10 BYTE)            NOT NULL,' ||
				  'STREET_PRE_DIR   VARCHAR2(2 BYTE),' ||
				  'STREET_NAME      VARCHAR2(28 BYTE)            NOT NULL,' ||
				  'STREET_SUFFIX    VARCHAR2(4 BYTE),' ||
				  'STREET_POST_DIR  VARCHAR2(2 BYTE),' ||
				  'APT_NUM_LOW      VARCHAR2(8 BYTE),' ||
				  'APT_NUM_HIGH     VARCHAR2(8 BYTE),' ||
				  'ZIPCODE          VARCHAR2(5 BYTE)             NOT NULL,' ||
				  'PLUSFOUR         VARCHAR2(4 BYTE)             NOT NULL,' ||
				  'STREET_NORMAL    VARCHAR2(36 BYTE)            NOT NULL,' ||
				  'ADDRESS_TYPE     VARCHAR2(1 BYTE),' ||
				  'CITY_STATE_KEY   VARCHAR2(6 BYTE)' ||
				')';
						

	    
			execute immediate 'CREATE UNIQUE INDEX ZPFL_LOCATOR_KEY ON ZIPPLUSFOUR_LOAD' ||
				'(ZIPCODE, STREET_NORMAL, BLDG_NUM_LOW, BLDG_NUM_HIGH, APT_NUM_LOW,APT_NUM_HIGH)';
			
            execute immediate 'GRANT SELECT ON DLV.ZIPPLUSFOUR_LOAD TO FDSTORE_PRDA';
			    END;
	END;
/

