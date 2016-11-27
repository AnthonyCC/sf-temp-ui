Insert into CUST.CASE_SUBJECT  (CODE, NAME, CASE_QUEUE, CASE_PRIORITY) Values   ('DSQ-035', 'Late Box - Reuinted w/ Truck', 'DSQ', 'MD');
Insert into CUST.CASE_SUBJECT  (CODE, NAME, CASE_QUEUE, CASE_PRIORITY) Values   ('DSQ-036', 'Late Box - Handoff to MOT', 'DSQ', 'MD');
Insert into CUST.CASE_SUBJECT  (CODE, NAME, CASE_QUEUE, CASE_PRIORITY) Values   ('DSQ-037', 'Shorted Box', 'DSQ', 'MD');
Insert into CUST.CASE_SUBJECT  (CODE, NAME, CASE_QUEUE, CASE_PRIORITY) Values   ('DSQ-038', 'Duplicate Box', 'DSQ', 'MD');
Insert into CUST.CASE_SUBJECT  (CODE, NAME, CASE_QUEUE, CASE_PRIORITY) Values   ('DSQ-039', 'Call in Response to a Not Home', 'DSQ', 'MD');

Insert into CUST.CASE_OPERATION   (CASE_SUBJECT, START_CASE_STATE, END_CASE_STATE, ROLE, CASEACTION_TYPE) select 'DSQ-035',START_CASE_STATE, END_CASE_STATE, ROLE, CASEACTION_TYPE from CUST.CASE_OPERATION where CASE_SUBJECT='OAQ-001' and ROLE in('ADM','SUP','OPS','SOP');
Insert into CUST.CASE_OPERATION   (CASE_SUBJECT, START_CASE_STATE, END_CASE_STATE, ROLE, CASEACTION_TYPE) select 'DSQ-036',START_CASE_STATE, END_CASE_STATE, ROLE, CASEACTION_TYPE from CUST.CASE_OPERATION where CASE_SUBJECT='OAQ-001' and ROLE in('ADM','SUP','OPS','SOP');
Insert into CUST.CASE_OPERATION   (CASE_SUBJECT, START_CASE_STATE, END_CASE_STATE, ROLE, CASEACTION_TYPE) select 'DSQ-037',START_CASE_STATE, END_CASE_STATE, ROLE, CASEACTION_TYPE from CUST.CASE_OPERATION where CASE_SUBJECT='OAQ-001' and ROLE in('ADM','SUP','OPS','SOP');
Insert into CUST.CASE_OPERATION   (CASE_SUBJECT, START_CASE_STATE, END_CASE_STATE, ROLE, CASEACTION_TYPE) select 'DSQ-038',START_CASE_STATE, END_CASE_STATE, ROLE, CASEACTION_TYPE from CUST.CASE_OPERATION where CASE_SUBJECT='OAQ-001' and ROLE in('ADM','SUP','OPS','SOP');
Insert into CUST.CASE_OPERATION   (CASE_SUBJECT, START_CASE_STATE, END_CASE_STATE, ROLE, CASEACTION_TYPE) select 'DSQ-039',START_CASE_STATE, END_CASE_STATE, ROLE, CASEACTION_TYPE from CUST.CASE_OPERATION where CASE_SUBJECT='OAQ-001' and ROLE in('ADM','SUP','OPS','SOP');


Insert into CUST.CASE_SUBJECT  (CODE, NAME, CASE_QUEUE, CASE_PRIORITY) Values   ('ORQ-017', 'Same Day Pull Box Request', 'ORQ', 'MD');

Insert into CUST.CASE_OPERATION   (CASE_SUBJECT, START_CASE_STATE, END_CASE_STATE, ROLE, CASEACTION_TYPE) select 'ORQ-017',START_CASE_STATE, END_CASE_STATE, ROLE, CASEACTION_TYPE from CUST.CASE_OPERATION where CASE_SUBJECT='OAQ-001';