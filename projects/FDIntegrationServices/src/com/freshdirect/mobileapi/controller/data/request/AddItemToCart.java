package com.freshdirect.mobileapi.controller.data.request;

import com.freshdirect.mobileapi.controller.data.Message;
import com.freshdirect.mobileapi.controller.data.ProductConfiguration;

public class AddItemToCart extends Message {

    //Add To Cart - Indicates users has checked "Email a copy of this recipe on the day of delivery!"
    private String requestNotification;

    private String recipeId;

    // Some products have "terms/conditions" associated. this value indicates that they agreed to t/c.
    private String agreeToTerms;

    private String trackingCode;

    private String trackingCodeEx;

    private String impressionId;

    private SmartStoreConfiguration smartStoreConfiguration = new SmartStoreConfiguration();

    private ProductConfiguration productConfiguration = new ProductConfiguration();

    public ProductConfiguration getProductConfiguration() {
        return productConfiguration;
    }

    public void setProductConfiguration(ProductConfiguration productConfiguration) {
        this.productConfiguration = productConfiguration;
    }

    public SmartStoreConfiguration getSmartStoreConfiguration() {
        return smartStoreConfiguration;
    }

    public void setSmartStoreConfiguration(SmartStoreConfiguration smartStoreConfiguration) {
        this.smartStoreConfiguration = smartStoreConfiguration;
    }

    public String getAgreeToTerms() {
        return agreeToTerms;
    }

    public void setAgreeToTerms(String agreeToTerms) {
        this.agreeToTerms = agreeToTerms;
    }

    public String getRecipeId() {
        return recipeId;
    }

    public void setRecipeId(String recipeId) {
        this.recipeId = recipeId;
    }

    public String getRequestNotification() {
        return requestNotification;
    }

    public void setRequestNotification(String requestNotification) {
        this.requestNotification = requestNotification;
    }

    public void setTrackingCode(String trackingCode) {
        this.trackingCode = trackingCode;
    }

    public void setTrackingCodeEx(String trackingCodeEx) {
        this.trackingCodeEx = trackingCodeEx;
    }

    public void setImpressionId(String impressionId) {
        this.impressionId = impressionId;
    }

    public String getTrackingCode() {
        return trackingCode;
    }

    public String getTrackingCodeEx() {
        return trackingCodeEx;
    }

    public String getImpressionId() {
        return impressionId;
    }

}
