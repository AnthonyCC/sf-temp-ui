package com.freshdirect.webapp.unbxdanalytics.event;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.freshdirect.webapp.unbxdanalytics.visitor.Visitor;

public final class AddToCartEvent extends AbstractAnalyticsEvent {

    private String productId;

    private Double quantity;

    public AddToCartEvent(Visitor visitor, LocationInfo location, String productId, Double quantity, boolean cosAction) {
        super(visitor, location, cosAction);

        this.productId = productId;
        this.quantity = quantity;
    }

    @JsonProperty("pid")
    public String getProductId() {
        return productId;
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
