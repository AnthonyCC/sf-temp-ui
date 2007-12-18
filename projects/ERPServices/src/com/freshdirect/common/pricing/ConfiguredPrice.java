package com.freshdirect.common.pricing;

import java.io.Serializable;

public class ConfiguredPrice implements Serializable {

	private final Price price;
	private final MaterialPrice pricingCondition;

	public ConfiguredPrice(Price price, MaterialPrice pricingCondition) {
		this.price = price;
		this.pricingCondition = pricingCondition;
	}

	public Price getPrice() {
		return this.price;
	}

	public MaterialPrice getPricingCondition() {
		return this.pricingCondition;
	}

}
