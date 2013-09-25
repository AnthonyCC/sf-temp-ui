package com.freshdirect.webapp.ajax.quickshop.data;

public class QuickShopYmalRequestObject {

	// SmartStore Site Feature ID (required)
	private String feature;
	private String tab;
	private String listId;
	private int numberOfItems=15;
	private String deptId;
	
	public String getTab() {
		return tab;
	}
	public void setTab(String tab) {
		this.tab = tab;
	}
	public String getListId() {
		return listId;
	}
	public void setListId(String listId) {
		this.listId = listId;
	}	
	public void setFeature(String feature) {
		this.feature = feature;
	}
	public String getFeature() {
		return feature;
	}
	public int getNumberOfItems() {
		return numberOfItems;
	}
	public void setNumberOfItems(int numberOfItems) {
		this.numberOfItems = numberOfItems;
	}	
	public String getDeptId() {
		return deptId;
	}	
	public void setDeptId( String deptId ) {
		this.deptId = deptId;
	}	
}
