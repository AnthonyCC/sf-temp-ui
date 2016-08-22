package com.freshdirect.webapp.search.unbxd.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class UnbxdSearchResponseRoot {

    private UnbxdSearchResponse response;

    @JsonProperty("didYouMean")
    private List<UnbxdSearchDidYouMean> didYouMeans;

    public UnbxdSearchResponse getResponse() {
        return response;
    }

    public void setResponse(UnbxdSearchResponse response) {
        this.response = response;
    }

    public List<UnbxdSearchDidYouMean> getDidYouMeans() {
        return didYouMeans;
    }

    public void setDidYouMeans(List<UnbxdSearchDidYouMean> didYouMeans) {
        this.didYouMeans = didYouMeans;
    }
}
