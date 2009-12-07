package com.freshdirect.mobileapi.controller;

public class MobileSessionData {

    private boolean checkoutAuthenticated;

    public MobileSessionData() {
        checkoutAuthenticated = false;
    }

    public void reset() {
        checkoutAuthenticated = false;
    }

    public boolean isCheckoutAuthenticated() {
        return checkoutAuthenticated;
    }

    public void setCheckoutAuthenticated(boolean checkoutAuthenticated) {
        this.checkoutAuthenticated = checkoutAuthenticated;
    }

}
