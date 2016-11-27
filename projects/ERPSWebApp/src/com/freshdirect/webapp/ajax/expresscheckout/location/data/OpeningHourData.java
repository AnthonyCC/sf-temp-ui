package com.freshdirect.webapp.ajax.expresscheckout.location.data;


public class OpeningHourData {

	private String day;
	private String open;

	public OpeningHourData(String day, String open) {
		this.day = day;
		this.open = open;
	}

	public String getDay() {
		return day;
	}

	public void setDay(String day) {
		this.day = day;
	}

	public String getOpen() {
		return open;
	}

	public void setOpen(String open) {
		this.open = open;
	}
}
