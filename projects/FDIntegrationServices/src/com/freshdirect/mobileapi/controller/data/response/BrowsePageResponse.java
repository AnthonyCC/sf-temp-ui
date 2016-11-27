package com.freshdirect.mobileapi.controller.data.response;

import java.util.Map;

public class BrowsePageResponse extends MessageResponse {

    private static final long serialVersionUID = 2442758769270937157L;

    private Map<String, ?> browse;

    public Map<String, ?> getBrowse() {
        return browse;
    }

    public void setBrowse(Map<String, ?> browse) {
        this.browse = browse;
    }

}
