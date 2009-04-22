
INSERT INTO SS_VARIANTS (id, feature, type) VALUES ('tabs_var1fave', 'CART_N_TABS', 'tab-strategy');
INSERT INTO SS_VARIANTS (id, feature, type) VALUES ('tabs_var1peak', 'CART_N_TABS', 'tab-strategy');
INSERT INTO SS_VARIANTS (id, feature, type) VALUES ('tabs_var1deal', 'CART_N_TABS', 'tab-strategy');
INSERT INTO SS_VARIANTS (id, feature, type) VALUES ('tabs_var2fave', 'CART_N_TABS', 'tab-strategy');

INSERT INTO SS_VARIANT_ASSIGNMENT (cohort_id, "DATE", variant_id) VALUES ('C1', sysdate, 'tabs_var1fave');
INSERT INTO SS_VARIANT_ASSIGNMENT (cohort_id, "DATE", variant_id) VALUES ('C2', sysdate, 'tabs_var1fave');
INSERT INTO SS_VARIANT_ASSIGNMENT (cohort_id, "DATE", variant_id) VALUES ('C3', sysdate, 'tabs_var1fave');
INSERT INTO SS_VARIANT_ASSIGNMENT (cohort_id, "DATE", variant_id) VALUES ('C4', sysdate, 'tabs_var1fave');
INSERT INTO SS_VARIANT_ASSIGNMENT (cohort_id, "DATE", variant_id) VALUES ('C5', sysdate, 'tabs_var1fave');
INSERT INTO SS_VARIANT_ASSIGNMENT (cohort_id, "DATE", variant_id) VALUES ('C6', sysdate, 'tabs_var1peak');
INSERT INTO SS_VARIANT_ASSIGNMENT (cohort_id, "DATE", variant_id) VALUES ('C7', sysdate, 'tabs_var1peak');
INSERT INTO SS_VARIANT_ASSIGNMENT (cohort_id, "DATE", variant_id) VALUES ('C8', sysdate, 'tabs_var1peak');
INSERT INTO SS_VARIANT_ASSIGNMENT (cohort_id, "DATE", variant_id) VALUES ('C9', sysdate, 'tabs_var1peak');
INSERT INTO SS_VARIANT_ASSIGNMENT (cohort_id, "DATE", variant_id) VALUES ('C10', sysdate, 'tabs_var1peak');
INSERT INTO SS_VARIANT_ASSIGNMENT (cohort_id, "DATE", variant_id) VALUES ('C11', sysdate, 'tabs_var1deal');
INSERT INTO SS_VARIANT_ASSIGNMENT (cohort_id, "DATE", variant_id) VALUES ('C12', sysdate, 'tabs_var1deal');
INSERT INTO SS_VARIANT_ASSIGNMENT (cohort_id, "DATE", variant_id) VALUES ('C13', sysdate, 'tabs_var1deal');
INSERT INTO SS_VARIANT_ASSIGNMENT (cohort_id, "DATE", variant_id) VALUES ('C14', sysdate, 'tabs_var1deal');
INSERT INTO SS_VARIANT_ASSIGNMENT (cohort_id, "DATE", variant_id) VALUES ('C15', sysdate, 'tabs_var1deal');
INSERT INTO SS_VARIANT_ASSIGNMENT (cohort_id, "DATE", variant_id) VALUES ('C16', sysdate, 'tabs_var2fave');
INSERT INTO SS_VARIANT_ASSIGNMENT (cohort_id, "DATE", variant_id) VALUES ('C17', sysdate, 'tabs_var2fave');
INSERT INTO SS_VARIANT_ASSIGNMENT (cohort_id, "DATE", variant_id) VALUES ('C18', sysdate, 'tabs_var2fave');
INSERT INTO SS_VARIANT_ASSIGNMENT (cohort_id, "DATE", variant_id) VALUES ('C19', sysdate, 'tabs_var2fave');
INSERT INTO SS_VARIANT_ASSIGNMENT (cohort_id, "DATE", variant_id) VALUES ('C20', sysdate, 'tabs_var2fave');

-- DELETE FROM SS_VARIANT_ASSIGNMENT WHERE variant_id = 'tabs_var1fave';
-- DELETE FROM SS_VARIANT_ASSIGNMENT WHERE variant_id = 'tabs_var1peak';
-- DELETE FROM SS_VARIANT_ASSIGNMENT WHERE variant_id = 'tabs_var1deal';
-- DELETE FROM SS_VARIANT_ASSIGNMENT WHERE variant_id = 'tabs_var2fave';
-- DELETE FROM SS_TAB_STRATEGY WHERE id = 'tabs_var1fave';
-- DELETE FROM SS_TAB_STRATEGY WHERE id = 'tabs_var1peak';
-- DELETE FROM SS_TAB_STRATEGY WHERE id = 'tabs_var1deal';
-- DELETE FROM SS_TAB_STRATEGY WHERE id = 'tabs_var2fave';



