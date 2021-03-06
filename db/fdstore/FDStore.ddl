CREATE TABLE ContentNode
(
ID VARCHAR2(16) NOT NULL,
ALT_TEXT VARCHAR2(255),
CONTENT_NAME VARCHAR2(40) NOT NULL,
BLURB VARCHAR2(1024),
BLURB_TITLE VARCHAR2(255),
EDITORIAL_TITLE VARCHAR2(255),
FULL_NAME VARCHAR2(100),
GLANCE_NAME VARCHAR2(60),
NAV_NAME VARCHAR2(60),
CONTENT_TYPE VARCHAR2(1) NOT NULL,
CREATED DATE NOT NULL,
CREATED_BY VARCHAR2(40) NOT NULL,
LASTMODIFIED DATE NOT NULL,
LASTMODIFIED_BY VARCHAR2(40) NOT NULL,
BM_ID VARCHAR2(20) NOT NULL,
STORE_ID VARCHAR2(16) NOT NULL,
KEYWORDS VARCHAR2(200),
  CONSTRAINT PK_ContentNode PRIMARY KEY (ID)
);


CREATE TABLE Department
(
CONTENT_ID VARCHAR2(16) NOT NULL,
PRIORITY NUMBER(4) NOT NULL,
MAX_ROWCOUNT NUMBER(2) NOT NULL,
STORE_ID VARCHAR2(16) NOT NULL,
  CONSTRAINT PK_Department PRIMARY KEY (CONTENT_ID)
);


CREATE TABLE Category
(
CONTENT_ID VARCHAR2(16) NOT NULL,
PARENT_CATEGORY_ID VARCHAR2(16),
SIDE_NAV_BOLD VARCHAR2(1),
SIDE_NAV_LINK VARCHAR2(1),
SIDE_NAV_PRIORITY NUMBER(4),
SIDE_NAV_SHOWCHILDREN VARCHAR2(1),
SIDE_NAV_SHOWSELF VARCHAR2(1),
SHOW_SELF VARCHAR2(1),
SHOW_CHILDREN VARCHAR2(1),
PRIORITY NUMBER(4) NOT NULL,
FEATURED VARCHAR2(1),
DEPARTMENT_ID VARCHAR2(16),
SECONDARY VARCHAR2(1),
STORE_ID VARCHAR2(16) NOT NULL,
  CONSTRAINT PK_Category PRIMARY KEY (CONTENT_ID)
);


CREATE TABLE Product
(
CONTENT_ID VARCHAR2(16) NOT NULL,
AKA VARCHAR2(100),
QUANTITY_MIN NUMBER(4,2) NOT NULL,
QUANTITY_MAX NUMBER(4,2) NOT NULL,
QUANTITY_INCR NUMBER(4,2) NOT NULL,
INVISIBLE VARCHAR2(1),
PERISHABLE VARCHAR2(1),
FROZEN VARCHAR2(1),
GROCERY VARCHAR2(1),
STORE_ID VARCHAR2(16) NOT NULL,
  CONSTRAINT PK_Product PRIMARY KEY (CONTENT_ID)
);


CREATE TABLE ProductCategory
(
CATEGORY_ID VARCHAR2(16) NOT NULL,
PRODUCT_ID VARCHAR2(16) NOT NULL,
PRIORITY NUMBER(4) NOT NULL,
  CONSTRAINT PK_ProductCategory PRIMARY KEY (CATEGORY_ID, PRODUCT_ID)
);


CREATE TABLE Attribute
(
CONTENT_ID VARCHAR2(16) NOT NULL,
ATTRIBUTE_TYPE VARCHAR2(2) NOT NULL,
ATTRIBUTE_VALUE VARCHAR2(255),
ATTRIBUTE_NAME VARCHAR2(40) NOT NULL,
MEDIA_ID VARCHAR2(16),
PRIORITY NUMBER(4),
INHERITABLE VARCHAR2(1),
DOMAIN_ID VARCHAR2(16),
DOMAINVALUE_ID VARCHAR2(16),
STORE_ID VARCHAR2(16) NOT NULL,
REF_NAME VARCHAR2(40),
REF_NAME2 VARCHAR2(255),
REF_NAME3 VARCHAR2(40)
);


CREATE TABLE Media
(
ID VARCHAR2(16) NOT NULL,
MEDIA_TYPE VARCHAR2(1) NOT NULL,
HEIGHT NUMBER(3),
WIDTH NUMBER(3),
PATH VARCHAR2(255) NOT NULL,
STORE_ID VARCHAR2(16) NOT NULL,
  CONSTRAINT PK_Media PRIMARY KEY (ID)
);


CREATE TABLE Sku
(
CONTENT_ID VARCHAR2(16) NOT NULL,
PRODUCT_ID VARCHAR2(16) NOT NULL,
SKUCODE VARCHAR2(20) NOT NULL,
ORGANIC VARCHAR2(1),
KOSHER VARCHAR2(1),
STORE_ID VARCHAR2(16) NOT NULL,
  CONSTRAINT PK_Sku PRIMARY KEY (CONTENT_ID)
);


CREATE TABLE Store
(
CONTENT_ID VARCHAR2(16) NOT NULL,
NAME VARCHAR2(40) NOT NULL,
VERSION NUMBER(6) NOT NULL,
LOAD DATE NOT NULL,
ACTIVE DATE NOT NULL,
  CONSTRAINT PK_Store PRIMARY KEY (CONTENT_ID)
);


CREATE TABLE Domain
(
ID VARCHAR2(16) NOT NULL,
DOMAIN_TYPE VARCHAR2(1) NOT NULL,
NAME VARCHAR2(40) NOT NULL,
STORE_ID VARCHAR2(16) NOT NULL,
LABEL VARCHAR2(40),
  CONSTRAINT PK_Domain PRIMARY KEY (ID)
);


CREATE TABLE DomainValue
(
DOMAIN_ID VARCHAR2(16) NOT NULL,
LABEL VARCHAR2(40) NOT NULL,
VALUE VARCHAR2(40) NOT NULL,
PRIORITY NUMBER(4) NOT NULL,
ID VARCHAR2(16) NOT NULL,
  CONSTRAINT PK_DomainValue PRIMARY KEY (ID)
);


CREATE TABLE Brand
(
CONTENT_ID VARCHAR2(16) NOT NULL,
STORE_ID VARCHAR2(16) NOT NULL,
  CONSTRAINT PK_Brand PRIMARY KEY (CONTENT_ID)
);


CREATE TABLE ProductBrand
(
PRODUCT_ID VARCHAR2(16) NOT NULL,
BRAND_ID VARCHAR2(16) NOT NULL,
PRIORITY NUMBER(4) NOT NULL,
  CONSTRAINT PK_ProductBrand PRIMARY KEY (PRODUCT_ID, BRAND_ID)
);


ALTER TABLE Department ADD CONSTRAINT DPT_CN_FK FOREIGN KEY (
CONTENT_ID
)
REFERENCES ContentNode (
ID
);


ALTER TABLE Department ADD CONSTRAINT DPT_STR_FK FOREIGN KEY (
STORE_ID
)
REFERENCES Store (
CONTENT_ID
);


ALTER TABLE Category ADD CONSTRAINT CAT_CN_FK FOREIGN KEY (
CONTENT_ID
)
REFERENCES ContentNode (
ID
);


ALTER TABLE Category ADD CONSTRAINT CAT_CAT_FK FOREIGN KEY (
PARENT_CATEGORY_ID
)
REFERENCES Category (
CONTENT_ID
);


ALTER TABLE Category ADD CONSTRAINT CAT_DPT_FK FOREIGN KEY (
DEPARTMENT_ID
)
REFERENCES Department (
CONTENT_ID
);


ALTER TABLE Product ADD CONSTRAINT PRD_CN_FK FOREIGN KEY (
CONTENT_ID
)
REFERENCES ContentNode (
ID
);


ALTER TABLE ProductCategory ADD CONSTRAINT PCT_CAT_FK FOREIGN KEY (
CATEGORY_ID
)
REFERENCES Category (
CONTENT_ID
);


ALTER TABLE ProductCategory ADD CONSTRAINT PCT_PRD_FK FOREIGN KEY (
PRODUCT_ID
)
REFERENCES Product (
CONTENT_ID
);


ALTER TABLE Attribute ADD CONSTRAINT ATR_CN_FK FOREIGN KEY (
CONTENT_ID
)
REFERENCES ContentNode (
ID
);


ALTER TABLE Attribute ADD CONSTRAINT ATR_MED_FK FOREIGN KEY (
MEDIA_ID
)
REFERENCES Media (
ID
);


ALTER TABLE Attribute ADD CONSTRAINT ATR_DOM_FK FOREIGN KEY (
DOMAIN_ID
)
REFERENCES Domain (
ID
);


ALTER TABLE Attribute ADD CONSTRAINT ATR_DMV_FK FOREIGN KEY (
DOMAINVALUE_ID
)
REFERENCES DomainValue (
ID
);


ALTER TABLE Sku ADD CONSTRAINT SKU_CN_FK FOREIGN KEY (
CONTENT_ID
)
REFERENCES ContentNode (
ID
);


ALTER TABLE Sku ADD CONSTRAINT SKU_PRD_FK FOREIGN KEY (
PRODUCT_ID
)
REFERENCES Product (
CONTENT_ID
);


ALTER TABLE Store ADD CONSTRAINT STR_CN_FK FOREIGN KEY (
CONTENT_ID
)
REFERENCES ContentNode (
ID
);


ALTER TABLE DomainValue ADD CONSTRAINT DMV_DOM_FK FOREIGN KEY (
DOMAIN_ID
)
REFERENCES Domain (
ID
);


ALTER TABLE Brand ADD CONSTRAINT BRD_CN_FK FOREIGN KEY (
CONTENT_ID
)
REFERENCES ContentNode (
ID
);


ALTER TABLE ProductBrand ADD CONSTRAINT PBD_PRD_FK FOREIGN KEY (
PRODUCT_ID
)
REFERENCES Product (
CONTENT_ID
);


ALTER TABLE ProductBrand ADD CONSTRAINT PBD_BRD_FK FOREIGN KEY (
BRAND_ID
)
REFERENCES Brand (
CONTENT_ID
);


