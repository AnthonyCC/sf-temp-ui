package com.freshdirect.mobileapi.controller;

public class MobileSessionData {

    private boolean checkoutAuthenticated;

    public MobileSessionData() {
        checkoutAuthenticated = true;
    }

    public void reset() {
        checkoutAuthenticated = true;
    }

    public boolean isCheckoutAuthenticated() {
        return checkoutAuthenticated;
    }

    public void setCheckoutAuthenticated(boolean checkoutAuthenticated) {
        this.checkoutAuthenticated = checkoutAuthenticated;
    }

}
