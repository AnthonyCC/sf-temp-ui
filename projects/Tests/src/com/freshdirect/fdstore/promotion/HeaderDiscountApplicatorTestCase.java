package com.freshdirect.fdstore.promotion;

import com.freshdirect.common.pricing.Discount;

import junit.framework.TestCase;

public class HeaderDiscountApplicatorTestCase extends TestCase {

	public void testApply() {
		HeaderDiscountApplicator applicator = new HeaderDiscountApplicator(new HeaderDiscountRule(40, 100));

		FakePromotionContext ctx = new FakePromotionContext();
		ctx.setSubTotal(30);
		applicator.apply("", ctx);
		assertEquals(0, ctx.getDiscounts().size());

		ctx = new FakePromotionContext();
		ctx.setSubTotal(45);
		applicator.apply("", ctx);
		assertEquals(1, ctx.getDiscounts().size());
		assertEquals(45, ((Discount) ctx.getDiscounts().get(0)).getAmount(), 0.001);

		ctx = new FakePromotionContext();
		ctx.setSubTotal(120);
		applicator.apply("", ctx);
		assertEquals(1, ctx.getDiscounts().size());
		assertEquals(100, ((Discount) ctx.getDiscounts().get(0)).getAmount(), 0.001);
	}

}