package com.freshdirect.fdstore;

import java.io.Serializable;

import com.freshdirect.common.pricing.MaterialPrice;

public class FDConfiguredPrice implements Serializable {

	private final double configuredPrice;
	private final double promotionValue;
	private final MaterialPrice pricingCondition;

	public FDConfiguredPrice(
		double configuredPrice,
		double promotionValue,
		MaterialPrice pricingCondition) {

		this.configuredPrice = configuredPrice;
		this.promotionValue = promotionValue;
		this.pricingCondition = pricingCondition;

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

}
