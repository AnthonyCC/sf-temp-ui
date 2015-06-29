package com.freshdirect.webapp.ajax.expresscheckout.deliverypass.service;

import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.webapp.ajax.expresscheckout.data.HeaderData;

public class SinglePageCheckoutHeaderService {

	private static final SinglePageCheckoutHeaderService INSTANCE = new SinglePageCheckoutHeaderService();

	private SinglePageCheckoutHeaderService() {
	}

	public static SinglePageCheckoutHeaderService defaultService() {
		return INSTANCE;
	}
	
	public HeaderData populateHeader(FDUserI user) {
		HeaderData headerData = new HeaderData();
		headerData.setPhoneNumber(user.getCustomerServiceContact());
		return headerData;
	}
}
