CREATE OR REPLACE PROCEDURE TIMESLOT_CRON_RUNNER
	   IS
		 BEGIN
		 	  DECLARE
			  		 CURSOR region_cur IS
					 		SELECT DISTINCT id
							FROM REGION;
							
					 CURSOR region_data_cur(region_id_in REGION.ID%TYPE) IS 
					 		SELECT ID FROM REGION_DATA 
			  				WHERE REGION_ID = region_id_in
							AND START_DATE = (SELECT MAX(START_DATE) FROM REGION_DATA WHERE START_DATE <= SYSDATE AND REGION_ID = region_id_in);
							
					 CURSOR zone_cur (region_data_id_in REGION_DATA.ID%TYPE) IS
					 		SELECT ID, TEMPLATE_ID FROM ZONE
							WHERE REGION_DATA_ID = region_data_id_in;
							
					 CURSOR missing_timeslot_cur (date_in DATE, zone_id_in ZONE.ID%TYPE) IS
					 		SELECT beg_datetime
							FROM TIMESLOT
							WHERE to_date(to_char(beg_datetime, 'MM/DD/YYYY'), 'MM/DD/YYYY') = to_date(to_char(date_in, 'MM/DD/YYYY'), 'MM/DD/YYYY')
							AND zone_id = zone_id_in;					
								 
					 missing_timeslot_rec missing_timeslot_cur%ROWTYPE;
					 zone_rec zone_cur%ROWTYPE;
					 today DATE := SYSDATE;
					 timeslot_date DATE;
					 region_id REGION.ID%TYPE;
					 region_data_id REGION_DATA.ID%TYPE;

					 BEGIN
					 	  IF NOT region_cur%ISOPEN
						  THEN
						  	 OPEN region_cur;
						  END IF;
						  FETCH region_cur INTO region_id;
						  --region loop
						  WHILE region_cur%FOUND
						  LOOP
						  DBMS_OUTPUT.PUT_LINE('region ID: '||region_id);
						  
						  	  IF NOT region_data_cur%ISOPEN
						  	  THEN
						  	  	  OPEN region_data_cur(region_id);
						  	  END IF;
						  
						  	  FETCH region_data_cur INTO region_data_id;
							 -- day loop
							 DBMS_OUTPUT.PUT_LINE('region data ID:'||region_data_id);
							 
							 IF NOT zone_cur%ISOPEN
							 THEN
							 	 OPEN zone_cur(region_data_id);
							 END IF;
							 FETCH zone_cur INTO zone_rec;
							 
							 WHILE zone_cur%FOUND
							 LOOP
							 
						 	 	 FOR loop_index IN 1 .. 8
								 	 LOOP
								 	 	 IF NOT missing_timeslot_cur%ISOPEN
									 	 THEN
											OPEN missing_timeslot_cur((today + loop_index), zone_rec.id);
										 END IF;
										 FETCH missing_timeslot_cur INTO missing_timeslot_rec;
										 IF missing_timeslot_cur%NOTFOUND
										 THEN
										 	--DBMS_OUTPUT.PUT_LINE('Date: '||(today+loop_index));
											TIMESLOT_CRON(today+loop_index, zone_rec.id, zone_rec.template_id);
											
										 END IF;
	
										 IF missing_timeslot_cur%ISOPEN
										 THEN
											CLOSE missing_timeslot_cur;
										 END IF;
	
								 	END LOOP;
								FETCH zone_cur INTO zone_rec;
							END LOOP;
							
							IF region_data_cur%ISOPEN
							THEN
								CLOSE region_data_cur;
								region_data_id := '-1';
							END IF;
							
							IF zone_cur%ISOPEN
							THEN
								CLOSE zone_cur;
							END IF;
								
							FETCH region_cur INTO region_id;
						 END LOOP;
						 
						 IF region_cur%ISOPEN
						 THEN
						 	 CLOSE region_cur;
						 END IF;

						 COMMIT;
					END;
			END;
/
