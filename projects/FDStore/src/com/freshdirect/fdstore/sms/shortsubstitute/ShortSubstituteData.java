package com.freshdirect.fdstore.sms.shortsubstitute;

import java.io.Serializable;
public class ShortSubstituteData implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String orderId;
	private String substituteItem;
	private String shortItem;
	
	
	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getSubstituteItem() {
		return substituteItem;
	}

	public void setSubstituteItem(String substituteItem) {
		this.substituteItem = substituteItem;
	}

	public String getShortItem() {
		return shortItem;
	}

	public void setShortItem(String shortItem) {
		this.shortItem = shortItem;
	}


}
