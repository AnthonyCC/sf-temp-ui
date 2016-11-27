package com.freshdirect.analytics.model;


public class OrderData {

	private float orderCount;
	private String cutoff;
	private String zone;
	private String cutoffTimeFormatted;
	public String getCutoffTimeFormatted() {
		return cutoffTimeFormatted;
	}
	public void setCutoffTimeFormatted(String cutoffTimeFormatted) {
		this.cutoffTimeFormatted = cutoffTimeFormatted;
	}
	
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
