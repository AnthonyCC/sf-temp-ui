package com.freshdirect.webapp.ajax.recommendation;

public class RecommendationRequestObject {

	// SmartStore Site Feature ID (required)
	private String feature;
	private String tab;
	private String listId;
	private int numberOfItems=15;
	private String deptId;
	private String parentImpressionId;
	private String impressionId;
	private String parentVariantId;
	
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
	public String getParentImpressionId() {
		return parentImpressionId;
	}
	public void setParentImpressionId(String parentImpressionId) {
		this.parentImpressionId = parentImpressionId;
	}
	public String getImpressionId() {
		return impressionId;
	}
	public void setImpressionId(String impressionId) {
		this.impressionId = impressionId;
	}
	public String getParentVariantId() {
		return parentVariantId;
	}
	public void setParentVariantId(String parentVariantId) {
		this.parentVariantId = parentVariantId;
	}	
}
