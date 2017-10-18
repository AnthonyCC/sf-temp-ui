package com.freshdirect.webapp.unbxdanalytics.event;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.freshdirect.webapp.unbxdanalytics.visitor.Visitor;

public final class OrderEvent extends AbstractAnalyticsEvent {

    private String productId;

    private Double price;

    private Double quantity;

    public OrderEvent(Visitor visitor, LocationInfo location, String productId, Double price, Double quantity, boolean cosAction) {
        super(visitor, location, cosAction);

        this.productId = productId;
        this.price = price;
        this.quantity = quantity;
    }

    @JsonProperty("pid")
    public String getProductId() {
        return productId;
    }

    public Double getPrice() {
        return price;
    }

    @JsonProperty("qty")
    public Double getQuantity() {
        return quantity;
    }

    @Override
    public AnalyticsEventType getType() {
        return AnalyticsEventType.ORDER;
    }
}
