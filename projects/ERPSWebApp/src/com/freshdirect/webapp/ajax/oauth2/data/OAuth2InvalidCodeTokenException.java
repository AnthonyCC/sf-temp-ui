package com.freshdirect.webapp.ajax.oauth2.data;

public class OAuth2InvalidCodeTokenException extends Exception {
	private static final long serialVersionUID = 724460872770388877L;
	
	public OAuth2InvalidCodeTokenException(String message) {
		super(message);
	}
}
