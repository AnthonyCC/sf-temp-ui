package com.freshdirect.deliverypass;

import java.io.Serializable;

public class DlvPassAvailabilityInfo implements Serializable {
	private Integer key;
	private String reason;
	public DlvPassAvailabilityInfo(Integer key, String reason) {
		super();
		this.key = key;
		this.reason = reason;
	}
	public Integer getKey() {
		return key;
	}
	public void setKey(Integer key) {
		this.key = key;
	}
	public String getReason() {
		return reason;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}

}
