package com.freshdirect.mobileapi.controller.data.request;

import com.freshdirect.mobileapi.controller.data.Message;

public class DeliverySlotReservation extends Message {

    private String deliveryTimeslotId;

    public String getDeliveryTimeslotId() {
        return deliveryTimeslotId;
    }

    public void setDeliveryTimeslotId(String deliveryTimeslotId) {
        this.deliveryTimeslotId = deliveryTimeslotId;
    }
}
