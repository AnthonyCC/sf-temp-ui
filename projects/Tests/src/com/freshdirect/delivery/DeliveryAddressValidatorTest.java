package com.freshdirect.delivery;

import junit.framework.TestCase;

import com.freshdirect.common.address.AddressInfo;
import com.freshdirect.common.address.AddressModel;
import com.freshdirect.common.address.EnumAddressType;
import com.freshdirect.common.customer.EnumServiceType;
import com.freshdirect.fdstore.FDInvalidAddressException;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.framework.webapp.ActionResult;
import com.freshdirect.webapp.taglib.fdstore.DeliveryAddressValidator;
import com.freshdirect.webapp.taglib.fdstore.EnumUserInfoName;

public class DeliveryAddressValidatorTest extends TestCase {

	protected void setUp() throws Exception {
		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}


	
	public void testCorpAddressAsHomeDelivery() throws FDResourceException {
		ActionResult r = new ActionResult();
		
		AddressModel address = getCommercialAddress();
		address.setServiceType(EnumServiceType.HOME);

		TestDeliveryAddressValidator v = new TestDeliveryAddressValidator(address);

		DlvServiceSelectionResult testSelRes = new DlvServiceSelectionResult();
		testSelRes.setRestrictionReason(EnumRestrictedAddressReason.COMMERCIAL);
		testSelRes.addServiceStatus(EnumServiceType.CORPORATE, EnumDeliveryStatus.DELIVER);
		testSelRes.addServiceStatus(EnumServiceType.HOME, EnumDeliveryStatus.DONOT_DELIVER);
		v.setTestSelectionResult(testSelRes);
		
		v.setTestGeocodeResponse(new DlvAddressGeocodeResponse(address, "result1"));

		
		assertFalse(v.validateAddress(r));
		assertTrue(r.hasError(EnumUserInfoName.DLV_SERVICE_TYPE.getCode()));
	}


	private AddressModel getCommercialAddress() {
		// we want a corp. address as a home delivery address
		AddressInfo ai = new AddressInfo();
		ai.setAddressType(EnumAddressType.FIRM);
		AddressModel address = new AddressModel(
				"27-00 CRESCENT ST", // address
				"",			// apartment
				"Astoria",	// city
				"NY",		// state
				"11102"		// zip
		);
		address.setAddressInfo(ai);

		return address;
	}
}






class TestDeliveryAddressValidator extends DeliveryAddressValidator {
	public DlvServiceSelectionResult testSelectionResult;
	public DlvAddressGeocodeResponse testGeocodeResponse;


	
	public DlvServiceSelectionResult getTestSelectionResult() {
		return testSelectionResult;
	}

	public void setTestSelectionResult(DlvServiceSelectionResult testSelectionResult) {
		this.testSelectionResult = testSelectionResult;
	}

	public DlvAddressGeocodeResponse getTestGeocodeResponse() {
		return testGeocodeResponse;
	}

	public void setTestGeocodeResponse(DlvAddressGeocodeResponse testGeocodeResponse) {
		this.testGeocodeResponse = testGeocodeResponse;
	}


	
	
	public TestDeliveryAddressValidator(AddressModel address) {
		super(address);
	}

	public TestDeliveryAddressValidator(AddressModel address, boolean strictGeocodeCheck) {
		super(address, strictGeocodeCheck);
	}

	

	
	// mocked external service call
	protected AddressModel doScrubAddress(AddressModel addr, ActionResult result) throws FDResourceException {
		// return AddressUtil.scrubAddress(addr, result);
		return addr;
	}

	// mocked external service call
	protected DlvServiceSelectionResult doCheckAddress(AddressModel addr) throws FDResourceException, FDInvalidAddressException {
		if (testSelectionResult == null) {
			throw new FDInvalidAddressException();
		}
		return testSelectionResult;
	}

	// mocked external service call
	protected DlvAddressGeocodeResponse doGeocodeAddress(AddressModel addr) throws FDResourceException, FDInvalidAddressException {
		if (testGeocodeResponse == null) {
			throw new FDInvalidAddressException();
		}
		 return testGeocodeResponse;		
	}
}
