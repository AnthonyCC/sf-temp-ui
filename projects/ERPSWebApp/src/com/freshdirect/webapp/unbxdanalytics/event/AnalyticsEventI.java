package com.freshdirect.webapp.unbxdanalytics.event;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
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
    String getReferrer();
    
    /**
     * Timestamp of the event creation
     * @return
     */
    @JsonIgnore
    long getTimestamp();
    
    @JsonProperty("visit_type")
    String getVisitType();
    
    @JsonIgnore
    boolean isVisitorEventRequired();
}
