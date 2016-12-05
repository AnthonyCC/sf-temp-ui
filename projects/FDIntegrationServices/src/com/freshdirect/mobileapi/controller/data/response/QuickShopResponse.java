package com.freshdirect.mobileapi.controller.data.response;

import com.freshdirect.mobileapi.controller.data.Message;
import com.freshdirect.webapp.ajax.reorder.data.QuickShopReturnValue;

public class QuickShopResponse extends Message {

    private QuickShopReturnValue quickShop;

    public QuickShopReturnValue getQuickShop() {
        return quickShop;
    }

    public void setQuickShop(QuickShopReturnValue quickShop) {
        this.quickShop = quickShop;
    }
}
