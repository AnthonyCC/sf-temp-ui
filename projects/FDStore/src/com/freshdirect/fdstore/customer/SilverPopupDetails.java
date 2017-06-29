package com.freshdirect.fdstore.customer;

import java.io.Serializable;

public class SilverPopupDetails implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6206865101358715497L;

	private String customerId;
	
	private String channel;
	
	private String qualifier;
	
	private String destination;	
	
	public String getCustomerId() {
		return customerId;
	}
	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}
	public String getChannel() {
		return channel;
	}
	public void setChannel(String channel) {
		this.channel = channel;
	}
	public String getQualifier() {
		return qualifier;
	}
	public void setQualifier(String qualifier) {
		this.qualifier = qualifier;
	}
	public String getDestination() {
		return destination;
	}
	public void setDestination(String destination) {
		this.destination = destination;
	}
}
