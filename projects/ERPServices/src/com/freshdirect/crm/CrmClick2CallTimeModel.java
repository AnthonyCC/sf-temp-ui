package com.freshdirect.crm;

import com.freshdirect.framework.core.ModelSupport;

public class CrmClick2CallTimeModel extends ModelSupport {
	
	public static final String[] TIMINGS={"Midnight","1 am","2 am","3 am","4 am","5 am","6 am","7 am","8 am","9 am","10 am","11 am","Noon", "1 pm",
		"2 pm","3 pm","4 pm","5 pm","6 pm","7 pm","8 pm","9 pm","10 pm","11 pm"};
	
	private String dayName;
	private String startTime;
	private String endTime;
	private boolean show;
	private String click2CallId;
	
	public CrmClick2CallTimeModel(String dayName, String startTime,
			String endTime, boolean show, String click2CallId) {
		super();
		this.dayName = dayName;
		this.startTime = startTime;
		this.endTime = endTime;
		this.show = show;
		this.click2CallId = click2CallId;
	}
	
	public CrmClick2CallTimeModel() {
		super();	
	}
	
	public String getDayName() {
		return dayName;
	}
	public void setDayName(String dayName) {
		this.dayName = dayName;
	}
	public String getStartTime() {
		return startTime;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	public String getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	public boolean isShow() {
		return show;
	}
	public void setShow(boolean show) {
		this.show = show;
	}
	public String getClick2CallId() {
		return click2CallId;
	}
	public void setClick2CallId(String click2CallId) {
		this.click2CallId = click2CallId;
	}
	
	

}
