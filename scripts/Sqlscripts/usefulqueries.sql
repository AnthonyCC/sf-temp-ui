--original queries

--insert profile name/val
insert into cust.profile (customer_id, profile_type, profile_name, profile_value, create_date) values ('11965729','S','Campaign','dpcontrol',sysdate);
--update profile name/val
update cust.profile set profile_value='dpcontrol' where profile_name='Campaign' and customer_id='11965729';

--gets info on OCF jobs
select * from cust.ocf_campaign where enddate > to_date('23-JAN-2008') order by enddate desc;

--end originals

--new stuff

--show all profile info on an account by an email 
select * from cust.profile where customer_id = (select id from cust.fdcustomer where erp_customer_id = (select customer_id from cust.customerinfo where email = 'mcfly00@freshdirect.com'))

--show all profile infos on accounts by an email like match 
select * from cust.profile where customer_id in 
	(select id from cust.fdcustomer where erp_customer_id in 
		(select customer_id from cust.customerinfo where email like 'testact%@freshdirect.com')
	)

--insert a profile name and value into an account by email
--change email to user's email
--RetentionProgram to the Profile Name to add
--2to5G3v2 to the Profile Value to add
--2008.03.31 changed to use IN instead
insert into cust.profile 
	(customer_id, profile_type, profile_name, profile_value, create_date) 
	values 
	(
		(select id from cust.fdcustomer where erp_customer_id in 
			(select customer_id from cust.customerinfo where email in 'mcfly00@freshdirect.com')
		),'S','RetentionProgram','2to5G3v2',sysdate);


--update user to be eligible for promotions by an email 
update cust.profile set profile_value='allow' where (profile_name='signup_promo_eligible') and customer_id in (select id from cust.fdcustomer where erp_customer_id in (select customer_id from cust.customerinfo where email like 'testact00@freshdirect.com'));


select id from cust.fdcustomer where erp_customer_id = (select customer_id from cust.customerinfo where email = 'mcfly00@freshdirect.com');

select customer_id from cust.customerinfo where email like '_oc_rown%' or email like '_cfly%' order by email

--find types of delivery addresses on account by email like 
select last_name, service_type from cust.address where customer_id in (select customer_id from cust.customerinfo where email like '%_oc_rown%' or email like '%_cfly%') order by id

--find product restrictions
select * from erps.attributes where atr_name='restrictions'

--find customer's resevation info by customer_id (id from crm prof)
select * from dlv.reservation where customer_id = (select customer_id from cust.customerinfo where customer_id = '1233592070')


--find all sales by customer email like 
select * from cust.sale where customer_id in 
	(select customer_id from cust.customerinfo where email like 'mcfly%@freshdirect.com')


-------------------2/7/2008 QS Testing
select * from cust.customer where id in (select customer_id from cust.customerinfo where email like 'testactLIST%@freshdirect.com');

--find user's lists
select * from cust.customerlist
	   where customer_id = (select customer_id from cust.customerinfo where email = 'testactLIST01@freshdirect.com');

--count items on specific list 
select * from cust.customerlist_details
	   where list_id ='2148585277';
	   
--delete qs items
delete from cust.customerlist_details where list_id ='2148585277'

--test 
update cust.customerlist 
set type = 'CCL', 
	name = 'QuickShop Old' 
where 
	 id = (select id from cust.customerlist 
	 	where 
	 	customer_id = (select customer_id from cust.customerinfo 
					where email = 'testactLIST00@freshdirect.com') 
	 AND name = 'QuickShop');


--get count
--count items on specific list 
select count(*) from cust.customerlist_details
	   where 
	 list_id = (select id from cust.customerlist 
	 		   		   where 
	 	customer_id = (select customer_id from cust.customerinfo 
					where email = 'bowen001@earthlink.net') 
	 AND name = 'QuickShop');

--------------------------------------
2008.02.07 : KOSHER restrictions
--------------------------------------

--get all kosher restricts 
select * from dlv.restricted_days where reason = 'KHR';

--updates 
update dlv.restricted_days
	   set
	   	  (
		   start_time = '',
		   end_time = '',
		   message = ''
		  )
	where id = ''

-----------------------------------------------


-----------------------------------------------
--2008.02.14_12.57.24.PM : Product Recalls
-----------------------------------------------

--This is easiest if you have a spreadsheet with the order#'s in a column.
--	1. Sort col by order#
--	2. paste into notepad, refotmat to 'ID1','ID2', etc
--	3. paste list inside (), run query to get ids
--	4. paste that info next to order# cols
--	5. compare cols, there may be less if same cust ordered multiple times
--   now with with cust ids:
--	6. repeat at step 2 

--get cust ids
select id,customer_id from cust.sale
where id in () order by id

--get cust info
select customer_id,first_name,Last_name,email,home_phone,home_ext,business_phone,business_ext,cell_phone,cell_ext,other_phone,other_ext,fax,fax_ext,alt_email from cust.customerinfo 
	   where customer_id in () 
	   order by customer_id

-----------------------------------------------

-----------------------------------------------
--2008.02.21_03.38.34.PM : From Robert
-----------------------------------------------

 

/*  get the campaign entry  */

select * from cust.ocf_campaign where name like '%%';

/*  get the list of flights for the campaign  */

select * from cust.ocf_flight where campaign_id ='5332';

 

/* list Flight Name, run id, timestamp and number of customers selected in that run for specified campaign */

select ocf_flight.name, run_id,timestamp,count(1) as num_custs 
from cust.ocf_run_custs, cust.ocf_run_log,cust.ocf_flight where ocf_flight.id 
	in 
	  ( 
	     select id from cust.ocf_flight where campaign_id 
	     in (select id from cust.ocf_campaign where name like 'campaign name goes here')
	  ) 
		and ocf_run_log.flightid=ocf_flight.id
			and ocf_run_log.id=run_id
	group by ocf_flight.name,run_id,timestamp
order by ocf_flight.name,timestamp;

 
----------------------------------2008.03.04_10.40.25.AM

--get customer info from the id from cust.profile
select * from cust.customerinfo where customer_id = (select erp_customer_id from cust.fdcustomer where id = '1668747053')

--in version get customer info from the id from cust.profile
select * from cust.customerinfo 
		where customer_id in (select erp_customer_id from cust.fdcustomer 
			where id in ('')
		)


----------------------------------2008.03.10_11.53.04.AM

--get sku codes from material ids, sap_id is 18 digits, use leading zeros
select distinct sku_code from erps.product 
	where id in 
		(select product_id from erps.materialproxy 
			where mat_id in 
				(select id from erps.material 
					where sap_id in 
						( [MATERIAL IDS HERE] )
				)
		)

-----------------------------------2008.07.09_03.53.04.PM

--get material ids from skus 

select unique(sap_id) from erps.material where id in 
	(select mat_id from erps.materialproxy where product_id in 
		(select id from erps.product where sku_code in		
			( [PASTE IDS] )
		)
	)


----------------------------------2008.03.27_12.09.09.PM

--validate for zip code recalculation (step 3)
select a.zipcode, a.home_coverage hdev, b.home_coverage hspa,a.cos_coverage cdev , b.cos_coverage cspa 
         from dlv.zipcode a, dlv.zipcode@dbstospa.nyc.freshdirect.com b 
                  where a.zipcode = b.zipcode and (a.home_coverage > b.home_coverage or a.cos_coverage > b.cos_coverage)


----------------------------------2008.03.31_10.43.49.AM

URL rededirect setup, dev, cust.url_rewrite_rules

request:
	from
http://www.freshdirect.com/baby
	to
http://www.freshdirect.com/category.jsp?catId=gro_baby&trk=purl

using toad, the trick here is to execute this as a script. this will allow set define off, and the & in the url will update fine

SET DEFINE OFF

--1 row inserted	
insert into cust.url_rewrite_rules
	(id, name, from_url, redirect, comments, options)
values
	(cust.system_seq.nextval, 'Baby Section Re-vamp', '/baby', '/category.jsp?catId=gro_baby&trk=purl', 'Baby Section Re-vamp APPDEV-51 20080331', 'N')


----------------------------------2008.04.03_11.48.58.AM

checking to make sure vip/metal wasn't reflagged by ocf:

--returns anyone who's flag doesn't match compared to dm_profile flag

select * from cust.profile where customer_id 
	   not in (select customer_id from cust.dm_profile natural join cust.profile)
	   and customer_id in (select customer_id from cust.dm_profile)
	   and profile_name = 'VIPCustomer'
	   order by customer_id

---------------------------------2008.04.16_02.14.27.PM

--get customerinfo from fd_id (like in CT flagging spreadsheet)

select first_name, last_name, email from cust.customerinfo where customer_id in 
	(select erp_customer_id from cust.fdcustomer where id in 
		(
			PASTE IDS HERE
		)
	)

---------------------------------2008.04.24_10.21.01.AM

--DP Testing

Alternatively you could use the below query and tweak data to set up an active BSGS pass.

SELECT CI.EMAIL,CI.CUSTOMER_ID FROM CUST.CUSTOMERINFO CI, CUST.DELIVERY_PASS DP WHERE DP.CUSTOMER_ID=CI.CUSTOMER_ID AND TYPE IN ('MKT0070536','MKT0070537','MKT0070538');

Some queries: 

--TO VIEW ALL DP PURCHASES-- 

select * from cust.delivery_pass where customer_id in 
(select id from cust.customer where user_id = 'xxx@xxx.com')

-- TO DELETE RECORDS FROM DELIVERY_PASS TABLE—(For e.g. you want to clear the customer account of all DP history, instead of setting up a new account)

--first step - update sale table—(this query is for deleting all passes in account…if only a specific pass needs to be deleted, then modify the query to look at Deliverypass id instead of email id)
update cust.sale set dlv_pass_id=null where dlv_pass_id in  
(select id from cust.delivery_pass where customer_id IN 
(select customer_id from cust.customerinfo where email='xxx@xxx.com'))


--second step-delete the pass from delivery_pass table--(this query is for deleting all passes in account…if only a specific pass needs to be deleted, then modify the query to look at Deliverypass id instead of email id)
delete cust.delivery_pass where customer_id in 
(select id from cust.customer where user_id = 'xxx@xxx.com')


NOTE: 

- To expire a pass, changing the status to ‘EXP’ doesn’t work. You have to change the expiry date!
- After purchase, pass will be in ‘PEN’ status. To make it RTU or Active, you can directly change the status to ‘ACT’ or ‘RTU’

-----------

-------------------------------------2008.04.28_04.03.38.PM

update customer's delivery pass expire date:

You need to modify the ORG_EXP_DATE too as this field to calculate the refund amount..

UPDATE CUST.DELIVERY_PASS SET EXP_DATE=EXP_DATE-30, ORG_EXP_DATE=ORG_EXP_DATE-30  WHERE ID= [ID OF DP ROW]


------------------------------------2008.04.29_03.26.14.PM

--OCF stuff

--get flight info that a specified user fit into 
select * from cust.ocf_flight where id in
	   (select flightid from cust.ocf_run_log where id in 
	   		   (select run_id from cust.ocf_run_custs where email = 'elena.co12@gmail.com')
		)

--use the above to get the id and replace it in here to show all custs in that send
select * from cust.ocf_run_custs where run_id in
	   (select id from cust.ocf_run_log where flightid in 
	   		  (select id from cust.ocf_flight where id = '32613')
		)
		
--get current stats of flights where a specified user was emailed 
select * from cust.ocf_run_log where id in 
	   		   (select run_id from cust.ocf_run_custs where email = 'elena.co12@gmail.com')

--get all customers emailed in a run where specified user was emailed  
select * from cust.ocf_run_custs where run_id in 
	   	(select run_id from cust.ocf_run_custs where email = 'elena.co12@gmail.com')

		
--show all profile infos on accounts that are in the same flight(s) as specified user 
select * from cust.profile where customer_id in 
	(select id from cust.fdcustomer where erp_customer_id in 
		(select customer_id from cust.customerinfo where email in 
				(
				 	select email from cust.ocf_run_custs where run_id in 
	   					   (select run_id from cust.ocf_run_custs where email = 'elena.co12@gmail.com')
				)
		)
	) and profile_name = 'RetentionProgram' order by create_date

	
--get surveys customer filled out by email
select * from cust.survey where customer_id in 
	   (select id from cust.fdcustomer where erp_customer_id = 
	   		   (select customer_id from cust.customerinfo where email = 'elena.co12@gmail.com')
		)

--get surveys' results by an email
select * from cust.surveydata where survey_id in  
	   (select id from cust.survey where customer_id in 
	   		   (select id from cust.fdcustomer where erp_customer_id = 
	   		   		   (select customer_id from cust.customerinfo where email = 'elena.co12@gmail.com')
					   )
		)
------------

--copy current restrictions from stage
insert into dlv.geo_restriction select cust.system_seq.nextval, name,geoloc, null, null from dlv.GEO_RESTRICTION_WORKTAB@DBSTG.NYC.FRESHDIRECT.COM

--show all restrictions setup
select * from dlv.geo_restriction order by id

--show all restrictions settings
select * from dlv.geo_restriction_days order by restriction_id

--set specific restriction to ON 
update dlv.geo_restriction set inactive = ' ' where id = '2148890051'

--insert new restriction setting 
insert into dlv.geo_restriction_days 
	   (restriction_id, day_of_week, condition, start_time, end_time)
	   					values
							  ('2148890051', '1', '>', sysdate, sysdate)

--update restriction start_date setting 			  
update dlv.geo_restriction_days set start_time = to_date('1/1/1970 1:00:00 AM', 'MM/DD/YYYY HH:MI:SS PM') where restriction_id = '2148890051'				  

--update restriction end_date setting 
update dlv.geo_restriction_days set end_time = to_date('1/1/1970 1:00:00 PM', 'MM/DD/YYYY HH:MI:SS PM') where restriction_id = '2148890051'

--update restriction condition setting 
update dlv.geo_restriction_days set condition = '<>' where restriction_id = '2148890051'

--update day_of_week setting 1=sunday  
update dlv.geo_restriction_days set day_of_week = '4' where restriction_id = '2148867917'

--make all restrictions active
update dlv.geo_restriction set inactive = ' '

delete from dlv.geo_restriction where id in ('2148887117', '2148887118')	
-----------------------------

------------------------------------2008.05.09_12.48.12.PM

/* 
select the email addresses of people who: 
	   have a settled order 
	   paid for that order with a credit card (that was NOT a make-good) 
	   and have an autorenew delivery pass 
	   
	   you can modify : 
	   change 'CC' to 'EC' for echeck 
	   change != 'M' to = 'M' for make good 

this does not check to make sure the dp is still active, so it might be expired.
*/
select email from cust.customerinfo where customer_id in 
	   (select customer_id from cust.salesaction where sale_id in 
	   		   (select id from cust.sale where status = 'STL' and customer_id in
	   	   	   		   (select customer_id from cust.paymentinfo_new where payment_method_type = 'CC' and on_fd_account != 'M')
	   			) and action_type = 'STL'
		) and has_autorenew_dp = 'Y'

-----------------------------

--Find credits out of sync, id is what you would send to pythian (id, customer_id, ' : ' is just to make it easier to find id in results)
select * from
	   (select 
	   id, customer_id, ' : ', 
	   customer_id a, complaint_id b, create_date c, 
	   customer_id x, complaint_id y, create_date z 
	   from cust.customercredit)
	   		where a = x and
	   		b = y and
	   		c != z order by c desc

--fix out of sync credits
update cust.customercredit set create_date = to_date('06/02/2008 04:09:07 PM', 'MM/DD/YYYY HH:MI:SS PM') where id = '3275973274';

---------------------------2008.06.04_03.20.06.PM

--deleting a future timeslot and related items

delete from dlv.timeslot t where base_date = '15-JUN-2008' and t.ZONE_ID = '65776298' 

delete from dlv.truck_resource tr where tr.RESOURCE_ID in (select id from dlv.planning_resource p where day = '15-JUN-2008' and p.ZONE_CODE = '001')

delete from dlv.planning_resource p where day = '15-JUN-2008' and p.ZONE_CODE = '001'

--------------------------2008.06.05_06.11.51.PM

--get people with a specific timeslot time reservation 

select customer_id, rsv_day_of_week, rsv_start_time, rsv_end_time from cust.customerinfo ts where 
	   ts.rsv_day_of_week = '1' and 
	   to_date(to_char(ts.rsv_start_time, 'HH:MI AM'),'HH:MI AM') >= to_date('9:00 PM','HH:MI AM') and
	   to_date(to_char(ts.rsv_end_time, 'HH:MI AM'),'HH:MI AM') <= to_date('10:30 PM','HH:MI AM');
	  

--update reservation timeslot times
update cust.customerinfo set 
	   rsv_start_time = to_date(to_char(rsv_start_time, 'MM/DD/YYYY') || ' 6:30 AM', 'MM/DD/YYYY HH:MI AM'),
	   rsv_end_time = to_date(to_char(rsv_end_time, 'MM/DD/YYYY') || ' 7:30 AM', 'MM/DD/YYYY HH:MI AM')
	   where customer_id in
		(
			select customer_id from cust.customerinfo ts where 
				ts.rsv_day_of_week = '1' and 
					to_date(to_char(ts.rsv_start_time, 'HH:MI AM'),'HH:MI AM') >= to_date('9:00 PM','HH:MI AM') and
					to_date(to_char(ts.rsv_end_time, 'HH:MI AM'),'HH:MI AM') <= to_date('10:30 PM','HH:MI AM')
		)

--2008.06.11_04.01.48.PM
--find zip code coverage, last line is narrowing it down to <0.1 & >0.0001

SELECT * FROM (SELECT zipcode, sum(sdo_geom.sdo_area(sdo_geom.sdo_intersection(zones.geoloc, zip.geoloc, .5), .5)/sdo_geom.sdo_area(zip.geoloc, .5)) as coverage 
	FROM dlv.zipcode zip, (SELECT zn.zone_code, zn.geoloc, rd.start_date  
		 FROM dlv.region r, dlv.region_data rd, dlv.zone zn
		 WHERE r.id = rd.region_id and zn.region_data_id = rd.id and r.service_type = 'HOME'
		 AND rd.start_date = (SELECT max(start_date) 
			 FROM dlv.region_data rd1 
			 WHERE rd1.region_id = r.id and rd1.start_date <= sysdate + 8
	  )
	) zones GROUP BY zipcode ORDER BY zipcode) WHERE coverage < 0.1 and coverage > .0001 order by coverage desc
							

--2008.07.10_11.30.54.AM
--find orders for wine

select unique(sale_id) from cust.salesaction where id in 
	(select unique(salesaction_id) from cust.orderline where salesaction_id in 
		(select id from cust.salesaction where action_date > sysdate - 1 and action_type in 
			('CRO', 'MOD') and action_type <> 'CAN') and affiliate = 'USQ' ) 

--2008.07.17_01.42.48.PM
--adding crm case subjects
--1st insert : insert new case subject
--2nd insert : perms for case subjects, changing the case subject code for each category

--1 row inserted
insert into cust.case_subject (CODE, NAME, DESCRIPTION, CASE_QUEUE, CASE_PRIORITY, OBSOLETE) values ('TCQ-006', 'Missing Box Follow-Up', NULL, 'TCQ', 'LO', NULL);

--insert row count varies
INSERT INTO CUST.CASE_OPERATION SELECT 'CAQ-014',START_CASE_STATE,END_CASE_STATE,ROLE,CASEACTION_TYPE FROM CUST.CASE_OPERATION WHERE CASE_SUBJECT='CAQ-001';

--regexp (note tab in find between groups 2 & 3
	find: ([A-Z]*)-([0-9]*)	([^\t]*)
	replace with: 
		--1 row inserted
		insert into cust.case_subject (CODE, NAME, DESCRIPTION, CASE_QUEUE, CASE_PRIORITY, OBSOLETE) values ('\1-\2', '\3', NULL, '\1', 'LO', NULL);


--2008.08.25_05.21.53.PM
--show timeslots for zone by code (no rows returned means bad zone id or capacity is all zeroes)
select * from dlv.timeslot where 
	   capacity > '0' and zone_id in 
	   			(select id from dlv.zone where zone_code = '181')
	 order by base_date desc

--2008.09.02_12.49.13.PM
--lookup customer info by what profiles they have 
select * from cust.customerinfo where customer_id in 
	   ( select erp_customer_id from cust.fdcustomer where id in 
	   	 ( select customer_id from cust.profile where profile_name = 'siteFeature.RATING' and profile_value = 'true')
		)


--get info
select p.sku_code,p.base_price,p.default_Price, p.version from erps.product p where sku_code='HMR0067028' and version=(select max(version) from erps.product where sku_code=p.sku_code)

--update base price
update erps.product set base_price=8.00 where version=(select max(version) from erps.product where sku_code='HMR0067028')
 and sku_code='HMR0067028'

 --find an order for a customer who doesn't come up in the normal CRM search
select id, customer_id from cust.sale where customer_id in (
	(select customer_id from cust.customerinfo where email like 'genaugust%') union
	(select id from cust.customer where user_id = 'genaugust%')
)

--credits

select * from cust.complaint where status = 'PEN' and amount <= 20;

select count(*) from cust.sale where id in (select sale_id from cust.complaint where status = 'PEN' and amount <= 20) and status in ('STL','PPG');

select c.id as complaint_id from cust.sale s, cust.complaint c 
            where s.status = 'PPG' and c.sale_id=s.id and c.amount <= 14 and c.status = 'PEN'
            and c.id not in (select complaint_id from cust.complaintline where c.id = complaint_id and method = 'CSH')
            union all 
            select c.id as complaint_id 
            from cust.sale s, cust.complaint c 
            where s.status = 'STL' and c.sale_id=s.id and c.amount <= 14 and c.status = 'PEN';

--find customer info for other customers at same addresss
select FIRST_NAME,LAST_NAME,EMAIL from cust.customerinfo where customer_id in 
(select customer_id from cust.sale where id in 
	(select sale_id from cust.salesaction where id in 
		   (select salesaction_id from cust.deliveryinfo where ADDRESS1 = '345 W 145TH ST')
		   ))

--find all cutoff times
select distinct cutoff_time from dlv.timeslot l where l.BASE_DATE > sysdate -100

--find zones with a specific cutoff where 6:00 is the cutoff time
select zone_code from dlv.zone where id in 
	   (select zone_id from dlv.timeslot where cutoff_time = to_date('1970/1/1 6:00:00 PM', 'YYYY/MM/DD HH:MI:SS PM') and base_date >= sysdate-8)

--find addresses in zones with a specific cutoff
select distinct address1, apartment, city, state, zip, zone from cust.deliveryinfo where zone in
	   (select zone_code from dlv.zone where id in 
	   		   (select zone_id from dlv.timeslot where cutoff_time = to_date('1970/1/1 6:00:00 PM', 'YYYY/MM/DD HH:MI:SS PM'))
			   ) order by zone

--find customerinfo for accounts in zones with a specific cutoff
select * from cust.customerinfo where customer_id in 
	(select customer_id from cust.salesaction where id in
		(select salesaction_id from cust.deliveryinfo where zone in
			   (select zone_code from dlv.zone where id in 
			   		   (select zone_id from dlv.timeslot where cutoff_time = to_date('1970/1/1 6:00:00 PM', 'YYYY/MM/DD HH:MI:SS PM'))
				)
			)
	)
    
-- Pretty URL 
insert into cust.url_rewrite_rules (
Id, Name, Disabled, from_url, redirect, comments, options, priority)
values (cust.system_seq.nextval, 'Feedback Redirect', ' ', '/feedback', '/help/contact_fd.jsp',
'Feedback', 'N', '');