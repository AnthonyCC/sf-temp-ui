GRANT DELETE, INSERT, SELECT, UPDATE ON TRANSP.DISPATCH_GROUP TO fdstore_stprd01;
GRANT DELETE, INSERT, SELECT, UPDATE ON TRANSP.DISPATCH_GROUP TO fdtrn_stprd01;
GRANT SELECT ON TRANSP.DISPATCH_GROUP TO appdev;

GRANT SELECT ON TRANSP.DISPATCHGROUPSEQ TO fdstore_stprd01;
GRANT SELECT ON TRANSP.DISPATCHGROUPSEQ TO fdtrn_stprd01;
GRANT SELECT ON TRANSP.DISPATCHGROUPSEQ TO appdev;