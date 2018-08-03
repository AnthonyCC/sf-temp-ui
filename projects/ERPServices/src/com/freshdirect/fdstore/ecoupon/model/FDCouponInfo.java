package com.freshdirect.fdstore.ecoupon.model;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.freshdirect.FDCouponProperties;
import com.freshdirect.common.date.SimpleDateDeserializer;
import com.freshdirect.fdstore.ecoupon.EnumCouponOfferType;
import com.freshdirect.framework.core.ModelSupport;
import com.freshdirect.framework.util.DateUtil;


public class FDCouponInfo extends ModelSupport{

	private static final long serialVersionUID = -762178378399009645L;
	private int version;
	private String couponId;
	private String category;
	private String brandName;
	private String manufacturer;
	private String shortDescription;
	private String longDescription;
	private String longDescriptionHeader;
	private String requirementDescription;
	private String offerPriority;
	private String newItem;
	private String imagePath;
	private String[] tags;
	private String startDate;
	@JsonDeserialize(using = SimpleDateDeserializer.class)
	private Date expirationDate;
	private boolean isExpired;
	private List<FDCouponUPCInfo> requiredUpcs;	
	private String value;
	private String requiredQuantity;
	private Date createdTime;
	private Date lastUpdatedTime;
	private EnumCouponOfferType offerType;
	private String enabled;
	private Date fdExpirationDate;//Coupon exp date + grace period.
		
	public String getCouponId() {
		return couponId;
	}
	public void setCouponId(String couponId) {
		this.couponId = couponId;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public String getBrandName() {
		return brandName;
	}
	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}
	public String getManufacturer() {
		return manufacturer;
	}
	public void setManufacturer(String manufacturer) {
		this.manufacturer = manufacturer;
	}
	public String getShortDescription() {
		return shortDescription;
	}
	public void setShortDescription(String shortDescription) {
		this.shortDescription = shortDescription;
	}
	public String getLongDescription() {
		return longDescription;
	}
	public void setLongDescription(String longDescription) {
		this.longDescription = longDescription;
	}
	public String getLongDescriptionHeader() {
		return longDescriptionHeader;
	}
	public void setLongDescriptionHeader(String longDescriptionHeader) {
		this.longDescriptionHeader = longDescriptionHeader;
	}
	public String getRequirementDescription() {
		return requirementDescription;
	}
	public void setRequirementDescription(String requirementDescription) {
		this.requirementDescription = requirementDescription;
	}
	public String getOfferPriority() {
		return offerPriority;
	}
	public void setOfferPriority(String offerPriority) {
		this.offerPriority = offerPriority;
	}
	public String getNewItem() {
		return newItem;
	}
	public void setNewItem(String newItem) {
		this.newItem = newItem;
	}
	public String getImagePath() {
		return imagePath;
	}
	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}
	public String[] getTags() {
		return tags;
	}
	public void setTags(String[] tags) {
		this.tags = tags;
	}
	public Date getExpirationDate() {
		return expirationDate;
	}
	public void setExpirationDate(Date expirationDate) {
		this.expirationDate = expirationDate;		
		setFdExpirationDate();
	}
	private void setFdExpirationDate() {
		if(null !=this.expirationDate){
			this.fdExpirationDate = DateUtil.addDays(this.expirationDate, FDCouponProperties.getExpiredCouponsGracePeriod());
		}		
	}
	
	public boolean isExpired() {
		return isExpired;
	}
	public void setExpired(boolean isExpired) {
		this.isExpired = isExpired;
	}
	public List<FDCouponUPCInfo> getRequiredUpcs() {
		return requiredUpcs;
	}
	public void setRequiredUpcs(List<FDCouponUPCInfo> requiredUpcs) {
		this.requiredUpcs = requiredUpcs;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String getRequiredQuantity() {
		return requiredQuantity;
	}
	public void setRequiredQuantity(String requiredQuantity) {
		this.requiredQuantity = requiredQuantity;
	}
	public Date getCreatedTime() {
		return createdTime;
	}
	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}
	public Date getLastUpdatedTime() {
		return lastUpdatedTime;
	}
	public void setLastUpdatedTime(Date lastUpdatedTime) {
		this.lastUpdatedTime = lastUpdatedTime;
	}
	/**
	 * @return the version
	 */
	public int getVersion() {
		return version;
	}
	/**
	 * @param version the version to set
	 */
	public void setVersion(int version) {
		this.version = version;
	}
	
	public EnumCouponOfferType getOfferType() {
		return offerType;
	}
	public void setOfferType(EnumCouponOfferType offerType) {
		this.offerType = offerType;
	}
	@Override
	public String toString() {
		return "FDCouponInfo [version=" + version + ", couponId=" + couponId
				+ ", category=" + category + ", brandName=" + brandName
				+ ", manufacturer=" + manufacturer + ", shortDescription="
				+ shortDescription + ", longDescription=" + longDescription
				+ ", longDescriptionHeader=" + longDescriptionHeader
				+ ", requirementDescription=" + requirementDescription
				+ ", offerPriority=" + offerPriority + ", tags="
				+ Arrays.toString(tags) + ", expirationDate=" + expirationDate
				+ ", value=" + value + ", requiredQuantity=" + requiredQuantity
				+ ", offerType=" + offerType + "]";
	}
	public String getStartDate() {
		return startDate;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	public String getEnabled() {
		return enabled;
	}
	public void setEnabled(String enabled) {
		this.enabled = enabled;
	}
	
	public boolean isEnabled(){
		return "Y".equalsIgnoreCase(enabled);
	}
	public Date getFdExpirationDate() {
		return fdExpirationDate;
	}
	
	
	
}
