package com.freshdirect.mobileapi.controller.data.request;

import java.util.List;

import com.freshdirect.mobileapi.controller.data.Message;
import com.freshdirect.mobileapi.controller.data.ProductConfiguration;

public class AddMultipleItemsToCart extends Message {
    private String recipeId;

    private String trackingCode;

    private String trackingCodeEx;

    private String impressionId;

    List<ProductConfiguration> productsConfiguration;

    private SmartStoreConfiguration smartStoreConfiguration = new SmartStoreConfiguration();
    
    private boolean quickBuy; //to denote add to cart from the quickshop
    
    private boolean dlvPassCart;

    public List<ProductConfiguration> getProductsConfiguration() {
        return productsConfiguration;
    }

    public void setProductsConfiguration(List<ProductConfiguration> productsConfiguration) {
        this.productsConfiguration = productsConfiguration;
    }

    public String getTrackingCode() {
        return trackingCode;
    }

    public void setTrackingCode(String trackingCode) {
        this.trackingCode = trackingCode;
    }

    public String getTrackingCodeEx() {
        return trackingCodeEx;
    }

    public void setTrackingCodeEx(String trackingCodeEx) {
        this.trackingCodeEx = trackingCodeEx;
    }

    public String getImpressionId() {
        return impressionId;
    }

    public void setImpressionId(String impressionId) {
        this.impressionId = impressionId;
    }

    public String getRecipeId() {
        return recipeId;
    }

    public void setRecipeId(String recipeId) {
        this.recipeId = recipeId;
    }

    public SmartStoreConfiguration getSmartStoreConfiguration() {
        return smartStoreConfiguration;
    }

    public void setSmartStoreConfiguration(SmartStoreConfiguration smartStoreConfiguration) {
        this.smartStoreConfiguration = smartStoreConfiguration;
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
