package com.freshdirect.common.pricing;

import junit.framework.TestCase;

import com.freshdirect.fdstore.FDConfiguration;
import com.freshdirect.fdstore.FDConfigurableI;

public class PricingEngineTestCase extends TestCase {

	public PricingEngineTestCase(String arg0) {
		super(arg0);
	}

	private MaterialPrice[] matPrices = new MaterialPrice[0];
	private CharacteristicValuePrice[] cvPrices = new CharacteristicValuePrice[0];
	private SalesUnitRatio[] salesUnits = new SalesUnitRatio[0];

	public void testSimpleEaches() throws PricingException {
		// setup
		this.matPrices = new MaterialPrice[] { new MaterialPrice(0.99, "EA")};

		// verify
		assertPrice(0.00, pc(0, "EA"));
		assertPrice(0.99, pc(1, "EA"));
		assertPrice(9.90, pc(10, "EA"));
	}

	public void testMultipleSalesUnits() throws PricingException {
		// setup
		this.matPrices = new MaterialPrice[] { new MaterialPrice(2.00, "A01"), new MaterialPrice(3.00, "A02")};
		//this.salesUnits = new SalesUnitRatio[] { new SalesUnitRatio("EA", "LB", 0.5)};

		// verify
		assertPrice(4.00, pc(2, "A01"));
		assertPrice(6.00, pc(2, "A02"));
	}

	public void testEachesWithConversion() throws PricingException {
		// setup
		this.matPrices = new MaterialPrice[] { new MaterialPrice(1.50, "LB")};
		this.salesUnits = new SalesUnitRatio[] { new SalesUnitRatio("EA", "LB", 0.5)};

		// verify
		assertPrice(0.00, pc(0, "EA"));
		assertPrice(0.15, pc(0.2, "EA"));
		assertPrice(0.75, pc(1, "EA"));
		assertPrice(7.50, pc(10, "EA"));
	}

	public void testScaleWithConversion() throws PricingException {
		// setup
		this.matPrices =
			new MaterialPrice[] {
				new MaterialPrice(1.50, "LB", 1, 10, "EA"),
				new MaterialPrice(1.25, "LB", 10, Double.POSITIVE_INFINITY, "EA")};
		this.salesUnits = new SalesUnitRatio[] { new SalesUnitRatio("EA", "LB", 0.5)};

		// verify
		assertPrice(0.00, pc(0, "EA"));
		assertPrice(0.15, pc(0.2, "EA"));

		assertPrice(0.75, pc(1, "EA"));
		assertPrice(3.75, pc(5, "EA"));
		assertPrice(6.75, pc(9, "EA"));

		assertPrice(6.25, pc(10, "EA"));
	}

	/*
	public void testTwoStepConversion() throws PricingException {
		// setup
		this.matPrices = new MaterialPrice[] { new MaterialPrice(2, "LB")};
		this.salesUnits = new SalesUnitRatio[] { new SalesUnitRatio("EA", "LB", 0.5), new SalesUnitRatio("A01", "EA", 5)};
	
		// verify
		assertPricing(0.00, pc(0, "A01"));
		assertPricing(0.75, pc(1, "A01"));
		assertPricing(7.50, pc(10, "A01"));
	}
	*/

	private FDConfiguration pc(double quantity, String salesUnit) {
		return new FDConfiguration(quantity, salesUnit);
	}

	private void assertPrice(double expectedBasePrice, FDConfiguration conf) throws PricingException {
		assertPrice(expectedBasePrice, 0.00, conf);
	}

	private void assertPrice(double expectedBasePrice, double expectedSurcharge, FDConfigurableI conf) throws PricingException {
		Pricing pricing = new Pricing(matPrices, cvPrices, salesUnits);
		Price price = PricingEngine.getConfiguredPrice(pricing, conf).getPrice();

		// verify
		assertEquals(expectedBasePrice, price.getBasePrice(), 0.01);
		assertEquals(expectedSurcharge, price.getSurcharge(), 0.01);
	}

}
