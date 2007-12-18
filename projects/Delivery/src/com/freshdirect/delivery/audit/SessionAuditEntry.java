package com.freshdirect.delivery.audit;

import java.io.Serializable;
import java.util.Date;

public class SessionAuditEntry implements Serializable {
	
	private long id;

	private String role;

	private String userId;

	private String sessionId;

	private Date startDate;

	private Date endDate;
	
	private Date lastInteraction;

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date end) {
		this.endDate = end;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date start) {
		this.startDate = start;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public Date getLastInteraction() {
		return lastInteraction;
	}

	public void setLastInteraction(Date lastInteraction) {
		this.lastInteraction = lastInteraction;
	}
	
}
