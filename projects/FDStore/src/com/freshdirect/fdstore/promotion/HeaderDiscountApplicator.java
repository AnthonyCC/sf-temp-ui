package com.freshdirect.fdstore.promotion;





public class HeaderDiscountApplicator implements PromotionApplicatorI {

	private final HeaderDiscountRule discountRule;
	private DlvZoneStrategy zoneStrategy;
	
	/**
	 * minSubTotal > amount
	 */
	public HeaderDiscountApplicator(HeaderDiscountRule discountRule) {
		this.discountRule = discountRule;
	}

	public boolean apply(String promoCode, PromotionContextI context) {
		//If delivery zone strategy is applicable please evaluate before applying the promotion.
		int e = zoneStrategy != null ? zoneStrategy.evaluate(promoCode, context) : PromotionStrategyI.ALLOW;
		if(e == PromotionStrategyI.DENY) return false;
		
		PromotionI promo = PromotionFactory.getInstance().getPromotion(promoCode);
		double preDeduction = context.getSubTotal(promo.getExcludeSkusFromSubTotal());
		if (preDeduction < this.discountRule.getMinSubtotal()) {
			return false;
		}
       
		double amount = Math.min(preDeduction, this.discountRule.getMaxAmount());
		if(promo.getOfferType() != null && promo.getOfferType().equals(EnumOfferType.WINDOW_STEERING)){
			return context.applyZoneDiscount(promo, amount);
		}
		return context.applyHeaderDiscount(promo, amount);
	}

	public HeaderDiscountRule getDiscountRule() {
		return this.discountRule;
	}

	public void setZoneStrategy(DlvZoneStrategy zoneStrategy) {
		this.zoneStrategy = zoneStrategy;
	}


	public DlvZoneStrategy getDlvZoneStrategy() {
		return this.zoneStrategy;
	}
	
	public String toString() {
		return "HeaderDiscountApplicator[" + this.discountRule + "]";
	}
}
