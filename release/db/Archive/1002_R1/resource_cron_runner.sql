CREATE OR REPLACE PROCEDURE DLV.RESOURCE_CRON_RUNNER
	   IS
		 BEGIN
		 	  DECLARE
			  		 CURSOR region_cur IS
					 		SELECT DISTINCT id
							FROM REGION;

					 CURSOR region_data_cur(region_id_in REGION.ID%TYPE) IS
					 		SELECT ID, START_DATE FROM REGION_DATA
			  				WHERE REGION_ID = region_id_in
							AND START_DATE = (SELECT MAX(START_DATE) FROM REGION_DATA WHERE START_DATE <= SYSDATE+8 AND REGION_ID = region_id_in);

					 CURSOR zone_cur (region_data_id_in REGION_DATA.ID%TYPE) IS
					         SELECT z.ID ID, z.ZONE_CODE ZONE_CODE, a.ACTIVE IS_DYNAMIC FROM ZONE z, transp.zone  zd, transp.trn_area a
       							WHERE z.REGION_DATA_ID = region_data_id_in and z.ZONE_CODE = zd.ZONE_CODE and zd.AREA = a.CODE;

					 zone_rec zone_cur%ROWTYPE;
					 region_id REGION.ID%TYPE;
					 region_data_rec region_data_cur%ROWTYPE;

					 BEGIN
					 	  IF NOT region_cur%ISOPEN
						  THEN
						  	 OPEN region_cur;
						  END IF;
						  FETCH region_cur INTO region_id;
						  --region loop
						  WHILE region_cur%FOUND
						  LOOP
						  	  IF NOT region_data_cur%ISOPEN
						  	  THEN
						  	  	  OPEN region_data_cur(region_id);
						  	  END IF;

						  	  FETCH region_data_cur INTO region_data_rec;

							  IF NOT zone_cur%ISOPEN
							  THEN
							 	  OPEN zone_cur(region_data_rec.ID);
							  END IF;
							  FETCH zone_cur INTO zone_rec;

							  WHILE zone_cur%FOUND
							  LOOP
							  	  RESOURCE_CRON(zone_rec.ZONE_CODE, zone_rec.ID, zone_rec.IS_DYNAMIC);
								  --DBMS_OUTPUT.PUT_LINE('REGION_ID '||region_id||' ZONE_CODE '||zone_rec.zone_code||' ZONE_ID '||zone_rec.id);
								  FETCH zone_cur INTO zone_rec;
							  END LOOP;

							 IF region_data_cur%ISOPEN
							 THEN
								 CLOSE region_data_cur;
								 region_data_rec := null;
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
					 END;
			 END;
/
