package com.freshdirect.webapp.util;


/**
 * Supplies context to the {@link ConfigurationStrategy configuration utility}
 * 
 * @author istvan
 *
 */
public class ConfigurationContext {
	
	
	private String erpCustomerId = null;
	
	public String getErpCustomerId() {
		return erpCustomerId;
	}
	
	public void setErpCustomerId(String erpCustomerId) {
		this.erpCustomerId = erpCustomerId;
	}
	
	
}
