package com.freshdirect.fdstore.promotion;

import javax.naming.Context;

import junit.framework.TestCase;

import org.mockejb.MockContainer;
import org.mockejb.interceptor.AspectSystem;

import com.freshdirect.TestUtils;
import com.freshdirect.common.pricing.Discount;
import com.freshdirect.fdstore.aspects.FDPromotionManagerAspect;
import com.freshdirect.fdstore.aspects.ModifiedPromosAspect;
import com.freshdirect.fdstore.aspects.PromotionForRTAspect;

public class HeaderDiscountApplicatorTestCase extends TestCase {

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        Context context = TestUtils.createContext();
        
        MockContainer mockContainer = TestUtils.createMockContainer(context);
        TestUtils.createTransaction(context);
        
        AspectSystem aspectSystem = TestUtils.createAspectSystem();
        aspectSystem.add(new FDPromotionManagerAspect());
        aspectSystem.add(new ModifiedPromosAspect());
        aspectSystem.add(new PromotionForRTAspect());
    }
    
    
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