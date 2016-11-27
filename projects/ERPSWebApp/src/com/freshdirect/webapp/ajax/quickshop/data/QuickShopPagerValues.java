package com.freshdirect.webapp.ajax.quickshop.data;

import java.io.Serializable;

public class QuickShopPagerValues implements Serializable {
	
	private static final long	serialVersionUID	= 8516839336642121634L;
	
	private int pageSize;
	private int itemCount;
	private int activePage;
	
	public QuickShopPagerValues(int pageSize, int itemCount, int activePage) {
		super();
		this.pageSize = pageSize;
		this.itemCount = itemCount;
		this.activePage = activePage;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public int getItemCount() {
		return itemCount;
	}

	public void setItemCount(int itemCount) {
		this.itemCount = itemCount;
	}

	public int getActivePage() {
		return activePage;
	}

	public void setActivePage(int activePage) {
		this.activePage = activePage;
	}

}
