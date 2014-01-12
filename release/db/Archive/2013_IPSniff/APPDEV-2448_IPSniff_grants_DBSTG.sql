GRANT delete, insert, select, update ON "MIS"."IPLOCATOR_EVENT_LOG" TO fdstore_ststg01;
GRANT select ON "MIS"."IPLOCATOR_EVENT_LOG" TO appdev;
GRANT select ON MIS.IPLOCATOR_EVENT_LOG_SEQ TO fdstore_ststg01, appdev;

