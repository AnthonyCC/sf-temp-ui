package com.freshdirect.webapp.util;

import com.freshdirect.fdstore.customer.FDUserI;


/**
 * Supplies context to the {@link ConfigurationStrategy configuration utility}
 * 
 * @author istvan
 *
 */
public class ConfigurationContext {
	
	
	private FDUserI fdUser = null;
	
	public FDUserI getFDUser() {
		return fdUser;
	}
	
	public void setFDUser(FDUserI fdUser) {
		this.fdUser = fdUser;
	}
	
	
}
