package com.freshdirect.fdstore;

import java.io.Serializable;

import com.freshdirect.framework.core.ModelSupport;

public class FDProductPromotionInfo extends ModelSupport {

	private int version;
	private String zoneId;
	private String skuCode;
	private String matNumber;
	private String erpDeptId;
	private boolean isFeatured;
	private String featuredHeader;
	private int priority;
	private String type;
	public int getVersion() {
		return version;
	}
	public void setVersion(int version) {
		this.version = version;
	}
	public String getZoneId() {
		return zoneId;
	}
	public void setZoneId(String zoneId) {
		this.zoneId = zoneId;
	}
	public String getSkuCode() {
		return skuCode;
	}
	public void setSkuCode(String skuCode) {
		this.skuCode = skuCode;
	}
	public String getMatNumber() {
		return matNumber;
	}
	public void setMatNumber(String matNumber) {
		this.matNumber = matNumber;
	}
	public String getErpDeptId() {
		return erpDeptId;
	}
	public void setErpDeptId(String erpDeptId) {
		this.erpDeptId = erpDeptId;
	}
	public boolean isFeatured() {
		return isFeatured;
	}
	public void setFeatured(boolean isFeatured) {
		this.isFeatured = isFeatured;
	}
	public String getFeaturedHeader() {
		return featuredHeader;
	}
	public void setFeaturedHeader(String featuredHeader) {
		this.featuredHeader = featuredHeader;
	}
	public int getPriority() {
		return priority;
	}
	public void setPriority(int priority) {
		this.priority = priority;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	
	
}
