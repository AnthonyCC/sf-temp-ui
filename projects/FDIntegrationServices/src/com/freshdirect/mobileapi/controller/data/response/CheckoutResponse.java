package com.freshdirect.mobileapi.controller.data.response;

import com.freshdirect.mobileapi.controller.data.Message;

public class CheckoutResponse extends Message {

    private CheckoutHeader checkoutHeader = new CheckoutHeader();

    public CheckoutHeader getCheckoutHeader() {
        return checkoutHeader;
    }

    public void setCheckoutHeader(CheckoutHeader checkoutHeader) {
        this.checkoutHeader = checkoutHeader;
    }

}
