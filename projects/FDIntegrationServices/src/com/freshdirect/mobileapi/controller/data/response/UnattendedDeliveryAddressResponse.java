package com.freshdirect.mobileapi.controller.data.response;

import com.freshdirect.mobileapi.controller.data.Message;

public class UnattendedDeliveryAddressResponse extends Message {

    private boolean isUnattendedDelivery;

    public boolean isUnattendedDelivery() {
        return isUnattendedDelivery;
    }

    public void setUnattendedDelivery(boolean isUnattendedDelivery) {
        this.isUnattendedDelivery = isUnattendedDelivery;
    }

}
