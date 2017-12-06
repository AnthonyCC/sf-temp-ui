package com.freshdirect.common.pricing;

import java.io.Serializable;

public class ConfiguredPrice implements Serializable {

	private final Price price;
	private double priceForQuantity1;
	private final MaterialPrice pricingCondition;
	private final ZoneInfo zoneInfo;

	public ConfiguredPrice(Price price, MaterialPrice pricingCondition,ZoneInfo zoneInfo) {
		this.price = price;
		this.pricingCondition = pricingCondition;
		this.zoneInfo=zoneInfo;
	}
	public ConfiguredPrice(Price price, MaterialPrice pricingCondition,ZoneInfo zoneInfo, double unscaledPrice) {
		this(price, pricingCondition, zoneInfo);
		priceForQuantity1 = unscaledPrice;
	}
	public Price getPrice() {
		return this.price;
	}

	public MaterialPrice getPricingCondition() {
		return this.pricingCondition;
	}
	public ZoneInfo getZoneInfo() {
		return zoneInfo;
	}
	public double getUnscaledPrice() {
		return priceForQuantity1;
	}

}
