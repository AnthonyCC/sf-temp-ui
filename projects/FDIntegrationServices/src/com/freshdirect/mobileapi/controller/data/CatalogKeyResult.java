package com.freshdirect.mobileapi.controller.data;

import java.util.List;

public class CatalogKeyResult extends Message{
	
	private String key;
	private List<String> keyList;
	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public List<String> getKeyList() {
		return keyList;
	}

	public void setKeyList(List<String> keyList) {
		this.keyList = keyList;
	}
	
	
	
}
