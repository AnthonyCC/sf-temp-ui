package com.freshdirect.fdstore.standingorders.service;

public class InventoryMapInfoBean {
	
	private String productName;
	private String skuCode;
	private String materialNum;
	private double unavailQnty = 0.0;
	public String getProductName() {
		return productName;
	}
	public String getSkuCode() {
		return skuCode;
	}
	public String getMaterialNum() {
		return materialNum;
	}
	public double getUnavailQnty() {
		return unavailQnty;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public void setSkuCode(String skuCode) {
		this.skuCode = skuCode;
	}
	public void setMaterialNum(String materialNum) {
		this.materialNum = materialNum;
	}
	public void setUnavailQnty(double unavailQnty) {
		this.unavailQnty = unavailQnty;
	}

}
