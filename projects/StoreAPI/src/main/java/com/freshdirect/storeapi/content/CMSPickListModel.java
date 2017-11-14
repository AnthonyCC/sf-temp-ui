package com.freshdirect.storeapi.content;

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

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((categories == null) ? 0 : categories.hashCode());
        result = prime * result + ((description == null) ? 0 : description.hashCode());
        result = prime * result + (displayName ? 1231 : 1237);
        result = prime * result + ((image == null) ? 0 : image.hashCode());
        result = prime * result + ((items == null) ? 0 : items.hashCode());
        result = prime * result + ((products == null) ? 0 : products.hashCode());
        result = prime * result + ((schedules == null) ? 0 : schedules.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!super.equals(obj))
            return false;
        if (getClass() != obj.getClass())
            return false;
        CMSPickListModel other = (CMSPickListModel) obj;
        if (categories == null) {
            if (other.categories != null)
                return false;
        } else if (!categories.equals(other.categories))
            return false;
        if (description == null) {
            if (other.description != null)
                return false;
        } else if (!description.equals(other.description))
            return false;
        if (displayName != other.displayName)
            return false;
        if (image == null) {
            if (other.image != null)
                return false;
        } else if (!image.equals(other.image))
            return false;
        if (items == null) {
            if (other.items != null)
                return false;
        } else if (!items.equals(other.items))
            return false;
        if (products == null) {
            if (other.products != null)
                return false;
        } else if (!products.equals(other.products))
            return false;
        if (schedules == null) {
            if (other.schedules != null)
                return false;
        } else if (!schedules.equals(other.schedules))
            return false;
        return true;
    }
}