CREATE OR REPLACE PROCEDURE REM_WEEKLY_RSV_3_NOTUSED IS
BEGIN
  --Archive the weekly reservation settings
  INSERT INTO CUST.RESERVATION_ARCHIVE
  SELECT sysdate, customer_id, rsv_day_of_week, rsv_start_time, rsv_end_time, 'Unused for 3 weeks'
  FROM CUST.customerinfo
  WHERE customer_id in (SELECT customer_id 
  					   	FROM (SELECT COUNT(*) cnt, customer_id
							  FROM DLV.RESERVATION
							  WHERE type = 'WRR' AND status_code = 20
							  AND expiration_datetime > sysdate - 21
							  and customer_id not in (SELECT customer_id
							  	  			  	  	  FROM DLV.RESERVATION
													  WHERE type = 'WRR' and status_code = 10)
							  )
					    )
  and rsv_start_time is not null;
  
  --Remove the weekly reservation settings from customer
  UPDATE CUST.CUSTOMERINFO
  SET rsv_start_time = null, rsv_end_time = null, rsv_day_of_week = null, rsv_address_id = null
  WHERE customer_id in (SELECT customer_id 
  					   	FROM (SELECT COUNT(*) cnt, customer_id
							  FROM DLV.RESERVATION
							  WHERE type = 'WRR' AND status_code = 20
							  AND expiration_datetime > sysdate - 21
							  and customer_id not in (SELECT customer_id
							  	  			  	  	  FROM DLV.RESERVATION
													  WHERE type = 'WRR' and status_code = 10)
							  )
					    )
  and rsv_start_time is not null;
  
  --Remove ther reservations that are already made.
  DELETE FROM DLV.RESERVATION 
  WHERE customer_id in (SELECT customer_id 
  					   	FROM (SELECT COUNT(*) cnt, customer_id
							  FROM DLV.RESERVATION
							  WHERE type = 'WRR' AND status_code = 20
							  AND expiration_datetime > sysdate - 21
							  and customer_id not in (SELECT customer_id
							  	  			  	  	  FROM DLV.RESERVATION
													  WHERE type = 'WRR' and status_code = 10)
							  )
					    )
  and status_code = 5 and type = 'WRR';
  
  COMMIT; 
						 
END REM_WEEKLY_RSV_3_NOTUSED;

/