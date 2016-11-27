package com.freshdirect.webapp.ajax.expresscheckout.cart.data;


public class BillingReferenceInfo {

    private String billingReference;
    private boolean corporateUser;

    public String getBillingReference() {
        return billingReference;
    }

    public void setBillingReference(String billingReference) {
        this.billingReference = billingReference;
    }

    public boolean isCorporateUser() {
        return corporateUser;
    }

    public void setCorporateUser(boolean corporateUser) {
        this.corporateUser = corporateUser;
    }
}
