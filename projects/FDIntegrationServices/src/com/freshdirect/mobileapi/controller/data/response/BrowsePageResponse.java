package com.freshdirect.mobileapi.controller.data.response;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class BrowsePageResponse extends MessageResponse {

    private static final long serialVersionUID = 2442758769270937157L;

    private Map<String, ?> browse;
    
    private boolean includeNullValue = true;

    public Map<String, ?> getBrowse() {
        return browse;
    }

    public void setBrowse(Map<String, ?> browse) {
        this.browse = browse;
    }
    
    @JsonIgnore
    public boolean includeNullValue() {
    	return includeNullValue;
    }
    public void setIncludeNullValue(boolean b) {
    	includeNullValue = b;
    }

}
