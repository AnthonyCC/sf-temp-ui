package com.freshdirect.mobileapi.controller.data.response;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import com.freshdirect.mobileapi.controller.data.Message;
import com.freshdirect.mobileapi.controller.data.OrderDetail;

public class Orders extends Message{
	
	List<OrderDetail> orders = new ArrayList<OrderDetail>();

	public List<OrderDetail> getOrders() {
		return orders;
	}

	public void setOrders(List<OrderDetail> orders) {
		this.orders = orders;
	}
	
	/**
	 * Adds the orderDetail object to the list orders
	 * @param orderDetail
	 * @throws ParseException 
	 */
	public void addOrder(com.freshdirect.mobileapi.controller.data.response.Order order, String orderId) throws ParseException{
		OrderDetail orderDetail = new OrderDetail();
		//Set all the info in order Object to orderDetail Object
		orderDetail.setCartDetail(order.getCartDetail());
		orderDetail.setDeliveryAddress(order.getDeliveryAddress());
		orderDetail.setDeliveryZone(order.getDeliveryZone());
		orderDetail.setModifiable(order.isModifiable());
		orderDetail.setModificationCutoffTime(order.getModificationCutoffTime());
		orderDetail.setOrderNumber(orderId);
		orderDetail.setPaymentMethod(order.getPaymentMethod());
		orderDetail.setReservationDateTime(order.getReservationDateTime());
		orderDetail.setReservationTimeRange(order.getReservationTimeRange());
		orderDetail.setStatus(order.getStatus());
		orderDetail.setModifycount(order.getModifycount());
		
		//Add the orderDetail Object to the list
		this.orders.add(orderDetail);
	}

}
