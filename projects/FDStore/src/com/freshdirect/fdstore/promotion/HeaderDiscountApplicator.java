package com.freshdirect.fdstore.promotion;


public class HeaderDiscountApplicator implements PromotionApplicatorI {

	private final HeaderDiscountRule discountRule;

	/**
	 * minSubTotal > amount
	 */
	public HeaderDiscountApplicator(HeaderDiscountRule discountRule) {
		this.discountRule = discountRule;
	}

	public boolean apply(String promoCode, PromotionContextI context) {
		if (context.getPreDeductionTotal() < this.discountRule.getMinSubtotal()) {
			return false;
		}

		double amount = Math.min(context.getPreDeductionTotal(), this.discountRule.getMaxAmount());
		return context.applyHeaderDiscount(promoCode, amount, EnumPromotionType.REDEMPTION);
	}

	public HeaderDiscountRule getDiscountRule() {
		return this.discountRule;
	}

	public String toString() {
		return "HeaderDiscountApplicator[" + this.discountRule + "]";
	}

}
