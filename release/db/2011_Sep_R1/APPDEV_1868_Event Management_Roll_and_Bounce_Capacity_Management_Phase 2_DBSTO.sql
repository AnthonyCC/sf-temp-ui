 
GRANT SELECT ON MIS.TIMESLOT_EVENT_HDR TO APPDEV;
  
GRANT DELETE, INSERT, SELECT, UPDATE ON MIS.TIMESLOT_EVENT_HDR TO FDMKT_PRDA;

GRANT DELETE, INSERT, SELECT, UPDATE ON MIS.TIMESLOT_EVENT_HDR TO FDSTORE_PRDA;

GRANT DELETE, INSERT, SELECT, UPDATE ON MIS.TIMESLOT_EVENT_HDR TO FDSTORE_PRDB;

GRANT DELETE, INSERT, SELECT, UPDATE ON MIS.TIMESLOT_EVENT_HDR TO FDTRN_PRDA;
 
GRANT SELECT ON MIS.TIMESLOT_EVENT_DTL TO APPDEV;
 
GRANT DELETE, INSERT, SELECT, UPDATE ON MIS.TIMESLOT_EVENT_DTL TO FDMKT_PRDA;

GRANT DELETE, INSERT, SELECT, UPDATE ON MIS.TIMESLOT_EVENT_DTL TO FDSTORE_PRDA;

GRANT DELETE, INSERT, SELECT, UPDATE ON MIS.TIMESLOT_EVENT_DTL TO FDSTORE_PRDB;

GRANT DELETE, INSERT, SELECT, UPDATE ON MIS.TIMESLOT_EVENT_DTL TO FDTRN_PRDA;

 GRANT DELETE, INSERT, SELECT, UPDATE ON MIS.BOUNCE_EVENT TO FDSTORE_PRDA;

 GRANT DELETE, INSERT, SELECT, UPDATE ON MIS.BOUNCE_EVENT TO FDSTORE_PRDB;
 
 GRANT SELECT ON MIS.BOUNCE_EVENT TO FDTRN_PRDA;
 
 GRANT DELETE, INSERT, SELECT, UPDATE ON MIS.ROLL_EVENT TO FDSTORE_PRDA;

 GRANT DELETE, INSERT, SELECT, UPDATE ON MIS.ROLL_EVENT TO FDSTORE_PRDB;
 
 GRANT SELECT ON MIS.ROLL_EVENT TO FDTRN_PRDA;

 GRANT DELETE, INSERT, SELECT, UPDATE ON MIS.SESSION_EVENT TO FDSTORE_PRDA;

 GRANT DELETE, INSERT, SELECT, UPDATE ON MIS.SESSION_EVENT TO FDSTORE_PRDB;
 
 GRANT SELECT ON MIS.SESSION_EVENT TO FDTRN_PRDA;
  
 GRANT DELETE, INSERT, SELECT, UPDATE ON MIS.ORDER_RATE TO FDSTORE_PRDA;

 GRANT DELETE, INSERT, SELECT, UPDATE ON MIS.ORDER_RATE TO FDSTORE_PRDB;
 
 GRANT SELECT ON MIS.ORDER_RATE TO FDTRN_PRDA;
 
 