package com.freshdirect.framework.event;

import java.io.Serializable;

public abstract class FDEvent implements Serializable {
	protected String eStoreId;
	
	public String getEStoreId() {
		return eStoreId;
	}

	public void setEStoreId(String eStoreId) {
		this.eStoreId = eStoreId;
	}
}
