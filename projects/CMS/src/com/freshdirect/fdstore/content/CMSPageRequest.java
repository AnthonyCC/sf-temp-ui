package com.freshdirect.fdstore.content;

import java.util.Date;

public class CMSPageRequest {
	private String pageType;
	private Date requestedDate;
	private boolean ignoreSchedule;
	private boolean preview;
	private String feedId;
	private String plantid;
	
	public String getPageType() {
		return pageType;
	}

	public void setPageType(String pageType) {
		this.pageType = pageType;
	}

	public Date getRequestedDate() {
		return requestedDate;
	}

	public void setRequestedDate(Date requestedDate) {
		this.requestedDate = requestedDate;
	}

	public boolean isIgnoreSchedule() {
		return ignoreSchedule;
	}

	public void setIgnoreSchedule(boolean ignoreSchedule) {
		this.ignoreSchedule = ignoreSchedule;
	}

	public boolean isPreview() {
		return preview;
	}

	public void setPreview(boolean preview) {
		this.preview = preview;
	}

	public String getFeedId() {
		return feedId;
	}

	public void setFeedId(String feedId) {
		this.feedId = feedId;
	}
	
	public String getPlantId() {
		return plantid;
	}

	public void setPlantId(String plantid) {
		this.plantid = plantid;
	}
	
	
}