package com.freshdirect.transadmin.model;

import java.io.Serializable;

public class UserPref implements Serializable
{
	private String userId;
	private String key;
	private String value;
	
	
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	
	

}
