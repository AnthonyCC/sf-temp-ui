--
-- Definition for table "VIRRecord"
--
CREATE TABLE VIRRecord  
(
    id VARCHAR2(30),   
    TruckNumber VARCHAR2 (10) NOT NULL , 
    VendorName VARCHAR2 (40) ,
    ReportingDriver VARCHAR2 (60) DEFAULT  '' NOT NULL , 
    CreatedBy VARCHAR2 (10) NOT NULL , 
    DateCreated  DATE NOT NULL , 
    PRIMARY KEY (id)
);

CREATE  SEQUENCE  VIRRecord_id_SEQ START WITH 1 INCREMENT BY 1;

CREATE INDEX FK_VIRRecord_AuthUser ON VIRRecord  ( CreatedBy );

CREATE INDEX FK_VIRRecord_TruckNumber ON VIRRecord  ( TruckNumber );

--
-- Definition for table "IssueLog"
--
CREATE TABLE IssueLog  
(
    id VARCHAR2(30), 
    VIRRECORD_ID VARCHAR2(30),       
    IssueType VARCHAR2(45) NOT NULL , 
    IssueSubType VARCHAR2(45) NOT NULL , 
    Comments VARCHAR2 (200) DEFAULT  '' , 
    IssueLocation VARCHAR2 (40) DEFAULT  '' NOT NULL , 
    IssueSide VARCHAR2 (40) DEFAULT  '' NOT NULL,
    MaintenanceIssueID VARCHAR2(30) ,     
    PRIMARY KEY (id) 
);

CREATE  SEQUENCE  IssueLog_id_SEQ START WITH 1 INCREMENT BY 1;

CREATE INDEX FK_IssueLog_IssueSubType ON IssueLog  ( IssueSubType );

CREATE INDEX FK_IssueLog_IssueType ON IssueLog  ( IssueType );

--
-- Definition for table "MaintenanceIssueID"
--
CREATE TABLE TRANSP.MAINTENANCEISSUE
(
  ID                    VARCHAR2(30 BYTE),
  TRUCKNUMBER           VARCHAR2(45 BYTE)       NOT NULL,
  VENDORNAME            VARCHAR2(45 BYTE)       NOT NULL,
  ISSUETYPE            VARCHAR2(45)              NOT NULL,
  ISSUESUBTYPE        VARCHAR2(45)             NOT NULL,
  ISSUESTATUS           VARCHAR2(20 BYTE)       DEFAULT 'Open',
  COMMENTS              VARCHAR2(200 BYTE)      DEFAULT '',
  ISSUELOCATION         VARCHAR2(40 BYTE)       DEFAULT ''                    NOT NULL,
  ISSUESIDE             VARCHAR2(20 BYTE)       DEFAULT ''                    NOT NULL,
  DATECREATED           DATE                    NOT NULL,
  MODIFIEDDATE          DATE                    NOT NULL,
  CREATEDBY             VARCHAR2(40 BYTE)       NOT NULL,
  SERVICESTATUS         VARCHAR2(20 BYTE)       DEFAULT 'In Service',
  ESTIMATED_REPAIRDATE  DATE,
  ACTUAL_REPAIRDATE     DATE,
  DATE_VERIFIED         DATE,
  VERIFIEDBY            VARCHAR2(20 BYTE)       DEFAULT '',
  REPAIREDBY            VARCHAR2(20 BYTE)       DEFAULT '',
  PRIMARY KEY (id)
)

CREATE SEQUENCE  MaintenanceIssue_id_SEQ  START WITH 1  INCREMENT BY 1;

CREATE INDEX FK_MaintenanceLog_IssueSubType ON MAINTENANCEISSUE  ( ISSUESUBTYPE );

CREATE INDEX FK_MaintenanceLog_IssueType ON MAINTENANCEISSUE  ( ISSUETYPE );

CREATE INDEX FK_MaintenanceLog_VerifiedBy ON MAINTENANCEISSUE  ( VERIFIEDBY );

CREATE INDEX FK_MaintenanceLog_SrcStatus ON MAINTENANCEISSUE  ( SERVICESTATUS );

--
-- Definition for table "IssueSubType"
--

CREATE TABLE IssueSubType  
(
    id NUMBER (10), 
    IssueTypeId NUMBER (10) NOT NULL , 
    IssueSubTypeName VARCHAR2 (45) DEFAULT  '' NOT NULL , 
    IssueSubTypeDescription VARCHAR2 (45) DEFAULT  '' NOT NULL , 
    isActive NUMBER (10) DEFAULT  '0' NOT NULL , 
    CreatedBy VARCHAR2 (45) NOT NULL , 
    DateCreated DATE  NOT NULL , 
    PRIMARY KEY (id) , 
    UNIQUE (IssueSubTypeName, IssueTypeId)
);

CREATE  SEQUENCE  IssueSubType_id_SEQ   START WITH 1 INCREMENT BY 1;

CREATE INDEX FK_IssueSubType_IssueTypeID ON IssueSubType  ( IssueTypeId );

CREATE INDEX FK_IssueSubType_UserAuthID ON IssueSubType  ( CreatedBy );

--
-- Definition for table "IssueType"
--

CREATE TABLE IssueType  
(
    id NUMBER (10), 
    IssueTypeName VARCHAR2 (45) DEFAULT  '' NOT NULL , 
    IssueTypeDescription VARCHAR2 (45) DEFAULT  '' NOT NULL , 
    isActive NUMBER (10) DEFAULT  '0' NOT NULL , 
    CreatedBy VARCHAR2 (20) NOT NULL , 
    DateCreated DATE  NOT NULL , 
    PRIMARY KEY (id) , 
    UNIQUE (IssueTypeName) 
);

CREATE  SEQUENCE  IssueType_id_SEQ  START WITH 1 INCREMENT BY 1;

CREATE INDEX FK_EventType_UserAuth ON IssueType  ( CreatedBy );

--
-- Foreign keys for table "IssueLog"
--
ALTER TABLE IssueLog add constraint  FK_IssueLog_IssueSubType FOREIGN KEY (IssueSubType) REFERENCES IssueSubType (IssueSubTypeName);
add
(
    CONSTRAINT FK_IssueLog_VIRRecord FOREIGN KEY (VIRRecord_id) REFERENCES VIRRecord (id) ,  
    CONSTRAINT FK_IssueLog_MaintenanceIssue FOREIGN KEY (MaintenanceIssueID) REFERENCES MAINTENANCEISSUE (id)
);

--
-- Foreign keys for table "IssueSubType"
--
ALTER TABLE IssueSubType ADD CONSTRAINT FK_IssueSubType_IssueTypeID FOREIGN KEY (IssueTypeId) REFERENCES IssueType (id);

--
-- Definition for table "ASSET_ATRTEMPLATE"
--

CREATE TABLE TRANSP.ASSETTEMPLATE
(
  ATR_TEMPLATE_ID          VARCHAR2(40 BYTE),
  ATR_TEMPLATE_NAME     VARCHAR2(40 BYTE)          NOT NULL,
  ASSET_TYPE         VARCHAR2(40 BYTE)          NOT NULL ,
  PRIMARY KEY (ATR_TEMPLATE_ID) 
);

CREATE TABLE TRANSP.ASSETTEMPLATE_ATTRIBUTE
(
  ATR_TEMPLATE_ID           VARCHAR2(40 BYTE),
  ATTRIBUTE_TYPE   VARCHAR2(40 BYTE)            NOT NULL,
  ATTRIBUTE_VALUE  VARCHAR2(40 BYTE)            NOT NULL,
  PRIMARY KEY (ATR_TEMPLATE_ID, ATTRIBUTE_TYPE)  
);

--
-- Foreign keys for table "ASSET_ATRTEMPLATE"
--
ALTER TABLE ASSETTEMPLATE ADD(CONSTRAINT FK_ASSETTYPE_ID FOREIGN KEY (ASSET_TYPE) REFERENCES  TRANSP.ASSET_TYPE (CODE));

--
-- Foreign keys for table "ASSETTEMPLATE_ATTRIBUTE"
--
ALTER TABLE ASSETTEMPLATE_ATTRIBUTE ADD
(
    CONSTRAINT FK_ASSETTEMPLATE_ID FOREIGN KEY (ATR_TEMPLATE_ID) REFERENCES  TRANSP.ASSETTEMPLATE (ATR_TEMPLATE_ID) ,
    CONSTRAINT FK_ATTRIBUTETYPE_ID FOREIGN KEY (ATTRIBUTE_TYPE) REFERENCES  TRANSP.ASSET_ATTRIBUTETYPE (CODE)
);

ALTER TABLE TRANSP.ASSET_ATTRIBUTETYPE add(ASSET_TYPE  VARCHAR2(40 BYTE)  NOT NULL);

ALTER TABLE TRANSP.ASSET_ATTRIBUTETYPE add(CONSTRAINT FK_ASSETTYPE FOREIGN KEY (ASSET_TYPE) REFERENCES  TRANSP.ASSET_TYPE (CODE));

ALTER TABLE TRANSP.ASSET add (ASSET_TEMPLATE       VARCHAR2(40 BYTE));

ALTER TABLE TRANSP.ASSET_ATTRIBUTE add(ATTRIBUTE_MATCH       VARCHAR2(1 BYTE));

CREATE INDEX TRANSP.IDX01_ASSETTEMPLATE ON TRANSP.ASSETTEMPLATE (ASSET_TYPE);

CREATE INDEX TRANSP.IDX01_ASSETTEMPLATE_ATTRIBUTE ON TRANSP.ASSETTEMPLATE_ATTRIBUTE (ATR_TEMPLATE_ID);

CREATE  SEQUENCE  ATTRIBUTE_TEMPLATE_SEQ   START WITH 1  INCREMENT BY 1;

CREATE  SEQUENCE  ASSETTEMPLATE_ATTRIBUTE_SEQ   START WITH 1  INCREMENT BY 1;

GRANT ALL ON TRANSP.VIRRecord_id_SEQ TO fdtrn_stprd01;
GRANT READ ON TRANSP.VIRRecord_id_SEQ TO APPDEV;

GRANT ALL ON TRANSP.IssueLog_id_SEQ TO fdtrn_prda;
GRANT READ ON TRANSP.IssueLog_id_SEQ TO APPDEV;

GRANT ALL ON TRANSP.VIRRECORD TO fdtrn_stprd01;
GRANT READ ON TRANSP.VIRRECORD TO APPDEV;

GRANT ALL ON TRANSP.IssueLog TO fdtrn_stprd01;
GRANT READ ON TRANSP.IssueLog TO APPDEV;

GRANT ALL ON TRANSP.IssueSubType_id_SEQ TO fdtrn_stprd01;
GRANT READ ON TRANSP.IssueSubType_id_SEQ TO APPDEV;

GRANT ALL ON TRANSP.IssueSubType TO fdtrn_stprd01;
GRANT READ ON TRANSP.IssueSubType TO APPDEV;

GRANT ALL ON TRANSP.IssueType TO fdtrn_stprd01;
GRANT READ ON TRANSP.IssueType TO APPDEV;

GRANT ALL ON TRANSP.IssueType_id_SEQ TO fdtrn_stprd01;
GRANT READ ON TRANSP.IssueType_id_SEQ TO APPDEV;

GRANT ALL ON TRANSP.MaintenanceIssue_id_SEQ TO fdtrn_prda;
GRANT READ ON TRANSP.MaintenanceIssue_id_SEQ TO APPDEV;

GRANT ALL ON TRANSP.MaintenanceIssue TO fdtrn_stprd01;
GRANT READ ON TRANSP.MaintenanceIssue TO APPDEV;

GRANT ALL ON TRANSP.ATTRIBUTE_TEMPLATE_SEQ TO fdtrn_stprd01;
GRANT READ ON TRANSP.ATTRIBUTE_TEMPLATE_SEQ TO appdev;

GRANT ALL ON TRANSP.ASSETTEMPLATE TO fdtrn_stprd01;
GRANT READ ON TRANSP.ASSETTEMPLATE TO appdev;

GRANT ALL ON TRANSP.ASSETTEMPLATE_ATTRIBUTE_SEQ TO fdtrn_stprd01;
GRANT READ ON TRANSP.ASSETTEMPLATE_ATTRIBUTE_SEQ TO appdev;

GRANT ALL ON TRANSP.ASSETTEMPLATE_ATTRIBUTE TO fdtrn_stprd01;
GRANT READ ON TRANSP.ASSETTEMPLATE_ATTRIBUTE TO appdev;




