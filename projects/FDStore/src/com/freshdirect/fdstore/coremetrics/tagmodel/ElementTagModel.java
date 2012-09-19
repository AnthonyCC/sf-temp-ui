package com.freshdirect.fdstore.coremetrics.tagmodel;


public class ElementTagModel extends AbstractTagModel  {
	private String elementId;
	private String elementCategory;
	
	public String getElementId() {
		return elementId;
	}
	
	public void setElementId(String elementId) {
		this.elementId = elementId;
	}
	
	public String getElementCategory() {
		return elementCategory;
	}

	public void setElementCategory(String elementCategory) {
		this.elementCategory = elementCategory;
	}
}