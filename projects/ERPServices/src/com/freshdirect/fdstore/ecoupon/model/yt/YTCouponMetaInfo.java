package com.freshdirect.fdstore.ecoupon.model.yt;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class YTCouponMetaInfo implements Serializable{
	
	private String coupon_id;
	private String category;
	private String brand_name;
	private String manufacturer;
	private String short_description;
	private String long_description;
	private String long_description_header;
	private String requirement_description;
	private String priority;
	private String newItem;
	private String imagePath;
	private String[] tags;
	private String expiration_date;
	private boolean isExpired;
	private List<String> requirement_upcs;
	private List<String> reward_upcs;
	private String value;
	private String requirement_quantity;
	private String offer_type;
	private String enabled;
	
	public String getCoupon_id() {
		return coupon_id;
	}
	public void setCoupon_id(String coupon_id) {
		this.coupon_id = coupon_id;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public String getBrand_name() {
		return brand_name;
	}
	public void setBrand_name(String brand_name) {
		this.brand_name = brand_name;
	}
	public String getManufacturer() {
		return manufacturer;
	}
	public void setManufacturer(String manufacturer) {
		this.manufacturer = manufacturer;
	}
	public String getShort_description() {
		return short_description;
	}
	public void setShort_description(String short_description) {
		this.short_description = short_description;
	}
	public String getLong_description() {
		return long_description;
	}
	public void setLong_description(String long_description) {
		this.long_description = long_description;
	}
	public String getLong_description_header() {
		return long_description_header;
	}
	public void setLong_description_header(String long_description_header) {
		this.long_description_header = long_description_header;
	}
	public String getRequirement_description() {
		return requirement_description;
	}
	public void setRequirement_description(String requirement_description) {
		this.requirement_description = requirement_description;
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
	public String getExpiration_date() {
		return expiration_date;
	}
	public void setExpiration_date(String expiration_date) {
		this.expiration_date = expiration_date;
	}
	public boolean isExpired() {
		return isExpired;
	}
	public void setExpired(boolean isExpired) {
		this.isExpired = isExpired;
	}
	public List<String> getRequirement_upcs() {
		return requirement_upcs;
	}
	public void setRequirement_upcs(List<String> requirement_upcs) {
		this.requirement_upcs = requirement_upcs;
	}
	public List<String> getReward_upcs() {
		return reward_upcs;
	}
	public void setReward_upcs(List<String> reward_upcs) {
		this.reward_upcs = reward_upcs;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String getRequirement_quantity() {
		return requirement_quantity;
	}
	public void setRequirement_quantity(String requirement_quantity) {
		this.requirement_quantity = requirement_quantity;
	}
	public String getOffer_type() {
		return offer_type;
	}
	public void setOffer_type(String offer_type) {
		this.offer_type = offer_type;
	}
	public String getEnabled() {
		return enabled;
	}
	public void setEnabled(String enabled) {
		this.enabled = enabled;
	}
	public String getPriority() {
		return priority;
	}
	public void setPriority(String priority) {
		this.priority = priority;
	}
	
		
	
}
