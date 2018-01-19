package com.freshdirect.webapp.ajax.oauth2.data;

public enum OAuth2Type {
	CODE("code"),
	TOKEN("token"),
	REFRESHTOKEN("refreshToken");
	
	private final String type;
	
	OAuth2Type(String type) {
		this.type = type;
	}
	
	public String getType() {
		return type;
	}
}
