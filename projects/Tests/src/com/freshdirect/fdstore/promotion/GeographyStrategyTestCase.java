package com.freshdirect.fdstore.promotion;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import junit.framework.TestCase;

import com.freshdirect.framework.core.PrimaryKey;

public class GeographyStrategyTestCase extends TestCase {

	public GeographyStrategyTestCase(String arg0) {
		super(arg0);
	}

	private final static DateFormat DF = new SimpleDateFormat("yyyy-MM-dd");

	public void testgetGeography() throws ParseException {
		// setup
		GeographyStrategy p = new GeographyStrategy();
		p.addGeography(new PromotionGeography(new PrimaryKey("f1"), DF.parse("2003-12-20")));
		p.addGeography(new PromotionGeography(new PrimaryKey("f2"), DF.parse("2004-01-01")));
		p.addGeography(new PromotionGeography(new PrimaryKey("f3"), DF.parse("2004-03-01")));

		// verify
		assertNull(p.getGeography(DF.parse("2003-12-19")));
		assertEquals("f1", p.getGeography(DF.parse("2003-12-20")).getPK().getId());
		assertEquals("f1", p.getGeography(DF.parse("2003-12-31")).getPK().getId());
		assertEquals("f2", p.getGeography(DF.parse("2004-01-01")).getPK().getId());
		assertEquals("f2", p.getGeography(DF.parse("2004-01-02")).getPK().getId());
		assertEquals("f3", p.getGeography(DF.parse("2004-03-01")).getPK().getId());
		assertEquals("f3", p.getGeography(DF.parse("2004-06-01")).getPK().getId());

	}

}
