package com.freshdirect.smartstore.impl;

import java.util.List;

public class UserShoppingHistory {
	private static final int MAX_ENTRY_DURATION = 10* 60* 1000;
	
	private long timeRecorded;
	
	// List<ContentKey> sorted by frequency
	private List contentKeys;
	
	public UserShoppingHistory(List contentKeys) {
		timeRecorded = System.currentTimeMillis();
		this.contentKeys = contentKeys;
	}
	
	public boolean expired() {
		long diff = System.currentTimeMillis() - timeRecorded;
		return  diff > MAX_ENTRY_DURATION;
	}
	
	public List getContentKeys() {
		return contentKeys;
	}
}
