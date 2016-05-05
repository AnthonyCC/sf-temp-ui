CREATE OR REPLACE PROCEDURE CUST.CANCEL_AUF_DP_RENEWALS AS
    CURSOR c1 IS
 select s.id sale,s.customer_id from cust.sale s, cust.salesaction sa where s.id=sa.sale_id and s.cromod_Date=sa.action_date and sa.action_type in ('CRO','MOD') and s.type='SUB' and s.status='AUF' and SA.REQUESTED_DATE<trunc(sysdate-15)
and not exists (Select 1 from CUST.DELIVERY_PASS dp where DP.STATUS in ('ACT','RTU' ) and DP.EXP_DATE>trunc(sysdate) and DP.CUSTOMER_ID=S.CUSTOMER_ID) ;
 

BEGIN
   
   FOR line IN c1 LOOP  
   UPDATE cust.sale s set s.status='CAN' where s.id=line.sale;
   update cust.delivery_pass dp set dp.status='CAO' where DP.PURCHASE_ORDER_ID=line.sale;
      update cust.customerinfo ci set CI.HAS_AUTORENEW_DP='N' where CI.CUSTOMER_ID=line.customer_id and CI.HAS_AUTORENEW_DP='Y';   
       IF SQL%ROWCOUNT >0 THEN
      Insert into CUST.ACTIVITY_LOG   (timestamp, customer_id, activity_id, source, initiator, note) Values   (SYSDATE, line.customer_id, 'DP AR OFF', 'SYS', 'SYSTEM',     'AutoRenew DP flag turned OFF');
      END IF;
     update cust.case c set C.CASE_STATE='CLSD', C.LAST_ACTION_DATE=SYSDATE where C.SALE_ID=line.sale and C.CASE_SUBJECT='ASQ-003' and C.CASE_STATE='OPEN';
     COMMIT;
    END LOOP;
END;


--DBSTG
GRANT EXECUTE ON CUST.CANCEL_AUF_DP_RENEWALS TO FDSTORE_STSTG01;
 
--DBSTO 
GRANT EXECUTE ON CUST.CANCEL_AUF_DP_RENEWALS TO FDSTORE_STPRD01;
