DECLARE
  X NUMBER;
BEGIN
  SYS.DBMS_JOB.SUBMIT
  ( job       => X 
   ,what      => 'CUST.DELIVERY_PASS_CRON(NULL)
  (SYSDATE );'
   ,next_date => to_date('12/14/2006 15:15:01','dd/mm/yyyy hh24:mi:ss')
   ,interval  => 'SYSDATE+60/1440 '
   ,no_parse  => TRUE
  );
  SYS.DBMS_OUTPUT.PUT_LINE('Job Number is: ' || to_char(x));  
END;
/



BEGIN
  SYS.DBMS_JOB.CHANGE
    (
      job        => X
     ,what       => 'CUST.DELIVERY_PASS_CRON
  (NULL);'
     ,next_date  => to_date('13/10/2006 18:54:59','dd/mm/yyyy hh24:mi:ss')
     ,interval   => 'SYSDATE+60/1440 '
    );
END;

  BEGIN  
  SYS.DBMS_JOB.CHANGE  
  ( job => 136  
  ,what => 'CUST.DELIVERY_PASS_CRON(NULL);'  
   ,next_date => to_date('13/10/2006 18:54:59','dd/mm/yyyy hh24:mi:ss')  7  
   ,interval => 'SYSDATE+60/1440 '  
   );  
   END;