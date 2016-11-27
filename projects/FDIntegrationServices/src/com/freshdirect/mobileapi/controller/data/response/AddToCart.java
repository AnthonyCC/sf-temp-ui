package com.freshdirect.mobileapi.controller.data.response;

import com.freshdirect.mobileapi.controller.data.Message;

/**
 * @author Rob
 *
 */
public class AddToCart extends Message {

    private String[] cartLineIds;

    public String[] getCartLineIds() {
        return cartLineIds;
    }

    public void setCartLineIds(String[] cartLineIds) {
        this.cartLineIds = cartLineIds;
    }

}
