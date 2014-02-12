package com.freshdirect.framework.event;


/**
 *  Enumeration for cartline event sources, that is, what part of the site
 *  was a product put into the cart.
 */
public enum EnumEventSource {
	UNKNOWN("Unknown"), /* The source of the event is not known */
	BROWSE("Browse"), /* The source of the event is the 'regular' site, looking at products, etc. */
	RECIPE("Recipe"), /* The source of the event is watching recipes. */
	QUICKSHOP("Quickshop"), /* The source of the event is the quickshop area. */
	TXYMAL("TxYmal"), /* The source if the event is the TX YMAL */
	CCL("CCL"), /* The source of the event is a customer created list. */
	SmartStore("SS"), /* The source of the event is SmartStore related. */
	QUICKBUY("Quickbuy"), /* The source of the event is Quickbuy related */
	CART("Cart"), /* The source of the event is the cart widget */	
	
	/* New quickshop event sources */
	qs_pastOrders("QS-past-orders"),
	qs_customerLists("QS-shopping-lists"),
	qs_fdLists("QS-FD-lists"),	
	qs_ymal("QS-top-recommender"),
	qs_tabbedRecommender("QS-bottom-recommender");
	
	String name;
	
	EnumEventSource(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
	public String toString() {
		return this.getName();
	}
}
