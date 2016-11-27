package com.freshdirect.mobileapi.controller.data.response;

import java.io.Serializable;

import com.freshdirect.mobileapi.controller.data.Message;

public class MessageResponse extends Message implements Serializable, HasLoggedInField, HasCartDetailField {

    private static final long serialVersionUID = 1851208469422041025L;

    private LoggedIn login;
    private CartDetail cartDetail;

    @Override
    public LoggedIn getLogin() {
        return login;
    }

    @Override
    public void setLogin(LoggedIn login) {
        this.login = login;
    }

    @Override
    public CartDetail getCartDetail() {
        return cartDetail;
    }

    @Override
    public void setCartDetail(CartDetail cartDetail) {
        this.cartDetail = cartDetail;
    }
}
