CREATE OR REPLACE PROCEDURE "CUST"."DELIVERY_PASS_CRON" (request_date IN DATE)
	IS
	BEGIN
		IF request_date IS NULL THEN

			 /*  Update delivery pass status to SSD when the original order got short shipped.   */
			UPDATE cust.delivery_pass SET status='SSD' WHERE status='PEN' AND purchase_order_id IN (SELECT /*+ USE_NL(s, sa, sa2) */s.id FROM cust.sale s, cust.salesaction sa, cust.salesaction sa2 WHERE sa.requested_date >= TRUNC(SYSDATE - 10) AND sa.sale_id = s.id AND sa.action_type IN ('CRO', 'MOD') AND s.status = 'STL' AND sa.action_date = (SELECT MAX(action_date) FROM cust.salesaction WHERE sale_id = s.id AND action_type IN ('CRO', 'MOD')) AND s.id = sa2.sale_id AND sa2.action_type = 'INV' AND sa2.amount = 0 AND sa2.sub_total = 0);


			DBMS_OUTPUT.PUT_LINE ('Updated ' || SQL%ROWCOUNT || ' records to SSD status');

			/* Credit back one delivery to the BSGS passes that were used by a short shipped order.*/
			UPDATE cust.delivery_pass SET rem_num_dlvs = rem_num_dlvs + 1, status = 'RTU' WHERE status IN ('ACT','EPG') AND type IN (SELECT sku_code from cust.dlv_pass_type WHERE is_unlimited = 'N') AND id IN (SELECT /*+ USE_NL(s, sa, sa2) */s.dlv_pass_id FROM cust.sale s, cust.salesaction sa, cust.salesaction sa2 WHERE sa.requested_date >= TRUNC(SYSDATE - 10) AND sa.sale_id = s.id AND sa.action_type IN ('CRO', 'MOD') AND s.status = 'STL' AND sa.action_date = (SELECT MAX(action_date) FROM cust.salesaction WHERE sale_id = s.id AND action_type IN ('CRO', 'MOD')) AND s.id = sa2.sale_id AND sa2.action_type = 'INV' AND sa2.amount = 0 AND sa2.sub_total = 0 AND (SELECT COUNT(id) FROM cust.salesaction WHERE action_type = 'RET' AND sale_id = s.id) = 0);


			DBMS_OUTPUT.PUT_LINE ('Credited ' || SQL%ROWCOUNT || ' records with one delivery');

			 /*  Update delivery pass status to RTU when the original order got delivered. --Modified  to account for orders using gift card as payment method */
			UPDATE cust.delivery_pass SET status='RTU' WHERE status IN ('PEN','STF') AND purchase_order_id IN (SELECT /*+ USE_NL(s, sa) */s.id FROM cust.sale s, cust.salesaction sa WHERE sa.requested_date >= trunc(SYSDATE -10) AND sa.sale_id = s.id AND sa.action_date =s.cromod_date and sa.action_type IN ('CRO', 'MOD') AND s.status IN ( 'PPG','STP') );

			DBMS_OUTPUT.PUT_LINE ('Updated ' || SQL%ROWCOUNT || ' records to ACT status');

			 /*  Update delivery pass status to RTU when the original order was a make good order/
				     entire order was discounted because customer had enough store credits.   */
			UPDATE cust.delivery_pass SET status='RTU' WHERE status IN ('PEN') AND purchase_order_id IN (SELECT /*+ USE_NL(s, sa, sa2) */s.id FROM cust.sale s, cust.salesaction sa, cust.salesaction sa2 WHERE sa.requested_date >= TRUNC(SYSDATE - 10) AND sa.sale_id = s.id AND sa.action_type IN ('CRO', 'MOD') AND s.status = 'STL' AND sa.action_date = (SELECT MAX(action_date) FROM cust.salesaction WHERE sale_id = s.id AND action_type IN ('CRO', 'MOD')) AND s.id = sa2.sale_id AND sa2.action_type = 'INV' AND sa2.amount = 0 AND sa2.sub_total > 0);


			DBMS_OUTPUT.PUT_LINE ('Updated ' || SQL%ROWCOUNT || ' make good orders containing delivery pass to RTU status ');

			 /*  Update delivery pass status from EPG (Expired pending) to EXP when the last-placed orders got delivered.   */
			UPDATE cust.delivery_pass SET status = 'EXP'  WHERE id NOT IN (SELECT dp.id  FROM cust.delivery_pass dp, cust.sale s WHERE s.dlv_pass_id IS NOT NULL AND dp.id = s.dlv_pass_id AND dp.status = 'EPG' AND s.status IN ('SUB','AUT','PRC','ENR','REF','NEW','MOD','MOC','AUF','AVE')) AND status = 'EPG';

			DBMS_OUTPUT.PUT_LINE ('Updated ' || SQL%ROWCOUNT || ' records to EXP status');
			
			 /*  Update delivery pass status to RTU when the original order got delivered and order is on FD account*/
			UPDATE cust.delivery_pass SET status='RTU' WHERE status IN ('PEN','STF') AND purchase_order_id IN (SELECT s.id FROM cust.sale s, cust.salesaction sa,cust.customer c, cust.fdcustomer fdc, cust.profile p   WHERE sa.requested_date >= trunc(SYSDATE -10) AND sa.sale_id = s.id and sa.customer_id=s.customer_id AND sa.action_date =s.cromod_date and sa.action_type IN ('CRO', 'MOD') AND s.status='STL' and S.CUSTOMER_ID=C.ID and c.id=FDC.ERP_CUSTOMER_ID and FDC.ID=P.CUSTOMER_ID and P.PROFILE_NAME='onFDAccount' and UPPER( P.PROFILE_VALUE)='TRUE'  );
			DBMS_OUTPUT.PUT_LINE ('Updated ' || SQL%ROWCOUNT || ' records to RTU status');

		ELSE

			 /*  Update delivery pass status to SSD when the original order got short shipped.   */
			UPDATE cust.delivery_pass SET status='SSD' WHERE status='PEN' AND purchase_order_id IN (SELECT /*+ USE_NL(s, sa, sa2) */s.id FROM cust.sale s, cust.salesaction sa, cust.salesaction sa2 WHERE sa.requested_date = TRUNC(request_date) AND sa.sale_id = s.id AND sa.action_type IN ('CRO', 'MOD') AND s.status = 'STL' AND sa.action_date = (SELECT MAX(action_date) FROM cust.salesaction WHERE sale_id = s.id AND action_type IN ('CRO', 'MOD')) AND s.id = sa2.sale_id AND sa2.action_type = 'INV' AND sa2.amount = 0 AND sa2.sub_total = 0);


			DBMS_OUTPUT.PUT_LINE ('Updated ' || SQL%ROWCOUNT || ' records to SSD status');

			/* Credit back one delivery to the BSGS passes that were used by a short shipped order.*/
			UPDATE cust.delivery_pass SET rem_num_dlvs = rem_num_dlvs + 1, status = 'RTU' WHERE status IN ('ACT','EPG') AND type IN (SELECT sku_code from cust.dlv_pass_type WHERE is_unlimited = 'N') AND id IN (SELECT /*+ USE_NL(s, sa, sa2) */s.dlv_pass_id FROM cust.sale s, cust.salesaction sa, cust.salesaction sa2 WHERE sa.requested_date = TRUNC(request_date) AND sa.sale_id = s.id AND sa.action_type IN ('CRO', 'MOD') AND s.status = 'STL' AND sa.action_date = (SELECT MAX(action_date) FROM cust.salesaction WHERE sale_id = s.id AND action_type IN ('CRO', 'MOD')) AND s.id = sa2.sale_id AND sa2.action_type = 'INV' AND sa2.amount = 0 AND sa2.sub_total = 0 AND (SELECT COUNT(id) FROM cust.salesaction WHERE action_type = 'RET' AND sale_id = s.id) = 0);


			DBMS_OUTPUT.PUT_LINE ('Credited ' || SQL%ROWCOUNT || ' records with one delivery');

			 /*  Update delivery pass status to RTU when the original order got delivered.  --Modified  to account for orders using gift card as payment method  */
			UPDATE cust.delivery_pass SET status='RTU' WHERE status IN ('PEN','STF') AND purchase_order_id IN (SELECT /*+ USE_NL(s, sa) */s.id FROM cust.sale s, cust.salesaction sa WHERE sa.requested_date = TRUNC(request_date) AND sa.sale_id = s.id AND sa.action_date =s.cromod_date and sa.action_type IN ('CRO', 'MOD') AND s.status IN ( 'PPG','STP') );
			DBMS_OUTPUT.PUT_LINE ('Updated ' || SQL%ROWCOUNT || ' records to RTU status');

			 /*  Update delivery pass status to RTU when the original order was a make good order/
				     entire order was discounted because customer had enough store credits.   */
			UPDATE cust.delivery_pass SET status='RTU' WHERE status IN ('PEN') AND purchase_order_id IN (SELECT /*+ USE_NL(s, sa, sa2) */s.id FROM cust.sale s, cust.salesaction sa, cust.salesaction sa2 WHERE sa.requested_date = TRUNC(request_date) AND sa.sale_id = s.id AND sa.action_type IN ('CRO', 'MOD') AND s.status = 'STL' AND sa.action_date = (SELECT MAX(action_date) FROM cust.salesaction WHERE sale_id = s.id AND action_type IN ('CRO', 'MOD')) AND s.id = sa2.sale_id AND sa2.action_type = 'INV' AND sa2.amount = 0 AND sa2.sub_total > 0);


			DBMS_OUTPUT.PUT_LINE ('Updated ' || SQL%ROWCOUNT || ' make good orders containing delivery pass to RTU status ');

			 /*  Update delivery pass status from EPG (Expired pending) to EXP when the last-placed orders got delivered.   */
			UPDATE cust.delivery_pass SET status = 'EXP'  WHERE id NOT IN (SELECT dp.id  FROM cust.delivery_pass dp, cust.sale s WHERE s.dlv_pass_id IS NOT NULL AND dp.id = s.dlv_pass_id AND dp.status = 'EPG' AND s.status IN ('SUB','PRC','ENR','REF','NEW','MOD','MOC','AUF','AVE')) AND status = 'EPG';

			DBMS_OUTPUT.PUT_LINE ('Updated ' || SQL%ROWCOUNT || ' records to EXP status');
			
			/*  Update delivery pass status to RTU when the original order got delivered and order is on FD account*/
			UPDATE cust.delivery_pass SET status='RTU' WHERE status IN ('PEN','STF') AND purchase_order_id IN (SELECT s.id FROM cust.sale s, cust.salesaction sa,cust.customer c, cust.fdcustomer fdc, cust.profile p   WHERE sa.requested_date = TRUNC(request_date) AND sa.sale_id = s.id and sa.customer_id=s.customer_id AND sa.action_date =s.cromod_date and sa.action_type IN ('CRO', 'MOD') AND s.status='STL' and S.CUSTOMER_ID=C.ID and c.id=FDC.ERP_CUSTOMER_ID and FDC.ID=P.CUSTOMER_ID and P.PROFILE_NAME='onFDAccount' and UPPER( P.PROFILE_VALUE)='TRUE'  );
			DBMS_OUTPUT.PUT_LINE ('Updated ' || SQL%ROWCOUNT || ' records to RTU status');



		END IF;


		COMMIT;

	END;
