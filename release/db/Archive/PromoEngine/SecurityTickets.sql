CREATE TABLE securityticket
  (
    key1    VARCHAR(45) PRIMARY KEY,
    owner   VARCHAR(80) NOT NULL,
    purpose VARCHAR(80) NOT NULL,
    expiration DATE NOT NULL,
    used DATE,
    created DATE NOT NULL
  );

GRANT
SELECT,
INSERT,
DELETE,
UPDATE ON CUST.securityticket TO FDSTORE_PRDA;

GRANT
SELECT,
INSERT,
DELETE,
UPDATE ON CUST.securityticket TO FDSTORE_PRDB;

GRANT
SELECT,
INSERT,
DELETE,
UPDATE ON CUST.securityticket TO appdev;

-- rollback
-- DROP TABLE securityticket;

