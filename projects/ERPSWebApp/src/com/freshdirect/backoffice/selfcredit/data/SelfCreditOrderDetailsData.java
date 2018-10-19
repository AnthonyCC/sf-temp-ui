package com.freshdirect.backoffice.selfcredit.data;

import java.util.List;

public class SelfCreditOrderDetailsData {

	private boolean customerChefsTableEnabled;
    private List<SelfCreditOrderItemData> orderLines;

	public boolean isCustomerChefsTableEnabled() {
		return customerChefsTableEnabled;
	}

	public void setCustomerChefsTableEnabled(boolean customerChefsTableEnabled) {
		this.customerChefsTableEnabled = customerChefsTableEnabled;
	}

	public List<SelfCreditOrderItemData> getOrderLines() {
        return orderLines;
    }

    public void setOrderLines(List<SelfCreditOrderItemData> orderLines) {
        this.orderLines = orderLines;
    }
}
