insert into CUST.COMPLAINT_DEPT_CODE select COMP_CODE, 'WGD', PRIORITY, OBSOLETE, CUST.SYSTEM_SEQ.NEXTVAL from CUST.COMPLAINT_DEPT_CODE x where X.COMP_CODE like 'DP%' and X.COMP_DEPT = 'OURPICKS';