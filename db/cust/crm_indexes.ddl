CREATE INDEX CUST.CASE_SUBJ_PRIORITY_IDX
     ON CUST.CASE_SUBJECT
        (CASE_PRIORITY);

CREATE INDEX CUST.CASE_SUBJ_QUEUE_SUBJ_IDX
     ON CUST.CASE_SUBJECT
        (CASE_QUEUE,
         CODE);

CREATE INDEX  CUST.CASE_ORIGIN_IDX 
    ON CUST.CASE(CASE_ORIGIN);

CREATE INDEX  CUST.CASE_SALE_IDX
    ON CUST.CASE(SALE_ID);

CREATE INDEX  CUST.CASE_LOCKAGENT_IDX
    ON CUST.CASE(LOCKED_AGENT_ID);

CREATE INDEX  CUST.CASE_PRIORITY_IDX
    ON CUST.CASE(CASE_PRIORITY);

CREATE INDEX CUST.CASE_SUBJ_STATE_ID_CUST_IDX
     ON CUST."CASE"
        (CASE_SUBJECT,
         CASE_STATE,
         "ID",
         CUSTOMER_ID);

CREATE INDEX  CUST.CASE_ASSIGNED_AGENT_IDX
    ON CUST.CASE(ASSIGNED_AGENT_ID);

CREATE INDEX CUST.CASEACTION_ID_TIME_IDX
     ON CUST.CASEACTION
        (CASE_ID,
		TIMESTAMP);

CREATE INDEX CUST.CASEACTION_AGENT_IDX
     ON CUST.CASEACTION
        (AGENT_ID);

CREATE INDEX CUST.CASEACTION_TYPE_IDX
     ON CUST.CASEACTION
        (CASEACTION_TYPE);

CREATE INDEX CUST.CASE_OPERATION_SUBJ_IDX
     ON CUST.CASE_OPERATION
        (CASE_SUBJECT);

CREATE INDEX CUST.CASE_OPERATION_ROLE_IDX
     ON CUST.CASE_OPERATION
        ("ROLE");

CREATE INDEX CUST.CASE_OPERATION_END_STATE_IDX
     ON CUST.CASE_OPERATION
        (END_CASE_STATE);

CREATE INDEX CUST.CASE_OPERATION_START_STATE_IDX
     ON CUST.CASE_OPERATION
    (START_CASE_STATE);
