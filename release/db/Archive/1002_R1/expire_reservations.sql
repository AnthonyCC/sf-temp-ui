CREATE OR REPLACE
PROCEDURE DLV.EXPIRE_RESERVATIONS AS
BEGIN
update DLV.reservation set status_code = 20  where expiration_datetime <= sysdate and status_code = 5 and IN_UPS is null;
update DLV.reservation set status_code = 20  where expiration_datetime <= sysdate and status_code = 5 and UNASSIGNED_ACTION is not null;
update DLV.reservation set status_code = 20, UNASSIGNED_DATETIME = sysdate,UNASSIGNED_ACTION = 'CANCEL_TIMESLOT', MODIFIED_DTTM = sysdate where expiration_datetime <= sysdate and status_code = 5 and UNASSIGNED_ACTION is null and IN_UPS = 'X';
commit;
END;
/
