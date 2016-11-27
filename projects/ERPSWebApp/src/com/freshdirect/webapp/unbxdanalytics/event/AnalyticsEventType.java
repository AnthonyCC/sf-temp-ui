package com.freshdirect.webapp.unbxdanalytics.event;

/**
 * UNBXD Analytics Integration
 * 
 * Analytics Event Type
 * 
 * @author segabor
 *
 */
public enum AnalyticsEventType {
    VISITOR("visitor"), /* Generic, visitor event */
    BROWSE("browse"), /* Browse event */
    SEARCH("search"), /* Search event */
    CLICK_THRU("click"), /* Product click-thru event */
    ATC("cart"), /* Add-To-Cart event */
    ORDER("order") /* Submit Order event */
    ;

    private final String action;
    
    AnalyticsEventType(String action) {
        this.action = action;
    }
    
    public String getAction() {
        return action;
    }
}
