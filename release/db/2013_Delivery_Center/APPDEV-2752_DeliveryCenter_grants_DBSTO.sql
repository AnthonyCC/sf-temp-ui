/*Grants*/
GRANT delete, insert, select, update ON MIS.ORDER_DELIVERY_METRIC TO fdtrn_stprd01;
GRANT delete, insert, select, update ON MIS.ORDER_DELIVERY_METRIC TO fdstore_stprd01;
GRANT select ON MIS.ORDER_DELIVERY_METRIC TO appdev;

GRANT DELETE, INSERT, SELECT, UPDATE ON TRANSP.EVENTLOGBOOK TO fdstore_stprd01;
GRANT DELETE, INSERT, SELECT, UPDATE ON TRANSP.EVENTLOGBOOK TO fdtrn_stprd01;
GRANT SELECT ON TRANSP.EVENTLOGBOOK TO appdev;
GRANT SELECT ON TRANSP.EVENTLOGBOOK TO mis;
GRANT DELETE, INSERT, SELECT, UPDATE ON TRANSP.EVENTLOG_MESSAGEGROUP TO fdstore_stprd01;
GRANT DELETE, INSERT, SELECT, UPDATE ON TRANSP.EVENTLOG_MESSAGEGROUP TO fdtrn_stprd01;
GRANT SELECT ON TRANSP.EVENTLOG_MESSAGEGROUP TO appdev;
GRANT DELETE, INSERT, SELECT, UPDATE ON TRANSP.EVENTSTOPNUMBERBREAKDOWN TO fdstore_stprd01;
GRANT DELETE, INSERT, SELECT, UPDATE ON TRANSP.EVENTSTOPNUMBERBREAKDOWN TO fdtrn_stprd01;
GRANT SELECT ON TRANSP.EVENTSTOPNUMBERBREAKDOWN TO appdev;
GRANT SELECT ON TRANSP.EVENTSTOPNUMBERBREAKDOWN TO mis;
GRANT DELETE, INSERT, SELECT, UPDATE ON TRANSP.EVENTTYPE TO fdstore_stprd01;
GRANT DELETE, INSERT, SELECT, UPDATE ON TRANSP.EVENTTYPE TO fdtrn_stprd01;
GRANT SELECT ON TRANSP.EVENTTYPE TO appdev;
GRANT SELECT ON TRANSP.EVENTTYPE TO mis;
GRANT DELETE, INSERT, SELECT, UPDATE ON TRANSP.EVENTSUBTYPE TO fdstore_stprd01;
GRANT DELETE, INSERT, SELECT, UPDATE ON TRANSP.EVENTSUBTYPE TO fdtrn_stprd01;
GRANT SELECT ON TRANSP.EVENTSUBTYPE TO appdev;

GRANT DELETE, INSERT, SELECT, UPDATE ON TRANSP.MOTEVENTLOGBOOK TO fdstore_stprd01;
GRANT DELETE, INSERT, SELECT, UPDATE ON TRANSP.MOTEVENTLOGBOOK TO fdtrn_stprd01;
GRANT SELECT ON TRANSP.MOTEVENTLOGBOOK TO mis;
GRANT SELECT ON TRANSP.MOTEVENTLOGBOOK TO appdev;
GRANT DELETE, INSERT, SELECT, UPDATE ON TRANSP.MOTSTOPNUMBERBREAKDOWN TO fdstore_stprd01;
GRANT DELETE, INSERT, SELECT, UPDATE ON TRANSP.MOTEVENTTYPE TO fdstore_stprd01;
GRANT DELETE, INSERT, SELECT, UPDATE ON TRANSP.MOTEVENTTYPE TO fdtrn_stprd01;
GRANT SELECT ON TRANSP.MOTEVENTTYPE TO appdev;
GRANT DELETE, INSERT, SELECT, UPDATE ON TRANSP.MOTEVENTLOG_MESSAGEGROUP TO fdstore_stprd01;
GRANT DELETE, INSERT, SELECT, UPDATE ON TRANSP.MOTEVENTLOG_MESSAGEGROUP TO fdtrn_stprd01;
GRANT SELECT ON TRANSP.MOTEVENTLOG_MESSAGEGROUP TO appdev;
GRANT DELETE, INSERT, SELECT, UPDATE ON TRANSP.AUTHUSERS TO fdstore_stprd01;
GRANT DELETE, INSERT, SELECT, UPDATE ON TRANSP.AUTHUSERS TO fdtrn_stprd01;
GRANT SELECT ON TRANSP.AUTHUSERS TO appdev;
GRANT DELETE, INSERT, SELECT, UPDATE ON TRANSP.AUTHUSER_PRIVILEGE TO fdstore_stprd01;
GRANT DELETE, INSERT, SELECT, UPDATE ON TRANSP.AUTHUSER_PRIVILEGE TO fdtrn_stprd01;
GRANT SELECT ON TRANSP.AUTHUSER_PRIVILEGE TO appdev;

GRANT SELECT ON TRANSP.EVENTLOGSEQ TO fdstore_stprd01;
GRANT SELECT ON TRANSP.EVENTLOGSEQ TO fdtrn_stprd01;
GRANT SELECT ON TRANSP.EVENTLOGSEQ TO appdev;
GRANT SELECT ON TRANSP.MOTEVENTLOGSEQ TO fdstore_stprd01;
GRANT SELECT ON TRANSP.MOTEVENTLOGSEQ TO fdtrn_stprd01;
GRANT SELECT ON TRANSP.MOTEVENTLOGSEQ TO appdev;

GRANT SELECT ON TRANSP.HANDOFF_BATCH TO mis;
GRANT SELECT ON TRANSP.HANDOFF_BATCHSTOP TO mis;
GRANT SELECT ON cust.lateissue_orders TO mis;
GRANT SELECT ON dlv.cartontracking TO mis;