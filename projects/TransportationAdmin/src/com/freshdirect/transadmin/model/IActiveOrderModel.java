package com.freshdirect.transadmin.model;

import java.util.Date;

public interface IActiveOrderModel {
	
	public String getArea();
	public void setArea(String area);
	
	public Date getEndTime();
	public void setEndTime(Date endTime);
	
	public Date getStartTime();
	public void setStartTime(Date startTime);
	
	public int getOrderCount();
	public void setOrderCount(int orderCount);
}
