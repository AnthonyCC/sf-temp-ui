package com.freshdirect.fdstore.content;

import java.util.List;

public class CMSPickListModel extends CMSPickListItemModel{
	private boolean displayName;
	private String description;
	private CMSImageBannerModel image;
	private List<CMSScheduleModel> schedules;
	private List<CMSPickListItemModel> items;
	private List<String> products;
	private List<String> categories;
	
	public boolean isDisplayName() {
		return displayName;
	}
	public void setDisplayName(boolean displayName) {
		this.displayName = displayName;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public CMSImageBannerModel getImage() {
		return image;
	}
	public void setImage(CMSImageBannerModel image) {
		this.image = image;
	}
	public List<CMSScheduleModel> getSchedules() {
		return schedules;
	}
	public void setSchedules(List<CMSScheduleModel> schedules) {
		this.schedules = schedules;
	}
	public List<CMSPickListItemModel> getItems() {
		return items;
	}
	public void setItems(List<CMSPickListItemModel> items) {
		this.items = items;
	}
	public List<String> getProducts() {
		return products;
	}
	public void setProducts(List<String> products) {
		this.products = products;
	}
	public List<String> getCategories() {
		return categories;
	}
	public void setCategories(List<String> categories) {
		this.categories = categories;
	}
}