package com.freshdirect.customer;

public class VSReasonCodes  {

	private static final long serialVersionUID = 1L;
	
	String reasonId;
	String reason;
	int delay;
	
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
	public int getDelay() {
		return delay;
	}
	public void setDelay(int delay) {
		this.delay = delay;
	}
		
	
}
