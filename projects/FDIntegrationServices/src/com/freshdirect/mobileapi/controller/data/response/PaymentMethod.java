package com.freshdirect.mobileapi.controller.data.response;

public class PaymentMethod {

    private String id;

    private BillingAddress billingAddress;

    private String bankName;

    private String abaRoutingNumber;

    private String maskedAccountNumber;

    private String cardType;
    
    private String name;

    public PaymentMethod(com.freshdirect.mobileapi.model.PaymentMethod paymentMethod) {
        this.id = paymentMethod.getId();
        this.billingAddress = new BillingAddress(paymentMethod);
        this.maskedAccountNumber = paymentMethod.getMaskedAccountNumber();
        this.name = paymentMethod.getName();
        this.bankName = paymentMethod.getBankName();
        this.abaRoutingNumber = paymentMethod.getAbaRouteNumber();
        this.cardType = paymentMethod.getCardType();
    }

    public String getBankName() {
        return bankName;
    }

    public String getAbaRoutingNumber() {
        return abaRoutingNumber;
    }

    public String getCardType() {
        return cardType;
    }

    public String getId() {
        return id;
    }

    public BillingAddress getBillingAddress() {
        return billingAddress;
    }

    public String getMaskedAccountNumber() {
        return maskedAccountNumber;
    }

    public String getName() {
        return name;
    }
}
