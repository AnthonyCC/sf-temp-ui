ALTER TABLE TRANSP.TRN_AREA ADD BALANCE_BY  VARCHAR2(16 BYTE);
ALTER TABLE TRANSP.TRN_AREA ADD LOADBALANCE_FACTOR  NUMBER(10,9);
ALTER TABLE TRANSP.TRN_AREA ADD NEEDS_LOADBALANCE     VARCHAR2(1 BYTE);

ALTER TABLE DLV.SERVICETIME_SCENARIO ADD BALANCE_BY  VARCHAR2(16 BYTE);
ALTER TABLE DLV.SERVICETIME_SCENARIO ADD LOADBALANCE_FACTOR  NUMBER(10,9);
ALTER TABLE DLV.SERVICETIME_SCENARIO ADD NEEDS_LOADBALANCE     VARCHAR2(1 BYTE);
