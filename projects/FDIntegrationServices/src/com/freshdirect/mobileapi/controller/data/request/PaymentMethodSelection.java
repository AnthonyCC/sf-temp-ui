package com.freshdirect.mobileapi.controller.data.request;

public class PaymentMethodSelection extends RequestMessage {

    private String paymentMethodId;
    private String billingRef;

    public String getBillingRef() {
        return billingRef;
    }

    public void setBillingRef(String billingRef) {
        this.billingRef = billingRef;
    }

    public String getPaymentMethodId() {
        return paymentMethodId;
    }

    public void setPaymentMethodId(String paymentMethodId) {
        this.paymentMethodId = paymentMethodId;
    }

}
