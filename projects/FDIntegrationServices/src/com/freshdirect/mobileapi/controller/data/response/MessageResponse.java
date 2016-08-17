package com.freshdirect.mobileapi.controller.data.response;

import java.io.Serializable;

import com.freshdirect.mobileapi.controller.data.Message;

public class MessageResponse extends Message implements Serializable {

    private static final long serialVersionUID = 1851208469422041025L;

    private LoggedIn login;
    private CartDetail cartDetail;

    public LoggedIn getLogin() {
        return login;
    }

    public void setLogin(LoggedIn login) {
        this.login = login;
    }

    public CartDetail getCartDetail() {
        return cartDetail;
    }

    public void setCartDetail(CartDetail cartDetail) {
        this.cartDetail = cartDetail;
    }
}
