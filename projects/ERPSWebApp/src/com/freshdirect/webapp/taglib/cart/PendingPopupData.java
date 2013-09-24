package com.freshdirect.webapp.taglib.cart;

import java.util.List;

import com.freshdirect.webapp.quickshop.contentmodels.QuickShopLineItem;

public class PendingPopupData {
	
	private List<PendingPopupOrderInfo> orderInfos;
	private List<QuickShopLineItem> pendingItems;
	private List<QuickShopLineItem> cartData;
	
	
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

}
