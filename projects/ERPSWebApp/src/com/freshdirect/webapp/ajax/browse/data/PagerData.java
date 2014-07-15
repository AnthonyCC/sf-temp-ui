package com.freshdirect.webapp.ajax.browse.data;

import java.io.Serializable;

import com.freshdirect.fdstore.FDStoreProperties;


public class PagerData implements Serializable {

	private static final long serialVersionUID = -6775517691271591424L;
	
	public static int GRID_ITEM_COLUMN_PER_PAGE_THRESHOLD = 5;
//	public static int GRID_ITEM_ROW_PER_PAGE_THRESHOLD = 5;
	public static int GRID_ITEM_PER_PAGE_THRESHOLD = 30;

	private int pageCount;
	private boolean all;
	private int activePage = 1;
	private int itemCount;
	private int firstItemIndex;
	private int lastItemIndex;
	private int pageSize = FDStoreProperties.getBrowsePageSize();

	public int getPageSize() {
		return pageSize;
	}
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
	public int getPageCount() {
		return pageCount;
	}
	public void setPageCount(int pageCount) {
		this.pageCount = pageCount;
	}
	public int getActivePage() {
		return activePage;
	}
	public void setActivePage(int activePage) {
		this.activePage = activePage;
	}
	public int getFirstItemIndex() {
		return firstItemIndex;
	}
	public void setFirstItemIndex(int firstItemIndex) {
		this.firstItemIndex = firstItemIndex;
	}
	public int getLastItemIndex() {
		return lastItemIndex;
	}
	public void setLastItemIndex(int lastItemIndex) {
		this.lastItemIndex = lastItemIndex;
	}
	public int getItemCount() {
		return itemCount;
	}
	public void setItemCount(int itemCount) {
		this.itemCount = itemCount;
	}
	public boolean isAll() {
		return all;
	}
	public void setAll(boolean all) {
		this.all = all;
	}
}
