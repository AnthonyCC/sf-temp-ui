# APPREQ-753 - Config DYF variants to hide YourFave bursts
# 
insert into cust.ss_variant_params
select id, 'hide_bursts', 'YOUR_FAVE'
FROM cust.ss_variants
WHERE feature='DYF' and id<>'nil';

