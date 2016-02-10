ALTER TABLE TRANSP.TIP_DISTRIBUTION_BATCH MODIFY (  BATCH_ID    NUMBER(20));

ALTER TABLE TRANSP.TIP_DISTRIBUTION MODIFY (  BATCH_ID    NUMBER(20));

GRANT SELECT ON TRANSP.TIP_DISTRIBUTION_BATCH  TO APPDEV;

CREATE INDEX TRANSP.IDX_FDX_BAGINFO_ERPORDID ON TRANSP.FDX_BAGINFO(ERPORDER_ID) tablespace TRANSPDAT;

ALTER TABLE TRANSP.FDX_EQUIPMENT_TYPE ADD CONSTRAINT FDX_EQUIPMENT_TYPE_PK PRIMARY KEY (ID) using index tablespace TRANSPDAT;

ALTER TABLE TRANSP.FDX_TRIP_ORDER ADD CONSTRAINT FDX_TRIP_ORDER_PK PRIMARY KEY (TRIP_ID, ORDER_ID) using index tablespace TRANSPDAT;

ALTER TABLE TRANSP.FDX_TRIP_STAFF ADD CONSTRAINT FDX_TRIP_STAFF_PK PRIMARY KEY (TRIP_ID, STAFF_ID) using index tablespace TRANSPDAT;

CREATE INDEX TRANSP.TS_STAFF_INDEX ON TRANSP.FDX_TRIP_STAFF(STAFF_ID) tablespace TRANSPDAT;

ALTER TABLE TRANSP.FDX_TRIP_ORDER ADD CONSTRAINT FDX_TRIP_ORDER_ORDID_FK FOREIGN KEY (ORDER_ID) REFERENCES TRANSP.FDX_ORDER (ORDER_ID);

CREATE INDEX TRANSP.FDX_TRIP_ORDER_ORDID_FK ON TRANSP.FDX_TRIP_ORDER(ORDER_ID) tablespace TRANSPDAT;

CREATE INDEX TRANSP.FDX_ORDERACTION_IDX3 ON TRANSP.FDX_ORDERACTION(ORDER_ID, ACTION_TYPE, INSERT_TIMESTAMP) tablespace TRANSPDAT;

drop index transp.PK_FDX_ORDERACTION;

ALTER TABLE TRANSP.FDX_ORDERACTION ADD CONSTRAINT PK_FDX_ORDERACTION PRIMARY KEY (ID) using index tablespace TRANSPDAT;

ALTER TABLE TRANSP.TIP_DISTRIBUTION_BATCH ADD EMAIL_SENT VARCHAR2(1);

ALTER TABLE TRANSP.TIP_DISTRIBUTION DROP COLUMN BATCH_STATUS;

CREATE INDEX TRANSP.IDX_BATCH_DLVDATE ON TRANSP.TIP_DISTRIBUTION(BATCH_ID, DELIVERY_DATE) tablespace TRANSPDAT;

ALTER TABLE TRANSP.TIP_DISTRIBUTION_BATCH ADD CONSTRAINT TIP_DISTRIBUTION_BATCH_PK PRIMARY KEY (BATCH_ID, WEEK_OF, DELIVERY_DATE) using index tablespace TRANSPDAT;

