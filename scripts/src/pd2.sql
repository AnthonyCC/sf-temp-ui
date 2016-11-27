
SELECT count(*) from erps.attributes where ROOT_ID in ('0', '000000000300740012', '000000000300720104') and restr in ('TMPC');

-- 7 records will be removed.

DELETE from erps.attributes where ROOT_ID in ('0', '000000000300740012', '000000000300720104') and restr in ('TMPC');



INSERT into erps.attributes (ID, ROOT_ID, CHILD1_ID, CHILD_ID2, ATR_TYPE, ATR_NAME, ATR_VALUE, DATE_MODIFIED)  
VALUES (ERPS.SYSTEM_SEQ_NEXTVAL, '000000000300740012', ' ', ' ', 'S', 'restrictions', 'TMPC', SYSDATE);

INSERT into erps.attributes (ID, ROOT_ID, CHILD1_ID, CHILD_ID2, ATR_TYPE, ATR_NAME, ATR_VALUE, DATE_MODIFIED)  
VALUES (ERPS.SYSTEM_SEQ_NEXTVAL, '000000000300720104', ' ', ' ', 'S', 'restrictions', 'TMPC', SYSDATE);

-- 2 records will be inserted.

update dlv.restricted_days set start_time = TO_DATE('2008/11/24 00:00:00', 'YYYY/MM/DD HH24:MI:SS'), end_time = TO_DATE('2008/12/31 23:59:59', 'YYYY/MM/DD HH24:MI:SS')  where reason = 'TMPC';

-- 1 record will be updated.


SELECT count(*) from erps.attribute from erps.attributes where ROOT_ID in ('0', '000000000300740012', '000000000300720104') and restr in ('TMPA');

-- n records will be updated

update erps.attributes set restr = 'TMPA,TMPC' where ROOT_ID in ('0', '000000000300740012', '000000000300720104') and restr in ('TMPA');
