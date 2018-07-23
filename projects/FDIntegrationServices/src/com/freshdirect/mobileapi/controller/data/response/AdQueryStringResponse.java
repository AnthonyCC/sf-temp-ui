package com.freshdirect.mobileapi.controller.data.response;

import java.util.Map;

import com.freshdirect.mobileapi.controller.data.Message;


public class AdQueryStringResponse extends Message {

    private final Map<String, String> queryParams;

    public AdQueryStringResponse(Map<String, String> queryParams) {
        this.queryParams = queryParams;
    }

    public Map<String, String> getQueryParams() {
        return queryParams;
    }

}
