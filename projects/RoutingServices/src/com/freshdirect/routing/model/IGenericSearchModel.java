package com.freshdirect.routing.model;

import java.util.Date;

public interface IGenericSearchModel {

	Date getSourceDate();
	void setSourceDate(Date sourceDate);
	String[] getArea();
	void setArea(String[] area);
	Date getCutOffDate();
	void setCutOffDate(Date cutOffDate);
	Date getStartTime();
	void setStartTime(Date startTime);
	Date getEndTime();
	void setEndTime(Date endTime);
}
