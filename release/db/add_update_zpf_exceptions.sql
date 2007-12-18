ALTER TABLE DLV.ZIPPLUSFOUR_EXCEPTIONS
  ADD COUNTY VARCHAR2(28)
  ADD STATE VARCHAR2(2);

update dlv.zipplusfour_exceptions zpfe set county = 
 (select cs.county 
	from dlv.city_state cs, dlv.zipplusfour zpf 
	where cs.city_state_key = zpf.city_state_key 
	and zpf.zipcode = zpfe.ZIPCODE
	group by county);

update dlv.zipplusfour_exceptions zpfe set state = 
	(select cs.state 
	from dlv.city_state cs, dlv.zipplusfour zpf 
	where cs.city_state_key = zpf.city_state_key 
	and zpf.zipcode = zpfe.ZIPCODE
	group by state );
