  ALTER TABLE DLV.RESERVATION ADD (BUILDINGID             VARCHAR2(16 BYTE),
  LOCATIONID             VARCHAR2(16 BYTE),
  PREV_BLDG_RSV_CNT      NUMBER(5),
  INSERT_TIMESTAMP       DATE);
  
  ALTER TABLE TRANSP.ZONE ADD   SVC_ADJ_REDUCTION_FACTOR  NUMBER;