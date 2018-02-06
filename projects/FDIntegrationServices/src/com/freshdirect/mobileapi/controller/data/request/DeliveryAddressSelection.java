package com.freshdirect.mobileapi.controller.data.request;

import com.freshdirect.mobileapi.controller.data.Message;

public class DeliveryAddressSelection extends Message {

    private String id;

    private String type;
    
    private String returnAddress;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
    
    
    public String getReturnAddress() {
        return returnAddress;
    }
    
    public void setReturnAddress(String returnAddress) {
        this.returnAddress = returnAddress;
    }

}
