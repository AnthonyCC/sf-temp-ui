package com.freshdirect.webapp.unbxdanalytics.event;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * UNBXD Analytics Integration
 * 
 * Analytics Event interface
 * 
 * @author segabor
 *
 */
public interface AnalyticsEventI {

    /**
     * Return event type, never null
     * 
     * @return
     */
    @JsonIgnore
    AnalyticsEventType getType();

    /**
     * Return unique id
     * @return
     */
    @JsonIgnore
    String getUid();
    
    String getUrl();
    String getReferer();
    
    @JsonProperty("visit_type")
    String getVisitType();
}
