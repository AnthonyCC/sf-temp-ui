package com.freshdirect.mktAdmin.exception;

import java.text.MessageFormat;
import java.util.List;
import java.util.ResourceBundle;



public class MktAdminApplicationException extends Exception {
	private static final String messageFileName="messages";
	private static final String ERROR_PREFIX="app.error.";
    private Exception causedException;
    private String errorCode;
    private String placeHolders[]=null;
    private List exceptionList=null;
    
	public MktAdminApplicationException(String errorCode, String args[]){		
		this.errorCode=errorCode;
		this.placeHolders =args;
	}
     
    
	public MktAdminApplicationException(Exception e){
		this.causedException=e;
	}
	
	public MktAdminApplicationException(List list){
		this.exceptionList=list;
	}

	
	public String getErrorCode(){
		return ERROR_PREFIX+this.errorCode;
	}


	public String[] getPlaceHolders() {
		return placeHolders;
	}
		
	public String getMessage()
	{
		try{
	        ResourceBundle bundle=ResourceBundle.getBundle(messageFileName);
	        String message=bundle.getString(ERROR_PREFIX+errorCode);
	        MessageFormat format=new MessageFormat(message);
	        return format.format(this.placeHolders);
		}catch(java.util.MissingResourceException exception){
			if(exceptionList!=null && exceptionList.size()>0){
				// lot of errors so skip this one
				return "";
			}else{
				// unknown error code throw system exception
				throw new MktAdminSystemException("1001",exception);
			}	
			
		}
		
	}


	public List getExceptionList() {
		return exceptionList;
	}


	
}
