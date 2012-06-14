alter table dlv.shift_timeslot add   PREMIUM_CUTOFF_TIME  DATE;

alter table dlv.timeslot add ( PREMIUM_CUTOFF_TIME  DATE, PREMIUM_CAPACITY NUMBER(10) DEFAULT 0 NOT NULL, PREMIUM_CT_CAPACITY NUMBER(10) DEFAULT 0 NOT NULL);

alter table dlv.reservation add (CLASS CHAR(2 BYTE));

alter table dlv.zone add (PREMIUM_CT_RELEASE_TIME NUMBER(4) DEFAULT 0 NOT NULL, PREMIUM_CT_ACTIVE VARCHAR2(1 BYTE));

alter table mis.timeslot_event_hdr add sameday char(1 BYTE);

alter table mis.session_event add sameday char(1 BYTE);