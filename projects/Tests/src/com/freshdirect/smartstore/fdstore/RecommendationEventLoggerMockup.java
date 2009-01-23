package com.freshdirect.smartstore.fdstore;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.naming.NamingException;

import com.freshdirect.event.RecommendationEventLogger;

public class RecommendationEventLoggerMockup extends RecommendationEventLogger {

    List collectedEvents = new ArrayList();

    public RecommendationEventLoggerMockup() throws NamingException {
        super();
    }

    protected void init() throws NamingException {
        // override
    }

    public void log(Class eventClazz, Collection events) {
        collectedEvents.addAll(events);
    }

    public List getCollectedEvents() {
        return collectedEvents;
    }

}
