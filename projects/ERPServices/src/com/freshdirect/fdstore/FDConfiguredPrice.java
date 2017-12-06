package com.freshdirect.fdstore;

import java.io.Serializable;

import com.freshdirect.common.pricing.MaterialPrice;
import com.freshdirect.common.pricing.ZoneInfo;

public class FDConfiguredPrice implements Serializable {

	private final double configuredPrice;
	private final double promotionValue;
	private double unscaledPrice;
	private final MaterialPrice pricingCondition;
	private double couponDiscountValue;
	private final ZoneInfo zoneInfo;

	public FDConfiguredPrice(
		double configuredPrice,
		double promotionValue,
		MaterialPrice pricingCondition,
		ZoneInfo zoneInfo) {

		this.configuredPrice = configuredPrice;
		this.promotionValue = promotionValue;
		this.pricingCondition = pricingCondition;
		this.zoneInfo=zoneInfo;

	}

	public FDConfiguredPrice(
			double configuredPrice,
			double promotionValue,
			double couponDiscountValue,
			MaterialPrice pricingCondition,
			ZoneInfo zoneInfo) {

			this.configuredPrice = configuredPrice;
			this.promotionValue = promotionValue;
			this.pricingCondition = pricingCondition;
			this.couponDiscountValue=couponDiscountValue;
			this.zoneInfo=zoneInfo;

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
	
	public double getOriginalPrice() {
		return pricingCondition.getOriginalPrice();
	}

	public double getCouponDiscountValue() {
		return couponDiscountValue;
	}

	public void setCouponDiscountValue(double couponDiscountValue) {
		this.couponDiscountValue = couponDiscountValue;
	}
	
	public ZoneInfo getZoneInfo() {
		return zoneInfo;
	}
	public double getUnscaledPrice() {
		return unscaledPrice;
	}
	public void setUnscaledPrice(double p){
		unscaledPrice = p;
	}

}
