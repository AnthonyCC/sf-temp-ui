package com.freshdirect.mobileapi.controller.data.request;

import java.util.List;

public class OrdersDetailRequest {
	List<String> orders;

	public List<String> getOrders() {
		return orders;
	}

	public void setOrders(List<String> orders) {
		this.orders = orders;
	}
	
	boolean dlvPassCart;

	public boolean isDlvPassCart() {
		return dlvPassCart;
	}

	public void setDlvPassCart(boolean dlvPassCart) {
		this.dlvPassCart = dlvPassCart;
	}
	

}
