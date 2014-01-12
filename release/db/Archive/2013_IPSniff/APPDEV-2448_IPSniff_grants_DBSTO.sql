GRANT delete, insert, select, update ON "MIS"."IPLOCATOR_EVENT_LOG" TO fdstore_stprd01;
GRANT select ON "MIS"."IPLOCATOR_EVENT_LOG" TO appdev;
GRANT select ON MIS.IPLOCATOR_EVENT_LOG_SEQ TO fdstore_stprd01, appdev;


