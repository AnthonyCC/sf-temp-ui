package com.freshdirect.transadmin.model;

import java.util.Date;

import com.freshdirect.routing.model.BaseModel;

public class ActiveOrderModel extends BaseModel implements IActiveOrderModel{

	private static final long serialVersionUID = -8210964574737542607L;
	
	private String area;
	private Date startTime;
	private Date endTime;
	private int orderCount;
	
	public String getArea() {
		return area;
	}
	public void setArea(String area) {
		this.area = area;
	}
	public Date getEndTime() {
		return endTime;
	}
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	public Date getStartTime() {
		return startTime;
	}
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}
	public int getOrderCount() {
		return orderCount;
	}
	public void setOrderCount(int orderCount) {
		this.orderCount = orderCount;
	}	 
}
