INSERT INTO cust.case_subject
(code, name, case_queue, case_priority)
VALUES
('ASQ-008', 'Bad Account Review', 'ASQ', 'MD');

INSERT INTO cust.case_operation
(case_subject, start_case_state, end_case_state, ROLE, caseaction_type)
(
SELECT 'ASQ-008', start_case_state, end_case_state, ROLE, caseaction_type
FROM cust.case_operation WHERE case_subject = 'ASQ-003'  -- same as authorization failure 
);

COMMIT;
