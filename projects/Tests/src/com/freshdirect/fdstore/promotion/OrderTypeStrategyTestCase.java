package com.freshdirect.fdstore.promotion;

import java.util.HashSet;
import java.util.Set;

import junit.framework.TestCase;

public class OrderTypeStrategyTestCase extends TestCase {

	public OrderTypeStrategyTestCase(String arg0) {
		super(arg0);
	}

	public void testEvaluate() {
		Set ot = new HashSet();
		ot.add(EnumOrderType.HOME);
		ot.add(EnumOrderType.DEPOT);
		ot.add(EnumOrderType.PICKUP);
		OrderTypeStrategy orderTypeStrategy = new OrderTypeStrategy(ot);

		assertEquals(PromotionStrategyI.DENY, orderTypeStrategy.evaluate("FOO", new FakePromotionContext(EnumOrderType.CORPORATE)));
		assertEquals(PromotionStrategyI.ALLOW, orderTypeStrategy.evaluate("FOO", new FakePromotionContext(EnumOrderType.HOME)));
		assertEquals(PromotionStrategyI.ALLOW, orderTypeStrategy.evaluate("FOO", new FakePromotionContext(EnumOrderType.DEPOT)));
		assertEquals(PromotionStrategyI.ALLOW, orderTypeStrategy.evaluate("FOO", new FakePromotionContext(EnumOrderType.PICKUP)));
	}

}
