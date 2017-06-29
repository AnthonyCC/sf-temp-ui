package com.freshdirect.webapp.ajax.analytics.service;

import java.util.List;

import com.freshdirect.webapp.ajax.analytics.data.GASearchResultsData;
import com.freshdirect.webapp.ajax.browse.data.BrowseData.SearchParams;
import com.freshdirect.webapp.ajax.browse.data.BrowseData.SearchParams.Tab;

public class GASearchResultsDataService {

    private static final GASearchResultsDataService INSTANCE = new GASearchResultsDataService();

    private GASearchResultsDataService() {

    }

    public static GASearchResultsDataService defaultService() {
        return INSTANCE;
    }

    public GASearchResultsData populateSearchResultsData(SearchParams searchParams) {

        GASearchResultsData searchResultsData = new GASearchResultsData();
        searchResultsData.setSearchKeyword(searchParams.getSearchParams());
        searchResultsData.setSearchResults(Integer.toString(countFilteredHits(searchParams.getTabs())));
        return searchResultsData;
    }

    private int countFilteredHits(List<Tab> tabs) {
        int filteredHitCount = 0;
        for (Tab tab : tabs) {
            filteredHitCount += tab.getFilteredHits();
        }
        return filteredHitCount;
    }

}
