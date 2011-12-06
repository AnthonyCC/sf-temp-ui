package com.freshdirect.analytics;

import java.util.Date;

public class BounceData {

	private int cnt;
	private String zone;
	private Date cutOff;
	private String type;
	private String time;
	public int getCnt() {
		return cnt;
	}
	public void setCnt(int cnt) {
		this.cnt = cnt;
	}
	public String getZone() {
		return zone;
	}
	public void setZone(String zone) {
		this.zone = zone;
	}
	public Date getCutOff() {
		return cutOff;
	}
	public void setCutOff(Date cutOff) {
		this.cutOff = cutOff;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	
}
