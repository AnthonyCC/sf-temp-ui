package com.freshdirect.mobileapi.controller.data.request;

import com.freshdirect.mobileapi.controller.data.Message;

public class PaymentMethodSelection extends Message {

    private String paymentMethodId;
    private String billingRef;
    private String isAccountLevel;
    private boolean dlvPassCart;

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
    
    public String getIsAccountLevel() {
        return isAccountLevel;
    }

    public void setIsAccountLevel(String isAccountLevel) {
        this.isAccountLevel = isAccountLevel;
    }

	public boolean isDlvPassCart() {
		return dlvPassCart;
	}

	public void setDlvPassCart(boolean dlvPassCart) {
		this.dlvPassCart = dlvPassCart;
	}
}
