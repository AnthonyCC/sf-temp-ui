--Creating Function index on redemption code column.

CREATE UNIQUE INDEX CUST.REDEM_CODE_IDX ON CUST.PROMOTION (UPPER("REDEMPTION_CODE"));