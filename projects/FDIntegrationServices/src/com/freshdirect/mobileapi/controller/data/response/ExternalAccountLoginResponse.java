package com.freshdirect.mobileapi.controller.data.response;

import com.freshdirect.mobileapi.controller.data.Message;

public class ExternalAccountLoginResponse extends Message {
	private boolean loggedInSuccess;
	public boolean isLoggedInSuccess() {
		return loggedInSuccess;
	}
	public void setLoggedInSuccess(boolean loggedInSuccess) {
		this.loggedInSuccess = loggedInSuccess;
	}
	
}
