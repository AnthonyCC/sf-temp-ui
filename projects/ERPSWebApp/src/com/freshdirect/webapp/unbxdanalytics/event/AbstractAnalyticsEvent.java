package com.freshdirect.webapp.unbxdanalytics.event;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.freshdirect.webapp.unbxdanalytics.visitor.Visitor;

public abstract class AbstractAnalyticsEvent implements AnalyticsEventI {

    protected final Visitor visitor;
    protected final LocationInfo location;
    
    @JsonIgnore
    protected final long timestamp;

    public AbstractAnalyticsEvent(Visitor visitor, LocationInfo location) {
        this.visitor = visitor;
        this.location = location;
        this.timestamp = System.currentTimeMillis();
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
    public boolean isVisitorEventRequired() {
        return visitor.isVisitorEventRequired();
    }

    @JsonIgnore
    public LocationInfo getLocationInfo() {
        return location;
    }
    
    @Override
    public String getUrl() {
        return location.url;
    }
    
    @Override
    public long getTimestamp() {
        return timestamp;
    }

    @Override
    @JsonInclude(Include.ALWAYS)
    public String getReferrer() {
        return location.referrer;
    }

    @Override
    public String toString() {
        StringBuffer buf = new StringBuffer();

        buf.append("{")
            .append("type:").append(getType()).append("; ")
            .append("uid:").append(visitor.getUID()).append("; ")
            .append("visit_type:").append(visitor.getVisitType()).append("; ")
            .append("ref:").append(location.url).append("; ")
            .append("}");
        ;

        return buf.toString();
    }
}
