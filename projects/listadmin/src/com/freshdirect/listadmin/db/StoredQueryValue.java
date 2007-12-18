package com.freshdirect.listadmin.db;

import java.io.Serializable;

import com.freshdirect.listadmin.nvp.NVPI;

public class StoredQueryValue implements Serializable, NVPI {
	private String storedQueryValueId;
	private String storedQueryId;
	private String name;
	private String value;
	
	public StoredQueryValue() {}
	
	public StoredQueryValue(String name,String value) {
		this.name = name;
		this.value = value;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}

	public String getStoredQueryValueId() {
		return storedQueryValueId;
	}

	public void setStoredQueryValueId(String storedQueryValueId) {
		this.storedQueryValueId = storedQueryValueId;
	}

	public String getStoredQueryId() {
		return storedQueryId;
	}

	public void setStoredQueryId(String storedQueryId) {
		this.storedQueryId = storedQueryId;
	}

	public boolean getSelected() {
		return false;
	}
}
