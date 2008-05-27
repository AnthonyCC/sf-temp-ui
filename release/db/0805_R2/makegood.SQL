ALTER TABLE CUST.complaint ADD makegood_sale_id VARCHAR2(16 BYTE);
CREATE INDEX COMPLAINT_MAKEGOOD_SALE_ID_IDX ON CUST.COMPLAINT
(MAKEGOOD_SALE_ID)
LOGGING
TABLESPACE FDCUSIDX
PCTFREE    10
INITRANS   2
MAXTRANS   255
STORAGE    (
            INITIAL          64K
            MINEXTENTS       1
            MAXEXTENTS       2147483645
            PCTINCREASE      0
            BUFFER_POOL      DEFAULT
           )
NOPARALLEL;

insert into cust.complaint_dept (code, name) values('MGD','Makegood');

insert into cust.complaint_dept_code (id,comp_code,comp_dept,priority,obsolete) values((cust.SYSTEM_SEQ.nextval), ' BRUISED','MGD','10','' ); 
insert into cust.complaint_dept_code (id,comp_code,comp_dept,priority,obsolete) values((cust.SYSTEM_SEQ.nextval), 'DAMBOX','MGD','10','' ); 
insert into cust.complaint_dept_code (id,comp_code,comp_dept,priority,obsolete) values((cust.SYSTEM_SEQ.nextval), ' DEFROST','MGD','10','' ); 
insert into cust.complaint_dept_code (id,comp_code,comp_dept,priority,obsolete) values((cust.SYSTEM_SEQ.nextval), ' MISBOX','MGD','10','' ); 
insert into cust.complaint_dept_code (id,comp_code,comp_dept,priority,obsolete) values((cust.SYSTEM_SEQ.nextval), ' MISFRZ','MGD','10','' ); 
insert into cust.complaint_dept_code (id,comp_code,comp_dept,priority,obsolete) values((cust.SYSTEM_SEQ.nextval), ' MISITM','MGD','10','' ); 
insert into cust.complaint_dept_code (id,comp_code,comp_dept,priority,obsolete) values((cust.SYSTEM_SEQ.nextval), ' PRDDAM','MGD','10','' ); 
insert into cust.complaint_dept_code (id,comp_code,comp_dept,priority,obsolete) values((cust.SYSTEM_SEQ.nextval), ' PRDEXP','MGD','10','' ); 
insert into cust.complaint_dept_code (id,comp_code,comp_dept,priority,obsolete) values((cust.SYSTEM_SEQ.nextval), ' PRDLEK','MGD','10','' ); 
insert into cust.complaint_dept_code (id,comp_code,comp_dept,priority,obsolete) values((cust.SYSTEM_SEQ.nextval), ' PRDTMP','MGD','10','' ); 
insert into cust.complaint_dept_code (id,comp_code,comp_dept,priority,obsolete) values((cust.SYSTEM_SEQ.nextval), ' PRDXCL','MGD','10','' ); 
insert into cust.complaint_dept_code (id,comp_code,comp_dept,priority,obsolete) values((cust.SYSTEM_SEQ.nextval), ' QUALDIS','MGD','10','' ); 
insert into cust.complaint_dept_code (id,comp_code,comp_dept,priority,obsolete) values((cust.SYSTEM_SEQ.nextval), ' RTLBWRBX','MGD','10','' ); 
insert into cust.complaint_dept_code (id,comp_code,comp_dept,priority,obsolete) values((cust.SYSTEM_SEQ.nextval), ' RTLBWRPR','MGD','10','' ); 
insert into cust.complaint_dept_code (id,comp_code,comp_dept,priority,obsolete) values((cust.SYSTEM_SEQ.nextval), ' SPOILED','MGD','10','' ); 
insert into cust.complaint_dept_code (id,comp_code,comp_dept,priority,obsolete) values((cust.SYSTEM_SEQ.nextval), ' EARLDEL','MGD','10','' ); 
insert into cust.complaint_dept_code (id,comp_code,comp_dept,priority,obsolete) values((cust.SYSTEM_SEQ.nextval), ' DOORMAN','MGD','10','' ); 
insert into cust.complaint_dept_code (id,comp_code,comp_dept,priority,obsolete) values((cust.SYSTEM_SEQ.nextval), ' WRMISPK','MGD','10','' ); 

