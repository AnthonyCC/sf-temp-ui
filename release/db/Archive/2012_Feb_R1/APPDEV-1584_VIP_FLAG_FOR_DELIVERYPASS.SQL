CREATE OR REPLACE PROCEDURE CUST.VIP_FLAG_FOR_DELIVERYPASS IS
tmpVar NUMBER;

BEGIN

/** These customers have active deliverypass but VIP is set to false.
     The below update will activate VIP flag for these customers.
 **/
   update cust.profile p set P.PROFILE_VALUE='true'
WHERE P.PROFILE_NAME ='VIPCustomer' and P.CUSTOMER_ID IN (
select p.customer_id from cust.profile p, CUST.FDCUSTOMER fdc, cust.customer c 
where C.ID=FDC.ERP_CUSTOMER_ID and P.CUSTOMER_ID=FDC.ID
and P.PROFILE_NAME='VIPCustomer' and UPPER(P.PROFILE_VALUE)=UPPER('false' )and c.id
IN (select dp.customer_id from CUST.DELIVERY_PASS dp where  DP.EXP_DATE>sysdate and DP.STATUS in ('ACT','RTU'))
);
/** These customers are marked as VIP due to deliverypass but donot have an active deliverypass.
Unflag these VIP customers 
**/

update cust.profile p set P.PROFILE_VALUE='false'
WHERE P.PROFILE_NAME ='VIPCustomer' and P.CUSTOMER_ID IN (
select fdc.id from cust.profile p, CUST.FDCUSTOMER fdc, cust.customer c 
where C.ID=FDC.ERP_CUSTOMER_ID and P.CUSTOMER_ID=FDC.ID
and P.PROFILE_NAME='VIPCustomer' and UPPER(P.PROFILE_VALUE)=UPPER('true') 
and not exists (
select 1 from CUST.DELIVERY_PASS dp where DP.CUSTOMER_ID=c.id and DP.EXP_DATE>sysdate and DP.STATUS in ('ACT','RTU')
)
and not exists ( select 1 from cust.profile p where P.PROFILE_NAME='ChefsTable' and P.PROFILE_VALUE='1' and P.CUSTOMER_ID=fdc.id)
);

/** Inserts profile attribute 'VIPCustomer' with value 'true' for customers who have an active delivery pass but not flagged as VIP */
insert into cust.profile (customer_id,profile_name,profile_value, profile_type, priority, create_date) 
select DISTINCT fdc.id, 'VIPCustomer', 'true', 'S', 
    -1, SYSDATE from CUST.DELIVERY_PASS dp, cust.fdcustomer fdc where  DP.EXP_DATE>sysdate and DP.STATUS in ('ACT','RTU')
and FDC.ERP_CUSTOMER_ID=dp.customer_id
and not exists (
select 1 from cust.profile p, CUST.FDCUSTOMER fdc, cust.customer c 
where C.ID=FDC.ERP_CUSTOMER_ID and P.CUSTOMER_ID=FDC.ID
and P.PROFILE_NAME='VIPCustomer' and c.id=DP.CUSTOMER_ID
);
COMMIT;

END VIP_FLAG_FOR_DELIVERYPASS;
/
