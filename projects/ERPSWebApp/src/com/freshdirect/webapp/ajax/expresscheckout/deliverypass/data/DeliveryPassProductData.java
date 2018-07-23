package com.freshdirect.webapp.ajax.expresscheckout.deliverypass.data;

import com.freshdirect.webapp.ajax.product.data.ProductData;


public class DeliveryPassProductData {

	private String id;
	private String title;
	private String saving;
	private double totalPrice;
	private String pricePerMonth;
	private String description;
	private boolean selected;
	private ProductData product;
	private boolean isMidWeekSku;
	private Integer duration; //no:of days for unlimited pass to expire.
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getSaving() {
		return saving;
	}

	public void setSaving(String saving) {
		this.saving = saving;
	}

	public double getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(double totalPrice) {
		this.totalPrice = totalPrice;
	}

	public String getPricePerMonth() {
		return pricePerMonth;
	}

	public void setPricePerMonth(String pricePerMonth) {
		this.pricePerMonth = pricePerMonth;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}

	public ProductData getProduct() {
		return product;
	}

	public void setProduct(ProductData product) {
		this.product = product;
	}

	public boolean isMidWeekSku() {
		return isMidWeekSku;
	}

	public void setMidWeekSku(boolean isMidWeekSku) {
		this.isMidWeekSku = isMidWeekSku;
	}

	public Integer getDuration() {
		return duration;
	}

	public void setDuration(Integer duration) {
		this.duration = duration;
	}

}
