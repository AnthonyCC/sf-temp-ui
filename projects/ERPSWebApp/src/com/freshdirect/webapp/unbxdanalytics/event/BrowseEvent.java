package com.freshdirect.webapp.unbxdanalytics.event;

import com.freshdirect.webapp.unbxdanalytics.visitor.Visitor;

public final class BrowseEvent extends AbstractAnalyticsEvent {

    /**
     * Character that separates path components
     */
    public static final String SEP = "|";

    /**
     * The complete <strike>category</strike>store hierarchy is sent as the query.
     */
    private String query;

    public BrowseEvent(Visitor visitor, LocationInfo location, String query) {
        super(visitor, location);

        this.query = query;
    }

    public String getQuery() {
        return query;
    }

    @Override
    public AnalyticsEventType getType() {
        return AnalyticsEventType.BROWSE;
    }
}
