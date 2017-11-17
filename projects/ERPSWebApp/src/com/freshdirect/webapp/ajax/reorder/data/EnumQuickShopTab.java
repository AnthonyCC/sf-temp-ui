package com.freshdirect.webapp.ajax.reorder.data;

import com.freshdirect.fdstore.cache.EhCacheUtil;

public enum EnumQuickShopTab {
	TOP_ITEMS(EhCacheUtil.QS_TOP_ITEMS_CACHE_NAME),
	PAST_ORDERS(EhCacheUtil.QS_PAST_ORDERS_CACHE_NAME),
	CUSTOMER_LISTS(EhCacheUtil.QS_SHOP_FROM_LISTS_CACHE_NAME),
	FD_LISTS(null);

	public String cacheName;
	
	private EnumQuickShopTab(String cacheName) {
		this.cacheName = cacheName;
	}
}
