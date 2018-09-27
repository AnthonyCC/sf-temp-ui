package com.freshdirect.backoffice.selfcredit.data;

import java.util.List;

public class SelfCreditOrderHistoryData {

	private String userFirstName;
	private String userLastName;	
    private List<SelfCreditOrderData> orders;

    public String getUserFirstName() {
		return userFirstName;
	}

	public void setUserFirstName(String userFirstName) {
		this.userFirstName = userFirstName;
	}

	public String getUserLastName() {
		return userLastName;
	}

	public void setUserLastName(String userLastName) {
		this.userLastName = userLastName;
	}

	public List<SelfCreditOrderData> getOrders() {
        return orders;
    }

    public void setOrders(List<SelfCreditOrderData> orders) {
        this.orders = orders;
    }

}
