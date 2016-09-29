package com.freshdirect.mobileapi.controller.data.response;

public class SearchMessageResponse extends MessageResponse {

    private static final long serialVersionUID = -4220385142597682317L;

    private WebSearchResult search;

    public WebSearchResult getSearch() {
        return search;
    }

    public void setSearch(WebSearchResult search) {
        this.search = search;
    }

}
