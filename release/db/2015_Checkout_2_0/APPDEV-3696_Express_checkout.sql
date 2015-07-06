INSERT INTO cms.relationshipdefinition (NAME,ID,CONTENTTYPE_ID,INHERITABLE,REQUIRED,NAVIGABLE,READONLY,LABEL,CARDINALITY_CODE) VALUES ('expressCheckoutReceiptHeader','Store.expressCheckoutReceiptHeader','Store','F','F','F','F','Express Checkout Receipt Header','One');
INSERT INTO cms.relationshipdestination (ID, RELATIONSHIPDEFINITION_ID,CONTENTTYPE_ID,REVERSE_ATTRIBUTE_NAME,REVERSE_ATTRIBUTE_LABEL) VALUES ('Store.expressCheckoutReceiptHeader.Html','Store.expressCheckoutReceiptHeader','Html',NULL,NULL);

INSERT INTO cms.relationshipdefinition (NAME,ID,CONTENTTYPE_ID,INHERITABLE,REQUIRED,NAVIGABLE,READONLY,LABEL,CARDINALITY_CODE) VALUES ('expressCheckoutReceiptEditorial','Store.expressCheckoutReceiptEditorial','Store','F','F','F','F','Express Checkout Receipt Editorial','One');
INSERT INTO cms.relationshipdestination (ID, RELATIONSHIPDEFINITION_ID,CONTENTTYPE_ID,REVERSE_ATTRIBUTE_NAME,REVERSE_ATTRIBUTE_LABEL) VALUES ('Store.expressCheckoutReceiptEditorial.Html','Store.expressCheckoutReceiptEditorial','Html',NULL,NULL);

INSERT INTO cms.relationshipdefinition (NAME,ID,CONTENTTYPE_ID,INHERITABLE,REQUIRED,NAVIGABLE,READONLY,LABEL,CARDINALITY_CODE) VALUES ('expressCheckoutTextMessageAlertHeader','Store.expressCheckoutTextMessageAlertHeader','Store','F','F','F','F','Express Checkout Text Message Alert Header','One');
INSERT INTO cms.relationshipdestination (ID, RELATIONSHIPDEFINITION_ID,CONTENTTYPE_ID,REVERSE_ATTRIBUTE_NAME,REVERSE_ATTRIBUTE_LABEL) VALUES ('Store.expressCheckoutTextMessageAlertHeader.Html','Store.expressCheckoutTextMessageAlertHeader','Html',NULL,NULL);

-- Update Payment with best # of billing inquiries
ALTER TABLE cust.paymentmethod ADD (BEST_NUM_BILLING_INQ VARCHAR2(15 BYTE));
ALTER TABLE cust.paymentinfo ADD (BEST_NUM_BILLING_INQ VARCHAR2(15 BYTE));

ALTER TABLE cust.address ADD (PHONE_TYPE VARCHAR2(6 BYTE));
ALTER TABLE cust.address ADD (ALT_CONTACT_TYPE VARCHAR2(6 BYTE));
ALTER TABLE cust.deliveryinfo ADD (PHONE_TYPE VARCHAR2(6 BYTE));
ALTER TABLE cust.deliveryinfo ADD (ALT_CONTACT_TYPE VARCHAR2(6 BYTE));
