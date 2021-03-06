CREATE INDEX SALACT_SALE_IDX ON SALESACTION(SALE_ID, ACTION_TYPE, ACTION_DATE);

CREATE INDEX PROFILE_FDCUST_ID_IDX ON PROFILE(CUSTOMER_ID);

CREATE INDEX CUST.SALE_CUST_IDX
     ON CUST.SALE
        (CUSTOMER_ID,
         "ID",
         "STATUS");

CREATE INDEX COMPLAINT_SALE_ID_IDX ON COMPLAINT(SALE_ID);

CREATE INDEX CUSTOMERCREDIT_CUSTOMER_ID_IDX ON CUSTOMERCREDIT(CUSTOMER_ID);

CREATE INDEX CUSTOMERCREDIT_COMPLNT_ID_IDX ON CUSTOMERCREDIT(COMPLAINT_ID);

CREATE INDEX APPLIEDCREDIT_SALESACTION_IDX ON APPLIEDCREDIT(SALESACTION_ID);

CREATE INDEX CAMPAIGN_USE_IDX ON CAMPAIGN_USE(CUSTOMER_ID, CAMPAIGN_CODE);

CREATE UNIQUE INDEX SEGMENT_UNIQ ON 
  SEGMENT(TOTAL_ORDERS, ORDER_FREQUENCY, ACTIVE) ; 

CREATE INDEX CSG_CUST_ID_IDX ON 
  CUST_SEGMENT(CUST_ID) ; 

CREATE INDEX CSG_SEGMENT_ID_IDX ON 
  CUST_SEGMENT(SEGMENT_ID) ; 

CREATE INDEX CEM_CUST_ID_IDX ON 
  CUST_EMAIL(CUST_ID) ; 

CREATE INDEX CEM_EMAIL_ID_IDX ON 
  CUST_EMAIL(EMAIL_CAMPAIGN_ID) ; 
  
CREATE INDEX CDC_UNIQ ON COMPLAINT_DEPT_CODE(COMP_DEPT, COMP_CODE);

CREATE INDEX CUST.DLVINFO_STARTTIME_IDX ON CUST.DELIVERYINFO(STARTTIME);

CREATE BITMAP INDEX CUST.SALE_STATUS_IDX ON CUST.SALE(STATUS);  

CREATE INDEX CUST.SALACT_REQDATE_ACTION_IDX ON CUST.SALESACTION(REQUESTED_DATE, ACTION_TYPE, ACTION_DATE);

CREATE INDEX ADDR_FRAUD ON ADDRESS(SCRUBBED_ADDRESS, APARTMENT, ZIP); 

CREATE INDEX DLVINFO_FRAUD ON DELIVERYINFO(SCRUBBED_ADDRESS, APARTMENT, ZIP);

CREATE INDEX DI_FIRSTNAME ON DELIVERYINFO (LOWER("FIRST_NAME"));

CREATE INDEX DI_LASTNAME ON DELIVERYINFO (LOWER("LAST_NAME"));

CREATE INDEX CUST.DI_PHONE ON  DELIVERYINFO(PHONE); 

CREATE INDEX CASE_SUBJ_STATE_ID_CUST_IDX ON CUST.CASE
(CASE_SUBJECT, CASE_STATE, CUSTOMER_ID);

CREATE INDEX CUST.FDCUST_CUST_IDX
     ON CUST.FDCUSTOMER
        (ERP_CUSTOMER_ID,
         "ID");

CREATE UNIQUE INDEX CUST.SALE_IDCUSTSTATUS_IDX
     ON CUST.SALE
        ("ID",
         CUSTOMER_ID,
         "STATUS");

CREATE INDEX CUST.SALESACTION_DATESIDS_IDX
     ON CUST.SALESACTION
        (REQUESTED_DATE,
         ACTION_DATE,
         SALE_ID,
         "ID",
         AMOUNT);

CREATE INDEX CUST.SALESACTION_TYPEID_IDX
     ON CUST.SALESACTION
        (ACTION_TYPE,
         SALE_ID,
         ACTION_DATE,
         SOURCE);
		 
CREATE INDEX CUST.SALACT_ATYPE_ADATE_SID_ID_IDX
     ON CUST.SALESACTION
        (ACTION_TYPE,
         ACTION_DATE,
         SALE_ID,
         "ID",
         AMOUNT,
         REQUESTED_DATE,
         SOURCE);

CREATE INDEX CUST.COMPLAINT_STATUS_SALE_AMOUNT
     ON CUST.COMPLAINT
        ("STATUS",
         SALE_ID,
         AMOUNT);
         

CREATE UNIQUE INDEX CUST.PROMO_CUST_PROMO_CUST_IDX
 ON CUST.PROMO_CUSTOMER(PROMOTION_ID, CUSTOMER_ID);
 
CREATE INDEX IDX_LIST_ID ON 
  CUST.CUSTOMERLIST_DETAILS(LIST_ID); 

CREATE INDEX IDX_LIST_ID_NAME ON 
  CUST.CUSTOMERLIST(CUSTOMER_ID, NAME);
  
CREATE INDEX EVENTS_TIMESTAMP_IDX ON EVENTS(TIMESTAMP);

CREATE INDEX SALE_CRO_MOD_MAX_DATE_IDX ON CUST.SALE_CRO_MOD_DATE(MAX_DATE);

CREATE INDEX CASE_STATE_AGENT_IDX ON CUST.CASE
(ASSIGNED_AGENT_ID, CASE_STATE);


CREATE INDEX CUSTINFO_FIRSTNAME_IDX ON CUST.CUSTOMERINFO
(LOWER("FIRST_NAME"));

CREATE INDEX CUSTINFO_LASTNAME_IDX ON CUST.CUSTOMERINFO
(LOWER("LAST_NAME"));
