CREATE TABLE CUST.SS_PRODUCT_RECOMMENDATION (
  RECOMMENDER VARCHAR2(20) NOT NULL,
  CONTENT_KEY VARCHAR2(30) NOT NULL,
  PRIORITY NUMBER(4, 0) NOT NULL,
  RECOMMENDED_PRODUCT VARCHAR2(30) NOT NULL
, CONSTRAINT SS_PRODUCT_RECOMMENDATION_PK PRIMARY KEY
  (
    RECOMMENDER,
    CONTENT_KEY,
    PRIORITY
  )
);

CREATE TABLE CUST.SS_USER_RECOMMENDATION
(
  RECOMMENDER VARCHAR2(20) NOT NULL,
  CUSTOMER_ID VARCHAR2(16) NOT NULL,
  PRIORITY NUMBER(4, 0) NOT NULL,
  RECOMMENDED_PRODUCT VARCHAR2(30) NOT NULL
, CONSTRAINT SS_USER_RECOMMENDATION_PK PRIMARY KEY
  (
    RECOMMENDER,
    CUSTOMER_ID,
    PRIORITY
  )
)

