package com.freshdirect.mobileapi.controller.data.response;

import com.freshdirect.mobileapi.controller.data.Message;

public class SocialResponse extends Message {
	private boolean loggedInSuccess;
	private String resultAction;
    private String resultMessage;
    
	public boolean isLoggedInSuccess() {
		return loggedInSuccess;
	}
	public void setLoggedInSuccess(boolean loggedInSuccess) {
		this.loggedInSuccess = loggedInSuccess;
	}
	public String getResultAction() {
		return resultAction;
	}
	public void setResultAction(String resultAction) {
		this.resultAction = resultAction;
	}
	public String getResultMessage() {
		return resultMessage;
	}
	public void setResultMessage(String resultMessage) {
		this.resultMessage = resultMessage;
	}	
}
