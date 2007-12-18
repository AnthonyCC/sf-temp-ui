/**
 * @author ekracoff
 * Created on Jun 20, 2005*/

package com.freshdirect.ocf.core;


public class RunCust {
	private String customerId;
	private String email;
	private String status;
	
	public RunCust() {
	}
	
	public RunCust(String customerId, String email) {
		this.customerId = customerId;
		this.email = email;
	}
	
	public String getCustomerId() {
		return customerId;
	}
	
	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}
	
	public String getEmail() {
		return email;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}

}
