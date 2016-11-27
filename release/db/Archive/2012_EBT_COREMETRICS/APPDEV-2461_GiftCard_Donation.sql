-- CUST schema
ALTER TABLE CUST.GIFT_CARD_RECIPIENT add (DONOR_ORGNAME     VARCHAR2(55 BYTE), GIFTCARD_TYPE   VARCHAR2(3 BYTE));
ALTER TABLE CUST.SAVED_RECIPIENT add (DONOR_ORGNAME     VARCHAR2(55 BYTE), GIFTCARD_TYPE   VARCHAR2(3 BYTE));

-- CMS schema
INSERT INTO cms.contenttype (ID, NAME, DESCRIPTION, GENERATE_ID) VALUES ('DonationOrganization', 'DonationOrganization', 'Definition of type DonationOrganization', 'F');

INSERT INTO cms.attributedefinition (NAME, ID, CONTENTTYPE_ID, ATTRIBUTETYPE_CODE, INHERITABLE, REQUIRED, LABEL, CARDINALITY_CODE, LOOKUP_CODE) VALUES ('ORGANIZATION_NAME', 'DonationOrganization.ORGANIZATION_NAME', 'DonationOrganization', 'S', 'F', 'T', 'Organization Name', 'One', NULL);
INSERT INTO cms.attributedefinition (NAME, ID, CONTENTTYPE_ID, ATTRIBUTETYPE_CODE, INHERITABLE, REQUIRED, LABEL, CARDINALITY_CODE, LOOKUP_CODE) VALUES ('EMAIL', 'DonationOrganization.EMAIL', 'DonationOrganization', 'S', 'F', 'T', 'Recipient Email', 'One', NULL);
INSERT INTO cms.attributedefinition (NAME, ID, CONTENTTYPE_ID, ATTRIBUTETYPE_CODE, INHERITABLE, REQUIRED, LABEL, CARDINALITY_CODE, LOOKUP_CODE) VALUES ('CONTACT_INFO', 'DonationOrganization.CONTACT_INFO', 'DonationOrganization', 'S', 'F', 'F', 'Contact Info', 'One', NULL);

INSERT INTO cms.relationshipdefinition (NAME, ID, CONTENTTYPE_ID, INHERITABLE, REQUIRED, NAVIGABLE, READONLY, LABEL, CARDINALITY_CODE) VALUES ('ORGANIZATION_LOGO', 'DonationOrganization.ORGANIZATION_LOGO', 'DonationOrganization', 'F', 'T', 'F', 'F', 'Organization logo', 'One');
INSERT INTO cms.relationshipdefinition (NAME, ID, CONTENTTYPE_ID, INHERITABLE, REQUIRED, NAVIGABLE, READONLY, LABEL, CARDINALITY_CODE) VALUES ('ORGANIZATION_LOGO_SMALL', 'DonationOrganization.ORGANIZATION_LOGO_SMALL', 'DonationOrganization', 'F', 'F', 'F', 'F', 'Organization logo Small', 'One');
INSERT INTO cms.relationshipdefinition (NAME, ID, CONTENTTYPE_ID, INHERITABLE, REQUIRED, NAVIGABLE, READONLY, LABEL, CARDINALITY_CODE) VALUES ('GIFTCARD_TYPE', 'DonationOrganization.GIFTCARD_TYPE', 'DonationOrganization', 'F', 'T', 'F', 'F', 'Gift Card Type', 'Many');
INSERT INTO cms.relationshipdefinition (NAME, ID, CONTENTTYPE_ID, INHERITABLE, REQUIRED, NAVIGABLE, READONLY, LABEL, CARDINALITY_CODE) VALUES ('EDITORIAL_MAIN', 'DonationOrganization.EDITORIAL_MAIN', 'DonationOrganization', 'F', 'F', 'F', 'F', 'Editorial for Main Content', 'One');
INSERT INTO cms.relationshipdefinition (NAME, ID, CONTENTTYPE_ID, INHERITABLE, REQUIRED, NAVIGABLE, READONLY, LABEL, CARDINALITY_CODE) VALUES ('EDITORIAL_DETAIL', 'DonationOrganization.EDITORIAL_DETAIL', 'DonationOrganization', 'F', 'F', 'F', 'F', 'Editorial for Detail Content', 'One');
INSERT INTO cms.relationshipdefinition (NAME, ID, CONTENTTYPE_ID, INHERITABLE, REQUIRED, NAVIGABLE, READONLY, LABEL, CARDINALITY_CODE) VALUES ('EDITORIAL_HEADER_MEDIA', 'DonationOrganization.EDITORIAL_HEADER_MEDIA', 'DonationOrganization', 'F', 'F', 'F', 'F', 'Editorial for Detail Content', 'One');

INSERT INTO cms.relationshipdestination (ID,  RELATIONSHIPDEFINITION_ID, CONTENTTYPE_ID, REVERSE_ATTRIBUTE_NAME, REVERSE_ATTRIBUTE_LABEL) VALUES ('DonationOrganization.ORGANIZATION_LOGO.Image', 'DonationOrganization.ORGANIZATION_LOGO', 'Image', NULL, NULL);
INSERT INTO cms.relationshipdestination (ID,  RELATIONSHIPDEFINITION_ID, CONTENTTYPE_ID, REVERSE_ATTRIBUTE_NAME, REVERSE_ATTRIBUTE_LABEL) VALUES ('DonationOrganization.ORGANIZATION_LOGO_SMALL.Image', 'DonationOrganization.ORGANIZATION_LOGO_SMALL', 'Image', NULL, NULL);
INSERT INTO cms.relationshipdestination (ID,  RELATIONSHIPDEFINITION_ID, CONTENTTYPE_ID, REVERSE_ATTRIBUTE_NAME, REVERSE_ATTRIBUTE_LABEL) VALUES ('DonationOrganization.GIFTCARD_TYPE.DomainValue', 'DonationOrganization.GIFTCARD_TYPE', 'DomainValue', NULL, NULL);
INSERT INTO cms.relationshipdestination (ID,  RELATIONSHIPDEFINITION_ID, CONTENTTYPE_ID, REVERSE_ATTRIBUTE_NAME, REVERSE_ATTRIBUTE_LABEL) VALUES ('DonationOrganization.EDITORIAL_MAIN.Html', 'DonationOrganization.EDITORIAL_MAIN', 'Html', NULL, NULL);
INSERT INTO cms.relationshipdestination (ID,  RELATIONSHIPDEFINITION_ID, CONTENTTYPE_ID, REVERSE_ATTRIBUTE_NAME, REVERSE_ATTRIBUTE_LABEL) VALUES ('DonationOrganization.EDITORIAL_DETAIL.Html', 'DonationOrganization.EDITORIAL_DETAIL', 'Html', NULL, NULL);
INSERT INTO cms.relationshipdestination (ID,  RELATIONSHIPDEFINITION_ID, CONTENTTYPE_ID, REVERSE_ATTRIBUTE_NAME, REVERSE_ATTRIBUTE_LABEL) VALUES ('DonationOrganization.EDITORIAL_HEADER_MEDIA.Html', 'DonationOrganization.EDITORIAL_HEADER_MEDIA', 'Html', NULL, NULL);

Insert into CMS.RELATIONSHIPDESTINATION
   (RELATIONSHIPDEFINITION_ID, CONTENTTYPE_ID, ID)
 Values
   ('FDFolder.children', 'DonationOrganization', 'FDFolder.children.DonationOrganization');

   
Insert into cms.contentnode (id,contenttype_id) values('FDFolder:donationOrganizationList', 'FDFolder');
 
Insert into CMS.ATTRIBUTE
   (CONTENTNODE_ID, ID, VALUE, ORDINAL, DEF_NAME, DEF_CONTENTTYPE)
 Values
   ('FDFolder:donationOrganizationList', cms.system_seq.nextval, 'Donation Organizations', 0, 'name', 'FDFolder');
   

 