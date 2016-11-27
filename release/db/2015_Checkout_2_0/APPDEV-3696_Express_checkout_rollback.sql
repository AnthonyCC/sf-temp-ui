delete from cms.relationshipdestination where ID like 'Store.expressCheckoutReceiptHeader%';
delete from cms.relationshipdefinition where ID = 'Store.expressCheckoutReceiptHeader';

delete from cms.relationshipdestination where ID like 'Store.expressCheckoutReceiptEditorial%';
delete from cms.relationshipdefinition where ID = 'Store.expressCheckoutReceiptEditorial%';

delete from cms.relationshipdestination where ID like 'Store.expressCheckoutTextMessageAlertHeader%';
delete from cms.relationshipdefinition where ID = 'Store.expressCheckoutTextMessageAlertHeader%';

ALTER TABLE cust.paymentmethod drop column BEST_NUM_BILLING_INQ;
ALTER TABLE cust.paymentinfo drop column BEST_NUM_BILLING_INQ;

ALTER TABLE cust.address drop column PHONE_TYPE;
ALTER TABLE cust.address drop column ALT_CONTACT_TYPE;
ALTER TABLE cust.deliveryinfo drop column PHONE_TYPE;
ALTER TABLE cust.deliveryinfo drop column ALT_CONTACT_TYPE;
