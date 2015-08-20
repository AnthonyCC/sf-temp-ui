package com.freshdirect.customer;

import java.io.Serializable;

public class VSReasonCodes implements Serializable  {

	private static final long serialVersionUID = 1L;
	
	String reasonId;
	String reason;
	
	
	public String getReasonId() {
		return reasonId;
	}
	public void setReasonId(String reasonId) {
		this.reasonId = reasonId;
	}
	public String getReason() {
		return reason;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}
	
		
	
}
