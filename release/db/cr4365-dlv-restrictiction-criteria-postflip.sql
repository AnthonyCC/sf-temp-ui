-- post-flip:
update dlv.restricted_days set type='RRN' where type='PTR';

INSERT INTO DLV.RESTRICTED_DAYS (ID, TYPE, NAME, DAY_OF_WEEK, START_TIME, END_TIME, REASON, MESSAGE, CRITERION ) VALUES ( 
dlv.system_seq.nextval, 'RRN', 'Kosher Cutoff', 7,
TO_Date( '01/01/2004 12:00:00 AM', 'MM/DD/YYYY HH:MI:SS AM'),
TO_Date( '01/01/2004 09:30:00 PM', 'MM/DD/YYYY HH:MI:SS AM'),
'KHR', 'Unavailable for Sunday delivery (with early cutoff)', 'CUTOFF');

-- for rollback: 
-- delete from dlv.restricted_days where criterion='CUTOFF';
-- update dlv.restricted_days set type='PTR' where criterion='PURCHASE';
