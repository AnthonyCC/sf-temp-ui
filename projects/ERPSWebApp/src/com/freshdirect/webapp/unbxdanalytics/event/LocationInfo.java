package com.freshdirect.webapp.unbxdanalytics.event;

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
    public final String referer;

    public LocationInfo(String url, String referer) {
        this.url = url;
        this.referer = referer;
    }

    public static LocationInfo withUrl(String url) {
        return new LocationInfo(url, null);
    }

    public static LocationInfo withUrlAndReferer(String url, String referer) {
        return new LocationInfo(url, referer);
    }
}
