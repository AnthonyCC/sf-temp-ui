CREATE OR REPLACE PROCEDURE USPS_LOAD_FLIP AS
    BEGIN
		DECLARE
		v_exist  PLS_INTEGER;
			BEGIN
			
			--index zipplusfour_load table
			execute immediate 'CREATE INDEX DLV.ZPFL_CITY_STATE_KEY_IDX ON DLV.ZIPPLUSFOUR_LOAD(CITY_STATE_KEY)';
			
			--drop existing backup tables
			select count(*)
			into v_exist
			from user_tables
			where table_name = 'ZIPPLUSFOUR_OLD';
			
			if v_exist = 1 then
			execute immediate
			'drop table ZIPPLUSFOUR_OLD purge';
			end if;
			
			
			select count(*)
			into v_exist
			from user_tables
			where table_name = 'CITY_STATE_OLD';		
			
			if v_exist = 1 then
			execute immediate
			'drop table CITY_STATE_OLD purge';
			end if;
		
	        
	        --backup existing tables
			execute immediate 'alter index PK_CITYSTATE rename to PK_CITYSTATE_OLD';
			execute immediate 'alter table city_state rename constraint PK_CITYSTATE to PK_CITYSTATE_OLD';
			execute immediate 'alter table city_state rename to city_state_old';
	       
	        execute immediate 'alter index zpf_city_state_key_idx rename to zpf_city_state_key_idx_old';
	        execute immediate 'alter index zpf_locator_key rename to zpf_locator_key_old';
			execute immediate 'ALTER TABLE DLV.ZIPPLUSFOUR DROP CONSTRAINT FK_CITYSTATE';
		    execute immediate 'alter table zipplusfour rename to zipplusfour_old';

	        --swap in load tables
	        execute immediate 'alter table city_state_load rename to city_state';
	        execute immediate 'alter index pk_citystatel rename to pk_citystate';
			execute immediate 'alter table city_state rename constraint PK_CITYSTATEL to PK_CITYSTATE';
	       
	        execute immediate 'alter table zipplusfour_load rename to zipplusfour';
	        execute immediate 'alter index zpfl_city_state_key_idx rename to zpf_city_state_key_idx';
	        execute immediate 'alter index zpfl_locator_key rename to zpf_locator_key';
			
			--apply referencial integrity
			execute immediate 'ALTER TABLE DLV.ZIPPLUSFOUR ADD CONSTRAINT FK_CITYSTATE FOREIGN KEY (CITY_STATE_KEY)REFERENCES DLV.CITY_STATE (CITY_STATE_KEY)';
	    END;
	END;
/

