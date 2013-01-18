CREATE OR REPLACE PROCEDURE CAL_ZIP_COVERAGE(service_type REGION.SERVICE_TYPE%TYPE) AS
	   BEGIN
	   		DECLARE
				   CURSOR coverage_cur(service_type_in REGION.SERVICE_TYPE%TYPE) is
				   SELECT * 
				   FROM (SELECT zipcode, sum(sdo_geom.sdo_area(sdo_geom.sdo_intersection(zones.geoloc, zip.geoloc, .5), .5)/sdo_geom.sdo_area(zip.geoloc, .5)) as coverage 
				   		FROM dlv.zipcode zip, (SELECT zn.zone_code, zn.geoloc, rd.start_date  
				   			 FROM dlv.region r, dlv.region_data rd, dlv.zone zn
							 WHERE r.id = rd.region_id and zn.region_data_id = rd.id and r.service_type = service_type_in
							 AND rd.start_date = (SELECT max(start_date) 
							 	 			   	 		 FROM dlv.region_data rd1 
														 WHERE rd1.region_id = r.id and rd1.start_date <= sysdate + 8
												  )
												) zones
							 GROUP BY zipcode ORDER BY zipcode) 
					WHERE coverage > 0.1;
				   
				    coverage_rec coverage_cur%ROWTYPE;
				   
		    BEGIN
				 IF NOT coverage_cur%ISOPEN
				 THEN
				 	 OPEN coverage_cur(service_type);
				 END IF;
						   
				 FETCH coverage_cur into coverage_rec;
				 WHILE coverage_cur%FOUND
				 LOOP
				 	 --DBMS_OUTPUT.PUT_LINE('ZIPCODE: ' ||coverage_rec.ZIPCODE|| ' COVERAGE: ' ||coverage_rec.coverage);
					 IF 'HOME' = service_type
					 THEN
					 	 UPDATE ZIPCODE set HOME_COVERAGE = coverage_rec.coverage
						 WHERE zipcode = coverage_rec.zipcode;
					  END IF;
					  
					  IF 'CORPORATE' = service_type
					  THEN
					  	  UPDATE ZIPCODE set COS_COVERAGE = coverage_rec.coverage
						  WHERE zipcode = coverage_rec.zipcode;
					  END IF;
					 
					 FETCH coverage_cur into coverage_rec;
				 END LOOP;
						   
			     IF coverage_cur%ISOPEN
			     THEN
			   	 	 CLOSE coverage_cur;
			     END IF;
			
			END;

	   END;
/
