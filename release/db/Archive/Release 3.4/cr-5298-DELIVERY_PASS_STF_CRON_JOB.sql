DECLARE
  X NUMBER;
BEGIN
  SYS.DBMS_JOB.SUBMIT
  ( job       => X 
   ,what      => 'CUST.DELIVERY_PASS_STF_CRON;'
   ,next_date => to_date('14/12/2006 15:15:00','dd/mm/yyyy hh24:mi:ss')
   ,interval  => 'TRUNC(SYSDATE+1)+13/24'
   ,no_parse  => TRUE
  );
  SYS.DBMS_OUTPUT.PUT_LINE('Job Number is: ' || to_char(x));
END;
/



