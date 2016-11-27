delete from activity_type;
insert into activity_type (code, name, description) values ('ACC_CR',    'Create Account', 'Create Account');
insert into activity_type (code, name, description) values ('ACC_UID',   'Update UserId', 'Update UserId');
insert into activity_type (code, name, description) values ('SNDFPEM',   'Send ''Forgot Password'' Email', 'Send ''Forgot Password'' Email');
insert into activity_type (code, name, description) values ('ACC_PSWD',  'Change Password', 'Change Password');
insert into activity_type (code, name, description) values ('ACC_ACT',   'Activate Account', 'Activate Account');
insert into activity_type (code, name, description) values ('ACC_DCT',   'Deactivate Account', 'Deactivate Account');
insert into activity_type (code, name, description) values ('SGNUP_EN',  'Enable Signup Promo', 'Enable Signup Promo');
insert into activity_type (code, name, description) values ('SGNUP_DS',  'Disable Signup Promo', 'Disable Signup Promo');
insert into activity_type (code, name, description) values ('ACC_CON',   'Change Contact Information', 'Change Contact Information');
insert into activity_type (code, name, description) values ('ACC_EML',   'Change Email Preferences', 'Change Email Preferences');
insert into activity_type (code, name, description) values ('DELV_A',    'Add Delivery Address', 'Add Delivery Address');
insert into activity_type (code, name, description) values ('DELV_U',    'Update Delivery Address', 'Update Delivery Address');
insert into activity_type (code, name, description) values ('DELV_D',    'Delete Delivery Address', 'Delete Delivery Address');
insert into activity_type (code, name, description) values ('PAYM_A',    'Add Payment Method', 'Add Payment Method');
insert into activity_type (code, name, description) values ('PAYM_U',    'Update Payment Method', 'Update Payment Method');
insert into activity_type (code, name, description) values ('PAYM_D',    'Delete Payment Method', 'Delete Payment Method');
insert into activity_type (code, name, description) values ('CSR_IN',    'Agent Login', 'Agent Login');
insert into activity_type (code, name, description) values ('CSR_OUT',   'Agent Logout', 'Agent Logout');

delete from case_origin;
insert into case_origin (code, name, description) values ('PHONE', 'Phone', 'Case created manually by agent');
insert into case_origin (code, name, description) values ('SYS', 'System', 'Case created automatically by system on behalf of customer');
insert into case_origin (code, name, description) values ('EMAIL', 'Email', 'Case created based on email received from customer');
insert into case_origin (code, name, description) values ('WEB_FORM', 'Web Form', 'Case create based on feedback/contact form on the website');

delete from case_state;
insert into case_state (code, name, description) values ('NEW', 'New', 'New case being created');
insert into case_state (code, name, description) values ('OPEN', 'Open', 'Starting state for all cases');
insert into case_state (code, name, description) values ('REVW', 'Review', 'Review is required before proceeding');
insert into case_state (code, name, description) values ('APVD', 'Approved', 'Approval granted for action');
insert into case_state (code, name, description) values ('CLSD', 'Closed', 'Terminal state for all cases');


delete from role;
insert into role (code, name, description) values ('GUE', 'Guest', 'Read-only Guest');
insert into role (code, name, description) values ('PRD', 'Production', 'Read-only Production Staff');
insert into role (code, name, description) values ('EXC', 'Executive', 'Read-only Executive');
insert into role (code, name, description) values ('CSR', 'Customer Service Agent', 'Customer Service Agent');
insert into role (code, name, description) values ('TRN', 'Transportation Dispatch', 'Transportation Dispatch Agent');
insert into role (code, name, description) values ('ASV', 'Account Services Agent', 'Account Services Agent');
insert into role (code, name, description) values ('SUP', 'Supervisor', 'Customer Service Supervisor');
insert into role (code, name, description) values ('ADM', 'Administrator', 'Customer Service System Administrator');
insert into role (code, name, description) values ('SYS', 'System', 'Automated System Functions');

delete from agent;
insert into agent (id, user_id, password, first_name, last_name, active, role, created) values (system_seq.nextval, 'admin', 'admin', 'Admin', 'User', 'X', 'ADM', sysdate);
insert into agent (id, user_id, password, first_name, last_name, active, role, created) values (system_seq.nextval, 'system', 'system', 'System', 'User', 'X', 'SYS', sysdate);
insert into agent (id, user_id, password, first_name, last_name, active, role, created) values (system_seq.nextval, 'guest', 'guest', 'Guest', 'User', 'X', 'GUE', sysdate);

delete from department;
insert into department (code, name, description) values ('FRU', 'Fruit', 'Fruit Department');
insert into department (code, name, description) values ('VEG', 'Vegetables', 'Vegetable Department');
insert into department (code, name, description) values ('MEA', 'Meat', 'Meat Department');
insert into department (code, name, description) values ('SEA', 'Seafood', 'Seafood Department');
insert into department (code, name, description) values ('DEL', 'Deli', 'Deli Department');
insert into department (code, name, description) values ('CHE', 'Cheese', 'Cheese Department');
insert into department (code, name, description) values ('DAI', 'Dairy', 'Dairy Department');
insert into department (code, name, description) values ('COF', 'Coffee', 'Coffee Department');
insert into department (code, name, description) values ('TEA', 'Tea', 'Tea Department');
insert into department (code, name, description) values ('PST', 'Pasta', 'Pasta Department');
insert into department (code, name, description) values ('BAK', 'Bakery', 'Bakery Department');
insert into department (code, name, description) values ('KIT', 'Meals', 'Meals Department');
insert into department (code, name, description) values ('GRO', 'Grocery', 'Grocery Department');
insert into department (code, name, description) values ('FRO', 'Frozen', 'Frozen Department');
insert into department (code, name, description) values ('SPE', 'Specialty', 'Specialty Department');
insert into department (code, name, description) values ('WEB', 'Website', 'Web Development');
insert into department (code, name, description) values ('TRN', 'Transportation', 'Transportation Department');
insert into department (code, name, description) values ('MKT', 'Marketing', 'Marketing Department');


delete from caseaction_type;
insert into caseaction_type (code, name, description) values ('NOTE',    'Add Case Note', 'Case work performed by agent');
insert into caseaction_type (code, name, description) values ('EML_IN',  'Email In', 'Email received from customer');
insert into caseaction_type (code, name, description) values ('EML_OUT', 'Email Out', 'Email sent to customer');
insert into caseaction_type (code, name, description) values ('CAS_EDT', 'Edit Case', 'Properties of the case were changed');
insert into caseaction_type (code, name, description) values ('ACT_EDT', 'Edit Account', 'Customer''s account was edited');
insert into caseaction_type (code, name, description) values ('ORD_EDT', 'Edit Order', 'Customer''s order was modified');
insert into caseaction_type (code, name, description) values ('ESCRVW',  'Escalate for Review', 'Case escalated for review');
insert into caseaction_type (code, name, description) values ('RETURN',  'Return Case to Agent', 'Case returned to agent based on review');
insert into caseaction_type (code, name, description) values ('APPROV',  'Approve for Closure', 'Approval granted for action proposed by agent');
insert into caseaction_type (code, name, description) values ('CLOSE',   'Close Case', 'No further work need be done on this case');


delete from case_subject;
delete from case_queue;
delete from case_priority;

insert into case_priority (code, name, description, priority) values ('HI', 'High', 'High Priority', 2);
insert into case_priority (code, name, description, priority) values ('MD', 'Medium', 'Medium Priority', 1);
insert into case_priority (code, name, description, priority) values ('LO', 'Low', 'Low Priority', 0);

insert into case_queue (code, name, description) values ('ASQ', 'Account Services', 'Account Services Queue');
insert into case_queue (code, name, description) values ('EAQ', 'Edit Account', 'Edit Account Queue');
insert into case_queue (code, name, description) values ('GIQ', 'General Inquiry', 'General Inquiry Queue');
insert into case_queue (code, name, description) values ('OIQ', 'Order', 'Order Issues Queue');
insert into case_queue (code, name, description) values ('PRQ', 'Promotion', 'Promotion Issues Queue');
insert into case_queue (code, name, description) values ('TRQ', 'Transportation', 'Transportation Issues Queue');
insert into case_queue (code, name, description) values ('WWQ', 'Website', 'Website Issues Queue');

insert into case_subject (case_queue, case_priority, code, name, description) values ('ASQ', 'MD', 'ASQ-001', 'Authorizations', ' ');
insert into case_subject (case_queue, case_priority, code, name, description) values ('ASQ', 'MD', 'ASQ-002', 'Chargebacks', ' ');
insert into case_subject (case_queue, case_priority, code, name, description) values ('ASQ', 'MD', 'ASQ-003', 'Authorization failures', ' ');
insert into case_subject (case_queue, case_priority, code, name, description) values ('ASQ', 'MD', 'ASQ-004', 'AVS failed', ' ');
insert into case_subject (case_queue, case_priority, code, name, description) values ('ASQ', 'MD', 'ASQ-005', 'Duplicate order', ' ');
insert into case_subject (case_queue, case_priority, code, name, description) values ('ASQ', 'MD', 'ASQ-006', 'Orders over $450', ' ');
insert into case_subject (case_queue, case_priority, code, name, description) values ('ASQ', 'MD', 'ASQ-007', 'Returns', ' ');
insert into case_subject (case_queue, case_priority, code, name, description) values ('ASQ', 'MD', 'ASQ-100', 'Other', ' ');

insert into case_subject (case_queue, case_priority, code, name, description) values ('EAQ', 'MD', 'EAQ-001', 'Account information', ' ');
insert into case_subject (case_queue, case_priority, code, name, description) values ('EAQ', 'MD', 'EAQ-002', 'Password', ' ');

insert into case_subject (case_queue, case_priority, code, name, description) values ('GIQ', 'MD', 'GIQ-001', 'Corporate inquiry', ' ');
insert into case_subject (case_queue, case_priority, code, name, description) values ('GIQ', 'MD', 'GIQ-002', 'Service availability', ' ');
insert into case_subject (case_queue, case_priority, code, name, description) values ('GIQ', 'MD', 'GIQ-003', 'CSR compliment', ' ');
insert into case_subject (case_queue, case_priority, code, name, description) values ('GIQ', 'MD', 'GIQ-004', 'CSR complaint', ' ');
insert into case_subject (case_queue, case_priority, code, name, description) values ('GIQ', 'MD', 'GIQ-100', 'Other', ' ');

insert into case_subject (case_queue, case_priority, code, name, description) values ('OIQ', 'MD', 'OIQ-001', 'New', ' ');
insert into case_subject (case_queue, case_priority, code, name, description) values ('OIQ', 'MD', 'OIQ-002', 'Status', ' ');
insert into case_subject (case_queue, case_priority, code, name, description) values ('OIQ', 'MD', 'OIQ-003', 'Cancel', ' ');
insert into case_subject (case_queue, case_priority, code, name, description) values ('OIQ', 'MD', 'OIQ-004', 'Modify', ' ');
insert into case_subject (case_queue, case_priority, code, name, description) values ('OIQ', 'MD', 'OIQ-005', 'Product complaint', ' ');
insert into case_subject (case_queue, case_priority, code, name, description) values ('OIQ', 'MD', 'OIQ-006', 'Product inquiry', ' ');
insert into case_subject (case_queue, case_priority, code, name, description) values ('OIQ', 'MD', 'OIQ-008', 'Missing items', ' ');

insert into case_subject (case_queue, case_priority, code, name, description) values ('PRQ', 'MD', 'PRQ-001', 'Gift certificates', ' ');
insert into case_subject (case_queue, case_priority, code, name, description) values ('PRQ', 'MD', 'PRQ-002', 'Promotion eligibility', ' ');
insert into case_subject (case_queue, case_priority, code, name, description) values ('PRQ', 'MD', 'PRQ-003', 'Rollback of promotion', ' ');
insert into case_subject (case_queue, case_priority, code, name, description) values ('PRQ', 'MD', 'PRQ-004', 'General inquiry', ' ');

insert into case_subject (case_queue, case_priority, code, name, description) values ('TRQ', 'MD', 'TRQ-001', 'Late delivery', ' ');
insert into case_subject (case_queue, case_priority, code, name, description) values ('TRQ', 'MD', 'TRQ-002', 'Early delivery', ' ');
insert into case_subject (case_queue, case_priority, code, name, description) values ('TRQ', 'MD', 'TRQ-003', 'Missing boxes/bags', ' ');
insert into case_subject (case_queue, case_priority, code, name, description) values ('TRQ', 'MD', 'TRQ-004', 'Unauthorized doorman drop', ' ');
insert into case_subject (case_queue, case_priority, code, name, description) values ('TRQ', 'MD', 'TRQ-005', 'Driver complaint', ' ');
insert into case_subject (case_queue, case_priority, code, name, description) values ('TRQ', 'MD', 'TRQ-006', 'Driver compliment', ' ');
insert into case_subject (case_queue, case_priority, code, name, description) values ('TRQ', 'MD', 'TRQ-007', 'Damaged product', ' ');
insert into case_subject (case_queue, case_priority, code, name, description) values ('TRQ', 'MD', 'TRQ-008', 'Not home', ' ');
insert into case_subject (case_queue, case_priority, code, name, description) values ('TRQ', 'MD', 'TRQ-009', 'Voice Shot/Outbound Late', ' ');
insert into case_subject (case_queue, case_priority, code, name, description) values ('TRQ', 'MD', 'TRQ-100', 'Other', ' ');

insert into case_subject (case_queue, case_priority, code, name, description) values ('WWQ', 'MD', 'WWQ-001', 'Website difficulties', ' ');
insert into case_subject (case_queue, case_priority, code, name, description) values ('WWQ', 'MD', 'WWQ-002', 'Password issues', ' ');
insert into case_subject (case_queue, case_priority, code, name, description) values ('WWQ', 'MD', 'WWQ-003', 'Timeslot availability', ' ');
insert into case_subject (case_queue, case_priority, code, name, description) values ('WWQ', 'MD', 'WWQ-100', 'Other', ' ');
