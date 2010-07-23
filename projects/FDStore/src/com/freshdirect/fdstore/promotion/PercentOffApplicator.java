package com.freshdirect.fdstore.promotion;

import com.freshdirect.common.pricing.Discount;
import com.freshdirect.common.pricing.EnumDiscountType;

/**
 * Header-level percent off order subtotal.
 */
public class PercentOffApplicator implements PromotionApplicatorI {

	private final double minSubtotal;
	private final double percentOff;
	private DlvZoneStrategy zoneStrategy;
	
	/**
	 * @param percentOff between 0 and 1
	 */
	public PercentOffApplicator(double minSubtotal, double percentOff) {
		if (percentOff < 0 || percentOff > 1) {
			throw new IllegalArgumentException("Expected value between 0 and 100");
		}
		this.minSubtotal = minSubtotal;
		this.percentOff = percentOff;
	}

	public double getMinSubtotal() {
		return minSubtotal;
	}

	public double getPercentOff() {
		return percentOff;
	}

	public boolean apply(String promoCode, PromotionContextI context) {   
		//If delivery zone strategy is applicable please evaluate before applying the promotion.
		int e = zoneStrategy != null ? zoneStrategy.evaluate(promoCode, context) : PromotionStrategyI.ALLOW;
		if(e == PromotionStrategyI.DENY) return false;
			
		PromotionI promo = PromotionFactory.getInstance().getPromotion(promoCode);
		double preDeduction = context.getSubTotal(promo.getExcludeSkusFromSubTotal());
		if (preDeduction < this.getMinSubtotal()) {
			return false;
		}
		double amount = preDeduction * this.percentOff;
		return context.applyHeaderDiscount(promo, amount);
	}
	
	public void setZoneStrategy(DlvZoneStrategy zoneStrategy) {
		this.zoneStrategy = zoneStrategy;
	}

	public DlvZoneStrategy getDlvZoneStrategy() {
		return this.zoneStrategy;
	}

}
