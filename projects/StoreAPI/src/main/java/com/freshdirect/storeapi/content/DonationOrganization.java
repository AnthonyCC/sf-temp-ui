package com.freshdirect.storeapi.content;

import java.util.ArrayList;
import java.util.List;

import com.freshdirect.cms.core.domain.ContentKey;
import com.freshdirect.storeapi.attributes.FDAttributeFactory;

/**
 * @author kkanuganti
 *
 */
public class DonationOrganization extends ContentNodeModelImpl {

	private static final long serialVersionUID = 1L;

	private final List giftcardTypes = new ArrayList();

    public DonationOrganization(ContentKey key) {
        super(key);
    }

	public String getOrganizationName(){
		return getAttribute("ORGANIZATION_NAME", "");
	}

	public String getEmail(){
		return getAttribute("EMAIL", "");
	}

	public String getContactInfo() {
		return getAttribute("CONTACT_INFO", "");
	}

	public List getGiftcardType() {
		ContentNodeModelUtil.refreshModels(this, "GIFTCARD_TYPE", giftcardTypes, false);
		return new ArrayList(giftcardTypes);
	}

	public Image getOrganizationLogo() {
        return FDAttributeFactory.constructImage(this, "ORGANIZATION_LOGO");
	}

	public Image getLogoSmall() {
        return FDAttributeFactory.constructImage(this, "ORGANIZATION_LOGO_SMALL");
	}

	public Image getLogoSmallEx() {
        return FDAttributeFactory.constructImage(this, "ORGANIZATION_RECIEPT_LOGO");
	}

	public Html getEditorialMain() {
		return FDAttributeFactory.constructHtml(this, "EDITORIAL_MAIN");
	}

	public Html getEditorialDetail(){
		return FDAttributeFactory.constructHtml(this, "EDITORIAL_DETAIL");
	}

	public Html getEditorialHeaderMedia(){
		return FDAttributeFactory.constructHtml(this, "EDITORIAL_HEADER_MEDIA");
	}

	public Html getReceiptEditorialMedia(){
		return FDAttributeFactory.constructHtml(this, "EDITORIAL_RECEIPT_MEDIA");
	}

}
