--Create table SCRIB_LABEL
CREATE TABLE TRANSP.SCRIB_LABEL
(
  ID           VARCHAR2(10 BYTE)                NOT NULL,
  SCRIB_DATE   DATE                             NOT NULL,
  SCRIB_LABEL  VARCHAR2(20 BYTE)                NOT NULL
);

CREATE UNIQUE INDEX TRANSP.SCRIB_UNIQUE_LABEL 
ON TRANSP.SCRIB_LABEL(SCRIB_DATE, SCRIB_LABEL);

CREATE SEQUENCE TRANSP.SCRIBLABELSEQ
  START WITH 744
  MAXVALUE 9999999999
  MINVALUE 0
  NOCYCLE
  NOCACHE
  NOORDER;

GRANT ALL ON TRANSP.SCRIB_LABEL TO fdstore_ststg01;
GRANT ALL ON TRANSP.SCRIB_LABEL TO fdtrn_ststg01;
GRANT SELECT ON TRANSP.SCRIB_LABEL TO appdev;

GRANT ALL ON TRANSP.SCRIBLABELSEQ TO fdstore_ststg01;
GRANT ALL ON TRANSP.SCRIBLABELSEQ TO fdtrn_ststg01;
GRANT SELECT ON TRANSP.SCRIBLABELSEQ TO appdev;
