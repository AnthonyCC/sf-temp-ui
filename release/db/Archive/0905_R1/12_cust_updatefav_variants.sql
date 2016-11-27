
 -- 1 row updated

update cust.ss_variants set type='scripted' where id='favorites-1';

-- 1 row inserted

insert into CUST.SS_VARIANT_PARAMS(id,key,value) values('favorites-1','cat_aggr','FALSE');

-- 1 row inserted

insert into CUST.SS_VARIANT_PARAMS(id,key,value) values('favorites-1','cos_filter',null);

-- 1 row inserted

insert into CUST.SS_VARIANT_PARAMS(id,key,value) values('favorites-1','include_cart_items','FALSE');

-- 1 row inserted

insert into CUST.SS_VARIANT_PARAMS(id,key,value) values('favorites-1','scoring','Popularity_Discretized;QualityRating_Discretized2');

-- 1 row inserted

insert into CUST.SS_VARIANT_PARAMS(id,key,value) values('favorites-1','smart_saving','FALSE');

-- 1 row updated
update CUST.SS_VARIANT_PARAMS set  value='0.7' where id='favorites-1' and key='exponent';

-- 1 row updated
update CUST.SS_VARIANT_PARAMS set  value='RecursiveNodes("fd_favs_lm")' where id='favorites-1' and key='favorite_list_id';

-- 1 row updated
update CUST.SS_VARIANT_PARAMS set  value='These are just a few of our most-loved products.' where id='favorites-1' and key='prez_desc';

-- 1 row updated
update CUST.SS_VARIANT_PARAMS set  value='FRESHDIRECT FAVORITES' where id='favorites-1' and key='prez_title';

-- 1 row updated
update CUST.SS_VARIANT_PARAMS set  value='power' where id='favorites-1' and key='sampling_strat';

-- 1 row updated
update CUST.SS_VARIANT_PARAMS set  value='1' where id='favorites-1' and key='top_n';

-- 1 row updated
update CUST.SS_VARIANT_PARAMS set  value='100' where id='favorites-1' and key='top_perc';

-- 1 row updated

update cust.ss_variants set type='scripted' where id='favorites-2';
 
-- 1 row insreed

insert into CUST.SS_VARIANT_PARAMS(id,key,value) values('favorites-2','cat_aggr','FALSE');

-- 1 row insreed
insert into CUST.SS_VARIANT_PARAMS(id,key,value) values('favorites-2','cos_filter',null);

-- 1 row insreed
insert into CUST.SS_VARIANT_PARAMS(id,key,value) values('favorites-2','include_cart_items','FALSE');

-- 1 row insreed
insert into CUST.SS_VARIANT_PARAMS(id,key,value) 
values('favorites-2','scoring','Popularity_Discretized;QualityRating_Discretized2');

-- 1 row insreed
insert into CUST.SS_VARIANT_PARAMS(id,key,value) values('favorites-2','smart_saving','FALSE');

-- 1 row insreed
update CUST.SS_VARIANT_PARAMS set  value='0.7' where id='favorites-2' and key='exponent';

-- 1 row insreed
update CUST.SS_VARIANT_PARAMS set  value='RecursiveNodes("fd_favs_ak")' where id='favorites-2' and key='favorite_list_id'; 

-- 1 row insreed
update CUST.SS_VARIANT_PARAMS set  value='These are just a few of our most-loved products.' where id='favorites-2' and key='prez_desc'; 

-- 1 row insreed
update CUST.SS_VARIANT_PARAMS set value='FRESHDIRECT FAVORITES' where id='favorites-2' and  key='prez_title';

-- 1 row insreed
update CUST.SS_VARIANT_PARAMS set  value='power' where id='favorites-2' and key='sampling_strat';

-- 1 row insreed
update CUST.SS_VARIANT_PARAMS set  value='1' where id='favorites-2' and key='top_n';

-- 1 row insreed
update CUST.SS_VARIANT_PARAMS set  value='100' where id='favorites-2' and key='top_perc';


-- 1 row updated

update cust.ss_variants set type='scripted' where id='favorites-3';

-- 1 row inserted
insert into CUST.SS_VARIANT_PARAMS(id,key,value) values('favorites-3','cat_aggr','FALSE');

-- 1 row inserted
insert into CUST.SS_VARIANT_PARAMS(id,key,value) values('favorites-3','cos_filter',null);

-- 1 row inserted
insert into CUST.SS_VARIANT_PARAMS(id,key,value) values('favorites-3','include_cart_items','FALSE');

-- 1 row inserted
insert into CUST.SS_VARIANT_PARAMS(id,key,value) values('favorites-3','scoring','Popularity_Discretized;QualityRating_Discretized2');

-- 1 row inserted
insert into CUST.SS_VARIANT_PARAMS(id,key,value) values('favorites-3','smart_saving','FALSE');

-- 1 row updated
update CUST.SS_VARIANT_PARAMS set  value='0.5' where id='favorites-3' and key='exponent';

-- 1 row updated
update CUST.SS_VARIANT_PARAMS set  value='RecursiveNodes("fd_favs_nb")' where id='favorites-3' and key='favorite_list_id';

-- 1 row updated
update CUST.SS_VARIANT_PARAMS set  value='These are just a few of our most-loved products.' where id='favorites-3' and key='prez_desc'; 

-- 1 row updated
update CUST.SS_VARIANT_PARAMS set  value='FRESHDIRECT FAVORITES' where id='favorites-3' and key='prez_title';

-- 1 row updated
update CUST.SS_VARIANT_PARAMS set  value='power' where id='favorites-3' and key='sampling_strat';

-- 1 row updated
update CUST.SS_VARIANT_PARAMS set  value='1' where id='favorites-3' and key='top_n';

-- 1 row updated
update CUST.SS_VARIANT_PARAMS set  value='100' where id='favorites-3' and key='top_perc';





