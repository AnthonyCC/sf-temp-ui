package com.freshdirect.fdstore.content;

import java.util.List;

public class CMSWebPageModel{

	private String seoMetaDescription;
	private String title;
	private String type;
	private List<CMSSectionModel> sections;
	private List<CMSScheduleModel> schedule;

	public List<CMSSectionModel> getSections() {
		return sections;
	}

	public void setSections(List<CMSSectionModel> sections) {
		this.sections = sections;
	}

	public String getTitle() {
		return title;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}

	public String getSeoMetaDescription() {
		return seoMetaDescription;
	}

	public void setSeoMetaDescription(String seoMetaDescription) {
		this.seoMetaDescription = seoMetaDescription;
	}

	public List<CMSScheduleModel> getSchedule() {
		return schedule;
	}

	public void setSchedule(List<CMSScheduleModel> schedule) {
		this.schedule = schedule;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
}