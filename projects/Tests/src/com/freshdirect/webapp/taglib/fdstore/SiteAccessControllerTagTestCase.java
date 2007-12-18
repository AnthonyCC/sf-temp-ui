package com.freshdirect.webapp.taglib.fdstore;

import com.freshdirect.common.address.AddressInfo;
import com.freshdirect.common.address.AddressModel;
import com.freshdirect.common.address.EnumAddressType;
import com.freshdirect.common.customer.EnumServiceType;

import junit.framework.TestCase;

public class SiteAccessControllerTagTestCase extends TestCase {

	private final static String SUCCESS = "success.jsp";
	private final static String ALT_DLV_HOME = "altDeliveryHome.jsp";
	private final static String ALT_DLV_CORPORATE = "altDeliveryCommercial.jsp";
	private final static String FAIL_HOME = "failureHome.jsp";
	private final static String FAIL_CORPORATE = "failureCorporate.jsp";

	private SiteAccessControllerTag tag = new SiteAccessControllerTag();

	public void testCheckAddress() {

		AddressModel address = new AddressModel();
		address.setAddress1("900 Main ST");
		address.setCity("New York");
		address.setState("NY");
		address.setZipCode("10044");
		AddressInfo info = new AddressInfo();
		info.setAddressType(EnumAddressType.FIRM);
		info.setScrubbedStreet("900 MAIN ST");
		address.setAddressInfo(info);

		// setup
		tag.setSuccessPage(SUCCESS);
		tag.setAltDeliveryHomePage(ALT_DLV_HOME);
		tag.setAltDeliveryCorporatePage(ALT_DLV_CORPORATE);
		tag.setFailureHomePage(FAIL_HOME);
		tag.setFailureCorporatePage(FAIL_CORPORATE);

		// execute & verify
		assertRedirectPage(SUCCESS, EnumServiceType.HOME, true, true, true);
		assertRedirectPage(SUCCESS, EnumServiceType.HOME, true, true, false);
		assertRedirectPage(ALT_DLV_CORPORATE, EnumServiceType.HOME, true, false, true);
		assertRedirectPage(FAIL_HOME, EnumServiceType.HOME, true, false, false);
		assertRedirectPage(ALT_DLV_CORPORATE, EnumServiceType.HOME, false, true, true);
		assertRedirectPage(FAIL_HOME, EnumServiceType.HOME, false, true, false);
		assertRedirectPage(ALT_DLV_CORPORATE, EnumServiceType.HOME, false, false, true);
		assertRedirectPage(FAIL_HOME, EnumServiceType.HOME, false, false, false);

		assertRedirectPage(SUCCESS, EnumServiceType.CORPORATE, true, true, true);
		assertRedirectPage(SUCCESS, EnumServiceType.CORPORATE, true, true, false);
		assertRedirectPage(ALT_DLV_HOME, EnumServiceType.CORPORATE, true, false, true);
		assertRedirectPage(FAIL_CORPORATE, EnumServiceType.CORPORATE, true, false, false);
		assertRedirectPage(ALT_DLV_HOME, EnumServiceType.CORPORATE, false, true, true);
		assertRedirectPage(FAIL_CORPORATE, EnumServiceType.CORPORATE, false, true, false);
		assertRedirectPage(ALT_DLV_HOME, EnumServiceType.CORPORATE, false, false, true);
		assertRedirectPage(FAIL_CORPORATE, EnumServiceType.CORPORATE, false, false, false);

	}

	private void assertRedirectPage(
		String page,
		EnumServiceType reqServiceType,
		boolean validServiceType,
		boolean reqDeliverable,
		boolean altDeliverable) {

		assertEquals(page, tag.getRedirectPage(reqServiceType, tag.getServiceAvailability(validServiceType, reqDeliverable,
			altDeliverable)));
	}
}