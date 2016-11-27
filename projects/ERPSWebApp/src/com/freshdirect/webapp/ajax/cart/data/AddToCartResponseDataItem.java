package com.freshdirect.webapp.ajax.cart.data;

import java.io.Serializable;

public class AddToCartResponseDataItem implements Serializable {
	
	private static final long	serialVersionUID	= 5260328381889774679L;
	
	private String itemId;
	private double inCartAmount;
	private String message;
	private Status status;
	private int cartlineId;
	private String productId;
	private String categoryId;
	
	
	public AddToCartResponseDataItem() {		
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
	
	public int getCartlineId() {
		return cartlineId;
	}
	
	public void setCartlineId( int cartlineId ) {
		this.cartlineId = cartlineId;
	}
	
	public String getProductId() {
		return productId;
	}
	
	public void setProductId( String productId ) {
		this.productId = productId;
	}
	
	public String getCategoryId() {
		return categoryId;
	}
	
	public void setCategoryId( String categoryId ) {
		this.categoryId = categoryId;
	}


	public static enum Status {
		SUCCESS, WARNING, ERROR;
	}


}
