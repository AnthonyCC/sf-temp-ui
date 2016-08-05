package com.freshdirect.mobileapi.api.data.request;

import com.freshdirect.mobileapi.controller.data.request.PaymentMethodSelection;

public class PaymentMessageRequest extends PaymentMethodSelection {

    private String source;

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }
}
