package com.freshdirect.webapp.unbxdanalytics.event;

import com.freshdirect.webapp.unbxdanalytics.visitor.Visitor;

public final class SearchEvent extends AbstractAnalyticsEvent {

    /**
     * The search query entered in the search box
     */
    private String query = null;

    public SearchEvent(Visitor visitor, LocationInfo location, String query) {
        super(visitor, location);

        this.query = query;
    }

    public String getQuery() {
        return query;
    }

    @Override
    public AnalyticsEventType getType() {
        return AnalyticsEventType.SEARCH;
    }

}
