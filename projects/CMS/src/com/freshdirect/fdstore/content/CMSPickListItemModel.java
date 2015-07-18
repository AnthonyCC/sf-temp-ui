package com.freshdirect.fdstore.content;

public class CMSPickListItemModel extends CMSComponentModel{
	private String product;
	private boolean defaultFlag;
	
	public String getProduct() {
		return product;
	}
	public void setProduct(String product) {
		this.product = product;
	}
	public boolean isDefaultFlag() {
		return defaultFlag;
	}
	public void setDefaultFlag(boolean defaultFlag) {
		this.defaultFlag = defaultFlag;
	}
}