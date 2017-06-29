package com.freshdirect.webapp.ajax.analytics.data;

import java.util.HashMap;
import java.util.Map;

public class GoogleAnalyticsData {

    private Map<String, Object> googleAnalyticsData = new HashMap<String, Object>();

    public Map<String, Object> getGoogleAnalyticsData() {
        return googleAnalyticsData;
    }

    public void setGoogleAnalyticsData(Map<String, Object> googleAnalyticsData) {
        this.googleAnalyticsData = googleAnalyticsData;
    }

}
