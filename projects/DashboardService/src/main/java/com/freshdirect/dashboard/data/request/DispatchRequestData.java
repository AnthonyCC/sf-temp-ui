package com.freshdirect.dashboard.data.request;


public class DispatchRequestData {

	private String requestedDate;	

	private String previousDate1;	

	private String previousDate2;
	
	private String zone;
	
	private String shift;

	public String getRequestedDate() {
		return requestedDate;
	}

	public void setRequestedDate(String requestedDate) {
		this.requestedDate = requestedDate;
	}

	public String getPreviousDate1() {
		return previousDate1;
	}

	public void setPreviousDate1(String previousDate1) {
		this.previousDate1 = previousDate1;
	}

	public String getPreviousDate2() {
		return previousDate2;
	}

	public void setPreviousDate2(String previousDate2) {
		this.previousDate2 = previousDate2;
	}

	public String getZone() {
		return zone;
	}

	public void setZone(String zone) {
		this.zone = zone;
	}
	
}
