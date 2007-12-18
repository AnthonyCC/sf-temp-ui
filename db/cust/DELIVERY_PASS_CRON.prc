CREATE OR REPLACE PROCEDURE DELIVERY_PASS_CRON(request_date IN DATE)
	IS
	BEGIN
		 /*  Update delivery pass status to SSD when the original order got short shipped.   */
		UPDATE cust.delivery_pass SET status='SSD' WHERE status='PEN' AND purchase_order_id IN (SELECT /*+ USE_NL(s, sa, sa2) */s.id FROM cust.sale s, cust.salesaction sa, cust.salesaction sa2 WHERE sa.requested_date = TRUNC(request_date) AND sa.sale_id = s.id AND sa.action_type IN ('CRO', 'MOD') AND s.status = 'STL' AND sa.action_date = (SELECT MAX(action_date) FROM cust.salesaction WHERE sale_id = s.id AND action_type IN ('CRO', 'MOD')) AND s.id = sa2.sale_id AND sa2.action_type = 'INV' AND sa2.amount = 0);


		DBMS_OUTPUT.PUT_LINE ('Updated ' || SQL%ROWCOUNT || ' records to SSD status');
		
		/* Credit back one delivery to the passes that were used by a short shipped order.*/
		UPDATE cust.delivery_pass SET rem_num_dlvs = rem_num_dlvs + 1 WHERE status='ACT' AND id IN (SELECT /*+ USE_NL(s, sa, sa2) */s.dlv_pass_id FROM cust.sale s, cust.salesaction sa, cust.salesaction sa2 WHERE sa.requested_date = TRUNC(request_date) AND sa.sale_id = s.id AND sa.action_type IN ('CRO', 'MOD') AND s.status = 'STL' AND sa.action_date = (SELECT MAX(action_date) FROM cust.salesaction WHERE sale_id = s.id AND action_type IN ('CRO', 'MOD')) AND s.id = sa2.sale_id AND sa2.action_type = 'INV' AND sa2.amount = 0);


		DBMS_OUTPUT.PUT_LINE ('Credited ' || SQL%ROWCOUNT || ' records with one delivery');

		 /*  Update delivery pass status to ACT when the original order got delivered.   */	
		UPDATE cust.delivery_pass SET status='ACT' WHERE status IN ('PEN','STF') AND purchase_order_id IN (SELECT /*+ USE_NL(s, sa) */s.id FROM cust.sale s, cust.salesaction sa WHERE sa.requested_date = TRUNC(request_date) AND sa.sale_id = s.id AND sa.action_type IN ('CRO', 'MOD') AND s.status IN ('ENR','PPG') AND sa.action_date = (SELECT MAX(action_date) FROM cust.salesaction WHERE sale_id = s.id AND action_type IN ('CRO', 'MOD')));


		DBMS_OUTPUT.PUT_LINE ('Updated ' || SQL%ROWCOUNT || ' records to ACT status');

		 /*  Update delivery pass status from EPG (Expired pending) to EXP when the last-placed order got delivered.   */		
		UPDATE cust.delivery_pass dlvpass SET status='EXP' WHERE status = 'EPG' AND 
		(SELECT /*+ USE_NL(s, sa) */COUNT(s.id) FROM cust.sale s, cust.salesaction sa WHERE sa.requested_date = TRUNC(request_date) AND sa.sale_id = s.id AND sa.action_type IN ('CRO', 'MOD') AND s.status <> ('CAN') AND s.dlv_pass_id = dlvpass.id AND sa.action_date = (SELECT MAX(action_date) FROM cust.salesaction WHERE sale_id = s.id AND action_type IN ('CRO', 'MOD'))) = 
		(SELECT /*+ USE_NL(s, sa) */COUNT(s.id) FROM cust.sale s, cust.salesaction sa WHERE sa.requested_date = TRUNC(request_date) AND sa.sale_id = s.id AND sa.action_type IN ('CRO', 'MOD') AND s.status NOT IN ('ENR', 'PPG') AND s.dlv_pass_id = dlvpass.id AND sa.action_date = (SELECT MAX(action_date) FROM cust.salesaction WHERE sale_id = s.id AND action_type IN ('CRO', 'MOD')));
		
		DBMS_OUTPUT.PUT_LINE ('Updated ' || SQL%ROWCOUNT || ' records to EXP status');
		
		COMMIT;

	END;
/

