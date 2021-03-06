package com.freshdirect.backoffice.selfcredit.data;

import java.util.List;

public class SelfCreditOrderDetailsData {

	private String customerServiceContact;
    private List<SelfCreditOrderItemData> orderLines;

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
