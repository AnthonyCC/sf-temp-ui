package com.freshdirect.fdstore.promotion;

import com.freshdirect.common.pricing.Discount;
import com.freshdirect.common.pricing.EnumDiscountType;

/**
 * Header-level percent off order subtotal.
 */
public class PercentOffApplicator implements PromotionApplicatorI {

	private double minSubtotal;
	private double percentOff;
	private DlvZoneStrategy zoneStrategy;
	private double maxPercentageDiscount;
	private CartStrategy cartStrategy;
	
	/**
	 * @param percentOff between 0 and 1
	 */
	public PercentOffApplicator(double minSubtotal, double percentOff, double maxPercentageDiscount) {
		if (percentOff < 0 || percentOff > 1) {
			throw new IllegalArgumentException("Expected value between 0 and 100");
		}
		this.minSubtotal = minSubtotal;
		this.percentOff = percentOff;
		this.maxPercentageDiscount = maxPercentageDiscount;
	}

	public PercentOffApplicator() {
		super();
		// TODO Auto-generated constructor stub
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
		
		e = cartStrategy != null ? cartStrategy.evaluate(promoCode, context, true) : PromotionStrategyI.ALLOW;
		if(e == PromotionStrategyI.DENY) return false;
			
		PromotionI promo = PromotionFactory.getInstance().getPromotion(promoCode);
		double subTotal = context.getSubTotal(promo.getExcludeSkusFromSubTotal());
		if (subTotal < this.getMinSubtotal()) {
			return false;
		}
		//[APPDEV-2407]-Change to Percent-Off Promotion Discount Logic (Header Level)
		//double amount = context.getShoppingCart().getPreDeductionTotal() * this.percentOff;
		double amount = context.getShoppingCart().getSubTotal() * this.percentOff;
		//[APPDEV-2433]-Apply max discount if available
		if(maxPercentageDiscount > 0) {
			if(amount > maxPercentageDiscount) {
				amount = maxPercentageDiscount;
			}
		}
		return context.applyHeaderDiscount(promo, amount);
	}
	
	public void setZoneStrategy(DlvZoneStrategy zoneStrategy) {
		this.zoneStrategy = zoneStrategy;
	}

	public DlvZoneStrategy getDlvZoneStrategy() {
		return this.zoneStrategy;
	}

	@Override
	public void setCartStrategy(CartStrategy cartStrategy) {
		this.cartStrategy = cartStrategy;		
	}

	@Override
	public CartStrategy getCartStrategy() {
		return this.cartStrategy;
	}

	public double getMaxPercentageDiscount() {
		return maxPercentageDiscount;
	}

	public void setMaxPercentageDiscount(double maxPercentageDiscount) {
		this.maxPercentageDiscount = maxPercentageDiscount;
	}

	public DlvZoneStrategy getZoneStrategy() {
		return zoneStrategy;
	}

	public void setMinSubtotal(double minSubtotal) {
		this.minSubtotal = minSubtotal;
	}

	public void setPercentOff(double percentOff) {
		this.percentOff = percentOff;
	}

}
