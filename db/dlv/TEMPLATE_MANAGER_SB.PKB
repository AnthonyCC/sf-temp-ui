CREATE OR REPLACE PACKAGE template_manager_sb AS
	   type myrctype is ref cursor;
	   cursor_num number;
	   mi_prinx number;
	   region_data_id varchar2(16);
	   new_template_id varchar2(16);
	   template_name TIMESLOT_TEMPLATE.NAME%TYPE;
	   template_date TIMESLOT_TEMPLATE.EFFECTIVE_DATE%TYPE;
	   
	   CURSOR zone_cur (region_id_in REGION.ID%TYPE) IS
	   		  SELECT NAME, GEOLOC, ZONE_CODE FROM zone 
			  WHERE region_data_id = region_id_in;
			  
	   CURSOR template_data_cur (template_id_in TIMESLOT_TEMPLATE.ID%TYPE) IS
	   		  SELECT START_TIME, END_TIME, CUTOFF_TIME, STATUS_CODE, DAYCODE 
			  FROM TIMESLOT_TEMPLATE_DATA
			  WHERE TEMPLATE_ID = template_id_in;	
			  
	   CURSOR mn_zone_cur IS
	   		  SELECT NAME, GEOLOC, CODE FROM zone_worktable;
			  
	   zone_rec zone_cur%ROWTYPE;
	   template_data_rec template_data_cur%ROWTYPE;
       function copy_zones(data_pk_in varchar2, region_id_in varchar2) return number;
	   function create_version(start_date_in date, region_id_in varchar2, dlv_charge_in number) return varchar2;
	   function copy_template(region_data_id_in varchar2, template_id_in varchar2) return number;
	   function delete_version(region_data_id_in varchar2) return number;
	   function update_version(region_data_id_in varchar2, start_date_in date, dlv_charge_in number) return number;
END template_manager_sb;
/
CREATE OR REPLACE PACKAGE BODY template_manager_sb AS
	   function copy_zones(data_pk_in varchar2, region_id_in varchar2) return number is
	   begin
	   		mi_prinx := 1;
	   		cursor_num := dbms_sql.OPEN_CURSOR;
			dbms_sql.parse(cursor_num, 'truncate table ZONE_WORKTABLE', 1);
			dbms_sql.close_cursor(cursor_num);
			
			IF NOT zone_cur%ISOPEN
			THEN
				OPEN zone_cur(data_pk_in);
			END IF;
			
			FETCH zone_cur INTO zone_rec;
			WHILE zone_cur%FOUND
			LOOP
				INSERT INTO zone_worktable (CODE, NAME, MI_PRINX, GEOLOC)
					   VALUES(zone_rec.ZONE_CODE, zone_rec.NAME, mi_prinx, zone_rec.GEOLOC);
				
				
				mi_prinx := mi_prinx + 1;
				FETCH zone_cur INTO zone_rec;
			END LOOP;
			
			IF zone_cur%ISOPEN
			THEN
				CLOSE zone_cur;
			END IF;
	   		return mi_prinx;
	   end copy_zones;
	   
	   function create_version(start_date_in date, region_id_in varchar2, dlv_charge_in number) return varchar2 is
	   begin
	   		mi_prinx := 1;
			SELECT SYSTEM_SEQ.NEXTVAL into region_data_id FROM DUAL ;
			
	   		INSERT INTO region_data 
				   values(region_data_id, region_id_in, start_date_in, dlv_charge_in);
				   
			IF NOT mn_zone_cur%ISOPEN
			THEN
				OPEN mn_zone_cur;
			END IF;
			
			FETCH mn_zone_cur INTO zone_rec;
			WHILE mn_zone_cur%FOUND
			LOOP
				INSERT INTO zone
					   (ID, NAME, GEOLOC, ZONE_CODE, REGION_DATA_ID)
					   values(SYSTEM_SEQ.NEXTVAL, zone_rec.NAME, zone_rec.GEOLOC, zone_rec.ZONE_CODE, region_data_id);
					   
				FETCH mn_zone_cur INTO zone_rec;
			END LOOP;
			
			IF mn_zone_cur%ISOPEN
			THEN
				CLOSE mn_zone_cur;
			END IF;
				   
			--return mi_prinx;
			return region_data_id;	   
	   
	   end create_version;
	   
	   function copy_template(region_data_id_in varchar2, template_id_in varchar2) return number is
	   begin
	   		mi_prinx := 1;
			
			SELECT SYSTEM_SEQ.NEXTVAL into new_template_id FROM DUAL;
			SELECT NAME, EFFECTIVE_DATE INTO template_name, template_date  FROM TIMESLOT_TEMPLATE WHERE ID = template_id_in;
			
			INSERT INTO TIMESLOT_TEMPLATE 
				   VALUES(new_template_id, template_name, template_date, region_data_id_in);
			
			
			IF NOT template_data_cur%ISOPEN
			THEN
				OPEN template_data_cur(template_id_in);
			END IF;
			
			FETCH template_data_cur INTO template_data_rec;
			WHILE template_data_cur%FOUND
			LOOP
				INSERT INTO TIMESLOT_TEMPLATE_DATA
					values(SYSTEM_SEQ.NEXTVAL, new_template_id, template_data_rec.START_TIME, template_data_rec.END_TIME, template_data_rec.CUTOFF_TIME, template_data_rec.STATUS_CODE, template_data_rec.DAYCODE);
				
				FETCH template_data_cur INTO template_data_rec;
			END LOOP;
			
			IF template_data_cur%ISOPEN
			THEN
				CLOSE template_data_cur;
			END IF;

			
			return mi_prinx;
			
		end copy_template;
	   
	   function delete_version(region_data_id_in varchar2) return number is
	   begin
	   		mi_prinx := 1;
			
			DELETE FROM zone where region_data_id = region_data_id_in;
			
			DELETE FROM region_data where id = region_data_id_in;

			
			return mi_prinx;
	   end delete_version;
	   
	   function update_version(region_data_id_in varchar2, start_date_in date, dlv_charge_in number) return number is
	   begin
	   		mi_prinx := 1;
			
			UPDATE region_data set start_date = start_date_in, delivery_charges = dlv_charge_in WHERE id = region_data_id_in;
			
			return mi_prinx;
	   
	   end update_version;


END template_manager_sb;
/
