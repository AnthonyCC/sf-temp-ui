ALTER TABLE CUST.GIFT_CARD_RECIPIENT drop (DONOR_ORGNAME, GIFTCARD_TYPE);
ALTER TABLE CUST.SAVED_RECIPIENT drop (DONOR_ORGNAME, GIFTCARD_TYPE);

delete from CMS.ATTRIBUTE where CONTENTNODE_ID='FDFolder:donationOrganizationList';
delete from cms.contentnode id ='FDFolder:donationOrganizationList';

delete from CMS.RELATIONSHIPDESTINATION where ID ='FDFolder.children.DonationOrganization';

delete from cms.attributedefinition where ID = 'DonationOrganization.ORGANIZATION_NAME';
delete from cms.attributedefinition where ID = 'DonationOrganization.EMAIL';

delete from cms.relationshipdestination where ID = 'DonationOrganization.ORGANIZATION_LOGO.Image';
delete from cms.relationshipdestination where ID = 'DonationOrganization.ORGANIZATION_LOGO_SMALL.Image';
delete from cms.relationshipdestination where ID = 'DonationOrganization.ORGANIZATION_RECIEPT_LOGO.Image';
delete from cms.relationshipdestination where ID = 'DonationOrganization.GIFTCARD_TYPE.DomainValue';
delete from cms.relationshipdestination where ID = 'DonationOrganization.EDITORIAL_MAIN.Html';
delete from cms.relationshipdestination where ID = 'DonationOrganization.EDITORIAL_DETAIL.Html';
delete from cms.relationshipdestination where ID = 'DonationOrganization.EDITORIAL_HEADER_MEDIA.Html';
delete from cms.relationshipdestination where ID = 'DonationOrganization.EDITORIAL_RECEIPT_MEDIA.Html';

delete from cms.relationshipdefinition where ID = 'DonationOrganization.ORGANIZATION_LOGO';
delete from cms.relationshipdefinition where ID = 'DonationOrganization.ORGANIZATION_LOGO_SMALL';
delete from cms.relationshipdefinition where ID = 'DonationOrganization.GIFTCARD_TYPE';
delete from cms.relationshipdefinition where ID = 'DonationOrganization.EDITORIAL_MAIN';
delete from cms.relationshipdefinition where ID = 'DonationOrganization.EDITORIAL_DETAIL';
delete from cms.relationshipdefinition where ID = 'DonationOrganization.EDITORIAL_HEADER_MEDIA';
delete from cms.relationshipdefinition where ID = 'DonationOrganization.EDITORIAL_RECEIPT_MEDIA';

delete from cms.contenttype ID = 'DonationOrganization';