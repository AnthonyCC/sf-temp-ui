package com.freshdirect.webapp.unbxdanalytics.event;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.freshdirect.webapp.unbxdanalytics.visitor.Visitor;

public final class OrderEvent extends AbstractAnalyticsEvent {

    private String skuCode;

    private Double price;

    private Double quantity;

    public OrderEvent(Visitor visitor, LocationInfo location, String skuCode, Double price, Double quantity) {
        super(visitor, location);

        this.skuCode = skuCode;
        this.price = price;
        this.quantity = quantity;
    }

    @JsonProperty("pid")
    public String getSkuCode() {
        return skuCode;
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
