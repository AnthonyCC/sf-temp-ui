create or replace procedure loadCustSegmentation
is 
   metalCategoryName varchar2(40) := 'MetalCategory';
   chefsTableName    varchar2(40) := 'ChefsTable';
   badCount          number (10)  :=  0;
     
BEGIN
    /*  remove all the existing profile entries for metalCategory and chefsTable   */
      DBMS_OUTPUT.PUT_Line('About to rename profile names '||to_char(sysdate,'MM/DD/YYYY HH:MI:SS'));
       update cust.dm_profile set profile_name = 'ChefsTable' where profile_name='CHEFS_TABLE';
       update cust.dm_profile set profile_name = 'MetalCategory' where profile_name='CUSTOMER_SEGMENT';
       commit;
      DBMS_OUTPUT.PUT_Line('Rename process complete: '||to_char(sysdate,'MM/DD/YYYY HH:MI:SS'));
    
      DBMS_OUTPUT.PUT_Line('About to start removal of old profile values: '||to_char(sysdate,'MM/DD/YYYY HH:MI:SS'));
       delete from cust.profile where profile_name =metalCategoryName or profile_name=chefsTableName;
      DBMS_OUTPUT.PUT_Line('Removal Complete About to Load new data: '||to_char(sysdate,'MM/DD/YYYY HH:MI:SS'));
       commit; 
    
      insert into cust.profile 
      (select customer_id,'S',profile_name,profile_value,-1 from cust.dm_profile  
      where exists (select 1 from cust.fdcustomer where id=cust.dm_profile.customer_id) );
     DBMS_OUTPUT.PUT_LINE('New data Load is complete: '||to_char(sysdate,'MM/DD/YYYY HH:MI:SS'));
     commit;  
        
END;