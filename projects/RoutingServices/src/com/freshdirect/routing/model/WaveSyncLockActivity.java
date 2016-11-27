package com.freshdirect.routing.model;

import java.util.Date;

@SuppressWarnings("serial")
public class WaveSyncLockActivity implements java.io.Serializable
{	
	private String lockId;
	private String initiator;
	private Date lockDateTime;
	private String unLockedBy;
	private Date releaselockDateTime;
	
	
	public WaveSyncLockActivity() {
		super();
		// TODO Auto-generated constructor stub
	}
	public String getLockId() {
		return lockId;
	}
	public void setLockId(String lockId) {
		this.lockId = lockId;
	}
	public String getInitiator() {
		return initiator;
	}
	public void setInitiator(String initiator) {
		this.initiator = initiator;
	}
	public Date getLockDateTime() {
		return lockDateTime;
	}
	public void setLockDateTime(Date lockDateTime) {
		this.lockDateTime = lockDateTime;
	}
	public String getUnLockedBy() {
		return unLockedBy;
	}
	public void setUnLockedBy(String unLockedBy) {
		this.unLockedBy = unLockedBy;
	}
	public Date getReleaselockDateTime() {
		return releaselockDateTime;
	}
	public void setReleaselockDateTime(Date releaselockDateTime) {
		this.releaselockDateTime = releaselockDateTime;
	}
}
