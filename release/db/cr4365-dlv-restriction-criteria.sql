alter table dlv.restricted_days add (CRITERION VARCHAR2(16));

update dlv.restricted_days set criterion='DELIVERY' where criterion is null;

update dlv.restricted_days set criterion='PURCHASE' where type='PTR';

alter table dlv.restricted_days modify ( CRITERION NOT NULL);

