package com.freshdirect.webapp.unbxdanalytics.visitor;

import com.freshdirect.cms.CmsServiceLocator;
import com.freshdirect.webapp.unbxdanalytics.event.VisitorEvent;

/**
 * This cache keeps track UNBXD visitor lifecycle.
 * 
 * When a user makes an event(search hit, add to cart, etc), you need to check if the visitor event has been fired for that user.
 * #1 If it has not been fired, you need to fire it
 *      passing the value "first_time" to "visitor_type"
 * #2 If it has been fired before, you need to check
 *      if the last time it got fired was before 30 minutes from now.
 * #3 If it was before 30 minutes, you need to fire it
 *      passing the value "repeat" to "visitor_type"
 * #4 Once the visitor event is fired
 *      you can fire other events' APIs.
 * 
 * @author segabor
 *
 */
public final class VisitTypeCache {

    /**
     * EhCache cache name
     */
    private final String CACHE_NAME = "UNBXDVisitorCache";

    /**
     * Timeout value in minutes
     */
    public final static long TIMEOUT = 30 * 1000 * 1000;

    private static VisitTypeCache sharedInstance = null;

    private VisitTypeCache() {
    }

    public static VisitTypeCache getInstance() {
        if (sharedInstance == null) {
            synchronized (VisitTypeCache.class) {
                if (sharedInstance == null) {
                    sharedInstance = new VisitTypeCache();
                }
            }
        }
        return sharedInstance;
    }


    /**
     * Update visit type attribute of UNBXD visitor
     * 
     * @param uid
     * 
     * @return true when a separate {@link VisitorEvent} event must be sent
     */
    public VisitType createVisitType(final String uid) {
        final String cacheKey = uid;
        
        final long now = System.currentTimeMillis();
        final Long lastCheck = CmsServiceLocator.ehCacheUtil().getObjectFromCache(CACHE_NAME, cacheKey);

        VisitType visitTypeInfo = createInternal(now, lastCheck);

        // update cache entry with current time
        CmsServiceLocator.ehCacheUtil().putObjectToCache(CACHE_NAME, cacheKey, Long.valueOf(now));

        return visitTypeInfo;
    }

    /**
     * Logic that evaluates visit type based on current time and last time it was checked
     * 
     * @param visitor Visitor object
     * @param now current time in millisecs
     * @param lastCheck Long object having the list time visit type was checked in millisecs
     * 
     * @return true when a separate {@link VisitorEvent} event must be sent
     */
    private static VisitType createInternal(final long now, final Long lastCheck) {
        // is visit type recurring?
        final boolean repeating = lastCheck != null;
        
        boolean shouldSendVisitorEvent = repeating
                ? ( now-lastCheck.longValue() ) >= TIMEOUT
                : true
        ;

        return new VisitType(repeating, shouldSendVisitorEvent);
    }
}
