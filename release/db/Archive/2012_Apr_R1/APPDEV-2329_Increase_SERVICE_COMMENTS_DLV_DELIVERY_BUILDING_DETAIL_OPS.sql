ALTER TABLE DLV.DELIVERY_BUILDING_DETAIL_OPS MODIFY SERVICE_COMMENTS VARCHAR2(255);

UPDATE  DLV.DELIVERY_BUILDING_DETAIL_OPS SET SERVICE_COMMENTS = SERVICE_COMMENTS2 WHERE SERVICE_COMMENTS2 IS NOT NULL;

COMMIT;

ALTER TABLE DLV.DELIVERY_BUILDING_DETAIL_OPS DROP COLUMN SERVICE_COMMENTS2;