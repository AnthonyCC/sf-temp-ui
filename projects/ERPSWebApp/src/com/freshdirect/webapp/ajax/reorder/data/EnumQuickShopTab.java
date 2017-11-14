package com.freshdirect.webapp.ajax.reorder.data;

import com.freshdirect.cms.cache.CmsCaches;

public enum EnumQuickShopTab {
    TOP_ITEMS(CmsCaches.QS_TOP_ITEMS_CACHE.cacheName),
    PAST_ORDERS(CmsCaches.QS_PAST_ORDERS_CACHE.cacheName),
    CUSTOMER_LISTS(CmsCaches.QS_SHOP_FROM_LISTS_CACHE.cacheName),
	FD_LISTS(null);

	public String cacheName;
	
	private EnumQuickShopTab(String cacheName) {
		this.cacheName = cacheName;
	}
}
