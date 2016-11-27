package com.freshdirect.webapp.unbxdanalytics.eventsink;

import com.freshdirect.webapp.unbxdanalytics.event.AnalyticsEventI;

public interface EventSinkI {

    public boolean log(AnalyticsEventI event);

}
