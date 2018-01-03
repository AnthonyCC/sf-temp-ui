package com.freshdirect.mail.service;

public class SilverpopPostReturnValue {
	private final int retcode;
	private final String postReturnBody;
	
	SilverpopPostReturnValue(int code, String body){
			this.retcode = code;
			this.postReturnBody = body;
		
	}
	
	public int getRetcode() {
		return retcode;
	}

	public String getPostReturnBody() {
		return postReturnBody;
	}
	

}
