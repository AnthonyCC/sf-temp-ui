package com.freshdirect.mobileapi.controller.data;

import com.freshdirect.mobileapi.controller.data.request.SmartStoreConfiguration;
import com.freshdirect.mobileapi.model.Product;

public class SmartStoreProductResult extends ProductSearchResult {

    private String trackingCode;

    private String trackingCodeEx;

    private String impressionId;

    private SmartStoreConfiguration smartStoreConfiguration;

    public SmartStoreProductResult(Product product) {
        super(product);
    }

    public SmartStoreConfiguration getSmartStoreConfiguration() {
        return smartStoreConfiguration;
    }

    public void setSmartStoreConfiguration(SmartStoreConfiguration smartStoreConfiguration) {
        this.smartStoreConfiguration = smartStoreConfiguration;
    }

    public static SmartStoreProductResult wrap(com.freshdirect.mobileapi.model.Product product) {
        return new SmartStoreProductResult(product);
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
}
