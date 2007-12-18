package com.freshdirect.fdstore.promotion;

import com.freshdirect.common.pricing.Discount;
import com.freshdirect.common.pricing.EnumDiscountType;

/**
 * Header-level percent off order subtotal.
 */
public class PercentOffApplicator implements PromotionApplicatorI {

	private final double minSubtotal;
	private final double percentOff;

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
		if (context.getPreDeductionTotal() < this.getMinSubtotal()) {
			return false;
		}

		double amount = context.getPreDeductionTotal() * this.percentOff;
		return context.applyHeaderDiscount(promoCode, amount, EnumPromotionType.REDEMPTION);
	}

}
