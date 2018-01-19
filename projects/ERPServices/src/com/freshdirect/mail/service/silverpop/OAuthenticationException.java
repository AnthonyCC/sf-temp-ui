package com.freshdirect.mail.service.silverpop;

public class OAuthenticationException extends Exception {
	OAuthenticationException(Exception e){
		super (e);
	}
	OAuthenticationException(String  ex){
		super (ex);
	}

}
