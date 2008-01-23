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

public class DeliveryAddressValidatorTest extends TestCase {

	protected void setUp() throws Exception {
		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	private DlvServiceSelectionResult createServiceResult(boolean homeDeliverable, boolean corpDeliverable, boolean pickupDeliverable) {
		DlvServiceSelectionResult r = new DlvServiceSelectionResult();
		r.addServiceStatus(EnumServiceType.HOME, homeDeliverable ? EnumDeliveryStatus.DELIVER : EnumDeliveryStatus.DONOT_DELIVER);
		r.addServiceStatus(EnumServiceType.CORPORATE, corpDeliverable ? EnumDeliveryStatus.DELIVER : EnumDeliveryStatus.DONOT_DELIVER);
		r.addServiceStatus(EnumServiceType.PICKUP, pickupDeliverable ? EnumDeliveryStatus.DELIVER : EnumDeliveryStatus.DONOT_DELIVER);
		return r;
	}
	
	private void assertAddressValidation(String scenario,
			boolean expectedValid, AddressModel address,
			DlvServiceSelectionResult serviceResult) throws FDResourceException {

		ActionResult r = new ActionResult();
		
		TestDeliveryAddressValidator v = new TestDeliveryAddressValidator(address);
		v.setTestSelectionResult(serviceResult);
		v.setTestGeocodeResponse(new DlvAddressGeocodeResponse(address, "result1"));

		assertEquals(scenario, expectedValid, v.validateAddress(r));
	}
	
	public void testAddressValidations() throws FDResourceException {
		
		assertAddressValidation("Residence home/corp zone as Residential",
				true, createAddress(false, EnumServiceType.HOME),
				createServiceResult(true, true, true));
		assertAddressValidation("Residence in home/corp zone as Corporate",
				true, createAddress(false, EnumServiceType.CORPORATE),
				createServiceResult(true, true, true));
		
		assertAddressValidation("Firm in home only zone as Residental", false,
				createAddress(true, EnumServiceType.HOME), createServiceResult(
						true, false, true));
		assertAddressValidation("Firm in home only zone as Corporate", false,
				createAddress(true, EnumServiceType.CORPORATE),
				createServiceResult(true, false, true));

		assertAddressValidation("Firm in home/corp zone as Residental", false,
				createAddress(true, EnumServiceType.HOME), createServiceResult(
						true, true, true));
		assertAddressValidation("Firm in home/corp zone as Corporate", true,
				createAddress(true, EnumServiceType.CORPORATE),
				createServiceResult(true, true, true));

		assertAddressValidation("Residence pickup-only zone as Residential",
				true, createAddress(false, EnumServiceType.HOME),
				createServiceResult(false, false, true));
		assertAddressValidation("Residence pickup-only zone as Corporate",
				false, createAddress(false, EnumServiceType.CORPORATE),
				createServiceResult(false, false, true));

	}

	private AddressModel createAddress(boolean firm, EnumServiceType serviceType) {
		AddressInfo ai = new AddressInfo();
		ai.setAddressType(firm ? EnumAddressType.FIRM : EnumAddressType.STREET);
		AddressModel address = new AddressModel(
				"BOGUS ST", // address
				"",			// apartment
				"Bogocity",	// city
				"NY",		// state
				"12345"		// zip
		);
		address.setServiceType(serviceType);
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
