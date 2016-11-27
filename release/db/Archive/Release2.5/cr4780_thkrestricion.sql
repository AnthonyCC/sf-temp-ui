
--- update existing thanksgiving entry . Expected result: 1 row updated  ---
update dlv.restricted_days 
  set 
      start_time = '23-nov-2005',
      end_time=to_date('11/24/2005 11:59:59 pm','MM/DD/YYYY HH:MI:SS pm'),
      message='Orders containing our chef''s Thanksgiving Dinner may only be placed for Tues, 11/23 and Wed, 11/24.'
where reason ='TKG';
