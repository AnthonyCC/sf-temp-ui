package com.freshdirect.fdstore;

import junit.framework.TestCase;

public class FDSalesUnitTestCase extends TestCase {

	public FDSalesUnitTestCase(String arg0) {
		super(arg0);
	}

	public void testGetDescriptionUnit() {
		FDSalesUnit u;

		u = new FDSalesUnit("A01", "6pk");
		assertEquals("pk", u.getDescriptionUnit());

		u = new FDSalesUnit("A01", "6pk (24oz)");
		assertEquals("pk", u.getDescriptionUnit());

		u = new FDSalesUnit("A01", "0.5 lb");
		assertEquals("lb", u.getDescriptionUnit());
	}

	public void testGetDescriptionQuantity() {
		FDSalesUnit u;

		u = new FDSalesUnit("A01", "6pk");
		assertEquals("6", u.getDescriptionQuantity());

		u = new FDSalesUnit("A01", "6pk (24oz)");
		assertEquals("6", u.getDescriptionQuantity());

		u = new FDSalesUnit("A01", "0.5 lb");
		assertEquals("0.5", u.getDescriptionQuantity());
	}

}
