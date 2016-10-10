package com.freshdirect.webapp.unbxdanalytics.event;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * Class holds data for location tracking
 *
 * @author segabor
 */
public final class LocationInfo {

    /**
     * The url of the page on which the search query was fired
     */
    public final String url;

    /**
     * The url of the previous page, will be empty if the user opened that particular url directly
     */
    @JsonInclude(Include.ALWAYS)
    public final String referrer;

    public LocationInfo(String url, String referrer) {
        this.url = url;
        this.referrer = referrer;
    }

    public static LocationInfo withUrl(String url) {
        return new LocationInfo(url, null);
    }

    public static LocationInfo withUrlAndReferer(String url, String referrer) {
        return new LocationInfo(url, referrer);
    }
}
