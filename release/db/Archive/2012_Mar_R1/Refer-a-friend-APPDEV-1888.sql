create table cust.referral_prgm (
ID				 VARCHAR2(16 BYTE)    NOT NULL PRIMARY KEY,
expiration_Date  DATE,
give_text	     VARCHAR2(256 BYTE)   NOT NULL,
get_text	     VARCHAR2(256 BYTE)   NOT NULL,
description      VARCHAR2(256 BYTE)   NOT NULL,
promotion_id     VARCHAR2(16 BYTE)    NOT NULL,
referral_fee     NUMBER(3)		      NOT NULL,
created_Date	 DATE,
MODIFIED_DATE	 DATE,
DEFAULT_PROMO	 VARCHAR2(1),
SHARE_HEADER	 VARCHAR2(100),
SHARE_TEXT       VARCHAR2(256),
GIVE_HEADER	 	 VARCHAR2(100),
GET_HEADER	 	 VARCHAR2(100),
NOTES			 VARCHAR2(2000),
FB_IMAGE_PATH	 VARCHAR2(256),
FB_HEADLINE      VARCHAR2(150),
FB_TEXT			 VARCHAR2(2000),
TWITTER_TEXT	 VARCHAR2(2000),
RL_PAGE_TEXT     VARCHAR2(2000),
RL_PAGE_LEGAL	 VARCHAR2(2000),
INV_EMAIL_SUBJECT VARCHAR2(500),
INV_EMAIL_TEXT	 VARCHAR2(3000),
INV_EMAIL_LEGAL  VARCHAR2(2000),
REF_CRE_EMAIL_SUB VARCHAR2(500),
REF_CRE_EMAIL_TEXT VARCHAR2(3000),
Delete_flag      VARCHAR2(1),
add_by_date      DATE,
change_by_date   DATE,
add_by_user      VARCHAR2(50),
change_by_user   VARCHAR2(50)
);

ALTER TABLE CUST.referral_prgm
ADD CONSTRAINT fk_rp_promotion_id
  FOREIGN KEY (promotion_id)
  REFERENCES CUST.promotion_new(ID);

create table cust.referral_customer_list (
referal_prgm_id VARCHAR2(16 BYTE)    NOT NULL,
erp_customer_id VARCHAR2(16)         NOT NULL
);

ALTER TABLE CUST.referral_customer_list
ADD CONSTRAINT fk_referal_prgm_id
  FOREIGN KEY (referal_prgm_id)
  REFERENCES CUST.referral_prgm(ID);

ALTER TABLE CUST.referral_customer_list
ADD CONSTRAINT fk_rcl_customer_id
  FOREIGN KEY (erp_customer_id)
  REFERENCES CUST.customer(ID);

alter table cust.fdcustomer add (referer_customer_id VARCHAR2(16));

ALTER TABLE cust.fdcustomer
  ADD
    CONSTRAINT customer_ref_cust_idx
    FOREIGN KEY
   ( referer_customer_id )
    REFERENCES cust.customer ( ID );
	
--alter prmotion table
ALTER TABLE CUST.PROMOTION_NEW ADD REFERRAL_PROMO VARCHAR2(1) DEFAULT 'N';

--create a new table to hold the referral link
create table cust.referral_link (
referral_link varchar2(150),
customer_id   varchar2(16) );

ALTER TABLE cust.referral_link
  ADD
    CONSTRAINT customer_ref_link_unique_idx
    UNIQUE( referral_link );
	
ALTER TABLE CUST.referral_link
ADD CONSTRAINT fk_rl_customer_id
  FOREIGN KEY (customer_id)
  REFERENCES CUST.customer(ID);


--mail sent list
CREATE TABLE CUST.CUSTOMER_INVITES (
REFERRAL_CUSTOMER_ID	VARCHAR2(16),
FRIENDS_EMAIL			VARCHAR2(80),
SENT_DATE				DATE,
CREDIT_ISSUED			NUMBER(10,2),
CREDIT_ISSUED_DATE		DATE,
FRIENDS_CUSTOMER_ID  	VARCHAR2(16),
CREDIT_SALE_ID			VARCHAR2(16),
COMPLAINT_ID			VARCHAR2(16),
REFERRED_REF_PRGM_ID     VARCHAR2(16 BYTE),
CREDIT_ERND_REF_PRGM_ID  VARCHAR2(16 BYTE)
);

ALTER TABLE CUST.CUSTOMER_INVITES ADD (
  CONSTRAINT CUSTOMER_INVITES_UNIQUE_IDX
 UNIQUE (REFERRAL_CUSTOMER_ID, FRIENDS_EMAIL));

CREATE TABLE CUST.RAF_FAILED_ATTEMPTS (
EMAIL             VARCHAR2(80),
CUSTOMER_ID	      VARCHAR2(16),
FIRST_NAME        VARCHAR2(100),
LAST_NAME         VARCHAR2(100),
ZIPCODE			  VARCHAR2(10),
RAF_URL			  VARCHAR2(250),
REASON			  VARCHAR2(50),
ROW_ADDED_DATE    DATE
);

--creating department and compalint code
insert into CUST.DEPARTMENT select 'RAF', 'Referral', 'Refer A Friend', null from dual;

insert into cust.complaint_dept select 'RAF', 'Referral' from dual;

insert into CUST.COMPLAINT_CODE (CODE, NAME, SUBJECT_CODE, PRIORITY) Values ('RAFPRGM', 'Referral Program', 'RAF-001', 1);

insert into cust.complaint_dept_code select 'RAFPRGM', 'RAF', 10, null, cust.system_seq.nextval from dual;
