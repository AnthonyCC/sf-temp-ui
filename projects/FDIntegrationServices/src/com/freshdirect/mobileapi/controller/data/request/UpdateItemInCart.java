package com.freshdirect.mobileapi.controller.data.request;

import com.freshdirect.mobileapi.controller.data.Message;
import com.freshdirect.mobileapi.controller.data.ProductConfiguration;

public class UpdateItemInCart extends Message {

    private String cartLineId;

    // Some products have "terms/conditions" associated. this value indicates that they agreed to t/c.
    private String agreeToTerms;

    public String getAgreeToTerms() {
        return agreeToTerms;
    }

    public void setAgreeToTerms(String agreeToTerms) {
        this.agreeToTerms = agreeToTerms;
    }

    public String getCartLineId() {
        return cartLineId;
    }

    public void setCartLineId(String cartLineId) {
        this.cartLineId = cartLineId;
    }

    private ProductConfiguration productConfiguration = new ProductConfiguration();

    public ProductConfiguration getProductConfiguration() {
        return productConfiguration;
    }

    public void setProductConfiguration(ProductConfiguration productConfiguration) {
        this.productConfiguration = productConfiguration;
    }

}
