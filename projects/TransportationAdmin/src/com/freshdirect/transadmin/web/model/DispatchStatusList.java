package com.freshdirect.transadmin.web.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class DispatchStatusList implements Serializable {
	
	private List<DispatchStatus> dispatchStatus;
	
	public DispatchStatusList() {
		dispatchStatus = new ArrayList<DispatchStatus>();
	}

	public List<DispatchStatus> getDispatchStatus() {
		return dispatchStatus;
	}

	public void setDispatchStatus(List<DispatchStatus> dispatchStatus) {
		this.dispatchStatus = dispatchStatus;
	}		
}
