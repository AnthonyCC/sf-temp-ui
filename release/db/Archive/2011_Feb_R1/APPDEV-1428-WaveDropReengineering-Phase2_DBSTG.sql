GRANT SELECT ON TRANSP.WAVE_INSTANCE_SEQ TO fdstore_ststg01;
GRANT SELECT ON TRANSP.WAVE_INSTANCE_SEQ TO fdtrn_ststg01;   
GRANT SELECT ON TRANSP.WAVE_INSTANCE_SEQ TO APPDEV;  
  
GRANT DELETE, INSERT, SELECT, UPDATE ON TRANSP.WAVE_INSTANCE TO fdstore_ststg01;
GRANT DELETE, INSERT, SELECT, UPDATE ON TRANSP.WAVE_INSTANCE TO fdtrn_ststg01;
GRANT SELECT ON TRANSP.WAVE_INSTANCE TO APPDEV;

GRANT DELETE, INSERT, SELECT, UPDATE ON TRANSP.WAVE_INSTANCE_PUBLISH TO fdstore_ststg01;
GRANT DELETE, INSERT, SELECT, UPDATE ON TRANSP.WAVE_INSTANCE_PUBLISH TO fdtrn_ststg01;
GRANT SELECT ON TRANSP.WAVE_INSTANCE_PUBLISH TO APPDEV;
