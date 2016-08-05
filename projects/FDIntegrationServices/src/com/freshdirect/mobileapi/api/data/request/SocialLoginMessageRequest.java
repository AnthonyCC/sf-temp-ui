package com.freshdirect.mobileapi.api.data.request;

public class SocialLoginMessageRequest {

    private String redirect_url;
    private String connection_token;

    public String getRedirect_url() {
        return redirect_url;
    }

    public void setRedirect_url(String redirect_url) {
        this.redirect_url = redirect_url;
    }

    public String getConnection_token() {
        return connection_token;
    }

    public void setConnection_token(String connection_token) {
        this.connection_token = connection_token;
    }

}
