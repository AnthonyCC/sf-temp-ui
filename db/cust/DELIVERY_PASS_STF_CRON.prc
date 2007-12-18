CREATE OR REPLACE PROCEDURE DELIVERY_PASS_STF_CRON IS
	BEGIN
		
		/*  Update active or pending delivery pass to STF status when the original order had a settement failed.   */	
		UPDATE cust.delivery_pass SET status='STF' WHERE purchase_order_id IN (SELECT id FROM cust.sale s WHERE s.id IN (SELECT purchase_order_id from cust.delivery_pass where status = 'ACT') and s.status = 'STF');
		
		DBMS_OUTPUT.PUT_LINE ('Updated ' || SQL%ROWCOUNT || ' records to STF status');		
		
		COMMIT;

	END;
/

