package com.freshdirect.mobileapi.catalog.model;

import java.util.List;

import com.freshdirect.fdstore.EnumAvailabilityStatus;

public class SkuInfo {
	
	public static enum AlcoholType {
		NON_ALCOHOLIC,BEER,WINE_AND_SPIRITS;
	}
	private String productId;
	private  String skuCode;
	private double basePrice;
	private String rating;
	private String freshness;
	
	private GroupInfo groupInfo;
	
	private List<SalesUnit> salesUnits;
	
	private UnitPrice unitPrice;
	
	private List<ScalePrice> scalePrice;
	private int available;
	
		
	public int getAvailable() {
		return available;
	}
	public void setAvailable(int availabilityId) {
		
		this.available = availabilityId;
	}
	public List<ScalePrice> getScalePrice() {
		return scalePrice;
	}
	public void setScalePrice(List<ScalePrice> scalePrice) {
		this.scalePrice = scalePrice;
	}
	public List<SalesUnit> getSalesUnits() {
		return salesUnits;
	}
	public void setSalesUnits(List<SalesUnit> salesUnits) {
		this.salesUnits = salesUnits;
	}
	public UnitPrice getUnitPrice() {
		return unitPrice;
	}
	public void setUnitPrice(UnitPrice unitPrice) {
		this.unitPrice = unitPrice;
	}
	public GroupInfo getGroupInfo() {
		return groupInfo;
	}
	public void setGroupInfo(GroupInfo groupInfo) {
		this.groupInfo = groupInfo;
	}
	public String getProductId() {
		return productId;
	}
	public void setProductId(String productId) {
		this.productId = productId;
	}
	public String getSkuCode() {
		return skuCode;
	}
	public void setSkuCode(String skuCode) {
		this.skuCode = skuCode;
	}
	public double getBasePrice() {
		return basePrice;
	}
	public void setBasePrice(double basePrice) {
		this.basePrice = basePrice;
	}
	public String getRating() {
		return rating;
	}
	public void setRating(String rating) {
		this.rating = rating;
	}
	public String getFreshness() {
		return freshness;
	}
	public void setFreshness(String freshness) {
		this.freshness = freshness;
	}
	public boolean isTaxable() {
		return isTaxable;
	}
	public void setTaxable(boolean isTaxable) {
		this.isTaxable = isTaxable;
	}
	
	public String getTaxCode() {
		return taxCode;
	}
	public void setTaxCode(String taxCode) {
		this.taxCode = taxCode;
	}
	public String getAlcoholType() {
		return alcoholType.name();
	}
	public void setAlcoholType(AlcoholType alcoholType) {
		this.alcoholType = alcoholType;
	}
	public String getSustainabilityRating() {
		return sustainabilityRating;
	}
	public void setSustainabilityRating(String sustainabilityRating) {
		this.sustainabilityRating = sustainabilityRating;
	}
	private boolean isTaxable;
	private String taxCode;
	private AlcoholType alcoholType;
	private String sustainabilityRating;
	
	private boolean isLimitedQuantity;
	
	public void setLimitedQuantity(boolean isLimitedQuantity) {
		this.isLimitedQuantity = isLimitedQuantity;
	}
	public boolean isLimitedQuantity() {
		return this.isLimitedQuantity;
	}
	

}
