-- [APPREQ-482]
alter table cust.case_subject add "CARTONS_REQ" CHAR(1 BYTE);
-- Mark case subjects that require cartons
update cust.case_subject set cartons_req='X' where code in ('ORQ-001', 'ORQ-002', 'ORQ-004', 'TCQ-006', 'DSQ-003', 'DSQ-007', 'DSQ-004', 'DSQ-005', 'DSQ-006');

