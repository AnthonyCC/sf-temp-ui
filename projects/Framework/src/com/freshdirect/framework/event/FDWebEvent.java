package com.freshdirect.framework.event;

import java.util.Date;

public class FDWebEvent extends FDEvent {
	private static final long serialVersionUID = 3284289023903633757L;
	
	private String customerId;
	private String cookie;
	private String url;
	private String queryString;
	private String eventType;
	private Date timestamp;
	private String application;
	private String server;
	private String trackingCode;   // trk
	private String trackingCodeEx; // trkd
	private EnumEventSource source;
	protected String [] eventValues = new String [15];
	
	public EnumEventSource getSource() {
		return source;
	}

	public void setSource(EnumEventSource source) {
		this.source = source;
	}

	public String getCustomerId() {
		return customerId;
	}
	
	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}
	
	public String getCookie() {
		return this.cookie;
	}
	
	public void setCookie(String cookie) {
		this.cookie = cookie;
	}
	
	public String getUrl() {
		return url;
	}
	
	public void setUrl(String url) {
		this.url = url;
	}
	
	public String getQueryString () {
		return this.queryString;
	}
	
	public void setQueryString (String queryString) {
		this.queryString = queryString;
	}
	
	public String getEventType() {
		return eventType;
	}
	
	public void setEventType(String eventType) {
		this.eventType = eventType;
	}
	
	public Date getTimestamp() {
		return timestamp;
	}
	
	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}
	
	public String getApplication() {
		return application;
	}
	
	public void setApplication(String application) {
		this.application = application;
	}
	
	public String getServer() {
		return server;
	}
	
	public void setServer(String server) {
		this.server = server;
	}
	
	public String getTrackingCode() {
		return trackingCode;
	}
	
	public void setTrackingCode(String trackingCode) {
		this.trackingCode = trackingCode;
	}
	
	public String getTrackingCodeEx() {
		return this.trackingCodeEx;
	}
	
	public void setTrackingCodeEx(String trackingCodeEx) {
		this.trackingCodeEx = trackingCodeEx;
	}
	
	public String[] getEventValues() {
		return eventValues;
	}
	
	public String toString() {
		
		StringBuffer bf = new StringBuffer();
		bf.append("[FDEvent: CustomerId=").append(this.customerId);
		bf.append(";cookie=").append(this.cookie);
		bf.append(";url=").append(this.url);
		bf.append(";queryString=").append(this.queryString);
		bf.append(";eventType=").append(this.eventType);
		bf.append(";timestamp=").append(this.timestamp);
		bf.append(";application=").append(this.application);
		bf.append(";server=").append(this.server);
		bf.append(";trackingCode=").append(this.trackingCode);
		bf.append(";trackingCodeEx=").append(this.trackingCodeEx);
		bf.append(";source=").append(this.source);
		bf.append(";eStoreId=").append(this.eStoreId);
		bf.append("\n");

		for(int i = 0; i < this.eventValues.length; i++){
			bf.append(";Param-").append(i+1).append("=").append(this.eventValues[i]);
		}
		
		bf.append("]");
		
		return  bf.toString();
	}
}
