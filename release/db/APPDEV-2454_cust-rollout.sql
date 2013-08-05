--
-- DB Rollout Script of
--   Site Features for Side Cart Redesign Project
--
-- Ticket: APPDEV-2454
--

-- #1 Featured Items Clone
insert into cust.ss_site_feature(ID, title, smart_saving)
values('SCR_FEAT_ITEMS', 'SideCart Featured Items', 0);

--   assign an aliased variant
insert into cust.ss_variants(id,feature,type,alias_id,archived)
values('scr-feat_pop4', 'SCR_FEAT_ITEMS', 'alias', 'prod-grp-pop-1', 'N');

--   Set presentation config
insert into cust.ss_variant_params(id,key,value)
values('scr-feat_pop4', 'prez_title', 'Customer Favorites');

--   Distribute
insert into ss_variant_assignment("DATE",cohort_id,variant_id) values(sysdate, 'C1', 'scr-feat_pop4');
insert into ss_variant_assignment("DATE",cohort_id,variant_id) values(sysdate, 'C2', 'scr-feat_pop4');
insert into ss_variant_assignment("DATE",cohort_id,variant_id) values(sysdate, 'C3', 'scr-feat_pop4');
insert into ss_variant_assignment("DATE",cohort_id,variant_id) values(sysdate, 'C4', 'scr-feat_pop4');
insert into ss_variant_assignment("DATE",cohort_id,variant_id) values(sysdate, 'C5', 'scr-feat_pop4');
insert into ss_variant_assignment("DATE",cohort_id,variant_id) values(sysdate, 'C6', 'scr-feat_pop4');
insert into ss_variant_assignment("DATE",cohort_id,variant_id) values(sysdate, 'C7', 'scr-feat_pop4');
insert into ss_variant_assignment("DATE",cohort_id,variant_id) values(sysdate, 'C8', 'scr-feat_pop4');
insert into ss_variant_assignment("DATE",cohort_id,variant_id) values(sysdate, 'C9', 'scr-feat_pop4');
insert into ss_variant_assignment("DATE",cohort_id,variant_id) values(sysdate, 'C10', 'scr-feat_pop4');

insert into ss_variant_assignment("DATE",cohort_id,variant_id) values(sysdate, 'C11', 'scr-feat_pop4');
insert into ss_variant_assignment("DATE",cohort_id,variant_id) values(sysdate, 'C12', 'scr-feat_pop4');
insert into ss_variant_assignment("DATE",cohort_id,variant_id) values(sysdate, 'C13', 'scr-feat_pop4');
insert into ss_variant_assignment("DATE",cohort_id,variant_id) values(sysdate, 'C14', 'scr-feat_pop4');
insert into ss_variant_assignment("DATE",cohort_id,variant_id) values(sysdate, 'C15', 'scr-feat_pop4');
insert into ss_variant_assignment("DATE",cohort_id,variant_id) values(sysdate, 'C16', 'scr-feat_pop4');
insert into ss_variant_assignment("DATE",cohort_id,variant_id) values(sysdate, 'C17', 'scr-feat_pop4');
insert into ss_variant_assignment("DATE",cohort_id,variant_id) values(sysdate, 'C18', 'scr-feat_pop4');
insert into ss_variant_assignment("DATE",cohort_id,variant_id) values(sysdate, 'C19', 'scr-feat_pop4');
insert into ss_variant_assignment("DATE",cohort_id,variant_id) values(sysdate, 'C20', 'scr-feat_pop4');


-- #2 Personal Recommendations
insert into cust.ss_site_feature(ID, title, smart_saving)
values('SCR_PERSONAL', 'SideCart Personal Recommendations', 0);

--   assign an aliased variant
insert into cust.ss_variants(id,feature,type,alias_id,archived)
values('scr-pers1','SCR_PERSONAL', 'alias', 'sc_personal', 'N');

--   Set presentation config
insert into cust.ss_variant_params(id,key,value)
values('scr-pers1','prez_title', 'You May Also Like');

--   Distribute
insert into ss_variant_assignment("DATE",cohort_id,variant_id) values(sysdate, 'C1', 'scr-pers1');
insert into ss_variant_assignment("DATE",cohort_id,variant_id) values(sysdate, 'C2', 'scr-pers1');
insert into ss_variant_assignment("DATE",cohort_id,variant_id) values(sysdate, 'C3', 'scr-pers1');
insert into ss_variant_assignment("DATE",cohort_id,variant_id) values(sysdate, 'C4', 'scr-pers1');
insert into ss_variant_assignment("DATE",cohort_id,variant_id) values(sysdate, 'C5', 'scr-pers1');
insert into ss_variant_assignment("DATE",cohort_id,variant_id) values(sysdate, 'C6', 'scr-pers1');
insert into ss_variant_assignment("DATE",cohort_id,variant_id) values(sysdate, 'C7', 'scr-pers1');
insert into ss_variant_assignment("DATE",cohort_id,variant_id) values(sysdate, 'C8', 'scr-pers1');
insert into ss_variant_assignment("DATE",cohort_id,variant_id) values(sysdate, 'C9', 'scr-pers1');
insert into ss_variant_assignment("DATE",cohort_id,variant_id) values(sysdate, 'C10', 'scr-pers1');

insert into ss_variant_assignment("DATE",cohort_id,variant_id) values(sysdate, 'C11', 'scr-pers1');
insert into ss_variant_assignment("DATE",cohort_id,variant_id) values(sysdate, 'C12', 'scr-pers1');
insert into ss_variant_assignment("DATE",cohort_id,variant_id) values(sysdate, 'C13', 'scr-pers1');
insert into ss_variant_assignment("DATE",cohort_id,variant_id) values(sysdate, 'C14', 'scr-pers1');
insert into ss_variant_assignment("DATE",cohort_id,variant_id) values(sysdate, 'C15', 'scr-pers1');
insert into ss_variant_assignment("DATE",cohort_id,variant_id) values(sysdate, 'C16', 'scr-pers1');
insert into ss_variant_assignment("DATE",cohort_id,variant_id) values(sysdate, 'C17', 'scr-pers1');
insert into ss_variant_assignment("DATE",cohort_id,variant_id) values(sysdate, 'C18', 'scr-pers1');
insert into ss_variant_assignment("DATE",cohort_id,variant_id) values(sysdate, 'C19', 'scr-pers1');
insert into ss_variant_assignment("DATE",cohort_id,variant_id) values(sysdate, 'C20', 'scr-pers1');

-- #3 Fix Product Detail Box Header
insert into cust.ss_variant_params(id,key,value)
values('ymal_pdtl', 'prez_title', 'You Might Also Like');

