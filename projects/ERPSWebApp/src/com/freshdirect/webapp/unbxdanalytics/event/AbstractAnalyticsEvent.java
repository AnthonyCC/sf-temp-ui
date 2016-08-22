package com.freshdirect.webapp.unbxdanalytics.event;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.freshdirect.webapp.unbxdanalytics.visitor.Visitor;

public abstract class AbstractAnalyticsEvent implements AnalyticsEventI {

    protected final Visitor visitor;
    protected final LocationInfo location;

    public AbstractAnalyticsEvent(Visitor visitor, LocationInfo location) {
        this.visitor = visitor;
        this.location = location;
    }

    @JsonIgnore
    public Visitor getVisitor() {
        return visitor;
    }

    @Override
    public String getUid() {
        return visitor.getUID();
    }

    @Override
    public String getVisitType() {
        return visitor.getVisitType();
    }

    @Override
    public String getUrl() {
        return location.url;
    }

    @Override
    @JsonInclude(Include.NON_NULL)
    public String getReferer() {
        return location.referer;
    }
}
