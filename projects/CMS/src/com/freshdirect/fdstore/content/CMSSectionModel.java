package com.freshdirect.fdstore.content;

import java.util.List;

public class CMSSectionModel {
	private String name;
	private List<CMSComponentModel> components;
	private List<CMSScheduleModel> schedules;
	private String type;
	
	
	public List<CMSComponentModel> getComponents() {
		return components;
	}

	public void setComponents(List<CMSComponentModel> components) {
		this.components = components;
	}

	public List<CMSScheduleModel> getSchedules() {
		return schedules;
	}

	public void setSchedules(List<CMSScheduleModel> schedules) {
		this.schedules = schedules;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
