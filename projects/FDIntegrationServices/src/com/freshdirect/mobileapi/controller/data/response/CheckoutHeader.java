package com.freshdirect.mobileapi.controller.data.response;

import com.freshdirect.mobileapi.model.Cart;

public class CheckoutHeader {

    private double deliverySurcharge;

    private double subTotal;

    private boolean containsAlcohol;

    public double getDeliverySurcharge() {
        return deliverySurcharge;
    }

    public void setDeliverySurcharge(double deliverySurcharge) {
        this.deliverySurcharge = deliverySurcharge;
    }

    public double getSubTotal() {
        return subTotal;
    }

    public void setSubTotal(double subTotal) {
        this.subTotal = subTotal;
    }

    public boolean isContainsAlcohol() {
        return containsAlcohol;
    }

    public void setContainsAlcohol(boolean containsAlcohol) {
        this.containsAlcohol = containsAlcohol;
    }

    public void setHeader(Cart cart) {
        this.containsAlcohol = cart.containsAlcohol();
        this.deliverySurcharge = cart.getDeliverySurcharge();
        this.subTotal = cart.getSubTotal();
    }

}
