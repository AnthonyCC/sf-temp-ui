package com.freshdirect.mobileapi.controller.data.response;

import java.util.List;

import com.freshdirect.mobileapi.controller.data.Message;
import com.freshdirect.mobileapi.controller.data.ProductConfiguration;

public class QuickShop extends Message {
    List<ProductConfiguration> products;

    public List<ProductConfiguration> getProducts() {
        return products;
    }

    public void setProducts(List<ProductConfiguration> products) {
        this.products = products;
    }

}
