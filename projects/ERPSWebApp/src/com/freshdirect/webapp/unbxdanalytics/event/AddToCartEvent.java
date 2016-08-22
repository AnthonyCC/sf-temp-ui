package com.freshdirect.webapp.unbxdanalytics.event;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.freshdirect.webapp.unbxdanalytics.visitor.Visitor;

public final class AddToCartEvent extends AbstractAnalyticsEvent {

    private String skuCode;

    private Double quantity;

    public AddToCartEvent(Visitor visitor, LocationInfo location, String skuCode, Double quantity) {
        super(visitor, location);

        this.skuCode = skuCode;
        this.quantity = quantity;
    }

    @JsonProperty("pid")
    public String getSkuCode() {
        return skuCode;
    }

    @JsonProperty("qty")
    public Double getQuantity() {
        return quantity;
    }

    @Override
    public AnalyticsEventType getType() {
        return AnalyticsEventType.ATC;
    }
}
