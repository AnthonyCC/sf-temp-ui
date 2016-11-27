package com.freshdirect.mobileapi.controller.data.response;

import com.freshdirect.mobileapi.controller.data.Message;

public class Price extends Message {
    Double price;
    Double estimatedQuantity;

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Double getEstimatedQuantity() {
        return estimatedQuantity;
    }

    public void setEstimatedQuantity(Double estimatedQuantity) {
        this.estimatedQuantity = estimatedQuantity;
    }

}
