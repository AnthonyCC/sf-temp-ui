package com.freshdirect.mobileapi.controller.data.response;

import java.util.ArrayList;
import java.util.List;

import com.freshdirect.fdstore.FDException;

public class PaymentMethods extends CheckoutResponse {

    private List<ElectronicCheck> electronicChecks = new ArrayList<ElectronicCheck>();

    private List<CreditCard> creditCards = new ArrayList<CreditCard>();
    
    private List<EBTCard> ebtCards = new ArrayList<EBTCard>();

    private String selectedId;

    private boolean isCheckEligible;

    private boolean isECheckRestricted;
    
    private boolean isEbtAccepted;

    public PaymentMethods(boolean isCheckEligible, boolean isECheckRestricted, boolean isEbtAccepted, List<com.freshdirect.mobileapi.model.PaymentMethod> creditCards,
            List<com.freshdirect.mobileapi.model.PaymentMethod> electronicChecks, List<com.freshdirect.mobileapi.model.PaymentMethod> ebtCards) throws FDException {
        this.isCheckEligible = isCheckEligible;
        this.isECheckRestricted = isECheckRestricted;
        this.isEbtAccepted = isEbtAccepted;

        for (com.freshdirect.mobileapi.model.PaymentMethod creditCard : creditCards) {
            this.creditCards.add(new CreditCard(creditCard));
        }
        for (com.freshdirect.mobileapi.model.PaymentMethod electronicCheck : electronicChecks) {
            this.electronicChecks.add(new ElectronicCheck(electronicCheck));
        }
        for (com.freshdirect.mobileapi.model.PaymentMethod ebtCard : ebtCards) {
            this.ebtCards.add(new EBTCard(ebtCard));
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

	public boolean isEbtAccepted() {
		return isEbtAccepted;
	}

	public void setEbtAccepted(boolean isEbtAccepted) {
		this.isEbtAccepted = isEbtAccepted;
	}

	public List<EBTCard> getEbtCards() {
		return ebtCards;
	}

	public void setEbtCards(List<EBTCard> ebtCards) {
		this.ebtCards = ebtCards;
	}

}
