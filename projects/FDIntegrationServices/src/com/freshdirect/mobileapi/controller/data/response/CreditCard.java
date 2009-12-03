package com.freshdirect.mobileapi.controller.data.response;

import java.text.SimpleDateFormat;

public class CreditCard extends PaymentMethod {

    public CreditCard(com.freshdirect.mobileapi.model.PaymentMethod paymentMethod) {
        super(paymentMethod);
        this.type = paymentMethod.getCardType();
        SimpleDateFormat dateFormatter = new SimpleDateFormat("MM/yyyy");
        String expirationDate = dateFormatter.format(paymentMethod.getExpirationDate());
        String[] parts = expirationDate.split("\\/");
        this.expirationMonth = parts[0];
        this.expirationYear = parts[1];
    }

    private String type;

    private String expirationMonth;

    private String expirationYear;

    public String getExpirationMonth() {
        return expirationMonth;
    }

    public void setExpirationMonth(String expirationMonth) {
        this.expirationMonth = expirationMonth;
    }

    public String getExpirationYear() {
        return expirationYear;
    }

    public void setExpirationYear(String expirationYear) {
        this.expirationYear = expirationYear;
    }
}
