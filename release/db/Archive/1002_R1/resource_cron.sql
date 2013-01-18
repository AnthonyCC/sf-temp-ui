CREATE OR REPLACE
PROCEDURE DLV.RESOURCE_CRON (zone_code_in IN varchar2, zone_id_in varchar2, is_dynamic_in varchar2)
	   IS
	   	BEGIN
		 	  DECLARE

					 CURSOR resource_cur(day_in PLANNING_RESOURCE.DAY%TYPE) is
					 		SELECT * FROM PLANNING_RESOURCE
							WHERE day = day_in and zone_code = zone_code_in;

					 CURSOR timeslot_cur (resource_id_in TIMESLOT.RESOURCE_ID%TYPE) is
					 		SELECT BASE_DATE, START_TIME, END_TIME, CUTOFF_TIME, STATUS, CAPACITY, PLANNED_CAPACITY, TRAFFIC_FACTOR, ZONE_ID, CT_CAPACITY
							FROM TIMESLOT
							WHERE RESOURCE_ID = resource_id_in;

					 CURSOR truck_cur (resource_id_in TRUCK_RESOURCE.RESOURCE_ID%TYPE) is
					 		SELECT TRUCK_TYPE, TRUCK_COUNT, TRUCK_CAPACITY
							FROM TRUCK_RESOURCE
							WHERE RESOURCE_ID = resource_id_in;

					 CURSOR sequence_cur is
					 		SELECT SYSTEM_SEQ.NEXTVAL FROM DUAL;

					 work_date DATE;
					 resource_id varchar2(16);
					 resource_rec PLANNING_RESOURCE%ROWTYPE;
					 timeslot_rec timeslot_cur%ROWTYPE;
					 truck_rec truck_cur%ROWTYPE;


					 BEGIN
					 	  work_date := to_date(to_char(sysdate+7, 'MM/DD/YY'), 'MM/DD/YY');

						  IF NOT resource_cur%ISOPEN
						  THEN
						  	  OPEN resource_cur(work_date);
						  END IF;

						  FETCH resource_cur into resource_rec;
						  IF resource_cur%NOTFOUND
						  THEN
						  	  IF resource_cur%ISOPEN
							  THEN
								CLOSE resource_cur;
								resource_rec := null;
							  END IF;
							  DBMS_OUTPUT.PUT_LINE('NO resource was found for '||to_char(work_date, 'DAY MM/DD/YYYY'));

							  IF NOT resource_cur%ISOPEN
							  THEN
							  	  OPEN resource_cur(to_date(to_char(sysdate, 'MM/DD/YY'), 'MM/DD/YY'));
							  END IF;
							  FETCH resource_cur into resource_rec;
							  WHILE resource_cur%FOUND
							  LOOP
							  	  DBMS_OUTPUT.PUT_LINE('Now working date is '|| resource_rec.DAY);
								  -- Getting the new resource id from sequence
								  IF NOT sequence_cur%ISOPEN
								  THEN
								  	  OPEN sequence_cur;
								  END IF;
								  FETCH sequence_cur into resource_id;
								  IF sequence_cur%ISOPEN
								  THEN
								  	  CLOSE sequence_cur;
								  END IF;

								  INSERT INTO PLANNING_RESOURCE (ID, ZONE_CODE, DAY, PEOPLE, DELIVERY_RATE, NAME)
								  VALUES(resource_id, resource_rec.ZONE_CODE, work_date, resource_rec.PEOPLE, resource_rec.DELIVERY_RATE, resource_rec.NAME);

								  -- Getting timeslots for last same day and inserting as as new resource for 8 day
								  IF NOT timeslot_cur%ISOPEN
								  THEN
								  	  OPEN timeslot_cur(resource_rec.ID);
								  END IF;
								  FETCH timeslot_cur into timeslot_rec;
								  WHILE timeslot_cur%FOUND
								  LOOP
								  	  INSERT INTO TIMESLOT(ID, RESOURCE_ID, BASE_DATE, START_TIME, END_TIME, CUTOFF_TIME, STATUS, CAPACITY, PLANNED_CAPACITY, TRAFFIC_FACTOR, ZONE_ID, CT_CAPACITY, IS_DYNAMIC)
									  VALUES(SYSTEM_SEQ.NEXTVAL, resource_id, work_date, timeslot_rec.START_TIME, timeslot_rec.END_TIME, timeslot_rec.CUTOFF_TIME, timeslot_rec.STATUS, timeslot_rec.CAPACITY, timeslot_rec.PLANNED_CAPACITY, timeslot_rec.TRAFFIC_FACTOR, zone_id_in, timeslot_rec.CT_CAPACITY, is_dynamic_in);

								  	  FETCH timeslot_cur into timeslot_rec;
								  END LOOP;

								  IF timeslot_cur%ISOPEN
								  THEN
								  	 CLOSE timeslot_cur;
									 timeslot_rec := null;
								  END IF;

								  -- Getting truck resources for last same day and inserting as new resource for 8 day
								  IF NOT truck_cur%ISOPEN
								  THEN
								  	  OPEN truck_cur(resource_rec.ID);
								  END IF;
								  FETCH truck_cur into truck_rec;
								  WHILE truck_cur%FOUND
								  LOOP
								  	  INSERT INTO TRUCK_RESOURCE(ID, RESOURCE_ID, TRUCK_TYPE, TRUCK_COUNT, TRUCK_CAPACITY)
									  VALUES (SYSTEM_SEQ.NEXTVAL, resource_id, truck_rec.TRUCK_TYPE, truck_rec.TRUCK_COUNT, truck_rec.TRUCK_CAPACITY);

									  FETCH truck_cur into truck_rec;
								  END LOOP;
								  IF truck_cur%ISOPEN
								  THEN
								  	  CLOSE truck_cur;
									  truck_rec := null;
								  END IF;
								  FETCH resource_cur into resource_rec;
							  END LOOP;

						  END IF;

						  COMMIT;
					 END;

		END;
/
