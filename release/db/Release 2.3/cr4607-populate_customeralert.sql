INSERT INTO cust.CUSTOMERALERT
(id, customer_id, alert_type, note, create_date, create_user_id)
SELECT cust.system_seq.NEXTVAL, c.ID, 'ECHECK', al.NOTE, al.TIMESTAMP, al.INITIATOR FROM cust.ACTIVITY_LOG al,cust.CUSTOMER c  
WHERE 
c.id=al.customer_id
AND c.ON_ALERT = '1'
AND al.ACTIVITY_ID='Place Alert'
AND al.TIMESTAMP = 
	(SELECT MAX(al2.TIMESTAMP) FROM cust.ACTIVITY_LOG al2 WHERE al2.ACTIVITY_ID='Place Alert' AND al2.customer_id=al.customer_id); 

COMMIT;