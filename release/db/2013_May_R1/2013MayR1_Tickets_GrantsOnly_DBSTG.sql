GRANT DELETE, INSERT, SELECT, UPDATE ON TRANSP.EMPLOYEESUPERVISOR TO fdstore_ststg01;
GRANT DELETE, INSERT, SELECT, UPDATE ON TRANSP.EMPLOYEESUPERVISOR TO fdtrn_ststg01;
GRANT SELECT ON TRANSP.EMPLOYEESUPERVISOR TO appdev;

GRANT DELETE, INSERT, SELECT, UPDATE ON TRANSP.TRN_FACILITYLOCATION TO fdstore_ststg01;
GRANT DELETE, INSERT, SELECT, UPDATE ON TRANSP.TRN_FACILITYLOCATION TO fdtrn_ststg01;
GRANT SELECT ON TRANSP.TRN_FACILITYLOCATION TO appdev;

GRANT DELETE, INSERT, SELECT, UPDATE ON TRANSP.ASSET_ACTIVITY TO fdstore_ststg01;
GRANT DELETE, INSERT, SELECT, UPDATE ON TRANSP.ASSET_ACTIVITY TO fdtrn_ststg01;
GRANT SELECT ON TRANSP.ASSET_ACTIVITY TO appdev;

GRANT DELETE, INSERT, SELECT, UPDATE ON CUST.SALE_MATERIAL TO fdstore_ststg01;
GRANT DELETE, INSERT, SELECT, UPDATE ON CUST.SALE_MATERIAL TO fdtrn_ststg01;
GRANT SELECT ON CUST.SALE_MATERIAL TO appdev;
