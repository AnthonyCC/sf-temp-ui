package com.freshdirect.mobileapi.controller.data.response;

public class BrowsePageResponse extends MessageResponse {

    private static final long serialVersionUID = 2442758769270937157L;

    private WebBrowseResult category;

    public WebBrowseResult getCategory() {
        return category;
    }

    public void setCategory(WebBrowseResult category) {
        this.category = category;
    }

}
