package com.freshdirect.mobileapi.api.data.request;

import com.freshdirect.fdstore.content.CMSPageRequest;

public class PageMessageRequest extends CMSPageRequest {

    private String source;

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

}
