package com.freshdirect.webapp.taglib.cart;

import java.io.Serializable;
import java.util.List;

/**
 *	Simple java bean for add to cart requests. 
 *	Class structure is representing the received JSON structure. 	
 * 
 * @author treer
 */
public class AddToCartRequestData implements Serializable {
	
	private static final long	serialVersionUID	= -4196044006826787164L;
	
	private List<AddToCartItem> items;
	private String orderId;
	private boolean newOrder;
	private String eventSource;
	private String siteFeature;
	private String variantId;
	private String tab;
	
	public List<AddToCartItem> getItems() {
		return items;
	}	
	public void setItems( List<AddToCartItem> items ) {
		this.items = items;
	}
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public boolean isNewOrder() {
		return newOrder;
	}
	public void setNewOrder(boolean newOrder) {
		this.newOrder = newOrder;
	}	
	public String getEventSource() {
		return eventSource;
	}	
	public void setEventSource( String eventSource ) {
		this.eventSource = eventSource;
	}	
	public String getVariantId() {
		return variantId;
	}	
	public void setVariantId( String variantId ) {
		this.variantId = variantId;
	}	
	public String getTab() {
		return tab;
	}
	public void setTab(String tab) {
		this.tab = tab;
	}	
	public String getSiteFeature() {
		return siteFeature;
	}	
	public void setSiteFeature( String siteFeature ) {
		this.siteFeature = siteFeature;
	}
}
