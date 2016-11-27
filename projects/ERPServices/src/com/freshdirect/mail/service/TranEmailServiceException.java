package com.freshdirect.mail.service;

public class TranEmailServiceException extends Exception{

	private String errorType;
	private Exception e;  
	
	
	
	public TranEmailServiceException(String type,Exception e){
		this.errorType=type;
		this.e=e;
	}
	
	public String getErrorDetails(){
		String message="";
		if(e==null) return null;
		if(e.getMessage()!=null && e.getMessage().length()>500) message=e.getMessage().substring(0,500);
		else message=e.getMessage();	
			
		return message;
	}
	
	public String getErrorType(){
		if(errorType==null ||errorType.trim().length()==0) return "EXTERNAL";
		else return errorType;
	}
	
	
}
