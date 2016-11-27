-------------------------------------------------------------
-- Statements for handling variants safely regarding archival
-- author: Tamas Gelesz
-------------------------------------------------------------


-- Statements for archiving variants safely. If the variant is used or there is 
-- an alias to this variant that is used, no record will be updated. All of the 
-- aliases to this variant will be archived as well using the 2nd statement. 
-- Fill in variant id in all places.

update CUST.ss_variants set archived='Y' where id ='[THIS IS WHERE YOU SHOULD WRITE THE VARIANT ID]' and id not in
(
  select v.id
  from CUST.ss_variant_assignment a join CUST.ss_variants v on a.variant_id=v.id
  join (
      select  a.cohort_id, max(a."DATE") "DATE", v.feature
      from CUST.ss_variant_assignment a join CUST.ss_variants v on a.variant_id=v.id 
      GROUP BY a.cohort_id, v.feature
      ) m
  using (cohort_id, "DATE", feature)
  
  union

  select v.alias_id
  from CUST.ss_variant_assignment a join CUST.ss_variants v on a.variant_id=v.id
  join (
      select  a.cohort_id, max(a."DATE") "DATE", v.feature
      from CUST.ss_variant_assignment a join CUST.ss_variants v on a.variant_id=v.id 
      GROUP BY a.cohort_id, v.feature
      ) m
  using (cohort_id, "DATE", feature)
  where v.alias_id is not null
);


update CUST.ss_variants set archived='Y' where alias_id ='[THIS IS WHERE YOU SHOULD WRITE THE VARIANT ID]' and alias_id not in
(
  select v.id
  from CUST.ss_variant_assignment a join CUST.ss_variants v on a.variant_id=v.id
  join (
      select  a.cohort_id, max(a."DATE") "DATE", v.feature
      from CUST.ss_variant_assignment a join CUST.ss_variants v on a.variant_id=v.id 
      GROUP BY a.cohort_id, v.feature
      ) m
  using (cohort_id, "DATE", feature)
  
  union

  select v.alias_id
  from CUST.ss_variant_assignment a join CUST.ss_variants v on a.variant_id=v.id
  join (
      select  a.cohort_id, max(a."DATE") "DATE", v.feature
      from CUST.ss_variant_assignment a join CUST.ss_variants v on a.variant_id=v.id 
      GROUP BY a.cohort_id, v.feature
      ) m
  using (cohort_id, "DATE", feature)
  where v.alias_id is not null
);



-- Query for checking whether a variant or its parent (if it's an alias) is archived. 
-- Use this before assigning variants using the ss_variant_assignment table.
-- Count should return 0 to proceed. Fill in variant id in all places.

select count(*) from CUST.ss_variants v 
left join CUST.ss_variants a on v.alias_id=a.id 
where (v.id = '[THIS IS WHERE YOU SHOULD WRITE THE VARIANT ID]' and v.archived='Y')
or (a.id = '[THIS IS WHERE YOU SHOULD WRITE THE VARIANT ID]' and a.archived='Y')



