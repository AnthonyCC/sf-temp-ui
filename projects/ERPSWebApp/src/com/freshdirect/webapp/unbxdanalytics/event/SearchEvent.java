package com.freshdirect.webapp.unbxdanalytics.event;

import com.freshdirect.webapp.unbxdanalytics.autosuggest.AutoSuggestData;
import com.freshdirect.webapp.unbxdanalytics.visitor.Visitor;

public final class SearchEvent extends AbstractAnalyticsEvent {

    /**
     * The search query entered in the search box
     */
    private String query = null;
    private AutoSuggestData autosuggest_data;
    
    public SearchEvent(Visitor visitor, LocationInfo location, String query, boolean cosAction, AutoSuggestData autosuggest_data) {
        super(visitor, location, cosAction);

        this.query = query;
        this.autosuggest_data = autosuggest_data;
    }

    public String getQuery() {
        return query;
    }

    @Override
    public AnalyticsEventType getType() {
        return AnalyticsEventType.SEARCH;
    }

	public AutoSuggestData getAutosuggest_data() {
		return autosuggest_data;
	}

}
