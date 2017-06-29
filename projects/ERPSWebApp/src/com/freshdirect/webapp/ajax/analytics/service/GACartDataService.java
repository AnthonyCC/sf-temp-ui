package com.freshdirect.webapp.ajax.analytics.service;

import com.freshdirect.fdstore.customer.FDCartLineI;
import com.freshdirect.webapp.ajax.analytics.data.GACartData;

public class GACartDataService {

    private static final GACartDataService INSTANCE = new GACartDataService();

    private GACartDataService() {

    }

    public static GACartDataService defaultService() {
        return INSTANCE;
    }

    public GACartData populateCartData(FDCartLineI cartLine, String quantity) {
        GACartData cartData = new GACartData();
        cartData.setProductData(GAProductDataService.defaultService().populateProductData(cartLine, quantity));
        return cartData;
    }

}
