CREATE OR REPLACE PROCEDURE DLV."RESOURCE_CRON" (ZONE_CODE_IN IN VARCHAR2, ZONE_ID_IN VARCHAR2, IS_DYNAMIC_IN VARCHAR2)
    IS
     BEGIN
      DECLARE

      CURSOR RESOURCE_CUR(DAY_IN DELIVERY_RESOURCE.DAY%TYPE) IS
        SELECT * FROM DLV.DELIVERY_RESOURCE
       WHERE DAY = DAY_IN AND ZONE_CODE = ZONE_CODE_IN;

      CURSOR TIMESLOT_CUR (RESOURCE_ID_IN TIMESLOT.RESOURCE_ID%TYPE) IS
        SELECT ZONE_ID, CAPACITY, BASE_DATE, START_TIME, END_TIME, CUTOFF_TIME, TRAFFIC_FACTOR, PLANNED_CAPACITY, STATUS, CT_CAPACITY,
        PREMIUM_CUTOFF_TIME, PREMIUM_CAPACITY, PREMIUM_CT_CAPACITY, ROUTING_START_TIME, ROUTING_END_TIME,
       MOD_START_X, MOD_CUTOFF_Y, DISPLAY_DATE, DISPLAY_TIME,IS_PRIMARY,SERVICE_TYPE,RELATIVE_CUTOFF_DAYS
        FROM DLV.TIMESLOT 
        WHERE RESOURCE_ID = RESOURCE_ID_IN;

      CURSOR SEQUENCE_CUR IS
        SELECT SYSTEM_SEQ.NEXTVAL FROM DUAL;

      BASE_DATE DATE;
      RESOURCE_ID VARCHAR2(16);
      RESOURCE_REC DELIVERY_RESOURCE%ROWTYPE;
      TIMESLOT_REC TIMESLOT_CUR%ROWTYPE;

      BEGIN
        BASE_DATE := TO_DATE(TO_CHAR(SYSDATE+7, 'MM/DD/YY'), 'MM/DD/YY');


        IF NOT RESOURCE_CUR%ISOPEN
        THEN
           OPEN RESOURCE_CUR(BASE_DATE);
        END IF;

        FETCH RESOURCE_CUR INTO RESOURCE_REC;
        IF RESOURCE_CUR%NOTFOUND
        THEN
           IF RESOURCE_CUR%ISOPEN
         THEN
        CLOSE RESOURCE_CUR;
        RESOURCE_REC := NULL;
         END IF;
         DBMS_OUTPUT.PUT_LINE('NO RESOURCE WAS FOUND FOR '||TO_CHAR(BASE_DATE, 'DAY MM/DD/YYYY'));

         IF NOT RESOURCE_CUR%ISOPEN
         THEN
            OPEN RESOURCE_CUR(TO_DATE(TO_CHAR(SYSDATE, 'MM/DD/YY'), 'MM/DD/YY'));
         END IF;
         FETCH RESOURCE_CUR INTO RESOURCE_REC;
         WHILE RESOURCE_CUR%FOUND
         LOOP
            DBMS_OUTPUT.PUT_LINE('NOW WORKING DATE IS '|| RESOURCE_REC.DAY);
          -- GETTING THE NEW RESOURCE ID FROM SEQUENCE
          IF NOT SEQUENCE_CUR%ISOPEN
          THEN
             OPEN SEQUENCE_CUR;
          END IF;
          FETCH SEQUENCE_CUR INTO RESOURCE_ID;
           DBMS_OUTPUT.PUT_LINE('RESOURCE ID '|| RESOURCE_ID);
          IF SEQUENCE_CUR%ISOPEN
          THEN
             CLOSE SEQUENCE_CUR;
          END IF;
          INSERT INTO DELIVERY_RESOURCE (ID, ZONE_CODE, DAY, PEOPLE, DELIVERY_RATE, NAME)
          VALUES(RESOURCE_ID, RESOURCE_REC.ZONE_CODE, BASE_DATE, RESOURCE_REC.PEOPLE, RESOURCE_REC.DELIVERY_RATE, RESOURCE_REC.NAME);

          -- GETTING TIMESLOTS FOR LAST SAME DAY AND INSERTING AS AS NEW RESOURCE FOR 8 DAY
          IF NOT TIMESLOT_CUR%ISOPEN
          THEN
             OPEN TIMESLOT_CUR(RESOURCE_REC.ID);
          END IF;
          FETCH TIMESLOT_CUR INTO TIMESLOT_REC;
          WHILE TIMESLOT_CUR%FOUND
          LOOP
             INSERT INTO TIMESLOT(ID, ZONE_ID, CAPACITY, BASE_DATE, START_TIME, END_TIME, CUTOFF_TIME, TRAFFIC_FACTOR, PLANNED_CAPACITY, STATUS, RESOURCE_ID,
            CT_CAPACITY, IS_DYNAMIC, PREMIUM_CUTOFF_TIME, PREMIUM_CAPACITY, PREMIUM_CT_CAPACITY, ROUTING_START_TIME, ROUTING_END_TIME,
            MOD_START_X, MOD_CUTOFF_Y, DISPLAY_DATE, DISPLAY_TIME, IS_PRIMARY, SERVICE_TYPE, RELATIVE_CUTOFF_DAYS )
           VALUES(SYSTEM_SEQ.NEXTVAL, ZONE_ID_IN, TIMESLOT_REC.CAPACITY, BASE_DATE, TIMESLOT_REC.START_TIME, TIMESLOT_REC.END_TIME, TIMESLOT_REC.CUTOFF_TIME,
           TIMESLOT_REC.TRAFFIC_FACTOR,    TIMESLOT_REC.PLANNED_CAPACITY,    TIMESLOT_REC.STATUS,  RESOURCE_ID,  TIMESLOT_REC.CT_CAPACITY, IS_DYNAMIC_IN,
           TIMESLOT_REC.PREMIUM_CUTOFF_TIME,
           TIMESLOT_REC.PREMIUM_CAPACITY,
           TIMESLOT_REC.PREMIUM_CT_CAPACITY,
           TIMESLOT_REC.ROUTING_START_TIME,
           TIMESLOT_REC.ROUTING_END_TIME , TIMESLOT_REC.MOD_START_X,
           TIMESLOT_REC.MOD_CUTOFF_Y, TIMESLOT_REC.DISPLAY_DATE + 7, TIMESLOT_REC.DISPLAY_TIME,TIMESLOT_REC.IS_PRIMARY, TIMESLOT_REC.SERVICE_TYPE, TIMESLOT_REC.RELATIVE_CUTOFF_DAYS );

             FETCH TIMESLOT_CUR INTO TIMESLOT_REC;
          END LOOP;

          IF TIMESLOT_CUR%ISOPEN
          THEN
            CLOSE TIMESLOT_CUR;
          TIMESLOT_REC := NULL;
          END IF;


          FETCH RESOURCE_CUR INTO RESOURCE_REC;
         END LOOP;

        END IF;

        COMMIT;
      END;

  END;
/

