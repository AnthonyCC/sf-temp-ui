package com.freshdirect.webapp.ajax.login;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.freshdirect.framework.webapp.ActionError;

public class LoginResponse implements Serializable{
	
	private boolean success = false;	
	private Map<String,String> errorMessages;	
	private String message;
	private String successPage;
	
	public LoginResponse() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public String getSuccessPage() {
		return successPage;
	}

	public void setSuccessPage(String successPage) {
		this.successPage = successPage;
	}

	public boolean isSuccess() {
		return success;
	}
	public void setSuccess(boolean isSuccess) {
		this.success = isSuccess;
	}
	public Map<String, String> getErrorMessages() {
		return errorMessages;
	}
	public void setErrorMessages(Map<String, String> errorMessages) {
		this.errorMessages = errorMessages;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}	
	public void addError(String errorCode, String errorDetail){
		if(null == errorMessages){
			errorMessages = new HashMap<String,String>();
		}
		errorMessages.put(errorCode, errorDetail);
	}
	
	public void addError(ActionError actionError){
		if(null == errorMessages){
			errorMessages = new HashMap<String,String>();
		}
		errorMessages.put(actionError.getType(), actionError.getDescription());
	}
	
	public void addErrors(Collection<ActionError> actionErrors){
		if(null == errorMessages){
			errorMessages = new HashMap<String,String>();
		}
		if(null != actionErrors){
			for (Iterator iterator = actionErrors.iterator(); iterator
					.hasNext();) {
				ActionError actionError = (ActionError) iterator.next();
				errorMessages.put(actionError.getType(), actionError.getDescription());
			}
		}
		
	}

}
