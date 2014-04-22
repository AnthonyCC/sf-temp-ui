package com.freshdirect.webapp.ajax.cart.data;

import java.io.Serializable;
import java.util.List;

import com.freshdirect.webapp.ajax.quickshop.data.QuickShopLineItem;

public class PendingPopupData implements Serializable {
	
	private static final long	serialVersionUID	= -187337745204915510L;
	
	private List<PendingPopupOrderInfo> orderInfos;
	private List<QuickShopLineItem> pendingItems;
	private List<QuickShopLineItem> cartData;
	
	private String eventSource;
	
	
	public List<PendingPopupOrderInfo> getOrderInfos() {
		return orderInfos;
	}
	public void setOrderInfos(List<PendingPopupOrderInfo> orderInfos) {
		this.orderInfos = orderInfos;
	}
	public List<QuickShopLineItem> getPendingItems() {
		return pendingItems;
	}
	public void setPendingItems(List<QuickShopLineItem> pendingItems) {
		this.pendingItems = pendingItems;
	}
	public List<QuickShopLineItem> getCartData() {
		return cartData;
	}
	public void setCartData(List<QuickShopLineItem> cartData) {
		this.cartData = cartData;
	}
	public String getEventSource() {
		return eventSource;
	}
	public void setEventSource(String eventSource) {
		this.eventSource = eventSource;
	}

}
