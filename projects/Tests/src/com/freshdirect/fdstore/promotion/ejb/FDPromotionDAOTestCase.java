package com.freshdirect.fdstore.promotion.ejb;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import com.freshdirect.DbTestCaseSupport;
import com.freshdirect.fdstore.content.ProductReference;
import com.freshdirect.fdstore.promotion.ActiveInactiveStrategy;
import com.freshdirect.fdstore.promotion.CustomerStrategy;
import com.freshdirect.fdstore.promotion.DateRangeStrategy;
import com.freshdirect.fdstore.promotion.EnumPromotionType;
import com.freshdirect.fdstore.promotion.FraudStrategy;
import com.freshdirect.fdstore.promotion.GeographyStrategy;
import com.freshdirect.fdstore.promotion.LimitedUseStrategy;
import com.freshdirect.fdstore.promotion.OrderTypeStrategy;
import com.freshdirect.fdstore.promotion.Promotion;
import com.freshdirect.fdstore.promotion.PromotionGeography;
import com.freshdirect.fdstore.promotion.SampleLineApplicator;
import com.freshdirect.fdstore.promotion.SampleStrategy;
import com.freshdirect.framework.core.PrimaryKey;

public class FDPromotionDAOTestCase extends DbTestCaseSupport {

	private final static DateFormat DF = new SimpleDateFormat("yyyy-MM-dd");

	public FDPromotionDAOTestCase(String name) {
		super(name);
	}

	protected String getSchema() {
		return "CUST";
	}

	protected String[] getAffectedTables() {
		return new String[] {
			"CUST.CAMPAIGN",
			"CUST.PROMOTION_NEW",
			"CUST.PROMO_GEOGRAPHY_NEW",
			"CUST.PROMO_GEOGRAPHY_DATA_NEW",
		};
	}

	public void testLoadAllAutomaticPromotions() throws Exception {
		// setup
		this.setUpDataSet("FDPromotionDAO-init.xml");

		// execute
		List l = FDPromotionNewDAO.loadAllAutomaticPromotions(conn);

		Collections.sort(l, new Comparator() {
			public int compare(Object o1, Object o2) {
				return ((Promotion)o1).getPK().getId().compareTo(((Promotion)o2).getPK().getId());
			}
		});
		
		// verify
		assertEquals(3, l.size());

		// p1
		Promotion p1 = (Promotion) l.get(0);
		assertEquals(new PrimaryKey("p1"), p1.getPK());
		assertEquals("P_ONE", p1.getPromotionCode());
		assertEquals("Header Promotion One", p1.getDescription());
		assertEquals(EnumPromotionType.HEADER, p1.getPromotionType());
		assertNull(p1.getApplicator());

		assertEquals(7, p1.getStrategies().size());
		assertNotNull(p1.getStrategy(DateRangeStrategy.class));
		assertNotNull(p1.getStrategy(LimitedUseStrategy.class));
		assertNotNull(p1.getStrategy(GeographyStrategy.class));
		assertNotNull(p1.getStrategy(FraudStrategy.class));
		assertNotNull(p1.getStrategy(SampleStrategy.class));

		// p2
		Promotion p2 = (Promotion) l.get(1);
		assertEquals(new PrimaryKey("p2"), p2.getPK());
		assertEquals("P_TWO", p2.getPromotionCode());
		assertEquals("Sample Product Promo 1", p2.getDescription());
		assertEquals(EnumPromotionType.SAMPLE, p2.getPromotionType());
		SampleLineApplicator papp2 = (SampleLineApplicator) p2.getApplicator();
		assertEquals(new ProductReference("foo1", "bar1"), papp2.getProductReference());
		assertEquals(30.0, papp2.getMinSubtotal(), 0.001);

		assertEquals(6, p2.getStrategies().size());
		assertNotNull(p2.getStrategy(DateRangeStrategy.class));
		assertNotNull(p2.getStrategy(LimitedUseStrategy.class));
		assertNotNull(p2.getStrategy(SampleStrategy.class));
		assertNotNull(p2.getStrategy(FraudStrategy.class));

		// p3
		Promotion p3 = (Promotion) l.get(2);
		assertEquals(new PrimaryKey("p3"), p3.getPK());
		assertEquals("P_THREE", p3.getPromotionCode());
		assertEquals("Line Item Promo", p3.getDescription());
		assertEquals(EnumPromotionType.LINE_ITEM, p3.getPromotionType());
		assertNotNull(p3.getApplicator());

		assertEquals(6, p3.getStrategies().size());
		assertNotNull(p3.getStrategy(DateRangeStrategy.class));
		assertNotNull(p3.getStrategy(LimitedUseStrategy.class));
		assertNotNull(p3.getStrategy(FraudStrategy.class));
		assertNotNull(p2.getStrategy(SampleStrategy.class));

	}

	public void testLoadGeographyStrategies() throws Exception {
		// setup
		this.setUpDataSet("FDPromotionDAO-init.xml");

		// execute
		Map m = FDPromotionNewDAO.loadGeographyStrategies(conn, null);

		// verify
		assertEquals(1, m.size());

		GeographyStrategy p1strat = (GeographyStrategy) m.get(new PrimaryKey("p1"));

		List p1geos = p1strat.getGeographies();
		assertEquals(2, p1geos.size());

		PromotionGeography p1g1 = (PromotionGeography) p1geos.get(0);
		assertEquals(new PrimaryKey("p1g1"), p1g1.getPK());
		assertEquals(DF.parse("2010-06-11"), p1g1.getStartDate());

		PromotionGeography p1g2 = (PromotionGeography) p1geos.get(1);
		assertEquals(new PrimaryKey("p1g2"), p1g2.getPK());
		assertEquals(DF.parse("2010-06-11"), p1g2.getStartDate());

		assertTrue(p1g1.isAllowAllZipCodes());
		assertEquals(2, p1g1.getExcludedZipCodes().size());
		assertTrue(p1g1.getExcludedZipCodes().contains("10001"));
		assertTrue(p1g1.getExcludedZipCodes().contains("10002"));

		assertFalse(p1g1.isAllowAllDepotCodes());
		assertEquals(1, p1g1.getExcludedDepotCodes().size());
		assertTrue(p1g1.getExcludedDepotCodes().contains("depot1"));

		assertFalse(p1g2.isAllowAllZipCodes());
		assertFalse(p1g2.isAllowAllDepotCodes());
	}

}
