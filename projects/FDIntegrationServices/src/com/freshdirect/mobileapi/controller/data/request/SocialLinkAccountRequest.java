package com.freshdirect.mobileapi.controller.data.request;

public class SocialLinkAccountRequest {
	private String email;
	private String password;
	private String existingToken;
	private String newToken;
	private String provider;
	
	
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getExistingToken() {
		return existingToken;
	}
	public void setExistingToken(String existingToken) {
		this.existingToken = existingToken;
	}
	public String getNewToken() {
		return newToken;
	}
	public void setNewToken(String newToken) {
		this.newToken = newToken;
	}
	
	public String getProvider() {
		return provider;
	}
	public void setProvider(String provider) {
		this.provider = provider;
	}
	
}
