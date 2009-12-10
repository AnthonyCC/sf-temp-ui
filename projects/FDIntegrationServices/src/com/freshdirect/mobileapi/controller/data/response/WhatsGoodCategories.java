package com.freshdirect.mobileapi.controller.data.response;

import java.util.Map;

import com.freshdirect.mobileapi.controller.data.Message;

public class WhatsGoodCategories extends Message {

    private Map<String, String> categories;

    public Map<String, String> getCategories() {
        return categories;
    }

    public void setCategories(Map<String, String> categories) {
        this.categories = categories;
    }
}
