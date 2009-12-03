package com.freshdirect.mobileapi.controller.data.response;

import com.freshdirect.fdstore.FDException;

public class ElectronicCheck extends PaymentMethod {

    private String routing;

    private String bankName;

    private String description;

    private boolean isRestricted;

    public ElectronicCheck(com.freshdirect.mobileapi.model.PaymentMethod paymentMethod) throws FDException {
        super(paymentMethod);
        this.routing = paymentMethod.getAbaRouteNumber();
        this.bankName = paymentMethod.getBankName();
        this.description = paymentMethod.getDescription();
        this.isRestricted = paymentMethod.isBadAccount();
    }

    public String getRouting() {
        return routing;
    }

    @Override
    public String getBankName() {
        return bankName;
    }

    public String getDescription() {
        return description;
    }

    public boolean isRestricted() {
        return isRestricted;
    }

}
