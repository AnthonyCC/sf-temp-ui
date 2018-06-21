package com.freshdirect.storeapi.content;

import java.util.List;

public class CMSWebPageModel{

	private String seoMetaDescription;
	private String title;
	private String type;
	private List<CMSSectionModel> sections;
	private List<CMSScheduleModel> schedule;
	
	private String id;

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
		
    public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((schedule == null) ? 0 : schedule.hashCode());
        result = prime * result + ((sections == null) ? 0 : sections.hashCode());
        result = prime * result + ((seoMetaDescription == null) ? 0 : seoMetaDescription.hashCode());
        result = prime * result + ((title == null) ? 0 : title.hashCode());
        result = prime * result + ((type == null) ? 0 : type.hashCode());
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        CMSWebPageModel other = (CMSWebPageModel) obj;
        if (schedule == null) {
            if (other.schedule != null)
                return false;
        } else if (!schedule.equals(other.schedule))
            return false;
        if (sections == null) {
            if (other.sections != null)
                return false;
        } else if (!sections.equals(other.sections))
            return false;
        if (seoMetaDescription == null) {
            if (other.seoMetaDescription != null)
                return false;
        } else if (!seoMetaDescription.equals(other.seoMetaDescription))
            return false;
        if (title == null) {
            if (other.title != null)
                return false;
        } else if (!title.equals(other.title))
            return false;
        if (type == null) {
            if (other.type != null)
                return false;
        } else if (!type.equals(other.type))
            return false;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }
}