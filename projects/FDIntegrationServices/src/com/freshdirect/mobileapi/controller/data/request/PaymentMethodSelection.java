package com.freshdirect.mobileapi.controller.data.request;

import com.freshdirect.mobileapi.controller.data.Message;

public class PaymentMethodSelection extends Message {

    private String paymentMethodId;
    private String billingRef;
    private String isOrderLevel;

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
    
    public String getIsOrderLevel() {
        return isOrderLevel;
    }

    public void setIsOrderLevel(String isOrderLevel) {
        this.isOrderLevel = isOrderLevel;
    }

}
