package com.freshdirect.webapp.ajax.login;

import java.io.Serializable;

public class LoginRequest implements Serializable{

	private static final long serialVersionUID = 8051699402519682381L;
	
	private String userId;
	private String password;
	private String successPage;
	private boolean fdtcacknowledge;
	
	public boolean isFdtcacknowledge() {
		return fdtcacknowledge;
	}
	public void setFdtcacknowledge(boolean fdtcacknowledge) {
		this.fdtcacknowledge = fdtcacknowledge;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getSuccessPage() {
		if (this.successPage == null) {
			this.setSuccessPage("");
		}
		return successPage;
	}
	public void setSuccessPage(String successPage) {
		this.successPage = successPage;
	}
}
