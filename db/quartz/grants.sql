GRANT select, insert, update, delete on QUARTZ.qrtz_calendars to fdstore_prda;
GRANT select, insert, update, delete on QUARTZ.qrtz_calendars to fdstore_prdb;
GRANT SELECT on QUARTZ.qrtz_calendars to appdev;

GRANT select, insert, update, delete on QUARTZ.qrtz_fired_triggers to fdstore_prda;
GRANT select, insert, update, delete on QUARTZ.qrtz_fired_triggers to fdstore_prdb;
GRANT SELECT on QUARTZ.qrtz_fired_triggers to appdev;

GRANT select, insert, update, delete on QUARTZ.qrtz_trigger_listeners to fdstore_prda;
GRANT select, insert, update, delete on QUARTZ.qrtz_trigger_listeners to fdstore_prdb;
GRANT SELECT on QUARTZ.qrtz_trigger_listeners to appdev;

GRANT select, insert, update, delete on QUARTZ.qrtz_blob_triggers to fdstore_prda;
GRANT select, insert, update, delete on QUARTZ.qrtz_blob_triggers to fdstore_prdb;
GRANT SELECT on QUARTZ.qrtz_blob_triggers to appdev;

GRANT select, insert, update, delete on QUARTZ.qrtz_cron_triggers to fdstore_prda;
GRANT select, insert, update, delete on QUARTZ.qrtz_cron_triggers to fdstore_prdb;
GRANT SELECT on QUARTZ.qrtz_cron_triggers to appdev;

GRANT select, insert, update, delete on QUARTZ.qrtz_simple_triggers to fdstore_prda;
GRANT select, insert, update, delete on QUARTZ.qrtz_simple_triggers to fdstore_prdb;
GRANT SELECT on QUARTZ.qrtz_simple_triggers to appdev;

GRANT select, insert, update, delete on QUARTZ.qrtz_triggers to fdstore_prda;
GRANT select, insert, update, delete on QUARTZ.qrtz_triggers to fdstore_prdb;
GRANT SELECT on QUARTZ.qrtz_triggers to appdev;


GRANT select, insert, update, delete on QUARTZ.qrtz_job_listeners to fdstore_prda;
GRANT select, insert, update, delete on QUARTZ.qrtz_job_listeners to fdstore_prdb;
GRANT SELECT on QUARTZ.qrtz_job_listeners to appdev;

GRANT select, insert, update, delete on QUARTZ.qrtz_job_details to fdstore_prda;
GRANT select, insert, update, delete on QUARTZ.qrtz_job_details to fdstore_prdb;
GRANT SELECT on QUARTZ.qrtz_job_details to appdev;

GRANT select, insert, update, delete on QUARTZ.qrtz_paused_trigger_grps to fdstore_prda;
GRANT select, insert, update, delete on QUARTZ.qrtz_paused_trigger_grps to fdstore_prdb;
GRANT SELECT on QUARTZ.qrtz_paused_trigger_grps to appdev;

GRANT select, insert, update, delete on QUARTZ.qrtz_locks to fdstore_prda;
GRANT select, insert, update, delete on QUARTZ.qrtz_locks to fdstore_prdb;
GRANT SELECT on QUARTZ.qrtz_locks to appdev;

GRANT select, insert, update, delete on QUARTZ.qrtz_scheduler_state to fdstore_prda;
GRANT select, insert, update, delete on QUARTZ.qrtz_scheduler_state to fdstore_prdb;
GRANT SELECT on QUARTZ.qrtz_scheduler_state to appdev;
