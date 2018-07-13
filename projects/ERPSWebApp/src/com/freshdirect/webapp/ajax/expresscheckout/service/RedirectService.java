package com.freshdirect.webapp.ajax.expresscheckout.service;

public class RedirectService {

    private static final RedirectService INSTANCE = new RedirectService();

    private RedirectService() {
    }

    public static RedirectService defaultService() {
        return INSTANCE;
    }

    public String populateRedirectUrl(String baseUrl, String key, String value) {
        StringBuffer redirectUrl = new StringBuffer();
        if (key != null && value != null) {
            redirectUrl.append(baseUrl).append('?').append(key).append('=').append(value);
        }
        return redirectUrl.toString();
    }

    public String replacedRedirectUrl(String redirectUrl) {
        if (redirectUrl.contains("/expressco/view_cart.jsp")) {
            return "/expressco/checkout.jsp";
        }
        return redirectUrl;
    }
}
