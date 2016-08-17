package com.freshdirect.mobileapi.controller.data.response;

import com.freshdirect.mobileapi.controller.data.SearchResult;

public class SearchMessageResponse extends MessageResponse {

    private static final long serialVersionUID = -4220385142597682317L;

    private SearchResult search;

    public SearchResult getSearch() {
        return search;
    }

    public void setSearch(SearchResult search) {
        this.search = search;
    }

}
