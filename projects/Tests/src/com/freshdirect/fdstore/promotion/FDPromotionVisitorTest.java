package com.freshdirect.fdstore.promotion;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import junit.framework.TestCase;

import com.freshdirect.fdstore.content.ProductRef;
import com.freshdirect.framework.core.PrimaryKey;

public class FDPromotionVisitorTest extends TestCase {

	private Map promotions;

	protected void setUp() throws Exception {

		promotions = new HashMap();

		Promotion p;
		Date d  = new Date();
		p = new Promotion(new PrimaryKey("s1"), EnumPromotionType.SAMPLE, "s1", "s1", "s1",new Timestamp(d.getTime()));
		p.setApplicator(new SampleLineApplicator(new ProductRef("cat", "prod1"), 40));
		promotions.put(p.getPromotionCode(), p);

		p = new Promotion(new PrimaryKey("s2"), EnumPromotionType.SAMPLE, "s2", "s2", "s2",new Timestamp(d.getTime()));
		p.setApplicator(new SampleLineApplicator(new ProductRef("cat", "prod2"), 40));
		promotions.put(p.getPromotionCode(), p);

		p = new Promotion(new PrimaryKey("gc1"), EnumPromotionType.GIFT_CARD, "gc1", "gc1", "gc1",new Timestamp(d.getTime()));
		p.addStrategy(new RedemptionCodeStrategy("GC1"));
		p.setApplicator(new HeaderDiscountApplicator(new HeaderDiscountRule(40, 10)));
		promotions.put(p.getPromotionCode(), p);

		p = new Promotion(new PrimaryKey("gc2"), EnumPromotionType.GIFT_CARD, "gc2", "gc2", "gc2",new Timestamp(d.getTime()));
		p.addStrategy(new RedemptionCodeStrategy("GC2"));
		p.setApplicator(new HeaderDiscountApplicator(new HeaderDiscountRule(40, 20)));
		promotions.put(p.getPromotionCode(), p);

		p = new Promotion(new PrimaryKey("su1"), EnumPromotionType.SIGNUP, "su1", "su1", "su1",new Timestamp(d.getTime()));
		p.setApplicator(new SignupDiscountApplicator(new SignupDiscountRule[] {new SignupDiscountRule(40, 50, 15)}));
		promotions.put(p.getPromotionCode(), p);

		p = new Promotion(new PrimaryKey("su2"), EnumPromotionType.SIGNUP, "su2", "su2", "su2",new Timestamp(d.getTime()));
		p.setApplicator(new SignupDiscountApplicator(new SignupDiscountRule[] {new SignupDiscountRule(40, 30, 15)}));
		promotions.put(p.getPromotionCode(), p);

		p = new Promotion(new PrimaryKey("rc1"), EnumPromotionType.REDEMPTION, "rc1", "rc1", "rc1",new Timestamp(d.getTime()));
		p.setApplicator(new PercentOffApplicator(40, 0.1));
		promotions.put(p.getPromotionCode(), p);

	}

	private List getPromotions(String[] codes) {
		List l = new ArrayList(codes.length);
		for (int i = 0; i < codes.length; i++) {
			Promotion p = (Promotion) promotions.get(codes[i]);
			if (p == null)
				throw new NullPointerException(codes[i]);
			l.add(p);
		}
		return l;
	}

	private void assertResolution(String comment, boolean allowMultipleHeader, String[] codes, String[] expectedCodes) {
		List promos = getPromotions(codes);
		List result = FDPromotionVisitor.resolveConflicts(allowMultipleHeader, promos);
		Set resultSet = new HashSet(result.size());
		for (Iterator i = result.iterator(); i.hasNext();) {
			Promotion p = (Promotion) i.next();
			resultSet.add(p.getPromotionCode());
		}
		Set expectedSet = new HashSet(Arrays.asList(expectedCodes));
		assertEquals(comment, expectedSet, resultSet);
	}

	public void testConflictResoltuion() {
		assertResolution("nothing", false, new String[] {}, new String[] {});

		assertResolution("sample item", false, new String[] {"s1"}, new String[] {"s1"});

		assertResolution("two samples", false, new String[] {"s1", "s2"}, new String[] {"s1", "s2"});

		assertResolution("two samples and a gift card", false, new String[] {"s1", "s2", "gc1"}, new String[] {"s1", "s2", "gc1"});

		assertResolution("two signup variants always conflict", false, new String[] {"su1", "su2"}, new String[] {"su1"});
		assertResolution("two signup variants always conflict", true, new String[] {"su1", "su2"}, new String[] {"su1"});

		assertResolution(
			"two samples and two gift cards with single header",
			false,
			new String[] {"s1", "s2", "gc1", "gc2"},
			new String[] {"s1", "s2", "gc1"});

		assertResolution(
			"two samples and two gift cards with multiple header",
			true,
			new String[] {"s1", "s2", "gc1", "gc2"},
			new String[] {"s1", "s2", "gc1", "gc2"});
	}
}
