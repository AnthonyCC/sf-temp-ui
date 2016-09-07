package com.freshdirect.mobileapi.controller.data.response;

public class DeliveryTimeslotPageResponse extends PageMessageResponse {

    private static final long serialVersionUID = 7936504814556721008L;

    private DeliveryTimeslots deliveryTimeslot;

    public DeliveryTimeslots getDeliveryTimeslot() {
        return deliveryTimeslot;
    }

    public void setDeliveryTimeslot(DeliveryTimeslots deliveryTimeslot) {
        this.deliveryTimeslot = deliveryTimeslot;
    }

}
