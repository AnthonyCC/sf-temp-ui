ALTER TABLE  cust.payment
 ADD (
 TRANS_REF_INDEX    VARCHAR2(4 BYTE),
PROFILE_ID    VARCHAR2(22 BYTE),
GATEWAY_ID    VARCHAR2(1 BYTE),
GATEWAY_ORDER  VARCHAR2(30 BYTE)
)

