package com.freshdirect.erp;

import com.freshdirect.framework.core.ModelSupport;
/**
 * 
 * @author ksriram
 *
 */
public class ErpProductPromotionInfo extends ModelSupport {

	private static final long serialVersionUID = 1L;
	
	private int version;
	private String zoneId;
	private String skuCode;
	private String matNumber;
	private String erpDeptId;
	private String featured;
	private String erpCategory;
	private int erpCatPosition;
	private String erpPromtoionId;
	private String salesOrg;
	private String distChannel;
	
	public String getFeatured() {
		return featured;
	}
	public void setFeatured(String featured) {
		this.featured = featured;
	}
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
	public String getErpCategory() {
		return erpCategory;
	}
	public void setErpCategory(String erpCategory) {
		this.erpCategory = erpCategory;
	}
	public int getErpCatPosition() {
		return erpCatPosition;
	}
	public void setErpCatPosition(int erpCatPosition) {
		this.erpCatPosition = erpCatPosition;
	}
	public String getErpPromtoionId() {
		return erpPromtoionId;
	}
	public void setErpPromtoionId(String erpPromtoionId) {
		this.erpPromtoionId = erpPromtoionId;
	}	
	public String getSalesOrg() {
		return salesOrg;
	}	
	public void setSalesOrg(String salesOrg) {
		this.salesOrg = salesOrg;
	}	
	public String getDistChannel() {
		return distChannel;
	}	
	public void setDistChannel(String distChannel) {
		this.distChannel = distChannel;
	}
}
