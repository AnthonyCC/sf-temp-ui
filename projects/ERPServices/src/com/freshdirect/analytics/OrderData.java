package com.freshdirect.analytics;

import java.util.Date;

public class OrderData {

	private float orderCount;
	private String cutoff;
	private String zone;
	
	public float getOrderCount() {
		return orderCount;
	}
	public void setOrderCount(float orderCount) {
		this.orderCount = orderCount;
	}
	
	public String getCutoff() {
		return cutoff;
	}
	public void setCutoff(String cutoff) {
		this.cutoff = cutoff;
	}
	public String getZone() {
		return zone;
	}
	public void setZone(String zone) {
		this.zone = zone;
	}
	
}
