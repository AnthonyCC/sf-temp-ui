package com.freshdirect.mobileapi.controller.data.response;

import java.util.List;

import com.freshdirect.mobileapi.controller.data.Message;

public class SelfCreditOrderDetailsData extends Message {

	private boolean customerChefsTableEnabled;
	private String customerServiceContact;
    private List<SelfCreditOrderItemData> orderLines;

    public SelfCreditOrderDetailsData(com.freshdirect.backoffice.selfcredit.data.SelfCreditOrderDetailsData selfCreditOrderDetailsData) {
    	this.customerChefsTableEnabled = selfCreditOrderDetailsData.isCustomerChefsTableEnabled();
    	this.customerServiceContact = selfCreditOrderDetailsData.getCustomerServiceContact();
        this.orderLines = com.freshdirect.mobileapi.controller.data.response.SelfCreditOrderItemData.wrap(selfCreditOrderDetailsData.getOrderLines());
    }

	public boolean isCustomerChefsTableEnabled() {
		return customerChefsTableEnabled;
	}

	public void setCustomerChefsTableEnabled(boolean customerChefsTableEnabled) {
		this.customerChefsTableEnabled = customerChefsTableEnabled;
	}

	public String getCustomerServiceContact() {
		return customerServiceContact;
	}

	public void setCustomerServiceContact(String customerServiceContact) {
		this.customerServiceContact = customerServiceContact;
	}

	public List<SelfCreditOrderItemData> getOrderLines() {
        return orderLines;
    }

    public void setOrderLines(List<SelfCreditOrderItemData> orderLines) {
        this.orderLines = orderLines;
    }
}
