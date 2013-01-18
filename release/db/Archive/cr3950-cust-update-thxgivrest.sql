update dlv.restricted_days set 
	start_time ='23-NOV-2004', 
	end_time = to_date('24-NOV-2004 11:59:59 pm','DD-MON-YYYY HH:MI:SS PM'),
	message = 'Thanksgiving - Order containing our chef''s Thanksgiving Dinner, may only be placed for Tues, 11/23 and Wed, 11/24.'
where name = 'Thanksgiving';

