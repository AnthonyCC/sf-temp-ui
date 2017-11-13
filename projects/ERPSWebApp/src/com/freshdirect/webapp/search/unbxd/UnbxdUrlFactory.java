package com.freshdirect.webapp.search.unbxd;

import com.freshdirect.fdstore.FDStoreProperties;


public class UnbxdUrlFactory {

    private static final String SEARCH_URL = "/search?q=";
    private static final String AUTOSUGGEST_URL = "/autosuggest?q=";

    public static String getBaseUnbxdUrl(UnbxdSearchProperties searchProperties) {
        String baseUrl = FDStoreProperties.getUnbxdBaseUrl() + "/" + FDStoreProperties.getUnbxdApiKey() + "/";

        if (searchProperties.isCorporateSearch()) {
            baseUrl += FDStoreProperties.getUnbxdCosSiteKey();
        } else {
            baseUrl += FDStoreProperties.getUnbxdSiteKey();
        }

        return baseUrl;
    }

    public static String getSearchUrl(UnbxdSearchProperties searchProperties) {
        return getBaseUnbxdUrl(searchProperties) + SEARCH_URL;
    }

    public static String getAutoSuggestUrl(UnbxdSearchProperties searchProperties) {
        return getBaseUnbxdUrl(searchProperties) + AUTOSUGGEST_URL;
    }

}
