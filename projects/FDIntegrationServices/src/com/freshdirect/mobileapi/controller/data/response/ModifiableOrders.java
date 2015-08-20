package com.freshdirect.mobileapi.controller.data.response;

import java.util.ArrayList;
import java.util.List;

import com.freshdirect.mobileapi.controller.data.Message;

public class ModifiableOrders extends Message{
	
	List<ModifiableOrder> orders = new ArrayList<ModifiableOrder>();

	public List<ModifiableOrder> getOrders() {
		return orders;
	}

	public void setOrders(List<ModifiableOrder> orders) {
		this.orders = orders;
	}
	
	/**
	 * Adds the ModifiableOrder object to the list orders
	 * @param orderDetail
	
	 */
	public void addOrder(ModifiableOrder order) {
		orders.add(order);
	}

}
