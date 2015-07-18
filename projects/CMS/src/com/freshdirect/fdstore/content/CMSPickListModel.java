package com.freshdirect.fdstore.content;

import java.util.List;

public class CMSPickListModel extends CMSPickListItemModel{
	private boolean displayName;
	private String description;
	private CMSImageModel image;
	private List<CMSScheduleModel> schedules;
	private List<CMSPickListItemModel> items;
	
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
	public CMSImageModel getImage() {
		return image;
	}
	public void setImage(CMSImageModel image) {
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
}