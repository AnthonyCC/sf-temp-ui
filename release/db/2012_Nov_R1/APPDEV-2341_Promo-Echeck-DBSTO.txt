--Please execute the below script in DBSTO
alter table cust.promo_cust_strategy add(echeck_match_type varchar2(3 byte));

--set 'GEQ' as the default value for the existing records.
update cust.promo_cust_strategy pcs set PCS.ECHECK_MATCH_TYPE='GEQ' where PCS.payment_type like '%ECP%' and PCS.ECHECK_MATCH_TYPE is null;
