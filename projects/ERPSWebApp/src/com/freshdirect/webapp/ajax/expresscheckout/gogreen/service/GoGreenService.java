package com.freshdirect.webapp.ajax.expresscheckout.gogreen.service;

import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.customer.FDCustomerManager;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.webapp.ajax.expresscheckout.data.FormDataRequest;
import com.freshdirect.webapp.ajax.expresscheckout.service.FormDataService;

public class GoGreenService {

	private static final GoGreenService INSTANCE = new GoGreenService();

	public static final String GO_GREEN_FIELD_ID = "goGreen";

	private GoGreenService() {
	}

	public static GoGreenService defaultService() {
		return INSTANCE;
	}
	
	public void saveGoGreenOption(FormDataRequest goGreenRequestData, FDUserI user) throws FDResourceException {
		String goGreen = FormDataService.defaultService().get(goGreenRequestData, GO_GREEN_FIELD_ID);
		FDCustomerManager.storeGoGreenPreferences(user.getIdentity().getErpCustomerPK(), goGreen);
	}
	
	public boolean loadGoGreenOption(FDUserI user) throws FDResourceException {
		return FDCustomerManager.loadGoGreenPreference(user.getIdentity().getErpCustomerPK());
	}
}
