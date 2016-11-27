package com.freshdirect.transadmin.web.model;

import java.io.Serializable;

public class DispatchStatus implements Serializable {
	
	private String dispatchId;
	private boolean isKeysReady;
	private boolean phoneAssigned;
	private boolean isDispatched;
	private boolean isCheckedIn;
	private boolean isKeysIn;
	
	public DispatchStatus() {		
	}
	
	public DispatchStatus(String dispatchId, boolean isKeysReady,
			boolean phoneAssigned, boolean isDispatched, boolean isCheckedIn, boolean isKeysIn) {
		super();
		this.dispatchId = dispatchId;
		this.isKeysReady = isKeysReady;
		this.phoneAssigned = phoneAssigned;
		this.isDispatched = isDispatched;		
		this.isCheckedIn = isCheckedIn;
		this.isKeysIn = isKeysIn;
	}
	
	public String getDispatchId() {
		return dispatchId;
	}
	public void setDispatchId(String dispatchId) {
		this.dispatchId = dispatchId;
	}
	public boolean isKeysReady() {
		return isKeysReady;
	}
	public void setKeysReady(boolean isKeysReady) {
		this.isKeysReady = isKeysReady;
	}
	public boolean isPhoneAssigned() {
		return phoneAssigned;
	}
	public void setPhoneAssigned(boolean phoneAssigned) {
		this.phoneAssigned = phoneAssigned;
	}
	public boolean isDispatched() {
		return isDispatched;
	}
	public void setDispatched(boolean isDispatched) {
		this.isDispatched = isDispatched;
	}
	public boolean isCheckedIn() {
		return isCheckedIn;
	}
	public void setCheckedIn(boolean isCheckedIn) {
		this.isCheckedIn = isCheckedIn;
	}

	public boolean isKeysIn() {
		return isKeysIn;
	}

	public void setKeysIn(boolean isKeysIn) {
		this.isKeysIn = isKeysIn;
	}
	
}
