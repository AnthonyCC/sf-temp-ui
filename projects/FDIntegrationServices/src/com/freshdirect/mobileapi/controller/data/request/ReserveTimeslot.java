package com.freshdirect.mobileapi.controller.data.request;

import com.freshdirect.mobileapi.controller.data.Message;

/**
 * 
 * @author Rob
 *
 */
public class ReserveTimeslot extends Message {

    private String addressId;

    private String deliveryTimeslotId;

    private String reservationType;

    public String getAddressId() {
        return addressId;
    }

    public void setAddressId(String addressId) {
        this.addressId = addressId;
    }

    public String getDeliveryTimeslotId() {
        return deliveryTimeslotId;
    }

    public void setDeliveryTimeslotId(String deliveryTimeslotId) {
        this.deliveryTimeslotId = deliveryTimeslotId;
    }

    public String getReservationType() {
        return reservationType;
    }

    public void setReservationType(String reservationType) {
        this.reservationType = reservationType;
    }

}
