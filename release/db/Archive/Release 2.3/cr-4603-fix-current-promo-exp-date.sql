update cust.promotion set expiration_date = to_date(to_char(expiration_date-1,'MM-DD-YYYY') || ' 11:59:59 PM', 'MM-DD-YYYY HH:MI:SS AM')  
where trunc(EXPIRATION_DATE) = EXPIRATION_DATE and EXPIRATION_DATE > SYSDATE - 1;
commit;