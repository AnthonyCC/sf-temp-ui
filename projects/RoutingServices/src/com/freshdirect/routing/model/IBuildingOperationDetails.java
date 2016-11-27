package com.freshdirect.routing.model;

import java.util.Date;

public interface IBuildingOperationDetails {
	
	String getDayOfWeek();
	Date getBldgStartHour();
	Date getBldgEndHour();
	Date getServiceStartHour();
	Date getServiceEndHour();
	String getBldgComments();
	String getServiceComments();
	void setDayOfWeek(String dayOfWeek);
	void setBldgStartHour(Date bldgStartHour);
	void setBldgEndHour(Date bldgEndHour);
	void setServiceStartHour(Date serviceStartHour);
	void setServiceEndHour(Date serviceEndHour);
	void setBldgComments(String bldgComments);
	void setServiceComments(String serviceComments);
	
}
