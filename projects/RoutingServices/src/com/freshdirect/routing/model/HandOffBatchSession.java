package com.freshdirect.routing.model;


public class HandOffBatchSession extends BaseModel implements IHandOffBatchSession, Comparable<HandOffBatchSession>  {
	
	private String batchId;
	private String sessionName;
	private String region;
		
	public HandOffBatchSession(String batchId, String sessionName, String region) {
		super();
		this.batchId = batchId;
		this.sessionName = sessionName;
		this.region = region;
	}
	
	public HandOffBatchSession() {
		super();
	}
	
	public String getBatchId() {
		return batchId;
	}
	public void setBatchId(String batchId) {
		this.batchId = batchId;
	}
	
	public String getSessionName() {
		return sessionName;
	}
	public void setSessionName(String sessionName) {
		this.sessionName = sessionName;
	}
	
	public String getRegion() {
		return region;
	}
	public void setRegion(String region) {
		this.region = region;
	}
	
	public int hashCode() {
		return
			(batchId==null || region==null || sessionName==null) ?
			super.hashCode() :
				batchId.hashCode() ^ region.hashCode() ^ sessionName.hashCode();
	}
	
	
	@Override
	public boolean equals(Object obj) {
		
		HandOffBatchSession other = (HandOffBatchSession) obj;
		if (batchId == null) {
			if (other.batchId != null)
				return false;
		} else if (!batchId.equals(other.batchId))
			return false;
		if (region == null) {
			if (other.region != null)
				return false;
		} else if (!region.equals(other.region))
			return false;
		if (sessionName == null) {
			if (other.sessionName != null)
				return false;
		} else if (!sessionName.equals(other.sessionName))
			return false;
		return true;
	}
	
	@Override
	public int compareTo(HandOffBatchSession o) {
		// TODO Auto-generated method stub
		if(this.equals(o)) {			
			return 0;
		} else {			
			return this.toString().compareTo(o.toString());
		}
	}
	@Override
	public String toString() {
		return "HandOffBatchSession [batchId=" + batchId  + ", region=" + region + ", sessionName="
				+ sessionName + "]";
	}
	@Override
	public boolean isDepot() {
		// TODO Auto-generated method stub
		return getRegion() != null && getRegion().equalsIgnoreCase("MDP");
	}
	
	
}
