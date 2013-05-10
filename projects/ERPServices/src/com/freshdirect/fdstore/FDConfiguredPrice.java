package com.freshdirect.fdstore;

import java.io.Serializable;

import com.freshdirect.common.pricing.MaterialPrice;

public class FDConfiguredPrice implements Serializable {

	private final double configuredPrice;
	private final double promotionValue;
	private final MaterialPrice pricingCondition;
	private double couponDiscountValue;

	public FDConfiguredPrice(
		double configuredPrice,
		double promotionValue,
		MaterialPrice pricingCondition) {

		this.configuredPrice = configuredPrice;
		this.promotionValue = promotionValue;
		this.pricingCondition = pricingCondition;

	}

	public FDConfiguredPrice(
			double configuredPrice,
			double promotionValue,
			double couponDiscountValue,
			MaterialPrice pricingCondition) {

			this.configuredPrice = configuredPrice;
			this.promotionValue = promotionValue;
			this.pricingCondition = pricingCondition;
			this.couponDiscountValue=couponDiscountValue;

	}
	
	public double getConfiguredPrice() {
		return configuredPrice;
	}

	public double getPromotionValue() {
		return promotionValue;
	}

	public double getBasePrice() {
		return pricingCondition.getPrice();
	}

	public String getBasePriceUnit() {
		return pricingCondition.getPricingUnit();
	}

	public double getCouponDiscountValue() {
		return couponDiscountValue;
	}

	public void setCouponDiscountValue(double couponDiscountValue) {
		this.couponDiscountValue = couponDiscountValue;
	}

}
