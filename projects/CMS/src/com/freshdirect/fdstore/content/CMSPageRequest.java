package com.freshdirect.fdstore.content;

import java.util.Date;

public class CMSPageRequest {
	private String pageType;
	private Date requestedDate;
	
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
}
