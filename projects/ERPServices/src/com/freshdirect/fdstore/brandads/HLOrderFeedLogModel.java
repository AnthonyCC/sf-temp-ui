package com.freshdirect.fdstore.brandads;

import java.io.Serializable;
import java.util.Date;

public class HLOrderFeedLogModel implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3300947164308030169L;
	
	private String id;
	private Date startTime;
	private Date endTime;
	private Date lastSentOrderTime;
	private String details;
	
		
	public String getId() {
		return id;
	}
	public Date getStartTime() {
		return startTime;
	}
	public Date getEndTime() {
		return endTime;
	}
	public Date getLastSentOrderTime() {
		return lastSentOrderTime;
	}
	public String getDetails() {
		return details;
	}
	public void setId(String id) {
		this.id = id;
	}
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	public void setLastSentOrderTime(Date lastSentOrderTime) {
		this.lastSentOrderTime = lastSentOrderTime;
	}
	public void setDetails(String details) {
		this.details = details;
	}
	
	

}
