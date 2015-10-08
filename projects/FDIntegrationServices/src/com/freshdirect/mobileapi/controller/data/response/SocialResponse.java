package com.freshdirect.mobileapi.controller.data.response;

import com.freshdirect.mobileapi.controller.data.Message;

public class SocialResponse extends Message {
	private boolean loggedInSuccess;
	private String resultAction;
    private String resultMessage;
    
    public static final String MESSAGE_RESULT_MESSAGE_ACCOUNT_EXIST_SIGNIN = "You have an existing account. You are signed in";
    public static final String MESSAGE_RESULT_MESSAGE_AUTO_SIGNIN = "Auto Sign In";
    public static final String MESSAGE_RESULT_MESSAGE_EXISTING_LINK_SIGNIN = "You have an existing account. It is now linked to your Social account.";
    public static final String MESSAGE_RESULT_MESSAGE_LINKED = "Your Social account linked successfully.";
    public static final String MESSAGE_RESULT_MESSAGE_UNLINKED = "Your Social account unlinked successfully.";
    public static final String MESSAGE_RESULT_MESSAGE_NO_ACCOUNT_CONNECTED = "No FreshDirect Account is connected to this Social account";
    public static final String MESSAGE_RESULT_MESSAGE_ACCOUNT_CREATED = "Created a new account using your Social account.";
    public static final String MESSAGE_ACTION_SIGNEDIN ="SIGNEDIN";
    public static final String MESSAGE_ACTION_LINKED ="LINKED";
    public static final String MESSAGE_ACTION_UNLINKED ="UNLINKED";
    public static final String MESSAGE_ACTION_NOMATCH ="NOMATCH";
    
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
