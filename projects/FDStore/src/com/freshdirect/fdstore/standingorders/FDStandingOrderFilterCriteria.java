package com.freshdirect.fdstore.standingorders;

public class FDStandingOrderFilterCriteria {

	private Integer frequency;
	private String errorType;
	private Integer dayOfWeek;
	private boolean activeOnly = true;
	
	
	public FDStandingOrderFilterCriteria() {
	
	}

	public FDStandingOrderFilterCriteria(Integer frequency, String errorType,
			Integer dayOfWeek) {	
		this.frequency = frequency;
		this.errorType = errorType;
		this.dayOfWeek = dayOfWeek;
	}

	public Integer getFrequency() {
		return frequency;
	}


	public void setFrequency(Integer frequency) {
		this.frequency = frequency;
	}


	public String getErrorType() {
		return errorType;
	}


	public void setErrorType(String errorType) {
		this.errorType = errorType;
	}


	public Integer getDayOfWeek() {
		return dayOfWeek;
	}


	public void setDayOfWeek(Integer dayOfWeek) {
		this.dayOfWeek = dayOfWeek;
	}
	
	public boolean isEmpty(){
		boolean isEmtpy = true;
		if((null != frequency) 
				|| (null != errorType && !errorType.trim().equalsIgnoreCase(""))
				|| (null != dayOfWeek )||!activeOnly)
				{
			isEmtpy = false;
		}
		return isEmtpy;
	}

	public boolean isActiveOnly() {
		return activeOnly;
	}

	public void setActiveOnly(boolean activeOnly) {
		this.activeOnly = activeOnly;
	}
	
}
