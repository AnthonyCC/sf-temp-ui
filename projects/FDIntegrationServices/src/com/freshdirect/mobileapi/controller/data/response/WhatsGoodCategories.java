package com.freshdirect.mobileapi.controller.data.response;

import java.util.List;

import com.freshdirect.mobileapi.controller.data.Message;
import com.freshdirect.mobileapi.model.data.WhatsGoodCategory;

public class WhatsGoodCategories extends Message {

    private List<WhatsGoodCategory> categories;

    public List<WhatsGoodCategory> getCategories() {
        return categories;
    }

    public void setCategories(List<WhatsGoodCategory> categories) {
        this.categories = categories;
    }
}
