package com.freshdirect.fdstore.promotion;

import java.util.Iterator;

import com.freshdirect.fdstore.promotion.management.FDPromoDollarDiscount;





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
		double subTotal = context.getSubTotal(promo.getExcludeSkusFromSubTotal());
		
		/*APPDEV-1792 - apply the streatchable dollar discount*/
		if(this.discountRule.getDollarList().size() > 0) {
			//check which discount is applicable		
			System.out.println("============Subtotal:" + subTotal);
			double tempTotal = 0;
			double tempDiscount = 0;
			for (Iterator<FDPromoDollarDiscount> i = this.discountRule.getDollarList().iterator(); i.hasNext();) {
				FDPromoDollarDiscount fdpdd = (FDPromoDollarDiscount) i.next();
				if(fdpdd.getOrderSubtotal() < subTotal) {
					if(tempTotal < fdpdd.getOrderSubtotal()) {
						tempTotal = fdpdd.getOrderSubtotal();
						tempDiscount = fdpdd.getDollarOff();
						System.out.println("=========tempTotal:" + tempTotal + "=====tempDiscount:" + tempDiscount);
					}
				}
			}
			if(tempDiscount != 0) {
				System.out.println("=========applying a tempDiscount:" + tempDiscount);
				return context.applyHeaderDiscount(promo, tempDiscount);
			}
		} 		
				
		if (subTotal < this.discountRule.getMinSubtotal()) {
			return false;
		}
       
		double amount = Math.min(context.getShoppingCart().getPreDeductionTotal(), this.discountRule.getMaxAmount());
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
