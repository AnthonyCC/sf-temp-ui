package com.freshdirect.webapp.ajax.cart.data;

import java.io.Serializable;

public class AddToCartCouponResponse implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3890808545614027237L;
	
	private String atcId;
	private String message;
			
	public AddToCartCouponResponse(String atcId, String message) {
		super();
		this.atcId = atcId;
		this.message = message;
	}
	
	public String getAtcId() {
		return atcId;
	}
	public void setAtcIds(String atcId) {
		this.atcId = atcId;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}

}
