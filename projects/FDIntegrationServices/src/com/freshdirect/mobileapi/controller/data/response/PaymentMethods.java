package com.freshdirect.mobileapi.controller.data.response;

import java.util.ArrayList;
import java.util.List;

import com.freshdirect.fdstore.FDException;
import com.freshdirect.mobileapi.model.PaymentMethod;

public class PaymentMethods extends CheckoutResponse {

    private List<ElectronicCheck> electronicChecks = new ArrayList<ElectronicCheck>();

    private List<CreditCard> creditCards = new ArrayList<CreditCard>();

    private String selectedId;

    private boolean isCheckEligible;

    private boolean isECheckRestricted;

    public PaymentMethods(boolean isCheckEligible, boolean isECheckRestricted, List<PaymentMethod> creditCards,
            List<PaymentMethod> electronicChecks) throws FDException {
        this.isCheckEligible = isCheckEligible;
        this.isECheckRestricted = isECheckRestricted;

        for (PaymentMethod creditCard : creditCards) {
            this.creditCards.add(new CreditCard(creditCard));
        }
        for (PaymentMethod electronicCheck : electronicChecks) {
            this.electronicChecks.add(new ElectronicCheck(electronicCheck));
        }
        setStatus(STATUS_SUCCESS);
    }

    public boolean isCheckEligible() {
        return isCheckEligible;
    }

    public void setCheckEligible(boolean isCheckEligible) {
        this.isCheckEligible = isCheckEligible;
    }

    public boolean isECheckRestricted() {
        return isECheckRestricted;
    }

    public void setECheckRestricted(boolean isECheckRestricted) {
        this.isECheckRestricted = isECheckRestricted;
    }

    public List<ElectronicCheck> getElectronicChecks() {
        return electronicChecks;
    }

    public void setElectronicChecks(List<ElectronicCheck> electronicChecks) {
        this.electronicChecks = electronicChecks;
    }

    public List<CreditCard> getCreditCards() {
        return creditCards;
    }

    public void setCreditCards(List<CreditCard> creditCards) {
        this.creditCards = creditCards;
    }

    public String getSelectedId() {
        return selectedId;
    }

    public void setSelectedId(String selectedId) {
        this.selectedId = selectedId;
    }

}
