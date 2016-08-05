package com.freshdirect.mobileapi.api.data.request;

import com.freshdirect.mobileapi.controller.data.request.SearchQuery;

public class SearchMessageRequest extends SearchQuery {

    private String source;

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }
}
