package com.freshdirect.mobileapi.controller.data.response;

import java.util.List;

import com.freshdirect.webapp.ajax.product.data.ProductPotatoData;

public class DeliveryTimeslotPageResponse extends PageMessageResponse {

    private static final long serialVersionUID = 7936504814556721008L;

    private DeliveryTimeslots deliveryTimeslot;
    private List<ProductPotatoData> removedProducts;

    public DeliveryTimeslots getDeliveryTimeslot() {
        return deliveryTimeslot;
    }

    public void setDeliveryTimeslot(DeliveryTimeslots deliveryTimeslot) {
        this.deliveryTimeslot = deliveryTimeslot;
    }

    public List<ProductPotatoData> getRemovedProducts() {
        return removedProducts;
    }

    public void setRemovedProducts(List<ProductPotatoData> removedProducts) {
        this.removedProducts = removedProducts;
    }

}
