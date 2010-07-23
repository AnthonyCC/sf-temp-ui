package com.freshdirect.webapp.taglib.promotion;

public class PromoFilterCriteria {

	private String offerType;
	private String customerType;
	private String promoStatus;
	private String createdBy;
	private String modifiedBy;
	private String keyword;
	
	public PromoFilterCriteria() {
		super();
	}
	public PromoFilterCriteria(String offerType, String customerType,
			String promoStatus, String createdBy, String modifiedBy,
			String keyword) {
		super();
		this.offerType = offerType;
		this.customerType = customerType;
		this.promoStatus = promoStatus;
		this.createdBy = createdBy;
		this.modifiedBy = modifiedBy;
		this.keyword = keyword;
	}
	public String getOfferType() {
		return offerType;
	}
	public void setOfferType(String offerType) {
		this.offerType = offerType;
	}
	public String getCustomerType() {
		return customerType;
	}
	public void setCustomerType(String customerType) {
		this.customerType = customerType;
	}
	public String getPromoStatus() {
		return promoStatus;
	}
	public void setPromoStatus(String promoStatus) {
		this.promoStatus = promoStatus;
	}
	public String getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	public String getModifiedBy() {
		return modifiedBy;
	}
	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}
	public String getKeyword() {
		return keyword;
	}
	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}
	
	public boolean isEmpty(){
		boolean isEmtpy = true;
		if((null != offerType && !offerType.trim().equalsIgnoreCase("")) 
				|| (null != customerType && !customerType.trim().equalsIgnoreCase(""))
				|| (null != promoStatus && !promoStatus.trim().equalsIgnoreCase("")) 
				|| (null != createdBy && !createdBy.trim().equalsIgnoreCase("")) 
				|| (null != modifiedBy && !modifiedBy.trim().equalsIgnoreCase("")) 
				|| (null != keyword && !keyword.trim().equalsIgnoreCase(""))){
			isEmtpy = false;
		}
		return isEmtpy;
	}
}
