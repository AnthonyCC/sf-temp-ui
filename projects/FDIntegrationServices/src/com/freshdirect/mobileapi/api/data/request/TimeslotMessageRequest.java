package com.freshdirect.mobileapi.api.data.request;

public class TimeslotMessageRequest {

    private String source;
    private String addressId;

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getAddressId() {
        return addressId;
    }

    public void setAddressId(String addressId) {
        this.addressId = addressId;
    }
}
