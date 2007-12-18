
ALTER TABLE FDCARTLINE ADD (
 RECIPE_SOURCE_ID VARCHAR2(128),
 REQUEST_NOTIFICATION VARCHAR2(1)
);

ALTER TABLE ORDERLINE ADD (
 RECIPE_SOURCE_ID VARCHAR2(128),
 REQUEST_NOTIFICATION VARCHAR2(1)
);

CREATE TABLE CUSTOMERLIST_RECIPES (
    ID             VARCHAR2 (16)    NOT NULL,
    LIST_ID        VARCHAR2 (16)    NOT NULL,
    RECIPE_ID      VARCHAR2 (128)    NOT NULL,
    FREQUENCY      INTEGER,
    CREATE_DATE    DATE             NOT NULL,
    RECENT_DATE    DATE             NOT NULL,
    DELETE_DATE    DATE,
    CONSTRAINT PK_CUSTOMER_LIST_RECIPES PRIMARY KEY ( ID )
);

grant select,insert,update, delete on CUSTOMERLIST_RECIPES to fdstore_prda, fdstore_prdb;


