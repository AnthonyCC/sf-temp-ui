package com.freshdirect.fdstore.content;

import java.util.Date;

public class CMSPageRequest {
	private String pageType;
	private Date requestedDate;
	private boolean ignoreSchedule;
	
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
}
