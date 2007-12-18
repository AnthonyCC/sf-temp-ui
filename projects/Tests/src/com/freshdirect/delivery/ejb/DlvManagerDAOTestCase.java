package com.freshdirect.delivery.ejb;

import com.freshdirect.DbTestCaseSupport;
import com.freshdirect.common.address.AddressInfo;
import com.freshdirect.common.address.AddressModel;
import com.freshdirect.delivery.EnumRestrictedAddressReason;

public class DlvManagerDAOTestCase extends DbTestCaseSupport {

	public DlvManagerDAOTestCase(String arg0) {
		super(arg0);
	}

	protected String getSchema() {
		return "DLV";
	}

	protected String[] getAffectedTables() {
		return new String[] { "DLV.REGION",
				              "DLV.REGION_DATA",
				              "DLV.ZONE",
				              "DLV.PLANNING_RESOURCE",
				              "DLV.RESTRICTED_ADDRESS",
				              "DLV.TIMESLOT",
				              "DLV.RESERVATION"
				            };
	}

	public void testNoAlcoholAddress() throws Exception {
		// setup
		this.setUpDataSet("DlvBeerAddressInit.xml");

		// execute
		boolean isDeliverable900 = DlvManagerDAO.isAlcoholDeliverable(conn, "900 MAIN ST", "10044");
		boolean isDeliverable510 = DlvManagerDAO.isAlcoholDeliverable(conn, "510 MAIN ST", "10044");
		
		AddressModel address = new AddressModel();
		AddressInfo info = new AddressInfo();
		
		info.setScrubbedStreet("575 MAIN ST");
		address.setAddressInfo(info);
		address.setZipCode("10044");
		EnumRestrictedAddressReason notBlocked = DlvManagerDAO.isAddressRestricted(conn, address);
		
		address = new AddressModel();
		info = new AddressInfo();
		info.setScrubbedStreet("GREENWICH ST");
		address.setAddressInfo(info);
		address.setZipCode("10013");
		EnumRestrictedAddressReason commercialReason = DlvManagerDAO.isAddressRestricted(conn, address);
		
		address = new AddressModel();
		info = new AddressInfo();
		info.setScrubbedStreet("510 MAIN ST");
		address.setAddressInfo(info);
		address.setApartment("10");
		address.setZipCode("10044");
		EnumRestrictedAddressReason fraudReason = DlvManagerDAO.isAddressRestricted(conn, address);

		// verify		
		assertEquals(false, isDeliverable900);
		assertEquals(true, isDeliverable510);
		assertEquals(EnumRestrictedAddressReason.NONE, notBlocked);
		assertEquals(EnumRestrictedAddressReason.COMMERCIAL, commercialReason);
		assertEquals(EnumRestrictedAddressReason.FRAUD, fraudReason);
	}

/*
	public void testGetZoneCode() throws Exception {
		// setup
		this.setUpDataSet("DlvBeerAddressInit.xml");
		DlvAddress validAddress = new DlvAddress();

		validAddress.setAddress1("900 Main St");
		validAddress.setApartment("");
		validAddress.setCity("New York");
		validAddress.setState("NY");
		validAddress.setZipCode("10044");

		DlvAddress invalidAddress = new DlvAddress();
		invalidAddress.setAddress1("blah blah st");
		invalidAddress.setCity("Texas");
		invalidAddress.setState("NJ");
		invalidAddress.setZipCode("11418");

		// execute
		DlvZoneInfoModel zoneInfo = DlvManagerDAO.getZoneCode(conn, validAddress, new Date(), true);
		try {
			DlvManagerDAO.getZoneCode(conn, invalidAddress, new Date(), true);
			fail();
		} catch (InvalidAddressException e) {
		}

		// verify		
		assertEquals(zoneInfo.getResponse(), EnumZipCheckResponses.DELIVER);

	}
*/

}
