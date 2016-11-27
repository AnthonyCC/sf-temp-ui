CREATE OR REPLACE PROCEDURE FDSTORE_PRDA."RESERVATION_CLEANER" (day_of_week_param IN NUMBER)IS

BEGIN
               DECLARE

                     CURSOR reservation_cleaner_cur(day_of_week_in DLV.RECURRING_RESERVATION.RSV_DAY_OF_WEEK%TYPE) is
                             SELECT CI.CUSTOMER_ID,CI.RSV_DAY_OF_WEEK,CI.RSV_START_TIME,CI.RSV_END_TIME,CI.RSV_ADDRESS_ID
                            FROM DLV.RECURRING_RESERVATION CI 
                            WHERE RSV_ADDRESS_ID IS NOT NULL and CI.RSV_DAY_OF_WEEK = day_of_week_in
                            FOR UPDATE OF RSV_DAY_OF_WEEK,RSV_START_TIME,RSV_END_TIME,RSV_ADDRESS_ID;

                    CURSOR reservation_count_cur(customer_id_in DLV.RECURRING_RESERVATION.CUSTOMER_ID%TYPE) is
                            SELECT COUNT(*)
                            FROM dlv.reservation r
                            where (status_code='20' or status_code='15') and last_actionby <> 'SYS' and type='WRR' and expiration_datetime> (sysdate-7) and customer_id= customer_id_in
                            and not exists( select 1 from dlv.reservation where customer_id=r.customer_id and status_code='10' and type='WRR' and expiration_datetime> (sysdate-7) )
                            ;


                        customer_info_rec reservation_cleaner_cur%ROWTYPE;
                        reservation_count NUMBER:=0;
                        day_of_week NUMBER:=0;
                     BEGIN
                           IF day_of_week_param IS NULL
                          THEN
                                 day_of_week :=TO_CHAR(sysdate,'D');
                          ELSE
                                day_of_week :=day_of_week_param;
                          END IF;

                          IF NOT reservation_cleaner_cur%ISOPEN
                          THEN
                                OPEN reservation_cleaner_cur(day_of_week);
                          END IF;

                        LOOP
                          FETCH reservation_cleaner_cur into customer_info_rec;
                          EXIT WHEN reservation_cleaner_cur%NOTFOUND;

                            OPEN reservation_count_cur(customer_info_rec.customer_id);

                            FETCH reservation_count_cur into reservation_count;

                            CLOSE reservation_count_cur;


                            IF reservation_count>=1
                            THEN
                                UPDATE DLV.RECURRING_RESERVATION
                                SET RSV_DAY_OF_WEEK = 0, RSV_START_TIME= NULL, RSV_END_TIME=NULL, RSV_ADDRESS_ID =NULL
                                WHERE CURRENT OF reservation_cleaner_cur;

                                insert into cust.activity_log (timestamp,customer_id, activity_id, source,initiator,note,sale_id,dlv_pass_id,reason)
                                values(sysdate, customer_info_rec.customer_id, 'Cancel Pre-Rsv', 'SYS', 'SYSTEM', 'weekly reservation not used in past  week', null, null, null);

                            END IF;

                        END LOOP;

                        CLOSE reservation_cleaner_cur;

                          COMMIT;

                     END;

END RESERVATION_CLEANER;
/

