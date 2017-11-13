package com.freshdirect.webapp.unbxdanalytics.event;

import com.freshdirect.webapp.unbxdanalytics.visitor.Visitor;

public final class VisitorEvent extends AbstractAnalyticsEvent {

    public VisitorEvent(Visitor visitor, LocationInfo location, boolean cosAction) {
        super(visitor, location, cosAction);
    }

    @Override
    public AnalyticsEventType getType() {
        return AnalyticsEventType.VISITOR;
    }

}
