CREATE OR REPLACE PROCEDURE CUST.DP_FLAG_AUTORENEW_CRON IS

/******************************************************************************
   NAME:       DP_FLAG_AUTORENEW_CRON
   PURPOSE:    

   REVISIONS:
   Ver        Date        Author           Description
   ---------  ----------  ---------------  ------------------------------------
   1.0        8/13/2007          1. Created this procedure.

   NOTES:

   Automatically available Auto Replace Keywords:
      Object Name:     DP_FLAG_AUTORENEW_CRON
      Sysdate:         8/13/2007
      Date and Time:   8/13/2007, 3:31:13 PM, and 8/13/2007 3:31:13 PM
      Username:         (set in TOAD Options, Procedure Editor)
      Table Name:       (set in the "New PL/SQL Object" dialog)

******************************************************************************/
BEGIN
  		update cust.customerinfo set has_autorenew_dp='Y' 
	   				where customer_id IN 
       					( select ci.customer_id from cust.delivery_pass dp, 
	                                			     cust.customerinfo ci 
									where dp.customer_id=ci.customer_id 
									and ci.has_autorenew_dp is NULL and 
									status IN ('RTU','ACT') and 
	   			   					trunc(exp_date) > trunc(sysdate) and 
									type IN (select sku_code from cust.dlv_pass_type where IS_AUTORENEW_DP='Y')
					);
	
               DBMS_OUTPUT.PUT_LINE ('Updated ' || SQL%ROWCOUNT || ' records to autoRenew customers');
		
		COMMIT;
END DP_FLAG_AUTORENEW_CRON;
/
