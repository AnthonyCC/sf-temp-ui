package com.freshdirect.mobileapi.controller.data.request;

import com.freshdirect.mobileapi.controller.data.Message;
import com.freshdirect.mobileapi.controller.data.ProductConfiguration;

public class UpdateItemInCart extends Message {

    private String cartLineId;
    
    private boolean quickBuy; //to denote add to cart from the quickshop
    
    // Some products have "terms/conditions" associated. this value indicates that they agreed to t/c.
    private String agreeToTerms;
    
    private boolean dlvPassCart;

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

	public boolean isQuickBuy() {
		return quickBuy;
	}

	public void setQuickBuy(boolean quickBuy) {
		this.quickBuy = quickBuy;
	}

	public boolean isDlvPassCart() {
		return dlvPassCart;
	}

	public void setDlvPassCart(boolean dlvPassCart) {
		this.dlvPassCart = dlvPassCart;
	}
}
