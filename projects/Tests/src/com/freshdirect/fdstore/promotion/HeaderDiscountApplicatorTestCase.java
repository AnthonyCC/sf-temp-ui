package com.freshdirect.fdstore.promotion;

import java.sql.Timestamp;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.naming.Context;

import junit.framework.TestCase;

import org.mockejb.interceptor.AspectSystem;
import org.mockejb.interceptor.InvocationContext;

import com.freshdirect.TestUtils;
import com.freshdirect.fdstore.aspects.BaseAspect;
import com.freshdirect.fdstore.customer.DebugMethodPatternPointCut;
import com.freshdirect.framework.core.PrimaryKey;

public class HeaderDiscountApplicatorTestCase extends TestCase {
	@Override
    protected void setUp() throws Exception {
        super.setUp();
        Context context = TestUtils.createContext();

        TestUtils.createMockContainer(context);
        TestUtils.createTransaction(context);
        
		AspectSystem aspectSystem = TestUtils.createAspectSystem();
		aspectSystem.add(new GetAllAutomaticPromotionsAspect());
		aspectSystem.add(new GetModifiedPromosAspect());
		aspectSystem.add(new PromoForRTAspect());
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
		assertEquals(45, ctx.getDiscounts().get(0).getAmount(), 0.001);

		ctx = new FakePromotionContext();
		ctx.setSubTotal(120);
		applicator.apply("", ctx);
		assertEquals(1, ctx.getDiscounts().size());
		assertEquals(100, ctx.getDiscounts().get(0).getAmount(), 0.001);
	}


	
	
	public static class GetAllAutomaticPromotionsAspect extends BaseAspect {
	    public GetAllAutomaticPromotionsAspect() {
	        super(new DebugMethodPatternPointCut("FDPromotionNewManager\\.getAllAutomaticPromotions\\(java.util.Date\\)"));
	    }

	    public List<PromotionI> getAllAutomaticPromotions() {
	        return Collections.<PromotionI>emptyList();
	    }
	    
	    @Override
	    public void intercept(InvocationContext arg0) throws Exception {
	        arg0.setReturnObject(getAllAutomaticPromotions());
	    }
	}

	public static class GetModifiedPromosAspect extends BaseAspect {
	    public GetModifiedPromosAspect() {
	        super(new DebugMethodPatternPointCut("FDPromotionManagerNewSB\\.getModifiedOnlyPromos\\(java.util.Date\\)"));
	    }

	    public List<PromotionI> getModifiedOnlyPromos(Date whatever) {
	        return Collections.<PromotionI>emptyList();
	    }
	    
	    @Override
	    public void intercept(InvocationContext arg0) throws Exception {
	        arg0.setReturnObject(getModifiedOnlyPromos((Date) arg0.getParamVals()[0]));
	    }
	}

	public static class PromoForRTAspect extends BaseAspect {
		public PromoForRTAspect() {
			super(new DebugMethodPatternPointCut("FDPromotionManagerNewSB\\.getPromotionForRT\\(java.lang.String\\)"));
		}

	    
	    public PromotionI getPromotionForRT(String promoCode) {
			return new Promotion(new PrimaryKey("non-existant"),
				EnumPromotionType.GIFT_CARD, promoCode, "promo name for "+promoCode, "description for "+promoCode,
				new Timestamp(System.currentTimeMillis() - 3600000));
	    }

	    @Override
	    public void intercept(InvocationContext arg0) throws Exception {
	        arg0.setReturnObject(getPromotionForRT((String) arg0.getParamVals()[0]));
	    }
	}
}
