package com.freshdirect.delivery.restriction;

import java.io.Serializable;
import java.util.List;

public class GeographyRestrictionMessage implements Serializable {
	
	private String message;	
	
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	private String showMessage;
	
	public String getShowMessage() {
		return showMessage;
	}
	public void setShowMessage(String showMessage) {
		this.showMessage = showMessage;
	}
	
}
