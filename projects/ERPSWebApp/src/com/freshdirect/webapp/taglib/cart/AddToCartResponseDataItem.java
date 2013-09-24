package com.freshdirect.webapp.taglib.cart;

public class AddToCartResponseDataItem {
	
	private String itemId;
	private double inCartAmount;
	private String message;
	private Status status;
	
	
	public AddToCartResponseDataItem(){
		
	}
	
	public AddToCartResponseDataItem(String itemId, double inCartAmount, String message) {
		super();
		this.itemId = itemId;
		this.inCartAmount = inCartAmount;
		this.message = message;
	}

	public String getItemId() {
		return itemId;
	}

	public void setItemId(String itemId) {
		this.itemId = itemId;
	}

	public double getInCartAmount() {
		return inCartAmount;
	}

	public void setInCartAmount(double inCartAmount) {
		this.inCartAmount = inCartAmount;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
	public Status getStatus() {
		return status;
	}

	public void setStatus( Status status ) {
		this.status = status;
	}


	public static enum Status {
		SUCCESS, WARNING, ERROR;
	}
}
