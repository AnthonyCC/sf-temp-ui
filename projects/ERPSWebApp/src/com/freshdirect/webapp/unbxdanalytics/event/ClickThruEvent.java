package com.freshdirect.webapp.unbxdanalytics.event;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.freshdirect.webapp.unbxdanalytics.visitor.Visitor;

public final class ClickThruEvent extends AbstractAnalyticsEvent {

    private String productId;

    private String categoryId;

    public ClickThruEvent(Visitor visitor, LocationInfo location, String productId, String categoryId, boolean cosAction) {
        super(visitor, location, cosAction);

        this.productId = productId;
        this.categoryId = categoryId;
    }

    @JsonProperty("pid")
    public String getProductId() {
        return productId;
    }

    @JsonIgnore
    public String getCategoryId() {
        return categoryId;
    }

    @Override
    public AnalyticsEventType getType() {
        return AnalyticsEventType.CLICK_THRU;
    }
}
