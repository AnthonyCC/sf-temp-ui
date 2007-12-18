CREATE OR REPLACE PROCEDURE TIMESLOT_CRON(timeslot_date IN DATE, zone_id_in IN ZONE.ID%TYPE, template_id_in TIMESLOT_TEMPLATE.ID%TYPE)
	   IS
	   	 BEGIN
		 	  DECLARE
			  		 work_date DATE;
							
					 BEGIN

						  IF (timeslot_date IS NOT NULL)
						  THEN
						  	  work_date := timeslot_date;
						  ELSE
							  work_date := SYSDATE + 8;
						  END IF;
						  
						  INSERT INTO TIMESLOT
						  		(ID, BEG_DATETIME, END_DATETIME, CUTOFF_DATETIME, STATUS_CODE, ZONE_ID, CAPACITY)
								SELECT SYSTEM_SEQ.NEXTVAL, 
								TO_DATE(TO_CHAR(work_date, 'MM/DD/YYYY') || ' '|| to_char(t.START_TIME, 'HH:MI:SS AM'), 'MM/DD/YYYY HH:MI:SS AM'),
								DECODE(LEAST(start_time, end_time),
								end_time,
								TO_DATE(TO_CHAR(work_date, 'MM/DD/YYYY')||' '|| to_char(end_time, 'HH:MI:SS AM'), 'MM/DD/YYYY HH:MI:SS AM')+1,
								TO_DATE(TO_CHAR(work_date, 'MM/DD/YYYY')||' '||to_char(end_time, 'HH:MI:SS AM'), 'MM/DD/YYYY HH:MI:SS AM')),
								TO_DATE(TO_CHAR(work_date, 'MM/DD/YYYY') || ' '|| to_char(t.CUTOFF_TIME, 'HH:MI:SS AM'), 'MM/DD/YYYY HH:MI:SS AM')-1,
								t.STATUS_CODE, c.zone_id, c.capacity
								FROM TIMESLOT_TEMPLATE_DATA t, CAPACITY_TEMPLATE_DATA c
								WHERE t.id = c.timeslot_template_data_id and t.TEMPLATE_ID = template_id_in and c.zone_id = zone_id_in
								AND status_code > 0
								AND t.DAYCODE = TO_CHAR(work_date, 'D');

						COMMIT;
		 			END;
			END;
/
