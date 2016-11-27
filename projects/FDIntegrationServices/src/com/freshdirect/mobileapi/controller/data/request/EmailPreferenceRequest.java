package com.freshdirect.mobileapi.controller.data.request;

import com.freshdirect.mobileapi.controller.data.Message;

public class EmailPreferenceRequest extends Message {

	private String email_subscribed;

	public String getEmail_subscribed() {
		return email_subscribed;
	}

	public void setEmail_subscribed(String email_subscribed) {
		this.email_subscribed = email_subscribed;
	}
	
	
	
}
